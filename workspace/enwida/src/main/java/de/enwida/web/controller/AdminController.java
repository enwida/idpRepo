package de.enwida.web.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import de.enwida.web.model.AspectRight;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IAspectService;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.validator.UserValidator;

/**
 * Handles requests for the user service.
 */
@Controller
@RequestMapping("/user/admin")
public class AdminController {
	
	@Autowired
	private IUserService userService;
	
    @Autowired
    private IAspectService aspectService;
	
	@Autowired
	private UserValidator userValidator;
	

    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	
	@RequestMapping(value="/editAspect", method = RequestMethod.GET)
	public String editAspect(Model model,long roleID) {
	    
	    List<AspectRight> aspectRights= aspectService.getAllAspects(roleID);
        model.addAttribute("aspectRights", aspectRights);
        
        List<Role> roles= userService.getAllRoles();
        model.addAttribute("roles", roles);
	    
		model.addAttribute("content", "editAspect");
		return "user/admin/master";
	}
	@RequestMapping(value="/userList", method = RequestMethod.GET)
	public String userList(Model model) {
        
		return admin(model);
	}
    
    @RequestMapping(value="/editGroup", method = RequestMethod.GET)
    public String editGroup(Model model,String action,Integer groupID,String newGroup) {    
        try {
            if( action!=null){
                switch(action){
                    case "delete":
                        userService.removeGroup(groupID);
                        break;
                    case "add":
                        Group group=new Group();
                        group.setGroupName(newGroup);            
                        userService.addGroup(group);
                }
                model.addAttribute("info", "OK");
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        
        List<Group> groupsWithUsers= userService.getAllGroupsWithUsers();
        model.addAttribute("groupsWithUsers", groupsWithUsers);
        
        List<User> users= userService.getUsers();
        model.addAttribute("users", users);
        
        model.addAttribute("content", "editGroup");
        return "user/admin/master";
    }
       
    
    @RequestMapping(value="/roleList", method = RequestMethod.GET)
    public String roleList(Model model) {
        
        List<Role> roles= userService.getAllRoles();
        model.addAttribute("roles", roles);
        
        List<Role> rolesWithGroups= userService.getAllRolesWithGroups();
        model.addAttribute("rolesWithGroups", rolesWithGroups);
        
        List<Group> groups= userService.getAllGroups();
        model.addAttribute("groups", groups);
        
        model.addAttribute("content", "roleList");
        return "user/admin/master";
    }
    
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String admin(Model model) {
        List<User> users= userService.findAllUsers();
        model.addAttribute("users", users);
        List<Group> groups= userService.getAllGroups();
        model.addAttribute("groups", groups);
        model.addAttribute("content", "userList");
        return "user/admin/master";
    }
    
    @RequestMapping(value="/userLog", method = RequestMethod.GET)
    public String  userLog(Model model,String user) {
        File file;
        try {
            file=new File(System.getenv("ENWIDA_HOME")+"/log/"+user+".log");
            model.addAttribute("userLog",FileUtils.readFileToString(file));
        } catch (Exception e) {
            model.addAttribute("error", "File can not be read");
            logger.error(e.getMessage());
        } 
        model.addAttribute("content", "userLog");
        return "user/admin/master";
    }
    
    
    @RequestMapping(value = "/enableDisableUser", method = RequestMethod.GET)
    @ResponseBody
    public boolean enableDisableUser(int userID,boolean enabled) {
        return userService.enableDisableUser(userID,enabled);
    }   
    
    @RequestMapping(value = "/enableDisableAspect", method = RequestMethod.GET)
    @ResponseBody
    public boolean enableDisableAspect(int rightID,boolean enabled) {
        return userService.enableDisableAspect(rightID,enabled);
    }   
    
    @RequestMapping(value="/admin_user", method = RequestMethod.GET)
    public String user(Model model,long userID) {
        
        User user= userService.getUser(userID);
        model.addAttribute("user", user);
        model.addAttribute("content", "admin_user");
        //save userID into session
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        
        session.setAttribute("userID", userID);
        return "user/admin/master";
    }
    
    @RequestMapping(value="/admin_user",method=RequestMethod.POST, params = "save")
    public String processForm(@ModelAttribute(value="USER")User user,long userID,HttpSession session, ModelMap model)
    {
        User newUser=userService.getUser(userID);
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setTelephone(user.getTelephone());
        userService.updateUser(newUser);
        model.addAttribute("user", newUser);
        model.addAttribute("content", "admin_user");
        return "user/admin/master";
    }

    @RequestMapping(value="/user",method=RequestMethod.POST, params = "resetPassword")
    public String reset(Model model,long userID)
    {
        System.out.println("ResetPassword");
        try {
            userService.resetPassword(userID);
            model.addAttribute("info", "OK");
        } catch (Exception e) {
            model.addAttribute("error", "Error:"+e.getLocalizedMessage());
            logger.error(e.getMessage());
        }
        return user(model,userID);
    }
    
    
    @RequestMapping(value="/user",method=RequestMethod.POST, params = "delete")
    public String deleteUser(Model model,long userID)
    {
        System.out.println("DeleteUser");
        User user=userService.getUser(userID);
        try {
            userService.deleteUser(user);
            
        } catch (Exception e) {
            model.addAttribute("error", "Deassign all groups to delete user");
            logger.info(e.getMessage());
            return user(model,userID);
        }
        return userList(model);
    }
    
    @RequestMapping(value="/editGroup",method=RequestMethod.POST, params = "assign")
    public String assignUserToGroup(Model model,int selectedUser,int selectedGroup)
    {
        String result= userService.assignUserToGroup(selectedUser,selectedGroup);
        model.addAttribute("info", result);
        return editGroup(model,null,0,null);
    }
    
    @RequestMapping(value="/editGroup",method=RequestMethod.POST, params = "deassign")
    public String deassignUserToGroup(Model model,int selectedUser,int selectedGroup)
    {
        String result= userService.deassignUserToGroup(selectedUser,selectedGroup);
        model.addAttribute("info", result);
        return editGroup(model,null,0,null);
    }
    
    @RequestMapping(value="/editGroup",method=RequestMethod.POST, params = "addGroup")
    public String addGroup(Model model,String newGroup,boolean autoPass)
    {
        Group group=new Group();
        group.setGroupName(newGroup);
        group.setAutoPass(autoPass);
        try {
            userService.addGroup(group);
            
        } catch (Exception e) {
            model.addAttribute("error", "Group can not be added");
            logger.error(e.getMessage());
        }
        return editGroup(model,null,0,null);
    }
    
    @RequestMapping(value="/roleList",method=RequestMethod.POST, params = "assign")
    public String assignRoleToGroup(Model model,int selectedRole,int selectedGroup)
    {
        String result=  userService.assignRoleToGroup(selectedRole,selectedGroup);
        model.addAttribute("info", result);
        return roleList(model);
    }
    
    @RequestMapping(value="/roleList",method=RequestMethod.POST, params = "deassign")
    public String deassignRoleToGroup(Model model,int selectedRole,int selectedGroup)
    {
        String result= userService.deassignRoleToGroup(selectedRole,selectedGroup);
        model.addAttribute("info", result);
        return roleList(model);
    }
}
