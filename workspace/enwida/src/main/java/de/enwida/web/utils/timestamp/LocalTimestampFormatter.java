package de.enwida.web.utils.timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalTimestampFormatter implements ITimestampFormatter {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

	@Override
	public String format(Date date) {
		return dateFormat.format(date);
	}

}
