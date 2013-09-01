package de.enwida.transport;


public interface IDataLine {
	
	public LineRequest getLineRequest();
	public String getTitle();
	public void setTitle(String title);
	public String getUnit();
	public void setUnit(String unit);

}
