package de.enwida.web.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.Constants;

public class UserSessionManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4849809441855791343L;

	@Autowired
	private IUserService userService;

	@PostConstruct
	public void init() {
		// System.out.println("Session initialized for user: " + user);
	}
	/**
	 * This user is set when user logs in.
	 */
	private User user;

	public User getUser() {
		return user;
	}

	private void setUser(User user) {
		this.user = user;
	}

	public void setUserInSession(String username) throws Exception {
		User user = userService.fetchUser(username);
		setUser(user);
	}

	public void setUserInSession(User user) throws Exception {
		user = userService.fetchUser(user.getUsername());
		setUser(user);
	}

	public String getUserName() {
		if (user != null) {
			return user.getUserName();
		} else {
			return Constants.ANONYMOUS_USER;
		}
	}

	public boolean isUserLoggedIn() {
		if (user != null) {
			return true;
		} else {
			return false;
		}
	}

}
