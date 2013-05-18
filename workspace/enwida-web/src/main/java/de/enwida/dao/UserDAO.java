package de.enwida.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

public class UserDAO {
	
	@Autowired
	DataSource dataSource;

    public void SaveToDB(User user) {
    	String sql = "INSERT INTO users( username, password) VALUES (?,?)";
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getName());
			ps.setString(2, user.getSex());
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
    
    public ArrayList<User> GetAllUsers() {
    	String sql = "select * from users";
		Connection conn = null;
		ArrayList<User> userList = new ArrayList<User>();
		return userList;
    }
}