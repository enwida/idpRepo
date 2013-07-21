package de.enwida.web.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import de.enwida.web.model.User;

@Controller
@Scope("session")
public class UserSessionManager {

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


}
