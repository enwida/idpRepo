package de.enwida.web.dao.implementation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.db.model.NavigationSettings;
import de.enwida.web.model.User;

@Repository
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public class NavigationDaoImpl extends AbstractBaseDao<NavigationSettings> implements INavigationDao {

	@Autowired
	private MessageSource messageSource;

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

	@Override
	public Set<NavigationSettings> getNavigationSettingsByUserID(int userId) {
		final TypedQuery<NavigationSettings> typedQuery = em.createQuery(
				"from " + NavigationSettings.class.getName() +
				" where " + User.USER_ID + " = :userId",
				NavigationSettings.class);

		typedQuery.setParameter("userId", userId);
		final List<NavigationSettings> result = typedQuery.getResultList();
		return new HashSet<NavigationSettings>(result);
	}

	@Override
	public Set<NavigationSettings> getNavigationSettingsByClientID(String clientId) {
		final TypedQuery<NavigationSettings> typedQuery = em.createQuery(
				"from " + NavigationSettings.class.getName() +
				" where " + User.CLIENT_ID + " = :clientId",
				NavigationSettings.class);

		typedQuery.setParameter("clientId", clientId);
		final List<NavigationSettings> result = typedQuery.getResultList();
		return new HashSet<NavigationSettings>(result);
	}

	@Override
	public void saveUserNavigationSettings(NavigationSettings navigationSettings) {
		em.merge(navigationSettings);
	}

}
