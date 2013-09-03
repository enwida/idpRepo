package de.enwida.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import de.enwida.rl.dtos.DOUserLines;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.db.model.UserLinesMetaData;
import de.enwida.web.model.FileUpload;
import de.enwida.web.model.User;
import de.enwida.web.model.UserUploadedFile;
import de.enwida.web.service.interfaces.IUploadFileService;
import de.enwida.web.service.interfaces.IUserLinesService;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.Constants;
import de.enwida.web.utils.EnwidaUtils;
import de.enwida.web.validator.FileValidator;

/**
 * Handles requests for the user service.
 */
@Controller
@RequestMapping("/upload")
public class UploadController {

	@Autowired
	private IUploadFileService uploadFileService;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private UserSessionManager userSession;
	
	@Autowired
	private FileValidator fileValidator;
	
	@Autowired
	private IUserLinesService userLineService;

	@Value("#{applicationProperties['fileUploadDirectory']}")
	private String fileUploadDirectory;

	@Value("#{applicationProperties['file.upload.parse.success']}")
	private String uploadsuccessmsg;
	
	private static org.apache.log4j.Logger logger = Logger.getLogger(UploadController.class);
    
	@PostConstruct
	public void init() {
		fileUploadDirectory = EnwidaUtils.resolveEnvVars(fileUploadDirectory);
	}

