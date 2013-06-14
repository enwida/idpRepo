package de.enwida.web.model;

import java.sql.Date;

public class User {
 
	private Long userID;
	private String userName;
	private String lastName;
    private String firstName;
    private String email;
	private String password;
	private String company;
	private String contactNo;
	private boolean enabled;
	private Date joiningDate;
	private Date loginCount;
	private Date lastLogin;
	private Date groups;
	private String companyName;
    private String joinDate;
    private String tel;


	private UserPermissionCollection userPermissionCollection=new UserPermissionCollection();

	public User(long userID, String userName, String password,String firstName,String lastName, boolean enabled) {
		// TODO Auto-generated constructor stub
		this.setUserID(userID);
		this.setUserName(userName);
		this.setLastName(lastName);
		this.setFirstName(firstName);
		this.setPassword(password);
		this.setEnabled(enabled);
	}
	
	public User(long userID,String userName,String password, boolean enabled){
		this(userID,userName,password,null,null,enabled);
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

	public UserPermissionCollection getUserPermissionCollection() {
		return userPermissionCollection;
	}

	public void setUserPermissionCollection(
			UserPermissionCollection userPermissionCollection) {
		this.userPermissionCollection = userPermissionCollection;
	}
	
	public boolean hasPermission(String permission){
		return getUserPermissionCollection().implies(new UserPermission(permission));
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

	public Date getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Date loginCount) {
		this.loginCount = loginCount;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getGroups() {
		return groups;
	}

	public void setGroups(Date groups) {
		this.groups = groups;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
