package de.enwida.web.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.model.FileUpload;
import de.enwida.web.model.User;
import de.enwida.web.service.implementation.MailServiceImpl;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.Constants;
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
	private UserSessionManager userSession;

	@Autowired
	private UserValidator userValidator;
 
	@Autowired	
	private MailServiceImpl mail;	

    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
	
	@Value("#{applicationProperties['fileUploadDirectory']}")
	protected String fileUploadDirectory;

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
			userStatusURL=userStatus="";
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

	@RequestMapping(value="/activateUser",method=RequestMethod.GET)
	public @ResponseBody String activateUser(ModelMap model, String username, String actId){
		
		boolean activated = userService.activateUser(username, actId);
		if(activated)
		{
			String name = "Test Test";
    		String userStatus="logout";
    		String userStatusURL="../j_spring_security_logout";

    		model.addAttribute("username", name);
    		model.addAttribute("userStatus", userStatus);
    		model.addAttribute("userStatusURL", userStatusURL);
    		return "user/index";  
		}
				
		return "";
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
	            logger.error(e.getMessage());
				model.addAttribute("error", "Mailling Error");
			}
		}
		return "user/forgotPassword";
	}
	
	@RequestMapping(value="/upload", method = RequestMethod.GET)
	public ModelAndView getUplaodUserData(ModelMap model) {
		User user = userService.getUser("username1");
		List<UploadedFile> filetable = new ArrayList<UploadedFile>(
				user.getUploadedFiles());
		Collections.sort(filetable);
		model.put("uploadedfiletable", filetable);
		model.put("fileUpload", new FileUpload());
		return new ModelAndView("user/upload", model);
	}
	
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public ModelAndView postUplaodUserData(ModelMap model,
			@ModelAttribute(value = "fileUpload") FileUpload fileUpload,
			BindingResult result, HttpServletRequest request) {
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		// fetch all files related to user
		User user = userService.getUser("username1");
		List<UploadedFile> filetable = new ArrayList<UploadedFile>(
				user.getUploadedFiles());

		String displayfileName = null;
		if (isMultipart) {
            try {
            	FileItem item = fileUpload.getFile().getFileItem();
            	if (!item.isFormField()) {
					displayfileName = item.getName();

					// Get the next generated value of file sequence
					Long generatedId = null;
					try {
						generatedId = userService.getNextSequence(
								Constants.UPLOADED_FILE_SEQUENCE_SCHEMA_NAME,
								Constants.UPLOADED_FILE_SEQUENCE_NAME);
					} catch (Exception e) {
						// TODO: handle exception
						generatedId = new Long(1);
					}
					String fileFormat = extractFileFormat(displayfileName);
					String fileName = "file";
					if (fileFormat != null && generatedId != null)
						fileName += "_" + generatedId + "." + fileFormat;
					else {
						throw new Exception("No file format");
					}
					// make sure that file directory is present
					// before uploading file
					createDirectory(fileUploadDirectory);

					// upload file
					File uploadedFile = new File(fileUploadDirectory
							+ File.separator + fileName);
					item.write(uploadedFile);

					// update file manifest data
					File manifestFile = new File(fileUploadDirectory
							+ File.separator + fileName + ".mfst");
					writeAllText(manifestFile, displayfileName);

					// create entry in file upload table
					UploadedFile file = new UploadedFile();
					file.setDisplayFileName(displayfileName);
					file.setFilePath(fileUploadDirectory + File.separator
							+ fileName);
					file.setFormat(fileFormat);
					file.setUploadDate(new Date());


					file.setUploader(user);
					// User user = userSession.getUser();
					user.addUploadedFile(file);
					userService.updateUser(user);

					file = userService.getFileByFilePath(file.getFilePath());
					// update uploaded files list
					filetable.add(file);
	            }
            } catch (Exception e) {
				logger.error("Unable to upload file : " + displayfileName, e);
            }
        }
		Collections.sort(filetable);
		model.put("uploadedfiletable", filetable);
		model.put("fileUpload", new FileUpload());
		return new ModelAndView("user/upload", model);
	}

	/**
	 * Creates a new directory depending on the input path given
	 * 
	 * @param path
	 *            directory path that needs to be created
	 * @return success status of directory creation
	 */
	public boolean createDirectory(String path) {
		boolean status = false;

		if (path != null && !path.trim().isEmpty()) {
			File f = new File(path);
			f.setWritable(true);

			if (!f.exists()) {
				status = f.mkdirs();
			}
		}
		return status;
	}

	/**
	 * Writes given content to a file.
	 * 
	 * @param file
	 *            the file to write to
	 * @param text
	 *            the text to write.
	 */
	public void writeAllText(File file, String text) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(text);
			out.close();
		} catch (Exception e) {
			logger.error(
					"Update Manifest data for uploaded File: " + file.getName(),
					e);
		}
	}

	private String extractFileFormat(String completeName) {
		String[] fnameParts = completeName.split("\\\\");
		String fname = fnameParts[fnameParts.length - 1];
		// this.displayFileName = fname;

		fnameParts = completeName.split("\\.");
		if (fnameParts.length < 2) {
			// file without format.
			return null;
		}

		return fnameParts[fnameParts.length - 1];
	}

	@RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET)
	@ResponseBody
	public String downloadFile(@PathVariable("fileId") String fileId,
			HttpServletResponse reponse) {
		if (fileId != null) {
			int fileid = Integer.parseInt(fileId);
			// System.out.println("File Id : " + userService.getFile(fileid));
			UploadedFile downloadFile = userService.getFile(fileid);
			if (downloadFile == null)
				return null;
			String filePath = downloadFile.getFilePath();
			File file = new File(filePath);
			reponse.setHeader("Content-Disposition", "attachment; filename=\""
					+ downloadFile.getDisplayFileName() + "\"");
			OutputStream out = null;
			try {
				out = reponse.getOutputStream();
				reponse.setContentType("text/plain; charset=utf-8");
				FileInputStream fi = new FileInputStream(file);

				IOUtils.copy(fi, out);
				out.flush();
				out.close();
			} catch (IOException e) {
				logger.error("Unable to download file : ", e);
			} finally {
				out = null;
			}
		}
		return null;
	}
}
