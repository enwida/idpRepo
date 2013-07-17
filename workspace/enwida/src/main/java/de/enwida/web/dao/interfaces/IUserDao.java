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

    public User getUserByID(Long id);

    public ArrayList<Group> getAvailableGroupsForUser(long userID);

    public ArrayList<Group> getUserGroups(long userID);

    public boolean checkEmailAvailability(String email);

    public User getUserByName(String userName);

    public void deleteUser(User user);

    List<User> findAllUsers();

    UserRoleCollection getUserRoles(long userID);

    List<User> getAllUsers();

    boolean checkUserActivationId(String username, String activationCode);

    public String assignUserToGroup(long userId, long groupID);

    public boolean activateUser(String username);

    public boolean updateUser(User user);

    public String deassignUserFromGroup(long userID, long groupID);

    public boolean enableDisableUser(long userID, boolean enabled);

    public boolean usernameAvailablility(String username);

    public List<User> getUsersByGroupID(Long groupID);

}
