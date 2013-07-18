package de.enwida.web.db.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_navigation", schema = "users")
public class UserNavigation {

	public static final String ID = "ID";
	public static final String CHART_ID = "CHART_ID";
	public static final String SETTINGS_DATA = "SETTINGS_DATA";

	/**
	 * 
	 */
	public UserNavigation() {
	}

	/**
	 * @param id
	 * @param chartId
	 * @param settingsData
	 */
	public UserNavigation(int id, String chartId, String settingsData) {
		super();
		this.id = id;
		this.chartId = chartId;
		this.settingsData = settingsData;
	}

	/**
	 * @param id
	 * @param chartId
	 * @param settingsData
	 */
	public UserNavigation(String chartId, String settingsData) {
		super();
		this.chartId = chartId;
		this.settingsData = settingsData;
	}

	@Id
	@Column(name = ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = CHART_ID)
	private String chartId;

	@Basic
	@Column(name = SETTINGS_DATA)
	private String settingsData;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the chartId
	 */
	public String getChartId() {
		return chartId;
	}

	/**
	 * @param chartId
	 *            the chartId to set
	 */
	public void setChartId(String chartId) {
		this.chartId = chartId;
	}

	/**
	 * @return the settingsData
	 */
	public String getSettingsData() {
		return settingsData;
	}

	/**
	 * @param settingsData
	 *            the settingsData to set
	 */
	public void setSettingsData(String settingsData) {
		this.settingsData = settingsData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserNavigation [id=" + id + ", "
				+ (chartId != null ? "chartId=" + chartId + ", " : "")
				+ (settingsData != null ? "settingsData=" + settingsData : "")
				+ "]";
	}

}
