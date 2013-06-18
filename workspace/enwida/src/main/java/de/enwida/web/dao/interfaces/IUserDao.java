package de.enwida.web.dao.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.enwida.web.model.AspectRight;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;


public interface IUserDao {
	public int save(User user);
	public void setUserRoles(int userId, HashMap roles);
	public String getPassword(String email) ;
    public List<User> findAllUsers();
	public void addPermission(int userID, int roleID);
	public void removePermission(int userID, int roleID);
	public User getUser(Long id);
	public ArrayList<Group> getAvailableGroupsForUser(long userID);
	public ArrayList<Group> getUserGroups(long userID);
    public boolean updateUser(User user);
    public User getUser(String userName);
    public boolean deleteUser(User user);
    public String assignUserToGroup(int userID, int groupID);
    public String deassignUserToGroup(int userID, int groupID);
    
	public List<Group> getAllGroups();
    public Group addGroup(final Group newGroup);
    public List<Group> getAllGroupsWithUsers();
    public String assignRoleToGroup(int roleID, int groupID);
    public String deassignRoleToGroup(int roleID, int groupID);
    
    public void addRole(Role role);
    public List<Role> getAllRoles();
    public List<Role> getAllRolesWithGroups();
    
    public boolean checkEmailAvailability(String email) ;
    public boolean saveUserInGroup(final long userId, final long groupId);
    public long getRoleIdOfGroup(final long groupId);
    public int getGroupIdByCompanyName(final String companyName);
    public Group getGroupByGroupId(long groupId);
    public boolean saveUserInAnonymousGroup(final long userId);

}
