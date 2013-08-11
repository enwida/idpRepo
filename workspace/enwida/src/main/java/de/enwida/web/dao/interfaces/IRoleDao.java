package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.Role;

public interface IRoleDao extends IDao<Role> {

    List<Role> getUserRoles(long userID) throws Exception;

    Role fetchById(long id);

    void addRole(Role role) throws Exception;

    void removeRole(Role role) throws Exception;

    List<Role> fetchAll();

}
