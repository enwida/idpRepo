package de.enwida.web.service.interfaces;

import java.util.List;
import java.util.Locale;

import de.enwida.web.model.Group;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;

/**
 * This interface will be implemented to provide user related actions
 * 
 * @author olcay tarazan
 * 
 */
public interface IUserService {
    
    public String getLastActivationLink();

    /**
     * Get users by userID
     * 
     * @param id
     * @return the user created in database
     * @throws Exception 
     */
    public User fetchUser(Long id);

    /**
     * Gets all the users from DB
     * 
     * @param userName
     * @return the user created in database
     */
    public User fetchUser(String userName);

    /**
     * Gets all the users
     * 
     * @return the collection of users
     * @throws Exception 
     */
    public List<User> fetchAllUsers() throws Exception;

    /**
     * Send activation to saved user
     * 
     * @param user
     *            to save into DB
     * @param activationHost
     *            hostname of website where activation will take place.
     * @param locale
     *            for internationalization.
     * @return the result of the action
     * @throws Exception 
     */
    public boolean saveUser(User user, String activationHost, Locale locale,boolean sendEmail) throws Exception;

    /**
     * Saves user into database
     * 
     * @param user
     * @return the result of the action
     */
    public boolean saveUser(User user,boolean sendEmail)throws Exception;

    /**
     * Gets the user password
     * 
     * @param email
     *            email of the user
     * @return Password of user
     */
    public String getPassword(String email)throws Exception;

    /**
     * Gets all the groups
     * 
     * @return collection of groups
     */
    public List<Group> fetchAllGroups()throws Exception;

    /**
     * Add a group into database
     * 
     * @param userID
     * @return collection of groups
     */
    public Group saveGroup(Group newGroup)throws Exception;

    /**
     * Adds role to the DB
     * 
     * @param role
     */
    public void saveRole(Role role)throws Exception;
    
    /**
     * Adds right to DB
     * @throws Exception 
     */
    public void saveRight(Right right) throws Exception;
    
    /**
     * Fetch role from DB
     * @param roleName
     */
    public Role fetchRole(String roleName);
    
    /**
     * Fetch right from DB
     * @param rightId
     */
    public Right fetchRight(Long rightId);

    /**
     * Get all roles
     * 
     * @return collection of roles
     */
    public List<Role> fetchAllRoles()throws Exception;
    
    /**
     * Get all rights
     */
    public List<Right> fetchAllRights() throws Exception;

    /**
     * Update User information based on userID
     * 
     * @param user
     *            user with new information
     * @return result of the action
     */
    public void updateUser(User user)throws Exception;

    /**
     * Resets the password of user
     * 
     * @param userID
     */
    public void resetPassword(long userID,Locale locale)throws Exception;

    /**
     * Deletes the user from DB
     * 
     * @param user
     * @return
     */
    public void deleteUser(User user) throws Exception;

    /**
     * Deletes the user from DB
     * 
     * @param user
     * @return
     */
    public void deleteUser(long userId) throws Exception;

    /**
	 * Assigns group to user. {@link User} object will be updated in place; updated {@link Group} will be returned.
	 * 
	 * @param user the persisted user object
	 * @param group the persisted group object
	 * @return the fresh group object
     * @throws Exception 
	 */
	public Group assignGroupToUser(User user, Group group) throws Exception;

    /**
	 * Assigns group to user.
	 * 
	 * @param user ID
	 * @param group ID
	 * @return the fresh group object
     * @throws Exception 
	 */
	public void assignGroupToUser(long userId, Long groupID) throws Exception;

    /**
	 * Revokes group from user. {@link User} object will be updated in place; updated {@link Group} will be returned.
	 * 
	 * @param user the persisted user object
	 * @param group the persisted group object
	 * @return the fresh group object
     * @throws Exception 
	 */
    public Group revokeUserFromGroup(User user, Group group) throws Exception;

    /**
     * Revokes User from Group
     * 
     * @param user ID
     * @param group ID
     * @return
     */
    public void revokeUserFromGroup(long userID, long groupID);

    /**
     * Assigns role to the groups
     * 
     * @param selectedRole
     * @param selectedGroup
     * @return
     * @throws Exception
     */
    public void assignRoleToGroup(long selectedRole, long selectedGroup) throws Exception;

