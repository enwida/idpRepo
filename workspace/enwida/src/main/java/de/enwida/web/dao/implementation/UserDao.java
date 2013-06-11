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
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
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

	public User loadUserFromDB(User user) {
		
		String sql = "SELECT users.user_id,user_name,user_password,enabled,string_agg(roles.name, ', ')as permissions" +
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
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM users where user_name=?";
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
	
	public int save(final User user) 
	{
		KeyHolder keyHolder = new GeneratedKeyHolder();	
		Number id = -1;

		try 
		{
			final String sql = "INSERT INTO \"users\" ( user_name, user_password, first_name, last_name, enabled, joining_date ) VALUES ( ?, ?, ?, ?, ?, ?)";	    			
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

	public User getUser(Long id) {
		String sql = "SELECT * FROM users WHERE user_id=?";
		Connection conn = null;
		User user = null;
		try {
			conn = datasource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
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
                user.setJoinDate(rs.getString("joining_date"));
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

	public ArrayList<Group> getAvailableGroupsForUser(long userID) {
		String sql = "select * FROM groups";
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
		String sql = "select * FROM groups INNER JOIN user_group ON user_group.group_id=groups.group_id where user_group.user_id=?";
		Connection conn = null;
		ArrayList<Group> groups = new ArrayList<Group>();
		try {
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
		String sql = "select * FROM groups";
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

    public void addGroup(Group newGroup) {
        if(newGroup.getGroupName().isEmpty()){
            return;
        }
                
        String sql = "INSERT INTO groups(group_name) VALUES (?);";
         
        Connection conn = null;
 
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newGroup.getGroupName());
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

    public void addRole(Role role) {
        if(role.getName().isEmpty()){
            return;
        }
                
        String sql = "INSERT INTO roles(name,description) VALUES (?,?);";
         
        Connection conn = null;
 
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, role.getName());
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
        String sql = "select * FROM roles";
        Connection conn = null;
        ArrayList<Role> roles = new ArrayList<Role>();
        try {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setRoleID(rs.getLong("role_id"));
                role.setName(rs.getString("name"));
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
}
