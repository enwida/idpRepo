package de.enwida.web;

import java.util.Calendar;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
public class UserManagement {
	private Logger logger = Logger.getLogger(getClass());
    
    @Autowired
    private IUserService userService;
	@Autowired
	private IGroupDao groupDao;
	@Autowired
	private IRoleDao roleDao;
	@Autowired
	private IRightDao rightDao;

	private User user;
	private Group group;
	private Role role;
	private Right right;

	@Test
	public void userGroupTest() throws Exception {
		user = addTestUser("testuser");
		group = addTestGroup("testgroup1");
		Group group2 = addTestGroup("testgroup2");

		assignUserToGroup(user, group);
		assignUserToGroup(user, group2);
		
		// removeUserFromGroup(user, group);
		// removeUserFromGroup(user, group2);

	}

	private void assignUserToGroup(User user, Group group) {
		userService.assignUserToGroup(user, group);
		group = groupDao.fetchByName(group.getGroupName());
		System.out.println(user.getGroups());
		System.out.println(group.getAssignedUsers());
	}

	private void removeUserFromGroup(User user, Group group) {
		userService.revokeUserFromGroup(user, group);
		group = groupDao.fetchByName(group.getGroupName());
		System.out.println(user.getGroups());
		System.out.println(group.getAssignedUsers());
	}

	@Test
	public void groupRoleTest() throws Exception {
		user = addTestUser("testuser");
		group = addTestGroup("testgroup1");
		role = addTestRole("testrole1");
		Role role2 = addTestRole("testrole2");
		Role role3 = addTestRole("testrole3");

		assignUserToGroup(user, group);
		assignRoleToGroup(group, role);
		assignRoleToGroup(group, role2);
		assignRoleToGroup(group, role3);

		removeRoleFromGroup(group, role2);

	}

	private void assignRoleToGroup(Group group, Role role) {
		userService.assignRoleToGroup(role, group);
		group = groupDao.fetchByName(group.getGroupName());
		System.out.println(role.getAssignedGroups());
		System.out.println(group.getAssignedRoles());
	}

	private void removeRoleFromGroup(Group group, Role role) {
		userService.revokeRoleFromGroup(role, group);
		group = groupDao.fetchByName(group.getGroupName());
		System.out.println(role.getAssignedGroups());
		System.out.println(group.getAssignedRoles());
	}

	@Test
	public void roleRightTest() throws Exception {
		role = addTestRole("testrole1");
		right = addTestRight(1);
		Right right2 = addTestRight(2);
		Right right3 = addTestRight(3);

		assignRightToRole(right, role);
		assignRightToRole(right2, role);
		assignRightToRole(right3, role);

		removeRightFromRole(right2, role);
		user = userService.getUser("testuser");
		logger.debug("Authorities : " + user.getAuthorities());
	}

	private void assignRightToRole(Right right, Role role) {
		userService.assignRightToRole(right, role);
		right = rightDao.fetchById(right.getRightID());
		System.out.println(role.getRights());
		System.out.println(right.getRole());
	}

	private void removeRightFromRole(Right right, Role role) {
		userService.revokeRightFromRole(right, role);
		right = rightDao.fetchById(right.getRightID());
		System.out.println(role.getRights());
		System.out.println(right.getRole());
	}

	@Test
	@Ignore
	// @Transactional
	public void userIsAddedToDatabase() throws Exception {
		final String userName = "test";
		final User user = addTestUser(userName);
		
		final User testee = userService.getUser(userName);
		// userDao.refresh(testee);
		Assert.assertNotNull(testee);
		Assert.assertEquals(testee.getUsername(), user.getUserName());
		Assert.assertEquals(testee.getCompanyName(), user.getCompanyName());
		Assert.assertEquals(testee.getFirstName(), user.getFirstName());
		Assert.assertEquals(testee.getLastName(), user.getLastName());
		Assert.assertEquals(testee.getPassword(), user.getPassword());
	}
	
	@Test
	@Ignore
	public void groupIsAddedToDatabase() throws Exception {
		final String groupName = "test";
		final Group group = addTestGroup(groupName);
		
		// final Group testee =
		// userService.getGroupDao().fetchByName(groupName);
		// Assert.assertNotNull(testee);
		// Assert.assertEquals(group.getGroupName(), testee.getGroupName());
	}
	
