package de.enwida.web.model;

import java.util.Date;


public class Right {
    private long rightID;
    private long roleID;
    private int tso;
    private int product;
    private String resolution;
    private Date timeFrom;
    private Date timeTo;
    private String aspect;
    private boolean enabled;
    
    public long getRightID() {
        return rightID;
    }
    public void setRightID(long rightID) {
        this.rightID = rightID;
    }
    public long getRoleID() {
        return roleID;
    }
    public void setRoleID(long roleID) {
        this.roleID = roleID;
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
    public Date getTimeFrom() {
        return timeFrom;
    }
    public void setTimeFrom(java.util.Date date) {
        this.timeFrom = date;
    }
    public Date getTimeTo() {
        return timeTo;
    }
    public void setTimeTo(Date time2) {
        this.timeTo = time2;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
}
