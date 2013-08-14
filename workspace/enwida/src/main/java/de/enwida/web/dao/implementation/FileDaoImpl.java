/**
 * 
 */
package de.enwida.web.dao.implementation;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IFileDao;
import de.enwida.web.db.model.UploadedFile;

/**
 * @author Jitin
 *
 */
@Repository
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public class FileDaoImpl extends AbstractBaseDao<UploadedFile> implements
		IFileDao {

	@Override
	public UploadedFile getFile(int fileId) {
		return fetchById(fileId);
	}

	@Override
	public UploadedFile getFileByFilePath(String filePath) {
		TypedQuery<UploadedFile> query = em.createQuery("from "
				+ UploadedFile.class.getName() + " where "
				+ UploadedFile.FILE_PATH + " = :filePath", UploadedFile.class);
		return query.setParameter("filePath", filePath).getSingleResult();
	}
}
