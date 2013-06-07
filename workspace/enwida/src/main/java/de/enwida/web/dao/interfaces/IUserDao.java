package de.enwida.web.dao.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.enwida.web.model.Group;
import de.enwida.web.model.User;


public interface IUserDao {
	public int save(User user);
	public void setUserRoles(int userId, HashMap roles);
	public String getPassword(String email) ;
	public List<User> findAllUsersWithPermissions();
	public void addPermission(int userID, int roleID);
	public void removePermission(int userID, int roleID);
	public User getUser(Long id);
	public ArrayList<Group> getAvailableGroupsForUser(long userID);
	public ArrayList<Group> getUserGroups(long userID);
	public List<Group> getAllGroups();
}
