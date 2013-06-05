package de.enwida.web.controller;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
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
import de.enwida.web.dto.UserDTO;
import de.enwida.web.model.User;
import de.enwida.web.service.implementation.Mail;
import de.enwida.web.service.interfaces.UserService;
import de.enwida.web.validator.UserValidator;

/**
 * Handles requests for the user service.
 */
@Controller
@RequestMapping("/user/admin")
public class AdminController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DriverManagerDataSource datasource;
	
	@Autowired
	private UserValidator userValidator;
	
	@RequestMapping(value="/editGroup", method = RequestMethod.GET)
	public String editGroup(Model model,long userID) {
		User user = userService.getUser(userID);
		model.addAttribute("username", user.getUserName());
		
		model.addAttribute("content", "editGroup");
		return "user/admin/master";
	}
	
	@RequestMapping(value="/aspect", method = RequestMethod.GET)
	public String aspect(Model model) {
		model.addAttribute("content", "aspect");
		return "user/admin/master";
	}
}
