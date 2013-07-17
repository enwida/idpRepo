package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.model.Role;

public interface IRoleDao {

    List<Role> getUserRoles(long userID);

    Role getRoleByID(Long id);

    void addRole(Role role);

    List<Role> getAllRoles();

}

