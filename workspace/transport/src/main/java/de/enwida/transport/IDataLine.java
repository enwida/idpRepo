package de.enwida.transport;

import java.util.Calendar;

public interface IDataLine {
	
	public String getTitle();
	public Product getProduct();
	public Calendar getStartTime();
	public Calendar getEndTime();
	public DataResolution getResolution();
	public boolean hasDateAxis();

}
