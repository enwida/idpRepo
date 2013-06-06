/**
 * 
 */
package de.enwida.web.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.transport.Aspect;
import de.enwida.web.dao.interfaces.IAspectsDao;
import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;
import de.enwida.web.model.ProductTree.ProductAttributes;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IAvailibilityService;
import de.enwida.web.service.interfaces.ISecurityService;
import de.enwida.web.service.interfaces.NavigationService;
import de.enwida.web.utils.ProductLeaf;
import de.enwida.web.utils.ProductRestriction;

@Service("NavigationService")
@Transactional
public class NavigationServiceImpl implements NavigationService {

	@Autowired
	private INavigationDao navigationDao;
	
	@Autowired
	private ISecurityService securityService;
	
	@Autowired
	private IAvailibilityService availibilityService;
	
	@Autowired
	private IAspectsDao aspectsDao;

    public ChartNavigationData getNavigationData(int chartId, User user, Locale locale) {
        final ChartNavigationData navigationData = navigationDao.getDefaultNavigation(chartId, locale);
        final List<Aspect> aspects = aspectsDao.getAspects(chartId);
        
        shrinkNavigationOnSecurity(navigationData, aspects);
        shrinkNavigationOnAvailibility(navigationData, aspects);
        addDefaults(navigationData);
        
        return navigationData;
    }
    
    private interface IProductRestrictionGetter {
        public ProductRestriction getProductRestriction(int productId, Aspect aspect);
    }
    
    private void shrinkNavigation(ChartNavigationData navigationData, List<Aspect> aspects, IProductRestrictionGetter service) {
        for (final ProductTree productTree : navigationData.getProductTrees()) {
            final List<ProductAttributes> products = productTree.flatten();
            
            for (final ProductAttributes productAttrs : products) {
                final List<ProductRestriction> restrictions = new ArrayList<ProductRestriction>();
                for (final Aspect aspect : aspects) {
                    restrictions.add(service.getProductRestriction(productAttrs.productId, aspect));
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
    
    private void shrinkNavigationOnSecurity(ChartNavigationData navigationData, List<Aspect> aspects) {
        shrinkNavigation(navigationData, aspects, new IProductRestrictionGetter() {
            
            public ProductRestriction getProductRestriction(int productId, Aspect aspect) {
                return securityService.getProductRestriction(productId, aspect);
            }
        });
    }

    private void shrinkNavigationOnAvailibility(ChartNavigationData navigationData, List<Aspect> aspects) {
        shrinkNavigation(navigationData, aspects, new IProductRestrictionGetter() {
            
            public ProductRestriction getProductRestriction(int productId, Aspect aspect) {
                return availibilityService.getProductRestriction(productId, aspect);
            }
        });
    }

    private void addDefaults(ChartNavigationData navigationData) {
        // TODO: stub
    }
}
