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

	public User getUser(Long id) {

		return userDao.getUserByID(id);
	}

	public List<User> getUsers() {
		return userDao.findAllUsers();
	}

	@Transactional
	public boolean saveUser(User user) {

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

	private boolean postSavingUser(long userId, User user) {
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

	public boolean updateUser(User user) {
		return userDao.updateUser(user);
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

	public void deleteUser(User user) {
		userDao.deleteUser(user);
	}

	public String assignUserToGroup(int userID, int groupID) {
		return userDao.assignUserToGroup(userID, groupID);
	}

	public String deassignUserToGroup(int userID, int groupID) {
		return userDao.deassignUserFromGroup(userID, groupID);
	}

	public List<Group> getAllGroupsWithUsers() {
		return userDao.getAllGroupsWithUsers();
	}

	public String assignRoleToGroup(int roleID, int groupID) {
		return userDao.assignRoleToGroup(roleID, groupID);
	}

	public String deassignRoleToGroup(int roleID, int groupID) {
		return userDao.deassignRoleFromGroup(roleID, groupID);
	}

	public List<Role> getAllRolesWithGroups() {
		return userDao.getAllRolesWithGroups();
	}

	public List<User> findAllUsers() {
		return userDao.findAllUsers();
	}

	@Override
	public boolean enableDisableUser(int userID, boolean enabled) {
		return userDao.enableDisableUser(userID, enabled);
	}

	@Override
	public void removeGroup(int groupID) throws Exception {
		userDao.removeGroup(groupID);
	}

	@Override
	public boolean usernameAvailablility(String username) {
		return userDao.usernameAvailablility(username);
	}

	@Override
	public boolean enableDisableAspect(int rightID, boolean enabled) {
		return userDao.enableDisableAspect(rightID, enabled);
	}

	@Override
	public boolean activateUser(String username, String activationCode) {
		if (userDao.checkUserActivationId(username, activationCode)) {
			return userDao.activateUser(username);

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
}
