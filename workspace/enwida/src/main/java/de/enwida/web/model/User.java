package de.enwida.web.model;

import java.sql.Date;
import java.util.List;

public class User {
 
	private long userID;
	private String userName;
	private String lastName;
    private String firstName;
	private String password;
    private String confirmPassword;
    private String companyName;
    private String companyLogo;
	private boolean enabled;
	private Date joiningDate;
	private Date loginCount;
	private String lastLogin;
    private List<Group> groups;
    private List<Role> roles;
    private String telephone;
    private String activationKey;

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

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}
	
	@Override
	public String toString() {
		return this.getUserName();
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

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

	public String getActivationKey() {
		return activationKey;
	}

	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
