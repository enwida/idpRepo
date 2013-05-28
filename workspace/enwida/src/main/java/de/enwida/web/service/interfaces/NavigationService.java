/**
 * 
 */
package de.enwida.web.service.interfaces;

import java.util.Map;

import de.enwida.web.model.ChartNavigationData;

/**
 * @author Jitin
 *
 */
public interface NavigationService {
	ChartNavigationData getNavigataionData(Map<String, Object> navigationParam);
}
