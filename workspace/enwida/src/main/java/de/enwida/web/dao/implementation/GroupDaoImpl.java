package de.enwida.web.dao.implementation;

import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IGroupDao;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;

@Repository
public class GroupDaoImpl extends AbstractBaseDao<Group> implements IGroupDao {
	
	@Override
    public Group addGroup(final Group newGroup) 
    {
	    return save(newGroup);
    }


    @Override
    public void removeGroup(long groupID) throws Exception {
        Group group=fetchById(groupID);
        delete(group);
    }
    
    @Override
    public Group save(Group group)
    {
        if(group==null) return null;
        
        Group exist = fetchByName(group.getGroupName());
        if (exist == null) {
            // create or refresh
            create(group);
        } else {
            exist.setGroupID(exist.getGroupID());
            exist = update(exist);
        }

        return exist;
    }

}
