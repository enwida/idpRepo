package de.enwida.web.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.web.model.ChartNavigationData;
import de.enwida.web.model.DataAuthorization;
import de.enwida.web.model.DataAvailibility;
import de.enwida.web.service.interfaces.IAvailibilityService;
import de.enwida.web.service.interfaces.INavigationService;
import de.enwida.web.service.interfaces.ISecurityService;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	public ISecurityService securityService;
	
	@Autowired
	public IAvailibilityService availibilitService;
	
	@Autowired
	private INavigationService navigationService;

	@RequestMapping(value="/availibilty", method = RequestMethod.GET)
	public String testAvailibilty(Model model, Locale locale) {
		
		Calendar cal = Calendar.getInstance();
				
		DataAvailibility da = new DataAvailibility();
		da.setProduct(211);		
		cal.set(2008, 00, 2, 00, 00, 00);
		da.setTimeFrom(new Date(cal.getTimeInMillis()));
		cal.set(2013, 2, 13, 19, 30, 00);
		da.setTimeTo(new Date(cal.getTimeInMillis()));
		da.setTableName("15min");
		
		boolean isAvailable = availibilitService.isAvailable(da);
		
		return "user";
	}
	
	@RequestMapping(value="/authorization", method = RequestMethod.GET)
	public String testAuthorization(Model model, Locale locale) {
		
		Calendar cal = Calendar.getInstance();
		
		DataAuthorization da = new DataAuthorization();
		da.setRole(3);
		da.setTso(99);
		da.setProductId(321);
		da.setAspect("rl_ab1");
		da.setResolution("15min");
		cal.set(2010, 00, 1, 00, 00, 00);
		da.setTimeFrom(new Date(cal.getTimeInMillis()));
		cal.set(2011, 00, 1, 00, 00, 00);
		da.setTimeTo(new Date(cal.getTimeInMillis()));	
		da.setEnabled(true);
		
		boolean isAuthorized = securityService.isAllowed(da);
		
		return "user";
	}
	
	@RequestMapping(value="/jsonTest", method = RequestMethod.GET)
	@ResponseBody
	public ChartNavigationData testJSON(Model model, Locale locale) throws IOException {
		
		ChartNavigationData chartNavigationData = navigationService.getNavigationDataFromJsonFile(1);		
		return chartNavigationData;
	}

}
