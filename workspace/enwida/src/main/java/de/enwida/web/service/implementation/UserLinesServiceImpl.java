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
	public void createUserLineMetaData(UserLinesMetaData metaData) {
		userLinesDao.create(metaData, true);
	}

	@Override
	public void updateUserLineMetaData(UserLinesMetaData metaData) {
		userLinesDao.update(metaData, true);
	}

	@Override
	public boolean createUserLines(List<DOUserLines> lines,
			UserLinesMetaData metaData) {

		if (metaData.getMetaDataId() == 0) {
			createUserLineMetaData(metaData);
		}
		for (DOUserLines line : lines) {
			line.setMetaDataId(metaData.getMetaDataId());
		}
		return userLinesDao.createUserLines(lines);
	}

	@Override
	public void createUserLineMetaData(UserLinesMetaData metaData,
			UploadedFile file) {
		metaData.setFile(file);
		createUserLineMetaData(metaData);

	}

	@Override
	public boolean eraseUserLines(int metaDataId) {
		return userLinesDao.deleteUserLines(metaDataId);
	}

	@Override
	public boolean eraseUserLineMetaData(long fileId) {
		UploadedFile oldFile = fileDao.getFile(fileId);
		if (oldFile.getMetaData() != null) {
			// First delete all user lines
			boolean linesremoved = eraseUserLines(oldFile.getMetaData()
					.getMetaDataId());
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
	public List<DOUserLines> getUserLines(int metaDataId) {
		return userLinesDao.getUserLines(metaDataId);
	}
}
