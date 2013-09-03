package de.enwida.web.utils.timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface ITimestampFormatter {
	
	public String format(Date date);
	public SimpleDateFormat getDateFormat();
	public void setDateFormat(SimpleDateFormat format);

}
