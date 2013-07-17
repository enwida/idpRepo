package de.enwida.web.dao.interfaces;

import java.util.ArrayList;
import java.util.List;

import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.model.UserRoleCollection;

public interface IGroupDao {

    public ArrayList<Group> getAvailableGroupsForUser(long userID);

    public ArrayList<Group> getUserGroups(long userID);

    public List<Group> getAllGroups();

    public Group addGroup(final Group newGroup);


    public long getRoleIdOfGroup(final long groupId);

    public Group getGroupByCompanyName(final String companyName);

    public Group getGroupByGroupId(long groupId);

    public long getAnonymousGroupId();


    public String assignUserToGroup(long userID, long groupID);

    public String deassignUserFromGroup(long userID, long groupID);

    public String assignRoleToGroup(long roleID, long groupID);

    public String deassignRoleFromGroup(long roleID, long groupID);

    public void removeGroup(long groupID) throws Exception;

    public int getRoleIdByCompanyName(String companyName);

    public void deleteUserGroup(long userID);

    public Group getGroupByName(String groupName);
    
    public boolean checkUserActivationId(String username, String activationCode);

    public List<Group> getGroupsByRole(long roleID);

}
