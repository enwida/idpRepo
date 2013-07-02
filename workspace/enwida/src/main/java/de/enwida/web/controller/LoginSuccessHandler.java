package de.enwida.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Service;

import de.enwida.web.service.interfaces.UserService;

@Service("loginSuccessHandler")
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
    @Autowired
    private UserService userService;
    
    private static org.apache.log4j.Logger log = Logger.getLogger(LoginSuccessHandler.class);
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String url = "";

        HttpSession session = request.getSession(false);
        if (session != null) {
            url = (String) request.getSession().getAttribute("url_prior_login");
        }

        System.out.println("Redirect url: "+ url);

        if (url == "") {
            
            try {
                super.onAuthenticationSuccess(request, response, authentication);
            } catch (ServletException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            response.sendRedirect(url);
        }
    }
}
