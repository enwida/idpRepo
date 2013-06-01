package de.enwida.web.model;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Enumeration;

public class UserPermissionCollection extends PermissionCollection implements Enumeration<UserPermission>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<String> permissions=new ArrayList<String>();
	
	@Override
	public void add(Permission permission) {
		// TODO Auto-generated method stub
		permissions.add(permission.getName());
	}

	@Override
	public boolean implies(Permission permission) {
		for (String item : permissions) {
			if(item.trim().equalsIgnoreCase(permission.getName().trim())){
				return true;
			}
		}
		return false;
	}

	public boolean hasMoreElements() {
		// TODO Auto-generated method stub
		return false;
	}

	public UserPermission nextElement() {
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
		for (String item : permissions) {
			toString+=item+",";
		}
		return toString;
	}
}
