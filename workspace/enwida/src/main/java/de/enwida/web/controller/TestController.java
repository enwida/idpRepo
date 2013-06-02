package de.enwida.web.controller;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.enwida.web.model.DataAuthorization;
import de.enwida.web.model.DataAvailibility;
import de.enwida.web.service.interfaces.IAvailibilityService;
import de.enwida.web.service.interfaces.ISecurityService;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Autowired
	public ISecurityService securityService;
	
	@Autowired
	public IAvailibilityService availibilitService;

	@RequestMapping(value="/availibilty", method = RequestMethod.GET)
	public String testAvailibilty(Model model, Locale locale) {
		
		DataAvailibility da = new DataAvailibility();
		da.setProduct(321);
		da.setTimeFrom(new Date());
		da.setTimeTo(new Date());
		da.setTableName("analysis_15min");
		boolean isAvailable = availibilitService.isAvailable(da);
		
		return "user";
	}
	
	@RequestMapping(value="/authorization", method = RequestMethod.GET)
	public String testAuthorization(Model model, Locale locale) {
		
		DataAuthorization da = new DataAuthorization();
		da.setRole(1);
		da.setTso(99);
		da.setProductId(321);
		da.setAspect("rl_ab1");
		da.setResolution("15min");
		da.setTimeFrom(new Date());
		da.setTimeTo(new Date());		
		boolean isAuthorized = securityService.isAllowed(da);
		
		return "user";
	}

}
