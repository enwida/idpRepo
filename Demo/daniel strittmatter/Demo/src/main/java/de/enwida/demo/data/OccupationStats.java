package de.enwida.demo.data;

import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;


public class OccupationStats {

	private OccupationDataPointMappingQuery mappingQuery;
	
	public OccupationStats(DataSource dataSource) {
		this.mappingQuery = new OccupationDataPointMappingQuery(dataSource);
	}
	
	public List<OccupationDataPoint> getDataPointsInDateRange(int startEpochSecs, int endEpochSecs) {
		return mappingQuery.execute(startEpochSecs, endEpochSecs);
	}
	
	public List<OccupationDataPoint> getDataPointsInDateRange(Calendar start, Calendar end) {
		final int startEpochSecs = (int) (start.getTimeInMillis() / 1000);
		final int endEpochSecs = (int) (end.getTimeInMillis() / 1000);
		return getDataPointsInDateRange(startEpochSecs, endEpochSecs);
	}
	
	public List<OccupationDataPoint> getAllDataPoints() {
		return getDataPointsInDateRange(0, Integer.MAX_VALUE);
	}
	
}
