package de.enwida.web.service.implementation;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.UserService;
import de.enwida.web.utils.Constants;

@Service("UserService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private IUserDao userDao;
	
    public User getUser(Long id) {
		
		return userDao.getUser(id);
	}

	public List<User> getUsers() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean saveUser(User user) 
	{
		// Saving user in the user table
		Date date = new Date(Calendar.getInstance().getTimeInMillis());
		user.setJoiningDate(date);
		user.setEnabled(false);
		long userId = userDao.save(user);
				
		if(userId != -1)
		{			
			long groupId = userDao.getGroupIdByCompanyName(user.getCompany());
			
			if(groupId == -1)
			{
				userDao.saveUserInAnonymousGroup(userId);
			}
			else
			{
				Group group = userDao.getGroupByGroupId(groupId);
				
				if(group.isAutoPass())
				{
					userDao.saveUserInGroup(userId, groupId);
				}
				else
				{
					userDao.saveUserInAnonymousGroup(userId);
				}
			}
			
			return true;
		}
		else
		{
			return false;
		}		 
	}

	public String getPassword(String email) {
		return userDao.getPassword(email);
	}

	public List<User> findAllUsersWithPermissions(){
		return userDao.findAllUsersWithPermissions();
	}
	

	public void addPermission(int userID, int roleID){
		userDao.addPermission(userID, roleID);		
	}
	public void removePermission(int userID, int roleID){
		userDao.removePermission(userID, roleID);	
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
        return userDao.getUser(userName);
    }

    public boolean resetPassword(long userID) {
        SecureRandom random = new SecureRandom();
        String newPassword=new BigInteger(130, random).toString(32);
        User user=userDao.getUser(userID);
        user.setPassword(newPassword);
        userDao.updateUser(user);
        try {
            Mail.SendEmail(user.getUserName(),"New Password","Your new Password:"+newPassword);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void deleteUser(User user) {
        userDao.deleteUser(user);
    }

    public String assignUserToGroup(int userID, int groupID) {
        return userDao.assignUserToGroup(userID,groupID);
    }

    public String deassignUserToGroup(int userID, int groupID) {
        return userDao.deassignUserToGroup(userID,groupID);
    }

    public List<Group> getAllGroupsWithUsers() {
        return userDao.getAllGroupsWithUsers();
    }

    public String assignRoleToGroup(int roleID, int groupID) {
        return  userDao.assignRoleToGroup(roleID,groupID);
    }

    public String deassignRoleToGroup(int roleID, int groupID) {
        return userDao.deassignRoleToGroup(roleID,groupID);
    }

    public List<Role> getAllRolesWithGroups() {
        return userDao.getAllRolesWithGroups();
    }

    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }
}
