package de.enwida.web.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.enwida.web.db.model.CalendarRange;
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
    private Long rightID;
    
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = Role.ROLE_ID)
	private Role role;
    
    @Column(name = TSO)
    private int tso;
    
    @Column(name = PRODUCT)
    private int product;
    
    @Column(name = RESOLUTION)
    private String resolution; 

    @Embedded
    private CalendarRange timeRange;
    
    @Column(name = ASPECT)
    private String aspect;
    
    @Column(name = ENABLED)
    private boolean enabled;
    
	/**
	 * 
	 */
	public Right() {
	}

	/**
	 * @param tso
	 * @param product
	 * @param resolution
	 * @param timeRange
	 * @param aspect
	 * @param enabled
	 */
	public Right(int tso, int product, String resolution,
			CalendarRange timeRange, String aspect, boolean enabled) {
		this.tso = tso;
		this.product = product;
		this.resolution = resolution;
		this.timeRange = timeRange;
		this.aspect = aspect;
		this.enabled = enabled;
	}

	public Long getRightID() {
        return rightID;
    }
    public void setRightID(long rightID) {
        this.rightID = rightID;
    }

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
    public CalendarRange getTimeRange() {
        return timeRange;
    }
    public void setTimeRange(CalendarRange timeRange) {
        this.timeRange = timeRange;
    }

	@Override
	public int hashCode() {
		return rightID.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Right)) {
			return false;
		}
		final Right other = (Right) obj;
		return rightID.equals(other.rightID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Right [rightID=" + rightID + "]";
	}

}
