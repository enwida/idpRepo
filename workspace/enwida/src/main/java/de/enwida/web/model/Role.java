package de.enwida.web.model;

import java.io.Serializable;
import java.util.Collections;
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
    private Long roleID;

    @Column(name = ROLE_NAME, unique = true)
    private String roleName;

    @Column(name = DESCRIPTION) 
    private String description;

	@ManyToMany(mappedBy = "assignedRoles", fetch = FetchType.EAGER)
	private Set<Group> assignedGroups;
    
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = Constants.ROLE_RIGHT_TABLE_NAME, schema = Constants.ROLE_RIGHT_TABLE_SCHEMA_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = {
            Right.RIGHT_ID, Role.ROLE_ID }) }, joinColumns = { @JoinColumn(name = ROLE_ID) }, inverseJoinColumns = { @JoinColumn(name = Right.RIGHT_ID) })
	private Set<Right> rights;
    
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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Long getRoleID() {
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
		if (assignedGroups == null) {
			assignedGroups = new HashSet<Group>();
		}
        return Collections.unmodifiableSet(assignedGroups);
    }

	public void setAssignedGroups(Set<Group> assignedGroups) {
        this.assignedGroups = assignedGroups;
    }
    
	public Set<Right> getRights() {
		if (rights == null) {
			return new HashSet<Right>();
		}
		return Collections.unmodifiableSet(rights);
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
	
	@Override
	public int hashCode() {
		return roleName.hashCode();
	}
	
	public boolean hasRight(Right right){
	    return rights.contains(right);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Role)) {
			return false;
		}
		final Role other = (Role) obj;
		return roleName.equals(other.roleName);
	}

}
