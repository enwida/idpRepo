package de.enwida.web.dao.interfaces;

import de.enwida.web.model.Group;

/**
 * @author olcay tarazan
 *
 */
public interface IGroupDao extends IDao<Group> {
    /**
     * Adds new group
     * @param newGroup
     * @return
     */
    public Group addGroup(final Group newGroup);
    /**
     * Removed the group from database
     * @param groupID
     * @throws Exception
     */
    public void removeGroup(long groupID) throws Exception;
}
