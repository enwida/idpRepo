package de.enwida.web.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

@Repository
public class GroupDaoImpl extends AbstractBaseDao<Group> implements IGroupDao {
	
	@Autowired
	private DataSource datasource;
	
	
    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	
	@Override
	public String getDbTableName() {
	    return "users.groups";
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Group> getAllGroups() {
	    
	    String sql = "SELECT * FROM users.groups";
	    List<Group> groups  = new ArrayList<Group>();
	    List<Map<String,Object>> rows =this.jdbcTemplate.queryForList(sql);
        for (Map row : rows) {
            Group group = new Group();
            group.setGroupID(Long.parseLong(row.get("group_id").toString()));
            group.setGroupName((String) row.get("group_name"));
            group.setAutoPass((Boolean) row.get("auto_pass"));
            groups.add(group);
        }
	    return groups;
	}
	
   @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group();
        group.setGroupID(rs.getLong("group_id"));
        group.setGroupName(rs.getString("group_name"));
        group.setAutoPass(rs.getBoolean("auto_pass"));
        return group;
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
    public void assignRoleToGroup(long roleID, long groupID) throws Exception {
        if(roleID==0 || groupID==0){
            throw new Exception("Invalid roleID or group ID Argument");
        }        
        
        String sql = "INSERT INTO users.group_role(role_id,group_id) VALUES (?, ?)";
        this.jdbcTemplate.update(sql,new Object[] { roleID, groupID });   
     
    }

    @Override
    public void deassignRoleFromGroup(long roleID, long groupID) throws Exception {
        if(roleID==0 || groupID==0){
            throw new Exception("Invalid roleID or group ID Argument");
        }
        
        String sql = "DELETE FROM users.group_role WHERE (group_id=? and role_id=?)";
 
        this.jdbcTemplate.update(sql,new Object[] { groupID, roleID });       
    }

    @Override
    public void removeGroup(long groupID) throws Exception {

        String sql = "delete FROM users.user_group where group_id=?; delete FROM users.group_role where group_id=?;" +
        		"delete FROM users.groups where group_id=?";

        this.jdbcTemplate.update(sql,new Object[] { groupID, groupID , groupID });
    }
   
   
	@SuppressWarnings("rawtypes")
	@Override
    public Group getGroupByName(String groupName) {
        String sql = "select * from users.groups where group_name=?";
        List<Map<String,Object>> rows =this.jdbcTemplate.queryForList(sql,groupName);
        for (Map row : rows) {
            Group group = new Group();
            group.setGroupID(Long.parseLong(row.get("group_id").toString()));
            group.setGroupName((String) row.get("group_name"));
            group.setAutoPass((Boolean) row.get("auto_pass"));
            return group;
        }
        return null;
    }

	@SuppressWarnings("rawtypes")
	@Override
    public List<Group> getGroupsByRole(long roleID) {
        String sql = "SELECT users.groups.group_id, group_name,auto_pass FROM users.groups" +
        		" INNER JOIN users.group_role ON users.groups.group_id=users.group_role.group_id WHERE role_ID="+roleID;
		@SuppressWarnings("unchecked")
		List<Group> groups = this.jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(Group.class));
        return groups;
    }

    @Override
    public List<Group> getUserGroups(long userID) {
        
        String sql = " SELECT users.groups.group_id, group_name,auto_pass FROM users.groups"
                + " INNER JOIN users.user_group ON users.user_group.group_id=users.groups.group_id where users.user_group.user_id=?";
        
        return this.jdbcTemplate.query(sql,new Object[]{userID},this);
    }
    
}
