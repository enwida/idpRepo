package de.enwida.transport;

import java.util.Calendar;
import java.util.Locale;

public class DataRequest {
	
	private ChartType chartType;
	private int product;
	private Calendar startTime;
	private Calendar endTime;
	private DataResolution resolution;
	private Locale locale;
	
	public DataRequest(ChartType chartType, int product, Calendar startTime,
					   Calendar endTime, DataResolution resolution, Locale locale) {
		this.chartType = chartType;
		this.product = product;
		this.startTime = startTime;
		this.endTime = endTime;
		this.resolution = resolution;
		this.locale = locale;
	}
	
	public ChartType getChartType() {
		return chartType;
	}
	public void setChartType(ChartType chartType) {
		this.chartType = chartType;
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
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public void calcResolution() {
		throw new RuntimeException("Stub!");
	}

}
