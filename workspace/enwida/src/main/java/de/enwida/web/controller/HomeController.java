package de.enwida.web.controller;

import java.security.Principal;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.web.model.User;
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
	
	@RequestMapping(value="/dashboard", method = RequestMethod.GET)
	public String printWelcome(ModelMap model, Principal principal ) {
		String name="unknown";
		if(principal!=null){
		name = principal.getName();
		}
		model.addAttribute("username", name);
		return "dashboard";
 
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
	
	@RequestMapping(value="/register",method=RequestMethod.GET)
    public String showForm(ModelMap model){
        User user = new User();
        model.addAttribute("USER", user);
        return "register";
    }
	

    @RequestMapping(value="/register",method=RequestMethod.POST)
    public String processForm(@ModelAttribute(value="USER") User user,BindingResult result){
        if(result.hasErrors()){
            return "registration";
        }else{
            System.out.println("User values is : " + user);
            return "hello";
        }
    }
}
