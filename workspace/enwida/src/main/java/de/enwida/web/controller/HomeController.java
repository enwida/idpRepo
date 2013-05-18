package de.enwida.web.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.web.utils.JsonResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {	
	
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
}
