package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.model.User;

public interface UserService {
	
	public User getUser(Long id);
	public List<User> getUsers();		
	public boolean saveUser(User user);
	public String getPassword(String email);
	public List<User> findAllUsersWithPermissions();
	public void addPermission(int userID, int roleID);
	public void removePermission(int userID, int roleID);
}
