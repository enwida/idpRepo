/**
 * 
 */
package de.enwida.web.model;

import java.util.Date;

import de.enwida.transport.DataResolution;

/**
 * @author Jitin
 *
 */
public class GenericData {

	private DataResolution resolution;
	private String role;
	private Date time1;
	private Date time2;
	private String username;

	/**
	 * 
	 */
	public GenericData() {
		super();
	}

	/**
	 * @param resolution
	 * @param role
	 * @param time1
	 * @param time2
	 * @param username
	 */
	public GenericData(DataResolution resolution, String role, Date time1,
			Date time2,
			String username) {
		super();
		this.resolution = resolution;
		this.role = role;
		this.time1 = time1;
		this.time2 = time2;
		this.username = username;
	}

	/**
	 * @return the resolution
	 */
	public DataResolution getResolution() {
		return this.resolution;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return this.role;
	}

	/**
	 * @return the time1
	 */
	public Date getTime1() {
		return this.time1;
	}

	/**
	 * @return the time2
	 */
	public Date getTime2() {
		return this.time2;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * @param resolution
	 *            the resolution to set
	 */
	public void setResolution(DataResolution resolution) {
		this.resolution = resolution;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @param time1
	 *            the time1 to set
	 */
	public void setTime1(Date time1) {
		this.time1 = time1;
	}

	/**
	 * @param time2
	 *            the time2 to set
	 */
	public void setTime2(Date time2) {
		this.time2 = time2;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GenericData ["
				+ (this.username != null ? "username=" + this.username + ", "
						: "")
						+ (this.resolution != null ? "resolution=" + this.resolution
								+ ", " : "")
								+ (this.role != null ? "role=" + this.role + ", " : "")
								+ (this.time1 != null ? "time1=" + this.time1 + ", " : "")
								+ (this.time2 != null ? "time2=" + this.time2 : "") + "]";
	}
}
