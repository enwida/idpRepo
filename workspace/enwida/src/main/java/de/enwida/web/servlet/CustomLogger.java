package de.enwida.web.servlet;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;



public class CustomLogger {

    public static void infoLogin(Logger logger,String message,String user,String IP,String cookie) {
        MDC.put("login", "login"); 
        MDC.put("IP", IP); 
        MDC.put("cookie", "cookie"); 
        MDC.put("user", "user");   
        logger.info(message);
        MDC.remove("login");
        MDC.remove("IP");
        MDC.remove("cookie");
        MDC.remove("user");
    }
}