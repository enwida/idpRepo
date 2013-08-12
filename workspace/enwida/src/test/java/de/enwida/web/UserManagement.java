package de.enwida.web;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.Calendar;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.IGroupDao;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.dao.interfaces.IRoleDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.Group;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.implementation.MailServiceImpl;
import de.enwida.web.service.interfaces.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = false)
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

	private static org.apache.log4j.Logger logger = Logger
			.getLogger(AdminController.class);

	private User user;

	@Test
	@Transactional
	public void createUser() throws Exception {
		user = new User("test", "q12wq12w", "test", "test", true);
		user.setJoiningDate(new Date(Calendar.getInstance().getTimeInMillis()));
		user.setCompanyName("enwida.de");
		user=userDao.fetchByName(user.getUsername());
		userDao.enableDisableUser(user.getUserID(), false);
		userDao.enableDisableUser(user.getUserID(), true);
		userDao.save(user);
	}

	   @Test
	    @Transactional
	public void testGroup(){
	        user=userDao.fetchByName("test");
		Group adminGroup = new Group("Admin");
		adminGroup =groupDao.addGroup(adminGroup);

		Group anonymousGroup = new Group("Anonymous");
		anonymousGroup = groupDao.addGroup(anonymousGroup);

		// save user in any group and remove it
		 userService.assignUserToGroup(user.getUserID(),adminGroup.getGroupID());
		 
		// save user in anonymous group
		 userService.assignUserToGroup(user.getUserID(),anonymousGroup.getGroupID());

        //CheckGroups
        Assert.assertTrue(anonymousGroup.getAssignedUsers().contains(user));
        Assert.assertTrue(adminGroup.getAssignedUsers().contains(user));
	}

    @Test
    @Transactional
	public void updateUser() throws Exception {
		user=userDao.fetchByName("test");
		user.setCompanyName("test");
		userDao.updateUser(user);
        userDao.save(user);
		User user2 = userDao.fetchByName(user.getUserName());
		assertEquals("test", user2.getCompanyName());
	}

    @Test
    @Transactional
	public void testMail() throws Exception {
		mailService.SendEmail("olcaytarazan@gmail.com", "User Management Test","Ignore");
	}
	    
    @Test
    @Transactional
	public void testRole()  {
	    //Create roles
		Role adminRole = new Role("Admin");
		Role anonymousRole = new Role("Anonymous");
		Role testRole = new Role("Test");
		//Add roles
		adminRole=roleDao.addRole(adminRole);
		anonymousRole=roleDao.addRole(anonymousRole);
		testRole=roleDao.addRole(testRole);
 
		//Get Groups
        Group adminGroup = new Group("Admin");
        adminGroup =groupDao.addGroup(adminGroup);

        Group anonymousGroup = new Group("Anonymous");
        anonymousGroup = groupDao.addGroup(anonymousGroup);

        //Assign
        userService.assignRoleToGroup(adminRole.getRoleID(), adminGroup.getGroupID());
        userService.assignRoleToGroup(anonymousRole.getRoleID(), anonymousGroup.getGroupID());
        
        
        //CheckRoles
        Assert.assertTrue(adminGroup.getAssignedRoles().contains(adminRole));
        Assert.assertTrue(anonymousGroup.getAssignedRoles().contains(anonymousRole));
		
	}
    @Test
    @Transactional
	public void testRight() throws Exception {

		Right right1 = new Right();
		Right right2 = new Right();
		rightDao.addRight(right1);
		rightDao.addRight(right2);
        rightDao.enableDisableAspect(right1.getRightID(), true);
		rightDao.removeRight(right1);
		rightDao.removeRight(right2);

	}
}
