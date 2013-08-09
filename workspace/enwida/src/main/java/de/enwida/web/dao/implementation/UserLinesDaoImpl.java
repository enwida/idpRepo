/**
 * 
 */
package de.enwida.web.dao.implementation;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
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

	private Logger logger = Logger.getLogger(getClass());

	@Override
	public boolean createUserLineMetaData(UserLinesMetaData metaData) {
		em.persist(metaData);
		return true;
	}

	@Override
	public boolean updateUserLineMetaData(UserLinesMetaData metaData) {
		metaData = em.merge(metaData);
		return true;
	}

	@Override
	public UserLines getUserLine(UserLines userline) {
		UserLines line = null;
		try {
			TypedQuery<UserLines> typedQuery = em.createQuery("from "
					+ UserLines.class.getName() + " WHERE "
					+ UserLines.TIMESTAMP + "= :timestamp AND "
					+ UserLines.VALUE + "=:value", UserLines.class);
			typedQuery.setParameter("timestamp", userline.getTimestamp());
			typedQuery.setParameter("value", userline.getValue());
			line = typedQuery.getSingleResult();
		} catch (NoResultException noresult) {
			// if there is no result
			logger.error("No user line found : " + userline);
		} catch (NonUniqueResultException notUnique) {
			// if more than one result
			logger.error("More than one userlines found : " + userline);
		}
		return line;
	}
}
