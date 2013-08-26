package de.enwida.web.service.implementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.dao.interfaces.IFileDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUploadFileService;

@Service("fileUploadService")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public class UploadFileServiceImpl implements IUploadFileService {

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
	public void makeFileActive(int fileId, User user) {
		UploadedFile file = fileDao.getFile(fileId);
		//1. Get Active File of FileSet by FileSetUniqueIdentifier
		//2. Erase the Data of the Active File from Database
		//3. Insert the data of the Required Active File
		//4. Set the File Active
			
		if (file.getUploader().equals(user)) {
			List<UploadedFile> fileSet = fileDao.fetchByFileSetUniqueIdentifier(file.getFileSetUniqueIdentifier());
			for (UploadedFile uploadedFile : fileSet) {
				if (uploadedFile.getId() == file.getId()) {
					uploadedFile.setActive(true);
				} else {
					uploadedFile.setActive(false);
				}
				fileDao.update(uploadedFile);
			}
		}
		
		
		
	}
}