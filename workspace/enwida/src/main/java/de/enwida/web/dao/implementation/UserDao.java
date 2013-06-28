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
	
	@Override
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
	
	@Override
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
	
	@Override
	public ArrayList<Role> getUserRoles(long userID) {
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
		
	@Override
	public void deleteUser(User user) {
	    deleteUserGroup(user.getUserID());
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
	}
	
	@Override
	public void deleteUserGroup(long userID) {
	    String sql = "DELETE FROM users.user_group WHERE user_id=?";
        
        Connection conn = null;
 
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, userID);
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

	@Override
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
	
    @Override
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

	@Override
	public User getUserByID(Long id) {
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
	

	@Override
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
	
	@Override
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


	@Override
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

	@Override
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

    @Override
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

    @Override
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
    
    @Override
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
	
	@Override
	public Group getGroupByCompanyName(final String companyName)
	{
		String sql = "select * FROM users.user_group INNER JOIN users ON user_group.user_id=users.user_id where users.company_name=?";
		Group group = null;
        Connection conn = null;
        
        try 
        {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, companyName);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) 
            {
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

	@Override
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
	
	@Override
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
	
	@Override
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

	@Override
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
	

	@Override
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

    @Override
    public User getUserByName(String userName) {
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

    @Override
    public String assignUserToGroup(long userID, long groupID) {
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

    @Override
    public String deassignUserFromGroup(long userID, long groupID) {
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

    @Override
    public String assignRoleToGroup(long roleID, long groupID) {
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

    @Override
    public String deassignRoleFromGroup(long roleID, long groupID) {
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

    @Override
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
                        Group group=getGroupByGroupId(Long.parseLong(groupID));
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

    @Override
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
                            User user=getUserByID(Long.parseLong(userID));
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
    public boolean enableDisableUser(long userID, boolean enabled) {
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
    public void removeGroup(long groupID) throws Exception {

        String sql = "delete FROM users.user_group where group_id=?;" +
        		"delete FROM users.groups where group_id=?";
        Connection conn = null;
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, groupID);
            ps.setLong(2, groupID);
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

    @Override
    public boolean enableDisableAspect(long rightID, boolean enabled) {
        String sql = "UPDATE users.rights SET enabled=? WHERE right_id=?";
        
        Connection conn = null;
 
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, enabled);
            ps.setLong(2, rightID);
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
    public Group getGroupByName(String groupName) {
        Group group = null;
        
        String sql = "select * from users.groups where group_name=?";
        Connection conn = null;
        
        try 
        {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, groupName);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) 
            {
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
}
