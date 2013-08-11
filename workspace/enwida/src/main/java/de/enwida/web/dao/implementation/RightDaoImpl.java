package de.enwida.web.dao.implementation;

import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.enwida.transport.Aspect;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.model.AuthorizationRequest;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;

@Repository
public class RightDaoImpl extends AbstractBaseDao<Right> implements IRightDao {
	
	@Autowired
	private DataSource datasource;
	
	/**
	 * Enables or disables the Aspects in the database.So that right won't see that aspect 
	 */
    @Override
    public boolean enableDisableAspect(long rightID, boolean enabled) throws Exception{
		Right right = fetchById(rightID);
		right.setEnabled(enabled);
		update(right);
        return true;
    }
    
    @Deprecated
    @Override
    public Right mapRow(ResultSet rs, int rowNum) throws SQLException {
        Right right = new Right();
        right.setRightID(rs.getLong("right_id"));
		right.setRole(new Role(rs.getLong("role_id")));
        right.setEnabled(rs.getBoolean("enabled"));
        right.setTso(rs.getInt("tso"));
        right.setProduct(rs.getInt("product"));
        right.setAspect(Aspect.values()[rs.getInt("aspect_id")].name());
        right.setResolution(rs.getString("resolution"));
        right.setTimeFrom(rs.getDate("time1"));
        right.setTimeTo(rs.getDate("time2"));
        return right;
    }
    

    public boolean isAuthorizedByExample(Right dataAuthorization) throws Exception{
        String SELECT_QUERY = "SELECT COUNT(*) FROM users.rights WHERE role_id = ? AND tso = ? AND product = ? AND aspect_id = ? AND resolution = ? AND time1 <= ? AND time2 >= ? AND enabled = ?;";
        
        Object[] param = new Object[8];
		param[0] = dataAuthorization.getRole();
        param[1] = dataAuthorization.getTso();
        param[2] = dataAuthorization.getProduct();
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
    
    @Override    public List<CalendarRange> getAllowedTimeRanges(AuthorizationRequest request) throws Exception {
        String SELECT_QUERY = "SELECT time1, time2 FROM users.rights WHERE role_id in ? AND tso = ? AND product = ? AND aspect_id = ? AND resolution = ? AND enabled = true;";

        final Connection connection = datasource.getConnection();
        final PreparedStatement stmt = connection.prepareStatement(SELECT_QUERY);
        final Array roleArray = connection.createArrayOf("int", request.getUser().getRoles().toArray());
        stmt.setArray(1, roleArray);
        stmt.setInt(2, request.getTso());
        stmt.setInt(3, request.getProduct());
        stmt.setInt(4, request.getAspect().ordinal());
        stmt.setString(5, request.getResolution().name());
        
        final ResultSet result = stmt.executeQuery();
        final List<CalendarRange> ranges = new ArrayList<>();
        while (result.next()) {
	        final Date from = result.getDate(1);
	        final Date to = result.getDate(2);
	        final CalendarRange range = new CalendarRange(from, to);
	        ranges.add(range);
        }
        return ranges;
    }
    
    public void addRight(Right right) {
        create(right);
    }
    
    @Override
    public void removeRight(Right right) throws Exception {
        delete(right);
    }

    public List<Right> getListByExample(Right dataAuthorization)throws Exception {
        String SELECT_QUERY = "SELECT * FROM users.rights WHERE role_id = ? AND tso = ? AND product = ? AND aspect_id = ? AND enabled = ?;";
        
        Object[] param = new Object[5];
		param[0] = dataAuthorization.getRole();
        param[1] = dataAuthorization.getTso();
        param[2] = dataAuthorization.getProduct();
        param[3] = Aspect.valueOf(dataAuthorization.getAspect()).ordinal();
        param[4] = dataAuthorization.isEnabled();
        
        List<Right> dAuthorizartion = jdbcTemplate.query(SELECT_QUERY, param, this);
        return dAuthorizartion; 
    }

    public void enableLine(Right dataAuthorization) throws Exception{
        update(dataAuthorization);
    }

    @Override
    public List<Right> getAllAspects(long roleID) throws Exception{

        String sql = "SELECT * FROM users.rights WHERE role_id=? LIMIT 10 OFFSET 10";
        return jdbcTemplate.query(sql, new Object[]{roleID}, this);
    }
    
}
