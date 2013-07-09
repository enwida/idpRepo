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
        Cookie[] cookies = request.getCookies();
        String cookieName="anonymousCookie";
        if (cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equalsIgnoreCase("enwida.de")){
                    cookieName=cookie.getValue();
                    break;
                }
            }
        }

        String logUserName=auth.getName();
        if(logUserName=="anonymousUser"){
            //Check if user already has a cookie
            if( auth!=null){
                logUserName="C_"+cookieName;
            }
        }
        String param=request.getQueryString();
        if(param==null) param="";
        if (!request.getRequestURL().toString().contains(".")){
            log(logUserName,request.getServletPath()+param,request.getRemoteAddr(),cookieName,request.getHeader("User-Agent"),request.getHeader("Referer"));
        }          
    }
    //Following information will be stored in log file
    // time, url,IP,Cookie,UA,Redirect
    public static void log(String userName,String url,String IP,String cookie,String UA,String redirectURL)
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

          logger.info("|"+url+"|"+IP+"|"+cookie+"|"+UA+"|"+redirectURL);               
          Logger.getRootLogger().removeAppender(fa);
    }
}
