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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IGroupDao;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.UserRole;
import de.enwida.web.model.UserRoleCollection;
import de.enwida.web.model.User;

@Repository
public class GroupDaoImpl extends AbstractBaseDao<User> implements IGroupDao {
	
	@Autowired
	private DataSource datasource;
	
	
    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	
	@Override
	public String getDbTableName() {
	    return "users.groups";
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
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
        }
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
            logger.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
				conn.close();
				} catch (SQLException e) {
                    logger.error(e.getMessage());
                }
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
            logger.error(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
				conn.close();
				} catch (SQLException e) {
                    logger.error(e.getMessage());
				}
			}
		}
		return groups;
	}


	@Override
	public List<Group> getAllGroups() {
	    
	    String sql = "SELECT * FROM users.groups";
	    List<Group> groups  = this.jdbcTemplate.query(sql,new BeanPropertyRowMapper(Group.class));
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
            logger.error(e.getMessage());
            return null;
        } 
        
        newGroup.setGroupID(id.longValue());
        
        return newGroup;
    }

   
	@Override
	public Group getGroupByCompanyName(final String companyName)
	{
		String sql = "select * FROM users.user_group INNER JOIN users.users ON user_group.user_id=users.user_id INNER" +
				" JOIN users.groups ON groups.group_id= user_group.group_id where users.company_name=?";
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
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {

                    logger.error(e.getMessage());
                }
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
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {

                    logger.error(e.getMessage());
                }
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
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {

                    logger.error(e.getMessage());
                }
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
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {

                    logger.error(e.getMessage());
                }
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
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
        }
		
		return -1;
		
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
            logger.error(e.getMessage());
            return e.getLocalizedMessage();
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
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
            logger.error(e.getMessage());
            return e.getLocalizedMessage();
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
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
            logger.error(e.getMessage());
            return e.getLocalizedMessage();
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
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
            logger.error(e.getMessage());
            return e.getLocalizedMessage();
        } finally {
            if (conn != null) {
                try {
                conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
        }       
        return "OK";
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
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
   
   
    @Override
    public Group getGroupByName(String groupName) {
        Group group = null;
        
        String sql = "select * from users.groups where group_name=?";
        return this.jdbcTemplate.queryForObject(sql, new Object[] { groupName }, new BeanPropertyRowMapper(Group.class));
        
    }
    
    
    @Override
    public boolean checkUserActivationId(String username, String activationCode) {

        String sql = "select activation_id from users.users where users.user_name=?";
        Connection conn = null;
        
        try 
        {
            conn = datasource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) 
            {
                if(rs.getString(1).equals(activationCode))
                {
                	return true;
                }                
            }
            
            rs.close();
            ps.close();
        } 
        catch (SQLException e) 
        {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } 
        finally 
        {
            if (conn != null) 
            {
                try 
                {
                	conn.close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        
        return false;        
    }

    @Override
    public List<Group> getGroupsByRole(long roleID) {
        String sql = "SELECT users.groups.group_id, group_name,auto_pass FROM users.groups" +
        		" INNER JOIN users.group_role ON users.groups.group_id=users.group_role.group_id WHERE role_ID="+roleID;
        List<Group> groups  = this.jdbcTemplate.query(sql,new BeanPropertyRowMapper(Group.class));
        return groups;
    }
}
