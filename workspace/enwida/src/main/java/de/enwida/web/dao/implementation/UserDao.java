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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.BaseDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;

@Repository
public class UserDao extends BaseDao<User> implements IUserDao {
	
	@Autowired
	private DriverManagerDataSource datasource;
	
	public List<User> findAllUsersWithPermissions(){
		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT * FROM users";
		 
		Connection conn = null;
 
		try 
		{
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
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				//users.add(loadUserFromDB(user));
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
		String sql = "SELECT * FROM users.users";
		 
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
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setTelephone(rs.getString("telephone"));
                user.setCompanyName(rs.getString("company_name"));
                ArrayList<Group> groups = getUserGroups(user.getUserID());
                user.setGroups(groups);
                ArrayList<Role> roles = getUserRoles(user.getUserID());
                user.setRoles(roles);
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
	
	private ArrayList<Role> getUserRoles(Long userID) {
	    String sql = "select DISTINCT ON (role_id) roles.role_id,roles.role_name FROM users.roles " +
	    		"INNER JOIN users.group_role ON group_role.role_id=roles.role_id " +
	    		"INNER JOIN users.user_group ON user_group.group_id=group_role.group_id " +
	    		" where users.user_group.user_id=?";
        Connection conn = null;
        ArrayList<Role> roles = new ArrayList<Role>();
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setRoleID(rs.getLong("role_id"));
                role.setRoleName(rs.getString("role_name"));
                roles.add(role);
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
        return roles;
    }
	
	public void addPermission(final long userID, final long roleID) 
	{				
		String sql = "INSERT INTO users.user_roles VALUES (?, ?)";
		 
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, userID);
			ps.setLong(2, roleID);
			ps.executeUpdate();
			ps.close();
		} 
		catch (SQLException e) 
		{
			throw new RuntimeException(e);
		} 
		finally 
		{
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
		
		
		String sql = "DELETE FROM users.user_roles WHERE (user_id=? and role_id=?)";
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
		String sql = "UPDATE users.users SET users.enabled=? WHERE users.user_id=?";
		 
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
		String sql = "DELETE FROM users.users WHERE users.user_name=?";
		 
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUserName());
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
	
	public String getPassword(String email) {
		String sql = "SELECT * FROM users.users where users.user_name=?";
		String password=null;
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				password= rs.getString("user_password");
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
		return password;
	}
	
	public long save(final User user) 
	{
		KeyHolder keyHolder = new GeneratedKeyHolder();	
		Number id = -1;

		try 
		{
			final String sql = "INSERT INTO users.users ( user_name, user_password, first_name, last_name, enabled, joining_date, telephone, company_name, company_logo )" +
					" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";	    			
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
				final String sql = "INSERT INTO users.user_roles (role_id, user_id, authority) VALUES ( ?, ?, ?)";
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

	public User getUser(Long id) {
		String sql = "SELECT * FROM users.users WHERE users.user_id=?";
		Connection conn = null;
		User user = null;
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) 
			{
				user = new User();
                user.setUserID(rs.getLong("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setPassword(rs.getString("user_password"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setLastName(rs.getString("last_name"));
                user.setJoiningDate(rs.getDate("joining_date"));
                user.setTelephone(rs.getString("telephone"));
                ArrayList<Group> groups = getUserGroups(user.getUserID());
                user.setGroups(groups);
                ArrayList<Role> roles = getUserRoles(user.getUserID());
                user.setRoles(roles);
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			throw new RuntimeException(e);
		} 
		finally 
		{
			if (conn != null) 
			{
				try 
				{
					conn.close();
				} 
				catch (SQLException e) {}
			}
		}

		return user;
	}
	
	public Group getGroup(Long id) {
        String sql = "SELECT * FROM users.groups WHERE users.groups.group_id=?";
        Connection conn = null;
        Group group = null;
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                group = new Group();
                group.setGroupID(rs.getLong("group_id"));
                group.setGroupName(rs.getString("group_name"));
                group.setAutoPass(rs.getBoolean("auto_pass"));
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

        return group;
    }

	public ArrayList<Group> getAvailableGroupsForUser(long userID) 
	{
		String sql = "select * FROM users.groups";
		Connection conn = null;
		ArrayList<Group> groups = new ArrayList<Group>();
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
		//	ps.setLong(1, userID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Group group = new Group();
				group.setGroupID(rs.getLong("group_id"));
				group.setGroupName(rs.getString("group_name"));
				groups.add(group);
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
		return groups;
	}
	
	public ArrayList<Group> getUserGroups(long userID) {
		String sql = "select * FROM users.groups INNER JOIN users.user_group ON users.user_group.group_id=groups.group_id where user_group.user_id=?";
		Connection conn = null;
		ArrayList<Group> groups = new ArrayList<Group>();
		
		try 
		{
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, userID);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Group group = new Group();
				group.setGroupID(rs.getLong("group_id"));
				group.setGroupName(rs.getString("group_name"));
				groups.add(group);
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
		return groups;
	}


	public List<Group> getAllGroups() {
		String sql = "select * FROM users.groups";
		Connection conn = null;
		ArrayList<Group> groups = new ArrayList<Group>();
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Group group = new Group();
				group.setGroupID(rs.getLong("group_id"));
				group.setGroupName(rs.getString("group_name"));
				groups.add(group);
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
		return groups;
	}

    public Group addGroup(final Group newGroup) 
    {
		KeyHolder keyHolder = new GeneratedKeyHolder();	
		Number id = -1;

		final String sql = "INSERT INTO users.groups(group_name, auto_pass) VALUES (?, ?);";         
        try 
        {
			this.jdbcTemplate.update(
				    new PreparedStatementCreator() {
				        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"group_id"});
				            ps.setString(1, newGroup.getGroupName());
				            ps.setBoolean(2, newGroup.isAutoPass());
				            return ps;
				        }
				    },
				    keyHolder);
				
		    id = keyHolder.getKey();
        } 
        catch (Exception e) 
        {
            return null;
        } 
        
        newGroup.setGroupID(id.longValue());
        
        return newGroup;
    }

    public void addRole(Role role) {
        if(role.getRoleName().isEmpty()){
            return;
        }
                
        String sql = "INSERT INTO users.roles(name,description) VALUES (?,?);";
         
        Connection conn = null;
 
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, role.getRoleName());
            ps.setString(2, role.getDescription());
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

    public List<Role> getAllRoles() {
        String sql = "select * FROM users.roles";
        Connection conn = null;
        ArrayList<Role> roles = new ArrayList<Role>();
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setRoleID(rs.getLong("role_id"));
                role.setRoleName(rs.getString("role_name"));
                role.setDescription(rs.getString("description"));
                roles.add(role);
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
        return roles;
    }
    
	public boolean checkEmailAvailability(String email) {
		
		String sql = "SELECT * FROM users.users where user_name=?";
		Connection conn = null;
 
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
		
			if (rs.next()) {
				return true;
			}
			
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			throw new RuntimeException(e);
		} 
		finally 
		{
			if (conn != null) 
			{
				try 
				{
					conn.close();
				} 
				catch (SQLException e) {}
			}
		}		
		return false;
	}
	
