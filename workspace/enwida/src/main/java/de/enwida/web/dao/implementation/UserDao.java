package de.enwida.web.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.tags.ParamAware;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import de.enwida.web.dao.interfaces.BaseDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.UserService;

@Repository
public class UserDao extends BaseDao<User> implements IUserDao {
	
	public List<User> findAll(){
		List users = new ArrayList<User>();
		String sql = "SELECT * FROM users";
		 
		Connection conn = null;
 
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			User user = null;
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				user = new User(
					rs.getLong("userid"),
					rs.getString("username"), 
					rs.getString("password"), 
					rs.getBoolean("enabled")
				);
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
	
	public int save(final User user) 
	{
		KeyHolder keyHolder = new GeneratedKeyHolder();	
		Number id = -1;

		try 
		{
			final String sql = "INSERT INTO \"user\" ( user_name, user_password, first_name, last_name, enabled, joining_date ) VALUES ( ?, ?, ?, ?, ?, ?)";	    			
			this.jdbcTemplate.update(
				    new PreparedStatementCreator() {
				        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"user_id"});
				            ps.setString(1, user.getUserName());
				            ps.setString(2, user.getPassword());
				            ps.setString(3, user.getFirstName());
				            ps.setString(4, user.getLastName());
				            ps.setBoolean(5, user.isEnabled());
				            ps.setDate(6, user.getJoiningDate());
				            return ps;
				        }
				    },
				    keyHolder);
				
		    id = keyHolder.getKey();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}      
		return id.intValue();
	}
	
	public void setUserRoles(final int userId, final HashMap roles) 
	{	
		KeyHolder keyHolder = new GeneratedKeyHolder();	
		Set set = roles.entrySet();
		Iterator i = set.iterator();		
		
		try 
		{
			while(i.hasNext()) 
			{
				final Map.Entry entry = (Map.Entry)i.next();
				final String sql = "INSERT INTO user_roles (role_id, user_id, authority) VALUES ( ?, ?, ?)";
				this.jdbcTemplate.update(
						new PreparedStatementCreator() {
							public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
								PreparedStatement ps = connection.prepareStatement(sql);
								ps.setInt(1, Integer.parseInt(entry.getValue().toString()));
								ps.setInt(2, userId);
								ps.setString(3, entry.getKey().toString());
								return ps;
							}
						},
						keyHolder);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}      
	}
}
