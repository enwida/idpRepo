package de.enwida.demo.data;

import java.util.Calendar;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import de.enwida.demo.json.CalendarEpochSecsSerializer;

public class OccupationDataPoint {

	@JsonProperty
	@JsonSerialize(using=CalendarEpochSecsSerializer.class)
	private Calendar timestamp;
	@JsonProperty
	private int count;
	
	public OccupationDataPoint(Calendar timestamp, int count) {
		this.timestamp = timestamp;
		this.count = count;
	}
	
	public Calendar getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
}
