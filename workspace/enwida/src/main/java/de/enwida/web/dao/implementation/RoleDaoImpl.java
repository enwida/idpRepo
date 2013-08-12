package de.enwida.web.dao.implementation;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IRoleDao;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;

@Repository
public class RoleDaoImpl extends AbstractBaseDao<Role> implements IRoleDao {

    @Override
    public void addRole(Role role){
        create(role);
    }
    
    @Override
    public void removeRole(Role role){
        delete(role);
    }    

    @Override
    public List<Right> getAllAspects(long roleID) {
        Role role = this.fetchById(roleID);
        return role.getRights();
    }
}
