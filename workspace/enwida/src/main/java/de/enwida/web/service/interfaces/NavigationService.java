/**
 * 
 */
package de.enwida.web.service.interfaces;

import java.util.Locale;

import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.User;

public interface NavigationService {
	ChartNavigationData getNavigationData(int chartId, User user, Locale locale);
}
