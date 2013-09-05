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
	
	Set<NavigationSettings> getNavigationSettingsByUserId(int userId) throws IOException;
	Set<NavigationSettings> getNavigationSettingsByClientId(String clientId) throws IOException;

	void saveUserNavigationSettings(NavigationSettings navigationSettings);

    ChartNavigationData getNavigationDataWithoutAvailablityCheck(int chartId, User user, Locale locale) throws Exception;
}
