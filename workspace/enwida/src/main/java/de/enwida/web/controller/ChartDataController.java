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

import de.enwida.chart.LineManager;
import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.transport.LineRequest;
import de.enwida.transport.XYDataLine;
import de.enwida.web.model.ChartNavigationData;

/**
 * Handles chart data requests
 */
@Controller
@RequestMapping("/data")
public class ChartDataController {
	
	@Autowired
	private LineManager lineManager;
	
	@RequestMapping(value="/lines", method = RequestMethod.GET)
	@ResponseBody
	public XYDataLine getLines (
								@RequestParam int chartId,
								@RequestParam int product,
								@RequestParam @DateTimeFormat(pattern="YYYY-MM-DD") Calendar startTime,
								@RequestParam @DateTimeFormat(pattern="YYYY-MM-DD") Calendar endTime,
								@RequestParam DataResolution resolution,
								Locale locale
							   )
	{
	    try {
    	    final LineRequest request = new LineRequest(Aspect.VOL_ACTIVATION, product, startTime, endTime, resolution, locale);
    	    final XYDataLine line = lineManager.getLine(request);
    		return line;
    	} catch (Exception e) {
    	    return null;
    	}
	}
	
	@RequestMapping(value="/chart", method=RequestMethod.GET)
	public String exampleChart() {
		return "charts/index";
	}
	
	@RequestMapping(value = "/navigation", method = RequestMethod.GET)
	@ResponseBody
	public ChartNavigationData initData() {
		// FIXME: Get navigation data from a dedicated service
		ChartNavigationData dummy = new ChartNavigationData();
		dummy.setWidth(600);
		dummy.setHeight(480);
		dummy.setTitle("Capacity");
		return dummy;
	}
	
}
