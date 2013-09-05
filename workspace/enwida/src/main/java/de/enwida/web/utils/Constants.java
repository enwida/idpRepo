package de.enwida.web.utils;

public class Constants {
	public static final String EMPTY_STRING = "";
	
	// Validation 
	public static final String EMAIL_REGULAR_EXPRESSION = "([_A-Za-z0-9-]+)(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})";
	public static final String WEBSITE_REGUALAR_EXPRESSION = "^(http(?:s)?\\:\\/\\/[a-zA-Z0-9\\-]+(?:\\.[a-zA-Z0-9\\-]+)*\\.[a-zA-Z]{2,6}(?:\\/?|(?:\\/[\\w\\-]+)*)(?:\\/?|\\/\\w+\\.[a-zA-Z]{2,4}(?:\\?[\\w]+\\=[\\w\\-]+)?)?(?:\\&[\\w]+\\=[\\w\\-]+)*)$";
	// User Roles
	public static final String ANONYMOUS_ROLE = "anonymous_role";
	public static final String ANONYMOUS_GROUP = "anonymous";
	public static final String ANONYMOUS_USER = "anonymous";
	
	// Rights
	
	// DB constants
	public static final String ENWIDA_USERS_JPA_CONTEXT = "enwida-users-jpa";
	public static final String USERS_SCHEMA_NAME = "users";

	// Table with schema names
	public static final String NAVIGATION_DEFAULTS_TABLE_NAME = "navigation_defaults";
	public static final String NAVIGATION_DEFAULTS_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;
	public static final String USER_NAVIGATION_SETTINGS_TABLE_NAME = "user_navigation_settings";
	public static final String USER_NAVIGATION_SETTINGS_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;
	public static final String USER_NAVIGATION_DISABLED_LINES_TABLE_NAME = "navigation_defaults_disabled_lines";
	public static final String USER_NAVIGATION_DISABLED_LINES_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;
	public static final String UPLOADED_FILE_TABLE_NAME = "uploaded_files";
	public static final String UPLOADED_FILE_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;
	public static final String UPLOADED_FILE_SEQUENCE_NAME = "uploaded_file_sequence";
	public static final String UPLOADED_FILE_SEQUENCE_SCHEMA_NAME = USERS_SCHEMA_NAME;
	public static final String USER_LINES_TABLE_NAME = "user_lines";
	public static final String USER_LINES_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;
	public static final String USER_LINES_METADATA_TABLE_NAME = "user_lines_metadata";
	public static final String USER_LINES_METADATA_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;

	public static final String USER_TABLE_NAME = "users";
	public static final String USER_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;
	public static final String GROUP_TABLE_NAME = "groups";
	public static final String GROUP_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;
    public static final String ROLE_TABLE_NAME = "roles";
    public static final String ROLE_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;
	public static final String RIGHT_TABLE_NAME = "rights";
    public static final String RIGHT_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;
    public static final String USER_GROUP_TABLE_NAME = "user_group";
    public static final String USER_GROUP_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;
    public static final String GROUP_ROLE_TABLE_NAME = "group_role";
    public static final String GROUP_ROLE_TABLE_SCHEMA_NAME = USERS_SCHEMA_NAME;


	// Cookie Constants
	public static final String ENWIDA_CHART_COOKIE_ANONYMOUS = "enwida_chart_cookie_anonymous";
	public static final String ENWIDA_CHART_COOKIE_USER = "enwida_chart_cookie_user";
	public static final int ENWIDA_CHART_COOKIE_EXPIRY_TIME = -1;
	public static final String ENCRYPTION_KEY = "enwidaChartCookieDataEncryptionKey";
	
	// URLs
	public static final String ACTIVATION_URL = "http://localhost:8080/enwida/user/activateuser?";
	
	public static final String DISPLAY_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss aaa";
	public static final String UPLOAD_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSSZ";
	public static final String UPLOAD_LINES_KEY = "uploadedlines";
	public static final String UPLOAD_LINES_METADATA_KEY = "uploadedlinesmetadata";
	public static final String GENERAL_ERROR_MESSAGE = "errormesessage";
	public static final String COMMENT_VALUE_SEPARATOR = ":";
	public static final String COMMENT_START_SYMBOL = "#";
	public static final String DATA_SEPARATOR = ";";

}
