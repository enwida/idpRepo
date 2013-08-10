package de.enwida.web.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import de.enwida.web.model.DataAuthorization;

public class DataAuthorizationRowMapper implements RowMapper<DataAuthorization> {

	public DataAuthorization mapRow(ResultSet rs, int arg1) throws SQLException {
		DataAuthorization dataAuthorization = new DataAuthorization();
		dataAuthorization.setRole(rs.getInt("role"));
		dataAuthorization.setTso(rs.getInt("tso"));
		dataAuthorization.setProductId(rs.getInt("product"));
		dataAuthorization.setAspect(rs.getString("aspect"));
		dataAuthorization.setResolution(rs.getString("resolution"));
		dataAuthorization.setTimeFrom(rs.getTimestamp("time_from"));
		dataAuthorization.setTimeTo(rs.getTimestamp("time_to"));
        return dataAuthorization;
	}

}
