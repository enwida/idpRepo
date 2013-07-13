package de.enwida.web.dao.interfaces;

import java.util.ArrayList;
import java.util.List;

import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.model.UserRoleCollection;

public interface IUserDao {
    public long save(User user);

    public String getPassword(String email);

    public List<User> findAllUsersWithPermissions();

    public User getUserByID(Long id);

    public ArrayList<Group> getAvailableGroupsForUser(long userID);

    public ArrayList<Group> getUserGroups(long userID);

    public List<Group> getAllGroups();

    public Group addGroup(final Group newGroup);

    public void addRole(Role role);

    public List<Role> getAllRoles();

    public boolean checkEmailAvailability(String email);

    public long getRoleIdOfGroup(final long groupId);

    public Group getGroupByCompanyName(final String companyName);

    public Group getGroupByGroupId(long groupId);

    public long getAnonymousGroupId();

    public User getUserByName(String userName);

    public String assignUserToGroup(long userID, long groupID);

    public String deassignUserFromGroup(long userID, long groupID);

    public String assignRoleToGroup(long roleID, long groupID);

    public String deassignRoleFromGroup(long roleID, long groupID);

    public List<Role> getAllRolesWithGroups();

    public void deleteUser(User user);

    public boolean updateUser(User user);

    public List<User> findAllUsers();

    public List<Group> getAllGroupsWithUsers();

    public boolean enableDisableUser(long userID, boolean enabled);

    public void removeGroup(long groupID) throws Exception;

    public boolean usernameAvailablility(final String username);

    public boolean enableDisableAspect(long rightID, boolean enabled);

    public int getRoleIdByCompanyName(String companyName);

    public void deleteUserGroup(long userID);

    public UserRoleCollection getUserRoles(long userID);

    public Group getGroupByName(String groupName);

    public List<User> getAllUsers();
    
    public boolean checkUserActivationId(String username, String activationCode);
    
    public boolean activateUser(String username);

}
