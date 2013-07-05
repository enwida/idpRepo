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
import org.springframework.stereotype.Service;

import de.enwida.web.service.interfaces.UserService;
import de.enwida.web.servlet.UserLog;

@Service("loginSuccessHandler")
public class LoginSuccessHandler extends
        SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

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
        //logging once
        UserLog.log(auth.getName() , "|IP: "+request.getRemoteAddr()+" USER-AGENT: "+request.getHeader("User-Agent")+"|");
        UserLog.log(auth.getName() , "|Redirect URL:"+url+"|");
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
          //logging once
            UserLog.log(auth.getName() ,"|"+ "Cookie"+cookie.getValue()+"|");
        }
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
}
