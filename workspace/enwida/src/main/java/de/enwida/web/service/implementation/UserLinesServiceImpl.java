/**
 * 
 */
package de.enwida.web.service.implementation;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.IUserLinesDao;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.db.model.UserLines;
import de.enwida.web.db.model.UserLinesMetaData;
import de.enwida.web.service.interfaces.IUserLinesService;

/**
 * @author Jitin
 *
 */
@Service("userLinesService")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional
public class UserLinesServiceImpl implements IUserLinesService {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private IUserLinesDao userLinesDao;

	@Override
	public boolean createUserLine(UserLines line) {
		if (userLinesDao.getUserLine(line) == null) {
			userLinesDao.create(line);
			return true;
		} else {
			// User line already present donot write
			return false;
		}
	}

	@Override
	public void createUserLineMetaData(UserLinesMetaData metaData) {
		userLinesDao.createUserLineMetaData(metaData);
	}

	@Override
	public void updateUserLineMetaData(UserLinesMetaData metaData) {
		userLinesDao.updateUserLineMetaData(metaData);
	}

	@Override
	public boolean createUserLines(List<UserLines> lines,
			UserLinesMetaData metaData) {
		boolean singleRecordCreate = false;
		int numberOfRecordsWritten = 0;
		for (UserLines line : lines) {
			line.setLineMetaData(metaData);
			boolean createstatus = createUserLine(line);
			if (createstatus) {
				singleRecordCreate = true;
				// get updated metadata with id
				metaData = line.getLineMetaData();
				numberOfRecordsWritten += 1;
			}
		}
		logger.debug("Number of records written : " + numberOfRecordsWritten);
		return singleRecordCreate;
	}

	@Override
	public void createUserLineMetaData(UserLinesMetaData metaData,
			UploadedFile file) {
		metaData.setFile(file);
		createUserLineMetaData(metaData);

	}
}
