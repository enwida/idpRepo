package de.enwida.web.dao.interfaces;

import java.util.Locale;
import java.util.Set;

import de.enwida.web.db.model.NavigationSettings;
import de.enwida.web.model.ChartNavigationData;

public interface INavigationDao {
    
    ChartNavigationData getDefaultNavigation(int chartId, Locale locale);

	Set<NavigationSettings> getUserNavigationSettings(int userId);

	boolean saveUserNavigationSettings(NavigationSettings navigationSettings);

	NavigationSettings getUserNavigationSettings(int id, int chartId,
			boolean isClient);

}
