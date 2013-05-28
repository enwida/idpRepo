/**
 * 
 */
package de.enwida.web.service.implementation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.chart.DataRequestManager;
import de.enwida.web.dao.interfaces.INavigationDao;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.service.interfaces.NavigationService;

/**
 * @author Jitin
 *
 */
@Service("NavigationService")
@Transactional
public class NavigationServiceImpl implements NavigationService {

	@Autowired
	private DataRequestManager dataRequestManager;

	@Autowired
	private INavigationDao navigationDao;

	public ChartNavigationData getNavigataionData(
			Map<String, Object> navigationParam) {
		// TODO Auto-generated method stub
		return null;
	}

}
