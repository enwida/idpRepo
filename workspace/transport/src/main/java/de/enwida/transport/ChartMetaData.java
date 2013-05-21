package de.enwida.transport;

public class ChartMetaData {
	
	private String chartTitle;
	private String hAxisLabel;
	private String vAxisLabel;


	public ChartMetaData(String chartTitle, String hAxisLabel, String vAxisLabel) {
		this.chartTitle = chartTitle;
		this.hAxisLabel = hAxisLabel;
		this.vAxisLabel = vAxisLabel;
	}
	
	public ChartMetaData(String chartTitle) {
		this(chartTitle, null, null);
	}
	
	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public String gethAxisLabel() {
		return hAxisLabel;
	}

	public void sethAxisLabel(String hAxisLabel) {
		this.hAxisLabel = hAxisLabel;
	}

	public String getvAxisLabel() {
		return vAxisLabel;
	}

	public void setvAxisLabel(String vAxisLabel) {
		this.vAxisLabel = vAxisLabel;
	}
	
}
