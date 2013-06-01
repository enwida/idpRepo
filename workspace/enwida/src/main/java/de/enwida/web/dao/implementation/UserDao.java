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
import de.enwida.web.model.UserPermission;

@Repository
public class UserDao extends BaseDao<User> implements IUserDao {
	
	@Autowired
	private DriverManagerDataSource datasource;
	
	public List<User> findAllUsersWithPermissions(){
		ArrayList<User> users = new ArrayList<User>();
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
					rs.getString("user_name"), 
					rs.getString("user_password"), 
					rs.getBoolean("enabled")
				);
				;
				users.add(loadUserFromDB(user));
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
	
	public List<User> findAllUsers(){
		ArrayList<User> users = new ArrayList<User>();
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
					rs.getString("user_name"), 
					rs.getString("user_password"), 
					rs.getBoolean("enabled")
				);
				;
				users.add(user);
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
	
	public boolean createUser(User user){
		
		String sql = "INSERT INTO users(user_name, user_password, first_name, last_name, enabled, joining_date) VALUES (?, ?, ?, ?, ?, current_date)";
		 
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUserName());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getFirstName());
			ps.setString(4, user.getLastName());
			ps.setBoolean(5, false);
			ps.executeQuery();
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

		return true;
	}

	public User loadUserFromDB(User user) {
		
		String sql = "SELECT users.user_id,user_name,user_password,enabled,string_agg(roles.authority, ', ')as permissions" +
				" FROM users  INNER JOIN user_roles ON users.user_id=user_roles.user_id" +
				" INNER JOIN roles ON roles.role_id=user_roles.role_id " +
				" where users.user_name=? group by users.user_id,user_name,user_password,enabled";

		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUserName());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String[] authority= rs.getString("permissions").split(",");
				for (String auth : authority) {
					UserPermission permission=new UserPermission(auth);	
					user.getUserPermissionCollection().add(permission);
				}
				user.setUserID(rs.getLong("user_id"));
				user.setUserName(rs.getString("user_name"));
				user.setPassword(rs.getString("user_password"));
				user.setEnabled(rs.getBoolean("enabled"));
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
	
	public void addPermission(int userID, int roleID) {
		
		if(roleID==0){
			enableDisableUser(true,userID);
		}
		
		
		String sql = "INSERT INTO user_roles VALUES (?, ?)";
		 
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, userID);
			ps.setLong(2, roleID);
			ps.executeUpdate();
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
	}
	
	

	public void removePermission(int userID, int roleID) {
		
		if(roleID==0){
			enableDisableUser(false,userID);
		}
		
		
		String sql = "DELETE FROM user_roles WHERE (user_id=? and role_id=?)";
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, userID);
			ps.setLong(2, roleID);
			ps.executeUpdate();
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
	}

	public boolean enableDisableUser(boolean enable,int userID) {
		String sql = "UPDATE users SET enabled=? WHERE user_id=?";
		 
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setBoolean(1, enable);
			ps.setInt(2, userID);
			ps.executeUpdate();
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

		return true;
	}

	public boolean deleteUser(User user) {
		String sql = "DELETE FROM users WHERE user_name=?";
		 
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUserName());
			ps.executeQuery();
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

		return true;
	}
}
