package se.cambio.loganalyzer.dao.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import se.cambio.loganalyzer.main.App;

/**
 * #com.microsoft.sqlserver.jdbc.SQLServerDriver old one
 * 
 *
 * DBNAMEE:TRT_ERRORLOG
 * SERVER CSLK-R81-REGDB
 * URL jdbc\:sqlserver\://          old one
 * jdbc:microsoft:sqlserver://      new one
 * 1433 port
 * 
 * 
 * MYSQL Connection 
 * jdbc\:mysql\://      server url
 * 3306                 port
 * localhost            server
 * loganalyser          db name
 * com.mysql.jdbc.Driver driver name
 * 
 * @author SRatnavel
 *
 */
public class DatabaseConnector
{
  private static DatabaseConnector instance;

  private static String JDBC_DRIVER;// = ConfigManager.getPorpsValue(10);//"com.mysql.jdbc.Driver"; //10

  private static String DB_NAME;// = ConfigManager.getPorpsValue(2);

  private static String DB_TYPE = "MYSQL";

  private static String DB_URL;/* = ConfigManager.getPorpsValue(9) + ConfigManager.getPorpsValue(11) + ":"
                               + ConfigManager.getPorpsValue(12) + "/" + "" + DB_NAME; *///";integratedSecurity=false";//authenticationScheme=NativeAuthentication;";//9

  //  Database credentials
  static String USER_NAME;/* = ConfigManager.getPorpsValue(3);*/

  static String PASSWORD;// = ConfigManager.getPorpsValue(4);

  static Connection conn = null;

  static Logger logger = Logger.getLogger(DatabaseConnector.class);

  private DatabaseConnector()
  {

    JDBC_DRIVER = App.configManager.getPorpsValue(10);
    DB_NAME = App.configManager.getPorpsValue(2);

    DB_TYPE = App.configManager.getPorpsValue(15);
    DB_URL = getDBURL(DB_TYPE);
    USER_NAME = App.configManager.getPorpsValue(3);
    PASSWORD = App.configManager.getPorpsValue(4);

  }

  public static DatabaseConnector getInstance()
  {
    if (instance == null)
    {
      synchronized (DatabaseConnector.class)
      {
        if (instance == null)
          instance = new DatabaseConnector();
      }
    }
    return instance;
  }

  private String getDBURL(String db_type)
  {
    String url = "";
    if (db_type.equalsIgnoreCase("mysql"))
    {
      url = App.configManager.getPorpsValue(9) + App.configManager.getPorpsValue(11) + ":"
          + App.configManager.getPorpsValue(12) + "/" + "" + DB_NAME;
    }
    else if (db_type.equalsIgnoreCase("sql"))
    {
      url = App.configManager.getPorpsValue(9) + App.configManager.getPorpsValue(11) + ":"
          + App.configManager.getPorpsValue(12) + ";DatabaseName=" + DB_NAME + ";";
    }
    return url;
  }

  public Connection getConnection()
  {
    try
    {
      Class.forName(JDBC_DRIVER);
      if (conn == null)
      {
        logger.info("Connecting to " + DB_NAME + " .....");
        conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
        logger.info("Connected to " + DB_NAME + " database successfully");
      }
      return conn;
    }
    catch (SQLException se)
    {
      logger.error("SQL Connection Error: " + se.getMessage() + " " + se.toString());
      se.printStackTrace();
    }
    catch (Exception e)
    {
      logger.error("SQL Exception: " + e.getMessage() + " " + e.toString());
      e.printStackTrace();
    }
    return conn;
  }
}
