package de.enwida.web.utils;

public class Constants {
	public static final String EMPTY_STRING = "";
	
	// Validation 
	public static final String EMAIL_REGULAR_EXPRESSION = "([_A-Za-z0-9-]+)(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})";
	public static final String WEBSITE_REGUALAR_EXPRESSION = "^(http(?:s)?\\:\\/\\/[a-zA-Z0-9\\-]+(?:\\.[a-zA-Z0-9\\-]+)*\\.[a-zA-Z]{2,6}(?:\\/?|(?:\\/[\\w\\-]+)*)(?:\\/?|\\/\\w+\\.[a-zA-Z]{2,4}(?:\\?[\\w]+\\=[\\w\\-]+)?)?(?:\\&[\\w]+\\=[\\w\\-]+)*)$";
	// User Roles
	public static final String ANONYMOUS_ROLE = "anonymous_role";
	
	// Rights
	
	// DB constants

	// Cookie Constants
	public static final String ENWIDA_CHART_COOKIE_ANONYMOUS = "enwida_chart_cookie_anonymous";
	public static final String ENWIDA_CHART_COOKIE_USER = "enwida_chart_cookie_user";
	public static final int ENWIDA_CHART_COOKIE_EXPIRY_TIME = -1;
	
	// URLs
	public static final String ACTIVATION_URL = "http://localhost:8080/enwida/user/activateUser?";
	
}
