package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.db.model.UploadedFile;

public interface IFileDao extends IDao<UploadedFile> {
	UploadedFile getFile(long fileId, int revision);

	UploadedFile getFileByFilePath(String filePath);

	UploadedFile fetchActiveFileByFileId(long id);

	List<UploadedFile> fetchFilesByFileId(long id);
}
