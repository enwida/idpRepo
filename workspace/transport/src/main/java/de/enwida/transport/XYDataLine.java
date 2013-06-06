package de.enwida.transport;

import java.util.Arrays;
import java.util.List;

public class XYDataLine extends DataLine<XYDataPoint> {
	
	public XYDataLine(LineRequest request) {
		super(request);
	}

	public void addDataPoint(double x, double y) {
		addDataPoint(new XYDataPoint(x, y));
	}

}
