package de.enwida.web.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.enwida.web.utils.Constants;

@Entity
@Table(name = Constants.RIGHT_TABLE_NAME, schema = Constants.RIGHT_TABLE_SCHEMA_NAME)
public class Right implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5914085492367851378L;
    
    public static final String RIGHT_ID = "RIGHT_ID";
    public static final String TSO = "TSO";
    public static final String PRODUCT = "PRODUCT";
    public static final String RESOLUTION = "RESOLUTION";
    public static final String TIMEFROM = "TIMEFROM";
    public static final String TIMETO = "TIMETO";
    public static final String ASPECT = "ASPECT";
    public static final String ENABLED = "ENABLED";
    
    @Id
    @Column(name = RIGHT_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rightID;
    
	@ManyToOne(targetEntity = Role.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = Role.ROLE_ID)
	private Role role;
    
    @Column(name = TSO)
    private int tso;
    
    @Column(name = PRODUCT)
    private int product;
    
    @Column(name = RESOLUTION)
    private String resolution;
    
    @Column(name = TIMEFROM)
    private Date timeFrom;
    
    @Column(name = TIMETO)
    private Date timeTo;
    
    @Column(name = ASPECT)
    private String aspect;
    
    @Column(name = ENABLED)
    private boolean enabled;
    
    public long getRightID() {
        return rightID;
    }
    public void setRightID(long rightID) {
        this.rightID = rightID;
    }

	@Transient
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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
