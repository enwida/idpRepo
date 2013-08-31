package de.enwida.transport;

import java.util.Calendar;

public interface IDataLine {
	
	public int getProduct();
	public Calendar getStartTime();
	public Calendar getEndTime();
	public DataResolution getResolution();
	public Aspect getAspect();
	public String getTitle();
	public void setTitle(String title);
	public String getUnit();
	public void setUnit(String unit);
	public int getTso();
	public void setTso(int tso);

}
