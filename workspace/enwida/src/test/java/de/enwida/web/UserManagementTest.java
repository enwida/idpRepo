package de.enwida.web;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.enwida.web.dao.implementation.UserDao;
import de.enwida.web.model.User;
import de.enwida.web.model.UserPermission;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class UserManagementTest {

	@Autowired
	private DriverManagerDataSource datasource;
	
	@Autowired
	UserDao userDao;

	@Test
	public void AddUser() {
//		User user=new User(100,"test","test","test","test",false);
//		userDao.createUser(user);
		
	}

	
	@Test
	public void SpringSecurtyLoginSQLCheck() {
		
	    String sql = "select user_name,user_password, enabled from users where user_name='test'";
		 
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeQuery();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
				conn.close();
				} catch (SQLException e) {}
			}
		}
	}
	
	@Test
	public void SpringSecurtyAuthoritySQLCheck() {
		
	    String sql = "SELECT users.user_name, roles.role_name FROM users" +
	    		" INNER JOIN user_group ON user_group.user_id=users.user_id " +
	    		" INNER JOIN group_role ON group_role.group_id=user_group.group_id" +
	    		" INNER JOIN roles ON roles.role_ID=group_role.role_id" +
	    		" WHERE user_name='test'";
		 
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeQuery();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
				conn.close();
				} catch (SQLException e) {}
			}
		}
	}

	@Test
	public void GetAllUser() {
//		List<User> users= userDao.findAllUsers();
//		assertEquals(true,!users.isEmpty());
	}

	
	@Test
	public void enableUser() {
		assertEquals(true,userDao.enableDisableUser(true, 1));
	}
	
	@Test
	public void disbleUser() {
		assertEquals(true,userDao.enableDisableUser(false, 1));
	}
	
	@Test
	public void DeleteUser() {
		User user=new User(100,"test","test","test","test",false);
		user.getUserPermissionCollection().implies(new UserPermission("admin"));
		assertEquals(true,userDao.deleteUser(user));
	}
}
