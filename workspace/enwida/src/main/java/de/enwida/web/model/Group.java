package de.enwida.web.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import de.enwida.web.utils.Constants;

@Entity
@Table(name = Constants.GROUP_TABLE_NAME, schema = Constants.GROUP_TABLE_SCHEMA_NAME)
public class Group implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 7173117643347079483L;
	
	public static final String GROUP_ID = "GROUP_ID";
	public static final String GROUP_NAME = "NAME";
	public static final String AUTO_PASS = "AUTO_PASS";

	@Id
	@Column(name = GROUP_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long groupId;
    
	@Column(name = GROUP_NAME)
    private String groupName;
    
	@Column(name = AUTO_PASS)
    private boolean autoPass;

	@ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = User.class)
	private Set<User> assignedUsers;
	
	// @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
	// targetEntity = Role.class)
	// @JoinTable(name = Constants.GROUP_ROLE_TABLE_NAME, schema =
	// Constants.GROUP_ROLE_TABLE_SCHEMA_NAME, uniqueConstraints = {
	// @UniqueConstraint(columnNames = {
	// Role.ROLE_ID, Group.GROUP_ID }) }, joinColumns = { @JoinColumn(name =
	// GROUP_ID) }, inverseJoinColumns = { @JoinColumn(name = Role.ROLE_ID) })
	@ManyToMany(mappedBy = "assignedGroups", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = Role.class)
	private Set<Role> assignedRoles;

    public Long getGroupID() {
		return new Long(groupId);
    }

    public void setGroupID(Long groupID) {
		this.groupId = groupID.intValue();
    }

	public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

	public Group(String groupName) {
        this.groupName = groupName;
    }
	
	public Group(){
	    this(null);
	}

	public Set<User> getAssignedUsers() {
        if (assignedUsers == null) {
			assignedUsers = new HashSet<User>();
        }
		return assignedUsers;
    }

	public void setAssignedUsers(Set<User> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public void addAssignedUsers(User user) {
        if (assignedUsers == null) {
			assignedUsers = new HashSet<User>();
        }
        this.assignedUsers.add(user);
    }

	public Set<Role> getAssignedRoles() {
        if (assignedRoles == null) {
			assignedRoles = new HashSet<Role>();
        }
		return assignedRoles;
	}

	public void setAssignedRoles(Set<Role> assignedRoles) {
		this.assignedRoles = assignedRoles;
	}

	public boolean isAutoPass() {
        return autoPass;
    }

    public void setAutoPass(boolean autoPass) {
        this.autoPass = autoPass;
    }
    
    @Override
    public String toString() {
        return getGroupName();
    }
}
