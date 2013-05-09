package de.enwida.transport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class DataLine<T> implements IDataLine {
	
	protected String title;
	protected Product product;
	protected Calendar startTime;
	protected Calendar endTime;
	protected DataResolution resolution;
	protected boolean hasDateAxis;
	protected List<T> dataPoints;
	
	public DataLine(String title, Product product, Calendar startTime,
			Calendar endTime, DataResolution resolution, boolean hasDateAxis) {
		this.title = title;
		this.product = product;
		this.startTime = startTime;
		this.endTime = endTime;
		this.resolution = resolution;
		this.hasDateAxis = hasDateAxis;
		this.dataPoints = new ArrayList<T>();
	}
	
	public DataLine(DataRequest request, String title, boolean hasDateAxis) {
		this(title, request.getProduct(), request.getStartTime(), request.getEndTime(),
				request.getResolution(), hasDateAxis);
	}

	public List<T> getDataPoints() {
		return dataPoints;
	}
	
	public void addDataPoint(T dataPoint) {
		dataPoints.add(dataPoint);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public DataResolution getResolution() {
		return resolution;
	}

	public void setResolution(DataResolution resolution) {
		this.resolution = resolution;
	}

	public boolean hasDateAxis() {
		return hasDateAxis;
	}

	public void setHasDateAxis(boolean hasDataAxis) {
		this.hasDateAxis = hasDataAxis;
	}

}
