package de.enwida.web.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.transport.DataResolution;
import de.enwida.transport.IDataLine;
import de.enwida.web.model.ChartLinesRequest;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.service.implementation.LineService;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.utils.CalendarRange;

/**
 * Handles chart data requests
 */
@Controller
@RequestMapping("/data")
public class ChartDataController {
	
	@Autowired
	private LineService lineService;
	
	@Autowired
	private INavigationService navigationService;
	
	@RequestMapping(value="/lines", method = RequestMethod.GET)
	@ResponseBody
	public List<IDataLine> getLines (
								@RequestParam int chartId,
								@RequestParam int product,
								@RequestParam int tso,
								@RequestParam @DateTimeFormat(pattern="YYYY-MM-DD") Calendar startTime,
								@RequestParam @DateTimeFormat(pattern="YYYY-MM-DD") Calendar endTime,
								@RequestParam DataResolution resolution,
								Locale locale
							   )
	{
	    final ChartLinesRequest request = new ChartLinesRequest(
	            chartId,
	            product,
	            tso,
	            new CalendarRange(startTime, startTime),
	            resolution,
	            locale
	            );
	    
	    return lineService.getLines(request);
	}
	
	@RequestMapping(value="/chart", method=RequestMethod.GET)
	public String exampleChart(Principal principal) {
		if(principal!=null){
			System.out.println(principal.getName());
		}
		return "charts/index";
	}
	
	@RequestMapping(value = "/navigation", method = RequestMethod.GET)
	@ResponseBody
	public ChartNavigationData getNavigationData(@RequestParam int chartId, Principal principal, Locale locale) {
	    // FIXME: get user / submit correct role
	    return navigationService.getNavigationData(chartId, 0, locale);
	}
	
}
