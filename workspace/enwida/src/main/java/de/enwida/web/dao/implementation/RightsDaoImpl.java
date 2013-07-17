package de.enwida.web.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IRightsDao;
import de.enwida.web.dao.interfaces.IRoleDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.model.UserRole;
import de.enwida.web.model.UserRoleCollection;

@Repository
public class RightsDaoImpl extends AbstractBaseDao<Role> implements IRightsDao {
	
	@Autowired
	private DataSource datasource;
	
	
    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	
	@Override
	public String getDbTableName() {
	    return "users.rights";
	}
	
    @Override
    public boolean enableDisableAspect(long rightID, boolean enabled) {
        String sql = "UPDATE users.rights SET enabled=? WHERE right_id=?";
        this.jdbcTemplate.update(sql,enabled,rightID);
        return true;
    }
}
