package de.enwida.web.model;

import java.sql.Date;

public class User {
 
	private Long userID;
	private String userName;
	private String lastName;
	private String firstName;
	private String password;
	private boolean enabled;
	private Date joiningDate;

	public User(long userID, String userName, String password, boolean enabled) 
	{
		this.setUserID(userID);
		this.setUserName(userName);
		this.setLastName(lastName);
		this.setFirstName(firstName);
		this.setPassword(password);
		this.setEnabled(enabled);
	}

	public User() {
		// TODO Auto-generated constructor stub
	}
	

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

}
