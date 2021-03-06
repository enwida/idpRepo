package de.enwida.web;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.transport.LineRequest;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.db.model.NavigationDefaults;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;
import de.enwida.web.model.ProductTree.ProductAttributes;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.service.interfaces.ISecurityService;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.ProductLeaf;
 
	
/**
 *  Warning: Running all the tests will take a lot of time (up to several hours). Use ChartNavigationTestsRelaxed for quick testing instead.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/root-context-test.xml")
public class ChartNavigationTest {

	private static boolean isDbSchemaRecreated = false;

	@Autowired
	private TestUtils testUtils;
	
	@Autowired
	protected INavigationService navigationService;
	
	@Autowired
	protected ISecurityService securityService;
	
	@Autowired
	protected IUserService userService;
	
	@Autowired
	protected IRightDao rightDao;
	
	@Autowired
	protected AbstractDataSource dataSource;
	
	protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	protected static final String username = "navigationTester";
	
	
	@Before
	public void cleanup() throws Exception {
		final Connection connection = dataSource.getConnection();
		
		if (!isDbSchemaRecreated) {
			TestUtils.recreateUsersSchema(connection);
			isDbSchemaRecreated = true;
		}

		TestUtils.cleanupDatabase(connection);
		connection.close();
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
	public void defaultRestrictionsArentExtended() throws Exception {
		final User user = testUtils.saveUserWithGroup(username);

		for (final Integer key : navigationService.getAllDefaultNavigationData().keySet()) {
			final ChartNavigationData defaultNavigation = navigationService.getDefaultNavigationData(key);
			final ChartNavigationData restrictedNavigation = navigationService.getNavigationDataWithoutAvailablityCheck(key, user, Locale.ENGLISH);
			
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
	
	@Test
	public void checkNoPermissions() throws Exception {
		final User user = testUtils.saveUserWithGroup(username);
		final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(0, user, Locale.ENGLISH);
		Assert.assertNotNull(navigationData);
		Assert.assertTrue(navigationData.getAllResolutions().isEmpty());
		
		for (final ProductTree tree : navigationData.getProductTrees()) {
			Assert.assertTrue(tree.flatten().isEmpty());
		}
	}
	
	@Test
	public void checkSinglePermission() throws Exception {
		final Right right = new Right();
		right.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right.setEnabled(true);
		right.setProduct(211);
		right.setResolution(DataResolution.DAILY.name());
        right.setTimeRange(new CalendarRange(dateFormat.parse("2008-05-18"), dateFormat.parse("2012-09-02")));
		right.setTso(99);
		
		User user = testUtils.saveUserWithGroup(username);
		testUtils.saveRole(user, "testrole1");
		testUtils.saveRight(user, right);
		user = userService.syncUser(user);
		
		final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(0, user, Locale.GERMAN);
		Assert.assertNotNull(navigationData);
		checkNavigationData(navigationData);

		Assert.assertTrue(navigationData.getAllResolutions().size() == 1);
		
		// Only one TSO
		Assert.assertTrue(navigationData.getProductTrees().size() == 1);

		final List<ProductAttributes> products = navigationData.getProductTrees().get(0).flatten();
		Assert.assertTrue(products.size() == 1);
		final ProductAttributes product = products.get(0);
		Assert.assertTrue(product.productId == right.getProduct());
		Assert.assertTrue(product.resolutions.size() == 1);
		Assert.assertEquals(product.resolutions.get(0).name(), right.getResolution());
		Assert.assertEquals(product.timeRange.getFrom().getTimeInMillis(), right.getTimeRange().getFrom().getTimeInMillis());
		Assert.assertEquals(product.timeRange.getTo().getTimeInMillis(), right.getTimeRange().getTo().getTimeInMillis());
		
		checkLines(user, navigationData);
	}

	@Test
	public void checkSinglePermissionPerRoleAndNonExistentTso() throws Exception {
		final Right right1 = new Right();
		right1.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right1.setEnabled(true);
		right1.setProduct(211);
		right1.setResolution(DataResolution.DAILY.name());
        right1.setTimeRange(new CalendarRange(dateFormat.parse("2008-05-18"), dateFormat.parse("2012-09-02")));
		right1.setTso(99);

		final Right right2 = new Right();
		right2.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right2.setEnabled(true);
		right2.setProduct(311);
		right2.setResolution(DataResolution.MONTHLY.name());
        right2.setTimeRange(new CalendarRange(dateFormat.parse("2008-05-18"), dateFormat.parse("2012-09-02")));
		right2.setTso(100);
		
		User user = testUtils.saveUserWithGroup(username);
		final Role role1 = testUtils.saveRole(user, "testrole1");
		final Role role2 = testUtils.saveRole(user, "testrole2");
		testUtils.saveRight(role1, right1);
		testUtils.saveRight(role2, right2);
		user = userService.syncUser(user);
		
		final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(0, user, Locale.GERMAN);
		Assert.assertNotNull(navigationData);
		checkNavigationData(navigationData);

		Assert.assertTrue(navigationData.getAllResolutions().size() == 1);
		
		// One tso
		Assert.assertTrue(navigationData.getProductTrees().size() == 1);
		
		final Map<Integer, ProductTree> treeMap = new Hashtable<Integer, ProductTree>();
		for (final ProductTree tree : navigationData.getProductTrees()) {
			treeMap.put(tree.getTso(), tree);
		}

		// Check TSO 99
		final List<ProductAttributes> products = treeMap.get(99).flatten();
		Assert.assertTrue(products.size() == 1);
		final ProductAttributes product = products.get(0);
		Assert.assertTrue(product.productId == right1.getProduct());
		Assert.assertTrue(product.resolutions.size() == 1);
		Assert.assertEquals(product.resolutions.get(0).name(), right1.getResolution());
		Assert.assertEquals(product.timeRange.getFrom().getTimeInMillis(), right1.getTimeRange().getFrom().getTimeInMillis());
		Assert.assertEquals(product.timeRange.getTo().getTimeInMillis(), right1.getTimeRange().getTo().getTimeInMillis());

		checkLines(user, navigationData);
	}

	@Test
	public void checkSinglePermissionPerRoleAndProduct() throws Exception {
		final Right right1 = new Right();
		right1.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right1.setEnabled(true);
		right1.setProduct(211);
		right1.setResolution(DataResolution.DAILY.name());
		right1.setTimeRange(new CalendarRange(dateFormat.parse("2009-05-18"), dateFormat.parse("2011-09-02")));
		right1.setTso(99);

		final Right right2 = new Right();
		right2.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right2.setEnabled(true);
		right2.setProduct(311);
		right2.setResolution(DataResolution.MONTHLY.name());
        right2.setTimeRange(new CalendarRange(dateFormat.parse("2009-05-18"), dateFormat.parse("2011-09-02")));
		right2.setTso(99);
		
		User user = testUtils.saveUserWithGroup(username);
		final Role role1 = testUtils.saveRole(user, "testrole1");
		final Role role2 = testUtils.saveRole(user, "testrole2");
		testUtils.saveRight(role1, right1);
		testUtils.saveRight(role2, right2);
		user = userService.syncUser(user);
		
		final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(0, user, Locale.GERMAN);
		Assert.assertNotNull(navigationData);
		checkNavigationData(navigationData);

		Assert.assertTrue(navigationData.getAllResolutions().size() == 2);
		
		// One tso
		Assert.assertTrue(navigationData.getProductTrees().size() == 1);
		
		// Check TSO 99
		final List<ProductAttributes> products = navigationData.getProductTrees().get(0).flatten();
		Assert.assertTrue(products.size() == 2);

		final ProductAttributes product1 = products.get(0);
		Assert.assertTrue(product1.productId == right1.getProduct());
		Assert.assertTrue(product1.resolutions.size() == 1);
		Assert.assertEquals(product1.resolutions.get(0).name(), right1.getResolution());
		Assert.assertEquals(product1.timeRange.getFrom().getTimeInMillis(), right1.getTimeRange().getFrom().getTimeInMillis());
		Assert.assertEquals(product1.timeRange.getTo().getTimeInMillis(), right1.getTimeRange().getTo().getTimeInMillis());		

		final ProductAttributes product2 = products.get(1);
		Assert.assertTrue(product2.productId == right2.getProduct());
		Assert.assertTrue(product2.resolutions.size() == 1);
		Assert.assertEquals(product2.resolutions.get(0).name(), right2.getResolution());
		Assert.assertEquals(product2.timeRange.getFrom().getTimeInMillis(), right2.getTimeRange().getFrom().getTimeInMillis());
		Assert.assertEquals(product2.timeRange.getTo().getTimeInMillis(), right2.getTimeRange().getTo().getTimeInMillis());

		checkLines(user, navigationData, toCalendar("2009-06-01"), toCalendar("2010-08-05"));
	}

	@Test
	public void checkSinglePermissionPerRoleForSameProduct() throws Exception {
		final Right right1 = new Right();
		right1.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right1.setEnabled(true);
		right1.setProduct(211);
		right1.setResolution(DataResolution.DAILY.name());
        right1.setTimeRange(new CalendarRange(dateFormat.parse("2008-05-18"), dateFormat.parse("2012-09-02")));
		right1.setTso(99);

		final Right right2 = new Right();
		right2.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right2.setEnabled(true);
		right2.setProduct(211);
		right2.setResolution(DataResolution.MONTHLY.name());
        right2.setTimeRange(new CalendarRange(dateFormat.parse("2008-05-18"), dateFormat.parse("2012-09-02")));
		right2.setTso(99);
		
		User user = testUtils.saveUserWithGroup(username);
		final Role role1 = testUtils.saveRole(user, "testrole1");
		final Role role2 = testUtils.saveRole(user, "testrole2");
		testUtils.saveRight(role1, right1);
		testUtils.saveRight(role2, right2);
		user = userService.syncUser(user);
		
		final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(0, user, Locale.GERMAN);
		Assert.assertNotNull(navigationData);
		checkNavigationData(navigationData);

		Assert.assertTrue(navigationData.getAllResolutions().size() == 2);
		
		// One TSO
		Assert.assertTrue(navigationData.getProductTrees().size() == 1);
		
		// Check TSO 99
		final List<ProductAttributes> products = navigationData.getProductTrees().get(0).flatten();
		Assert.assertTrue(products.size() == 1);

		final ProductAttributes product1 = products.get(0);
		Assert.assertTrue(product1.productId == right1.getProduct());
		Assert.assertTrue(product1.resolutions.size() == 2);
		Assert.assertTrue(product1.resolutions.contains(DataResolution.valueOf(right1.getResolution())));
		Assert.assertTrue(product1.resolutions.contains(DataResolution.valueOf(right2.getResolution())));
		
		// Check that the time matches the maximum of allowed times
		Assert.assertEquals(product1.timeRange.getFrom().getTimeInMillis(), right2.getTimeRange().getFrom().getTimeInMillis());
		Assert.assertEquals(product1.timeRange.getTo().getTimeInMillis(), right1.getTimeRange().getTo().getTimeInMillis());

		checkLines(user, navigationData, toCalendar("2009-06-01"), toCalendar("2010-02-04"));
	}

	@Test
	public void checkTimeRangeExpansionOverRoles() throws Exception {
		final Right right1 = new Right();
		right1.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right1.setEnabled(true);
		right1.setProduct(211);
		right1.setResolution(DataResolution.DAILY.name());
        right1.setTimeRange(new CalendarRange(dateFormat.parse("2008-05-18"), dateFormat.parse("2012-09-02")));
		right1.setTso(99);

		final Right right2 = new Right();
		right2.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right2.setEnabled(true);
		right2.setProduct(211);
		right2.setResolution(DataResolution.DAILY.name());
        right2.setTimeRange(new CalendarRange(dateFormat.parse("2008-05-18"), dateFormat.parse("2012-09-02")));
		right2.setTso(99);
		
		User user = testUtils.saveUserWithGroup(username);
		final Role role1 = testUtils.saveRole(user, "testrole1");
		final Role role2 = testUtils.saveRole(user, "testrole2");
		testUtils.saveRight(role1, right1);
		testUtils.saveRight(role2, right2);
		user = userService.syncUser(user);
		
		final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(0, user, Locale.GERMAN);
		Assert.assertNotNull(navigationData);
		checkNavigationData(navigationData);

		Assert.assertTrue(navigationData.getAllResolutions().size() == 1);
		
		// One TSO
		Assert.assertTrue(navigationData.getProductTrees().size() == 1);
		
		// Check TSO 99
		final List<ProductAttributes> products = navigationData.getProductTrees().get(0).flatten();
		Assert.assertTrue(products.size() == 1);

		final ProductAttributes product1 = products.get(0);
		Assert.assertTrue(product1.productId == right1.getProduct());
		Assert.assertTrue(product1.resolutions.size() == 1);
		Assert.assertTrue(product1.resolutions.contains(DataResolution.valueOf(right1.getResolution())));
		Assert.assertTrue(product1.resolutions.contains(DataResolution.valueOf(right2.getResolution())));
		
		// Check that the time matches the maximum of allowed times
		Assert.assertEquals(product1.timeRange.getFrom().getTimeInMillis(), right2.getTimeRange().getFrom().getTimeInMillis());
		Assert.assertEquals(product1.timeRange.getTo().getTimeInMillis(), right1.getTimeRange().getTo().getTimeInMillis());

		// Check expanding time range over roles
		checkLines(user, navigationData);
	}

	@Test
	public void checkMultiplePermissionsPerRoleForSameProduct() throws Exception {
		final Right right1 = new Right();
		right1.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right1.setEnabled(true);
		right1.setProduct(211);
		right1.setResolution(DataResolution.DAILY.name());
        right1.setTimeRange(new CalendarRange(dateFormat.parse("2008-05-18"), dateFormat.parse("2012-09-02")));
		right1.setTso(99);

		final Right right2 = new Right();
		right2.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right2.setEnabled(true);
		right2.setProduct(211);
		right2.setResolution(DataResolution.MONTHLY.name());
        right2.setTimeRange(new CalendarRange(dateFormat.parse("2008-05-18"), dateFormat.parse("2012-09-02")));
		right2.setTso(99);

		final Right right3 = new Right();
		right3.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right3.setEnabled(true);
		right3.setProduct(211);
		right3.setResolution(DataResolution.WEEKLY.name());
        right3.setTimeRange(new CalendarRange(dateFormat.parse("2008-05-18"), dateFormat.parse("2012-09-02")));
		right3.setTso(99);

		final Right right4 = new Right();
		right4.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right4.setEnabled(true);
		right4.setProduct(211);
		right4.setResolution(DataResolution.WEEKLY.name());
        right4.setTimeRange(new CalendarRange(dateFormat.parse("2008-03-13"), dateFormat.parse("2012-09-02")));
		right4.setTso(99);

		User user = testUtils.saveUserWithGroup(username);
		final Role role1 = testUtils.saveRole(user, "testrole1");
		final Role role2 = testUtils.saveRole(user, "testrole2");
		testUtils.saveRight(role1, right1);
		testUtils.saveRight(role2, right2);
		testUtils.saveRight(role1, right3);
		testUtils.saveRight(role2, right4);
		user = userService.syncUser(user);
		
		final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(0, user, Locale.GERMAN);
		Assert.assertNotNull(navigationData);
		checkNavigationData(navigationData);

		Assert.assertTrue(navigationData.getAllResolutions().size() == 3);
		
		// One tso
		Assert.assertTrue(navigationData.getProductTrees().size() == 1);
		
		// Check TSO 99
		final List<ProductAttributes> products = navigationData.getProductTrees().get(0).flatten();
		Assert.assertTrue(products.size() == 1);

		final ProductAttributes product1 = products.get(0);
		Assert.assertTrue(product1.productId == right1.getProduct());
		Assert.assertTrue(product1.resolutions.size() == 3);
		Assert.assertTrue(product1.resolutions.contains(DataResolution.valueOf(right1.getResolution())));
		Assert.assertTrue(product1.resolutions.contains(DataResolution.valueOf(right2.getResolution())));
		Assert.assertTrue(product1.resolutions.contains(DataResolution.valueOf(right3.getResolution())));
		
		// Check that the time matches the maximum of allowed times
		Assert.assertEquals(product1.timeRange.getFrom().getTimeInMillis(), right4.getTimeRange().getFrom().getTimeInMillis());
		Assert.assertEquals(product1.timeRange.getTo().getTimeInMillis(), right3.getTimeRange().getTo().getTimeInMillis());

		checkLines(user, navigationData, toCalendar("2009-12-01"), toCalendar("2010-05-28"));
	}
	
	@Test
	public void checkWithBasicRightsOnly() throws Exception {
		User user = testUtils.saveUserWithGroup(username);
		final Role role1 = testUtils.saveRole(user, "testrole1");
		final Role role2 = testUtils.saveRole(user, "testrole2");
		setupBasicRights(role1);
		setupBasicRights(role2);
		user = userService.syncUser(user);

		for (final int key : navigationService.getAllDefaultNavigationData().keySet()) {
			final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(key, user, Locale.ENGLISH);
			final ChartNavigationData defaultNavigationData = navigationService.getDefaultNavigationData(key);

			Assert.assertNotNull(navigationData);
			Assert.assertNotNull(defaultNavigationData);
			checkNavigationData(navigationData);
			
			Assert.assertEquals(navigationData.getAllResolutions().size(), defaultNavigationData.getAllResolutions().size());
			for (final DataResolution resolution : defaultNavigationData.getAllResolutions()) {
				Assert.assertTrue(navigationData.getAllResolutions().contains(resolution));
			}
			
			Assert.assertEquals(navigationData.getProductTrees().size(), 1);
			final List<ProductAttributes> products = navigationData.getProductTrees().get(0).flatten();
			Assert.assertFalse(products.isEmpty());
			
			for (final ProductAttributes product : products) {
				Assert.assertEquals(product.resolutions.size(), defaultNavigationData.getAllResolutions().size());
				Assert.assertEquals(product.timeRange.getFrom().getTime(), dateFormat.parse("2009-01-01"));
				Assert.assertEquals(product.timeRange.getTo().getTime(), dateFormat.parse("2012-01-01"));
			}

			checkLines(user, navigationData);
		}
	}

	@Test
	public void checkWithBasicRightsAndProductsRemoved() throws Exception {
		User user = testUtils.saveUserWithGroup(username);
		final Role role1 = testUtils.saveRole(user, "testrole1");
		final Role role2 = testUtils.saveRole(user, "testrole2");
		setupBasicRights(role1);
		setupBasicRights(role2);

		final int[] removedProducts = new int[] { 211, 311, 324, 222 };

		final Connection connection = dataSource.getConnection();
		for (final long roleId : new long[] { role1.getRoleID(), role2.getRoleID() }) {
			for (final int product : removedProducts) {
				final PreparedStatement stmt = connection.prepareStatement("DELETE FROM users.rights WHERE role_id = ? AND product = ?");
				stmt.setLong(1, roleId);
				stmt.setInt(2, product);
				stmt.execute();
			}
		}
		connection.close();
		user = userService.syncUser(user);
		
		for (final int key : navigationService.getAllDefaultNavigationData().keySet()) {
			final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(key, user, Locale.GERMAN);
			final ChartNavigationData defaultNavigationData = navigationService.getDefaultNavigationData(key);

			Assert.assertNotNull(navigationData);
			Assert.assertNotNull(defaultNavigationData);
			Assert.assertEquals(navigationData.getProductTrees().size(), 1);
			
			final List<ProductAttributes> products = navigationData.getProductTrees().get(0).flatten();
			final List<ProductAttributes> defaultProducts = defaultNavigationData.getProductTrees().get(0).flatten();
			
			for (final ProductAttributes product : products) {
				for (final int removedProduct : removedProducts) {
					Assert.assertFalse(product.productId == removedProduct);
				}
				Assert.assertEquals(product.timeRange.getFrom().getTime(), dateFormat.parse("2009-01-01"));
				Assert.assertEquals(product.timeRange.getTo().getTime(), dateFormat.parse("2012-01-01"));
			}
			
			if (defaultProducts.size() > removedProducts.length) {
				Assert.assertTrue(products.size() > 0);
			}

			checkLines(user, navigationData);
		}
	}

	@Test
	public void checkWithBasicRightsAndResolutionsRemoved() throws Exception {
		User user = testUtils.saveUserWithGroup(username);
		final Role role1 = testUtils.saveRole(user, "testrole1");
		final Role role2 = testUtils.saveRole(user, "testrole2");
		setupBasicRights(role1);
		setupBasicRights(role2);
		
		final int productId = 211;
		final DataResolution[] removedResolutions = new DataResolution[] { DataResolution.WEEKLY, DataResolution.YEARLY, DataResolution.DAILY };

		final Connection connection = dataSource.getConnection();
		for (final long roleId : new long[] { role1.getRoleID(), role2.getRoleID() }) {
			for (final DataResolution resolution : removedResolutions) {
				final PreparedStatement stmt = connection.prepareStatement("DELETE FROM users.rights WHERE role_id = ? AND product = ? AND resolution = ?");
				stmt.setLong(1, roleId);
				stmt.setInt(2, productId);
				stmt.setString(3, resolution.name());
				stmt.execute();
			}
		}
		connection.close();
		user = userService.syncUser(user);
		
		for (final int key : navigationService.getAllDefaultNavigationData().keySet()) {
			final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(key, user, Locale.GERMAN);
			final ChartNavigationData defaultNavigationData = navigationService.getDefaultNavigationData(key);

			Assert.assertNotNull(navigationData);
			Assert.assertNotNull(defaultNavigationData);
			Assert.assertEquals(navigationData.getProductTrees().size(), 1);
			
			final List<ProductAttributes> products = navigationData.getProductTrees().get(0).flatten();
			final List<ProductAttributes> defaultProducts = defaultNavigationData.getProductTrees().get(0).flatten();
			
			// Find the product
			for (final ProductAttributes product : products) {
				if (product.productId == productId) {
					for (final DataResolution resolution : removedResolutions) {
						Assert.assertFalse(product.resolutions.contains(resolution));
					}
				}
				// Find product in default navigation data
				for (final ProductAttributes defaultProduct : defaultProducts) {
					if (defaultProduct.productId == product.productId) {
						if (defaultProduct.resolutions.size() > removedResolutions.length) {
							Assert.assertFalse(product.resolutions.isEmpty());
						}
					}
				}
				Assert.assertEquals(product.timeRange.getFrom().getTime(), dateFormat.parse("2009-01-01"));
				Assert.assertEquals(product.timeRange.getTo().getTime(), dateFormat.parse("2012-01-01"));
			}

			checkLines(user, navigationData);
		}
	}

	@Test
	public void checkWithBasicRightsAndTimeRangeRestricted() throws Exception {
		User user = testUtils.saveUserWithGroup(username);
		final Role role1 = testUtils.saveRole(user, "testrole1");
		final Role role2 = testUtils.saveRole(user, "testrole2");
		setupBasicRights(role1);
		setupBasicRights(role2);

		final int productId = 211;
		final java.util.Date startTime = dateFormat.parse("2011-05-09");
		final java.util.Date endTime   = dateFormat.parse("2011-08-21");

		final Connection connection = dataSource.getConnection();

		for (final long roleId : new long[] { role1.getRoleID(), role2.getRoleID() }) {
			for (final DataResolution resolution : DataResolution.values()) {
				final PreparedStatement stmt = connection.prepareStatement("UPDATE users.rights SET start_date = ? , end_date = ? WHERE role_id = ? AND product = ? AND resolution = ?");
				stmt.setDate(1, new Date(startTime.getTime()));
				stmt.setDate(2, new Date(endTime.getTime()));
				stmt.setLong(3, roleId);
				stmt.setInt(4, productId);
				stmt.setString(5, resolution.name());
				stmt.execute();
			}
		}
		connection.close();
		user = userService.syncUser(user);
		
		for (final int key : navigationService.getAllDefaultNavigationData().keySet()) {
			final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(key, user, Locale.GERMAN);

			Assert.assertNotNull(navigationData);
			checkNavigationData(navigationData);
			Assert.assertEquals(navigationData.getProductTrees().size(), 1);
			
			final List<ProductAttributes> products = navigationData.getProductTrees().get(0).flatten();
			
			// Find the product
			for (final ProductAttributes product : products) {
				if (product.productId == productId) {
					Assert.assertEquals(product.timeRange.getFrom().getTime(), startTime);
					Assert.assertEquals(product.timeRange.getTo().getTime(), endTime);
				} else {
					Assert.assertEquals(product.timeRange.getFrom().getTime(), dateFormat.parse("2009-01-01"));
					Assert.assertEquals(product.timeRange.getTo().getTime(), dateFormat.parse("2012-01-01"));		
				}
			}

			checkLines(user, navigationData);
		}
	}

	@Test
	public void checkWithBasicRightsAndSeveralRestrictions() throws Exception {
		User user = testUtils.saveUserWithGroup(username);
		final Role role1 = testUtils.saveRole(user, "testrole1");
		final Role role2 = testUtils.saveRole(user, "testrole2");
		setupBasicRights(role1);
		setupBasicRights(role2);
		
		final int productIdRemoved = 211;
		final int productIdRestricted = 311;

		final DataResolution[] removedResolutions = new DataResolution[] { DataResolution.WEEKLY, DataResolution.YEARLY, DataResolution.DAILY };
		final java.util.Date startTime = dateFormat.parse("2011-05-09");
		final java.util.Date endTime   = dateFormat.parse("2011-08-21");
		
		final long[] roleIds = new long[] { role1.getRoleID(), role2.getRoleID() };

		final Connection connection = dataSource.getConnection();

		for (final long roleId : roleIds) {
			final PreparedStatement stmt = connection.prepareStatement("DELETE FROM users.rights WHERE role_id = ? AND product = ?");
			stmt.setLong(1, roleId);
			stmt.setInt(2, productIdRemoved);
			stmt.execute();
		}

		for (final long roleId : roleIds) {
			for (final DataResolution resolution : removedResolutions) {
				final PreparedStatement stmt = connection.prepareStatement("DELETE FROM users.rights WHERE role_id = ? AND product = ? AND resolution = ?");
				stmt.setLong(1, roleId);
				stmt.setInt(2, productIdRestricted);
				stmt.setString(3, resolution.name());
				stmt.execute();
			}
		}

		for (final long roleId : roleIds) {
			final PreparedStatement stmt = connection.prepareStatement("UPDATE users.rights SET start_date = ? , end_date = ? WHERE role_id = ? AND product = ?");
			stmt.setDate(1, new Date(startTime.getTime()));
			stmt.setDate(2, new Date(endTime.getTime()));
			stmt.setLong(3, roleId);
			stmt.setInt(4, productIdRestricted);
			stmt.execute();
		}
		connection.close();
		user = userService.syncUser(user);

		for (final int key : navigationService.getAllDefaultNavigationData().keySet()) {
			final ChartNavigationData navigationData = navigationService.getNavigationDataWithoutAvailablityCheck(key, user, Locale.GERMAN);
			final ChartNavigationData defaultNavigationData = navigationService.getDefaultNavigationData(key);

			Assert.assertNotNull(navigationData);
			Assert.assertNotNull(defaultNavigationData);
			Assert.assertEquals(navigationData.getProductTrees().size(), 1);
			
			final List<ProductAttributes> products = navigationData.getProductTrees().get(0).flatten();
			final List<ProductAttributes> defaultProducts = defaultNavigationData.getProductTrees().get(0).flatten();
			
			// Find the product
			for (final ProductAttributes product : products) {
				Assert.assertFalse(product.productId == productIdRemoved);
				
				if (product.productId == productIdRestricted) {
					for (final DataResolution resolution : removedResolutions) {
						Assert.assertFalse(product.resolutions.contains(resolution));
					}
					Assert.assertEquals(product.timeRange.getFrom().getTime(), startTime);
					Assert.assertEquals(product.timeRange.getTo().getTime(), endTime);
				} else {
					Assert.assertEquals(product.timeRange.getFrom().getTime(), dateFormat.parse("2009-01-01"));
					Assert.assertEquals(product.timeRange.getTo().getTime(), dateFormat.parse("2012-01-01"));
				}
				// Find product in default navigation data
				for (final ProductAttributes defaultProduct : defaultProducts) {
					if (defaultProduct.productId == product.productId) {
						if (defaultProduct.resolutions.size() > removedResolutions.length) {
							Assert.assertFalse(product.resolutions.isEmpty());
						}
					}
				}
			}
			checkLines(user, navigationData, toCalendar(startTime), toCalendar(endTime));
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
		final Set<Integer> tsos = new HashSet<Integer>();
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
	
	protected void checkLines(User user, ChartNavigationData navigationData, Calendar startTime, Calendar endTime) throws Exception {
		final int[] allProducts = new int[] { 200, 300, 211, 212, 221, 222, 311, 321, 312, 322, 313, 323, 314, 324, 315, 325, 316, 326 };
		
		for (final ProductTree tree : navigationData.getProductTrees()) {
			for (final int product : allProducts) {
				for (final DataResolution resolution : DataResolution.values()) {
					for (final Aspect aspect : navigationData.getAspects()) {
						System.out.println("Product: " + product + " | Aspect: " + aspect.toString());

						final ProductLeaf leaf = tree.getLeaf(product);
						if (leaf == null || !leaf.getResolution().contains(resolution)) {
							final Calendar savedStartTime = startTime;
							final Calendar savedEndTime = endTime;

							if (startTime == null) {
								startTime = Calendar.getInstance();
								startTime.setTime(dateFormat.parse("2011-01-01"));
							}
							if (endTime == null) {
								endTime = Calendar.getInstance();
								endTime.setTime(dateFormat.parse("2012-01-01"));
							}

							final LineRequest lineRequest = new LineRequest(aspect, product, tree.getTso(), startTime, endTime, resolution, Locale.ENGLISH);
							Assert.assertFalse(securityService.isAllowed(lineRequest, user));
							
							startTime = savedStartTime;
							endTime = savedEndTime;
						} else {
							if (startTime == null) {
								startTime = leaf.getTimeRange().getFrom();
							}
							if (endTime == null) {
								endTime = leaf.getTimeRange().getTo();
							}

							final LineRequest lineRequest = new LineRequest(aspect, product, tree.getTso(), startTime, endTime, resolution, Locale.ENGLISH);
							Assert.assertTrue(securityService.isAllowed(lineRequest, user));

							// Enlarge time range
							final Calendar t1 = (Calendar) startTime.clone();
							final Calendar t2 = (Calendar) endTime.clone();
							t1.add(Calendar.YEAR, -2);
							t2.add(Calendar.YEAR, 2);
							lineRequest.setStartTime(t1);
							lineRequest.setEndTime(t2);

							Assert.assertFalse(securityService.isAllowed(lineRequest, user));
						}
					}
				}
			}
		}
	}

	protected void checkLines(User user, ChartNavigationData navigationData) throws Exception {
		checkLines(user, navigationData, null, null);
	}
	
	private Calendar toCalendar(java.util.Date date) {
		final Calendar result = Calendar.getInstance();
		result.setTime(date);
		return result;
	}
	
	private Calendar toCalendar(String source) throws ParseException {
		return toCalendar(dateFormat.parse(source));
	}

	@Transactional
	private Role setupBasicRights(Role role) throws Exception {
		final Connection connection = dataSource.getConnection();
		final int[] tsos = new int[] { 99, 199 };
		final int[] products = new int[] { 211, 212, 221, 222, 311, 312, 321, 322, 313, 323, 314, 324, 315, 325, 316, 326 };
		final Date time1 = new Date(dateFormat.parse("2009-01-01").getTime());
		final Date time2 = new Date(dateFormat.parse("2012-01-01").getTime());
//		final CalendarRange timeRange = new CalendarRange(time1, time2);
		
		for (final int tso : tsos) {
			for (final int product : products) {
				for (final Aspect aspect : Aspect.values()) {
					for (final DataResolution resolution : DataResolution.values()) {
						final PreparedStatement stmt = connection.prepareStatement("INSERT INTO users.rights (aspect,product,resolution,start_date,end_date,tso,enabled,role_id) VALUES (?,?,?,?,?,?,?,?)");
						stmt.setString(1, aspect.name());
						stmt.setInt(2, product);
						stmt.setString(3, resolution.name());
						stmt.setDate(4, time1);
						stmt.setDate(5, time2);
						stmt.setInt(6, tso);
						stmt.setBoolean(7, true);
						stmt.setLong(8, role.getRoleID());
						stmt.execute();
						
						// TOO SLOW:
//						final Right right = new Right(tso, product, resolution.name(), timeRange, aspect.name(), true);
//						userService.saveRight(right);
//						right.setRole(role);
//						userService.saveRight(right);

//						if (++i % 100 == 0) {
//							System.out.println(i + " rights inserted");
//						}
					}
				}
			}
		}
		connection.close();
		return role;
	}

}
