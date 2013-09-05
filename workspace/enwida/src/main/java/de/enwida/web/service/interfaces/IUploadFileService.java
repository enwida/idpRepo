package de.enwida.web.service.interfaces;

import java.util.List;

import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.model.User;
import de.enwida.web.validator.FileValidator;

public interface IUploadFileService {
	
	public UploadedFile getFile(long fileId, int revision);
    public UploadedFile getFileByFilePath(String filePath);
    public int getUploadedFileVersion(UploadedFile file, User user);
    public List<UploadedFile> getUploadedFiles(User user);
    
    User saveUserUploadedFile(User user, UploadedFile file) throws Exception;
    User updateUserUploadedFile(User user, UploadedFile file) throws Exception;
	void removeUserUploadedFile(User user, UploadedFile file) throws Exception;
	
	public void makeFileActive(long fileId, int revision, User user,
			FileValidator fileValidator) throws Exception;

	public List<UploadedFile> getFileSetByFileId(long fileid);

	User replaceUserUploadedFile(User user, UploadedFile file, UploadedFile oldFile) throws Exception;
}
