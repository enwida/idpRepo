package de.enwida.web.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.model.User;
import de.enwida.web.utils.Constants;

@Repository
public class UserDaoImpl extends AbstractBaseDao<User> implements IUserDao {
	
	@Autowired
	private DataSource datasource;
	
	
    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	@Override
	public List<User> findAllUsers() {
        return this.findAll();
	}
	
	@Override
	public String getDbTableName() {
	    return Constants.USERS_SCHEMA_NAME+Constants.USER_TABLE_NAME;
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
	    String sql = "DELETE FROM  "+Constants.USERS_SCHEMA_NAME+Constants.USER_TABLE_NAME+" WHERE user_name=?";
        this.jdbcTemplate.update(sql,user.getUserName());
	}
	
    @Override
	public long save(final User user)
	{
		KeyHolder keyHolder = new GeneratedKeyHolder();	
		Number id = -1;

		final String sql = "INSERT INTO "+Constants.USERS_SCHEMA_NAME+Constants.USER_TABLE_NAME+" ( user_name, user_password, first_name, last_name, enabled, joining_date, telephone, company_name, company_logo, activation_id )" +
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
     
		return id.intValue();
	}

    @Override
	public User getUserByID(Long id) {
        String sql = "SELECT * FROM "+Constants.USERS_SCHEMA_NAME+Constants.USER_TABLE_NAME+" WHERE user_id=?";
        List<User> users = this.jdbcTemplate.query(sql,new Object[]{id}, this);
        return ((users.size() > 0) ? users.get(0) : null);
    }
  
    @Override
	public List<User> getAllUsers() {
        return this.findAll();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean checkUserActivationId(String username, String activationCode) {

        String sql = "select activation_id from "+Constants.USERS_SCHEMA_NAME+Constants.USER_TABLE_NAME+" where users.user_name=?";
        String activationID = (String) this.jdbcTemplate.queryForObject(sql, new Object[] { username }, new RowMapper() {
            @Override
            public String mapRow(ResultSet rs, int arg1) throws SQLException {
                return rs.getString("activation_id");
            }
        });
        
        //String activationID=(String)this.jdbcTemplate.queryForObject(sql, new Object[] { username }, String.class);
        return activationID.equalsIgnoreCase(activationCode);
    }
    
    @Override
    public User getUserByName(String userName) {
		User user = null;

		try {
			TypedQuery<User> typedQuery = em.createQuery(
					"from " + User.class.getName()
							+ " WHERE user_name= :username", User.class);
			user = typedQuery.setParameter("username", userName)
					.getSingleResult();
		} catch (NoResultException noresult) {
			// if there is no result
			logger.error("No user found with user name : " + userName);
		} catch (NonUniqueResultException notUnique) {
			// if more than one result
			logger.error("More than one users found with user name : "
					+ userName);
		}
		return user;

		// String sql = "SELECT * FROM users.users WHERE user_name=?";
		// List<User> users=this.jdbcTemplate.query(sql,new Object[]{userName},
		// this);
		// return ((users.size()>0)? users.get(0) : null);
    }

    @Override
	public void assignUserToGroup(long userId, long groupID) {
        if (userId == 0 || groupID == 0) {
            throw new RuntimeException("Invalid userID or GroupID");
        }
        String sql = "INSERT INTO users.user_group(user_id,group_id) VALUES (?, ?)";
        this.jdbcTemplate.update(sql, userId, groupID);
    }

    @Override
    public void deassignUserFromGroup(long userId, long groupID)
 {
        if (userId == 0 || groupID == 0) {
            throw new RuntimeException("Invalid userID or GroupID");
        }
        String sql = "DELETE FROM users.user_group WHERE (user_id=? and group_id=?)";
        this.jdbcTemplate.update(sql, userId, groupID);
    }

    @Override
	public void activateUser(String username) {
        String sql = "UPDATE "+Constants.USERS_SCHEMA_NAME+Constants.USER_TABLE_NAME+" SET enabled=? WHERE user_name=?";
        this.jdbcTemplate.update(sql, true, username);
    }

    @Override
	public void updateUser(User user) {
        String sql = "UPDATE "+Constants.USERS_SCHEMA_NAME+Constants.USER_TABLE_NAME+" SET first_name=?,last_name=?,telephone=?,user_password=?,company_name=? WHERE user_name=?";
        this.jdbcTemplate.update(sql,new Object[]{ user.getFirstName(),user.getLastName(),user.getTelephone(),user.getPassword(),user.getCompanyName(),user.getUserName()});
    }

    @Override
	public void enableDisableUser(long userID, boolean enabled) {
        String sql = "UPDATE "+Constants.USERS_SCHEMA_NAME+Constants.USER_TABLE_NAME+" SET enabled=? WHERE user_id=?";
        this.jdbcTemplate.update(sql, new Object[] {enabled,userID});
    }
    
    @Override
    public boolean usernameAvailablility(String userName) {
        return this.getUserByName(userName)!=null;
    }

    @Override
    public List<User> getUsersByGroupID(Long groupID) {

        String sql = "SELECT * FROM "+Constants.USERS_SCHEMA_NAME+Constants.USER_TABLE_NAME+" INNER JOIN users.user_group ON users.users.user_id=users.user_group.user_id WHERE user_group.group_ID="+groupID;
        List<User> users  = this.jdbcTemplate.query(sql,this);
        return users;
        
    }

    @Override
	public int getUploadedFileVersion(UploadedFile uplaodedfile, User user) {
		int revision = 1;
		User latestuser = getUserByName(user.getUserName());
		Set<UploadedFile> uploadedFiles = latestuser.getUploadedFiles();
		for (UploadedFile file : uploadedFiles) {
			if (file.getDisplayFileName().equals(
					uplaodedfile.getDisplayFileName())) {
				revision += 1;
			}
		}
		return revision;
	}

	@Override
	public Long getNextSequence(String schema, String sequenceName) {
		return super.getNextSequenceNumber(schema, sequenceName);
	}
}
