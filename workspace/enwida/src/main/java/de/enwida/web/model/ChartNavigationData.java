package de.enwida.web.model;

import java.util.ArrayList;
import java.util.List;

import de.enwida.transport.DataResolution;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.NavigationDefaults;
import de.enwida.web.utils.TSO;


public class ChartNavigationData {

	private String chartTitle;
	private NavigationDefaults defaults;
	private NavigationDataStructure navigationDS;
	private ProductTree productTree;
	private List<DataResolution> resolutions;
	private CalendarRange timeRange;
	private List<TSO> tsos;
	private String xAxisLabel;
	private String yAxisLabel;

	public ChartNavigationData() {
	}

	public ChartNavigationData(String chartTitle, String xAxisLabel, String yAxisLabel) {
		this(chartTitle, xAxisLabel, yAxisLabel, new ArrayList<TSO>(), null, new ArrayList<DataResolution>(), new ProductTree(), null);
		this.timeRange = CalendarRange.always();
	}

	public ChartNavigationData(String chartTitle, String xAxisLabel, String yAxisLabel, List<TSO> tsos, CalendarRange timeRange,
			List<DataResolution> resolutions, ProductTree productTree, NavigationDefaults defaults) {
		this.chartTitle = chartTitle;
		this.xAxisLabel = xAxisLabel;
		this.yAxisLabel = yAxisLabel;
		this.tsos = tsos;
		this.timeRange = timeRange;
		this.resolutions = resolutions;
		this.productTree = productTree;
		this.defaults = defaults;
	}

	public void addResolution(DataResolution resolution) {
		this.resolutions.add(resolution);
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

	public List<DataResolution> getResolutions() {
		return this.resolutions;
	}

	public CalendarRange getTimeRange() {
		return this.timeRange;
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

	public void setResolutions(List<DataResolution> resolutions) {
		this.resolutions = resolutions;
	}

	public void setTimeRange(CalendarRange timeRange) {
		this.timeRange = timeRange;
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
