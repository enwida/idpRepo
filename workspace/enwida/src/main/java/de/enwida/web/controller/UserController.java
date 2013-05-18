package de.enwida.web.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	@RequestMapping(value="/dashboard", method = RequestMethod.GET)
	public String displayDashboard(Model model, Locale locale) {
		
		User u = userService.getUser(new Long(0));
		model.addAttribute("user", u);
		
		return "dashboard";
	}

}
