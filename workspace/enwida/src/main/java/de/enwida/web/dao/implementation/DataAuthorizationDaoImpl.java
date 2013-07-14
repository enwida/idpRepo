package de.enwida.web.dao.implementation;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IDataAutorizationDao;
import de.enwida.web.dao.rowmapper.DataAuthorizationRowMapper;
import de.enwida.web.model.DataAuthorization;

@Repository
public class DataAuthorizationDaoImpl extends AbstractBaseDao<DataAuthorization> implements IDataAutorizationDao {

	public boolean isAuthorizedByExample(DataAuthorization dataAuthorization) {
		String SELECT_QUERY = "SELECT COUNT(*) FROM data_authorization WHERE role = ? AND tso = ? AND product = ? AND aspect SIMILAR TO ? AND resolution SIMILAR TO ? AND time_from >= ? AND time_to <= ? AND enabled = ?;";
		
		Object[] param = new Object[8];
		param[0] = dataAuthorization.getRole();
		param[1] = dataAuthorization.getTso();
		param[2] = dataAuthorization.getProductId();
		param[3] = "%" + dataAuthorization.getAspect() + "%";
		param[4] = "%" + dataAuthorization.getResolution() + "%";
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

	public DataAuthorization getByExample(DataAuthorization dataAuthorization) {
		String SELECT_QUERY = "SELECT * FROM data_authorization WHERE role = ? AND tso = ? AND product = ? AND aspect SIMILAR TO ? AND resolution SIMILAR TO ? AND time_from >= ? AND time_to <= ? AND enabled = ?;";
		
		Object[] param = new Object[8];
		param[0] = dataAuthorization.getRole();
		param[1] = dataAuthorization.getTso();
		param[2] = dataAuthorization.getProductId();
		param[3] = "%" + dataAuthorization.getAspect() + "%";
		param[4] = "%" + dataAuthorization.getResolution() + "%";
		java.sql.Timestamp t1 = new java.sql.Timestamp(dataAuthorization.getTimeFrom().getTime());
		t1.setNanos(0);
		param[5] = t1;
		java.sql.Timestamp t2 = new java.sql.Timestamp(dataAuthorization.getTimeTo().getTime());
		t2.setNanos(0);
		param[6] = t2;
		param[7] = dataAuthorization.isEnabled();
		
		DataAuthorization dAuthorizartion = jdbcTemplate.queryForObject(SELECT_QUERY, param, new DataAuthorizationRowMapper());
		return dAuthorizartion;		
	}

	public List<DataAuthorization> getListByExample(DataAuthorization dataAuthorization) {
		String SELECT_QUERY = "SELECT * FROM data_authorization WHERE role = ? AND tso = ? AND product = ? AND aspect SIMILAR TO ? AND enabled = ?;";
		
		Object[] param = new Object[5];
		param[0] = dataAuthorization.getRole();
		param[1] = dataAuthorization.getTso();
		param[2] = dataAuthorization.getProductId();
		param[3] = "%" + dataAuthorization.getAspect() + "%";
		param[2] = dataAuthorization.isEnabled();
		
		List<DataAuthorization> dAuthorizartion = jdbcTemplate.queryForList(SELECT_QUERY, param, DataAuthorization.class);
		return dAuthorizartion;	
	}

	public void enableLine(DataAuthorization dataAuthorization) {
		String UPDATET_QUERY = "UPDATE data_authorization SET enabled = ? WHERE role = ? AND tso = ? AND product = ? AND aspect SIMILAR TO ?;";
		
		Object[] param = new Object[4];
		param[0] = dataAuthorization.isEnabled();
		param[1] = dataAuthorization.getRole();
		param[2] = dataAuthorization.getTso();
		param[3] = dataAuthorization.getProductId();
		param[4] = "%" + dataAuthorization.getAspect() + "%";	
		
		jdbcTemplate.update(UPDATET_QUERY, param);
	}
	
}