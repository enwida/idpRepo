package de.enwida.web.service.implementation;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.controller.AdminController;
import de.enwida.web.dao.interfaces.IFileDao;
import de.enwida.web.dao.interfaces.IGroupDao;
import de.enwida.web.dao.interfaces.IRightDao;
import de.enwida.web.dao.interfaces.IRoleDao;
import de.enwida.web.dao.interfaces.IUserDao;
import de.enwida.web.db.model.UploadedFile;
import de.enwida.web.model.Group;
import de.enwida.web.model.Role;
import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.EnwidaUtils;

@Service("userService")
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional
public class UserServiceImpl implements IUserService {

    /**
     * User Data Access Object
     */
    @Autowired
    private IUserDao userDao;
    /**
     * Group Data Access Object
     */
    @Autowired
    private IGroupDao groupDao;
    /**
     * Role Data Access Object
     */   
    @Autowired
    private IRoleDao roleDao;
    /**
     * Rights Data Access Object
     */
    @Autowired
    private IRightDao rightDao;
    
    @Autowired
    private IFileDao fileDao;
    
    /**
     * Mailing Service to send activation link or password
     */
    @Autowired
    private MailServiceImpl mailService;
    /**
     * Log4j static class
     */
    private static org.apache.log4j.Logger logger = Logger.getLogger(AdminController.class);
    /**
     * Gets the User from UserID
     * @throws Exception 
     */
    @Override
    public User getUser(Long id) {
        return userDao.fetchById(id);
    }

    /**
     * Gets all the user from database
     * @throws Exception 
     */
    @Override
    public List<User> getUsers() throws Exception {
        return userDao.fetchAll();
    }

    /**
     * Saves user into Database
     * @throws Exception 
     */
    @Transactional
    @Override
    public boolean saveUser(User user, String activationHost) throws Exception 
    {
        
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        user.setJoiningDate(date);
        user.setEnabled(false);
        
        // Generating activation Id for User
        EnwidaUtils activationIdGenerator = new EnwidaUtils();
        user.setActivationKey(activationIdGenerator.getActivationId());
        
        // Saving user in the user table
        long userId;
        try {
            userId = userDao.save(user);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return false;
        }
                
        if(userId != -1)
        {           
            Group group = this.getGroupByCompanyName(user.getCompanyName());
            
            if(group != null && group.isAutoPass())
            {
                Group newGroup = groupDao.fetchById(group.getGroupID());
                this.assignUserToGroup(userId, newGroup.getGroupID());

            }
            else
            {
                // saving in default group (Anonymous)
				Group anonymousGroup = groupDao.fetchByName("Anonymous");
                if(anonymousGroup == null)
                {
                    anonymousGroup = new Group();
					anonymousGroup.setGroupName("Anonymous");
                    anonymousGroup.setAutoPass(true);
                    anonymousGroup = groupDao.addGroup(anonymousGroup);
                }
                 //  userDao.assignUserToGroup(userId, anonymousGroup.getGroupID());
            }
            
            try 
            {
                mailService.SendEmail(user.getUserName(), "Activation Link", activationHost + "activateuser.html?username=" + user.getUserName() + "&actId=" + user.getActivationKey());
            }
            catch (Exception e) {
                logger.error(e.getMessage());
                return false;
            }
            
            return true;
        }
        else
        {
            return false;
        }        
    }

    /**
     * Gets user Password from the mail
     */
    @Override
    public String getPassword(String email)throws Exception {
        return userDao.fetchByName(email).getPassword();
    }
    /**
     * Get all user group
     */
    @Override
	public Set<Group> getUserGroups(long userID) throws Exception {
        return userDao.fetchById(userID).getGroups();
    }

    /**
     * Gets all the groups
     */
    @Override
    public List<Group> getAllGroups() {
        return groupDao.fetchAll();
    }

    /**
     * Adds new group
     */
    @Override
    public Group addGroup(Group newGroup) {
        newGroup.setAutoPass(false);
        return groupDao.addGroup(newGroup);
    }

    /**
     * Adds new role to the DB
     */
    @Override
    public void addRole(Role role)throws Exception  {
        roleDao.addRole(role);
    }
    /**
     * Gets all Roles
     */
    @Override
    public List<Role> getAllRoles()throws Exception  {
        return roleDao.fetchAll();
    }
    
    /**
     * Updates the user
     */
    @Override
    public void updateUser(User user) throws Exception {
        userDao.updateUser(user);
    }
    /**
     * Gets the user based on userName
     */
    @Override
    public User getUser(String userName)  {
        return userDao.fetchByName(userName);
    }
    /**
     * Resets user Password and send an email link
     */
    @Override
    public void resetPassword(long userID)throws Exception  {
        SecureRandom random = new SecureRandom();
        String newPassword=new BigInteger(130, random).toString(32);
        User user=userDao.fetchById(userID);
        userDao.updateUser(user);
        try {
            mailService.SendEmail(user.getUserName(),"New Password","Your new Password:"+newPassword);
        } catch (Exception e) {
            throw new Exception("Invalid Email.Please contact info@enwida.de");
        }       
        user.setPassword(newPassword);
    }
    /**
     * Deletes the user
     */
    @Override
    public void deleteUser(User user) throws Exception {
        userDao.deleteUser(user);
    }
    
