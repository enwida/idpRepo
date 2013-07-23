package de.enwida.web.dao.interfaces;

import de.enwida.web.db.model.UploadedFile;

public interface IFileDao {
	UploadedFile getFile(int fileId);

	UploadedFile getFileByFilePath(String filePath);
}
