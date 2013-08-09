package de.enwida.web.dao;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.dao.interfaces.IUserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = false)
@Transactional
public class UserNavigationDaoTest {

	@Autowired
	private IUserDao userDao;
	@Autowired
	private INavigationDao navigationDao;

	private Logger logger = Logger.getLogger(getClass());

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		// boolean createsuccess = navigationDao
		// .createNavigationSettings(new NavigationSettings("13",
		// "soemdata"));
		// logger.debug("Created User navigation settings : " + createsuccess);
		// Assert.assertTrue(createsuccess);
	}

}
