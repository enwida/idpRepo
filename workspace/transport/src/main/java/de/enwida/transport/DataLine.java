package de.enwida.transport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class DataLine<T> implements IDataLine {
	
	protected int product;
	protected Calendar startTime;
	protected Calendar endTime;
	protected DataResolution resolution;
	protected boolean hasDateAxis;
	protected List<T> dataPoints;
	
	public DataLine(int product, Calendar startTime,
			Calendar endTime, DataResolution resolution, boolean hasDateAxis) {
		this.product = product;
		this.startTime = startTime;
		this.endTime = endTime;
		this.resolution = resolution;
		this.hasDateAxis = hasDateAxis;
		this.dataPoints = new ArrayList<T>();
	}
	
	public DataLine(DataRequest request, boolean hasDateAxis) {
		this(request.getProduct(), request.getStartTime(), request.getEndTime(),
				request.getResolution(), hasDateAxis);
	}

	public List<T> getDataPoints() {
		return dataPoints;
	}
	
	public void addDataPoint(T dataPoint) {
		dataPoints.add(dataPoint);
	}

	public int getProduct() {
		return product;
	}

	public void setProduct(int product) {
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

	public boolean getHasDateAxis() {
		return hasDateAxis;
	}

	public void setHasDateAxis(boolean hasDataAxis) {
		this.hasDateAxis = hasDataAxis;
	}

}
