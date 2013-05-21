package de.enwida.web.controller;

import java.security.Principal;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.UserService;

/**
 * Handles requests for the user service.
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
	public String displayDashboard(Model model, Locale locale) {
		
		User u = userService.getUser(new Long(0));
		model.addAttribute("user", u);
		
		return "user/user";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(ModelMap model, Principal principal) {
		String name,userStatus;
		
		if(principal!=null){
			name = principal.getName();
			userStatus="logout";
		}else{
			name="anonymous";
			userStatus="login";
		}
		model.addAttribute("username", name);
		model.addAttribute("userStatus", userStatus);
		return "user/index";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(ModelMap model) {
 
		return "user/login";
 
	}
 
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
 
		model.addAttribute("error", "true");
		return "user/login";
 
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
 
		return "user/logout";
 
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
