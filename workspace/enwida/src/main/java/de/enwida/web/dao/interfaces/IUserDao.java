package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.User;

/**
 * User DAO
 * @author olcay tarazan
 *
 */
public interface IUserDao {
    long save(User user) throws Exception;

    User getUserByID(Long id) throws Exception;

    void deleteUser(User user) throws Exception;

    List<User> findAllUsers() throws Exception;

    List<User> getAllUsers() throws Exception;

    boolean checkUserActivationId(String username, String activationCode) throws Exception;

    void assignUserToGroup(long userId, long groupID) throws Exception;

    void activateUser(String username) throws Exception;

    void updateUser(User user) throws Exception;

    void deassignUserFromGroup(long userID, long groupID) throws Exception;

    void enableDisableUser(long userID, boolean enabled) throws Exception;

    boolean usernameAvailablility(String username) throws Exception;

    List<User> getUsersByGroupID(Long groupID) throws Exception;

    User getUserByName(String userName) throws Exception;

    public Long getNextSequence(String schema, String sequenceName);

	int getUploadedFileVersion(UploadedFile file, User user);

}
