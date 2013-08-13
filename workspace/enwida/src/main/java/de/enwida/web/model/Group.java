package de.enwida.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

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

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = Constants.USER_GROUP_TABLE_NAME, schema = Constants.USER_GROUP_TABLE_SCHEMA_NAME,
		joinColumns = {@JoinColumn(name=GROUP_ID)}, inverseJoinColumns={@JoinColumn(name=User.USER_ID)})
	private List<User> assignedUsers;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@JoinTable(name = Constants.GROUP_ROLE_TABLE_NAME, schema = Constants.GROUP_ROLE_TABLE_SCHEMA_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = {
			Role.ROLE_ID, Group.GROUP_ID }) }, joinColumns = { @JoinColumn(name = GROUP_ID, referencedColumnName = GROUP_ID) }, inverseJoinColumns = { @JoinColumn(name = Role.ROLE_ID, referencedColumnName = Role.ROLE_ID) })
	private List<Role> assignedRoles;   

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

    @Transient
    public List<User> getAssignedUsers() {
        if (assignedUsers == null) {
            assignedUsers = new ArrayList<User>();
        }
        return Collections.unmodifiableList(assignedUsers);
    }

    public void setAssignedUsers(List<User> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public void addAssignedUsers(User user) {
        if (assignedUsers == null) {
            assignedUsers = new ArrayList<User>();
        }
        this.assignedUsers.add(user);
    }

	@Transient
	public List<Role> getAssignedRoles() {
        if (assignedRoles == null) {
            assignedRoles = new ArrayList<Role>();
        }
		return assignedRoles;
	}

	public void setAssignedRoles(List<Role> assignedRoles) {
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
