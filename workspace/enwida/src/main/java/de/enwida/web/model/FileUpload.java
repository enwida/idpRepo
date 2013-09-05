package de.enwida.web.model;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import de.enwida.transport.Aspect;

public class FileUpload {
	
	private CommonsMultipartFile file;
	private String fileIdToBeReplaced;
	private int revision;
	private Aspect aspect;
	
	public CommonsMultipartFile getFile() {
		return file;
	}

	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}

	public String getFileIdToBeReplaced() {
		return fileIdToBeReplaced;
	}

	public void setFileIdToBeReplaced(String fileIdToBeReplaced) {
		this.fileIdToBeReplaced = fileIdToBeReplaced;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}
	
	public Aspect getAspect() {
		return aspect;
	}
	
	public void setAspect(Aspect aspect) {
		this.aspect = aspect;
	}
}