package de.enwida.transport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class DataLine<T> implements IDataLine {
	
	protected int tso;
	protected int product;
	protected Aspect aspect;
	protected Calendar startTime;
	protected Calendar endTime;
	protected DataResolution resolution;
	protected String title;
	protected String unit;
	protected List<T> dataPoints;
	
	public DataLine(int tso, int product, Aspect aspect, Calendar startTime, Calendar endTime, DataResolution resolution) {
		this.product = product;
		this.aspect = aspect;
		this.startTime = startTime;
		this.endTime = endTime;
		this.resolution = resolution;
		this.dataPoints = new ArrayList<T>();
	}
	
	public DataLine(LineRequest request) {
		this(request.getTso(), request.getProduct(), request.getAspect(), request.getStartTime(), request.getEndTime(), request.getResolution());
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

    public Aspect getAspect() {
        return aspect;
    }

    public void setAspect(Aspect aspect) {
        this.aspect = aspect;
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
	
	public int getTso() {
		return tso;
	}
	
	public void setTso(int tso) {
		this.tso = tso;
	}
	
}
