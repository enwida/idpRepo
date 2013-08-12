package de.enwida.web.dao.implementation;

import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IGroupDao;
import de.enwida.web.model.Group;

@Repository
public class GroupDaoImpl extends AbstractBaseDao<Group> implements IGroupDao {
	
	@Override
    public Group addGroup(final Group newGroup) 
    {
	    //TODO:make sure groupID is updated
	    create(newGroup);
	    return newGroup;
    }


    @Override
    public void removeGroup(long groupID) throws Exception {
        Group group=fetchById(groupID);
        delete(group);
    }

}
