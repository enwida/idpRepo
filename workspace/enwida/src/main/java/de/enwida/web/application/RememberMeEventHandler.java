package de.enwida.web.application;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;

import de.enwida.web.controller.UserSessionManager;

/**
 * Remember me successful authentication triggers event defined in this class
 * 
 * @author Jitin
 * 
 */
public class RememberMeEventHandler implements
		ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private UserSessionManager userSession;

	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		String username = event.getAuthentication().getName();
		// update user session value if not present
		if (userSession.getUser() == null) {
			// Fetch User details and save in session
			// System.out.println(userService.getUser(auth.getName()));
			try {
				userSession.setUserInSession(username);
			} catch (Exception e) {
				logger.error("Remember me: unable to set user '" + username
						+ "' in session", e);
			}
		}

	}

}
