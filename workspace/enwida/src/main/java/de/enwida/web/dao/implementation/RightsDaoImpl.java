package de.enwida.web.dao.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.enwida.transport.Aspect;
import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IRightsDao;
import de.enwida.web.model.DataAuthorization;
import de.enwida.web.model.Right;

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
    

    public boolean isAuthorizedByExample(DataAuthorization dataAuthorization) {
        String SELECT_QUERY = "SELECT COUNT(*) FROM users.rights WHERE role_id = ? AND tso = ? AND product = ? AND aspect_id = ? AND resolution = ? AND time_from >= ? AND time_to <= ? AND enabled = ?;";
        
        Object[] param = new Object[8];
        param[0] = dataAuthorization.getRole();
        param[1] = dataAuthorization.getTso();
        param[2] = dataAuthorization.getProductId();
        param[3] = Aspect.valueOf(dataAuthorization.getAspect()).ordinal();
        param[4] = dataAuthorization.getResolution();
        java.sql.Timestamp t1 = new java.sql.Timestamp(dataAuthorization.getTimeFrom().getTime());
        t1.setNanos(0);
        param[5] = t1;
        java.sql.Timestamp t2 = new java.sql.Timestamp(dataAuthorization.getTimeTo().getTime());
        t2.setNanos(0);
        param[6] = t2;
        param[7] = dataAuthorization.isEnabled();
        
        int count = jdbcTemplate.queryForInt(SELECT_QUERY, param);
        return count > 0 ? true : false;
    }

    public List<DataAuthorization> getListByExample(DataAuthorization dataAuthorization) {
        String SELECT_QUERY = "SELECT * FROM users.rights WHERE role_id = ? AND tso = ? AND product = ? AND aspect_id = ? AND enabled = ?;";
        
        Object[] param = new Object[5];
        param[0] = dataAuthorization.getRole();
        param[1] = dataAuthorization.getTso();
        param[2] = dataAuthorization.getProductId();
        param[3] = Aspect.valueOf(dataAuthorization.getAspect()).ordinal();
        param[4] = dataAuthorization.isEnabled();
        
        List<DataAuthorization> dAuthorizartion = jdbcTemplate.queryForList(SELECT_QUERY, param, DataAuthorization.class);
        return dAuthorizartion; 
    }

    public void enableLine(DataAuthorization dataAuthorization) {
        String UPDATET_QUERY = "UPDATE users.rights SET enabled = ? WHERE role_id = ? AND tso = ? AND product = ? AND aspect_id = ?;";
        
        Object[] param = new Object[4];
        param[0] = dataAuthorization.isEnabled();
        param[1] = dataAuthorization.getRole();
        param[2] = dataAuthorization.getTso();
        param[3] = dataAuthorization.getProductId();
        param[4] = Aspect.valueOf(dataAuthorization.getAspect()).ordinal();
        
        jdbcTemplate.update(UPDATET_QUERY, param);
    }
    
}
