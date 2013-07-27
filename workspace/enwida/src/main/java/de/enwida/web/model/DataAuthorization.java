package de.enwida.web.model;

import java.util.Date;

public class DataAuthorization {
	
	private int role;
	private int productId;
	private int tso;
	private String aspect;
	private String resolution;
	private Date timeFrom;
	private Date timeTo;
	private boolean enabled;
	
	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getTso() {
		return tso;
	}

	public void setTso(int tso) {
		this.tso = tso;
	}

	public String getAspect() {
		return aspect;
	}

	public void setAspect(String aspect) {
		this.aspect = aspect;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Date getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Date timeFrom) {
		this.timeFrom = timeFrom;
	}

	public Date getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(Date timeto) {
		this.timeTo = timeto;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}	
}
