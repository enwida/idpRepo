package de.enwida.web.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.Group;
import de.enwida.web.model.User;

@Repository
public class UserDaoImpl extends AbstractBaseDao<User> implements IUserDao {
	
	@Autowired
	private DataSource datasource;
	
	
    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	@Override
	public List<User> findAllUsers(){
        return this.findAll();
	}
	
	@Override
	public String getDbTableName() {
	    return "users.users";
	}
	
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserID(rs.getLong("user_id"));
        user.setUserName(rs.getString("user_name"));
        user.setPassword(rs.getString("user_password"));
        user.setFirstName(rs.getString("first_Name"));
        user.setLastName(rs.getString("last_Name"));
        user.setEnabled(rs.getBoolean("enabled"));
        user.setCompanyLogo(rs.getString("company_Logo"));
        user.setCompanyName(rs.getString("company_name"));
        user.setTelephone(rs.getString("telephone"));
        user.setJoiningDate(rs.getDate("joining_date"));
        return user;
    }
		
	@Override
	public void deleteUser(User user) {
	    String sql = "DELETE FROM  users.users WHERE user_name=?";
        try {
            this.jdbcTemplate.update(sql,user.getUserName());
        }catch (Exception e) {
            logger.error(e.getMessage());
        }
	}
	
    @Override
	public long save(final User user) 
	{
		KeyHolder keyHolder = new GeneratedKeyHolder();	
		Number id = -1;

		try 
		{
			final String sql = "INSERT INTO users.users ( user_name, user_password, first_name, last_name, enabled, joining_date, telephone, company_name, company_logo, activation_id )" +
					" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";	    			
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
				            ps.setString(7, user.getTelephone());
                            ps.setString(8, user.getCompanyName());
                            ps.setString(9, user.getCompanyLogo());
                            ps.setString(10, user.getActivationKey());
				            return ps;
				        }
				    },
				    keyHolder);
				
		    id = keyHolder.getKey();
		}
		catch (Exception e) 
		{
            logger.error(e.getMessage());
			e.printStackTrace();
		}      
		return id.intValue();
	}

    @Override
    public User getUserByID(Long id) {
        String sql = "SELECT * FROM users.users WHERE user_id=?";
        List<User> users = this.jdbcTemplate.query(sql,new Object[]{id}, this);
        return ((users.size() > 0) ? users.get(0) : null);
    }
  
    @Override
    public List<User> getAllUsers() {
        return this.findAll();
    }
    
    @Override
    public boolean checkUserActivationId(String username, String activationCode) {

        String sql = "select activation_id from users.users where users.user_name="+username;
        String activationID=(String)this.jdbcTemplate.queryForObject(sql,  String.class);
        return activationID.equalsIgnoreCase(activationCode);
    }

    @Override
    public ArrayList<Group> getUserGroups(long userID) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public User getUserByName(String userName) {
        String sql = "SELECT * FROM users.users WHERE user_name=?";
        List<User> users=this.jdbcTemplate.query(sql,new Object[]{userName}, this);
        return ((users.size()>0)? users.get(0) : null);
    }

    @Override
    public String assignUserToGroup(long userId, long groupID) {
        if(userId==0 || groupID==0){
            throw new RuntimeException("Invalid userID or GroupID");
        }
        String sql = "INSERT INTO users.user_group(user_id,group_id) VALUES (?, ?)";
        try {
            this.jdbcTemplate.update(sql, userId,groupID);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return e.getLocalizedMessage();
        }
        return "OK";
    }
    
    @Override
    public String deassignUserFromGroup(long userId, long groupID) {
        if(userId==0 || groupID==0){
            throw new RuntimeException("Invalid userID or GroupID");
        }
        String sql = "DELETE FROM users.user_group WHERE (user_id=? and group_id=?)";
        try {
            this.jdbcTemplate.update(sql, userId,groupID);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return e.getLocalizedMessage();
        }
        return "OK";
    }

    @Override
    public boolean activateUser(String username) {
        String sql = "UPDATE users.users SET enabled=? WHERE user_name=?";
        this.jdbcTemplate.update(sql, username);
        return true;
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users.users SET first_name=?,last_name=?,telephone=?,user_password=?,company_name=? WHERE user_name=?";
        this.jdbcTemplate.update(sql,new Object[]{ user.getFirstName(),user.getLastName(),user.getTelephone(),user.getPassword(),user.getCompanyName(),user.getUserName()});
        return true;
    }

    @Override
    public boolean enableDisableUser(long userID, boolean enabled) {
        String sql = "UPDATE users.users SET enabled=? WHERE user_id=?";
        this.jdbcTemplate.update(sql, new Object[] {enabled,userID});
        return true;
    }
    
    @Override
    public boolean usernameAvailablility(String userName) {
        return this.getUserByName(userName)!=null;
    }

    @Override
    public List<User> getUsersByGroupID(Long groupID) {

        String sql = "SELECT * FROM users.users INNER JOIN users.user_group ON users.users.user_id=users.user_group.user_id WHERE user_group.group_ID="+groupID;
        List<User> users  = this.jdbcTemplate.query(sql,this);
        return users;
        
    }
}
