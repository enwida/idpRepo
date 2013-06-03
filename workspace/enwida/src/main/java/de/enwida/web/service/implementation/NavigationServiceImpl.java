/**
 * 
 */
package de.enwida.web.service.implementation;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.chart.DataRequestManager;
import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.NavigationService;

@Service("NavigationService")
@Transactional
public class NavigationServiceImpl implements NavigationService {

	@Autowired
	private DataRequestManager dataRequestManager;
	
	@Autowired
	private INavigationDao navigationDao;

    public ChartNavigationData getNavigationData(int chartId, User user, Locale locale) {
        ChartNavigationData navigationData = navigationDao.getDefaultNavigation(chartId, locale);
        
        return navigationData;
    }

}
