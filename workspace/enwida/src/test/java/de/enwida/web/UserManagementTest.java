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
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.implementation.MailServiceImpl;
import de.enwida.web.service.interfaces.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class UserManagementTest {
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
        //mailService.SendEmail("olcaytarazan@gmail.com", "User Management Test","Ignore");
    }
    
    @Test
    public void createInitialValues() throws Exception{
        //Get user
        User user=userService.fetchUser("test");
        //if there is no test user create it
        if(user==null){
              user = new User("jsdfjsdg@gmail.com", "test", "secret", "test", "test", true);
              user.setCompanyName("enwida.de");
              userService.saveUser(user);
        }
        //create admin group
        userService.saveGroup(new Group("admin"));
        Group adminGroup=userService.fetchGroup("admin");
        //Check if group is saved
        if(adminGroup==null){
            //this is not expected
            throw new Exception("Group is not saved");
        }
        //Assign group to user
        userService.assignGroupToUser(user, adminGroup);
        
        //create anonymous group
        userService.saveGroup(new Group("anonymous"));
        Group anonymousGroup=userService.fetchGroup("anonymous");
        if(anonymousGroup==null){
            //this is not expected
            throw new Exception("Group is not saved");
        }
        //Assign group to user
        userService.assignGroupToUser(user, anonymousGroup);
        
        //Create Admin role
        userService.saveRole(new Role("admin"));
        Role adminRole=userService.fetchRole("admin");
        if(adminRole==null){
            //this is not expected
            throw new Exception("AdminRole is not saved");
        }
        
        //Assign group to role
        userService.assignRoleToGroup(adminRole, adminGroup);
        
        //Create Anonymous role
        userService.saveRole(new Role("anonymous"));
        Role anonymousRole=userService.fetchRole("anonymous");
        if(anonymousRole==null){
            //this is not expected
            throw new Exception("AnonymousRole is not saved");
        }
        //Assign group to role
        userService.assignRoleToGroup(anonymousRole, anonymousGroup);
    }

 }

