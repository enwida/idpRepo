package de.enwida.web.model;

import java.util.Date;

import de.enwida.web.db.model.UploadedFile;

public class UserUploadedFile {
	private long id;
	private int revision;
	private String displayFileName;
	private String fileName;
	private Date uploadDate;
	private boolean active;
	
	public UserUploadedFile(UploadedFile file) {
		this.id = file.getUploadedFileId().getId();
		this.revision = file.getUploadedFileId().getRevision();
		this.displayFileName = file.getDisplayFileName();
		this.fileName = file.getFileName();
		this.uploadDate = file.getUploadDate();
		this.active = file.isActive();
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
