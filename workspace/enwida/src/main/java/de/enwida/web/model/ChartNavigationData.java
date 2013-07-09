package de.enwida.web.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.web.model.ProductTree.ProductAttributes;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.NavigationDefaults;


public class ChartNavigationData implements Cloneable {

	private String chartTitle;
	private NavigationDefaults defaults;
	private List<ProductTree> productTrees;
	private Map<Integer, String> tsos;
	private Map<String, String> timeRanges;
	private String xAxisLabel;
	private String yAxisLabel;
	private List<Aspect> aspects;
	private boolean isDateScale;

	public ChartNavigationData() {
	}

	public ChartNavigationData(String chartTitle, String xAxisLabel, String yAxisLabel) {
		this(chartTitle, xAxisLabel, yAxisLabel, false, new ArrayList<ProductTree>(), null);
	}

	public ChartNavigationData(String chartTitle, String xAxisLabel, String yAxisLabel,
	       boolean dateScale, List<ProductTree> productTrees, NavigationDefaults defaults) {
	    
		this.chartTitle = chartTitle;
		this.xAxisLabel = xAxisLabel;
		this.yAxisLabel = yAxisLabel;
		this.isDateScale = dateScale;
		this.tsos = new HashMap<Integer, String>();
		this.productTrees = productTrees;
		this.defaults = defaults;
		this.aspects = new ArrayList<>();
		this.timeRanges = new HashMap<>();
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
	    final Set<DataResolution> set = new HashSet<DataResolution>();

	    for (final ProductTree productTree : productTrees) {
    	    for (final ProductAttributes product : productTree.flatten()) {
    	        set.addAll(product.resolutions);
    	    }
	    }
	    result.addAll(set);
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
	
	public ChartNavigationData clone() {
	    final ChartNavigationData result = new ChartNavigationData(chartTitle, xAxisLabel, yAxisLabel);
	    result.setIsDateScale(isDateScale);
	    result.setDefaults(defaults.clone());
	    result.tsos = new HashMap<>(tsos);
	    result.aspects = new ArrayList<Aspect>(aspects);
	    result.timeRanges = new HashMap<>(timeRanges);

	    for (final ProductTree tree : productTrees) {
	        result.addProductTree(tree.clone());
	    }
	    return result;
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

    public boolean getIsDateScale() {
        return isDateScale;
    }

    public void setIsDateScale(boolean dateScale) {
        this.isDateScale = dateScale;
    }

    public List<Aspect> getAspects() {
        return aspects;
    }

    public void setAspects(List<Aspect> aspects) {
        this.aspects = aspects;
    }

    public Map<String, String> getTimeRanges() {
        return timeRanges;
    }

}
