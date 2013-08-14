package de.enwida.web.dao.implementation;

import org.springframework.stereotype.Repository;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IGroupDao;
import de.enwida.web.model.Group;

@Repository
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public class GroupDaoImpl extends AbstractBaseDao<Group> implements IGroupDao {
	
	@Override
	public Group addGroup(final Group newGroup) throws Exception
    {
	    return save(newGroup);
    }

    @Override
	public Group save(Group group) throws Exception
    {
        if(group==null) return null;
        
        Group exist = fetchByName(group.getGroupName());
        if (exist == null) {
            // create or refresh
            create(group);
        } else {
			group.setGroupID(exist.getGroupID());
			group = update(group);
        }

		return group;
    }

}
