package de.enwida.web.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.enwida.transport.DataResolution;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.NavigationDefaults;
import de.enwida.web.utils.ProductPart;
import de.enwida.web.utils.TSO;


public class ChartNavigationData {

	private String chartTitle;
	private String xAxisLabel;
	private String yAxisLabel;
	private List<TSO> tsos;
	private CalendarRange timeRange;
	private List<DataResolution> resolutions;
	private List<ProductPart> products;
	private NavigationDefaults defaults;
	
	

	public ChartNavigationData(String chartTitle, String xAxisLabel, String yAxisLabel, List<TSO> tsos, CalendarRange timeRange,
                    	       List<DataResolution> resolutions, List<ProductPart> products, NavigationDefaults defaults) {
        this.chartTitle = chartTitle;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.tsos = tsos;
        this.timeRange = timeRange;
        this.resolutions = resolutions;
        this.products = products;
        this.defaults = defaults;
    }
	
	public ChartNavigationData(String chartTitle, String xAxisLabel, String yAxisLabel) {
	    this(chartTitle, xAxisLabel, yAxisLabel, new ArrayList<TSO>(), null, new ArrayList<DataResolution>(), new ArrayList<ProductPart>(), null);
	    
	    // Set the time range to the maximum
	    final Calendar from = Calendar.getInstance();
	    final Calendar to = Calendar.getInstance();
	    from.setTimeInMillis(0);
	    to.setTimeInMillis(Long.MAX_VALUE);
	    this.timeRange = new CalendarRange(from, to);
	}
	
	public void addTso(TSO tso) {
	    tsos.add(tso);
	}
	
	public void addResolution(DataResolution resolution) {
	    resolutions.add(resolution);
	}
	
	public void addProduct(ProductPart product) {
	    products.add(product);
	}

    public String getTitle() {
		return chartTitle;
	}

	public void setTitle(String title) {
		this.chartTitle = title;
	}

    public String getChartTitle() {
        return chartTitle;
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public List<TSO> getTsos() {
        return tsos;
    }

    public void setTsos(List<TSO> tsos) {
        this.tsos = tsos;
    }

    public CalendarRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(CalendarRange timeRange) {
        this.timeRange = timeRange;
    }

    public List<DataResolution> getResolutions() {
        return resolutions;
    }

    public void setResolutions(List<DataResolution> resolutions) {
        this.resolutions = resolutions;
    }

    public List<ProductPart> getProducts() {
        return products;
    }

    public void setProducts(List<ProductPart> products) {
        this.products = products;
    }

    public NavigationDefaults getDefaults() {
        return defaults;
    }

    public void setDefaults(NavigationDefaults defaults) {
        this.defaults = defaults;
    }
	
}
