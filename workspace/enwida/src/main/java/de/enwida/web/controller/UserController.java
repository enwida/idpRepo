package de.enwida.web.controller;

import java.security.Principal;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.web.model.User;
import de.enwida.web.service.implementation.Mail;
import de.enwida.web.service.interfaces.UserService;
import de.enwida.web.validator.UserValidator;

/**
 * Handles requests for the user service.
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DriverManagerDataSource datasource;
	
	@Autowired
	private UserValidator userValidator;
 
	@Autowired	
	private Mail mail;
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
	public String displayDashboard(Model model, Locale locale) {
		
		User u = userService.getUser(new Long(0));
		model.addAttribute("user", u);
		
		return "user";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(ModelMap model, Principal principal) {
		String name,userStatus,userStatusURL;
		
		if(principal!=null){
			name = principal.getName();
			userStatus="logout";
			userStatusURL="../j_spring_security_logout";
		}else{
			name="anonymous";
			userStatusURL=userStatus="login";
		}
		model.addAttribute("username", name);
		model.addAttribute("userStatus", userStatus);
		model.addAttribute("userStatusURL", userStatusURL);
		model.addAttribute("content", "user/index");
		return "master";
	}
	
	   @RequestMapping(value = "/index", method = RequestMethod.GET)
	    public String index(ModelMap model, Principal principal) {
	        String name,userStatus,userStatusURL;
	        
	        if(principal!=null){
	            name = principal.getName();
	            userStatus="logout";
	            userStatusURL="../j_spring_security_logout";
	        }else{
	            name="anonymous";
	            userStatusURL=userStatus="login";
	        }
	        model.addAttribute("username", name);
	        model.addAttribute("userStatus", userStatus);
	        model.addAttribute("userStatusURL", userStatusURL);
	        model.addAttribute("content", "user/index");
	        return "master";
	    }
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(ModelMap model, Principal principal) {
		String name,userStatus,userStatusURL;
		
		if(principal!=null){
			name = principal.getName();
			userStatus="logout";
			userStatusURL="../j_spring_security_logout";
		}else{
			name="anonymous";
			userStatusURL=userStatus="login";
		}
		model.addAttribute("username", name);
		model.addAttribute("userStatus", userStatus);
		model.addAttribute("userStatusURL", userStatusURL);
		return "user/test";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(ModelMap model) {
		return "user/login";
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		return "logout";
	}
	
	@RequestMapping(value="/loginFailed", method = RequestMethod.GET)
	public String loginFailed(ModelMap model) {
	    model.addAttribute("error", "true");
		return "user/login";
	}
	
	@RequestMapping(value="/download", method = RequestMethod.GET)
	public String download(ModelMap model) {
		return "user/download";
	}
	
	@RequestMapping(value="/register",method=RequestMethod.GET)
    public String showForm(ModelMap model){
		User user = new User();
        model.addAttribute("USER", user);
        model.addAttribute("content", "register");
        return "user/master";
    }
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String processForm(@ModelAttribute(value="USER") User user, BindingResult result, ModelMap model)
	{
		userValidator.validate(user, result);	    

		if (!result.hasErrors())
		{
	        if(userService.saveUser(user))
	        {	        		        
        		String name = user.getFirstName() + " " + user.getLastName();
        		String userStatus="logout";
        		String userStatusURL="../j_spring_security_logout";

        		model.addAttribute("username", name);
        		model.addAttribute("userStatus", userStatus);
        		model.addAttribute("userStatusURL", userStatusURL);
        		return "user/index";        		
	        }
	    }
	    model.addAttribute("content", "register");
        return "user/master";
	}
	
	@RequestMapping(value="/checkEmail",method=RequestMethod.GET)
	public @ResponseBody String checkEmail(ModelMap model,String email){
		
		boolean availabilityCheck = userService.usernameAvailablility(email);
		
		if(availabilityCheck)
		{
			model.addAttribute("emailAvailabilityError", "This email is already in use by some other user.");
		}
		
		return availabilityCheck + "";
	}
	
	@RequestMapping(value="/forgotPassword",method=RequestMethod.GET)
    public String showForgotPassForm(ModelMap model){
		return "user/forgotPassword";
    }
	
   @RequestMapping(value="/checkImages",method=RequestMethod.GET)
    public @ResponseBody String checkImages(String company){
       LogoFinder logoFinder=new LogoFinder();
       return logoFinder.getImages(company);
    }
	
	@RequestMapping(value="/forgotPassword",method=RequestMethod.POST)
	public String forgotPassword(ModelMap model,String email){
		
		String password=userService.getPassword(email);
		if(password==null){
			model.addAttribute("error", "User is not found");
		}else{
			try {
				mail.SendEmail(email,"Your Password:",password);
			} catch (Exception e) {
				model.addAttribute("error", "Mailling Error");
			}
		}
		return "user/forgotPassword";
	}
}
