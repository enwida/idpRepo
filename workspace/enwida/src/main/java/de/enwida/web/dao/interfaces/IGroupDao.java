package de.enwida.web.dao.interfaces;

import de.enwida.web.model.Group;

/**
 * @author olcay tarazan
 *
 */
public interface IGroupDao extends IDao<Group> {
    /**
	 * Adds new group
	 * 
	 * @param newGroup
	 * @return
	 * @throws Exception
	 */
	public Group addGroup(final Group newGroup) throws Exception;
    /**
     * Removed the group from database
     * @param groupID
     * @throws Exception
     */
    public void removeGroup(long groupID) throws Exception;

	Group save(Group group) throws Exception;
}
