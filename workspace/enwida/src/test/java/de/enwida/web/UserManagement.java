package de.enwida.web;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.IGroupDao;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.dao.interfaces.IRoleDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.implementation.MailServiceImpl;
import de.enwida.web.service.interfaces.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
public class UserManagement {

	@Autowired
	private DriverManagerDataSource datasource;

	@Autowired
	private IUserDao userDao;

    @Autowired
    private MailServiceImpl mailService;
    
    @Autowired
    private IUserService userService;

	@Autowired
	private IGroupDao groupDao;

	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private IRightDao rightDao;

	@Test
	@Transactional
	public void userIsAddedToDatabase() throws Exception {
		final String userName = "test";
		final User user = addTestUser(userName);
		
		final User testee = userService.getUser(userName);
		userDao.refresh(testee);
		Assert.assertNotNull(testee);
		Assert.assertEquals(testee.getUsername(), user.getUserName());
		Assert.assertEquals(testee.getCompanyName(), user.getCompanyName());
		Assert.assertEquals(testee.getFirstName(), user.getFirstName());
		Assert.assertEquals(testee.getLastName(), user.getLastName());
		Assert.assertEquals(testee.getPassword(), user.getPassword());
	}
	
	@Test
	@Transactional
	public void groupIsAddedToDatabase() throws Exception {
		final String groupName = "test";
		final Group group = addTestGroup(groupName);
		
		final Group testee = groupDao.fetchByName(groupName);
		groupDao.refresh(testee);
		Assert.assertNotNull(testee);
		Assert.assertEquals(group.getGroupName(), testee.getGroupName());
	}
	
	@Test
	@Transactional
	public void cannotModifyGroupsOfUserDirectly() throws Exception {
		final User user = addTestUser("test");
		final Group group = addTestGroup("test");
		
		Assert.assertTrue(user.getGroups().isEmpty());
		Assert.assertTrue(group.getAssignedUsers().isEmpty());
		
		try {
			user.getGroups().add(group); // should throw
			throw new Exception("Read-only list expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
		
		userService.assignUserToGroup(user, group);
		Assert.assertTrue(user.getGroups().size() == 1);

		try {
			user.getGroups().clear();
			throw new Exception("Read-only list expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}	
	}	
	@Test
	@Transactional
	public void cannotModifyUsersOfGroupDirectly() throws Exception {
		final User user = addTestUser("test");
		final Group group = addTestGroup("test");
		
		Assert.assertTrue(user.getGroups().isEmpty());
		Assert.assertTrue(group.getAssignedUsers().isEmpty());
		
		try {
			group.getAssignedUsers().add(user); // should throw
			throw new Exception("Read-only list expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}
		
		userService.assignUserToGroup(user, group);
		Assert.assertTrue(group.getAssignedUsers().size() == 1);

		try {
			group.getAssignedUsers().clear();
			throw new Exception("Read-only list expected");
		} catch (UnsupportedOperationException e) {
			// Expected
		}	
	}
	
	@Test
	@Transactional
	public void groupsAreAssignedToUser() throws Exception {
		final User user = addTestUser("test");
		final Group group1 = addTestGroup("test1");
		final Group group2 = addTestGroup("test2");
		
		Assert.assertTrue(user.getGroups().isEmpty());
		Assert.assertTrue(group1.getAssignedUsers().isEmpty());
		Assert.assertTrue(group2.getAssignedUsers().isEmpty());
		
		userService.assignUserToGroup(user, group1);
		userService.assignUserToGroup(user, group2);

		Assert.assertTrue(user.getGroups().size() == 2);
		Assert.assertTrue(user.getGroups().contains(group1));
		Assert.assertTrue(user.getGroups().contains(group2));
		
		Assert.assertTrue(group1.getAssignedUsers().size() == 1);
		Assert.assertTrue(group1.getAssignedUsers().contains(user));

		Assert.assertTrue(group2.getAssignedUsers().size() == 1);
		Assert.assertTrue(group2.getAssignedUsers().contains(user));
		
		// Check with fresh instances
		
		final User freshUser = userService.getUser("test");
		userDao.refresh(freshUser);
		Assert.assertTrue(freshUser.getGroups().size() == 2);
		Assert.assertTrue(user.getGroups().contains(group1));
		Assert.assertTrue(user.getGroups().contains(group2));

		final Group freshGroup1 = groupDao.fetchByName("test1");
		groupDao.refresh(freshGroup1);
		Assert.assertTrue(freshGroup1.getAssignedUsers().size() == 1);
		Assert.assertTrue(group1.getAssignedUsers().contains(user));

		final Group freshGroup2 = groupDao.fetchByName("test2");
		groupDao.refresh(freshGroup2);
		Assert.assertTrue(freshGroup2.getAssignedUsers().size() == 1);
		Assert.assertTrue(group2.getAssignedUsers().contains(user));
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
    	final User user = new User(name, "secret", "test", "test", true);
		user.setCompanyName("enwida.de");
		userService.saveUser(user);
		return user;
    }
    
    private Group addTestGroup(String name) throws Exception {
        final Group group = new Group(name);
        userService.addGroup(group);
        return group;
    }
    
    private Role addTestRole(String name) throws Exception {
        final Role role = new Role(name);
        userService.addRole(role);
        return role;
    }
 }
