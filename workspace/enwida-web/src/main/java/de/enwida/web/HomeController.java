package de.enwida.web;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
	
	@RequestMapping(value = "/data.json", method = RequestMethod.GET)
	@ResponseBody
	public GoogleChartData exampleData(HttpServletRequest request) {
		// Example from enwida homepage: Balancing Power -> Abruf -> Activition of control reserve
		// https://enwida.de/data.json?type=rl_ab1&pro=210&res=15min&t1=20101230&locale=en
		
		final String completeUrl=""+request.getRequestURL().append('?').append(request.getQueryString());
		Map pMap=request.getParameterMap();
		
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
	
	@RequestMapping(value = "/init.json", method = RequestMethod.GET)
	@ResponseBody
	public ChartInitData initData(HttpServletRequest request) {
		//Chart settings object
		ChartInitData cid=new ChartInitData();
		cid.setWidth(400);
		cid.setHeight(300);
		cid.setTitle("Capacity");
		return cid;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request) {
		

		//log it via log4j
				if(logger.isDebugEnabled()){
					logger.debug("home");
				}
		return "home";
	}
	
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public String download(HttpServletRequest request) {
		//download page
		return "download";
	}
	
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	@ResponseBody		
	public ModelAndView chart_csv(HttpServletResponse response, Locale locale, HttpServletRequest request) {
	    response.setStatus(HttpServletResponse.SC_OK);	    
	    final String completeUrl=""+request.getRequestURL().append('?').append(request.getQueryString());
		Map pMap =request.getParameterMap();
		DataRequest dr = new DataRequest("csv",request.getLocale(),completeUrl,pMap);
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
		dr.setChartType("rl_ab1");
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		dr.setTime1(calStart.getTimeInMillis());
		dr.setTime2(calEnd.getTimeInMillis());
		dr.setProduct(211);
		dataRequestManager.setDatef(dateFormat);
		dataRequestManager.setDatef2(dateFormat);
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition","attachment;filename="+dr.getChartType()+dataRequestManager.getDatef2().format(dr.getTime1())+".csv");
			
		dr.setDataFormat("csv");
		Map<String, String> map=new HashMap<String, String>();

		map.put("title", "de.enwida.chart.pc2.title");
		map.put("description", "de.enwida.chart.pc1.text1");
		map.put("type", "0");
		map.put("sign", "0");
		map.put("block", "0");
		map.put("from", "0");
		map.put("to", "0");
		map.put("content", dataRequestManager.csv_pc2(dr).toString());
		ModelAndView mav=new  ModelAndView("csv", map);
		return mav;	

	}
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(HttpServletRequest request) {
		//download page
		return "register";
	}
	
	@RequestMapping(value="/welcome", method = RequestMethod.GET)
	public String printWelcome(ModelMap model, Principal principal ) {
		String name="unknown";
		if(principal!=null){
		name = principal.getName();
		}
		model.addAttribute("username", name);
		model.addAttribute("message", "Spring Security Custom Form example");
		return "home";
 
	}
 
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(ModelMap model) {
 
		return "login";
 
	}
	
	@RequestMapping(value="/404", method = RequestMethod.GET)
	public String error404(ModelMap model) {
 
		return "404";
 
	}
 
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
 
		model.addAttribute("error", "true");
		return "login";
 
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
 
		return "logout";
 
	}
}
