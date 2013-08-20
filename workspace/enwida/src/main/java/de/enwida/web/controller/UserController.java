package de.enwida.web.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.db.model.UserLines;
import de.enwida.web.db.model.UserLinesMetaData;
import de.enwida.web.model.FileUpload;
import de.enwida.web.model.User;
import de.enwida.web.model.UserUploadedFile;
import de.enwida.web.service.implementation.MailServiceImpl;
import de.enwida.web.service.interfaces.IUserLinesService;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.Constants;
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
            	if(userService.saveUser(user,"http://localhost:8080/enwida/user/", request.getLocale()))
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
	
	@RequestMapping(value="/upload", method = RequestMethod.GET)
	public ModelAndView getUplaodUserData(ModelMap model) throws Exception {
		User user = userSession.getUser();
		if (user != null) {
			List<UploadedFile> filetable = new ArrayList<UploadedFile>(userService.getUploadedFiles(user));
			Collections.sort(filetable);
			model.put("uploadedfiletable", filetable);			
		}
		model.put("fileUpload", new FileUpload());
		model.put("fileReplace", new FileUpload());
		return new ModelAndView("user/upload", model);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/upload", method = RequestMethod.POST)
	public ModelAndView postUplaodUserData(ModelMap model,
			@ModelAttribute(value = "fileUpload") FileUpload fileUpload,
			BindingResult result, HttpServletRequest request) throws Exception {
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		// fetch all files related to user
		User user = userSession.getUser();

		String displayfileName = null;
		if (user != null && isMultipart) {
			List<UploadedFile> filetable = new ArrayList<UploadedFile>(
					user.getUploadedFiles());
            try {
            	FileItem item = fileUpload.getFile().getFileItem();
				File filetobeuploaded = null;
            	if (!item.isFormField()) {
					// save file in temporary directory
					filetobeuploaded = getTemporaryFile(item);
					// do validation here
					BindingResult results = validateFile(filetobeuploaded,
							fileValidator);
					ObjectError status = results.getGlobalError();

					if (status.getCode().equalsIgnoreCase(
							"file.upload.parse.success")) {

						Map<String, Object> parsedData = (Map<String, Object>) status
								.getArguments()[0];
						List<UserLines> userlines = (List<UserLines>) parsedData
								.get(Constants.UPLOAD_LINES_KEY);
						UserLinesMetaData metaData = (UserLinesMetaData) parsedData
								.get(Constants.UPLOAD_LINES_METADATA_KEY);

						boolean recordsInserted = userLineService
								.createUserLines(userlines, metaData);
						if (recordsInserted) {
							// if atleast one record is written then upload
							// file.
							UploadedFile file = saveFile(filetobeuploaded,
									user, null);
							// update user in session as well
							userSession.setUserInSession(user);
							// update file Id (which already have owner details)
							metaData.setFile(file);
							userLineService.updateUserLineMetaData(metaData);

							EnwidaUtils.removeTemporaryFile(filetobeuploaded);

							model.put("successmsg", uploadsuccessmsg);
							// update uploaded files list
							//filetable.clear();
							filetable.add(file);
						} else {
							model.put("errormsg", "Duplicate file uploaded");
						}
					} else if (status.getCode().equalsIgnoreCase(
							"file.upload.parse.error")) {
						model.put("errormsg", status.getDefaultMessage());
					}
	            }
            } catch (Exception e) {
				logger.error("Unable to upload file : " + displayfileName, e);
            }
			Collections.sort(filetable);
			model.put("uploadedfiletable", filetable);
        }
		model.put("fileUpload", new FileUpload());
		model.put("fileReplace", new FileUpload());
		return new ModelAndView("user/upload", model);
	}

	@RequestMapping(value = "/upload/replace", method = RequestMethod.POST)
	public ModelAndView replaceUplaodUserData(ModelMap model,
			@ModelAttribute(value = "fileReplace") FileUpload fileReplace,
			BindingResult result, HttpServletRequest request) throws Exception {

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		// fetch all files related to user
		User user = userSession.getUser();

		String displayfileName = null;
		if (user != null && isMultipart) {
            try {
            	FileItem item = fileReplace.getFile().getFileItem();
            	File filetobeuploaded = null;
            	if (!item.isFormField()) {
					// save file in temporary directory
					filetobeuploaded = getTemporaryFile(item);
					// do validation here
					BindingResult results = validateFile(filetobeuploaded, fileValidator);
					ObjectError status = results.getGlobalError();

					if (status.getCode().equalsIgnoreCase("file.upload.parse.success")) {

						Map<String, Object> parsedData = (Map<String, Object>) status.getArguments()[0];
						List<UserLines> userlines = (List<UserLines>) parsedData.get(Constants.UPLOAD_LINES_KEY);
						UserLinesMetaData metaData = (UserLinesMetaData) parsedData.get(Constants.UPLOAD_LINES_METADATA_KEY);

						UploadedFile oldFile = userService.getFile(fileReplace.getFileIdToBeReplaced());						
						if (oldFile != null && oldFile.getUploader().equals(user)) {
							boolean success = userLineService
									.eraseUserLines(fileReplace
											.getFileIdToBeReplaced());
							if (success) {
								boolean recordsInserted = userLineService.createUserLines(userlines, metaData);
								if (recordsInserted) {
									// if atleast one record is written then upload file.
									UploadedFile file = saveFile(
											filetobeuploaded, user, oldFile);
									// update user in session as well
									userSession.setUserInSession(user);
									// update file Id (which already have owner details)
									metaData.setFile(file);
									userLineService.updateUserLineMetaData(metaData);

									EnwidaUtils.removeTemporaryFile(filetobeuploaded);

									model.put("successmsg", uploadsuccessmsg);
								} else {
									model.put("errormsg", "Duplicate file uploaded");
								}
							}
						} else {
							model.put("errormsg", status.getDefaultMessage());
						}						
					} else if (status.getCode().equalsIgnoreCase(
							"file.upload.parse.error")) {
						model.put("errormsg", status.getDefaultMessage());
					}
	            }
            } catch (Exception e) {
            	logger.error("Unable to upload file : " + displayfileName, e);
            }
			/*List<UploadedFile> filetable = new ArrayList<UploadedFile>(user.getUploadedFiles());
            Collections.sort(filetable);
            model.put("uploadedfiletable", filetable);*/
		}	
		/*model.put("fileUpload", new FileUpload());
		model.put("fileReplace", new FileUpload());*/
		return new ModelAndView("redirect:/user/upload");
	}

	public BindingResult validateFile(File file, Validator validator) {
		// Map<String, Object> objectMap = new LinkedHashMap<String, Object>();
		// objectMap.put("file", file);
		DataBinder binder = new DataBinder(file);
		binder.setValidator(validator);
		// validate the target object
		binder.validate();
		// get BindingResult that includes any validation errors
		return binder.getBindingResult();
	}

	private File getTemporaryFile(FileItem item) throws Exception {
		String tempFile = fileUploadDirectory + File.separator + "temp"
				+ File.separator + EnwidaUtils.extractFileName(item.getName());
		EnwidaUtils.createDirectory(fileUploadDirectory + File.separator
				+ "temp");
		// do validation here
		File filetobeuploaded = new File(tempFile);
		item.write(filetobeuploaded);
		return filetobeuploaded;
	}

	private UploadedFile saveFile(File file, User user,
			UploadedFile previousFile)
			throws Exception {
		String displayfileName = file.getName();

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
		String fileFormat = EnwidaUtils.extractFileFormat(displayfileName);
		String fileName = "file";
		if (fileFormat != null && generatedId != null)
			fileName += "_" + generatedId + "." + fileFormat;
		else {
			throw new Exception("No file format");
		}
		// make sure that file directory is present
		// before uploading file
		EnwidaUtils.createDirectory(fileUploadDirectory);

		// upload file
		FileUtils.copyFile(file, new File(fileUploadDirectory + File.separator
				+ fileName));
		// IOUtils.copyLarge(new FileInputStream(file), new FileOutputStream(
		// uploadedFile));
		// item.write(uploadedFile);

		// update file manifest data
		File manifestFile = new File(fileUploadDirectory + File.separator + fileName + ".mfst");
		try {
			EnwidaUtils.writeAllText(manifestFile, displayfileName);
		} catch (Exception e) {
			logger.error("Update Manifest data for uploaded File: " + file.getName(), e);
		}

		// create entry in file upload table
		// UploadedFile uploadedfile = fileId > 0 ? userService.getFile(fileId)
		// : new UploadedFile();
		UploadedFile uploadedfile = new UploadedFile();
		uploadedfile.setDisplayFileName(displayfileName);
		uploadedfile.setFileName(fileName);
		uploadedfile.setFilePath(fileUploadDirectory + File.separator + fileName);
		uploadedfile.setFormat(fileFormat);
		uploadedfile.setUploadDate(new Date());
		uploadedfile.setUploader(user);
		
		if (previousFile != null) {
			// replace option means previous file should be set as reference in
			// new file. Revision will be automatically updated
			// based on previous file revision
			uploadedfile.setPreviousFile(previousFile);
		}

		user = userService.saveUserUploadedFile(user, uploadedfile);

		return uploadedfile;
	}
	
	@RequestMapping(value = "/files/delete", method = RequestMethod.GET)
	public ModelAndView deleteUploadedFile(
			@RequestParam("fileId") String fileId, Locale locale) {

		if (fileId != null && !fileId.isEmpty()) {
			int fileid = Integer.parseInt(fileId);
			UploadedFile downloadFile = userService.getFile(fileid);
			
			if (downloadFile != null) {
				boolean isFileToDeleteLatest = true;
				UploadedFile fileToMakeActive = null;
				// Before deleting file entry update previous file properly
				// get uploaded file where the deleted file was used as previous
				// file. update previous entry reference with deleted file
				// previous reference
				User user = userSession.getUser();
				for (UploadedFile upfile : user.getUploadedFiles()) {
					if (upfile.getPreviousFile() != null
							&& upfile.getPreviousFile()
									.equals(downloadFile)) {
						// this means deleted file has been used as previous
						// file in some other file
						isFileToDeleteLatest = false;
						logger.debug("Updating references");
						upfile.setPreviousFile(downloadFile
								.getPreviousFile());
						userService.updateUserUploadedFile(user, upfile);
						fileToMakeActive = upfile;
					}
				}
				
				// now delete file safely
				try {
					boolean success = userLineService
							.eraseUserLines(downloadFile.getId());
					// first remove all mapppings
					userService.removeUserUploadedFile(user, downloadFile);
					// actual row deleted
					userService.removeUserUploadedFile(user, downloadFile);
					userSession.setUserInSession(userService.fetchUser(user
							.getUserId()));
					if (success) {
						EnwidaUtils.removeTemporaryFile(downloadFile
								.getActualFile());
						EnwidaUtils.removeTemporaryFile(downloadFile
								.getManifestFile());
					}
				} catch (Exception e) {
					logger.error(
							"Unable to delete file :'"
									+ downloadFile.getFilePath()
									+ "' .Please delete file manually.", e);
				}
				if (isFileToDeleteLatest) {
					// switch back to last revision
					// fileToMakeActive should be made active here
				}
			}
		}
		return new ModelAndView("redirect:/user/upload");
	}

	@RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource downloadFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
		
		if (fileId != null && !fileId.isEmpty()) {
			int fileid = Integer.parseInt(fileId);
			UploadedFile downloadFile = userService.getFile(fileid);
			
			if (downloadFile != null) {
				String filePath = downloadFile.getFilePath();
				File file = new File(filePath);
				response.setHeader("Content-Disposition", "attachment; filename=\""
						+ downloadFile.getDisplayFileName() + "\"");
				try {
					response.setContentType("text/csv; charset=utf-8");
					return new FileSystemResource(file);
				} catch (Exception e) {
					logger.error("Unable to download file : ", e);
				}
			} else {
				//TODO: Complete the Implementation
			}
		}
		return null;
	}
	
	@RequestMapping(value = "/files/revisions", method = RequestMethod.GET)
	public @ResponseBody List<UserUploadedFile> getFileRevisions(@RequestParam("fileId") String fileId, Locale locale) {
		
		List<UserUploadedFile> revisions = new ArrayList<UserUploadedFile>();
		if (fileId != null && !fileId.isEmpty()) {
			int fileid = Integer.parseInt(fileId);
			UploadedFile file = userService.getFile(fileid);			
			if (file != null) {
				while (file.getPreviousFile() != null) {
					file = file.getPreviousFile();
					revisions.add(new UserUploadedFile(file));					
				}
			}
		}
		return revisions;
	}
}
