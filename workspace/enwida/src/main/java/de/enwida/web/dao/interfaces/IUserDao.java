package de.enwida.web.dao.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;


public interface IUserDao {
	public long save(User user);
	public String getPassword(String email) ;
	public List<User> findAllUsersWithPermissions();
	public void addPermission(final long userID, final long roleID);
	public void removePermission(int userID, int roleID);
	public User getUser(Long id);
	public ArrayList<Group> getAvailableGroupsForUser(long userID);
	public ArrayList<Group> getUserGroups(long userID);
	public List<Group> getAllGroups();
	public Group addGroup(final Group newGroup);
    public void addRole(Role role);
    public List<Role> getAllRoles();
    public boolean checkEmailAvailability(String email) ;
    public Group getGroupByGroupName(final String groupName);
    public boolean saveUserInGroup(final long userId, final long groupId);
    public long getRoleIdOfGroup(final long groupId);
}
