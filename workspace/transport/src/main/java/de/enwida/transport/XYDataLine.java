package de.enwida.transport;

import java.util.Calendar;

public class XYDataLine extends DataLine<XYDataPoint> {
	
	public XYDataLine(String title, Product product, Calendar startTime,
			Calendar endTime, DataResolution resolution, boolean hasDataAxis) {
		
		super(title, product, startTime, endTime, resolution, hasDataAxis);
	}
	
	public XYDataLine(DataRequest request, String title, boolean hasDataAxis) {
		super(request, title, hasDataAxis);
	}
	
	public void addDataPoint(double x, double y) {
		addDataPoint(new XYDataPoint(x, y));
	}

}
