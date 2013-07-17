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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IRoleDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.model.UserRole;
import de.enwida.web.model.UserRoleCollection;

@Repository
public class RoleDaoImpl extends AbstractBaseDao<Role> implements IRoleDao {
	
	@Autowired
	private DataSource datasource;
	
	
    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	
	@Override
	public String getDbTableName() {
	    return "users.roles";
	}
	
	@Override
	public UserRoleCollection getUserRoles(long userID) {
	    String sql = "select DISTINCT ON (role_id) roles.role_id,roles.role_name FROM users.roles " +
	    		"INNER JOIN users.group_role ON group_role.role_id=roles.role_id " +
	    		"INNER JOIN users.user_group ON user_group.group_id=group_role.group_id " +
	    		" where users.user_group.user_id=?";
        Connection conn = null;
        UserRoleCollection roles = new UserRoleCollection();
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                UserRole role = new UserRole(rs.getString("role_name"));
                role.setRoleID(rs.getLong("role_id"));
                roles.add(role);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return roles;
    }

	@Override
	public Role getRoleByID(Long id) {
	    String sql = "SELECT * FROM users.roles where roles.role_id=?;";
	    return this.jdbcTemplate.queryForObject(sql, new Object[] { id }, new BeanPropertyRowMapper(Role.class));
	}
	
    @Override
    public void addRole(Role role) {
                
        String sql = "INSERT INTO users.roles(name,description) VALUES (?,?);";
        this.save(sql, role);
    }

    @Override
    public List<Role> getAllRoles() {
        String sql = "SELECT * FROM users.roles";
        List<Role> roles  = this.jdbcTemplate.query(sql,new BeanPropertyRowMapper(Role.class));
        return roles;
    }
}
