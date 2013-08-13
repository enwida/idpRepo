package de.enwida.web;

import junit.framework.Assert;

import org.hibernate.cfg.beanvalidation.GroupsPerOperation;
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
	public void groupIsAssignedToUser() throws Exception {
		final User user = addTestUser("test");
		final Group group = addTestGroup("test");
		
		Assert.assertTrue(user.getGroups().isEmpty());
		Assert.assertTrue(group.getAssignedUsers().isEmpty());
		
		user.getGroups().add(group);
		// Changes only in memory
		Assert.assertTrue(group.getAssignedUsers().isEmpty());
		
		userDao.update(user);
		
		final User freshUser = userService.getUser("test");
		userDao.refresh(freshUser);
		Assert.assertTrue(freshUser.getGroups().size() == 1);
		Assert.assertEquals(freshUser.getGroups().get(0).getGroupName(), "test");

		final Group freshGroup = groupDao.fetchByName("test");
		groupDao.refresh(freshGroup);
		Assert.assertTrue(freshGroup.getAssignedUsers().size() == 1);
		Assert.assertEquals(freshGroup.getAssignedUsers().get(0).getUsername(), "test");
	}

	@Test
	@Transactional
	public void userIsAssignedToGroup() throws Exception {
		final User user = addTestUser("test");
		final Group group = addTestGroup("test");
		
		Assert.assertTrue(user.getGroups().isEmpty());
		Assert.assertTrue(group.getAssignedUsers().isEmpty());
		
		group.getAssignedUsers().add(user);
		// Changes only in memory
		Assert.assertTrue(user.getGroups().isEmpty());
		
		groupDao.update(group);

		final Group freshGroup = groupDao.fetchByName("test");
		groupDao.refresh(freshGroup);
		Assert.assertTrue(freshGroup.getAssignedUsers().size() == 1);
		Assert.assertEquals(freshGroup.getAssignedUsers().get(0).getUsername(), "test");
		
		final User freshUser = userService.getUser("test");
		userDao.refresh(freshUser);
		Assert.assertTrue(freshGroup.getAssignedUsers().size() == 1);
		Assert.assertEquals(freshUser.getGroups().get(0).getGroupName(), "test");
	}


//    @Test
//    @Transactional
//	public void testGroup(){
//        user=userDao.fetchByName("test");
//		Group adminGroup = new Group("Admin");
//		adminGroup =groupDao.addGroup(adminGroup);
//
//		Group anonymousGroup = new Group("Anonymous");
//		anonymousGroup = groupDao.addGroup(anonymousGroup);
//
//		// save user in any group and remove it
//		 userService.assignUserToGroup(user.getUserID(),adminGroup.getGroupID());
//		 
//		// save user in anonymous group
//		 userService.assignUserToGroup(user.getUserID(),anonymousGroup.getGroupID());
//
//        //CheckGroups
//        Assert.assertTrue(anonymousGroup.getAssignedUsers().contains(user));
//        Assert.assertTrue(adminGroup.getAssignedUsers().contains(user));
//	}
//
//    @Test
//    @Transactional
//	public void updateUser() throws Exception {
//		user=userDao.fetchByName("test");
//		user.setCompanyName("test");
//		userDao.updateUser(user);
//        userDao.save(user);
//		User user2 = userDao.fetchByName(user.getUserName());
//		assertEquals("test", user2.getCompanyName());
//	}
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
 }
