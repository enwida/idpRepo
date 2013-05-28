package de.enwida.web.model;


public class User {
 
	private Long userID;
	private String userName;
	private String lastName;
	private String firstName;
	private String password;
	private boolean enabled=false;
	private boolean admin=false;
	private boolean testuser=false;
	private boolean export=false;

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
	
	@Override
	public String toString() {
		return this.getUserName();
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public boolean isTestuser() {
		return testuser;
	}

	public void setTestuser(boolean testuser) {
		this.testuser = testuser;
	}

	public boolean isExport() {
		return export;
	}

	public void setExport(boolean export) {
		this.export = export;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