	@RequestMapping(value="/files", method = RequestMethod.GET)
	public ModelAndView getUplaodUserData(ModelMap model) throws Exception {
		User user = userSession.getUser();
		if (user != null) {
			List<UploadedFile> filetable = new ArrayList<UploadedFile>(uploadFileService.getUploadedFiles(user));
			Collections.sort(filetable);
			model.put("uploadedfiletable", filetable);			
		}
		model.put("fileUpload", new FileUpload());
		model.put("fileReplace", new FileUpload());
		return new ModelAndView("user/upload", model);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/files", method = RequestMethod.POST)
	public ModelAndView postUplaodUserData(ModelMap model,
			@ModelAttribute(value = "fileUpload") FileUpload fileUpload,
			BindingResult result, HttpServletRequest request) throws Exception {
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		User user = userSession.getUser();

		String displayfileName = null;
		if (user != null && isMultipart) {
            try {
            	FileItem item = fileUpload.getFile().getFileItem();
				File filetobeuploaded = null;
            	if (!item.isFormField()) {
					// save file in temporary directory
					filetobeuploaded = getTemporaryFile(item);
					// do validation here
					BindingResult results = EnwidaUtils.validateFile(filetobeuploaded, fileValidator);
					ObjectError status = results.getGlobalError();

					if (status.getCode().equalsIgnoreCase("file.upload.parse.success")) {

						Map<String, Object> parsedData = (Map<String, Object>) status.getArguments()[0];
						List<DOUserLines> userlines = (List<DOUserLines>) parsedData
								.get(Constants.UPLOAD_LINES_KEY);
						UserLinesMetaData metaData = (UserLinesMetaData) parsedData.get(Constants.UPLOAD_LINES_METADATA_KEY);

						boolean recordsInserted = userLineService.createUserLines(userlines, metaData);
						if (recordsInserted) {
							// if atleast one record is written then upload
							// file.
							UploadedFile file = saveFile(filetobeuploaded, user, null);
							// update user in session as well
							userSession.setUserInSession(user);
							// update file Id (which already have owner details)
							metaData.setFile(file);
							userLineService.updateUserLineMetaData(metaData);

							EnwidaUtils.removeTemporaryFile(filetobeuploaded);
						} else {
							//TODO: FileUpload Fail: Duplicate File Upload
						}
					} else if (status.getCode().equalsIgnoreCase("file.upload.parse.error")) {
						//TODO: FileUpload Fail: Parsing Error
					}
	            }
            } catch (Exception e) {
				logger.error("Unable to upload file : " + displayfileName, e);
            }			
        }		
		return new ModelAndView("redirect:/upload/files");
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/files/replace", method = RequestMethod.POST)
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
					BindingResult results = EnwidaUtils.validateFile(filetobeuploaded, fileValidator);
					ObjectError status = results.getGlobalError();

					if (status.getCode().equalsIgnoreCase("file.upload.parse.success")) {

						Map<String, Object> parsedData = (Map<String, Object>) status.getArguments()[0];
						List<DOUserLines> userlines = (List<DOUserLines>) parsedData
								.get(Constants.UPLOAD_LINES_KEY);
						UserLinesMetaData metaData = (UserLinesMetaData) parsedData.get(Constants.UPLOAD_LINES_METADATA_KEY);

						UploadedFile oldFile = uploadFileService.getFile(fileReplace.getFileIdToBeReplaced());						
						if (oldFile != null && oldFile.getUploader().equals(user)) {
							//Deleting the old lines from the database
							boolean success = userLineService.eraseUserLineMetaData(fileReplace.getFileIdToBeReplaced());
							
							if (success) {
								boolean recordsInserted = userLineService.createUserLines(userlines, metaData);
								if (recordsInserted) {
									// if atleast one record is written then upload file.
									UploadedFile file = saveFile(filetobeuploaded, user, oldFile);
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
		}
		return new ModelAndView("redirect:/upload/files");
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
		uploadedfile.setActive(true);
		
		if (previousFile != null) {
			// replace option means previous file should be set as reference in
			// new file. Revision will be automatically updated
			// based on previous file revision
			uploadedfile.setPreviousFile(previousFile);					
		}

		user = uploadFileService.saveUserUploadedFile(user, uploadedfile);

		return uploadedfile;
	}
	
	@RequestMapping(value = "/files/delete", method = RequestMethod.GET)
	public @ResponseBody String deleteUploadedFile(@RequestParam("fileId") String fileId, Locale locale) throws Exception {

		if (fileId != null && !fileId.isEmpty()) {
			UploadedFile downloadFile = uploadFileService.getFile(Integer.parseInt(fileId));
			User user = userSession.getUser();
			
			if (downloadFile != null && downloadFile.getUploader().equals(user) && !downloadFile.isActive()) {
				boolean isFileToDeleteLatest = true;
				UploadedFile fileToMakeActive = null;
				
				// Before deleting file entry update previous file properly
				// get uploaded file where the deleted file was used as previous
				// file. update previous entry reference with deleted file
				// previous reference				
				for (UploadedFile upfile : user.getUploadedFiles()) {
					if (upfile.getPreviousFile() != null && upfile.getPreviousFile().equals(downloadFile)) {
						// this means deleted file has been used as previous
						// file in some other file
						isFileToDeleteLatest = false;
						logger.debug("Updating references");
						upfile.setPreviousFile(downloadFile.getPreviousFile());
						uploadFileService.updateUserUploadedFile(user, upfile);
						fileToMakeActive = upfile;
					}
				}
				
				// now delete file safely
				try {
					boolean success = userLineService.eraseUserLineMetaData(downloadFile.getId());
					// first remove all mappings
					uploadFileService.removeUserUploadedFile(user, downloadFile);
					// actual row deleted
					uploadFileService.removeUserUploadedFile(user, downloadFile);
					userSession.setUserInSession(userService.fetchUser(user.getUserId()));
					if (success) {
						EnwidaUtils.removeTemporaryFile(downloadFile.getActualFile());
						EnwidaUtils.removeTemporaryFile(downloadFile.getManifestFile());
					}
				} catch (Exception e) {
					logger.error("Unable to delete file :'"	+ downloadFile.getFilePath()
									+ "' .Please delete file manually.", e);
				}
				
				// This case will never be executed as File to be Deleted is always not active 
				if (isFileToDeleteLatest) {
					// Switch back to last revision
					// fileToMakeActive should be made active here
				}
			}
		}
		return "SUCCESS";
	}
	
	@RequestMapping(value = "/files/set/delete", method = RequestMethod.GET)
	public @ResponseBody String deleteUploadedFileSet(@RequestParam("fileId") String fileId, Locale locale) {

		if (fileId != null && !fileId.isEmpty()) {
			int fileid = Integer.parseInt(fileId);
			UploadedFile file = uploadFileService.getFile(fileid);
			//TODO: Complete the Implementation
			
		}
		return "SUCCESS";
	}

	@RequestMapping(value = "/files/{fileId}", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource downloadFile(@PathVariable("fileId") String fileId, HttpServletResponse response) {
		
		if (fileId != null && !fileId.isEmpty()) {
			int fileid = Integer.parseInt(fileId);
			
			User user = userSession.getUser();
			UploadedFile downloadFile = uploadFileService.getFile(fileid);				
			if (downloadFile != null && downloadFile.getUploader().equals(user)) {
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
			}
		}
		return null;
	}
	
	@RequestMapping(value = "/files/{fileId}/action/{action}", method = RequestMethod.GET)
	public ModelAndView makeFileActive(@PathVariable("fileId") String fileId, @PathVariable("action") String action, HttpServletResponse response) {
		
		if (fileId != null && !fileId.isEmpty() && action!= null && !action.isEmpty()) {
			if (action.equals("ma")) {				
				try {
					int fileid = Integer.parseInt(fileId);
					User user = userSession.getUser();
					uploadFileService.makeFileActive(fileid, user, fileValidator);
					userSession.setUserInSession(user);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return new ModelAndView("redirect:/upload/files");
	}
	
	@RequestMapping(value = "/files/revisions", method = RequestMethod.GET)
	public @ResponseBody List<UserUploadedFile> getFileRevisions(@RequestParam("fileId") String fileId, Locale locale) {
		
		List<UserUploadedFile> revisions = new ArrayList<UserUploadedFile>();
		if (fileId != null && !fileId.isEmpty()) {
			int fileid = Integer.parseInt(fileId);
			List<UploadedFile> file = uploadFileService.getFileSetByUniqueIdentifier(fileid);	
			for (UploadedFile uploadedFile : file) {
				revisions.add(new UserUploadedFile(uploadedFile));
			}
			/*if (file != null) {
				revisions.add(new UserUploadedFile(file));
				while (file.getPreviousFile() != null) {
					file = file.getPreviousFile();
					revisions.add(new UserUploadedFile(file));
				}
			}*/
		}
		return revisions;
	}
}
