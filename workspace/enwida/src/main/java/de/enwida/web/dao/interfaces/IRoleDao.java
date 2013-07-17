package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.Role;
import de.enwida.web.model.UserRoleCollection;

public interface IRoleDao {

    UserRoleCollection getUserRoles(long userID);

    Role getRoleByID(Long id);

    void addRole(Role role);

    List<Role> getAllRoles();

}

