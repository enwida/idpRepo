package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;

public interface UserService {
	
	public User getUser(Long id);
	public List<User> getUsers();		
	public boolean saveUser(User user);
	public String getPassword(String email);
	public List<User> findAllUsersWithPermissions();
	public void addPermission(int userID, int roleID);
	public void removePermission(int userID, int roleID);
	public List<Group> getAvailableGroupsForUser(long userID);
	public List<Group> getUserGroups(long userID);
	public List<Group> getAllGroups();
    public void addGroup(Group newGroup);
    public void saveRole(Role role);
    public List<Role> getAllRoles();
}
