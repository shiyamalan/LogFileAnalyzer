package se.cambio.loganalyzer.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import se.cambio.loganalyzer.common.DataManagement;
import se.cambio.loganalyzer.common.TaskHandler;
import se.cambio.loganalyzer.config.ConfigManager;
import se.cambio.loganalyzer.config.Configuration;
import se.cambio.loganalyzer.dao.config.Table;
import se.cambio.loganalyzer.dao.config.TableInitialization;
import se.cambio.loganalyzer.dao.entities.Entity;
import se.cambio.loganalyzer.dao.entities.Environment;
import se.cambio.loganalyzer.dao.entities.Issue;
import se.cambio.loganalyzer.dao.entities.LogMonitor;
import se.cambio.loganalyzer.dao.service.FindingService;
import se.cambio.loganalyzer.dao.service.InsertingService;
import se.cambio.loganalyzer.paths.traverser.PathTraverser;

public class DataInitializer
{
  public static String TABLE_NAMES[] = { "log_location", "environments", "issues", "skipmessages", "log_history",
      "mail_users" };

  public static List<LogMonitor> logs = null;

  public static List<Issue> issues = null;

  public static List<Environment> environments_list = null;

  static PathTraverser traverser = null;

  static List<Entity> log_monitor_list = null;

  private static Logger logger = Logger.getLogger(DataInitializer.class);
  static
  {
    App.configManager = new ConfigManager(Configuration.CONFIGURATION_PROPERTY_FILE_DIR,
        Configuration.CONFIGURATION_FILE_NAME);
    traverser = new PathTraverser();
    List<? extends Entity> environments = new FindingService(new Environment()).findAll(TABLE_NAMES[1]);
    environments_list = new ArrayList<>();
    
    logger.info("Copying Log Location to database....");
    for (Entity obj : environments)
    {
      if (isEnable(obj))
      {
        environments_list.add((Environment) obj);

      }
    }

    log_monitor_list = new ArrayList<>();
    logs = new ArrayList<LogMonitor>();
    for (Environment env : environments_list)
    {
      List<Entity> dummy = new FindingService(new LogMonitor()).findAllForIntegervalue(TABLE_NAMES[0], "env_id",
          Integer.toString(env.id));
      log_monitor_list.addAll(dummy);
    }

  }

  public static void init()
  {

    for (Environment env : environments_list)
    {
      System.out.println("Environement Path " + env.name.trim() );
      File directories[] = new File(env.name.trim()).listFiles();
      System.out.println("Directories Path " +directories );
      if(directories == null)
        continue;
      Thread tasks[] = new Thread[se.cambio.loganalyzer.common.ElementSize.getThreadSize(directories)];
      se.cambio.loganalyzer.common.TaskHandler.createThreadTask(tasks, directories);

      TaskHandler.waitForTaskCompletion(tasks);

      insertLogMonitorEntry(env);
    }
    logger.info("Fetching Log Location From database....");
    for (Entity obj : log_monitor_list)
    {
      if (isEnable(obj))
        logs.add((LogMonitor) obj);
    }

    List<? extends Entity> log_issues_list = new FindingService(new Issue()).findAll(TABLE_NAMES[2]);
    issues = new ArrayList<Issue>();

    for (Entity obj : log_issues_list)
    {
      if (isEnable(obj))
        issues.add((Issue) obj);
    }
    logger.info("Log Analysing is starting....");
  }

  private static void insertLogMonitorEntry(Entity entity)
  {
    InsertingService insertingService = new InsertingService();
    logger.info("Inserting Log Locations to database....");
    for (String location : DataManagement.logsLocation)
    {
      File file = new File(location);
      String root = file.getAbsolutePath().replace(file.getName(), "");
      String fileName = file.getName();
      String splitFileName = fileName.substring(0, fileName.lastIndexOf("."));
      boolean isExits = isContainsEntry(root, splitFileName);
      if (!isExits)
      {
        Object[] values = { entity.id, root, splitFileName, "", Integer.toString(1) };
        int id = insertingService.insertTo(TABLE_NAMES[0], getAttributeValuePair(TABLE_NAMES[0], values));
        LogMonitor log_location = new LogMonitor(id, entity.id, root, splitFileName, 1, "");
        log_monitor_list.add(log_location);
      }
    }
    logger.info("Inserting Log Locations Entries finished");
  }

  private static Map<String, Object> getAttributeValuePair(String tableName, Object[] values)
  {
    Map<String, Object> pair = new HashMap<>();
    int i = 0;
    Table table = TableInitialization.getTables().get(tableName);
    for (String attribute_name : table.attributes)
    {
      if (!attribute_name.equals("id"))
        pair.put(attribute_name, values[i++]);
    }
    return pair;
  }

  private static boolean isContainsEntry(String fileRootLocation, String fileName)
  {
    for (Entity log_monitor : log_monitor_list)
    {
      LogMonitor log = (LogMonitor) log_monitor;
      if (log.location.trim().equals(fileRootLocation))
        if (log.file_prefix.trim().equals(fileName))
          return true;
    }
    return false;
  }

  private static boolean isEnable(Entity entity)
  {
    return entity.status == 1 ? true : false;
  }

}
