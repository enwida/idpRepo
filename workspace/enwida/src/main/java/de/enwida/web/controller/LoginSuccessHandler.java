package de.enwida.web.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.ICookieSecurityService;
import de.enwida.web.service.interfaces.IUserService;
import de.enwida.web.utils.Constants;

public class LoginSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler implements
		ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
    private ICookieSecurityService cookieSecurityService;
    @Autowired
    private UserSessionManager userSession;
    @Autowired
    private IUserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException {

        String url = "";

        HttpSession session = request.getSession(false);
        if (session != null) {
            url = (String) request.getSession().getAttribute("url_prior_login");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //delete all cookies and update cookie
        String cookieString=cookieSecurityService.encryptJsonString(auth.getName(), Constants.ENCRYPTION_KEY);
        Cookie cookie=new Cookie("enwida.de",cookieString);
      
        Cookie[] cookies = request.getCookies();
        if(cookies!=null)
            for (int i = 0; i < cookies.length; i++) {
             cookies[i].setMaxAge(0);
        }

		// update user session value if not present
		setUserInSession(auth.getName());
            
        response.addCookie(cookie);

        if (url != null) {

            response.sendRedirect(url);

        } else {
            try {
                super.onAuthenticationSuccess(request, response, authentication);
            } catch (ServletException e) {
				logger.error(
						"Unable to perform default authentication success event : ",
						e);
            }
        }

        try {     
            User user=userService.getCurrentUser();
            java.util.Date utilDate = new java.util.Date(); 
            user.setLastLogin(new java.sql.Timestamp(utilDate.getTime()));
            userService.updateUser(user);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

	/**
	 * This method is executed when remember me authentication is successful
	 * without user intervention (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
		setUserInSession(event.getAuthentication().getName());

	}

	private void setUserInSession(String username) {
		// update user session value if not present
		if (userSession.getUser() == null) {
			// Fetch User details and save in session
			// System.out.println(userService.getUser(auth.getName()));
			try {
				userSession.setUserInSession(username);
			} catch (Exception e) {
				logger.error(
						"Unable to set user '" + username
						+ "' in session", e);
			}
		}
	}

}
