package de.enwida.web.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.chart.DataManager;
import de.enwida.transport.ChartType;
import de.enwida.transport.DataRequest;
import de.enwida.transport.DataResolution;
import de.enwida.transport.DataResponse;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.service.interfaces.NavigationService;

/**
 * Handles chart data requests
 */
@Controller
@RequestMapping("/data")
public class ChartDataController {

	@Autowired
	private DataManager dataManager;
	@Autowired
	private NavigationService navigationService;

	@RequestMapping(value="/lines", method = RequestMethod.GET)
	@ResponseBody
	public DataResponse displayDashboard(
			@RequestParam ChartType type,
			@RequestParam int product,
			@RequestParam @DateTimeFormat(pattern="YYYY-MM-DD") Calendar startTime,
			@RequestParam @DateTimeFormat(pattern="YYYY-MM-DD") Calendar endTime,
			@RequestParam DataResolution resolution,
			Locale locale
			)
	{
		final DataRequest request = new DataRequest(type, product, startTime, endTime, resolution, locale);
		final DataResponse response = this.dataManager.getData(request);
		return response;
	}

	@RequestMapping(value="/chart", method=RequestMethod.GET)
	public String exampleChart() {
		return "charts/index";
	}

	@RequestMapping(value="/navigationData", method = RequestMethod.GET)
	@ResponseBody
	public ChartNavigationData getNavigation(
			@RequestParam ChartType type,
			@RequestParam int product,
			@RequestParam @DateTimeFormat(pattern="YYYY-MM-DD") Calendar startTime,
			@RequestParam @DateTimeFormat(pattern="YYYY-MM-DD") Calendar endTime,
			@RequestParam DataResolution resolution,
			Locale locale,Principal principal
			)
	{
		final DataRequest request = new DataRequest(type, product, startTime, endTime, resolution, locale);
		final Map<String,Object> navigationMap = this.prepareNavigationMap(request,principal);
		final ChartNavigationData defaultNavigationData =  this.navigationService.getNavigataionData(navigationMap);
		// final DataResponse response = this.dataManager.getData(request);
		return defaultNavigationData;
	}

	private Map<String, Object> prepareNavigationMap(DataRequest request,
			Principal principal) {
		final Map<String, Object> paramMap = new HashMap<String, Object>();

		/**
		 * bind all params thats needs to be sent to navigation layer.
		 */
		paramMap.put("loggedInUser", principal.getName());
		return paramMap;
	}

}
