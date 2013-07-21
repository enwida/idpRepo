package de.enwida.web.db.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import de.enwida.transport.DataResolution;
import de.enwida.web.utils.Constants;

@Entity
@Table(name = Constants.NAVIGATION_DEFAULTS_TABLE_NAME, schema = Constants.NAVIGATION_DEFAULTS_TABLE_SCHEMA_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = {
		"TSO_ID", "RESOLUTION", "PRODUCT", CalendarRange.FROM,
		CalendarRange.TO }) })
public class NavigationDefaults implements Cloneable,Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1268487366015431606L;
	public static final String NAVIGATION_DEFAULTS_ID = "NAVIGATION_DEFAULTS_ID";
	public static final String LINE_ID = "LINE_ID";

	@Id
	@Column(name = NAVIGATION_DEFAULTS_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "TSO_ID")
	private int tsoId;

	@Column(name = "RESOLUTION")
	@Enumerated(EnumType.STRING)
    private DataResolution resolution;

	@Column(name = "PRODUCT")
	private int product;

	@Embedded
	private CalendarRange timeRange;

	@ElementCollection(targetClass = Integer.class)
	@JoinTable(name = Constants.USER_NAVIGATION_DISABLED_LINES_TABLE_NAME, schema = Constants.USER_NAVIGATION_DISABLED_LINES_TABLE_SCHEMA_NAME, joinColumns = { @JoinColumn(name = NAVIGATION_DEFAULTS_ID, referencedColumnName = NAVIGATION_DEFAULTS_ID) })
	private Set<Integer> disableLines;
    
	@Transient
	private List<Integer> disabledLines;
	/**
	 * 
	 */
	public NavigationDefaults() {
	}

	public NavigationDefaults(int tsoId, DataResolution resolution,
			int product, CalendarRange timeRange) {
        this.tsoId = tsoId;
        this.resolution = resolution;
        this.product = product;
        this.timeRange = timeRange;
		this.disabledLines = new ArrayList<Integer>();
    }
    
    public NavigationDefaults clone() {
        final NavigationDefaults result = new NavigationDefaults(tsoId, resolution, product, new CalendarRange(timeRange.getFrom(), timeRange.getTo()));
        result.getDisabledLines().addAll(disabledLines);
        return result;
    }

    public int getTsoId() {
        return tsoId;
    }

    public void setTsoId(int tsoId) {
        this.tsoId = tsoId;
    }

    public DataResolution getResolution() {
        return resolution;
    }

    public void setResolution(DataResolution resoulution) {
        this.resolution = resoulution;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public CalendarRange getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(CalendarRange timeRange) {
        this.timeRange = timeRange;
    }
    
	@Transient
	public List<Integer> getDisabledLines() {
		if (disableLines != null) {
			disabledLines = new ArrayList<Integer>();
			disabledLines.addAll(disableLines);
		}
        return disabledLines;
    }
    
	public void setDisabledLines(List<Integer> disabledLines) {
		if (disabledLines != null) {
			disableLines = new HashSet<Integer>();
		}
        this.disabledLines = disabledLines;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Transient
	public Set<Integer> getDisableLines() {
		if (disabledLines != null) {
				disableLines = new HashSet<Integer>();
				disableLines.addAll(disabledLines);
		}
		return disableLines;
	}

	public void setDisableLines(Set<Integer> disableLines) {
		if (disableLines != null) {
			disabledLines = new ArrayList<>(disableLines);
		}
		this.disableLines = disableLines;
	}

}
