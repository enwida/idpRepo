package de.enwida.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Service;


public class TestLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint
{
    @Override
    protected String determineUrlToUseForThisRequest(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) {
        // TODO Auto-generated method stub
        System.out.println("test");
        System.out.println("request: "+request.getParameter("origin"));
        return super.determineUrlToUseForThisRequest(request, response, exception);
    }
    
    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        System.out.println("test");
        // TODO Auto-generated method stub
        super.commence(request, response, authException);
    }
}
