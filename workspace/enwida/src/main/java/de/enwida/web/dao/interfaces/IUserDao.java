package de.enwida.web.dao.interfaces;

import java.util.HashMap;
import java.util.List;
import de.enwida.web.model.User;

public interface IUserDao {
	public int save(User user);
	public void setUserRoles(int userId, HashMap roles);
}
