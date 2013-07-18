package de.enwida.web.dao;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.implementation.UserDaoImpl;
import de.enwida.web.db.model.UserNavigation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = false)
@Transactional
public class UserNavigationDaoTest {

	@Autowired
	private UserDaoImpl userDao;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Assert.assertTrue(userDao.updateUserNavigation(new UserNavigation(1,
				"13", "soemdata")));
	}

}
