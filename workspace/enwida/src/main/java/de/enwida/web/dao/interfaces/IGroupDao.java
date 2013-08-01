package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.Group;

public interface IGroupDao {

    public List<Group> getAllGroups();

    public Group addGroup(final Group newGroup);


    public long getRoleIdOfGroup(final long groupId);

    public Group getGroupByCompanyName(final String companyName);

    public Group getGroupByGroupId(long groupId);

    public String assignRoleToGroup(long roleID, long groupID);

    public String deassignRoleFromGroup(long roleID, long groupID);

    public void removeGroup(long groupID) throws Exception;

    public int getRoleIdByCompanyName(String companyName);

    public Group getGroupByName(String groupName);
    
    public List<Group> getGroupsByRole(long roleID);

    public List<Group> getUserGroups(long userID);

}
