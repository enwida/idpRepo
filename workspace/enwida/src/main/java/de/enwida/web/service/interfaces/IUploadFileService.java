package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.model.User;
import de.enwida.web.validator.FileValidator;

public interface IUploadFileService {
	
	public UploadedFile getFile(int fileId);
    public UploadedFile getFileByFilePath(String filePath);
    public int getUploadedFileVersion(UploadedFile file, User user);
    public List<UploadedFile> getUploadedFiles(User user);
    
    User saveUserUploadedFile(User user, UploadedFile file);
    User updateUserUploadedFile(User user, UploadedFile file);
	void removeUserUploadedFile(User user, UploadedFile file) throws Exception;
	
	public void makeFileActive(int fileId, User user, FileValidator fileValidator);	
}
