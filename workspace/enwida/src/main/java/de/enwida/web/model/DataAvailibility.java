package de.enwida.web.model;

import java.util.Date;

public class DataAvailibility {
	
	private String tableName;
	private int product;
	private int tso;
	private Date timeFrom;
	private Date timeTo;
	private int nrows;
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getProduct() {
		return product;
	}

	public void setProduct(int product) {
		this.product = product;
	}
	
	public int getTso() {
		return tso;
	}

	public void setTso(int tso) {
		this.tso = tso;
	}

	public Date getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Date timeFrom) {
		this.timeFrom = timeFrom;
	}

	public Date getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(Date timeTo) {
		this.timeTo = timeTo;
	}

	public int getNrows() {
		return nrows;
	}

	public void setNrows(int nrows) {
		this.nrows = nrows;
	}
}
