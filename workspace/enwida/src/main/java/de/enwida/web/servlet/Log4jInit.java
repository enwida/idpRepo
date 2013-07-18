package de.enwida.web.servlet;

import javax.servlet.http.HttpServlet;

import org.apache.log4j.PropertyConfigurator;

/**
 * This has been marked deprecated since log4j configuration is provided from
 * classpath folder at application level (not webapplication level). Required
 * sice need to enable Junit test case logging.
 * 
 * @author Jitin
 * 
 */
@Deprecated
public class Log4jInit extends HttpServlet {
 
    private static final long serialVersionUID = -30662830391078294L;
    
    public void init()
     {
         String prefix =  getServletContext().getRealPath("/");
         String file = getInitParameter("log4j-init-file");
		initialization(file, prefix);
     }

	public static void initialization(String file, String prefix) {
		// if the log4j-init-file context parameter is not set, then no point in
		// trying
		if (file != null) {
			PropertyConfigurator.configure(prefix + file);
			System.out.println("Log4J Logging started: " + prefix + file);

		} else {
			System.out.println("Log4J Is not configured for your Application: "
					+ prefix + file);
		}
	}
}
