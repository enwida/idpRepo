package de.enwida.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import de.enwida.web.service.implementation.CookieSecurityService;
import de.enwida.web.service.interfaces.UserService;
import de.enwida.web.utils.Constants;


public class LoginSuccessHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;
    
  //  @Autowired
    private CookieSecurityService cookieSecurityService=new CookieSecurityService();

    private static org.apache.log4j.Logger log = Logger
            .getLogger(LoginSuccessHandler.class);

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
        Cookie cookie=new Cookie("enwida.de",cookieSecurityService.encryptJsonString(auth.getName(), Constants.ENCRYPTION_KEY));
      
        Cookie[] cookies = request.getCookies();
        if(cookies!=null)
            for (int i = 0; i < cookies.length; i++) {
             cookies[i].setMaxAge(0);
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

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public CookieSecurityService getCookieSecurityService() {
        return cookieSecurityService;
    }

    public void setCookieSecurityService(CookieSecurityService cookieSecurityService) {
        this.cookieSecurityService = cookieSecurityService;
    }
}
