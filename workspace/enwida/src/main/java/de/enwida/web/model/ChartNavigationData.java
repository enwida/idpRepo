package de.enwida.web.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.enwida.transport.DataResolution;
import de.enwida.web.model.ProductTree.ProductAttributes;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.NavigationDefaults;
import de.enwida.web.utils.TSO;


public class ChartNavigationData {

	private String chartTitle;
	private NavigationDefaults defaults;
	private NavigationDataStructure navigationDS;
	private ProductTree productTree;
	private List<TSO> tsos;
	private String xAxisLabel;
	private String yAxisLabel;

	public ChartNavigationData() {
	}

	public ChartNavigationData(String chartTitle, String xAxisLabel, String yAxisLabel) {
		this(chartTitle, xAxisLabel, yAxisLabel, new ArrayList<TSO>(), new ProductTree(), null);
	}

	public ChartNavigationData(String chartTitle, String xAxisLabel, String yAxisLabel, List<TSO> tsos,
	       ProductTree productTree, NavigationDefaults defaults) {
		this.chartTitle = chartTitle;
		this.xAxisLabel = xAxisLabel;
		this.yAxisLabel = yAxisLabel;
		this.tsos = tsos;
		this.productTree = productTree;
		this.defaults = defaults;
	}

	public void addTso(TSO tso) {
		this.tsos.add(tso);
	}

	public String getChartTitle() {
		return this.chartTitle;
	}

	public NavigationDefaults getDefaults() {
		return this.defaults;
	}

	public NavigationDataStructure getNavigationDS() {
		return this.navigationDS;
	}

	public ProductTree getProductTree() {
		return this.productTree;
	}
	
	public void setProductTree(ProductTree productTree) {
	    this.productTree = productTree;
	}

	public List<DataResolution> getAllResolutions() {
	    final List<DataResolution> result = new ArrayList<DataResolution>();

	    for (final ProductAttributes product : productTree.flatten()) {
	        result.addAll(product.resolutions);
	    }
	    return result;
	}

	public CalendarRange getTimeRangeMax() {
	    final List<ProductAttributes> products = productTree.flatten();
	    Calendar from = null;
	    Calendar to = null;
	    
	    for (final ProductAttributes product : products) {
	        if (from == null || product.timeRange.getFrom().compareTo(from) < 0) {
	            from = product.timeRange.getFrom();
	        }
	        if (to == null || product.timeRange.getTo().compareTo(to) > 0) {
	            to = product.timeRange.getTo();
	        }
	    }
	    return new CalendarRange(from, to);
	}

	public CalendarRange getTimeRangeMin() {
	    final List<ProductAttributes> products = productTree.flatten();
	    Calendar from = null;
	    Calendar to = null;
	    
	    for (final ProductAttributes product : products) {
	        if (from == null || product.timeRange.getFrom().compareTo(from) > 0) {
	            from = product.timeRange.getFrom();
	        }
	        if (to == null || product.timeRange.getTo().compareTo(to) < 0) {
	            to = product.timeRange.getTo();
	        }
	    }
	    return new CalendarRange(from, to);
	}

	public String getTitle() {
		return this.chartTitle;
	}

	public List<TSO> getTsos() {
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

	public void setNavigationDS(NavigationDataStructure navigationDS) {
		this.navigationDS = navigationDS;
	}

	public void setTitle(String title) {
		this.chartTitle = title;
	}

	public void setTsos(List<TSO> tsos) {
		this.tsos = tsos;
	}

	public void setxAxisLabel(String xAxisLabel) {
		this.xAxisLabel = xAxisLabel;
	}

	public void setyAxisLabel(String yAxisLabel) {
		this.yAxisLabel = yAxisLabel;
	}

}
