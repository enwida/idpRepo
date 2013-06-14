package de.enwida.web.controller;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import de.enwida.web.model.User;
import de.enwida.web.service.interfaces.UserService;

public final class LoginSuccessHandler implements AuthenticationSuccessHandler
{
    @Autowired
    private UserService userService;
    
    
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String userName = authentication.getName();
        HttpSession session = request.getSession();
        User user = userService.getUser(userName);
        Date date = new Date();
        
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        user.setLastLogin(sdfDate.format(now));
    }
}
