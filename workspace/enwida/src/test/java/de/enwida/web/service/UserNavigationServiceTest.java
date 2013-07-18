package de.enwida.web.service;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.enwida.web.service.interfaces.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class UserNavigationServiceTest {
	@Autowired
	private IUserService userService;

	@Before
	public void setUp() throws Exception {
		System.out.println(userService.getUsers());
		// userDao.updateUserNavigation(new UserNavigation(1, "13",
		// "soemdata"));
		// em.persist(new UserNavigation("12", "sjdajsdajd"));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Assert.assertNotNull(userService.getUsers());
	}

}
