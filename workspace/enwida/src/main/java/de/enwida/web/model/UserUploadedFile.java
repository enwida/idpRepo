package de.enwida.web.model;

import java.util.Date;

import de.enwida.web.db.model.UploadedFile;

public class UserUploadedFile {
	private long id;
	private String displayFileName;
	private String fileName;
	private Date uploadDate;
	
	public UserUploadedFile(UploadedFile file) {
		this.id = file.getId();
		this.displayFileName = file.getDisplayFileName();
		this.fileName = file.getFileName();
		this.uploadDate = file.getUploadDate();
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getDisplayFileName() {
		return displayFileName;
	}
	
	public void setDisplayFileName(String displayFileName) {
		this.displayFileName = displayFileName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Date getUploadDate() {
		return uploadDate;
	}
	
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}
}
