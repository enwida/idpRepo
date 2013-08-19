package de.enwida.web.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.enwida.transport.DataResolution;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.db.model.NavigationDefaults;
import de.enwida.web.db.model.NavigationSettings;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.service.interfaces.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class UserNavigationServiceTest {

	@Autowired
	private IUserService userService;
	@Autowired
	private INavigationService navigationService;

	private Logger logger = Logger.getLogger(getClass());

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		try {
			test1();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test2();
	}

	private void test1() throws Exception {
		User user1 = new User("ab@tum.de", "username1", "password1", "firstname", "lastname", false);
		userService.saveUser(user1);
		System.out.println(userService.fetchAllUsers());
		User user = userService.fetchUser("username1");
		NavigationSettings navSet = new NavigationSettings();
		navSet.setChartId(100);
		navSet.setUser(user);
		NavigationDefaults data = new NavigationDefaults(12,
				DataResolution.WEEKLY, 120, new CalendarRange(
						Calendar.getInstance(), Calendar.getInstance()));
		List<Integer> diabledLines = new ArrayList<Integer>();
		diabledLines.add(1);
		diabledLines.add(2);

		data.setDisabledLines(new HashSet<Integer>(diabledLines));
		navSet.setSettingsData(data);

		user.getNavigationSettings().add(navSet);
		userService.updateUser(user);
	}

	private void test2() {
		NavigationSettings navSet = new NavigationSettings();
		navSet.setChartId(100);
		navSet.setClientId("112455");
		NavigationDefaults data = new NavigationDefaults(12,
				DataResolution.WEEKLY, 120, new CalendarRange(
						Calendar.getInstance(), Calendar.getInstance()));

		List<Integer> diabledLines = new ArrayList<Integer>();
		diabledLines.add(1);
		diabledLines.add(2);

		data.setDisabledLines(new HashSet<Integer>(diabledLines));
		navSet.setSettingsData(data);
		// save or update navigation settings
		navigationService.saveUserNavigationSettings(navSet);
	}

}
