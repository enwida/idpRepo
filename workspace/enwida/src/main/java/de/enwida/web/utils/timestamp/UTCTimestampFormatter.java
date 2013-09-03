package de.enwida.web.utils.timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class UTCTimestampFormatter implements ITimestampFormatter {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public String format(Date date) {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}
	
	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	
	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

}
