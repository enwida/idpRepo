package de.enwida.web.service.implementation;

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
		// If successfully saved, than assign default roles to user and save userId in user_roles table
		if(userId != -1)
		{
			
			int groupId = userDao.getGroupIdByCompanyName(user.getCompanyName());
			
			if(groupId == -1)
			{
				userDao.saveUserInAnonymousGroup(userId);
			}
			else
			{
				Group group = userDao.getGroupByGroupId(groupId);
				
				if(group.isStatus())
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
    	newGroup.setStatus(false);
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
}
