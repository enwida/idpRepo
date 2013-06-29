package de.enwida.web;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import de.enwida.web.dao.implementation.UserDao;
import de.enwida.web.model.Group;
import de.enwida.web.model.User;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class UserManagementTest {

	@Autowired
	private DriverManagerDataSource datasource;
	
	@Autowired
	private UserDao userDao;

	
	private User user;
	
	@Before
	public void testUser() {
	    user=new User(100,"test1","test","test","test",false);
	    user.setJoiningDate(new Date(Calendar.getInstance().getTimeInMillis()));
	    user.setCompanyName("enwida.de");
        User existingUser = userDao.getUserByName(user.getUserName());
	    if(existingUser==null){
	        userDao.save(user);
	    }else{
	        user=existingUser;
	    }
	    userDao.enableDisableUser(user.getUserID(), false);
	    userDao.enableDisableUser(user.getUserID(), true);
	    userDao.deleteUser(user);
	}
	
   @Test
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
				} catch (SQLException e) {}
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
				} catch (SQLException e) {}
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
	public void testEnabledDisableAspect() {
        userDao.enableDisableAspect(1, true);
        userDao.enableDisableAspect(1, false);
	}

}