	public long getGroupIdByCompanyName(final String companyName)
	{
		String sql = "select group_id FROM users.user_group INNER JOIN users ON user_group.user_id=users.user_id where users.company_name=?";
		Connection conn = null;
		
		try 
		{
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, companyName);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) 
			{
				return rs.getLong(1);
			}
			
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{			
			return -1;
		} 
		finally 
		{
			if (conn != null) 
			{
				try 
				{
					conn.close();
				} 
				catch (SQLException e) 
				{
					return -1;
				}
			}
		}
		
		return -1;
	}

	public boolean saveUserInGroup(final long userId, final long groupId)
	{
		final String sql = "INSERT INTO users.user_group(user_id, group_id) VALUES (?, ?);";        
 
        try 
        {
			this.jdbcTemplate.update(
				    new PreparedStatementCreator() {
				        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"group_id"});
				            ps.setLong(1, userId);
				            ps.setLong(2, groupId);
				            return ps;
				        }
				    });
				
        } 
        catch (Exception e) 
        {
            return false;
        } 
        
        return true;
	}

	public long getRoleIdOfGroup(long groupId) 
	{
		String sql = "select role_id FROM users.user_roles INNER JOIN user_group ON user_roles.user_id=user_group.user_id where user_group.group_id=?";
		Connection conn = null;
		
		try 
		{
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, groupId);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) 
			{
				return rs.getLong(1);
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
        return -1;  
	}
	
	public Group getGroupByGroupId(long groupId) 
	{
		Group group = null;
		
		String sql = "select * from users.groups where group_id=?";
		Connection conn = null;
		
		try 
		{
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, groupId);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) 
			{
				group = new Group();
				group.setGroupID(groupId);
				group.setGroupName(rs.getString("group_name"));
				group.setAutoPass(rs.getBoolean("auto_pass"));
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
		
		return group;
		
	}
	
	public int getRoleIdByCompanyName(final String companyName)
	{
		String sql = "select role_id FROM users.user_roles INNER JOIN users.users ON user_roles.user_id=user.user_id where users.company_name=?";
		Connection conn = null;
		
		try 
		{
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, companyName);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) 
			{
				return rs.getInt(1);
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
		
		return -1;
	}
	
	public boolean saveUserInAnonymousGroup(final long userId)
	{
		final String sql = "INSERT INTO users.user_group(user_id, group_id) VALUES (?, (select group_id from users.groups where users.groups.group_name='anonymous'));";        
 
        try 
        {
			this.jdbcTemplate.update(
				    new PreparedStatementCreator() {
				        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"group_id"});
				            ps.setLong(1, userId);
				            return ps;
				        }
				    });
				
        } 
        catch (Exception e) 
        {
            throw new RuntimeException(e);
        } 
        
        return true;

	}

	public long getAnonymousGroupId() 
	{
		String sql = "select group_id from users.groups where group_name='anonymous'";
		Connection conn = null;
		
		try 
		{
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) 
			{
				rs.getLong("group_id");
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
		
		return -1;
		
	}
	

    public boolean updateUser(User user) {
        String sql = "UPDATE users.users SET first_name=?,last_name=?,telephone=?,user_password=? WHERE user_id=?";
        
        Connection conn = null;
 
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getTelephone());
            ps.setString(4, user.getPassword());
            ps.setLong(5, user.getUserID());
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

    public User getUser(String userName) {
        String sql = "SELECT * FROM users.users WHERE user_name=?";
        Connection conn = null;
        User user = null;
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setUserID(rs.getLong("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setPassword(rs.getString("user_password"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                user.setLastName(rs.getString("last_name"));
                user.setJoiningDate(rs.getDate("joining_date"));
                user.setTelephone(rs.getString("telephone"));
                ArrayList<Group> groups = getUserGroups(user.getUserID());
                user.setGroups(groups);
                ArrayList<Role> roles = getUserRoles(user.getUserID());
                user.setRoles(roles);
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

    public String assignUserToGroup(int userID, int groupID) {
        if(userID==0 || groupID==0){
            return "Invalid userID  or groupID ";
        }
        
        
        String sql = "INSERT INTO users.user_group VALUES (?, ?)";
         
        Connection conn = null;
 
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, userID);
            ps.setLong(2, groupID);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            return e.getLocalizedMessage();
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {}
            }
        }
        return "OK";
    }

    public String deassignUserToGroup(int userID, int groupID) {
        if(userID==0 || groupID==0){
            return "Invalid userID  or groupID ";
        }
        
        String sql = "DELETE FROM users.user_group WHERE (user_id=? and group_id=?)";
        Connection conn = null;
 
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, userID);
            ps.setLong(2, groupID);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            return e.getLocalizedMessage();
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {}
            }
        }       
        return "OK";
    }

    public String assignRoleToGroup(int roleID, int groupID) {
        if(roleID==0 || groupID==0){
            return "Invalid roleID  or groupID ";
        }
        
        
        String sql = "INSERT INTO users.group_role VALUES (?, ?)";
         
        Connection conn = null;
 
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, groupID);
            ps.setLong(2, roleID);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            return e.getLocalizedMessage();
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {}
            }
        }
        return "OK"; 
    }

    public String deassignRoleToGroup(int roleID, int groupID) {
        if(roleID==0 || groupID==0){
            return "Invalid roleID  or groupID ";
        }
        
        String sql = "DELETE FROM users.group_role WHERE (group_id=? and role_id=?)";
        Connection conn = null;
 
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, groupID);
            ps.setLong(2, roleID);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            return e.getLocalizedMessage();
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {}
            }
        }       
        return "OK";
    }

    public List<Role> getAllRolesWithGroups() {
        String sql = "SELECT roles.role_id,roles.role_name,roles.description, array_to_string(array_agg(groups.group_id), ',') as groups FROM users.roles " +
        		"INNER JOIN users.group_role ON roles.role_id=group_role.role_id" +
        		" INNER JOIN users.groups ON group_role.group_ID=groups.group_ID" +
        		" GROUP BY users.roles.role_name,roles.role_id,roles.description";
        Connection conn = null;
        ArrayList<Role> roles = new ArrayList<Role>();
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setRoleID(rs.getLong("role_id"));
                role.setRoleName(rs.getString("role_name"));
                role.setDescription(rs.getString("description"));
                String[] groups=rs.getString("groups").split(",");
                for (String groupID : groups) {
                    if(groupID!=null){
                        Group group=getGroup(Long.parseLong(groupID));
                        role.addAssignedGroups(group);
                    }
                }
                roles.add(role);
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
        return roles;
    }


	public List<Group> getAllGroupsWithUsers() {
		String sql = "SELECT groups.group_id,groups.group_name,groups.auto_pass, array_to_string(array_agg(users.user_id), ',')  as users" +
				"  FROM users.groups INNER JOIN users.user_group ON user_group.group_id=groups.group_id" +
				"        INNER JOIN users.users ON user_group.user_ID=users.user_ID" +
				" GROUP BY users.groups.group_id,groups.group_name,groups.auto_pass "+
				" UNION SELECT groups.group_id,groups.group_name,groups.auto_pass, NULL as users from users.groups"+
				" WHERE users.groups.group_id NOT IN (SELECT group_id FROM users.user_group)";
		Connection conn = null;
		ArrayList<Group> groups = new ArrayList<Group>();
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Group group = new Group();
				group.setGroupID(rs.getLong("group_id"));
                group.setGroupName(rs.getString("group_name"));
                group.setAutoPass(rs.getBoolean("auto_pass"));
                String users=rs.getString("users");
                if(users!=null){
                    String[] userIDs=users.split(",");
                    for (String userID : userIDs) {
                        if(userID!=null){
                            User user=getUser(Long.parseLong(userID));
                            group.addAssignedUsers(user);
                        }
                    }
                }
				groups.add(group);
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
		return groups;
	}

    @Override
    public boolean enableDisableUser(int userID, boolean enabled) {
        String sql = "UPDATE users.users SET enabled=? WHERE user_id=?";
        
        Connection conn = null;
 
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, enabled);
            ps.setLong(2, userID);
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

    @Override
    public void removeGroup(int groupID) throws Exception {

        String sql = "delete FROM users.user_group where group_id=?;" +
        		"delete FROM users.groups where group_id=?";
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, groupID);
            ps.setInt(2, groupID);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }
    
    @Override
    public boolean usernameAvailablility(String username) {

        String sql = "select user_id from users.users where users.user_name=?";
        Connection conn = null;
        
        try 
        {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) 
            {
                return true;
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
        return false;
        
    }
}
