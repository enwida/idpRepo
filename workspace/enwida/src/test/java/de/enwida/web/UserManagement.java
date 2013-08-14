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
import de.enwida.web.service.implementation.MailServiceImpl;
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

	@Autowired
	private MailServiceImpl mailService;
	
	/**
	 * Basic test were factored out to BasicUserManagement
	 * Test the hard stuff here ;)
	 */

    @Test
	public void testMail() throws Exception {
		mailService.SendEmail("olcaytarazan@gmail.com", "User Management Test","Ignore");
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

 }

