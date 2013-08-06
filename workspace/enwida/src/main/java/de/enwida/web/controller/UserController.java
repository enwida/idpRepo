package de.enwida.web.controller;

import java.io.File;
import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.enwida.web.model.FileUpload;
import de.enwida.web.model.User;
import de.enwida.web.service.implementation.MailServiceImpl;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.LogoFinder;
import de.enwida.web.validator.UserValidator;

/**
 * Handles requests for the user service.
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private UserValidator userValidator;
    
    @Autowired
    ReloadableResourceBundleMessageSource messageSource;
 
	@Autowired	
	private MailServiceImpl mail;	

    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	@RequestMapping(value="/user", method = RequestMethod.GET)
	public String displayDashboard(Model model, Locale locale) {
		try{
	        User u = userService.getUser(new Long(0));
	        model.addAttribute("user", u);		    
		}catch(Exception e){
            logger.info(e.getMessage());
            model.addAttribute("Error", "Not Allowed"); 		    
		}
		
		return "user";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request,ModelMap model, Principal principal) {
		return index(request,model, principal);
	}
	
	   @RequestMapping(value = "/index", method = RequestMethod.GET)
	    public String index(HttpServletRequest request,ModelMap model, Principal principal) {
	        String name,userStatus,userStatusURL;
	        
	        if(principal!=null){
	            name = principal.getName();
	            userStatus=messageSource.getMessage("de.enwida.userManagement.logout", null, request.getLocale());
	            userStatusURL="../j_spring_security_logout";
	        }else{
	            name="anonymous";
	            userStatusURL="login";
	            userStatus=messageSource.getMessage("de.enwida.userManagement.login", null, request.getLocale());
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
	public String login(ModelMap model,HttpServletRequest request,Principal principal) {

	    String referrer = request.getHeader("Referer");
	    if(referrer!=null){
	        request.getSession().setAttribute("url_prior_login", referrer);
	    }
	    if(principal!=null){
	        return "user/index";
	    }else{
		return "user/login";
	    }
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
	
	public void preProcessRegisterForm(HttpServletRequest request,ModelMap model) throws Exception{
	    
	    if(request.getMethod().equalsIgnoreCase("GET")){  
	        User user=new User();
            model.addAttribute("USER", user);
            model.addAttribute("content", "user/register");
	    }else{
	        User user=(User) model.get("USER");
	        BindingResult result =new BeanPropertyBindingResult(user, "USER");
	        userValidator.validate(user, result);       

            if (!result.hasErrors())
            {
                if(userService.saveUser(user,"http://localhost:8080/enwida/user/"))
                {                           
                    String name = user.getFirstName() + " " + user.getLastName();
                    String userStatus="logout";
                    String userStatusURL="../j_spring_security_logout";
    
                    model.addAttribute("username", name);
                    model.addAttribute("userStatus", userStatus);
                    model.addAttribute("userStatusURL", userStatusURL);     
                    model.addAttribute("content","user/index");        
                }
            }else{
                model.addAttribute("content","user/register");
            }
            model.addAllAttributes(result.getModel());
	    }
	}
	
	@RequestMapping(value="/register",method=RequestMethod.GET)
    public String showForm(ModelMap model, HttpServletRequest request){
	    try {
            preProcessRegisterForm(request,model);
        } catch (Exception e) {
            logger.info(e.getMessage());
            model.addAttribute("Error", "Not Allowed");
        }
	    
        return "master";
    }
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String processForm(@ModelAttribute(value="USER") User user, ModelMap model, HttpServletRequest request)
	{
		try {
            preProcessRegisterForm(request,model);
        } catch (Exception e) {
            logger.info(e.getMessage());
            model.addAttribute("Error", "Not Allowed");
        }
        return "master";
	}
	
	@RequestMapping(value="/checkEmail",method=RequestMethod.GET)
	public @ResponseBody String checkEmail(ModelMap model,String email){
		
		boolean availabilityCheck = false;
        try {
            availabilityCheck = userService.userNameAvailability(email);
        } catch (Exception e) {
            logger.info(e.getMessage());
            model.addAttribute("Error", "Not Allowed");
        }
		
		if(availabilityCheck)
		{
			model.addAttribute("emailAvailabilityError", "This email is already in use by some other user.");
		}
		
		return availabilityCheck + "";
	}

	@RequestMapping(value="/activateuser.html",method=RequestMethod.GET)
	public String activateUser(ModelMap model, String username, String actId){
		
		boolean activated = false;
        try {
            activated = userService.activateUser(username, actId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            model.addAttribute("Error", "Not Allowed");
        }
		if(activated)
		{
			String name = "Test Test";
    		String userStatus="logout";
    		String userStatusURL="../j_spring_security_logout";

    		model.addAttribute("username", name);
    		model.addAttribute("userStatus", userStatus);
    		model.addAttribute("userStatusURL", userStatusURL);
            model.addAttribute("info", "Activated");  
		}else{
	        model.addAttribute("error", "Not Allowed");  
		}
   
        model.addAttribute("content", "user/index");
        return "master";
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
		
		String password = null;
        try {
            password = userService.getPassword(email);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
		if(password==null){
			model.addAttribute("error", "User is not found");
		}else{
			try {
				mail.SendEmail(email,"Your Password:",password);
			} catch (Exception e) {
	            logger.error(e.getMessage());
				model.addAttribute("error", "Mailling Error");
			}
		}
		return "user/forgotPassword";
	}
	
	@RequestMapping(value="/upload", method = RequestMethod.GET)
	public ModelAndView getUplaodUserData() {

		return new ModelAndView("user/upload");
	}
	
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public ModelAndView postUplaodUserData(@ModelAttribute(value="fileUpload") FileUpload fileUpload, BindingResult result, HttpServletRequest request) {
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            try {
            	FileItem item = fileUpload.getFile().getFileItem();
            	
            	if (!item.isFormField()) {
	                String fileName = item.getName();
	
	                String root = request.getSession().getServletContext().getRealPath("/");
	                File path = new File(root + "/uploads");
	                if (!path.exists()) {
	                    boolean status = path.mkdirs();
	                }
	
	                File uploadedFile = new File(path + "/" + fileName);
	                System.out.println(uploadedFile.getAbsolutePath());
	                //item.write(uploadedFile);
	            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return new ModelAndView("user/upload", "fileUpload", new FileUpload() );
	}
}
