package de.enwida.web;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
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
@Transactional
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

	@Before
	public void createUser() throws Exception {
		user = new User("test", "test", "test", "test", true);
		user.setJoiningDate(new Date(Calendar.getInstance().getTimeInMillis()));
		user.setCompanyName("enwida.de");
		User existingUser = userDao.fetchByName(user.getUserName());
		if (existingUser == null) {
			user.setUserID(userDao.save(user));
		} else {
			user = existingUser;
		}
		userDao.enableDisableUser(user.getUserID(), false);
		userDao.enableDisableUser(user.getUserID(), true);
	}

    @After
    public void cleanUpTestCase() throws Exception {
        userDao.deleteUser(user);
        //Create Default Values
        Group adminGroup = new Group("Admin");
        groupDao.addGroup(adminGroup);

        Group anonymousGroup = new Group("Anonymous");
        groupDao.addGroup(anonymousGroup);
    }

	@Test
	public void saveUserInAGroup() throws Exception {
		Group adminGroup = new Group("Admin");
		groupDao.addGroup(adminGroup);

		Group anonymousGroup = new Group("Anonymous");
		groupDao.addGroup(anonymousGroup);

		// save user in any group and remove it
		 userService.assignUserToGroup(user.getUserID(),adminGroup.getGroupID());
		 userService.deassignUserFromGroup(user.getUserID(),adminGroup.getGroupID());
		 
		// save user in anonymous group
		 userService.assignUserToGroup(user.getUserID(),anonymousGroup.getGroupID());
	}

	@Test
	@Transactional
	public void updateUser() throws Exception {
		userDao.save(user);
		user.setCompanyName("test");
		userDao.updateUser(user);
        userDao.save(user);
		User user2 = userDao.fetchByName(user.getUserName());
		assertEquals("test", user2.getCompanyName());
	}

	@Test
	public void testMail() throws Exception {
		mailService.SendEmail("olcaytarazan@gmail.com", "User Management Test","Ignore");
	}

	@Test
	public void testRole() throws Exception {
	    //Create roles
		Role adminRole = new Role("Admin");
		Role anonymousRole = new Role("Anonymous");
		Role testRole = new Role("Test");
		//Add roles
		roleDao.addRole(adminRole);
		roleDao.addRole(anonymousRole);
		roleDao.addRole(testRole);
		//Test remove role method
		roleDao.removeRole(testRole);
	}

	@Test
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