	// @Test
	@Transactional
	@Ignore
	public void cannotModifyGroupsOfUserDirectly() throws Exception {
		final User user = addTestUser("test");
		final Group group = addTestGroup("test");
		
		// Assert.assertTrue(user.getGroups().isEmpty());
		// Assert.assertTrue(group.getAssignedUsers().isEmpty());
		
		try {
			user.getGroups().add(group); // should throw
			// throw new Exception("Read-only list expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
		
		// userService.assignUserToGroup(user, group);
		Assert.assertTrue(user.getGroups().size() == 1);

		try {
			// user.getGroups().clear();
			// throw new Exception("Read-only list expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}	
	}	

	@Test
	@Ignore
	public void cannotModifyUsersOfGroupDirectly() throws Exception {
		final User user = addTestUser("test");
		final Group group = addTestGroup("test");
		
		Assert.assertTrue(user.getGroups().isEmpty());
		Assert.assertTrue(group.getAssignedUsers().isEmpty());
		
		try {
			group.getAssignedUsers().add(user); // should throw
			// throw new Exception("Read-only list expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
		
		// userService.assignUserToGroup(user, group);
		Assert.assertTrue(group.getAssignedUsers().size() == 1);

		try {
			// group.getAssignedUsers().clear();
			// throw new Exception("Read-only list expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}	
	}
	
	@Test
	@Ignore
	public void groupsAreAssignedToUser() throws Exception {
		final User user = addTestUser("test");
		final Group group1 = addTestGroup("test1");
		final Group group2 = addTestGroup("test2");
		
		// Assert.assertTrue(user.getGroups().isEmpty());
		// Assert.assertTrue(group1.getAssignedUsers().isEmpty());
		// Assert.assertTrue(group2.getAssignedUsers().isEmpty());
		
		// userService.assignUserToGroup(user, group1);
		// userService.assignUserToGroup(user, group2);

		// Assert.assertTrue(user.getGroups().size() == 2);
		Assert.assertTrue(user.getGroups().contains(group1));
		Assert.assertTrue(user.getGroups().contains(group2));
		
		Assert.assertTrue(group1.getAssignedUsers().size() == 1);
		Assert.assertTrue(group1.getAssignedUsers().contains(user));

		Assert.assertTrue(group2.getAssignedUsers().size() == 1);
		Assert.assertTrue(group2.getAssignedUsers().contains(user));
		
		// Check with fresh instances
		
		final User freshUser = userService.getUser("test");
		// userDao.refresh(freshUser);
		// Assert.assertTrue(freshUser.getGroups().size() == 2);
		Assert.assertTrue(user.getGroups().contains(group1));
		Assert.assertTrue(user.getGroups().contains(group2));

		// final Group freshGroup1 =
		// daoService.getGroupDao().fetchByName("test1");
		// // groupDao.refresh(freshGroup1);
		// Assert.assertTrue(freshGroup1.getAssignedUsers().size() == 1);
		// Assert.assertTrue(group1.getAssignedUsers().contains(user));
		//
		// final Group freshGroup2 =
		// daoService.getGroupDao().fetchByName("test2");
		// // groupDao.refresh(freshGroup2);
		// Assert.assertTrue(freshGroup2.getAssignedUsers().size() == 1);
		// Assert.assertTrue(group2.getAssignedUsers().contains(user));
	}
	
    @Test
    @Transactional
    public void roleIsAddedToDatabase() throws Exception {
        final String roleName = "test";
        final Role role = addTestRole(roleName);

        final Role testee = roleDao.fetchByName(roleName);
        roleDao.refresh(testee);
        Assert.assertNotNull(testee);
        Assert.assertEquals(role.getRoleName(), testee.getRoleName());
    }
    
    @Test
    @Transactional
    public void createTestUser() throws Exception {
        //Create an admin user to use for testing
        addTestUser("test");
        user=userService.getUser("test");
        //create required groups
        addTestGroup("admin");
        addTestGroup("anonymous");
        //create required roles
        addTestRole("admin");
        addTestRole("anonymous");
        assignUserToGroup(user,  userService.findGroup(new Group("admin")));
        groupDao.refresh(new Group("admin"));
//        Assert.assertTrue(testee.getRoles().contains(new Role("admin")));
    }

//
//    @Test
//    @Transactional
//	public void testMail() throws Exception {
//		mailService.SendEmail("olcaytarazan@gmail.com", "User Management Test","Ignore");
//	}
//	    
//    @Test
//    @Transactional
//	public void testRole()  {
//	    //Create roles
//		Role adminRole = new Role("Admin");
//		Role anonymousRole = new Role("Anonymous");
//		Role testRole = new Role("Test");
//		//Add roles
//		adminRole=roleDao.addRole(adminRole);
//		anonymousRole=roleDao.addRole(anonymousRole);
//		testRole=roleDao.addRole(testRole);
// 
//		//Get Groups
//        Group adminGroup = new Group("Admin");
//        adminGroup =groupDao.addGroup(adminGroup);
//
//        Group anonymousGroup = new Group("Anonymous");
//        anonymousGroup = groupDao.addGroup(anonymousGroup);
//
//        //Assign
//        userService.assignRoleToGroup(adminRole.getRoleID(), adminGroup.getGroupID());
//        userService.assignRoleToGroup(anonymousRole.getRoleID(), anonymousGroup.getGroupID());
//        
//        
//        //CheckRoles
//        Assert.assertTrue(adminGroup.getAssignedRoles().contains(adminRole));
//        Assert.assertTrue(anonymousGroup.getAssignedRoles().contains(anonymousRole));
//		
//	}
//    @Test
//    @Transactional
//	public void testRight() throws Exception {
//
//		Right right1 = new Right();
//		Right right2 = new Right();
//		rightDao.addRight(right1);
//		rightDao.addRight(right2);
//        rightDao.enableDisableAspect(right1.getRightID(), true);
//		rightDao.removeRight(right1);
//		rightDao.removeRight(right2);
//
//	}
    
    private User addTestUser(String name) throws Exception {
		User user = userService.getUser(name);
		if (user == null) {
			user = new User(name, "secret", "test", "test", true);
		}
		user.setCompanyName("enwida.de");
		userService.saveUser(user);
		return user;
    }
    
    private Group addTestGroup(String name) throws Exception {
		Group group = groupDao.fetchByName(name);
		if (group == null) {
			group = new Group(name);
		}
		groupDao.addGroup(group);
    	return group;
    }
    
	private Role addTestRole(String name) throws Exception {
		Role role = roleDao.fetchByName(name);
		if (role == null) {
			role = new Role(name);
		}
		roleDao.save(role);
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
