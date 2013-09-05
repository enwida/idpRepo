/**
 * 
 */
package de.enwida.web.dao.implementation;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.rl.controller.UserLinesController;
import de.enwida.rl.dtos.DOUserLines;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IUserLinesDao;
import de.enwida.web.db.model.UserLinesMetaData;

/**
 * @author Jitin
 *
 */
@Repository
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public class UserLinesDaoImpl extends AbstractBaseDao<UserLinesMetaData>
		implements IUserLinesDao {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private UserLinesController ulc;

	@Override
	public boolean createUserLines(List<DOUserLines> userlines) {
		int recordsWritten = ulc.writeUserLines(userlines);
		logger.debug("Number of records written : " + recordsWritten);
		if (recordsWritten == userlines.size()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<DOUserLines> getUserLines(long userLineId) {
		return ulc.readUserLines(userLineId);
	}

	@Override
	public boolean deleteUserLines(long userLineId) {
		int deletedRecords = ulc.deleteUserLines(userLineId);
		logger.debug("Number of records deleted : " + deletedRecords);
		return true;
	}

}
