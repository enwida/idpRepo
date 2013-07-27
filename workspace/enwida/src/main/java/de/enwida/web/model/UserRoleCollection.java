package de.enwida.web.model;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

public class UserRoleCollection extends PermissionCollection implements Iterable<UserRole>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<UserRole> permissions=new ArrayList<UserRole>();
	
	@Override
	public void add(Permission permission) {
		// TODO Auto-generated method stub
		permissions.add((UserRole)permission);
	}

	@Override
	public boolean implies(Permission permission) {
		for (UserRole item : permissions) {
			if(item.getName().equalsIgnoreCase(permission.getName())){
				return true;
			}
		}
		return false;
	}

	public boolean hasMoreElements() {
		// TODO Auto-generated method stub
		return false;
	}

	public UserRole nextElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<Permission> elements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		String toString="";
		for (UserRole item : permissions) {
			toString+=item+",";
		}
		return toString;
	}

    @Override
    public Iterator<UserRole> iterator() {
        Iterator<UserRole> iprof = permissions.iterator();
        return iprof; 
    }
}
