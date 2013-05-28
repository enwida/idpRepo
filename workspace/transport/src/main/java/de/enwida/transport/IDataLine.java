package de.enwida.transport;

import java.util.Calendar;
import java.util.List;

public interface IDataLine {
	
	public List<String> getTitles();
	public int getProduct();
	public Calendar getStartTime();
	public Calendar getEndTime();
	public DataResolution getResolution();
	public boolean getHasDateAxis();

}
