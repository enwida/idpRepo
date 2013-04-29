package de.enwida.demo.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;


public class OccupationDataPointMappingQuery extends MappingSqlQuery<OccupationDataPoint> {

	public OccupationDataPointMappingQuery(DataSource ds) {
		super(ds, "SELECT timestamp, count FROM counts WHERE timestamp BETWEEN ? AND ?");
		declareParameter(new SqlParameter("start", Types.INTEGER));
		declareParameter(new SqlParameter("end", Types.INTEGER));
		compile();
	}
	
	@Override
	protected OccupationDataPoint mapRow(ResultSet rs, int rowNumber) throws SQLException {
		final int epochSecs = rs.getInt(1);
		final int count = rs.getInt(2);
		
		final Calendar timestamp = Calendar.getInstance();
		timestamp.setTimeInMillis(epochSecs * 1000l);
		
		return new OccupationDataPoint(timestamp, count);
	}

}
