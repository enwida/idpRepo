/**
 * 
 */
package de.enwida.web.service.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.db.model.NavigationSettings;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;
import de.enwida.web.model.ProductTree.ProductAttributes;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IAvailibilityService;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.service.interfaces.ISecurityService;
import de.enwida.web.utils.ChartNavigationLocalizer;
import de.enwida.web.utils.EnwidaUtils;
import de.enwida.web.utils.ObjectMapperFactory;
import de.enwida.web.utils.ProductLeaf;
import de.enwida.web.utils.ProductRestriction;

@TransactionConfiguration(defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
@Service("navigationService")
public class NavigationServiceImpl implements INavigationService {

    private static Logger logger = Logger.getLogger(NavigationServiceImpl.class);

	@Autowired
	private ISecurityService securityService;
	
	@Autowired
	private IAvailibilityService availibilityService;

	@Autowired
	private INavigationDao navigationDao;

	@Autowired
	private ObjectMapperFactory objectMapperFactory;
	
	@Autowired
	private ChartNavigationLocalizer navigationLocalizer;

	@Value("#{applicationProperties['navigation.json.dir']}")
	protected String jsonDir;

	private ObjectMapper objectMapper;
	private Hashtable<Integer, ChartNavigationData> defaultNavigationData =  new Hashtable<Integer, ChartNavigationData>();
	
	@PostConstruct
	public void init() throws IOException {
		objectMapper = objectMapperFactory.create();
		readJsonNavigationFiles();
		// System.out.println(jsonDir);
	}

	@Override
	public Hashtable<Integer, ChartNavigationData> getAllDefaultNavigationData() {
		// Clone every stored NavigationData instance
		final Hashtable<Integer, ChartNavigationData> result = new Hashtable<Integer, ChartNavigationData>();
		
		for (final int key : defaultNavigationData.keySet()) {
			result.put(key, defaultNavigationData.get(key).clone());
		}
		return result;
	}

	public ChartNavigationData getDefaultNavigationData(int chartId) {
		final ChartNavigationData result = defaultNavigationData.get(chartId);
		if (result == null) {
			return null;
		}
	    return defaultNavigationData.get(chartId).clone();
	}

    public ChartNavigationData getNavigationData(int chartId, User user, Locale locale) throws Exception {
	    // Get basic navigation data from hash table and apply
	    // internationalized properties
        final ChartNavigationData navigationData = getDefaultNavigationData(chartId);
        
        // Add user uploaded data
        addUploadedLines(navigationData, user);
        
        // Fetch the related aspects and shrink the navigation data
        // under security and availability perspective
        shrinkNavigationOnAvailibility(navigationData, user);
        shrinkNavigationOnSecurity(navigationData, user);
        
        // Localize Strings
        return navigationLocalizer.localize(navigationData, chartId, locale);
    }
    
    /**
	 * Only for testing purposes! Skips availability service.
	 * 
	 * @throws Exception
	 */
	@Override
	public ChartNavigationData getNavigationDataWithoutAvailablityCheck(
			int chartId, User user, Locale locale) throws Exception {
        final ChartNavigationData navigationData = getDefaultNavigationData(chartId);
        shrinkNavigationOnSecurity(navigationData, user);
        return navigationLocalizer.localize(navigationData, chartId, locale);
    }
	
	@Override
	public ChartNavigationData getNavigationDataFromJsonFile(int chartId) throws IOException {
		final InputStream in = new FileInputStream(new File(jsonDir, chartId + ".json"));
		return objectMapper.readValue(in, ChartNavigationData.class);
	}
	
	private void addUploadedLines(ChartNavigationData navigationData, User user) {
		final ProductTree tree = new ProductTree(0);
		for (final UploadedFile file : user.getUploadedFiles()) {
			try {
				// Only consider active uploads
				if (!file.isActive()) {
					continue;
				}
				// Filter by aspect
				final Aspect aspect = Aspect.valueOf(file.getMetaData().getAspect());
				if (!navigationData.getAspects().contains(aspect)) {
					continue;
				}
				final DataResolution resolution = DataResolution.valueOf(file.getMetaData().getResolution().toUpperCase());
	
				// Add file as a leaf to the product tree with TSO 0
				final ProductLeaf leaf = new ProductLeaf((int) file.getUploadedFileId().getId(),
						                                 file.getMetaData().getName(),
														 Collections.singletonList(resolution),
						                                 CalendarRange.always()
						                                );
				tree.getRoot().addChild(leaf);
			} catch (Exception e) {
				logger.error("Error while adding uploaded lines: " + e.getLocalizedMessage());
			}
		}
		if (tree.getRoot().getChildren().size() > 0) {
			navigationData.addProductTree(tree);
		}
	}
	
    private interface IProductRestrictionGetter {
        public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect) throws Exception;
    }
    
    private void shrinkNavigation(ChartNavigationData navigationData, IProductRestrictionGetter service) throws Exception {
        for (final ProductTree productTree : navigationData.getProductTrees()) {
            final List<ProductAttributes> products = productTree.flatten();
            
            for (final ProductAttributes productAttrs : products) {
                final List<ProductRestriction> restrictions = new ArrayList<ProductRestriction>();
                for (final Aspect aspect : navigationData.getAspects()) {
                    restrictions.add(service.getProductRestriction(productAttrs.productId, productTree.getTso(), aspect));
                }
                final ProductRestriction combinedRestriction = ProductRestriction.combineMaximum(restrictions);
    
                if (combinedRestriction == null) {
                    // Assume nothing is allowed (i.e. fully restricted)
                    // Delete the corresponding product leaf from the tree
                    productTree.removeProduct(productAttrs.productId);
                } else {
                    // Apply restrictions to the tree
                    final ProductLeaf leaf = productTree.getLeaf(productAttrs.productId);
                    
                    // Restrict time range
                    final List<CalendarRange> timeRanges = Arrays.asList(new CalendarRange[] { leaf.getTimeRange(), combinedRestriction.getTimeRange() });
                    leaf.setTimeRange(CalendarRange.getMinimum(timeRanges));
                    
                    // Restrict resolutions
                    final Iterator<DataResolution> iter = leaf.getResolution().iterator();
                    while (iter.hasNext()) {
                    	if (!combinedRestriction.getResolutions().contains(iter.next())) {
                    		iter.remove();
                    	}
                    }
                }
            }
        }
    }
    
    private void shrinkNavigationOnSecurity(ChartNavigationData navigationData, final User user) throws Exception {
        shrinkNavigation(navigationData, new IProductRestrictionGetter() {
            
            public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect) throws Exception {
                return securityService.getProductRestriction(productId, tso, aspect, user);
            }
        });
    }

    private void shrinkNavigationOnAvailibility(ChartNavigationData navigationData, final User user) throws Exception {
        shrinkNavigation(navigationData, new IProductRestrictionGetter() {
            
            public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect) {
                return availibilityService.getProductRestriction(productId, tso, aspect, user);
            }
        });
    }
    
	private void readJsonNavigationFiles() {
		jsonDir = EnwidaUtils.resolveEnvVars(jsonDir);
		final Pattern fileNamePattern = Pattern.compile("^(\\d+)\\.json$");
		final File dir = new File(jsonDir);
		
		// Create the containing directory if it does not exist
		if (!dir.exists()) {
			dir.mkdir();
			
			// There won't be any files in the newly created directory
			return;
		}
		
		for (final File file : dir.listFiles()) {
			if (file.isDirectory()) {
				continue;
			}
			final Matcher match = fileNamePattern.matcher(file.getName());
			if (match.matches()) {
				final int chartId = Integer.parseInt(match.group(1));
				try {
				    final ChartNavigationData navigationData = getNavigationDataFromJsonFile(chartId);
					defaultNavigationData.put(chartId, navigationData);
				} catch (IOException e) {
					logger.error("Error while reading navigation JSON (" + file.getName() + "): " + e.getMessage());
				}
			}
		}
	}

	@Override
	public Set<NavigationSettings> getNavigationSettingsByUserId(int userId) throws IOException {
		return navigationDao.getNavigationSettingsByUserID(userId);
	}

	@Override
	public Set<NavigationSettings> getNavigationSettingsByClientId(String clientId) throws IOException {
		return navigationDao.getNavigationSettingsByClientID(clientId);
	}

	@Override
	public void saveUserNavigationSettings(NavigationSettings navigationSettings) {
		navigationDao.saveUserNavigationSettings(navigationSettings);
	}

}
