package se.cambio.loganalyzer.config;

public class Configuration
{

  public static final String CONFIG_PROPERTY_FILE_KEYS[] = { "LAST_ANALYSED_TIME_STAMP", "LAST_FILE_TIME_STAMP",
      "DB_NAME", "DB_USER", "DB_PASSWORD", "MAIL_PROGRAM_SCHEDULED", "MAIL_SENDING_TIME", "MAIL_RECEIPEINTS",
      "MAIL_HISTORICAL_SCHEDULED_DATE", "DB_CONNECTION_URL", "DB_DRIVER_NAME", "DB_SERVER", "DB_SERVER_PORT",
      "INSTANTS_MAIL_ACTIVE", "DAILY_MAIL_ACTIVE","DB_TYPE","MAIL_FROM","ERROR_FILE_NAME_EXTENSION" };

  public static final String CONFIGURATION_PROPERTY_FILE_DIR = System.getProperty("user.dir");

  public static final String CONFIGURATION_FILE_NAME = "program_config.properties";
  
  public static final String INTERNAL_CONFIGURATION_FILE_NAME = "config.properties";

}
