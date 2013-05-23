package de.enwida.web.model;


public class User {
 
	private Long userID;
	private String userName;
	private String lastName;
	private String firstName;
	private String password;

	public User(long userID, String userName, String password, boolean enabled) {
		// TODO Auto-generated constructor stub
		this.setUserID(userID);
		this.setUserName(userName);
		this.setLastName(lastName);
		this.setFirstName(firstName);
		this.setPassword(password);
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

}