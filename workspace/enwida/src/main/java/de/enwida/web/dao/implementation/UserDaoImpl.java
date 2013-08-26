package de.enwida.web.dao.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.AbstractBaseDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.model.User;

@Repository
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
public class UserDaoImpl extends AbstractBaseDao<User> implements IUserDao {
	
    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);

 
    @Override
    public boolean checkUserActivationId(String username, String activationCode) {
        User user=this.fetchByName(username);
        return user.getActivationKey().equals(activationCode);
    }
    

    @Override
	public void activateUser(String username) {
        User user=fetchByName(username);
        user.setEnabled(true);
        update(user);
    }

    @Override
	public void enableDisableUser(long userID, boolean enabled) {
        User user=fetchById(userID);
		user.setEnabled(enabled);
        update(user);
    }
    
    @Override
    public boolean usernameAvailablility(String userName) {
        return this.fetchByName(userName)!=null;
    }

    @Override
	public int getUploadedFileVersion(UploadedFile uplaodedfile, User user) {
		int revision = 1;
		User latestuser = fetchByName(user.getUsername());
		Set<UploadedFile> uploadedFiles = latestuser.getUploadedFiles();
		for (UploadedFile file : uploadedFiles) {
			if (file.getDisplayFileName().equals(
					uplaodedfile.getDisplayFileName())) {
				revision += 1;
			}
		}
		return revision;
	}

	@Override
	public Long getNextSequence(String schema, String sequenceName) {
		return super.getNextSequenceNumber(schema, sequenceName);
	}

	@Override
	public List<UploadedFile> getActiveUploadedFiles(User user) {
		
		List<UploadedFile> activeUploadedFiles = new ArrayList<UploadedFile>();
			
		List<UploadedFile> userFiles = new ArrayList<UploadedFile>();
		userFiles.addAll(user.getUploadedFiles());
		for (UploadedFile uploadedFile : userFiles) {
			if (uploadedFile.isActive()) {
				activeUploadedFiles.add(uploadedFile);
			}
		}		
		
		/*Collections.sort(userFiles, new Comparator<UploadedFile>(){
            public int compare(UploadedFile f1, UploadedFile f2){
            	return f1.getRevision() < f2.getRevision() ? 1 : (f1.getRevision() > f2.getRevision() ? -1 : 0);
            }});*/
		
		return activeUploadedFiles;
	}
}
