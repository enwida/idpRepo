package de.enwida.web.model;


/**
 * getter and setter class of the data that is needed to initiate the charts
 * including the navigation buttons. it also holds the {@link ChartOptions} and
 * {@link TableOptions}.
 * 
 * @author root
 * 
 */
public class ChartInitData {

	private int width;
	private int height;
	private String title;

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
