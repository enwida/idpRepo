package de.enwida.web.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.BaseDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.UserService;

@Repository
public class UserDao extends BaseDao<User> implements IUserDao {
	
	@Autowired
	private DriverManagerDataSource datasource;
	
	public List<User> findAll(){
		List users = new ArrayList<User>();
		String sql = "SELECT * FROM users";
		 
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			User user = null;
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = new User(
					rs.getLong("user_id"),
					rs.getString("username"), 
					rs.getString("password"), 
					rs.getBoolean("enabled")
				);
				;
				users.add(getUserPermissions(user));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
				conn.close();
				} catch (SQLException e) {}
			}
		}

		return users;
	}

	private User getUserPermissions(User user) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM user_roles where user_id=?";
		 
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, user.getUserID());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int permission= rs.getInt("role_id");
				if (permission==1){
					user.setAdmin(true);
				}else if(permission==2){
					user.setTestuser(true);
				}else if(permission==3){
					user.setExport(true);
				}
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
				conn.close();
				} catch (SQLException e) {}
			}
		}
		return user;
	}

	public void addPermission(String userID, String roleID) {
	}

	public void removePermission(String userID, String roleID) {
		// TODO Auto-generated method stub
		
	}
}
