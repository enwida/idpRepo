package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.model.User;

/**
 * User DAO
 * @author olcay tarazan
 *
 */
public interface IUserDao extends IDao<User> {
	long save(User user) throws Exception;

	User fetchById(long id);

    void deleteUser(User user) throws Exception;

    List<User> fetchAll();

    boolean checkUserActivationId(String username, String activationCode) throws Exception;

    void activateUser(String username) throws Exception;

    void updateUser(User user) throws Exception;

    void enableDisableUser(long userID, boolean enabled) throws Exception;

    boolean usernameAvailablility(String username) throws Exception;

    List<User> getUsersByGroupID(Long groupID) throws Exception;

    User fetchByName(String userName) throws Exception;

    Long getNextSequence(String schema, String sequenceName);

    int getUploadedFileVersion(UploadedFile uplaodedfile, User user);
}