    /**
	 * Assigns role to group. {@link Group} object will be updated in place; updated {@link Role} will be returned.
	 * 
	 * @param role the persisted role object
	 * @param group the persisted group object
	 * @return the fresh {@link Role} object
     * @throws Exception 
	 */
	public Role assignRoleToGroup(Role role, Group group) throws Exception;

	/**
	 * Revoke role from group.
	 * @param role ID
	 * @param group ID
	 * @throws Exception 
	 */
	public void revokeRoleFromGroup(long roleId, long groupId) throws Exception;

    /**
	 * Revoke role from group. {@link Group} object will be updated in place; updated {@link Role} will be returned.
	 * 
	 * @param role the persisted role object
	 * @param group the persisted group object
	 * @return the fresh {@link Role} object
     * @throws Exception 
	 */
	public Role revokeRoleFromGroup(Role role, Group group) throws Exception;

    /**
     * Enables or disables the user
     * 
     * @param userID
     * @param enabled
     * @return
     * @throws Exception 
     */
    public void enableDisableUser(int userID, boolean enabled) throws Exception;

    /**
     * Removes the group from database
     * 
     * @param groupID
     * @throws Exception
     */
    public void deleteGroup(long groupID) throws Exception;
    
    /**
     * Removes the role from DB
     * @param role ID
     * @throws Exception
     */
    public void deleteRole(long roleID) throws Exception;
    
    /**
     * Removes the right from DB
     * @param right ID
     * @throws Exception
     */
    public void deleteRight(long rightID) throws Exception;

    /**
     * Checks user availability
     * 
     * @param username
     * @return
     * @throws Exception 
     */
    public boolean userNameAvailability(final String username) throws Exception;

    /**
     * Checks email availability
     * 
     * @param email
     * @return
     * @throws Exception 
     */
    public boolean emailAvailability(final String email) throws Exception;
    
    /**
     * Enabled or disables the user
     * 
     * @param rightID
     * @param enabled
     * @return
     * @throws Exception 
     */
    public void enableDisableAspect(int rightID, boolean enabled) throws Exception;

    /**
     * Activates the user
     * 
     * @param username
     * @param activationCode
     * @return
     * @throws Exception 
     */
    public boolean activateUser(String username, String activationCode) throws Exception;

    /**
     * Gets the current user from database TODO:This should be updated.User info
     * should be kept in session all the time
     * 
     * @return
     * @throws Exception 
     */
    public User getCurrentUser() throws Exception;
    
    /**
     * Fetch the group from database
     * @param groupName name of the group
     * @return the group object
     * @throws Exception
     */
    public Group fetchGroup(String groupName) throws Exception;
    
    /**
     * Synchronize user object with database content. Use this if you need to have all current mappings.
     * @param user prototype object
     * @return up-to-date user object with all mappings
     * @throws Exception 
     */
    public User syncUser(User user) throws Exception;
    /**
     * Gets nextSequence from the database
     */
	public Long getNextSequence(String schema, String sequenceName);

	/**
	 * make nextSequence in the database
	 */
	public Long getNextSequence(String schema, String sequenceName,
			boolean reset);

    /**
     * Gets the group name from the company name
     */
    Group fetchGroupByCompanyName(String companyName);
    /**
     * Finds the group
     */
	Group findGroup(Group group);
    /**
     * Finds the role
     */
	Role findRole(Role role);
    /**
     * Finds the right
     */
	Right findRight(Right right);	
    /**
     * Finds the group by id
     */
	public Group fetchGroupById(long groupId);
    /**
     * Finds the role by id
     */
	public Role fetchRoleById(long roleId);
    /**
     * Enables/Disables the autopass
     */
    public void enableDisableAutoPass(Long groupID, boolean enabled) throws Exception;
    /**
     * Get group by group domain
     */
	public Group fetchGroupByDomainName(String domainName);
	/**
     * Update domain auto pass of the group
     */
	void updateDomainAutoPass(Long groupID, String domainAutoPass)
			throws Exception;

    public Role enableDisableAspectForRole(Right right, Role role,boolean enabled) throws Exception;
}
