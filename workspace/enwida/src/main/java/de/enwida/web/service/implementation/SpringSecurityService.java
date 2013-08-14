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
		User matchingUser = null;

		try {
			matchingUser = userService.getUser(username);
		} catch (Exception e) {
			logger.error("Unable to fetch user for name : " + username, e);
		}
		if (matchingUser == null) {
		    matchingUser=userService.getUserByFirstAndLastName(username);
		    if (matchingUser == null)
		        throw new UsernameNotFoundException("Wrong username or password");
		}
        //I dont know why but this trigger is required here.Otherwise roles are not fetched
        matchingUser.getAuthorities();
		return matchingUser;
	}

}
