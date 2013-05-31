package de.enwida.web.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.enwida.chart.DataLineRequestManager;
import de.enwida.chart.DataRequest;
import de.enwida.chart.DataRequestManager;
import de.enwida.chart.GoogleChartData;
import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.User;
import de.enwida.web.service.implementation.AspectServiceImp;
import de.enwida.web.utils.JsonResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {	
	
	
	@Autowired
	private DataLineRequestManager dataLineRequestManager;
	
	@RequestMapping(value = "/data.json", method = RequestMethod.GET)
	@ResponseBody
	public GoogleChartData exampleData(HttpServletRequest request) {
		// Example from enwida homepage: Balancing Power -> Abruf -> Activition of control reserve
		// https://enwida.de/data.json?type=rl_ab1&pro=210&res=15min&t1=20101230&locale=en
		
		final String completeUrl=""+request.getRequestURL().append('?').append(request.getQueryString());
		Map pMap=request.getParameterMap();
		//TODO:Fix here
		AspectServiceImp aspectService=new AspectServiceImp();
		aspectService.dataLineRequestManager=dataLineRequestManager;
		return aspectService.getChart(pMap);
	}
	
	@RequestMapping(value = "/init.json", method = RequestMethod.GET)
	@ResponseBody
	public ChartNavigationData initData(HttpServletRequest request) {
		//Chart settings object
		ChartNavigationData cid=new ChartNavigationData();
		cid.setWidth(400);
		cid.setHeight(300);
		cid.setTitle("Capacity");
		return cid;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale) {
	
		return "index";
	}
	
	@RequestMapping(value="/getexampledata", method = RequestMethod.GET)
	public @ResponseBody JsonResponse getExampleData(ModelMap model) {

		JsonResponse ret = new JsonResponse();
	    ret.addColl("string", "Operating System");
	    ret.addColl("number", "Percentage");
	    
	    ret.addRow(new JsonResponse.Cell("Mac"), new JsonResponse.Cell(15));
	    ret.addRow(new JsonResponse.Cell("Windows"), new JsonResponse.Cell(50));
	    ret.addRow(new JsonResponse.Cell("Linux"), new JsonResponse.Cell(25));
	    ret.addRow(new JsonResponse.Cell("Others"), new JsonResponse.Cell(10));
		
		return ret;
	}	
    
//	@RequestMapping(value = "/export", method = RequestMethod.GET)
//	@ResponseBody		
//	public ModelAndView chart_csv(HttpServletResponse response, Locale locale, HttpServletRequest request) {
//	    response.setStatus(HttpServletResponse.SC_OK);	    
//	    final String completeUrl=""+request.getRequestURL().append('?').append(request.getQueryString());
//		Map pMap =request.getParameterMap();
//		DataRequest dr = new DataRequest("csv",request.getLocale(),completeUrl,pMap);
//		final Calendar calStart = Calendar.getInstance();
//		calStart.set(Calendar.YEAR, 2010);
//		calStart.set(Calendar.MONTH, Calendar.DECEMBER);
//		calStart.set(Calendar.DAY_OF_MONTH, 30);
//		calStart.set(Calendar.HOUR, 0);
//		calStart.set(Calendar.MINUTE, 0);
//		
//		final Calendar calEnd = Calendar.getInstance();
//		calEnd.set(Calendar.YEAR, 2010);
//		calEnd.set(Calendar.MONTH, Calendar.DECEMBER);
//		calEnd.set(Calendar.DAY_OF_MONTH, 31);	
//		calStart.set(Calendar.HOUR, 23);
//		calStart.set(Calendar.MINUTE, 59);
//		dr.setChartType("rl_ab1");
//		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//		dr.setTime1(calStart.getTimeInMillis());
//		dr.setTime2(calEnd.getTimeInMillis());
//		dr.setProduct(211);
//		dataLineRequestManager.setDatef(dateFormat);
//		dataLineRequestManager.setDatef2(dateFormat);
//		response.setContentType("text/plain");
//		response.setHeader("Content-Disposition","attachment;filename="+dr.getChartType()+dataLineRequestManager.getDatef2().format(dr.getTime1())+".csv");
//			
//		dr.setDataFormat("csv");
//		Map<String, String> map=new HashMap<String, String>();
//
//		map.put("title", "de.enwida.chart.pc2.title");
//		map.put("description", "de.enwida.chart.pc1.text1");
//		map.put("type", "0");
//		map.put("sign", "0");
//		map.put("block", "0");
//		map.put("from", "0");
//		map.put("to", "0");
//	//	map.put("content", dataRequestManager.csv_pc1(dr).toString());
//		ModelAndView mav=new  ModelAndView("csv", map);
//		return mav;	
//
//	}
}
