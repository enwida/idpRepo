package de.enwida.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.chart.DataRequest;
import de.enwida.chart.DataRequestManager;
import de.enwida.chart.GoogleChartData;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private DataRequestManager dataRequestManager;
	
//    ValidatorDataRequest dataValidator;
    
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public GoogleChartData home(HttpServletRequest request) {
		
		// Example from enwida homepage: Balancing Power -> Abruf -> Activition of control reserve
		// https://enwida.de/data.json?type=rl_ab1&pro=210&res=15min&t1=20101230&locale=en
		
		final String completeUrl= "https://enwida.de/data.json?type=rl_ab1&pro=210&res=15min&t1=20101230&locale=en";
		final Map<String, String[]> pMap = new HashMap<String, String[]>();
		pMap.put("type", new String[] { "rl_ab1" });
		pMap.put("pro", new String[] { "210" });
		pMap.put("res", new String[] { "15min" });
		pMap.put("t1", new String[] { "20101230" });
		pMap.put("locale", new String[] { "de" });
		
		// Define the start and end dates manually
		final Calendar calStart = Calendar.getInstance();
		calStart.set(Calendar.YEAR, 2010);
		calStart.set(Calendar.MONTH, Calendar.DECEMBER);
		calStart.set(Calendar.DAY_OF_MONTH, 30);
		calStart.set(Calendar.HOUR, 0);
		calStart.set(Calendar.MINUTE, 0);
		
		final Calendar calEnd = Calendar.getInstance();
		calEnd.set(Calendar.YEAR, 2010);
		calEnd.set(Calendar.MONTH, Calendar.DECEMBER);
		calEnd.set(Calendar.DAY_OF_MONTH, 31);	
		calStart.set(Calendar.HOUR, 23);
		calStart.set(Calendar.MINUTE, 59);
		
		// Create the data request
		final DataRequest dr = new DataRequest("json",request.getLocale(),completeUrl, pMap);
		
		// Set these values manually because they are not converted automatically from the string
		// representation
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		dr.setTime1(calStart.getTimeInMillis());
		dr.setTime2(calEnd.getTimeInMillis());
		dr.setProduct(211);
		dataRequestManager.setDatef(dateFormat);
		dataRequestManager.setDatef2(dateFormat);
		
		// Fetch and display chart data in JSON representation
		final GoogleChartData chartData = dataRequestManager.getData(dr);
		return chartData;
	}
	
}
