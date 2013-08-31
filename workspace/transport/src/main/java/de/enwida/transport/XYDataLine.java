package de.enwida.transport;


public class XYDataLine extends DataLine<XYDataPoint> {
	
	public XYDataLine(LineRequest request) {
		super(request);
	}

	public void addDataPoint(double x, double y) {
		addDataPoint(new XYDataPoint(x, y));
	}

}
