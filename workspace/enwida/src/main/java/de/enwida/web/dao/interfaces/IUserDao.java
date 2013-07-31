package de.enwida.web.dao.interfaces;

import java.util.ArrayList;
import java.util.List;

import de.enwida.web.model.Group;
import de.enwida.web.model.User;

public interface IUserDao {
    public long save(User user) throws Exception;

    public User getUserByID(Long id);

    public ArrayList<Group> getUserGroups(long userID);

    public String deleteUser(User user);

    List<User> findAllUsers();

    List<User> getAllUsers();

    boolean checkUserActivationId(String username, String activationCode);

    public String assignUserToGroup(long userId, long groupID);

    public boolean activateUser(String username);

    public boolean updateUser(User user);

    public String deassignUserFromGroup(long userID, long groupID);

    public boolean enableDisableUser(long userID, boolean enabled);

    public boolean usernameAvailablility(String username);

    public List<User> getUsersByGroupID(Long groupID);

    User getUserByName(String userName);

}
