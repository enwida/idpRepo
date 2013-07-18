/**
 * 
 */
package de.enwida.web.service.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.enwida.transport.Aspect;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;
import de.enwida.web.model.ProductTree.ProductAttributes;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IAvailibilityService;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.service.interfaces.ISecurityService;
import de.enwida.web.utils.ObjectMapperFactory;
import de.enwida.web.utils.ProductLeaf;
import de.enwida.web.utils.ProductRestriction;

public class NavigationServiceImpl implements INavigationService {

    private static Logger logger = Logger.getLogger(NavigationServiceImpl.class);

	@Autowired
	private ISecurityService securityService;
	
	@Autowired
	private IAvailibilityService availibilityService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ObjectMapperFactory objectMapperFactory;
	
	private String jsonDir;
	private ObjectMapper objectMapper;
	private Hashtable<Integer, ChartNavigationData> defaultNavigationData =  new Hashtable<Integer, ChartNavigationData>();
	
	public NavigationServiceImpl(String jsonDir) {
		this.jsonDir = jsonDir;
	}
	
	@PostConstruct
	public void init() throws IOException {
		objectMapper = objectMapperFactory.create();
		readJsonNavigationFiles();
	}
	
	public ChartNavigationData getDefaultNavigationData(int chartId) {
		final ChartNavigationData result = defaultNavigationData.get(chartId);
		if (result == null) {
			return null;
		}
	    return defaultNavigationData.get(chartId).clone();
	}

    public ChartNavigationData getNavigationData(int chartId, User user, Locale locale) {
	    // Get basic navigation data from hash table and apply
	    // internationalized properties
        final ChartNavigationData navigationData = getDefaultNavigationData(chartId);
        
        // Fetch the related aspects and shrink the navigation data
        // under security and availability perspective
        shrinkNavigationOnSecurity(navigationData, user);
        shrinkNavigationOnAvailibility(navigationData, user);
        setLocalAttributes(navigationData, chartId, locale);
        
        return navigationData;
    }
    
    public ChartNavigationData getNavigationDataUNSECURE(int chartId, User user, Locale locale) {
	    // Get basic navigation data from hash table and apply
	    // internationalized properties
        final ChartNavigationData navigationData = defaultNavigationData.get(chartId).clone();
        setLocalAttributes(navigationData, chartId, locale);
        
        return navigationData;
    }

	@Override
	public ChartNavigationData getNavigationDataFromJsonFile(int chartId) throws IOException {
		final InputStream in = new FileInputStream(new File(jsonDir, chartId + ".json"));
		return objectMapper.readValue(in, ChartNavigationData.class);
	}
	
    public String getJsonDir() {
		return jsonDir;
	}

	public void setJsonDir(String jsonDir) {
		this.jsonDir = jsonDir;
	}

	private void setLocalAttributes(ChartNavigationData navigationData, int chartId, Locale locale) {
	    final String chartTitle = getChartMessage("title", chartId, locale);
	    final String xAxisLabel = getChartMessage("xlabel", chartId, locale);
	    final String yAxisLabel = getChartMessage("ylabel", chartId, locale);
	    
        navigationData.setChartTitle(chartTitle);
        navigationData.setxAxisLabel(xAxisLabel);
        navigationData.setyAxisLabel(yAxisLabel);
        
        setTsos(navigationData, locale);
        setTimeRange(navigationData, locale);
    }

    
    private interface IProductRestrictionGetter {
        public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect);
    }
    
    private void shrinkNavigation(ChartNavigationData navigationData, IProductRestrictionGetter service) {
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
                    leaf.setTimeRange(combinedRestriction.getTimeRange());
                    leaf.setResolution(combinedRestriction.getResolutions());
                }
            }
        }
    }
    
    private void shrinkNavigationOnSecurity(ChartNavigationData navigationData, final User user) {
        shrinkNavigation(navigationData, new IProductRestrictionGetter() {
            
            public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect) {
                return securityService.getProductRestriction(productId, tso, aspect, user);
            }
        });
    }

    private void shrinkNavigationOnAvailibility(ChartNavigationData navigationData, final User user) {
        shrinkNavigation(navigationData, new IProductRestrictionGetter() {
            
            public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect) {
                return availibilityService.getProductRestriction(productId, tso, aspect, user);
            }
        });
    }
    
	private void setTsos(ChartNavigationData navigationData, Locale locale) {
	    for (final ProductTree tree : navigationData.getProductTrees()) {
	        final int tso = tree.getTso();
    	    final String tsoName = messageSource.getMessage("de.enwida.chart.tso." + tso + ".name", null, "TSO " + tso, locale);
    	    navigationData.addTso(tso, tsoName);
	    }
	}
	
	private void setTimeRange(ChartNavigationData navigationData, Locale locale) {
	    for (final String key : navigationData.getTimeRanges().keySet()) {
    	    final String timeRangeName = messageSource.getMessage("de.enwida.chart.timerange." + key ,null, key, locale);
    	    navigationData.getTimeRanges().put(key, timeRangeName);
	    }
	}
	
    private String getChartMessage(String property, int chartId, Locale locale) {
	    return messageSource.getMessage("de.enwida.chart." + chartId + "." + property, null, "", locale);
    }
    
	private void readJsonNavigationFiles() {
		final Pattern fileNamePattern = Pattern.compile("^(\\d+)\\.json$");
		final File dir = new File(jsonDir);
		
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

}
