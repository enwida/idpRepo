package de.enwida.web.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.transport.IDataLine;
import de.enwida.transport.LineRequest;
import de.enwida.transport.XYDataLine;
import de.enwida.transport.XYDataPoint;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.service.interfaces.ILineService;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.service.interfaces.IUserService;

@Controller
@RequestMapping("/data")
public class DownloadController {
	
	@Autowired
	private INavigationService navigationService;
	
	@Autowired
	private ILineService lineService;
	
	@Autowired
	private IUserService userService;

    @RequestMapping(value = "/download.csv", method = RequestMethod.GET)
    @ResponseBody
    public String getNavigationData(
	    @RequestParam int chartId,
	    @RequestParam int product,
	    @RequestParam int tso,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar startTime,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar endTime,
	    @RequestParam DataResolution resolution,
	    @RequestParam String disabledLines,
	    Locale locale) throws Exception {
    	
    	// TODO: enable security
    	final ChartNavigationData navigationData = navigationService.getNavigationData(chartId, null, locale);
        final List<Aspect> originalAspects = navigationData.getAspects();
        final List<Aspect> aspects = new ArrayList<>(originalAspects);
        final String[] lineStrings = disabledLines.split(",");
        
        for (final String lineString : lineStrings) {
        	if (lineString.length() == 0) {
        		continue;
        	}
	    	final int line = Integer.parseInt(lineString);
        	aspects.remove(line);
        }
        
        final List<IDataLine> lines = new ArrayList<>();
        for (final Aspect aspect : aspects) {
        	final LineRequest lineRequest = new LineRequest(aspect, product, tso, startTime, endTime, resolution, locale);
        	try {
        		// TODO: enable security
//				lineService.getLine(lineRequest, userService.getCurrentUser());
				final IDataLine line = lineService.getLine(lineRequest, null);
				lines.add(line);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        return createCSV(lines, navigationData.getxAxisLabel());
    }
    
    private String createCSV(List<IDataLine> lines, String xTitle) {
    	final StringBuilder result = new StringBuilder();
    	final SortedMap<Double, List<Double>> csvDataLines = new TreeMap<>();
    	
    	for (final IDataLine line : lines) {
    		if (!(line instanceof XYDataLine)) {
    			throw new NotImplementedException("Only XY datalines are supported");
    		}
    		final XYDataLine xyLine = (XYDataLine) line;
    		for (final XYDataPoint dp : xyLine.getDataPoints()) {
    			List<Double> ys = csvDataLines.get(dp.x);
    			if (ys == null) {
    				ys = new ArrayList<>();
    				csvDataLines.put(dp.x, ys);
    			}
    			ys.add(dp.y);
    		}
    	}

    	// Heading
    	result.append(xTitle);
    	for (final IDataLine line : lines) {
    		result.append(",").append(line.getTitle());
    	}

    	for (final Double x : csvDataLines.keySet()) {
    		result.append("\n");
    		result.append(x);
    		for (final Double y : csvDataLines.get(x)) {
    			result.append(",");
    			result.append(y);
    		}
    	}
    	return result.toString();
    }
}
