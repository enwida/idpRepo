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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;

import de.enwida.web.utils.Constants;

@Entity
@Table(name = Constants.ROLE_TABLE_NAME, schema = Constants.ROLE_TABLE_SCHEMA_NAME)
public class Role implements Serializable, GrantedAuthority {

    /**
     * 
     */
    private static final long serialVersionUID = -2772444487718010995L;
    
	public static final String ROLE_ID = "ROLE_ID";
	public static final String ROLE_NAME = "NAME";
	public static final String DESCRIPTION = "DESCRIPTION";
    
    @Id
    @Column(name = ROLE_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleID;

    @Column(name = ROLE_NAME)
    private String roleName;

    @Column(name = DESCRIPTION) 
    private String description;
    
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Group.class)
	@JoinTable(name = Constants.GROUP_ROLE_TABLE_NAME, schema = Constants.GROUP_ROLE_TABLE_SCHEMA_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = {
			Role.ROLE_ID, Group.GROUP_ID }) }, joinColumns = { @JoinColumn(name = ROLE_ID) }, inverseJoinColumns = { @JoinColumn(name = Group.GROUP_ID) })
	private Set<Group> assignedGroups = new HashSet<Group>(0);
    
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "role", targetEntity = Right.class)
	private Set<Right> rights = new HashSet<Right>(0);
    
	/**
	 * 
	 */
	public Role() {
	}
	
    public Role(String roleName) {
        this.roleName=roleName;
    }

	/**
	 * @param roleID
	 */
	public Role(long roleID) {
		this.roleID = roleID;
	}

	public void addAssignedGroups(Group group) {
        this.assignedGroups.add(group);
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public long getRoleID() {
        return roleID;
    }
    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

	public Set<Group> getAssignedGroups() {
        return assignedGroups;
    }

	public void setAssignedGroups(Set<Group> assignedGroups) {
        this.assignedGroups = assignedGroups;
    }
    
	public Set<Right> getRights() {
		return rights;
	}

	public void setRights(Set<Right> rights) {
		this.rights = rights;
	}

	@Override
    public String toString() {
        return getRoleName();
    }

	@Override
	public String getAuthority() {
		return this.roleName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (roleName == null) {
			if (other.roleName != null)
				return false;
		} else if (!roleName.equals(other.roleName))
			return false;
		return true;
	}

}
