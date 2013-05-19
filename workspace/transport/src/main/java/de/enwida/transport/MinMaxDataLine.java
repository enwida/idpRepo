package de.enwida.transport;

import java.util.Arrays;
import java.util.List;

public class MinMaxDataLine extends DataLine<MinMaxDataPoint> {
	
	public MinMaxDataLine(DataRequest request, boolean hasDateAxis) {
		super(request, hasDateAxis);
	}

	private String xTitle;
	private String meanTitle;
	private String minTitle;
	private String maxTitle;

	public void addDataPoint(double x, double mean, double min, double max) {
		addDataPoint(new MinMaxDataPoint(x, mean, min, max));
	}

	public String getxTitle() {
		return xTitle;
	}

	public void setxTitle(String xTitle) {
		this.xTitle = xTitle;
	}

	public String getMeanTitle() {
		return meanTitle;
	}

	public void setMeanTitle(String meanTitle) {
		this.meanTitle = meanTitle;
	}

	public String getMinTitle() {
		return minTitle;
	}

	public void setMinTitle(String minTitle) {
		this.minTitle = minTitle;
	}

	public String getMaxTitle() {
		return maxTitle;
	}

	public void setMaxTitle(String maxTitle) {
		this.maxTitle = maxTitle;
	}

	public List<String> getTitles() {
		return Arrays.asList(new String[] { xTitle, meanTitle, minTitle, maxTitle });
	}

}
