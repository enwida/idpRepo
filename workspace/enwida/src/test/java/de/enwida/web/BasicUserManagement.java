package de.enwida.web;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.enwida.web.dao.interfaces.IGroupDao;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.dao.interfaces.IRoleDao;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.model.Group;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class BasicUserManagement {
	private Logger logger = Logger.getLogger(getClass());
    
    @Autowired
    private IUserService userService;
	@Autowired
	private IGroupDao groupDao;
	@Autowired
	private IRoleDao roleDao;
	@Autowired
	private IRightDao rightDao;

	@Before
	public void cleanup() throws Exception {
		for (final User user : userService.getAllUsers()) {
			userService.deleteUser(user.getUserId());
		}
	}
	
	
	/***
	 ***** Basic persistence tests
	 ***/
	
	@Test
	public void userIsSaved() throws Exception {
		final User user = saveTestUser("testuser");
		final User testee = userService.getUser("testuser");
		
		// Instance is fresh from database
		Assert.assertFalse(user == testee);

		Assert.assertNotNull(testee);
		Assert.assertEquals(user, testee);
		Assert.assertEquals(user.getUserId(), testee.getUserId());
		Assert.assertEquals(user.getUserName(), testee.getUserName());
		Assert.assertEquals(user.getPassword(), testee.getPassword());
		Assert.assertEquals(user.getFirstName(), testee.getFirstName());
		Assert.assertEquals(user.getLastName(), testee.getLastName());
		Assert.assertEquals(user.getCompanyName(), testee.getCompanyName());
		Assert.assertEquals(user.getActivationKey(), testee.getActivationKey());
	}
	
	@Test
	public void groupIsSaved() throws Exception {
		final Group group = saveTestGroup("testgroup");
		final Group testee = userService.getGroup("testgroup");
		
		// Instance is fresh from database
		Assert.assertFalse(group == testee);
		
		Assert.assertNotNull(testee);
		Assert.assertEquals(group, testee);
		Assert.assertEquals(group.getGroupID(), testee.getGroupID());
		Assert.assertEquals(group.getGroupName(), testee.getGroupName());
	}
	
	@Test
	public void roleIsSaved() throws Exception {
		final Role role = saveTestRole("testrole");
		final Role testee = userService.getRole("testrole");
		
		// Instance if fresh from database
		Assert.assertFalse(role == testee);
		
		Assert.assertNotNull(testee);
		Assert.assertEquals(role, testee);
		Assert.assertEquals(role.getRoleID(), testee.getRoleID());
		Assert.assertEquals(role.getRoleName(), testee.getRoleName());
	}

	/***
	 ***** Users <-> Groups relationship tests
	 ***/

	@Test
	public void addGroupsToUser() throws Exception {
		final User user = saveTestUser("testuser");
		final Group group1 = saveTestGroup("testgroup1");
		final Group group2 = saveTestGroup("testgroup2");
		
		Assert.assertTrue(user.getGroups().isEmpty());
		
		final Group freshGroup1 = userService.assignGroupToUser(user, group1);
		final Group freshGroup2 = userService.assignGroupToUser(user, group2);
		
		// Groups got added to user object
		Assert.assertEquals(2, user.getGroups().size());
		Assert.assertTrue(user.getGroups().contains(group1));
		Assert.assertTrue(user.getGroups().contains(group2));
		
		// User got added to fresh group objects
		Assert.assertEquals(1, freshGroup1.getAssignedUsers().size());
		Assert.assertTrue(freshGroup1.getAssignedUsers().contains(user));
		Assert.assertEquals(1, freshGroup2.getAssignedUsers().size());
		Assert.assertTrue(freshGroup2.getAssignedUsers().contains(user));
		
		// User is NOT added to stale groups objects
		Assert.assertTrue(group1.getAssignedUsers().isEmpty());
		Assert.assertTrue(group2.getAssignedUsers().isEmpty());
		
		// Groups got added to freshly fetched user object
		final User fetchedUser = userService.getUser("testuser");
		Assert.assertEquals(2, fetchedUser.getGroups().size());
		Assert.assertTrue(fetchedUser.getGroups().contains(group1));
		Assert.assertTrue(fetchedUser.getGroups().contains(group2));
		
		// User got added to freshly fetched group objects
		final Group fetchedGroup1 = userService.getGroup("testgroup1");
		final Group fetchedGroup2 = userService.getGroup("testgroup2");

		Assert.assertEquals(1, fetchedGroup1.getAssignedUsers().size());
		Assert.assertTrue(fetchedGroup1.getAssignedUsers().contains(user));
		Assert.assertEquals(1, fetchedGroup2.getAssignedUsers().size());
		Assert.assertTrue(fetchedGroup2.getAssignedUsers().contains(user));
	}
	
	@Test
	public void addGroupTwice() throws Exception {
		final User user = saveTestUser("testuser");
		final Group group1 = saveTestGroup("testgroup1");
		
		userService.assignGroupToUser(user, group1);

		// Second add succeeds due to set semantics
		final Group freshGroup1 = userService.assignGroupToUser(user, group1);
		
		Assert.assertEquals(1, user.getGroups().size());
		Assert.assertEquals(1, freshGroup1.getAssignedUsers().size());
	}
	
	@Test
	public void addNonPersistedGroup() throws Exception {
		final User user = saveTestUser("testuser");

		// Group is not persisted 
		final Group group = new Group("testgroup1");
		
		// Assigning it should cause an exception
		try {
			userService.assignGroupToUser(user, group);
			throw new Exception("Shouldn't be reachable; IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// Expected
			Assert.assertTrue(e.getMessage().toLowerCase().contains("persisted"));
		}
	}
	
	@Test
	public void revokeGroupsFromUser() throws Exception {
		final User user = saveTestUser("testuser");
		final Group group1 = saveTestGroup("testgroup1");
		final Group group2 = saveTestGroup("testgroup2");
		
		userService.assignGroupToUser(user, group1);
		userService.assignGroupToUser(user, group2);
		
		final Group freshGroup1 = userService.revokeUserFromGroup(user, group1);
		
		// Group was removed from user object
		Assert.assertEquals(1, user.getGroups().size());
		Assert.assertTrue(user.getGroups().contains(group2));

		// User is removed from fresh group object
		Assert.assertTrue(freshGroup1.getAssignedUsers().isEmpty());

		// Group is removed from freshly fetched user object
		final User fetchedUser = userService.getUser("testuser");
		Assert.assertEquals(1, fetchedUser.getGroups().size());
		Assert.assertTrue(fetchedUser.getGroups().contains(group2));
		
		// User is removed from freshly fetched group object
		final Group fetchedGroup = userService.getGroup("testgroup1");
		Assert.assertTrue(fetchedGroup.getAssignedUsers().isEmpty());
	}
	
	@Test
	public void doNotAllowDirectModificationOfUserGroupsRelationship() throws Exception {
		final User user = saveTestUser("testuser");
		final Group group = saveTestGroup("testgroup");
		
		try {
			user.getGroups().add(group);
			throw new Exception("Shouldn't be reachable; UnsupportedOperationException expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
		
		try {
			group.getAssignedUsers().add(user);
			throw new Exception("Shouldn't be reachable; UnsupportedOperationException expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
	}
	

	/***
	 ***** Groups <-> Roles relationship tests
	 ***/

	@Test
	public void addRolesToGroup() throws Exception {
		final Group group = saveTestGroup("testgroup");
		final Role role1 = saveTestRole("testrole1");
		final Role role2 = saveTestRole("testrole2");
		
		Assert.assertTrue(group.getAssignedRoles().isEmpty());
		
		final Role freshRole1 = userService.assignRoleToGroup(role1, group);
		final Role freshRole2 = userService.assignRoleToGroup(role2, group);
		
		// Roles were added to group object
		Assert.assertEquals(2, group.getAssignedRoles().size());
		Assert.assertTrue(group.getAssignedRoles().contains(role1));
		Assert.assertTrue(group.getAssignedRoles().contains(role2));
		
		// Group was added to fresh role objects
		Assert.assertEquals(1, freshRole1.getAssignedGroups().size());
		Assert.assertTrue(freshRole1.getAssignedGroups().contains(group));
		Assert.assertEquals(1, freshRole2.getAssignedGroups().size());
		Assert.assertTrue(freshRole2.getAssignedGroups().contains(group));
		
		// Group was NOT added to stale role objects
		Assert.assertTrue(role1.getAssignedGroups().isEmpty());
		Assert.assertTrue(role2.getAssignedGroups().isEmpty());
		
		// Roles were added to freshly fetched group object
		final Group fetchedGroup = userService.getGroup("testgroup");
		Assert.assertEquals(2, fetchedGroup.getAssignedRoles().size());
		Assert.assertTrue(fetchedGroup.getAssignedRoles().contains(role1));
		Assert.assertTrue(fetchedGroup.getAssignedRoles().contains(role2));
		
		// Group was added to freshly fetched role objects
		final Role fetchedRole1 = userService.getRole("testrole1");
		final Role fetchedRole2 = userService.getRole("testrole2");
		
		Assert.assertEquals(1, fetchedRole1.getAssignedGroups().size());
		Assert.assertTrue(fetchedRole1.getAssignedGroups().contains(group));
		Assert.assertEquals(1, fetchedRole2.getAssignedGroups().size());
		Assert.assertTrue(freshRole2.getAssignedGroups().contains(group));
	}
	
	@Test
	public void addRoleTwice() throws Exception {
		final Group group = saveTestGroup("testgroup");
		final Role role = saveTestRole("testrole");
		
		userService.assignRoleToGroup(role, group);

		// Second add succeeds due to set semantics
		final Role freshRole = userService.assignRoleToGroup(role, group);
		
		Assert.assertEquals(1, group.getAssignedRoles().size());
		Assert.assertEquals(1, freshRole.getAssignedGroups().size());
	}
	
	@Test
	public void addNonPersistedRole() throws Exception {
		final Group group = new Group("testgroup");

		// Role is not persisted 
		final Role role = new Role("testrole");
		
		// Assigning it should cause an exception
		try {
			userService.assignRoleToGroup(role, group);
			throw new Exception("Shouldn't be reachable; IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// Expected
			Assert.assertTrue(e.getMessage().toLowerCase().contains("persisted"));
		}
	}
	
	@Test
	public void revokeRolesFromUser() throws Exception {
		final Group group = saveTestGroup("testgroup");
		final Role role1 = saveTestRole("testrole1");
		final Role role2 = saveTestRole("testrole2");
		
		userService.assignRoleToGroup(role1, group);
		userService.assignRoleToGroup(role2, group);
		
		final Role freshRole1 = userService.revokeRoleFromGroup(role1, group);
		
		// Role was removed from group object
		Assert.assertEquals(1, group.getAssignedRoles().size());
		Assert.assertTrue(group.getAssignedRoles().contains(role2));

		// Group is removed from fresh role object
		Assert.assertTrue(freshRole1.getAssignedGroups().isEmpty());

		// Role is removed from freshly fetched group object
		final Group fetchedGroup = userService.getGroup("testgroup");
		Assert.assertEquals(1, fetchedGroup.getAssignedRoles().size());
		Assert.assertTrue(fetchedGroup.getAssignedRoles().contains(role2));
		
		// User is removed from freshly fetched group object
		final Role fetchedRole1 = userService.getRole("testrole1");
		Assert.assertTrue(fetchedRole1.getAssignedGroups().isEmpty());
	}
	
	@Test
	public void doNotAllowDirectModificationOfGroupRoleRelationship() throws Exception {
		final Group group = saveTestGroup("testgroup");
		final Role role = saveTestRole("testrole");
		
		try {
			group.getAssignedRoles().add(role);
			throw new Exception("Shouldn't be reachable; UnsupportedOperationException expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
		
		try {
			role.getAssignedGroups().add(group);
			throw new Exception("Shouldn't be reachable; UnsupportedOperationException expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
	}
	    private User saveTestUser(String name) throws Exception {
		final User user = new User(name, "secret", "test", "test", true);
		user.setCompanyName("enwida.de");
		userService.saveUser(user);
		return user;
    }
    
    private Group saveTestGroup(String name) throws Exception {
    	final Group group = new Group(name);
    	userService.addGroup(group);
    	return group;
    }
    
    private Role saveTestRole(String name) throws Exception {
    	final Role role = new Role(name);
    	userService.addRole(role);
    	return role;
    }

	private Right addTestRight(long id) throws Exception {
		Right right = rightDao.fetchById(id);
		if (right == null) {
			Calendar cal1 = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			right = new Right(99, 110, "WEEKLY", new CalendarRange(cal1, cal2),
					"POWER", true);
		}
		rightDao.addRight(right);
		return right;
	}
 }
