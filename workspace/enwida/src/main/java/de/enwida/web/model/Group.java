package de.enwida.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Table;
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
	public static final String DOMAIN_AUTO_PASS = "DOMAIN_AUTO_PASS";

	@Id
	@Column(name = GROUP_ID)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long groupId;
    
	@Column(name = GROUP_NAME, unique = true, nullable = false)
    private String groupName;
    
	@Column(name = AUTO_PASS)
    private boolean autoPass;

	@Column(name = DOMAIN_AUTO_PASS)
    private String domainAutoPass;
	
	@ManyToMany(mappedBy = "groups", fetch = FetchType.EAGER)
	private Set<User> assignedUsers;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = Constants.GROUP_ROLE_TABLE_NAME, schema = Constants.GROUP_ROLE_TABLE_SCHEMA_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = {
			Role.ROLE_ID, Group.GROUP_ID }) }, joinColumns = { @JoinColumn(name = GROUP_ID) }, inverseJoinColumns = { @JoinColumn(name = Role.ROLE_ID) })
	private Set<Role> assignedRoles;

    public Long getGroupID() {
		return groupId;
    }

    public void setGroupID(Long groupID) {
		this.groupId = groupID;
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
        return Collections.unmodifiableSet(assignedUsers);
    }

	public void setAssignedUsers(Set<User> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

	public Set<Role> getAssignedRoles() {
        if (assignedRoles == null) {
			assignedRoles = new HashSet<Role>();
        }
		return Collections.unmodifiableSet(assignedRoles);
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
    
    public List<Right> getAllRights() {
    	final List<Right> result = new ArrayList<Right>();
    	for (final Role role : getAssignedRoles()) {
    		result.addAll(role.getRights());
    	}
    	return result;
    }
    
    @Override
    public String toString() {
        return getGroupName();
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}

	public String getDomainAutoPass() {
		return domainAutoPass;
	}

	public void setDomainAutoPass(String domainAutoPass) {
		this.domainAutoPass = domainAutoPass;
	}

}
