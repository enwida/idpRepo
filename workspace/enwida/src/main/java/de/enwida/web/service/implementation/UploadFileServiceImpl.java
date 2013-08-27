package de.enwida.web.service.implementation;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import de.enwida.web.dao.interfaces.IFileDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.dao.interfaces.IUserLinesDao;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.db.model.UserLines;
import de.enwida.web.db.model.UserLinesMetaData;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUploadFileService;
import de.enwida.web.service.interfaces.IUserLinesService;
import de.enwida.web.utils.Constants;
import de.enwida.web.utils.EnwidaUtils;
import de.enwida.web.validator.FileValidator;

@Service("fileUploadService")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public class UploadFileServiceImpl implements IUploadFileService {


	@Autowired
	private IUserLinesService userLineService;
	
	/**
	 * File Data Access Object
	 */
    @Autowired
    private IFileDao fileDao;  
    
    /**
	 * User Data Access Object
	 */
    @Autowired
    private IUserDao userDao;
    
    /**
	 * User Lines Data Access Object
	 */
    @Autowired
	private IUserLinesDao userLinesDao;
 	    
	/**
	 * Caution: user should be persisted and in clean state! Dirty attributes
	 * might be applied (i.e. committed to database, eventually).
	 * 
	 * @return the updated and managed user and file object
	 * @throws Exception
	 */
	@Override
	public User saveUserUploadedFile(User user, UploadedFile file) {
		if (user.getUserId() == null) {
			throw new IllegalArgumentException("user object is not persisted");
		}
		// check for revision
		UploadedFile previousFile = file.getPreviousFile();
		if (previousFile != null) {
			int newrevision = previousFile.getRevision() + 1;
			file.setRevision(newrevision);
			file.setFileSetUniqueIdentifier(previousFile.getFileSetUniqueIdentifier());	
		} else {
			file.setRevision(1);
			file.setFileSetUniqueIdentifier(UUID.randomUUID().toString());
		}

		if (file.getId() > 0) {
			file = fileDao.update(file, true); // with flush			
		} else {
			fileDao.create(file, true); // with flush
			if (previousFile != null) {
				previousFile.setActive(false);
				previousFile = fileDao.update(previousFile, true); // with flush
			}
		}

		// Refresh the user in order to reflect the changes
		user = userDao.update(user);
		userDao.refresh(user);
		return user;
	}
    
	/**
	 * Caution: user should be persisted and in clean state! Dirty attributes
	 * might be applied (i.e. committed to database, eventually).
	 * 
	 * @return the updated and managed user and file object
	 * @throws Exception
	 */
	@Override
	public User updateUserUploadedFile(User user, UploadedFile file) {
		if (user.getUserId() == null) {
			throw new IllegalArgumentException("user object is not persisted");
		}
		if (file.getId() > 0) {
			file = fileDao.update(file, true); // with flush
		} else {
			fileDao.create(file, true); // with flush
		}

		// Refresh the user in order to reflect the changes
		user = userDao.update(user);
		userDao.refresh(user);
		return user;
	}

	/**
	 * deletes user uploaded file
	 */
	@Override
	public void removeUserUploadedFile(User user, UploadedFile file)
			throws Exception {
		if (user.getUserId() == null) {
			throw new IllegalArgumentException("user object is not persisted");
		}
		if (file.getId() == 0) {
			throw new IllegalArgumentException("user object is not persisted");
		}
		if (file.getId() > 0) {
			file.setUploader(null);
			file.setMetaData(null);
			file.setPreviousFile(null);
			fileDao.delete(file); // with flush
		}

		// Refresh the user in order to reflect the changes
		// user = userDao.update(user);
		// userDao.refresh(user);
	}
	
	@Override
    public UploadedFile getFile(int fileId) {
        return fileDao.getFile(fileId);
    }

    @Override
    public UploadedFile getFileByFilePath(String filePath) {
        return fileDao.getFileByFilePath(filePath);
    }

    @Override
    public int getUploadedFileVersion(UploadedFile file, User user) {
        return userDao.getUploadedFileVersion(file, user);
    }
    
    @Override
	public List<UploadedFile> getUploadedFiles(User user) {
		return userDao.getActiveUploadedFiles(user);
	}

	@Override
	public void makeFileActive(int fileId, User user, FileValidator fileValidator) {
		
		UploadedFile fileToMakeActive = fileDao.getFile(fileId);		
		if (fileToMakeActive.getUploader().equals(user)) {
			
			//1. Get Active File of FileSet by FileSetUniqueIdentifier
			UploadedFile fileAlreadyActive = fileDao.fetchActiveFileByFileSetUniqueIdentifier(fileToMakeActive.getFileSetUniqueIdentifier());
			
			//2. Erase the Data of the Active File from Database
			UserLinesMetaData metadata = fileAlreadyActive.getMetaData();
			for (UserLines line : metadata.getUserLines()) {
				userLinesDao.delete(line, true);
			}
			metadata.setUserLines(null);
			userLineService.updateUserLineMetaData(metadata);
			
			//3. Insert the data of the Required Active File
			BindingResult results = EnwidaUtils.validateFile(new File(fileToMakeActive.getFilePath()), fileValidator);
			ObjectError status = results.getGlobalError();			
			if (status.getCode().equalsIgnoreCase("file.upload.parse.success")) {
				Map<String, Object> parsedData = (Map<String, Object>) status.getArguments()[0];
				List<UserLines> userlines = (List<UserLines>) parsedData.get(Constants.UPLOAD_LINES_KEY);
				UserLinesMetaData metaData = fileToMakeActive.getMetaData();
				userLineService.createUserLines(userlines, metaData);			
			} else if (status.getCode().equalsIgnoreCase("file.upload.parse.error")) {
				//TODO: FileUpload Fail: Parsing Error
			}
			
			//4. Set the File Active
			fileAlreadyActive.setActive(false);
			fileDao.update(fileAlreadyActive, true);
			fileToMakeActive.setActive(true);
			fileDao.update(fileToMakeActive, true);
		}	
	}
}