    @Override
    public void assignUserToGroup(User user, Group group) {
    	final List<Group> groups = new ArrayList<>(user.getGroups());
    	groups.add(group);
    	user.setGroups(groups);
    	userDao.update(user);
    	userDao.refresh(user);
    	groupDao.refresh(group);
    }
    /**
     * Assign users into group
     */
    @Override
    public void assignUserToGroup(long userID, long groupID){
        final User user = userDao.fetchById(userID);
        final Group group = groupDao.fetchById(groupID);
        assignUserToGroup(user, group);
    }
    
    @Override
    public void revokeUserFromGroup(User user, Group group) {
    	final List<Group> groups = new ArrayList<>(user.getGroups());
    	final Iterator<Group> iter = groups.iterator();

    	while (iter.hasNext()) {
    		if (iter.next().getGroupID() == group.getGroupID()) {
    			iter.remove();
    			break;
    		}
    	}
    	user.setGroups(groups);
    	userDao.update(user);
    	userDao.refresh(user);
    	groupDao.refresh(group);
    }
    
    /**
     * deAssign users into group
     */
    @Override
    public void revokeUserFromGroup(long userID, long groupID) {
        User user=userDao.fetchById(userID);
        Group group=groupDao.fetchById(groupID);
        if (group.getAssignedUsers().contains(user)){
            group.getAssignedUsers().remove(user);
        }
        if(group!=null  || user!=null)
            groupDao.save(group);
    }
    /**
     * Gets all groups with users attached
     */
    @Override
    public List<Group> getAllGroupsWithUsers() throws Exception {
        List<Group> groups = groupDao.fetchAll();
        return groups;
    }

    /**
     * Assigns role to group
     */
    @Override
    public void assignRoleToGroup(long roleID, long groupID) {
        Role role=roleDao.fetchById(roleID);
        Group group=groupDao.fetchById(groupID);
        if (!group.getAssignedRoles().contains(role)){
            group.getAssignedRoles().add(role);
        }
        if(role!=null  || group!=null)
            groupDao.save(group);
    }

    @Override
    public void deassignRoleToGroup(long roleID, long groupID){
        Role role=roleDao.fetchById(roleID);
        Group group=groupDao.fetchById(groupID);
        if (group.getAssignedRoles().contains(role)){
            group.getAssignedRoles().remove(role);
        }
        if(role!=null  || group!=null)
            groupDao.save(group);
    }

    @Override
    public List<Role> getAllRolesWithGroups()throws Exception  {
        return roleDao.fetchAll();
    }

    @Override
	public List<User> getAllUsers() throws Exception {
        return userDao.fetchAll();
    }
    /**
     * Enables or Disables the user
     */
    @Override
    public void enableDisableUser(int userID, boolean enabled)throws Exception  {
        userDao.enableDisableUser(userID,enabled);
    }
    /**
     * Removes the group
     */
    @Override
    public void removeGroup(int groupID) throws Exception {
        groupDao.removeGroup(groupID);
    }
    /**
     * Checks usernameAvailability
     */
    @Override
    public boolean userNameAvailability(String username) throws Exception {
        return userDao.usernameAvailablility(username);
    }
    /**
     * Enables or disables the aspect based on rightID
     */
    @Override
    public void enableDisableAspect(int rightID, boolean enabled)throws Exception  {
        rightDao.enableDisableAspect(rightID,enabled);
    }
    /**
     * Activates the user
     */
    @Override
    public boolean activateUser(String username, String activationCode) throws Exception 
    {
        if(userDao.checkUserActivationId(username, activationCode))
        {
            userDao.activateUser(username);
            return true;
        }
        return false;
    }
    
    @Override
    public Long getNextSequence(String schema, String sequenceName) {
        Long value = null;
        try {
            value = userDao.getNextSequence(schema, sequenceName);
        } catch (Exception e) {
            logger.error("Do nothing");
        }
        return value;
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
    
    
    /**
     * Gets the current User
     */
    @Override
    public User getCurrentUser()throws Exception  {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user=this.getUser(userName);
        //If user is not found return anonymous user;
        if (user==null){
            user=new User();
            user.setUserName("anonymous");
        }
        return user;
    }

    /**
     * Saves the user
     * @throws Exception 
     */
    @Override
    public boolean saveUser(User user) throws Exception {
        return saveUser(user,null);
    }
    
    @Override
    public Group getGroupByCompanyName(final String companyName)
    {
        for (Group group : groupDao.fetchAll()) {
            for (User user : group.getAssignedUsers()) {
                if(user.getCompanyName()==companyName)
                    return group;
            }
        }
        return null;
    }

    @Override
    public User getUserByFirstAndLastName(String username) {
        for (User user : userDao.fetchAll()) {
            if(user.getFirstName()+" "+user.getLastName()==username){
                return user;
            }
        }
        return null;
    }
}
