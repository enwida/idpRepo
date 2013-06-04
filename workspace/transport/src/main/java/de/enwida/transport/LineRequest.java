package de.enwida.transport;

import java.util.Calendar;
import java.util.Locale;

public class LineRequest {
	
	private Aspect aspect;
	private int product;
	private Calendar startTime;
	private Calendar endTime;
	private DataResolution resolution;
	private Locale locale;
	private int tso;
	
	public LineRequest(Aspect aspect, int product, int tso, Calendar startTime,
					   Calendar endTime, DataResolution resolution, Locale locale) {
		this.aspect = aspect;
		this.product = product;
		this.tso = tso;
		this.startTime = startTime;
		this.endTime = endTime;
		this.resolution = resolution;
		this.locale = locale;
	}
	
	public Aspect getAspect() {
		return aspect;
	}
	public void setAspect(Aspect aspect) {
		this.aspect = aspect;
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

	public int getTso() {
		return tso;
	}

	public void setTso(int tso) {
		this.tso = tso;
	}

}
