package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;

/**
 * User DAO
 * @author olcay tarazan
 *
 */
public interface IUserDao {
	long save(User user);

	User getUserByID(Long id);

	void deleteUser(User user);

	List<User> findAllUsers();

	List<User> getAllUsers();

	boolean checkUserActivationId(String username, String activationCode);

	void assignUserToGroup(long userId, long groupID);

	void activateUser(String username);

	void updateUser(User user);

	void deassignUserFromGroup(long userID, long groupID);

	void enableDisableUser(long userID, boolean enabled);

	boolean usernameAvailablility(String username);

	List<User> getUsersByGroupID(Long groupID);

	User getUserByName(String userName);

    public Long getNextSequence(String schema, String sequenceName);

	int getUploadedFileVersion(UploadedFile file, User user);

	Group getGroupByCompanyName(String companyName);

	Group getGroupByGroupId(Long groupID);

	Group getGroupByName(String string);

	Group addGroup(Group anonymousGroup);

	String getPassword(String email);

	List<User> findAllUsersWithPermissions();

	List<Group> getAvailableGroupsForUser(long userID);

	List<Group> getUserGroups(long userID);

	List<Group> getAllGroups();

	void addRole(Role role);

	List<Role> getAllRoles();

	boolean checkEmailAvailability(String email);

	String assignRoleToGroup(int roleID, int groupID);

	List<Group> getAllGroupsWithUsers();

	Object deassignRoleFromGroup(int roleID, int groupID);

	List<Role> getAllRolesWithGroups();

	void removeGroup(int groupID);

	void enableDisableAspect(int rightID, boolean enabled);

}
