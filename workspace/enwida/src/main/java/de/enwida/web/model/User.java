package de.enwida.web.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import de.enwida.web.db.model.NavigationDefaults;
import de.enwida.web.db.model.NavigationSettings;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.utils.Constants;

@Entity
@Table(name = Constants.USER_TABLE_NAME, schema = Constants.USER_TABLE_SCHEMA_NAME)
public class User implements Serializable, UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7333400626851874286L;

	public static final String USER_ID = "USER_ID";
	public static final String CLIENT_ID = "CLIENT_ID";
	public static final String USER_NAME = "NAME";
	public static final String FIRST_NAME = "FIRST_NAME";
	public static final String LAST_NAME = "LAST_NAME";
	public static final String PASSWORD = "USER_PASSWORD";
	public static final String JOINING_DATE = "JOINING_DATE";
	public static final String COMPANY_NAME = "COMPANY_NAME";
	public static final String COMPANY_LOGO = "COMPANY_LOGO";
	public static final String ACTIVATION_ID = "ACTIVATION_ID";
	public static final String ENABLED = "ENABLED";
	public static final String LOGIN_COUNT = "LOGIN_COUNT";
	public static final String LAST_LOGIN = "LAST_LOGIN";
	public static final String TELEPHONE = "TELEPHONE";

	@Id
	@Column(name = USER_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@Column(name = USER_NAME, nullable = false, unique = true)
    private String userName;

	@Column(name = LAST_NAME)
    private String lastName;

	@Column(name = FIRST_NAME)
    private String firstName;

	@Column(name = PASSWORD)
    private String password;

	@Transient
    private String confirmPassword;

	@Column(name = COMPANY_NAME)
    private String companyName;

	@Column(name = COMPANY_LOGO)
    private String companyLogo;

	@Column(name = ENABLED)
    private boolean enabled;

	@Column(name = JOINING_DATE)
    private Date joiningDate;

	@Column(name = LOGIN_COUNT)
	private int loginCount;

	@Column(name = LAST_LOGIN)
	private Date lastLogin;

	@Column(name = TELEPHONE)
	private String telephone;

	@Column(name = ACTIVATION_ID)
	private String activationKey;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = Constants.USER_GROUP_TABLE_NAME, schema = Constants.USER_GROUP_TABLE_SCHEMA_NAME,
		joinColumns = {@JoinColumn(name=USER_ID)}, inverseJoinColumns={@JoinColumn(name=Group.GROUP_ID)})
	private Set<Group> groups;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user",targetEntity=NavigationSettings.class)
	// @ElementCollection(targetClass = NavigationSettings.class)
	private Set<NavigationSettings> navigationSettings;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "uploader", targetEntity = UploadedFile.class)
	// @ElementCollection(targetClass = UploadedFile.class)
	private Set<UploadedFile> uploadedFiles;

	@Transient
	private Map<Integer, NavigationDefaults> chartDefaults;

	@Transient
	private Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
   
	@Transient	
	private List<Role> roles;

    public User(String userName, String password,
            String firstName, String lastName, boolean enabled) {
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
		return new Long(userId);
    }

	public void setUserID(Long userID) {
		this.userId = userID.intValue();
    }

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
    public String toString() {
        return this.getUserName();
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

    @Transient
	public int getLoginCount() {
        return loginCount;
    }

	public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    @Transient
	public Date getLastLogin() {
        return lastLogin;
    }

	public void setLastLogin(Date lastLogin) {
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

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    @Transient
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

	public Set<Group> getGroups() {
	    if( groups==null){
			this.groups = new HashSet<Group>();
	    }
		return groups;
    }

	public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

	@Transient
	public Map<Integer, NavigationDefaults> getChartDefaults() {
		if (chartDefaults == null) {
			chartDefaults = new HashMap<Integer, NavigationDefaults>();
			for (NavigationSettings setting : getNavigationSettings()) {
					chartDefaults.put(setting.getChartId(),
							setting.getSettingsData());
			}
		}
		return chartDefaults;
	}

	public void setChartDefaults(Map<Integer, NavigationDefaults> chartDefaults) {
		this.chartDefaults = chartDefaults;
	}

	@Transient
	public Set<NavigationSettings> getNavigationSettings() {
		if (navigationSettings == null) {
			navigationSettings = new HashSet<NavigationSettings>();
		}
		return navigationSettings;
	}

	public void setNavigationSettings(Set<NavigationSettings> navigationSettings) {
		this.navigationSettings = navigationSettings;
	}

	public void addNavigationSettings(int chartId,
			NavigationDefaults updateddefaults) {
		if (getChartDefaults().containsKey(chartId)) {
			for (NavigationSettings setting : getNavigationSettings()) {
				if (setting.getChartId() == chartId) {
					setting.setSettingsData(updateddefaults);
				}
			}
		} else {
			getNavigationSettings().add(
new NavigationSettings(chartId, updateddefaults, this,
							null));
		}
	}

	public void removeNavigationSettings(int chartId,
			NavigationDefaults updateddefaults) {
		if (getChartDefaults().containsKey(chartId)) {
			// Navigation settings must override equals method and for same
			// chartId and same user object should be equal
			getNavigationSettings().remove(new NavigationSettings(chartId));
		}
	}

	@Transient
	public Set<UploadedFile> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(Set<UploadedFile> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	public void addUploadedFile(UploadedFile uploadedFile) {
		if (uploadedFile != null) {
			if (uploadedFiles == null) {
				this.uploadedFiles = new HashSet<UploadedFile>();
			}
			this.uploadedFiles.add(uploadedFile);
		}
	}

	public void removeUploadedFile(UploadedFile uploadedFile) {
		if (uploadedFile != null && uploadedFiles != null
				&& uploadedFiles.contains(uploadedFile)) {
			uploadedFiles.remove(uploadedFile);
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (this.groups != null && this.groups.size() > 0) {
			for (Group group : this.groups) {
				for (Role role : group.getAssignedRoles()) {
					this.authorities.add(role);
				}
			}
		}
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

    public List<Role> getRoles() {
        if (roles==null){
            roles=new ArrayList<Role>();
            for (Group group : this.getGroups()) {
                for (Role role : group.getAssignedRoles()) {
                    roles.add(role);
                }
            }
        }
        return roles;
    }

    public void setRoles(List<Role> roles) {
       this.roles=roles;
    }

}
