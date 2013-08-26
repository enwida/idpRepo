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

    boolean checkUserActivationId(String username, String activationCode) throws Exception;

    void activateUser(String username) throws Exception;

    void enableDisableUser(long userID, boolean enabled) throws Exception;

    boolean usernameAvailablility(String username) throws Exception;

    Long getNextSequence(String schema, String sequenceName);

    int getUploadedFileVersion(UploadedFile uplaodedfile, User user);

	List<UploadedFile> getActiveUploadedFiles(User user);
}
