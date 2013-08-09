package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;

/**
 * This interface will be implemented to provide user related actions
 * 
 * @author olcay tarazan
 * 
 */
public interface IUserService {

    /**
     * Get users by userID
     * 
     * @param id
     * @return the user created in database
     * @throws Exception 
     */
    public User getUser(Long id) throws Exception;

    /**
     * Gets all the users from DB
     * 
     * @param userName
     * @return the user created in database
     */
    public User getUser(String userName)throws Exception;

    /**
     * Gets all the users
     * 
     * @return the collection of users
     * @throws Exception 
     */
    public List<User> getUsers() throws Exception;

    /**
     * Send activation to saved user
     * 
     * @param user
     *            to save into DB
     * @param activationHost
     *            hostname of website where activation will take place.
     * @return the result of the action
     * @throws Exception 
     */
    public boolean saveUser(User user, String activationHost) throws Exception;

    /**
     * Saves user into database
     * 
     * @param user
     * @return the result of the action
     */
    public boolean saveUser(User user)throws Exception;

    /**
     * Gets the user password
     * 
     * @param email
     *            email of the user
     * @return Password of user
     */
    public String getPassword(String email)throws Exception;

    /**
     * Finds all the user from database
     * 
     * @return Collection of users
     */
    public List<User> getAllUsers()throws Exception;

    /**
     * Gets all the groups of a user
     * 
     * @param userID
     * @return collection of groups
     */
    public List<Group> getUserGroups(long userID)throws Exception;

    /**
     * Gets all the groups
     * 
     * @return collection of groups
     */
    public List<Group> getAllGroups()throws Exception;

    /**
     * Add a group into database
     * 
     * @param userID
     * @return collection of groups
     */
    public Group addGroup(Group newGroup)throws Exception;

    /**
     * Adds role to the DB
     * 
     * @param role
     */
    public void addRole(Role role)throws Exception;

    /**
     * Get all roles
     * 
     * @return collection of roles
     */
    public List<Role> getAllRoles()throws Exception;

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
    public void resetPassword(long userID)throws Exception;

    /**
     * Deletes the user from DB
     * 
     * @param user
     * @return
     */
    public void deleteUser(User user) throws Exception;

    /**
     * Assigns User into Group
     * 
     * @param userID
     * @param groupID
     * @return
     */
    public void assignUserToGroup(int userID, int groupID) throws Exception;

    /**
     * Deassigns User into Group
     * 
     * @param userID
     * @param groupID
     * @return
     */
    public void deassignUserToGroup(int userID, int groupID) throws Exception;

    /**
     * Get All Groups with Users attached
     * 
     * @return Collection of groups
     * @throws Exception 
     */
    public List<Group> getAllGroupsWithUsers() throws Exception;

    /**
     * Assigns role to the groups
     * 
     * @param selectedRole
     * @param selectedGroup
     * @return
     * @throws Exception
     */
    public void assignRoleToGroup(int selectedRole, int selectedGroup)
            throws Exception;

    /**
     * Deassigns role to the groups
     * 
     * @param selectedRole
     * @param selectedGroup
     * @return
     * @throws Exception
     */
    public void deassignRoleToGroup(int selectedRole, int selectedGroup)
            throws Exception;

    /**
     * Get All roles with Groups attached
     * 
     * @return List of Roles
     */
    public List<Role> getAllRolesWithGroups()throws Exception;

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
    public void removeGroup(int groupID) throws Exception;

    /**
     * Checks user availability
     * 
     * @param username
     * @return
     * @throws Exception 
     */
    public boolean userNameAvailability(final String username) throws Exception;

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

    Long getNextSequence(String schema, String sequenceName);

	UploadedFile getFile(int fileId);

	UploadedFile getFileByFilePath(String filePath);

	public int getUploadedFileVersion(UploadedFile file, User user);
}
