package de.enwida.web.db.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import de.enwida.web.model.User;
import de.enwida.web.utils.Constants;

@Entity
@Table(name = Constants.USER_NAVIGATION_SETTINGS_TABLE_NAME, schema = Constants.USER_NAVIGATION_SETTINGS_TABLE_SCHEMA_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = {
 "CHART_ID",
				NavigationDefaults.NAVIGATION_DEFAULTS_ID, User.USER_ID }),
		@UniqueConstraint(columnNames = { "CHART_ID",
				NavigationDefaults.NAVIGATION_DEFAULTS_ID, User.CLIENT_ID }) })
public class NavigationSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4804531988368126366L;

	public static final String NAVIGATION_SETTINGS_ID = "NAVIGATION_SETTINGS_ID";
	public static final String CHART_ID = "CHART_ID";


	@Id
	@Column(name = NAVIGATION_SETTINGS_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = CHART_ID)
	private int chartId;

	@OneToOne(cascade = CascadeType.ALL, targetEntity = NavigationDefaults.class)
	@JoinColumn(name = NavigationDefaults.NAVIGATION_DEFAULTS_ID)
	private NavigationDefaults settingsData;

	@ManyToOne
	@JoinColumn(name = User.USER_ID)
	private User user;

	@Column(name = User.CLIENT_ID)
	private int clientId;

	/**
	 * 
	 */
	public NavigationSettings() {
	}

	/**
	 * @param chartId
	 */
	public NavigationSettings(int chartId) {
		this.chartId = chartId;
	}

	/**
	 * @param chartId
	 * @param settingsData
	 * @param user
	 * @param clientId
	 */
	public NavigationSettings(int chartId, NavigationDefaults settingsData,
			User user, int clientId) {
		this.chartId = chartId;
		this.settingsData = settingsData;
		this.user = user;
		this.clientId = clientId;
	}

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
	public int getChartId() {
		return chartId;
	}

	/**
	 * @param chartId
	 *            the chartId to set
	 */
	public void setChartId(int chartId) {
		this.chartId = chartId;
	}

	/**
	 * @return the settingsData
	 */
	@Transient
	public NavigationDefaults getSettingsData() {
		return settingsData;
	}

	/**
	 * @param settingsData
	 *            the settingsData to set
	 */
	public void setSettingsData(NavigationDefaults settingsData) {
		this.settingsData = settingsData;
	}

	/**
	 * @return the user
	 */
	@Transient
	public User getUser() {
		if (user != null) {
			clientId = -1;
		}
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		if (user != null) {
			clientId = -1;
		}
		this.user = user;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + chartId;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NavigationSettings other = (NavigationSettings) obj;
		if (chartId != other.chartId)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NavigationSettings [id="
				+ id
				+ ", chartId="
				+ chartId
				+ ", "
				+ (settingsData != null ? "settingsData=" + settingsData + ", "
						: "") + (user != null ? "user=" + user : "") + "]";
	}
}
