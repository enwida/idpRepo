package de.enwida.web;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.enwida.web.dao.interfaces.IGroupDao;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.dao.interfaces.IRoleDao;
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

 }

