package de.enwida.web.dao.implementation;

import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IRoleDao;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;

@Repository
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public class RoleDaoImpl extends AbstractBaseDao<Role> implements IRoleDao {

    @Override
    public Role addRole(Role role) throws Exception{
        return save(role);
    }
    
    @Override
    public Role save(Role role) throws Exception
    {
        if(role==null) return null;
        
        Role exist = fetchByName(role.getRoleName());
        if (exist == null) {
            // create or refresh
            create(role);
        } else {
			role.setRoleID(exist.getRoleID());
			role = update(role);
        }
		return role;
    }
  
    @Override
	public Set<Right> getAllAspects(long roleID) {
        Role role = this.fetchById(roleID);
        return role.getRights();
    }
}
