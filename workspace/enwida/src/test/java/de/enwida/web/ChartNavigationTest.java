package de.enwida.web;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.Group;
import de.enwida.web.model.ProductTree;
import de.enwida.web.model.ProductTree.ProductAttributes;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;
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
	private IRightDao rightDao;
	
	@Autowired
	private AbstractDataSource dataSource;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
	
	private Group setupGroup() throws Exception {
		
        for (final Group group : userService.getAllGroups()) {
        	if (group.getGroupID() == 42) {
        		// Test group is already there
        		return group;
        	}
        }
		
		final Group group = new Group();
		group.setGroupID(groupId);
		group.setGroupName("navigationTestGroup");
		try {
            userService.addGroup(group);
            userService.assignUserToGroup(userId, group.getGroupID().intValue());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
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
	
	private void setupBasicRights(int roleId) throws Exception {
		final Connection connection = dataSource.getConnection();
		final int[] tsos = new int[] { 99, 199 };
		final int[] products = new int[] { 211, 212, 221, 222, 311, 312, 321, 322, 313, 323, 314, 324, 315, 325, 316, 326 };
		final Date time1 = new Date(dateFormat.parse("2009-01-01").getTime());
		final Date time2 = new Date(dateFormat.parse("2012-01-01").getTime());
		
		for (final int tso : tsos) {
			for (final int product : products) {
				for (final Aspect aspect : Aspect.values()) {
					for (final DataResolution resolution : DataResolution.values()) {
						final PreparedStatement stmt = connection.prepareStatement(
							"INSERT INTO users.rights (role_id,tso,product,resolution,time1,time2,aspect_id,enabled) VALUES " +
						    "(?,?,?,?,?,?,?,?)"
					    );
						stmt.setInt(1, roleId);
						stmt.setInt(2, tso);
						stmt.setInt(3, product);
						stmt.setString(4, resolution.name());
						stmt.setDate(5, time1);
						stmt.setDate(6, time2);
						stmt.setInt(7, aspect.ordinal());
						stmt.setBoolean(8, true);
					}
				}
			}
		}
	}
	
	private void revokeAllRights(int roleId) throws Exception {
		final Connection connection = dataSource.getConnection();
		final PreparedStatement stmt = connection.prepareCall("DELETE FROM users.rights WHERE role_id = ?");
		stmt.setInt(1, roleId);
		stmt.execute();
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
		revokeAllRights(42);
		setupBasicRights(42);
		
		final User user = getTestUser();

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
	
	@Test
	public void checkNoPermissions() throws Exception {
		revokeAllRights(42);
		revokeAllRights(43);
		
		final User user = getTestUser();
		final ChartNavigationData navigationData = navigationService.getNavigationData(0, user, Locale.ENGLISH);
		Assert.assertNotNull(navigationData);
		Assert.assertTrue(navigationData.getAllResolutions().isEmpty());
		
		for (final ProductTree tree : navigationData.getProductTrees()) {
			Assert.assertTrue(tree.flatten().isEmpty());
		}
	}
	
	@Test
	public void checkSinglePermission() throws Exception {
		revokeAllRights(42);
		revokeAllRights(43);
		
		final Right right = new Right();
		right.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right.setEnabled(true);
		right.setProduct(211);
		right.setResolution(DataResolution.DAILY.name());
		right.setRoleID(42);
		right.setTimeFrom(dateFormat.parse("2009-05-18"));
		right.setTimeTo(dateFormat.parse("2011-09-02"));
		right.setTso(99);
		rightDao.addRight(right);
		
		final User user = getTestUser();
		
		final ChartNavigationData navigationData = navigationService.getNavigationData(0, user, Locale.GERMAN);
		Assert.assertNotNull(navigationData);
		Assert.assertTrue(navigationData.getAllResolutions().size() == 1);
		
		// Only one TSO
		Assert.assertTrue(navigationData.getProductTrees().size() == 1);

		final List<ProductAttributes> products = navigationData.getProductTrees().get(0).flatten();
		Assert.assertTrue(products.size() == 1);
		final ProductAttributes product = products.get(0);
		Assert.assertTrue(product.productId == right.getProduct());
		Assert.assertTrue(product.resolutions.size() == 1);
		Assert.assertEquals(product.resolutions.get(0).name(), right.getResolution());
		Assert.assertEquals(product.timeRange.getFrom().getTimeInMillis(), right.getTimeFrom().getTime());
		Assert.assertEquals(product.timeRange.getTo().getTimeInMillis(), right.getTimeTo().getTime());
	}

	@Test
	public void checkSinglePermissionPerRoleAndNonExistentTso() throws Exception {
		revokeAllRights(42);
		revokeAllRights(43);
		
		final Right right1 = new Right();
		right1.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right1.setEnabled(true);
		right1.setProduct(211);
		right1.setResolution(DataResolution.DAILY.name());
		right1.setRoleID(42);
		right1.setTimeFrom(dateFormat.parse("2009-05-18"));
		right1.setTimeTo(dateFormat.parse("2011-09-02"));
		right1.setTso(99);
		rightDao.addRight(right1);

		final Right right2 = new Right();
		right2.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right2.setEnabled(true);
		right2.setProduct(311);
		right2.setResolution(DataResolution.MONTHLY.name());
		right2.setRoleID(43);
		right2.setTimeFrom(dateFormat.parse("2007-05-18"));
		right2.setTimeTo(dateFormat.parse("2010-09-02"));
		right2.setTso(100);
		rightDao.addRight(right2);
		
		final User user = getTestUser();
		
		final ChartNavigationData navigationData = navigationService.getNavigationData(0, user, Locale.GERMAN);
		Assert.assertNotNull(navigationData);
		Assert.assertTrue(navigationData.getAllResolutions().size() == 1);
		
		// One tso
		Assert.assertTrue(navigationData.getProductTrees().size() == 1);
		
		final Map<Integer, ProductTree> treeMap = new Hashtable<>();
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
		Assert.assertEquals(product.timeRange.getFrom().getTimeInMillis(), right1.getTimeFrom().getTime());
		Assert.assertEquals(product.timeRange.getTo().getTimeInMillis(), right1.getTimeTo().getTime());
	}

	@Test
	public void checkSinglePermissionPerRoleAndProduct() throws Exception {
		revokeAllRights(42);
		revokeAllRights(43);
		
		final Right right1 = new Right();
		right1.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right1.setEnabled(true);
		right1.setProduct(211);
		right1.setResolution(DataResolution.DAILY.name());
		right1.setRoleID(42);
		right1.setTimeFrom(dateFormat.parse("2009-05-18"));
		right1.setTimeTo(dateFormat.parse("2011-09-02"));
		right1.setTso(99);
		rightDao.addRight(right1);

		final Right right2 = new Right();
		right2.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right2.setEnabled(true);
		right2.setProduct(311);
		right2.setResolution(DataResolution.MONTHLY.name());
		right2.setRoleID(43);
		right2.setTimeFrom(dateFormat.parse("2007-05-18"));
		right2.setTimeTo(dateFormat.parse("2010-09-02"));
		right2.setTso(99);
		rightDao.addRight(right2);
		
		final User user = getTestUser();
		
		final ChartNavigationData navigationData = navigationService.getNavigationData(0, user, Locale.GERMAN);
		Assert.assertNotNull(navigationData);
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
		Assert.assertEquals(product1.timeRange.getFrom().getTimeInMillis(), right1.getTimeFrom().getTime());
		Assert.assertEquals(product1.timeRange.getTo().getTimeInMillis(), right1.getTimeTo().getTime());		

		final ProductAttributes product2 = products.get(1);
		Assert.assertTrue(product2.productId == right2.getProduct());
		Assert.assertTrue(product2.resolutions.size() == 1);
		Assert.assertEquals(product2.resolutions.get(0).name(), right2.getResolution());
		Assert.assertEquals(product2.timeRange.getFrom().getTimeInMillis(), right2.getTimeFrom().getTime());
		Assert.assertEquals(product2.timeRange.getTo().getTimeInMillis(), right2.getTimeTo().getTime());
	}

	@Test
	public void checkSinglePermissionPerRoleForSameProduct() throws Exception {
		revokeAllRights(42);
		revokeAllRights(43);
		
		final Right right1 = new Right();
		right1.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right1.setEnabled(true);
		right1.setProduct(211);
		right1.setResolution(DataResolution.DAILY.name());
		right1.setRoleID(42);
		right1.setTimeFrom(dateFormat.parse("2009-05-18"));
		right1.setTimeTo(dateFormat.parse("2011-09-02"));
		right1.setTso(99);
		rightDao.addRight(right1);

		final Right right2 = new Right();
		right2.setAspect(Aspect.CR_VOL_ACTIVATION.name());
		right2.setEnabled(true);
		right2.setProduct(211);
		right2.setResolution(DataResolution.MONTHLY.name());
		right2.setRoleID(43);
		right2.setTimeFrom(dateFormat.parse("2008-05-18"));
		right2.setTimeTo(dateFormat.parse("2010-09-02"));
		right2.setTso(99);
		rightDao.addRight(right2);
		
		final User user = getTestUser();
		
		final ChartNavigationData navigationData = navigationService.getNavigationData(0, user, Locale.GERMAN);
		Assert.assertNotNull(navigationData);
		Assert.assertTrue(navigationData.getAllResolutions().size() == 2);
		
		// One tso
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
		Assert.assertEquals(product1.timeRange.getFrom().getTimeInMillis(), right2.getTimeFrom().getTime());
		Assert.assertEquals(product1.timeRange.getTo().getTimeInMillis(), right1.getTimeTo().getTime());
	}

	
	private User getTestUser() throws Exception {
		final User user = userService.getUser(username);
		Assert.assertNotNull(user);
		
		// FIXME mock the roles
		final Role role1 = new Role();
		role1.setRoleID(42);
		
		final Role role2 = new Role();
		role2.setRoleID(43);

		user.setRoles(Arrays.asList(new Role[] { role1, role2 }));
		return user;
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
	
}
