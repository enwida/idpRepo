package de.enwida.web.dao.interfaces;

import java.util.List;

import de.enwida.web.db.model.UploadedFile;

public interface IFileDao extends IDao<UploadedFile> {
	UploadedFile getFile(long fileId);

	UploadedFile getFileByFilePath(String filePath);

	List<UploadedFile> fetchByFileSetUniqueIdentifier(
			String fileSetUniqueIdentifier);

	UploadedFile fetchActiveFileByFileSetUniqueIdentifier(
			String fileSetUniqueIdentifier);
}
