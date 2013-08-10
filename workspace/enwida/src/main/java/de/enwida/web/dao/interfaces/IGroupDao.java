package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.Group;

/**
 * @author olcay tarazan
 *
 */
public interface IGroupDao {
    /**
     * Gets all the groups
     * @return
     */
    public List<Group> getAllGroups();
    /**
     * Adds new group
     * @param newGroup
     * @return
     */
    public Group addGroup(final Group newGroup);

    /**
     * Gets the role ID of a group
     * @param groupId
     * @return
     */
    public long getRoleIdOfGroup(final long groupId);
    /**
     * Gets group of the Specified Company if exists
     * @param companyName
     * @return
     */
    public Group getGroupByCompanyName(final String companyName);
    /**
     * Gets group details  by Group ID
     * @param groupId
     * @return
     */
    public Group getGroupByGroupId(long groupId);
    /**
     * Assigns role to group
     * @param roleID
     * @param groupID
     * @throws Exception
     */
    public void assignRoleToGroup(long roleID, long groupID) throws Exception ;
    /**
     * DeAssign role from group 
     * @param roleID
     * @param groupID
     * @throws Exception
     */
    public void deassignRoleFromGroup(long roleID, long groupID) throws Exception ;
    /**
     * Removed the group from database
     * @param groupID
     * @throws Exception
     */
    public void removeGroup(long groupID) throws Exception;
    /**
     * Gets group by group Name
     * @param groupName
     * @return
     */
    public Group getGroupByName(String groupName);
    /**
     * Get the groups which belongs to specified role
     * @param roleID
     * @return
     */
    public List<Group> getGroupsByRole(long roleID);
    /**
     * Gets the groups of the user
     * @param userID
     * @return
     */
    public List<Group> getUserGroups(long userID);

}
