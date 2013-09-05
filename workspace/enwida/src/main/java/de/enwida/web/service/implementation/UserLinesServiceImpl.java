/**
 * 
 */
package de.enwida.web.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.rl.dtos.DOUserLines;
import de.enwida.web.dao.interfaces.IFileDao;
import de.enwida.web.dao.interfaces.IUserLinesDao;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.db.model.UserLinesMetaData;
import de.enwida.web.service.interfaces.IUserLinesService;

/**
 * @author Jitin
 *
 */
@Service("userLinesService")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public class UserLinesServiceImpl implements IUserLinesService {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private IUserLinesDao userLinesDao;
	
	@Autowired
	private IFileDao fileDao;

	@Override
	public boolean createUserLine(DOUserLines line) {
		List<DOUserLines> userlines = new ArrayList<DOUserLines>();
		userlines.add(line);
		return createUserLines(userlines);
	}

	@Override
	public void createUserLineMetaData(UserLinesMetaData metaData) throws Exception {
		userLinesDao.create(metaData, true);
	}

	@Override
	public void updateUserLineMetaData(UserLinesMetaData metaData) throws Exception {
		userLinesDao.update(metaData, true);
	}

	@Override
	public boolean createUserLines(List<DOUserLines> lines, long userLineId)
			throws Exception {

		for (DOUserLines line : lines) {
			line.setUserLineId(userLineId);
		}
		return userLinesDao.createUserLines(lines);
	}

	@Override
	public void createUserLineMetaData(UserLinesMetaData metaData,
			UploadedFile file) throws Exception {
		// metaData.setFile(file);
		createUserLineMetaData(metaData);

	}

	@Override
	public boolean eraseUserLines(long userLineId) {
		return userLinesDao.deleteUserLines(userLineId);
	}

	@Override
	public boolean eraseUserLineMetaData(long fileId, int revision)
			throws Exception {
		UploadedFile oldFile = fileDao.getFile(fileId, revision);
		if (oldFile.getMetaData() != null) {
			// First delete all user lines
			boolean linesremoved = eraseUserLines(oldFile
					.getUserLineIdOnNewRevision());
			if (linesremoved) {
				// delete user lines
				userLinesDao.delete(oldFile.getMetaData());
			}
		}
		return true;
	}

	@Override
	public boolean createUserLines(List<DOUserLines> userlines) {
		return userLinesDao.createUserLines(userlines);
	}

	@Override
	public List<DOUserLines> getUserLines(long userLineId) {
		return userLinesDao.getUserLines(userLineId);
	}
}
