package de.enwida.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.enwida.web.model.Group;
import de.enwida.web.model.User;
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
		
		List<Group> availableGroups= userService.getAvailableGroupsForUser(userID);
		model.addAttribute("availableGroups", availableGroups);
	
		List<Group> assignedGroups= userService.getUserGroups(userID);
		model.addAttribute("assignedGroups", assignedGroups);
		
		model.addAttribute("content", "editGroup");
		return "user/admin/master";
	}
	
	@RequestMapping(value="/editAspect", method = RequestMethod.GET)
	public String aspect(Model model) {
		model.addAttribute("content", "editAspect");
		return "user/admin/master";
	}
	
	@RequestMapping(value="/editRole", method = RequestMethod.GET)
	public String editRole(Model model,long groupID) {
		
//		List<Role> availableRoles= userService.getAvailableRolesForGroup(groupID);
//		model.addAttribute("availableGroups", availableRoles);
//	
//		List<Role> assignedGroups= userService.getGroupRoles(groupID);
//		model.addAttribute("assignedGroups", assignedGroups);
		
		model.addAttribute("content", "editRole");
		return "user/admin/master";
	}
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String admin(Model model) {
		
		List<User> users= userService.findAllUsersWithPermissions();
		model.addAttribute("users", users);
		
		List<Group> groups= userService.getAllGroups();
		model.addAttribute("groups", groups);
		model.addAttribute("content", "index");
		return "user/admin/master";
	}
	

}
