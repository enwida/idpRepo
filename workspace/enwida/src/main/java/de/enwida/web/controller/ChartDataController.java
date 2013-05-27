package de.enwida.web.controller;

import java.util.Calendar;
import java.util.Locale;

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

/**
 * Handles chart data requests
 */
@Controller
@RequestMapping("/data")
public class ChartDataController {
	
	@Autowired
	private DataManager dataManager;
	
	@RequestMapping(value="/json", method = RequestMethod.GET)
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
		final DataResponse response = dataManager.getData(request);
		return response;
	}
	
	@RequestMapping(value="/chart", method=RequestMethod.GET)
	public String exampleChart() {
		return "charts/index";
	}
	
}
