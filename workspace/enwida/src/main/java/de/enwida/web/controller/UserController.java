package de.enwida.web.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.enwida.web.model.User;
import de.enwida.web.service.implementation.MailServiceImpl;
import de.enwida.web.service.interfaces.IUserLinesService;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.EnwidaUtils;
import de.enwida.web.utils.LogoFinder;
import de.enwida.web.validator.FileValidator;
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
	private IUserLinesService userLineService;

	@Autowired
	private UserSessionManager userSession;
	
	@Autowired
	private UserValidator userValidator;

	@Autowired
	private FileValidator fileValidator;
    
	@Autowired
	private MessageSource messageSource;
 
	@Autowired	
	private MailServiceImpl mail;	

    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);

    @Value("#{applicationProperties['fileUploadDirectory']}")
	private String fileUploadDirectory;

	@Value("#{applicationProperties['file.upload.parse.success']}")
	private String uploadsuccessmsg;
	
	@PostConstruct
	public void init() {
		fileUploadDirectory = EnwidaUtils.resolveEnvVars(fileUploadDirectory);
	}

	@RequestMapping(value="/user", method = RequestMethod.GET)
	public String displayDashboard(Model model, Locale locale) {
		try{
	        User u = userService.fetchUser(new Long(0));
	        model.addAttribute("user", u);		    
		}catch(Exception e){
            logger.info(e.getMessage());
            model.addAttribute("Error", messageSource.getMessage("de.enwida.userManagement.notAllowed", null, locale)); 		    
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
	        return index(request, model, principal);
	    }else{
	        return "user/login";
	    }
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
		return "logout";
	}
	
	@RequestMapping(value="/loginFailed", method = RequestMethod.GET)
    public String loginFailed(HttpServletRequest request,HttpServletResponse response,ModelMap model,Principal principal) {
        //if user is logged in, dont return him to loginFailed page
	    if(principal!=null){
            try {
                response.sendRedirect("index");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        model.addAttribute("error", "true");
        return "user/login";
    }
	
	@RequestMapping(value="/download", method = RequestMethod.GET)
	public String download(ModelMap model) {
		return "user/download";
	}
	
	public void preProcessRegisterForm(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
	    
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
        		user.setUserName(user.getEmail());
            	if(userService.saveUser(user,"http://localhost:8080/enwida/user/", request.getLocale(),true))
            	{                                       		
            		String name = user.getFirstName() + " " + user.getLastName();
            		String userStatus="logout";
            		String userStatusURL="../j_spring_security_logout";

            		model.addAttribute("username", name);
            		model.addAttribute("userStatus", userStatus);
            		model.addAttribute("userStatusURL", userStatusURL);  
            		model.addAttribute("content","user/index");    
            		//We dont want prelogin
            		//clear the session and security context and redirect user to main page
            		SecurityContextHolder.clearContext();
            		HttpSession session = request.getSession(false);
            		if (session != null) {
            			session.invalidate();
            		}     
            		response.sendRedirect("index");
            	}
            }else{
                model.addAttribute("content","user/register");
            }
            model.addAllAttributes(result.getModel());
            }
	    
	}
	
	@RequestMapping(value="/register",method=RequestMethod.GET)
    public String showForm(ModelMap model, HttpServletRequest request,HttpServletResponse response,Principal principal){
	    try {
            preProcessRegisterForm(request,response,model);
        } catch (Exception e) {
            logger.info(e.getMessage());
            model.addAttribute("Error", messageSource.getMessage("de.enwida.userManagement.notAllowed", null, request.getLocale()));
        }
	    if(principal!=null){
            return index(request, model, principal);
        }else{
            return "master";
        }
    }
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String processForm(@ModelAttribute(value="USER") User user, ModelMap model, HttpServletRequest request,HttpServletResponse response)
	{
		try {
            preProcessRegisterForm(request,response,model);
        } catch (Exception e) {
            logger.info(e.getMessage());
            model.addAttribute("Error",messageSource.getMessage("de.enwida.userManagement.notAllowed", null, request.getLocale()));
        }
        return "master";
	}
	
	@RequestMapping(value="/checkEmail",method=RequestMethod.GET)
	public @ResponseBody String checkEmail(HttpServletRequest request,ModelMap model,String email){
		
		boolean availabilityCheck = false;
        try {
            availabilityCheck = userService.userNameAvailability(email);
        } catch (Exception e) {
            logger.info(e.getMessage());
            model.addAttribute("Error", messageSource.getMessage("de.enwida.userManagement.notAllowed", null, request.getLocale()));
        }
		
		if(availabilityCheck)
		{
			model.addAttribute("emailAvailabilityError",messageSource.getMessage("de.enwida.userManagement.userNameNotAvailable", null, request.getLocale()));
		}
		
		return availabilityCheck + "";
	}

	@RequestMapping(value="/activateuser.html",method=RequestMethod.GET)
	public String activateUser(HttpServletRequest request,ModelMap model, String username, String actId){
		
		boolean activated = false;
        try {
            activated = userService.activateUser(username, actId);
        } catch (Exception e) {
            logger.info(e.getMessage());
            model.addAttribute("Error", messageSource.getMessage("de.enwida.userManagement.notAllowed", null, request.getLocale()));
        }
		if(activated)
		{
			User user = userService.fetchUser(username);
			String name = user.getFirstName() + " " + user.getLastName();
    		String userStatus="logout";
    		String userStatusURL="../j_spring_security_logout";

    		model.addAttribute("username", name);
    		model.addAttribute("userStatus", userStatus);
    		model.addAttribute("userStatusURL", userStatusURL);
            model.addAttribute("info", "Activated");  
		}else{
	        model.addAttribute("error", messageSource.getMessage("de.enwida.userManagement.notAllowed", null, request.getLocale()));  
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
	public String forgotPassword(HttpServletRequest request,ModelMap model,String email){
		
		String password = null;
        try {
            password = userService.getPassword(email);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
		if(password==null){
			model.addAttribute("error", messageSource.getMessage("de.enwida.userManagement.userNotFound", null, request.getLocale()));
		}else{
			try {
				mail.SendEmail(email, messageSource.getMessage("de.enwida.userManagement.yourPassword", null, request.getLocale())+":",password);
			} catch (Exception e) {
	            logger.error(e.getMessage());
				model.addAttribute("error", messageSource.getMessage("de.enwida.userManagement.mailingError", null, request.getLocale()));
			}
		}
		return "user/forgotPassword";
	}	
}
