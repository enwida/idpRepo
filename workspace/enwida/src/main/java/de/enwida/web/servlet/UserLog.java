package de.enwida.web.servlet;

import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import de.enwida.web.utils.AESencrp;

/**
 * logs the user web requests
 * @author root
 *
 */
public class UserLog extends HandlerInterceptorAdapter{
    static Logger logger = Logger.getLogger(UserLog.class);   
    
    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();    
        String userID="";
        boolean loggedin=false;
        if( auth!=null){
            //check if user has enwida cookie
            Cookie[] cookies = request.getCookies();
            if (cookies!=null){
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equalsIgnoreCase("enwida.de")){
                        userID=cookie.getValue();
                        break;
                    }
                }
            }
            //check if we could get userID from cookie, if not create new cookie with random id
            if(userID.equalsIgnoreCase("")){
                SecureRandom random = new SecureRandom();
                Cookie cookie=new Cookie("enwida.de","not"+new BigInteger(130, random).toString(32));
                userID=cookie.getValue();
                response.addCookie(cookie);
            }
            
            //if user name is anonymous
            if (auth.getName().equalsIgnoreCase("anonymousUser")){
                if (!userID.contains("not")){
                    try{
                        userID=AESencrp.decrypt(userID);
                    }catch(Exception e){
                        if(cookies!=null)
                        for (int i = 0; i < cookies.length; i++) {
                         cookies[i].setMaxAge(0);
                        }
                    }
                }
                else{
                    loggedin=false;
                    userID="C_"+userID;
                }
            }
            else{
                loggedin=true;
                userID=auth.getName();
            }
        }

        String param=request.getQueryString();
        if(param==null) param="";
        if (!request.getRequestURL().toString().contains(".")){
            log(userID,request.getServletPath()+param,request.getRemoteAddr(),loggedin,request.getHeader("User-Agent"),request.getHeader("Referer"));
        }          
    }
    //Following information will be stored in log file
    // time, url,IP,loggedIn,UA,Redirect
    public static void log(String userName,String url,String IP,boolean loggedin,String UA,String redirectURL)
    {
        if(Logger.getRootLogger().getAppender(userName)==null){ //create FileAppender if necessary
            RollingFileAppender fa = new RollingFileAppender();
            fa.setName(userName);
            fa.setFile(System.getenv("ENWIDA_HOME")+"/log/"+userName+".log");
            fa.setLayout(new PatternLayout("%n%d{ISO8601} %m"));
            fa.setThreshold(Level.INFO);
            fa.setMaxFileSize("1MB");
            fa.activateOptions();
            Logger.getRootLogger().addAppender(fa);
          }
          FileAppender fa = (FileAppender) Logger.getRootLogger().getAppender(userName);

          logger.info("|"+url+"|"+IP+"|"+loggedin+"|"+UA+"|"+redirectURL);               
          Logger.getRootLogger().removeAppender(fa);
    }
}
