/**
 * 
 */
package de.enwida.web.service.implementation;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.chart.DataRequestManager;
import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree.ProductAttributes;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.ISecurityService;
import de.enwida.web.service.interfaces.NavigationService;
import de.enwida.web.utils.ProductLeaf;
import de.enwida.web.utils.ProductRestriction;

@Service("NavigationService")
@Transactional
public class NavigationServiceImpl implements NavigationService {

	@Autowired
	private DataRequestManager dataRequestManager;
	
	@Autowired
	private INavigationDao navigationDao;
	
	@Autowired
	private ISecurityService securityService;

    public ChartNavigationData getNavigationData(int chartId, User user, Locale locale) {
        ChartNavigationData navigationData = navigationDao.getDefaultNavigation(chartId, locale);
        
        shrinkNavigationOnSecurity(navigationData);
        shrinkNavigationOnAvailibility(navigationData);
        addDefaults(navigationData);
        
        return navigationData;
    }
    
    private void shrinkNavigationOnSecurity(ChartNavigationData navigationData) {
        final List<ProductAttributes> products = navigationData.getProductTree().flatten();
        
        for (final ProductAttributes productAttrs : products) {
            // TODO: multiplex over aspects
            final ProductRestriction restriction = securityService.getProductRestriction(productAttrs.productId, null);

            if (restriction == null) {
                // Assume nothing is allowed (i.e. fully restricted)
                // Delete the corresponding product leaf from the tree
                navigationData.getProductTree().removeProduct(productAttrs.productId);
            } else {
                // Apply restrictions to the tree
                final ProductLeaf leaf = navigationData.getProductTree().getLeaf(productAttrs.productId);
                leaf.setTimeRange(restriction.getTimeRange());
                leaf.setResolution(restriction.getResolutions());
            }
        }
    }

    private void shrinkNavigationOnAvailibility(ChartNavigationData navigationData) {
        // TODO: stub
    }

    private void addDefaults(ChartNavigationData navigationData) {
        // TODO: stub
    }
}
