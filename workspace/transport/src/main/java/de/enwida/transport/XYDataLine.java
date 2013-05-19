package de.enwida.transport;

import java.util.Arrays;
import java.util.List;

public class XYDataLine extends DataLine<XYDataPoint> {
	
	private String xTitle;
	private String yTitle;
	
	public XYDataLine(DataRequest request, boolean hasDateAxis) {
		super(request, hasDateAxis);
	}
	
	public void addDataPoint(double x, double y) {
		addDataPoint(new XYDataPoint(x, y));
	}

	public String getxTitle() {
		return xTitle;
	}

	public void setxTitle(String xTitle) {
		this.xTitle = xTitle;
	}

	public String getyTitle() {
		return yTitle;
	}

	public void setyTitle(String yTitle) {
		this.yTitle = yTitle;
	}

	public List<String> getTitles() {
		return Arrays.asList(new String[] { xTitle, yTitle });
	}
	
}
