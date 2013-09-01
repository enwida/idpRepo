package de.enwida.transport;

import java.util.ArrayList;
import java.util.List;

public abstract class DataLine<T> implements IDataLine {
	
	protected LineRequest lineRequest;
	protected String title;
	protected String unit;
	protected List<T> dataPoints;
	
	public DataLine(LineRequest request) {
		this.lineRequest = request;
		this.dataPoints = new ArrayList<T>();
	}

	public List<T> getDataPoints() {
		return dataPoints;
	}
	
	public void addDataPoint(T dataPoint) {
		dataPoints.add(dataPoint);
	}

	public LineRequest getLineRequest() {
		return lineRequest;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}

}
