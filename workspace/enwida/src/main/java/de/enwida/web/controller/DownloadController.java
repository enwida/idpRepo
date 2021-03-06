package de.enwida.web.controller;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.enwida.transport.Aspect;
import de.enwida.transport.DataResolution;
import de.enwida.transport.IDataLine;
import de.enwida.transport.LineRequest;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.ICSVService;
import de.enwida.web.service.interfaces.ILineService;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.numbers.DefaultNumberFormatter;
import de.enwida.web.utils.numbers.GermanNumberFormatter;
import de.enwida.web.utils.numbers.INumberFormatter;
import de.enwida.web.utils.timestamp.ITimestampFormatter;
import de.enwida.web.utils.timestamp.LocalTimestampFormatter;
import de.enwida.web.utils.timestamp.UTCTimestampFormatter;

@Controller
@RequestMapping("/data")
public class DownloadController {
	
	@Autowired
	private INavigationService navigationService;
	
	@Autowired
	private ILineService lineService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICSVService csvService;

    @RequestMapping(value = "/csv", method = RequestMethod.GET)
    public void getNavigationData(
	    @RequestParam int chartId,
	    @RequestParam int product,
	    @RequestParam int tso,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar startTime,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Calendar endTime,
	    @RequestParam DataResolution resolution,
	    @RequestParam String timezone,
	    @RequestParam String timezoneInformation,
	    @RequestParam String numberFormat,
	    @RequestParam String disabledLines,
	    HttpServletResponse response,
	    Locale locale) throws Exception {
    	
    	final User user = userService.getCurrentUser();
    	final ChartNavigationData navigationData = navigationService.getNavigationData(chartId, user, locale);
        final List<Aspect> originalAspects = navigationData.getAspects();
        final List<Aspect> aspects = new ArrayList<Aspect>(originalAspects);
        final String[] lineStrings = disabledLines.split(",");
        
        for (final String lineString : lineStrings) {
        	if (lineString.length() == 0) {
        		continue;
        	}
	    	final int line = Integer.parseInt(lineString);
        	aspects.remove(line);
        }
        
        final List<IDataLine> lines = new ArrayList<IDataLine>();
        for (final Aspect aspect : aspects) {
        	final LineRequest lineRequest = new LineRequest(aspect, product, tso, startTime, endTime, resolution, locale);
        	try {
				final IDataLine line = lineService.getLine(lineRequest, user, locale);
				lines.add(line);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        
        ITimestampFormatter timestampFormatter;
        if (timezone.equalsIgnoreCase("UTC")) {
        	timestampFormatter = new UTCTimestampFormatter();
        } else {
        	timestampFormatter = new LocalTimestampFormatter();
        }
        if (timezoneInformation.equalsIgnoreCase("with")) {
        	timestampFormatter.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z"));
        } else {
        	timestampFormatter.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
        
        INumberFormatter numberFormatter;
        if (numberFormat.equalsIgnoreCase("de")) {
        	numberFormatter = new GermanNumberFormatter();
        } else {
        	numberFormatter = new DefaultNumberFormatter();
        }
        
    	response.setHeader("Content-Disposition", "attachment;filename=data.csv");
    	response.setContentType("text/csv");
    	
    	final String result = csvService.createCSV(navigationData, lines, locale, timestampFormatter, numberFormatter);

    	final OutputStream out = response.getOutputStream();
    	out.write(result.getBytes("UTF-8"));
    	out.close();
    }
    
}
