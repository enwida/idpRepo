package de.enwida.web.dao.interfaces;

import de.enwida.web.db.model.UploadedFile;

public interface IFileDao extends IDao<UploadedFile> {
	UploadedFile getFile(int fileId);

	UploadedFile getFileByFilePath(String filePath);
}