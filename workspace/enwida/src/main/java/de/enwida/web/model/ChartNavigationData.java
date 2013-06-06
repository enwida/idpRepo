package de.enwida.web.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.enwida.transport.DataResolution;
import de.enwida.web.model.ProductTree.ProductAttributes;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.NavigationDefaults;
import de.enwida.web.utils.TSO;


public class ChartNavigationData {

	private String chartTitle;
	private NavigationDefaults defaults;
	private List<ProductTree> productTrees;
	private Map<Integer, String> tsos;
	private String xAxisLabel;
	private String yAxisLabel;

	public ChartNavigationData() {
	}

	public ChartNavigationData(String chartTitle, String xAxisLabel, String yAxisLabel) {
		this(chartTitle, xAxisLabel, yAxisLabel, new ArrayList<ProductTree>(), null);
	}

	public ChartNavigationData(String chartTitle, String xAxisLabel, String yAxisLabel,
	       List<ProductTree> productTrees, NavigationDefaults defaults) {
		this.chartTitle = chartTitle;
		this.xAxisLabel = xAxisLabel;
		this.yAxisLabel = yAxisLabel;
		this.tsos = new HashMap<Integer, String>();
		this.productTrees = productTrees;
		this.defaults = defaults;
	}

	public void addTso(int id, String name) {
		this.tsos.put(id, name);
	}

	public String getChartTitle() {
		return this.chartTitle;
	}

	public NavigationDefaults getDefaults() {
		return this.defaults;
	}

	public List<ProductTree> getProductTrees() {
		return this.productTrees;
	}
	
	public void addProductTree(ProductTree productTree) {
	    this.productTrees.add(productTree);
	}

	public List<DataResolution> getAllResolutions() {
	    final List<DataResolution> result = new ArrayList<DataResolution>();

	    for (final ProductTree productTree : productTrees) {
    	    for (final ProductAttributes product : productTree.flatten()) {
    	        result.addAll(product.resolutions);
    	    }
	    }
	    return result;
	}

	public CalendarRange getTimeRangeMax() {
	    final List<CalendarRange> ranges = new ArrayList<CalendarRange>();

	    for (final ProductTree productTree : productTrees) {
    	    final List<ProductAttributes> products = productTree.flatten();
    	    
    	    for (final ProductAttributes product : products) {
    	        ranges.add(product.timeRange);
    	    }
	    }
	    return CalendarRange.getMaximum(ranges);
	}

	public CalendarRange getTimeRangeMin() {
	    final List<CalendarRange> ranges = new ArrayList<CalendarRange>();

	    for (final ProductTree productTree : productTrees) {
    	    final List<ProductAttributes> products = productTree.flatten();
    	    
    	    for (final ProductAttributes product : products) {
    	        ranges.add(product.timeRange);
    	    }
	    }
	    return CalendarRange.getMinimum(ranges);
	}

	public String getTitle() {
		return this.chartTitle;
	}

	public Map<Integer, String> getTsos() {
		return this.tsos;
	}

	public String getxAxisLabel() {
		return this.xAxisLabel;
	}

	public String getyAxisLabel() {
		return this.yAxisLabel;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public void setDefaults(NavigationDefaults defaults) {
		this.defaults = defaults;
	}

	public void setTitle(String title) {
		this.chartTitle = title;
	}

	public void setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}

	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}

}
