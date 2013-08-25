package de.enwida.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.model.Group;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.Constants;

@Transactional
public class TestUtils {
	
	@Autowired
	protected DataSource dataSource;
	
	@Autowired
	protected IUserService userService;

	public void cleanupDatabase() throws Exception {
		for (final Right right : userService.fetchAllRights()) {
			userService.deleteRight(right.getRightID());
		}
		for (final Role role : userService.fetchAllRoles()) {
			userService.deleteRole(role.getRoleID());
		}
		for (final Group group : userService.fetchAllGroups()) {
			userService.deleteGroup(group.getGroupID());
		}
		for (final User user : userService.fetchAllUsers()) {
			userService.deleteUser(user.getUserId());
		}
	}
	
	public static void cleanupDatabase(Connection connection) throws Exception {
		clearTable(connection, Constants.RIGHT_TABLE_SCHEMA_NAME + "." + Constants.RIGHT_TABLE_NAME);
		clearTable(connection, Constants.ROLE_TABLE_SCHEMA_NAME + "." + Constants.ROLE_TABLE_NAME);
		clearTable(connection, Constants.GROUP_TABLE_SCHEMA_NAME + "." + Constants.GROUP_TABLE_NAME);
		clearTable(connection, Constants.USER_TABLE_SCHEMA_NAME + "." + Constants.USER_TABLE_NAME);
	}
	
	public static void clearTable(Connection connection, String tableName) throws Exception {
		PreparedStatement stmt = connection.prepareStatement("TRUNCATE " + tableName + " CASCADE");
		stmt.execute();
	}
	
	public static void recreateUsersSchema(Connection connection) throws Exception {
		final PreparedStatement stmt = connection.prepareStatement("DROP SCHEMA ? CASCADE; CREATE SCHEMA ?");
		stmt.setString(1, Constants.USERS_SCHEMA_NAME);
		stmt.setString(2, Constants.USERS_SCHEMA_NAME);

	}
	
    public User saveTestUser(String name) throws Exception {
		final User user = new User(name + "@pleasedontsendmailshere.com", name, "secret", "test", "test", true);
		user.setCompanyName("enwida.de");
		userService.saveUser(user,false);
		return user;
    }
    
    public Group saveTestGroup(String name) throws Exception {
    	final Group group = new Group(name);
    	userService.saveGroup(group);
    	return group;
    }
    
    public Role saveTestRole(String name) throws Exception {
    	final Role role = new Role(name);
    	userService.saveRole(role);
    	return role;
    }
    
    public Right saveTestRight(int product) throws Exception {
    	final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	final CalendarRange timeRange = new CalendarRange(dateFormat.parse("2010-01-01"), dateFormat.parse("2012-01-01"));

    	final Right right = new Right(99, product, DataResolution.MONTHLY.toString(), timeRange, Aspect.CR_VOL_ACTIVATION.toString(), true);
    	userService.saveRight(right);
    	return right;
    }
	
	public User saveUserWithGroup(String username) throws Exception {
		final User user = new User("ab@tum.de", username, "secret", "test", "test", true);
		user.setCompanyName("enwida.de");
		userService.saveUser(user,false);
		
		final Group group = new Group("testgroup");
		userService.saveGroup(group);
		
		userService.assignGroupToUser(user, group);
		return user;
	}
	
	public Role saveRole(User user, String roleName) throws Exception {
		final Role role = new Role(roleName);
		userService.saveRole(role);
		return userService.assignRoleToGroup(role, user.getGroups().iterator().next());
	}
	
	public Role saveRight(User user, Right right) throws Exception {
		userService.saveRight(right);
		return userService.assignRightToRole(right, user.getAllRoles().get(0));
	}
	
	public Role saveRight(Role role, Right right) throws Exception {
		userService.saveRight(right);
		return userService.assignRightToRole(right, role);
	}

	
}
