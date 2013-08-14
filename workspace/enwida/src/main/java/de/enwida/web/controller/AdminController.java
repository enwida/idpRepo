package de.enwida.web.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.web.model.Group;
import de.enwida.web.model.Right;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IAspectService;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.validator.UserValidator;


/**
 * Handles requests for the user service.
 * @author olcay tarazan
 *
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
	
	@Autowired
	private MessageSource messageSource;
	

    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	
	@RequestMapping(value="/admin_editaspect", method = RequestMethod.GET)
	public String editAspect(HttpServletRequest request,Model model,long roleID,int start,int max) {
	    List<Right> aspectRights;
        List<Role> roles = null;
        try {
            aspectRights = aspectService.getAllAspects(roleID,start,max);
            roles = userService.getAllRoles();
            //Get all roles
            model.addAttribute("roles", roles);
            //Get all aspects status of requested role
            model.addAttribute("aspectRights", aspectRights);
        } catch (Exception e) {
            model.addAttribute("Info", messageSource.getMessage("de.enwida.userManagement.error.notAllowed", null, request.getLocale()));
        }
	    //Present the page
		model.addAttribute("content", "admin/admin_editAspect");
		return "user/master";
	}
	/**
	 * Gets all the groups and user and present
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/admin_userlist", method = RequestMethod.GET)
	public String userList(HttpServletRequest request, Model model) {
	    //Gets all the users
	    List<User> users;
        try { 
            //Get all the groups
            List<Group> groups=userService.getAllGroups();
            users = userService.getAllUsers();
            model.addAttribute("users", users);  
            model.addAttribute("groups", groups);
        } catch (Exception e) {
            logger.info(e.getMessage());
            model.addAttribute("error", messageSource.getMessage("de.enwida.userManagement.error.notAllowed", null, request.getLocale()));
        }

        model.addAttribute("content", "admin/admin_userList");
        return "user/master";
	}
    /**
     * Handles editGroup page reuqests
     * @param model
     * @param action delete or add is allowed when it presents
     * @param groupID
     * @param newGroup
     * @return
     */
    @RequestMapping(value="/admin_editgroup", method = RequestMethod.GET)
    public String editGroup(HttpServletRequest request,Model model,String action,Integer groupID,String newGroup) {    
        try {
            if (action!=null){
                //Check which action to be executed
                if( action.equalsIgnoreCase("delete")){
                            userService.deleteGroup(groupID);
                }else if (action.equalsIgnoreCase("add")){
                            Group group=new Group();
                            group.setGroupName(newGroup);            
                            userService.addGroup(group);
                }
                //Print info message
                //TODO:These messages will be localized
                model.addAttribute("info", "OK");
            }
            //Get groups with user information attached
            List<Group> groupsWithUsers= userService.getAllGroups();
            model.addAttribute("groupsWithUsers", groupsWithUsers);
            //Get all the users
            List<User> users= userService.getAllUsers();
            model.addAttribute("users", users);
            
        } catch (Exception e) {
            logger.info(e.getMessage());
            model.addAttribute("error", messageSource.getMessage("de.enwida.userManagement.error.notAllowed", null, request.getLocale()));
        }
        
        model.addAttribute("content", "admin/admin_editGroup");
        return "user/master";
    }
       
    /**
     * Handles role list page requests
     * @param model
     * @return
     */
    @RequestMapping(value="/admin_rolelist", method = RequestMethod.GET)
    public String roleList(HttpServletRequest request,Model model) {
        try {
            List<Role> roles= userService.getAllRoles();
            model.addAttribute("roles", roles);
            
            List<Role> rolesWithGroups= userService.getAllRolesWithGroups();
            model.addAttribute("rolesWithGroups", rolesWithGroups);
            
            List<Group> groups= userService.getAllGroups();
            model.addAttribute("groups", groups);
        } catch (Exception e) {
            model.addAttribute("error", messageSource.getMessage("de.enwida.userManagement.error.notAllowed", null, request.getLocale()));
            logger.error(e.getMessage());
        }
       
        model.addAttribute("content", "admin/admin_roleList");
        return "user/master";
    }
    
    /**
     * Default user list page is displayed
     * @param model
     * @return
     */
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String admin(HttpServletRequest request,Model model) {
        return userList(request,model);
    }
    
    /**
     * User actions log will be displayed by reading the user log file
     * @param model
     * @param user
     * @return
     */
    @RequestMapping(value="/admin_userlog", method = RequestMethod.GET)
    public String  userLog(HttpServletRequest request,Model model,String user) {
        File file;
        try {
            //read the user log file and display it
            file=new File(System.getenv("ENWIDA_HOME")+"/log/"+user+".log");
            model.addAttribute("userLog",FileUtils.readFileToString(file));
        } catch (Exception e) {
            model.addAttribute("error", messageSource.getMessage("de.enwida.userManagement.error.notAllowed", null, request.getLocale()));
            logger.error(e.getMessage());
        } 
        model.addAttribute("content", "admin/admin_userLog");
        return "user/master";
    }
    
    
    @RequestMapping(value = "/enableDisableUser", method = RequestMethod.GET)
    @ResponseBody
    public boolean enableDisableUser(int userID,boolean enabled) {
        try {
            userService.enableDisableUser(userID,enabled); 
            return true;       
        } catch (Exception e) {   
            logger.info(e.getMessage());
            return false;      
        }
    }   
    
    @RequestMapping(value = "/enableDisableAspect", method = RequestMethod.GET)
    @ResponseBody
    public boolean enableDisableAspect(int rightID,boolean enabled) {
        try {
            userService.enableDisableAspect(rightID,enabled);
            return true;       
        } catch (Exception e) {   
            logger.info(e.getMessage());
            return false;      
        }
    }   
    
    @RequestMapping(value="/admin_user", method = RequestMethod.GET)
    public String user(HttpServletRequest request,Model model,long userID) {
        
        User user = null;
        try {
            user = userService.getUser(userID);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        if (user==null){
            model.addAttribute("Info",  messageSource.getMessage("de.enwida.userManagement.userNotFound", null, request.getLocale()));
            //This shouldnt happen
            logger.info("User is not found:userID:"+userID);
            //Redirect user to main page;
            return admin(request,model);            
        }
        model.addAttribute("user", user);
        model.addAttribute("content", "admin/admin_user");
        return "user/master";
    }
    
    @RequestMapping(value="/admin_user",method=RequestMethod.POST, params = "save")
    public String processForm(@ModelAttribute(value="USER")User user,long userID,HttpSession session, Model model,HttpServletRequest request)
    {
        User newUser = null;
        try {
            newUser = userService.getUser(userID);
            newUser.setFirstName(user.getFirstName());
            newUser.setLastName(user.getLastName());
            newUser.setTelephone(user.getTelephone());
            newUser.setCompanyName(user.getCompanyName());
            userService.updateUser(newUser);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        if (user==null){
            model.addAttribute("Info", messageSource.getMessage("de.enwida.userManagement.userNotFound", null, request.getLocale()));
            //This shouldnt happen
            logger.info("User is not found:userID:"+userID);
            //Redirect user to main page;
            return admin(request,model);            
        }

        model.addAttribute("user", newUser);
        model.addAttribute("content", "admin/admin_user");
        return "user/master";
    }

    @RequestMapping(value="/admin_user",method=RequestMethod.POST, params = "resetPassword")
    public String reset(HttpServletRequest request,Model model,long userID)
    {
        System.out.println("ResetPassword");
        try {
            userService.resetPassword(userID);
            model.addAttribute("info", "OK");
        } catch (Exception e) {
            model.addAttribute("error", "Error:"+e.getLocalizedMessage());
            logger.error(e.getMessage());
        }
        return user(request,model,userID);
    }
    
    
    @RequestMapping(value="/admin_user",method=RequestMethod.POST, params = "delete")
    public String deleteUser(HttpServletRequest request,Model model,long userID)
    {
        System.out.println("DeleteUser");
        try {
            User user=userService.getUser(userID);
            userService.deleteUser(user);
            
        } catch (Exception e) {
            model.addAttribute("Info", messageSource.getMessage("de.enwida.userManagement.error.notAllowed", null, request.getLocale()));
            logger.info(e.getMessage());
            return user(request,model,userID);
        }
        return userList(request,model);
    }
    
    @RequestMapping(value="/admin_editgroup",method=RequestMethod.POST, params = "assign")
    public String assignUserToGroup(HttpServletRequest request,Model model,int selectedUser,int selectedGroup)
    {
        try {
			userService.assignGroupToUser(new Long(selectedUser), new Long(
					selectedGroup));
            model.addAttribute("info", "OK");       
        } catch (Exception e) {   
            logger.info(e.getMessage());
            model.addAttribute("Error", messageSource.getMessage("de.enwida.userManagement.error.notAllowed", null, request.getLocale()));       
        }
        return editGroup(request,model,null,0,null);
    }
    
    @RequestMapping(value="/admin_editgroup",method=RequestMethod.POST, params = "deassign")
    public String deassignUserToGroup(HttpServletRequest request,Model model,int selectedUser,int selectedGroup)
    {
        try {
            userService.revokeUserFromGroup(selectedUser,selectedGroup); 
            model.addAttribute("info", "OK");       
        } catch (Exception e) {   
            logger.info(e.getMessage());
            model.addAttribute("Error", messageSource.getMessage("de.enwida.userManagement.error.notAllowed", null, request.getLocale()));       
        }
        return editGroup(request,model,null,0,null);
    }
    
    @RequestMapping(value="/admin_editgroup",method=RequestMethod.POST, params = "addGroup")
    public String addGroup(HttpServletRequest request,Model model,String newGroup,boolean autoPass)
    {
        Group group=new Group();
        group.setGroupName(newGroup);
        group.setAutoPass(autoPass);
        try {
            userService.addGroup(group);
            
        } catch (Exception e) {
            model.addAttribute("error", messageSource.getMessage("de.enwida.userManagement.error.notAllowed", null, request.getLocale()));
            logger.error(e.getMessage());
        }
        return editGroup(request,model,null,0,null);
    }
    
    @RequestMapping(value="/admin_rolelist",method=RequestMethod.POST, params = "assign")
    public String assignRoleToGroup(HttpServletRequest request,Model model,int selectedRole,int selectedGroup)
    {
        try {
            userService.assignRoleToGroup(selectedRole,selectedGroup);     
            model.addAttribute("info", "OK");       
        } catch (Exception e) {   
            logger.info(e.getMessage());
            model.addAttribute("Error", messageSource.getMessage("de.enwida.userManagement.error.notAllowed", null, request.getLocale()));       
        }
        return roleList(request,model);
    }
    
    @RequestMapping(value="/admin_rolelist",method=RequestMethod.POST, params = "deassign")
    public String deassignRoleToGroup(HttpServletRequest request,Model model,int selectedRole,int selectedGroup)
    {
        try {
            userService.revokeRoleFromGroup(selectedRole,selectedGroup);  
            model.addAttribute("info", "OK");       
        } catch (Exception e) {   
            logger.info(e.getMessage());
            model.addAttribute("Error", messageSource.getMessage("de.enwida.userManagement.error.notAllowed", null, request.getLocale()));       
        }
        return roleList(request,model);
    }
}
