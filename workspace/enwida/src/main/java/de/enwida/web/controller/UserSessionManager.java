package de.enwida.web.controller;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUserService;

public class UserSessionManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4849809441855791343L;

	@Autowired
	private IUserService userService;

	/**
	 * This user is set when user logs in.
	 */
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setUserInSession(String username) {
		User user = userService.getUser(username);
		setUser(user);
	}

}
