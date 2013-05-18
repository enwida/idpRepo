package de.enwida.web;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.enwida.dao.User;
import de.enwida.dao.UserDAO;
/**
 * Handles requests for the application registration pages.
 */
@Controller
public class UserManagement {
	
	@RequestMapping(value="/welcome", method = RequestMethod.GET)
	public String printWelcome(ModelMap model, Principal principal ) {
		String name="unknown";
		if(principal!=null){
		name = principal.getName();
		}
		model.addAttribute("username", name);
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
            UserDAO userDao=new UserDAO();
            userDao.SaveToDB(user);
            return "hello";
        }
    }
}
