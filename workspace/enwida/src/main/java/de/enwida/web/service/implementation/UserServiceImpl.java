package de.enwida.web.service.implementation;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.chart.DataRequestManager;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.UserService;
import de.enwida.web.utils.Constants;

@Service("UserService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private DataRequestManager dataRequestManager;
	
	public User getUser(Long id) {
		// TODO Auto-generated method stub
		return null;
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
		int userId = userDao.save(user);
		
		// If successfully saved, than assign default roles to user and save userId in user_roles table
		if(userId != -1)
		{
			HashMap userRoles = new HashMap(); 
			userRoles.put(Constants.ANONYMOUS_ROLE, 3);
			userDao.setUserRoles(userId, userRoles);
			return true;
		}
		else
		{
			return false;
		}		 
	}

	@Override
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
}
