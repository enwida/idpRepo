package de.enwida.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
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
	
	@RequestMapping(value="/userList", method = RequestMethod.GET)
	public String userList(Model model) {
		
		List<User> users= userService.findAllUsersWithPermissions();
		model.addAttribute("users", users);
		
		List<Group> groups= userService.getAllGroups();
		model.addAttribute("groups", groups);
		model.addAttribute("content", "userList");
		return "user/admin/master";
	}
	
    @RequestMapping(value="/user", method = RequestMethod.GET)
    public String user(Model model,long userID) {
        
        User users= userService.getUser(userID);
        model.addAttribute("user", users);
        model.addAttribute("content", "user");
        return "user/admin/master";
    }
    
    @RequestMapping(value="/editGroup", method = RequestMethod.GET)
    public String editGroup(Model model) {
//      User user = userService.getUser(userID);
//      model.addAttribute("username", user.getUserName());
//      
        List<Group> groups= userService.getAllGroups();
        model.addAttribute("groups", groups);
//  
//      List<Group> assignedGroups= userService.getUserGroups(userID);
//      model.addAttribute("assignedGroups", assignedGroups);
        
        model.addAttribute("content", "editGroup");
        return "user/admin/master";
    }
    
    @RequestMapping(value="/editGroup",method=RequestMethod.POST)
    public String addGroup(Model model,String newGroup){
        System.out.println(newGroup+" is added");
        if(newGroup.isEmpty()){
            model.addAttribute("error", "Group name is not valid");
        }
        else{     
        	Group group = new Group();
        	group.setGroupName(newGroup);
            userService.addGroup(group);
        }
        return editGroup(model);
    }
    
    
    @RequestMapping(value="/roleList", method = RequestMethod.GET)
    public String editRole(Model model) {
        
        List<Role> roles= userService.getAllRoles();
        model.addAttribute("roles", roles);
        
        model.addAttribute("content", "roleList");
        return "user/admin/master";
    }
    
    @RequestMapping(value="/roleList",method=RequestMethod.POST)
    public String addRole(Model model,String newRole,String roleDescription){
        if(newRole.isEmpty()){
            model.addAttribute("error", "Role name is not valid");
        }
        else{
            Role role= new Role();
            role.setName(newRole);
            role.setDescription(roleDescription);
            userService.saveRole(role);
        }
        return editRole(model);
    }
}
