package de.enwida.web.dao.implementation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import de.enwida.transport.DataResolution;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.db.model.CalendarRange;
import de.enwida.web.db.model.NavigationSettings;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.ProductTree;
import de.enwida.web.model.User;
import de.enwida.web.utils.ProductLeaf;
import de.enwida.web.utils.ProductNode;

@Repository
public class NavigationDaoImpl extends AbstractBaseDao<User> implements INavigationDao {

	@Autowired
	private MessageSource messageSource;

	private Logger logger = Logger.getLogger(getClass());

    public ChartNavigationData getDefaultNavigation(int chartId, Locale locale) {
	    final String chartTitle = getChartMessage("title", chartId, locale);
	    final String xAxisLabel = getChartMessage("xlabel", chartId, locale);
	    final String yAxisLabel = getChartMessage("ylabel", chartId, locale);
	    
		// final ChartNavigationData navigationData = new
		// ChartNavigationData(chartTitle, xAxisLabel, yAxisLabel);
		final ChartNavigationData navigationData = new ChartNavigationData();
	    fillDefaultProducts(navigationData);
	    

	    return navigationData;
    }
    
    private String getChartMessage(String property, int chartId, Locale locale) {
	    return messageSource.getMessage("de.enwida.chart." + chartId + "." + property, null, "", locale);
    }
    
    // FIXME: Store default navigation in JSON format
    private void fillDefaultProducts(ChartNavigationData navigationData) {
	    final List<DataResolution> allResolutions = Arrays.asList(DataResolution.values());
	    
	    // ProdA (type of RC)
	    final ProductNode prodSCR = new ProductNode(2, "SCR");
	    final ProductNode prodTCR = new ProductNode(3, "TCR");
	    
	    // ProdB (time slot)
	    final ProductNode prodWholeDay = new ProductNode(1, "");

	    final ProductNode prodPT = new ProductNode(1, "PT");
	    final ProductNode prodOPT = new ProductNode(2, "OPT");

	    final ProductNode prod04 = new ProductNode(1, "0-4");
	    final ProductNode prod48 = new ProductNode(2, "4-8");
	    final ProductNode prod812 = new ProductNode(3, "8-12");
	    final ProductNode prod1216 = new ProductNode(4, "12-16");
	    final ProductNode prod1620 = new ProductNode(5, "16-20");
	    final ProductNode prod2024 = new ProductNode(6, "20-24");
	    
	    // ProdC (positive / negative) as leaves
	    final ProductLeaf prodPos = new ProductLeaf(1, "pos", allResolutions, CalendarRange.always());
	    final ProductLeaf prodNeg = new ProductLeaf(2, "neg", allResolutions, CalendarRange.always());
	    
	    // Add pos/neg to every time slot
	    for (final ProductNode timeslot : new ProductNode[]
	        { prodWholeDay, prodPT, prodOPT, prod04, prod48, prod812, prod1216, prod1620, prod2024 }
	    ) {
	        timeslot.addChild(prodPos);
	        timeslot.addChild(prodNeg);
	    }
	    
	    // Append time slots to RC types
	    prodSCR.addChild(prodPT);
	    prodSCR.addChild(prodOPT);
	    
	    prodTCR.getChildren().addAll(Arrays.asList(new ProductNode[]
	        { prod04, prod48, prod812, prod1216, prod1620, prod2024 }
	    ));
	    
	    // Add root elements (RC types)
	    final ProductTree tree = new ProductTree(99);
	    tree.addNode(prodSCR);
	    tree.addNode(prodTCR);
	    navigationData.addProductTree(tree);
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

	@Override
	public Set<NavigationSettings> getUserNavigationSettings(int userId) {
		TypedQuery<NavigationSettings> typedQuery = em
				.createQuery(
						"from "
				+ NavigationSettings.class.getName() + " where " + User.USER_ID
				+ " =:userId", NavigationSettings.class);
		typedQuery.setParameter("userId", userId);
		Set<NavigationSettings> navigationSettings = new HashSet<NavigationSettings>(
				typedQuery.getResultList());
		return navigationSettings;
	}

	@Override
	public NavigationSettings getUserNavigationSettings(Object id,
			int chartId, boolean isClient) {
		NavigationSettings settings = null;
		TypedQuery<NavigationSettings> typedQuery = null;
		if (!isClient) {
			typedQuery = em.createQuery(
					"from " + NavigationSettings.class.getName() + " where "
							+ User.USER_ID + " =:id and "
							+ NavigationSettings.CHART_ID + " =:chartId",
					NavigationSettings.class);
		} else {
			typedQuery = em.createQuery(
					"from " + NavigationSettings.class.getName() + " where "
							+ User.CLIENT_ID + " =:id and "
							+ NavigationSettings.CHART_ID + " =:chartId",
					NavigationSettings.class);
		}
		typedQuery.setParameter("id", id);
		typedQuery.setParameter("chartId", chartId);
		try {
			settings = typedQuery.getSingleResult();
		} catch (NoResultException noresult) {
			// if there is no result
			logger.error("Navigation settings not found for user id : " + id
					+ ", chartId :" + chartId);
		} catch (NonUniqueResultException notUnique) {
			// if more than one result
			logger.error("More than one navigation settings found for user id : "
					+ id + ", chartId :" + chartId);
		}
		return settings;
	}

	@Override
	public boolean saveUserNavigationSettings(
			NavigationSettings navigationSettings) {
		boolean success = false;
		if (navigationSettings.getUser() != null) {
			NavigationSettings existingNavigationSetting = getUserNavigationSettings(
					navigationSettings.getUser().getUserId(),
					navigationSettings.getChartId(), false);
			logger.debug("Updating navigation settings for user : "
					+ navigationSettings.getUser().getUserId() + ", chartId:"
					+ navigationSettings.getChartId());
			success = persistOrMerge(existingNavigationSetting,
					navigationSettings);
		} else {
			NavigationSettings existingNavigationSetting = getUserNavigationSettings(
					navigationSettings.getClientId(),
					navigationSettings.getChartId(), true);
			logger.debug("Updating navigation settings for Client : "
					+ navigationSettings.getClientId() + ", chartId:"
					+ navigationSettings.getChartId());
			success = persistOrMerge(existingNavigationSetting,
					navigationSettings);
		}
		return success;
	}

	private boolean persistOrMerge(
			NavigationSettings existingNavigationSetting,
			NavigationSettings navigationSettings) {
		boolean success = false;
		if (existingNavigationSetting != null) {
			// record exist
			navigationSettings = em.merge(navigationSettings);
			em.flush();
			success = true;
		} else {
			// create record
			em.persist(navigationSettings);
			em.flush();
			success = true;
		}
		return success;
	}
    
}
