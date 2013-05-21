package de.enwida.web.model;


/**
 * getter and setter class of the data that is needed to initiate the charts
 * including the navigation buttons
 * 
 * @author root
 * 
 */
public class ChartNavigationData {

	private int width;
	private int height;
	private String title;
	
	public ChartNavigationData() { }
	
	public ChartNavigationData(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
