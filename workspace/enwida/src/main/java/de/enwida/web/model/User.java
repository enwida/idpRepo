package de.enwida.web.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "users.users")
public class User {
    
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userID;
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
    private UserRoleCollection roles;
    private String telephone;
    private String activationKey;
    private UserRoleCollection userPermissionCollection;

    public User(long userID, String userName, String password,
            String firstName, String lastName, boolean enabled) {
        // TODO Auto-generated constructor stub
        this.setUserID(userID);
        this.setUserName(userName);
        this.setLastName(lastName);
        this.setFirstName(firstName);
        this.setPassword(password);
        this.setEnabled(enabled);
    }

    public User(long userID, String userName, String password, boolean enabled) {
        this(userID, userName, password, null, null, enabled);
    }

    public User() {
        // TODO Auto-generated constructor stub
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "user_password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Id
    @Column(name = "user_id")
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

    @Transient
    public UserRoleCollection getUserPermissionCollection() {
        return userPermissionCollection;
    }

    public void setUserPermissionCollection(
            UserRoleCollection userPermissionCollection) {
        this.userPermissionCollection = userPermissionCollection;
    }

    public boolean hasPermission(String permission) {
        return getUserPermissionCollection().implies(new UserRole(permission));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "joining_date")
    public Date getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(Date joiningDate) {
        this.joiningDate = joiningDate;
    }

    @Transient
    public Date getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Date loginCount) {
        this.loginCount = loginCount;
    }

    @Transient
    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Column(name = "company_name")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Column(name = "telephone")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Transient
    public UserRoleCollection getRoles() {
        return roles;
    }

    public void setRoles(UserRoleCollection roles) {
        this.roles = roles;
    }

    @Column(name = "company_logo")
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

    @Column(name = "activation_id")
    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    @Transient
    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
