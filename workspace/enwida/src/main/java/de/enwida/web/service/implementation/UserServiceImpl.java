package de.enwida.web.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.chart.DataRequestManager;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.UserService;

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


}
