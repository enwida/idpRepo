package de.enwida.web.dao.implementation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IDataAvailibilityDao;
import de.enwida.web.model.DataAvailibility;

@Repository
public class DataAvailibilityDaoImpl extends AbstractBaseDao<DataAvailibility> implements IDataAvailibilityDao {
	
	@Override
	public DataAvailibility mapRow(ResultSet rs, int arg1) throws SQLException {
		DataAvailibility dataAvailibility = new DataAvailibility();
		dataAvailibility.setTableName(rs.getString("tablename"));
		dataAvailibility.setProduct(rs.getInt("product"));
		dataAvailibility.setTimeFrom(rs.getTimestamp("timefrom"));
		dataAvailibility.setTimeTo(rs.getTimestamp("timeto"));
		dataAvailibility.setNrows(rs.getInt("nrows"));
        return dataAvailibility;
	}

	public boolean isAvailableByExample(DataAvailibility dataAvailibility) {
		
		String SELECT_QUERY = "SELECT COUNT(*) FROM availability WHERE product = ? AND timefrom <= ? AND timeto >= ? AND tablename SIMILAR TO ?;";
		
		Object[] param = new Object[4];
		param[0] = dataAvailibility.getProduct();
		param[1] = new java.sql.Timestamp(dataAvailibility.getTimeFrom().getTime());
		param[2] = new java.sql.Timestamp(dataAvailibility.getTimeTo().getTime());
		param[3] = "%" + dataAvailibility.getTableName() + "%";
		
		int count = jdbcTemplate.queryForInt(SELECT_QUERY, param);
		return count > 0 ? true : false;
	}

	public DataAvailibility getByExample(DataAvailibility dataAvailibility) {
		String SELECT_QUERY = "SELECT * FROM availability WHERE product = ? AND timefrom <= ? AND timeto >= ? AND tablename SIMILAR TO ?;";
		
		Object[] param = new Object[4];
		param[0] = dataAvailibility.getProduct();
		param[1] = new java.sql.Timestamp(dataAvailibility.getTimeFrom().getTime());
		param[2] = new java.sql.Timestamp(dataAvailibility.getTimeTo().getTime());
		param[3] = "%" + dataAvailibility.getTableName() + "%";
		
		DataAvailibility dAvailability = jdbcTemplate.queryForObject(SELECT_QUERY, param, this);
		return dAvailability;
	}


	public List<DataAvailibility> getListByExample(DataAvailibility dataAvailibility) {
		String SELECT_QUERY = "SELECT * FROM availability WHERE product = ? AND tso = ? AND tablename SIMILAR TO ?;";
		
		Object[] param = new Object[3];
		param[0] = dataAvailibility.getProduct();
		param[1] = dataAvailibility.getTso();		
		param[2] = "%" + dataAvailibility.getTableName() + "%";		
		
		List<DataAvailibility> dAvailibilities = jdbcTemplate.query(SELECT_QUERY, param, this);
		return dAvailibilities;	
	}

}
