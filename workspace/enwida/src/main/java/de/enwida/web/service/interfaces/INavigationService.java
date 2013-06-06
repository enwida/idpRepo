/**
 * 
 */
package de.enwida.web.service.interfaces;

import java.util.Locale;

import de.enwida.web.model.ChartNavigationData;

public interface INavigationService {
	ChartNavigationData getNavigationData(int chartId, int role, Locale locale);
}
