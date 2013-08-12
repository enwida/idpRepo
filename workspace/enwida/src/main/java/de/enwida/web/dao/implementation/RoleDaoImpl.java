package de.enwida.web.dao.implementation;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IRoleDao;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;

@Repository
public class RoleDaoImpl extends AbstractBaseDao<Role> implements IRoleDao {

    @Override
    public void addRole(Role role){
        save(role);
    }
    
    @Override
    public void removeRole(Role role){
        delete(role);
    }    
    
    
    @Override
    public Role save(Role role)
    {
        Role exist = fetchByName(role.getRoleName());
        if (exist == null) {
            // create or refresh
            create(role);
        } else {
            exist = update(exist);
        }

        return exist;
    }
  

    @Override
    public List<Right> getAllAspects(long roleID) {
        Role role = this.fetchById(roleID);
        return role.getRights();
    }
}
