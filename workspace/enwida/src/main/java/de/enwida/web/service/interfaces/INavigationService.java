/**
 * 
 */
package de.enwida.web.service.interfaces;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Set;

import de.enwida.web.db.model.NavigationSettings;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.User;

public interface INavigationService {
	ChartNavigationData getNavigationData(int chartId, User user, Locale locale) throws Exception;
	ChartNavigationData getDefaultNavigationData(int chartId);
	ChartNavigationData getNavigationDataFromJsonFile(int chartId) throws IOException;
	Hashtable<Integer, ChartNavigationData> getAllDefaultNavigationData();
	
	Set<NavigationSettings> getNavigationSettingsByUserId(int userId)
			throws IOException;

	boolean saveUserNavigationSettings(NavigationSettings navigationSettings);

	NavigationSettings getUserNavigationSettings(Object id, int chartId,
			boolean isClient);

	ChartNavigationData getNavigationDataUNSECURE(int chartId, User user,
			Locale locale);
	
    ChartNavigationData getNavigationDataWithoutAvailablityCheck(int chartId, User user, Locale locale) throws Exception;
}
