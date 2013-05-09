package de.enwida.transport;

import java.util.Calendar;

public class MinMaxDataLine extends DataLine<MinMaxDataPoint> {

	public MinMaxDataLine(String title, Product product, Calendar startTime,
			Calendar endTime, DataResolution resolution, boolean hasDataAxis) {
		
		super(title, product, startTime, endTime, resolution, hasDataAxis);
	}
	
	public MinMaxDataLine(DataRequest request, String title, boolean hasDataAxis) {
		super(request, title, hasDataAxis);
	}
	
	public void addDataPoint(double x, double mean, double min, double max) {
		addDataPoint(new MinMaxDataPoint(x, mean, min, max));
	}

}
