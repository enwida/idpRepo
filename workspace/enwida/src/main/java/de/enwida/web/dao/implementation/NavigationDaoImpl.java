package de.enwida.web.dao.implementation;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import de.enwida.transport.DataResolution;
import de.enwida.web.dao.interfaces.BaseDao;
import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.User;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.ProductLeaf;
import de.enwida.web.utils.ProductNode;

@Repository
public class NavigationDaoImpl extends BaseDao<User> implements INavigationDao {

	@Autowired
	private MessageSource messageSource;

    public ChartNavigationData getDefaultNavigation(int chartId, Locale locale) {
	    final String chartTitle = getChartMessage("title", chartId, locale);
	    final String xAxisLabel = getChartMessage("xlabel", chartId, locale);
	    final String yAxisLabel = getChartMessage("ylabel", chartId, locale);
	    
	    final ChartNavigationData navigationData = new ChartNavigationData(chartTitle, xAxisLabel, yAxisLabel);
	    fillDefaultProducts(navigationData);
	    
	    return navigationData;
    }
    
    private String getChartMessage(String property, int chartId, Locale locale) {
	    return messageSource.getMessage("de.enwida.chart." + chartId + "." + property, null, "", locale);
    }
    
    // FIXME: Store default navigation in JSON format
    private void fillDefaultProducts(ChartNavigationData navigationData) {
	    final List<DataResolution> allResolutions = Arrays.asList(DataResolution.values());
	    
	    // ProdA (type of RC)
	    final ProductNode prodSCR = new ProductNode(2, "SCR");
	    final ProductNode prodTCR = new ProductNode(3, "TCR");
	    
	    // ProdB (time slot)
	    final ProductNode prodWholeDay = new ProductNode(1, "");

	    final ProductNode prodPT = new ProductNode(1, "PT");
	    final ProductNode prodOPT = new ProductNode(2, "OPT");

	    final ProductNode prod04 = new ProductNode(1, "0-4");
	    final ProductNode prod48 = new ProductNode(2, "4-8");
	    final ProductNode prod812 = new ProductNode(3, "8-12");
	    final ProductNode prod1216 = new ProductNode(4, "12-16");
	    final ProductNode prod1620 = new ProductNode(5, "16-20");
	    final ProductNode prod2024 = new ProductNode(6, "20-24");
	    
	    // ProdC (positive / negative) as leaves
	    final ProductLeaf prodPos = new ProductLeaf(1, "pos", allResolutions, CalendarRange.always());
	    final ProductLeaf prodNeg = new ProductLeaf(2, "neg", allResolutions, CalendarRange.always());
	    
	    // Add pos/neg to every time slot
	    for (final ProductNode timeslot : new ProductNode[]
	        { prodWholeDay, prodOPT, prod04, prod48, prod812, prod1216, prod1620, prod2024 }
	    ) {
	        timeslot.addChild(prodPos);
	        timeslot.addChild(prodNeg);
	    }
	    
	    // Append time slots to RC types
	    prodSCR.addChild(prodPT);
	    prodSCR.addChild(prodOPT);
	    
	    prodTCR.getChildren().addAll(Arrays.asList(new ProductNode[]
	        { prod04, prod48, prod812, prod1216, prod1620, prod2024 }
	    ));
	    
	    // Add root elements (RC types)
	    navigationData.getProductTree().addNode(prodSCR);
	    navigationData.getProductTree().addNode(prodTCR);
    }
}
