package de.enwida.web;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.Group;
import de.enwida.web.model.ProductTree;
import de.enwida.web.model.ProductTree.ProductAttributes;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.CalendarRange;
import de.enwida.web.utils.NavigationDefaults;
 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class ChartNavigationTest {

	@Autowired
	private INavigationService navigationService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private AbstractDataSource dataSource;
	
	private static final String username = "navigationTester";
	private static final int userId = 42;
	private static final long groupId = 42l;
	
	@Before
	public void setup() throws Exception {
		if (userService.getUser(username) != null) {
			// Test user is already there
			return;
		}
		final User user = new User(userId, username, "", "John", "Doe", true);
		user.setCompanyLogo("");
		user.setCompanyName("");
		user.setTelephone("");
		userService.saveUser(user);
		setupGroup();
		setupRoles();
	}
	
	private Group setupGroup() {
		for (final Group group : userService.getAllGroups()) {
			if (group.getGroupID() == 42) {
				// Test group is already there
				return group;
			}
		}
		
		final Group group = new Group();
		group.setGroupID(groupId);
		group.setGroupName("navigationTestGroup");
		userService.addGroup(group);
		userService.assignUserToGroup(userId, group.getGroupID().intValue());
		
		return group;
	}
	
	private void setupRoles() throws Exception {
		final Connection connection = dataSource.getConnection();
		PreparedStatement stmt = connection.prepareStatement("INSERT INTO users.roles VALUES(?, ?, ?)");
		stmt.setInt(1, 42);
		stmt.setString(2, "navigationTestRole1");
		stmt.setString(3, "navigation testing purposes");
		try {
			stmt.execute();
		} catch (PSQLException e) {
			// Ignore "already exists" failure
			if (!e.getMessage().contains("already exists")) {
				throw e;
			}
		}
		
		stmt = connection.prepareStatement("INSERT INTO users.roles VALUES(?, ?, ?)");
		stmt.setInt(1, 43);
		stmt.setString(2, "navigationTestRole2");
		stmt.setString(3, "navigation testing purposes");
		try {
			stmt.execute();
		} catch (PSQLException e) {
			// Ignore "already exists" failure
			if (!e.getMessage().contains("already exists")) {
				throw e;
			}
		}
	}
	
	@Test
	public void serviceIsAvailable() {
		Assert.assertNotNull(navigationService);
	}
	
	@Test
	public void returnsValidDefaultNavigations() {
		for (final ChartNavigationData navigationData : navigationService.getAllDefaultNavigationData().values()) {
			checkNavigationData(navigationData);
		}
	}
	
	@Test
	public void defaultRestrictionsArentExtended() {
		final User user = userService.getUser(username);
		Assert.assertNotNull(user);
		
		// FIXME mock the roles
		user.setRoles(Collections.singletonList(userService.getAllRoles().get(3)));

		for (final Integer key : navigationService.getAllDefaultNavigationData().keySet()) {
			final ChartNavigationData defaultNavigation = navigationService.getDefaultNavigationData(key);
			final ChartNavigationData restrictedNavigation = navigationService.getNavigationData(key, user, Locale.ENGLISH);
			
			Assert.assertTrue(defaultNavigation.getProductTrees().size() >= restrictedNavigation.getProductTrees().size());
			
			for (final int tso : restrictedNavigation.getTsos().keySet()) {
				// Restricted navigation data contains only TSOs which are in the default navigation data
				Assert.assertTrue(defaultNavigation.getTsos().keySet().contains(tso));

				// Find the corresponding tree in the default navigation data
				ProductTree defaultTree = null;
				for (final ProductTree tree : defaultNavigation.getProductTrees()) {
					if (tree.getTso() == tso) {
						defaultTree = tree;
						break;
					}
				}
				Assert.assertNotNull(defaultTree);

				// Find the corresponding tree in the restricted navigation data
				ProductTree restrictedTree = null;
				for (final ProductTree tree : restrictedNavigation.getProductTrees()) {
					if (tree.getTso() == tso) {
						restrictedTree = tree;
						break;
					}
				}
				Assert.assertNotNull(restrictedTree);
				
				final List<ProductAttributes> defaultProducts = defaultTree.flatten();
				final List<ProductAttributes> restrictedProducts = restrictedTree.flatten();

				for (final ProductAttributes restrictedProduct : restrictedProducts) {
					// Product has to exist in default navigation, too
					ProductAttributes defaultProduct = null;
					for (final ProductAttributes currentProduct : defaultProducts) {
						if (currentProduct.productId == restrictedProduct.productId) {
							defaultProduct = currentProduct;
							break;
						}
					}
					Assert.assertNotNull(defaultProduct);

					// Restricted resolutions is a subset of default resolutions
					Assert.assertTrue(defaultProduct.resolutions.containsAll(restrictedProduct.resolutions));
					
					// Default time range is not extended
					Assert.assertTrue(restrictedProduct.timeRange.getFrom().compareTo(defaultProduct.timeRange.getFrom()) >= 0);
					Assert.assertTrue(restrictedProduct.timeRange.getTo().compareTo(defaultProduct.timeRange.getTo()) <= 0);
				}
			}
		}
	}
	
	private void checkNavigationData(ChartNavigationData navigationData) {
		Assert.assertNotNull(navigationData);
		
		Assert.assertNotNull(navigationData.getTitle());
		Assert.assertNotNull(navigationData.getxAxisLabel());
		Assert.assertNotNull(navigationData.getyAxisLabel());

		Assert.assertNotNull(navigationData.getAllResolutions());
		Assert.assertFalse(navigationData.getAllResolutions().isEmpty());
		
		Assert.assertNotNull(navigationData.getAspects());
		Assert.assertFalse(navigationData.getAspects().isEmpty());
		
		Assert.assertNotNull(navigationData.getProductTrees());
		Assert.assertFalse(navigationData.getProductTrees().isEmpty());
		
		checkNavigationDataDefaults(navigationData.getDefaults());
		
		// Only one tree for a specific TSO
		final Set<Integer> tsos = new HashSet<>();
		for (final ProductTree tree : navigationData.getProductTrees()) {
			// Tree isn't empty
			Assert.assertNotNull(tree.getRoot());
			Assert.assertFalse(tree.getRoot().getChildren().isEmpty());
			
			// Assert the element wasn't in the set already
			Assert.assertTrue(tsos.add(tree.getTso()));
		}
	}
	
	private void checkNavigationDataDefaults(NavigationDefaults defaults) {
		Assert.assertNotNull(defaults);
		Assert.assertNotNull(defaults.getDisabledLines());
		checkCalendarRange(defaults.getTimeRange());
	}
	
	private void checkCalendarRange(CalendarRange range) {
		Assert.assertNotNull(range);
		Assert.assertNotNull(range.getFrom());
		Assert.assertNotNull(range.getTo());
		
		// No negative range allowed
		Assert.assertTrue(range.getFrom().compareTo(range.getTo()) <= 0);
	}
	
}
