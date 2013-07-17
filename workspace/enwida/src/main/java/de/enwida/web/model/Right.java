package de.enwida.web.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Right {
    private long rightID;
    private long roleID;
    private int tso;
    private int product;
    private int resolution;
    private Date time1;
    private Date time2;
    private long aspectID;
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
    public int getResolution() {
        return resolution;
    }
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }
    public Date getTime1() {
        return time1;
    }
    public void setTime1(Date time1) {
        this.time1 = time1;
    }
    public Date getTime2() {
        return time2;
    }
    public void setTime2(Date time2) {
        this.time2 = time2;
    }
    public long getAspectID() {
        return aspectID;
    }
    public void setAspectID(long aspectID) {
        this.aspectID = aspectID;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
