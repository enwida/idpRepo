package de.enwida.web;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

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
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

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
	private IGroupDao groupDao;

	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private IRightDao rightDao;

	private static org.apache.log4j.Logger logger = Logger
			.getLogger(AdminController.class);

	private User user;

	@Before
	public void testUser() throws Exception {
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
		// userDao.deleteUser(user);
	}

	@After
	public void cleanUpTestCase() throws Exception {
		// userDao.deleteUser(user);
	}

	@Test
	public void saveUserInAGroup() throws Exception {
		Group adminGroup = new Group("Admin");
		groupDao.addGroup(adminGroup);

		Group anonymousGroup = new Group("Anonymous");
		groupDao.addGroup(anonymousGroup);

		// save user in any group
		// userDao.assignUserToGroup(user.getUserID(),adminGroup.getGroupID());
		// userDao.deassignUserFromGroup(user.getUserID(),
		// adminGroup.getGroupID());
		// save user in anonymous group
		// userDao.assignUserToGroup(user.getUserID(),anonymousGroup.getGroupID());
		// userDao.deassignUserFromGroup(user.getUserID(),
		// anonymousGroup.getGroupID());
	}

	@Test
	public void updateUser() throws Exception {
		userDao.save(user);
		user.setCompanyName("test");
		userDao.updateUser(user);
		User user2 = userDao.fetchByName(user.getUserName());
		assertEquals("test", user2.getCompanyName());
		userDao.deleteUser(user);
		assertEquals(null, userDao.fetchByName(user.getUserName()));
	}

	@Test
	public void testMail() throws Exception {
		mailService.SendEmail("olcaytarazan@gmail.com", "User Management Test",
				"Ignore");
	}

	@Test
	public void testRole() throws Exception {
		Role adminRole = new Role("Admin");
		Role anonymousRole = new Role("Anonymous");
		Role testRole = new Role("Test");
		roleDao.addRole(adminRole);
		roleDao.addRole(anonymousRole);
		roleDao.addRole(testRole);
		roleDao.removeRole(testRole);
	}

	@Test
	public void testRight() throws Exception {
		Right right1 = new Right();
		Right right2 = new Right();
		rightDao.addRight(right1);
		rightDao.addRight(right2);
		rightDao.enableDisableAspect(right1.getRightID(), true);
		rightDao.enableDisableAspect(right1.getRightID(), false);
		rightDao.removeRight(right1);
		rightDao.removeRight(right2);

	}

	@Test
	public void SpringSecurtyLoginSQLCheck() throws Exception {

		String sql = getSpringSecurityQuery("//authentication-manager/authentication-provider/jdbc-user-service/@users-by-username-query");

		Connection conn = null;

		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUserName());
			ps.executeQuery();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	@Test
	public void SpringSecurtyAuthoritySQLCheck() throws Exception {

		String sql = getSpringSecurityQuery("//authentication-manager/authentication-provider/jdbc-user-service/@authorities-by-username-query");

		Connection conn = null;

		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUserName());
			ps.executeQuery();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	private String getSpringSecurityQuery(String xPathInFile) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document dDoc = builder
				.parse("src/main/webapp/WEB-INF/spring/security-app-context.xml");

		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodes = (NodeList) xPath.evaluate(xPathInFile, dDoc,
				XPathConstants.NODESET);
		return nodes.item(0).getTextContent();
	}

}
