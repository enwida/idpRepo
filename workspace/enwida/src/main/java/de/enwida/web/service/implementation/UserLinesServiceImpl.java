/**
 * 
 */
package de.enwida.web.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.IUserLinesDao;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.db.model.UserLines;
import de.enwida.web.db.model.UserLinesMetaData;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUserLinesService;

/**
 * @author Jitin
 *
 */
@Service("userLinesService")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional
public class UserLinesServiceImpl implements IUserLinesService {
	@Autowired
	private IUserLinesDao userLinesDao;

	@Override
	public void createUserLine(UserLines line) {
		userLinesDao.create(line);
	}

	@Override
	public void createUserLineMetaData(UserLinesMetaData metaData) {
		userLinesDao.create(metaData);
	}

	@Override
	public void createUserLines(List<UserLines> lines,
			UserLinesMetaData metaData, User user, UploadedFile file) {
		for (UserLines line : lines) {
			metaData.setOwner(user);
			metaData.setFile(file);
			line.setLineMetaData(metaData);
			createUserLine(line);
		}
	}

	@Override
	public void createUserLineMetaData(UserLinesMetaData metaData, User user,
			UploadedFile file) {
		metaData.setFile(file);
		metaData.setOwner(user);
		createUserLineMetaData(metaData);

	}
}
