/**
 * 
 */
package de.enwida.web.service.interfaces;

import java.io.IOException;
import java.util.Locale;

import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.User;

public interface INavigationService {
	ChartNavigationData getNavigationData(int chartId, User user, Locale locale);
	ChartNavigationData getDefaultNavigationData(int chartId);
	
	ChartNavigationData getNavigationDataFromJsonFile(int chartId) throws IOException;
}
