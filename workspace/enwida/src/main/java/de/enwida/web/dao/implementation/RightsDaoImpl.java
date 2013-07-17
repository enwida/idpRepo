package de.enwida.web.dao.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IRightsDao;
import de.enwida.web.model.Right;
import de.enwida.web.model.User;

@Repository
public class RightsDaoImpl extends AbstractBaseDao<Right> implements IRightsDao {
	
	@Autowired
	private DataSource datasource;
	
    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	@Override
	public String getDbTableName() {
	    return "users.rights";
	}
	
	/**
	 * Enables or disables the Aspects in the database.So that right won't see that aspect 
	 */
    @Override
    public boolean enableDisableAspect(long rightID, boolean enabled) {
        String sql = "UPDATE users.rights SET enabled=? WHERE right_id=?";
        try{
        this.jdbcTemplate.update(sql,enabled,rightID);
        }catch(Exception e){
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
    
    @Override
    public Right mapRow(ResultSet rs, int rowNum) throws SQLException {
        Right right = new Right();
        right.setRightID(rs.getLong("right_id"));
        right.setAspectID(rs.getLong("aspect_id"));
        right.setRoleID(rs.getLong("role_id"));
        right.setEnabled(rs.getBoolean("enabled"));
        return right;
    }
}
