/**
 * 
 */
package de.enwida.web.dao.implementation;

import org.springframework.stereotype.Repository;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IUserLinesDao;
import de.enwida.web.db.model.UserLines;
import de.enwida.web.db.model.UserLinesMetaData;

/**
 * @author Jitin
 *
 */
@Repository
public class UserLinesDaoImpl extends AbstractBaseDao<UserLines> implements
		IUserLinesDao {

	/**
	 * 
	 */
	public UserLinesDaoImpl() {
		setModelClass(UserLines.class);
	}

	@Override
	public void create(UserLinesMetaData metaData) {
		create(metaData);
	}
}
