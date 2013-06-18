package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;

public interface UserService {

    public User getUser(Long id);
    public User getUser(String userName);
	public List<User> getUsers();		
	public boolean saveUser(User user);
	public String getPassword(String email);
	public List<User> findAllUsers();
	public void addPermission(int userID, int roleID);
	public void removePermission(int userID, int roleID);
	public List<Group> getAvailableGroupsForUser(long userID);
	public List<Group> getUserGroups(long userID);
	public List<Group> getAllGroups();
    public Group addGroup(Group newGroup);
    public void saveRole(Role role);
    public List<Role> getAllRoles();
    public boolean updateUser(User user);
    public boolean resetPassword(long userID);
    public void deleteUser(User user);
    public String assignUserToGroup(int userID, int groupID);
    public String deassignUserToGroup(int userID, int groupID);
    public List<Group> getAllGroupsWithUsers();
    public String assignRoleToGroup(int selectedRole, int selectedGroup);
    public String deassignRoleToGroup(int selectedRole, int selectedGroup);
    public List<Role> getAllRolesWithGroups();
}
