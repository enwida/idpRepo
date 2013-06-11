package de.enwida.transport;

import java.util.Calendar;

public interface IDataLine {
	
	public int getProduct();
	public Calendar getStartTime();
	public Calendar getEndTime();
	public DataResolution getResolution();
	public Aspect getAspect();
	public String getTitle();

}
