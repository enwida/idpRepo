package de.enwida.web.dao.interfaces;

import java.util.List;
import java.util.Set;

import de.enwida.web.model.Right;
import de.enwida.web.model.Role;

public interface IRoleDao extends IDao<Role> {

    Role fetchById(long id);

    Role addRole(Role role) throws Exception;

    List<Role> fetchAll();

	Set<Right> getAllAspects(long roleID);

    Role save(Role role) throws Exception;

}
