package de.enwida.web;

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
import org.junit.Before;
import org.junit.Ignore;
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
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.Group;
import de.enwida.web.model.User;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = false)
@Transactional
public class UserManagementTest {

	@Autowired
	private DriverManagerDataSource datasource;
	
	@Autowired
	private IUserDao userDao;

	private static Logger logger = Logger.getLogger(AdminController.class);

	
	private User user;
	
	@Before
	public void setUp() {
	}
	
	 @Test
	public void userTest() {
		createTestUser();
		findUser();
		// deleteTestUser();
	}

	private void createTestUser() {
		user = new User(0, "username1", "password1", "firstname", "lastname",
				false);
		user.setJoiningDate(new Date(Calendar.getInstance().getTimeInMillis()));
		user.setCompanyName("enwida.de");
		userDao.save(user);

		User user1 = new User(0, "username2", "password2", "firstname",
				"lastname", false);
		userDao.save(user1);
		// userDao.deleteUser(user);

	}

	private User findUser() {
		return userDao.getUserByName(user.getUserName());
	}

	private void deleteTestUser() {

	}

	@Test
	@Ignore
    public void HibernateTest() {
		// Session session = HibernateUtil.getSessionFactory().openSession();
		// session.beginTransaction();
        user.setJoiningDate(new Date(Calendar.getInstance().getTimeInMillis()));
        user.setCompanyName("enwida.de");
		userDao.save(user);
		// session.getTransaction().commit();
    }
	
   @Test
	@Ignore
    public void saveUserGroup() {
       Group group = userDao.getAllGroups().get(0);
       //save user in any group
       userDao.assignUserToGroup(user.getUserID(),group.getGroupID());
       userDao.deassignUserFromGroup(user.getUserID(), group.getGroupID());
       //save user in anonymous group
       group =userDao.getGroupByName("anonymous");
       userDao.assignUserToGroup(user.getUserID(),group.getGroupID());
       userDao.deassignUserFromGroup(user.getUserID(), group.getGroupID());
    }

	@Test
	@Ignore
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
	@Ignore
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
	
	
	private String getSpringSecurityQuery(String xPathInFile) throws Exception{
	    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document dDoc = builder.parse("src/main/webapp/WEB-INF/spring/security-app-context.xml");

        XPath xPath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList) xPath.evaluate(xPathInFile, dDoc, XPathConstants.NODESET);
        return nodes.item(0).getTextContent();
	}

	
	@Test
	@Ignore
	public void testEnabledDisableAspect() {
        userDao.enableDisableAspect(1, true);
        userDao.enableDisableAspect(1, false);
	}

}
