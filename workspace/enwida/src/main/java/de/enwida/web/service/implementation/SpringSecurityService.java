package de.enwida.web.service.implementation;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.IUserService;

/**
 * This class loads all user details when the server starts
 * 
 * @author Jitin
 * 
 */
@TransactionConfiguration(transactionManager = "jpaTransactionManager", defaultRollback = true)
@Transactional(rollbackFor = Exception.class)
@Service("springSecurityService")
public class SpringSecurityService implements UserDetailsService {
	/**
	 * Log4j static class
	 */
	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IUserService userService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
	    //make sure User is null
		User matchingUser = null;
		try {
	        //check if we can verify user with first and lastname or email
	        if (matchingUser == null) {
	            matchingUser=userService.fetchUserByUserNameOrEmail(username);
	        }
	        //check if we can verify user with username
	        if (matchingUser == null){
	            matchingUser = userService.fetchUser(username);
	        }
        } catch (Exception e) {
            logger.error("Unable to fetch user for name : " + username, e);
        }

		//we couldnt find the user
        if (matchingUser == null)
            throw new UsernameNotFoundException("Wrong username or password");
        
		return matchingUser;
	}

}
