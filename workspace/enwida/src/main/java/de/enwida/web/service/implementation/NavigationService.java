/**
 * 
 */
package de.enwida.web.service.implementation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;

import de.enwida.transport.Aspect;
import de.enwida.web.dao.interfaces.IAspectsDao;
import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;
import de.enwida.web.model.ProductTree.ProductAttributes;
import de.enwida.web.service.interfaces.IAvailibilityService;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.service.interfaces.ISecurityService;
import de.enwida.web.utils.EnwidaUtils;
import de.enwida.web.utils.ProductLeaf;
import de.enwida.web.utils.ProductRestriction;

@Service("NavigationService")
@Transactional
public class NavigationService implements INavigationService {

	@Autowired
	private INavigationDao navigationDao;
	
	@Autowired
	private ISecurityService securityService;
	
	@Autowired
	private IAvailibilityService availibilityService;
	
	@Autowired
	private IAspectsDao aspectsDao;
	
	private Hashtable<Integer, ChartNavigationData> defaultNavigationData =  new Hashtable<Integer, ChartNavigationData>();
	
	@PostConstruct
	public void init() throws IOException {
	     for (int i = 1; i <= 1; i++) {
	    	 defaultNavigationData.put(i, getNavigationDataFromJsonFile(i));
		}
	}

    public ChartNavigationData getNavigationData(int chartId, int role, Locale locale) {
        final ChartNavigationData navigationData = navigationDao.getDefaultNavigation(chartId, locale);
        final List<Aspect> aspects = aspectsDao.getAspects(chartId);
        
        shrinkNavigationOnSecurity(navigationData, aspects, role);
        shrinkNavigationOnAvailibility(navigationData, aspects, role);
        addDefaults(navigationData);
        
        return navigationData;
    }
    
    private interface IProductRestrictionGetter {
        public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect);
    }
    
    private void shrinkNavigation(ChartNavigationData navigationData, List<Aspect> aspects, IProductRestrictionGetter service) {
        for (final ProductTree productTree : navigationData.getProductTrees()) {
            final List<ProductAttributes> products = productTree.flatten();
            
            for (final ProductAttributes productAttrs : products) {
                final List<ProductRestriction> restrictions = new ArrayList<ProductRestriction>();
                for (final Aspect aspect : aspects) {
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
    
    private void shrinkNavigationOnSecurity(ChartNavigationData navigationData, List<Aspect> aspects, final int role) {
        shrinkNavigation(navigationData, aspects, new IProductRestrictionGetter() {
            
            public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect) {
                return securityService.getProductRestriction(productId, tso, aspect, role);
            }
        });
    }

    private void shrinkNavigationOnAvailibility(ChartNavigationData navigationData, List<Aspect> aspects, final int role) {
        shrinkNavigation(navigationData, aspects, new IProductRestrictionGetter() {
            
            public ProductRestriction getProductRestriction(int productId, int tso, Aspect aspect) {
                return availibilityService.getProductRestriction(productId, tso, aspect, role);
            }
        });
    }

    private void addDefaults(ChartNavigationData navigationData) {
        // TODO: stub
    }
    
    @Override
	public void putNavigationDataToJsonFile(int chartId, ChartNavigationData chartNavigationData) throws IOException {
		String json = JsonWriter.objectToJson(chartNavigationData);
		System.out.println(json);
		//TODO: Complete the Implementation
	}

	@Override
	public ChartNavigationData getNavigationDataFromJsonFile(int chartId) throws IOException {
		
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream( chartId + ".json");
		String json = EnwidaUtils.getStringFromInputStream(in);
		
		JsonReader jr = new JsonReader(new ByteArrayInputStream(json.getBytes()));		
		ChartNavigationData chartNavigationDataDeSerialized = (ChartNavigationData) jr.readObject();
		jr.close();	
        
        return chartNavigationDataDeSerialized;
	}
}
