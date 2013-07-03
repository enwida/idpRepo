package de.enwida.web.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

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
        if (auth!=null){
            String logUserName="";
            String name=logUserName=auth.getName();
            if(name=="anonymousUser"){
                logUserName=name;
                Cookie[] cookies = request.getCookies();
                //Check if user already has a cookie
                if( cookies!=null){
                    logUserName="C_"+name;
                    for (Cookie cookie : cookies) {
                        //logging once
                          UserLog.log(logUserName ,"|"+ "Cookie"+cookie.getValue()+"|");
                      }
                }
                UserLog.log(logUserName , "|IP: "+request.getRemoteAddr()+" USER-AGENT: "+request.getHeader("User-Agent")+"|");
            }
            String param=request.getQueryString();
            if(param==null) param="";
            if (!request.getRequestURL().toString().contains(".")){
                log(logUserName,"|            |"+request.getRequestURL()+param+"|");
            }
        }           
    }
    
    public static void log(String name,String message)
    {
        if(Logger.getRootLogger().getAppender(name)==null){ //create FileAppender if necessary
            RollingFileAppender fa = new RollingFileAppender();
            fa.setName(name);
            fa.setFile(System.getenv("ENWIDA_HOME")+"/log/"+name+".log");
            fa.setLayout(new PatternLayout("%d{ISO8601}: %m%n"));
            fa.setThreshold(Level.INFO);
            fa.setMaxFileSize("1MB");
            fa.activateOptions();
            Logger.getRootLogger().addAppender(fa);
          }
          FileAppender fa = (FileAppender) Logger.getRootLogger().getAppender(name);

          logger.info(message);               
          Logger.getRootLogger().removeAppender(fa);
    }
	
	public void infoLogin(Logger logger,String message,String IP,String cookie) {
        MDC.put("login", "login"); 
        MDC.put("IP", IP); 
        MDC.put("cookie", "cookie"); 
        logger.info(message);
        MDC.remove("login");
        MDC.remove("IP");
        MDC.remove("cookie");
        MDC.remove("user");
    }
}
