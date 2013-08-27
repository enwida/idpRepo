/**
 * 
 */
package de.enwida.web.dao.implementation;

import java.util.List;

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
	public UploadedFile getFile(long fileId) {
		return fetchById(fileId);
	}

	@Override
	public UploadedFile getFileByFilePath(String filePath) {
		TypedQuery<UploadedFile> query = em.createQuery("from "
				+ UploadedFile.class.getName() + " where "
				+ UploadedFile.FILE_PATH + " = :filePath", UploadedFile.class);
		return query.setParameter("filePath", filePath).getSingleResult();
	}

	@Override
	public List<UploadedFile> fetchByFileSetUniqueIdentifier(
			String fileSetUniqueIdentifier) {
		TypedQuery<UploadedFile> query = em.createQuery("from "
				+ UploadedFile.class.getName() + " where "
				+ UploadedFile.FILE_SET_UNIQUE_IDENTIFIER + " = :fileSetUniqueIdentifier", UploadedFile.class);
		return query.setParameter("fileSetUniqueIdentifier", fileSetUniqueIdentifier).getResultList();
	}

	@Override
	public UploadedFile fetchActiveFileByFileSetUniqueIdentifier(
			String fileSetUniqueIdentifier) {
		TypedQuery<UploadedFile> query = em.createQuery("from "
				+ UploadedFile.class.getName() + " where "
				+ UploadedFile.FILE_SET_UNIQUE_IDENTIFIER + " = :fileSetUniqueIdentifier" + " AND "
				+ UploadedFile.ACTIVE + " = :active", UploadedFile.class);
		return query.setParameter("fileSetUniqueIdentifier", fileSetUniqueIdentifier)
				.setParameter("active", true).getSingleResult();
	}
}
