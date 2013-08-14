package de.enwida.web.dao.implementation;

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
	public long save(User user)
	{
        if(user==null) return 0;
        
		User exisinguser = fetchByName(user.getUserName());
		try {
			if (exisinguser == null) {
				// create the user and refresh the user object
				create(user);
			} else {
			    user.setUserID(exisinguser.getUserId());
				user = update(user);
			}
		} catch (Exception e) {
			logger.error("Error saving user : " + user.getUserName(), e);
		}

		return user.getUserId();
	}
  
    @Override
    public boolean checkUserActivationId(String username, String activationCode) {
        User user=this.fetchByName(username);
        return user.getActivationKey().equals(activationCode);
    }
    

    @Override
	public void activateUser(String username) {
        User user=fetchByName(username);
        user.setEnabled(true);
        save(user);
    }

    @Override
	public void updateUser(User user) {
		save(user);
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
		User latestuser = fetchByName(user.getUserName());
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
}
