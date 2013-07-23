package de.enwida.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import de.enwida.web.service.interfaces.ICookieSecurityService;
import de.enwida.web.utils.Constants;

public class LoginSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
    private ICookieSecurityService cookieSecurityService;
	@Autowired
	private UserSessionManager userSession;

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
		if (userSession.getUser() == null) {
			// Fetch User details and save in session
			// System.out.println(userService.getUser(auth.getName()));
			userSession.setUserInSession(auth.getName());
		}
            
        response.addCookie(cookie);

        if (url != null) {

            response.sendRedirect(url);

        } else {
            try {
                super.onAuthenticationSuccess(request, response, authentication);
            } catch (ServletException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

	// public ICookieSecurityService getCookieSecurityService() {
	// return cookieSecurityService;
	// }
	//
	// public void setCookieSecurityService(
	// ICookieSecurityService cookieSecurityService) {
	// this.cookieSecurityService = cookieSecurityService;
	// }
	//
	// public UserSessionManager getUserSession() {
	// return userSession;
	// }
	//
	// public void setUserSession(UserSessionManager userSession) {
	// this.userSession = userSession;
	// }

}
