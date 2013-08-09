package de.enwida.web.service.implementation;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.IFileDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.Constants;
import de.enwida.web.utils.EnwidaUtils;

@Service("userService")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserDao userDao;

	@Autowired
	private IFileDao fileDao;

	@Autowired
	private MailServiceImpl mailService;

	private static org.apache.log4j.Logger logger = Logger
			.getLogger(AdminController.class);

	public User getUser(Long id) throws Exception {

		return userDao.getUserByID(id);
	}

	public List<User> getUsers() throws Exception {
		return userDao.findAllUsers();
	}

	@Transactional
	public boolean saveUser(User user) throws Exception {

		Date date = new Date(Calendar.getInstance().getTimeInMillis());
		user.setJoiningDate(date);
		user.setEnabled(false);

		// Generating activation Id for User
		EnwidaUtils activationIdGenerator = new EnwidaUtils();
		user.setActivationKey(activationIdGenerator.getActivationId());

		// Saving user in the user table
		long userId = userDao.save(user);

		boolean success = true;
		// success = postSavingUser(userId,user);

		return success;
	}

	private boolean postSavingUser(long userId, User user) throws Exception {
		if (userId != -1) {
			Group group = userDao.getGroupByCompanyName(user.getCompanyName());

			if (group != null && group.isAutoPass()) {
				Group newGroup = userDao.getGroupByGroupId(group.getGroupID());
				userDao.assignUserToGroup(userId, newGroup.getGroupID());

			} else {
				// saving in default group (Anonymous)
				Group anonymousGroup = userDao.getGroupByName("anonymous");
				if (anonymousGroup == null) {
					anonymousGroup = new Group();
					anonymousGroup.setGroupName("anonymous");
					anonymousGroup.setAutoPass(true);
				}
				anonymousGroup = userDao.addGroup(anonymousGroup);
				userDao.assignUserToGroup(userId, anonymousGroup.getGroupID());
			}

			try {
				mailService.SendEmail(
						user.getUserName(),
						"Activation Link",
						Constants.ACTIVATION_URL + "username="
								+ user.getUserName() + "&actId="
								+ user.getActivationKey());
			} catch (Exception e) {
				logger.error(e.getMessage());
				return false;
			}

			return true;
		} else {
			return false;
		}
	}
	public String getPassword(String email) {
		return userDao.getPassword(email);
	}

	public List<User> findAllUsersWithPermissions() {
		return userDao.findAllUsersWithPermissions();
	}

	public List<Group> getAvailableGroupsForUser(long userID) {
		return userDao.getAvailableGroupsForUser(userID);
	}

	public List<Group> getUserGroups(long userID) {
		return userDao.getUserGroups(userID);
	}

	public List<Group> getAllGroups() {
		return userDao.getAllGroups();
	}

	public Group addGroup(Group newGroup) {
		newGroup.setAutoPass(false);
		return userDao.addGroup(newGroup);
	}

	public void saveRole(Role role) {
		userDao.addRole(role);
	}

	public List<Role> getAllRoles() {
		return userDao.getAllRoles();
	}

	public boolean checkEmailAvailability(String email) {
		return userDao.checkEmailAvailability(email);
	}

	public void updateUser(User user) {
		userDao.updateUser(user);
	}

	public User getUser(String userName) {
		return userDao.getUserByName(userName);
	}

	public void resetPassword(long userID) {
		SecureRandom random = new SecureRandom();
		String newPassword = new BigInteger(130, random).toString(32);
		User user = userDao.getUserByID(userID);
		user.setPassword(newPassword);
		userDao.updateUser(user);
		try {
			mailService.SendEmail(user.getUserName(), "New Password",
					"Your new Password:" + newPassword);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void deleteUser(User user) throws Exception {
		userDao.deleteUser(user);
	}

	public void assignUserToGroup(int userID, int groupID) {
		userDao.assignUserToGroup(userID, groupID);
	}

	public void deassignUserToGroup(int userID, int groupID) {
		userDao.deassignUserFromGroup(userID, groupID);
	}

	public List<Group> getAllGroupsWithUsers() {
		return userDao.getAllGroupsWithUsers();
	}

	public void assignRoleToGroup(int roleID, int groupID) {
		userDao.assignRoleToGroup(roleID, groupID);
	}

	public void deassignRoleToGroup(int roleID, int groupID) {
		userDao.deassignRoleFromGroup(roleID, groupID);
	}

	public List<Role> getAllRolesWithGroups() {
		return userDao.getAllRolesWithGroups();
	}

	public List<User> findAllUsers() {
		return userDao.findAllUsers();
	}

	@Override
	public void enableDisableUser(int userID, boolean enabled) {
		userDao.enableDisableUser(userID, enabled);
	}

	@Override
	public void removeGroup(int groupID) throws Exception {
		userDao.removeGroup(groupID);
	}

	public boolean usernameAvailablility(String username) {
		return userDao.usernameAvailablility(username);
	}

	@Override
	public void enableDisableAspect(int rightID, boolean enabled) {
		userDao.enableDisableAspect(rightID, enabled);
	}

	@Override
	public boolean activateUser(String username, String activationCode) {
		if (userDao.checkUserActivationId(username, activationCode)) {
			userDao.activateUser(username);
			return true;

		}

		return false;
	}

	@Override
	public Long getNextSequence(String schema, String sequenceName) {
		Long value = null;
		try {
			value = userDao.getNextSequence(schema, sequenceName);
		} catch (Exception e) {
			logger.error("Do nothing");
		}
		return value;
	}

	@Override
	public UploadedFile getFile(int fileId) {
		return fileDao.getFile(fileId);
	}

	@Override
	public UploadedFile getFileByFilePath(String filePath) {
		return fileDao.getFileByFilePath(filePath);
	}

	@Override
	public int getUploadedFileVersion(UploadedFile file, User user) {
		return userDao.getUploadedFileVersion(file, user);
	}

	@Override
	public boolean saveUser(User user, String activationHost) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<User> getAllUsers() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRole(Role role) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean userNameAvailability(String username) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User getCurrentUser() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
