package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.model.User;

public interface UserService {
	
	public User getUser(Long id);
	public List<User> getUsers();
	public boolean saveUser(User user);
	public boolean sendVerificationEmail(User user);
}
