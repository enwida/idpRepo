package de.enwida.web.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.chart.LineManager;
import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.transport.LineRequest;
import de.enwida.transport.XYDataLine;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.service.interfaces.NavigationService;

/**
 * Handles chart data requests
 */
@Controller
@RequestMapping("/data")
public class ChartDataController {
	
	@Autowired
	private LineManager lineManager;
	
	@Autowired
	private NavigationService navigationService;
	
	@RequestMapping(value="/lines", method = RequestMethod.GET)
	@ResponseBody
	public XYDataLine getLines (
								@RequestParam int chartId,
								@RequestParam int product,
								@RequestParam int tso,
								@RequestParam @DateTimeFormat(pattern="YYYY-MM-DD") Calendar startTime,
								@RequestParam @DateTimeFormat(pattern="YYYY-MM-DD") Calendar endTime,
								@RequestParam DataResolution resolution,
								Locale locale
							   )
	{
	    try {
    	    final LineRequest request = new LineRequest(Aspect.DEGREE_OF_ACTIVATION, product, tso, startTime, endTime, resolution, locale);
    	    final XYDataLine line = lineManager.getLine(request);
    		return line;
    	} catch (Exception e) {
    	    return null;
    	}
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
	    // FIXME: get user
	    return navigationService.getNavigationData(chartId, null, locale);
	}
	
}
