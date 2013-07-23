/**
 * 
 */
package de.enwida.web.dao.implementation;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IFileDao;
import de.enwida.web.db.model.UploadedFile;

/**
 * @author Jitin
 *
 */
@Repository
public class FileDaoImpl extends AbstractBaseDao<UploadedFile> implements
		IFileDao {

	public FileDaoImpl() {
		setModelClass(UploadedFile.class);
	}

	@Override
	public UploadedFile getFile(int fileId) {
		return findById(fileId);
	}

	@Override
	public UploadedFile getFileByFilePath(String filePath) {
		TypedQuery<UploadedFile> query = em.createQuery("from "
				+ UploadedFile.class.getName() + " where "
				+ UploadedFile.FILE_PATH + " = :filePath", UploadedFile.class);
		return query.setParameter("filePath", filePath).getSingleResult();
	}
}
