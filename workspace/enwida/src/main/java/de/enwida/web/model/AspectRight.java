package de.enwida.web.model;

import java.sql.Date;

public class AspectRight {
    private long rightID;
    private long roleID;
    private String aspectName;
    private Date t1;
    private Date t2;
    private String product;
    private String resolution;
    private String tso;
    private boolean enabled;
    public String getTso() {
        return tso;
    }
    public void setTso(String tso) {
        this.tso = tso;
    }
    public long getRightID() {
        return rightID;
    }
    public void setRightID(long rightID) {
        this.rightID = rightID;
    }
    public String getAspectName() {
        return aspectName;
    }
    public void setAspectName(String aspectName) {
        this.aspectName = aspectName;
    }
    public Date getT1() {
        return t1;
    }
    public void setT1(Date t1) {
        this.t1 = t1;
    }
    public Date getT2() {
        return t2;
    }
    public void setT2(Date t2) {
        this.t2 = t2;
    }
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }
    public String getResolution() {
        return resolution;
    }
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
    public long getRoleID() {
        return roleID;
    }
    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
