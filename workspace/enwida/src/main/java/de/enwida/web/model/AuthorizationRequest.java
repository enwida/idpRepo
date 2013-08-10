package de.enwida.web.model;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.web.db.model.CalendarRange;

public class AuthorizationRequest {

    private User user;
    private int tso;
    private int product;
    private DataResolution resolution;
    private CalendarRange timeRange;
    private Aspect aspect;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getTso() {
		return tso;
	}
	public void setTso(int tso) {
		this.tso = tso;
	}
	public int getProduct() {
		return product;
	}
	public void setProduct(int product) {
		this.product = product;
	}
	public DataResolution getResolution() {
		return resolution;
	}
	public void setResolution(DataResolution resolution) {
		this.resolution = resolution;
	}
	public CalendarRange getTimeRange() {
		return timeRange;
	}
	public void setTimeRange(CalendarRange timeRange) {
		this.timeRange = timeRange;
	}
	public Aspect getAspect() {
		return aspect;
	}
	public void setAspect(Aspect aspect) {
		this.aspect = aspect;
	}
    
}
