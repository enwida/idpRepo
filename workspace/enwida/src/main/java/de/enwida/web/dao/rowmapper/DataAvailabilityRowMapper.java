package de.enwida.web.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import de.enwida.web.model.DataAvailibility;

public class DataAvailabilityRowMapper implements RowMapper<DataAvailibility> {

	public DataAvailibility mapRow(ResultSet rs, int arg1) throws SQLException {
		DataAvailibility dataAvailibility = new DataAvailibility();
		dataAvailibility.setTableName(rs.getString("tablename"));
		dataAvailibility.setProduct(rs.getInt("product"));
		dataAvailibility.setTimeFrom(rs.getTimestamp("timefrom"));
		dataAvailibility.setTimeTo(rs.getTimestamp("timeto"));
		dataAvailibility.setNrows(rs.getInt("nrows"));
        return dataAvailibility;
	}

}
