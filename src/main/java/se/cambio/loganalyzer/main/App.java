package se.cambio.loganalyzer.main;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import se.cambio.loganalyzer.common.Common;
import se.cambio.loganalyzer.config.ConfigManager;
import se.cambio.loganalyzer.config.Configuration;
import se.cambio.loganalyzer.dao.entities.Issue;
import se.cambio.loganalyzer.dao.entities.LogMonitor;
import se.cambio.loganalyzer.taskmgnt.TaskManager;
import se.cambio.loganalyzer.time.TimeProperty;

/**
 * Hello world!
 *
 */
public class App
{
  public static ConfigManager configManager = null;

  public static List<Issue> issuesList = null;

  public static List<LogMonitor> logsList = null;

  private static Logger logger = Logger.getLogger(App.class);

  static
  {
    logger.info("----------------" + "LogAnalyser Started" + "----------------");
    DataInitializer.init();
    issuesList = DataInitializer.issues;
    logsList = DataInitializer.logs;

  }

  public static void main(String[] args)
  {
    try
    {
      if (Common.canRun(14, configManager))
        TaskManager.setMailSchduler();
      if (Common.canRun(13, configManager))
        TaskManager.startingTask(logsList);

      if (!Common.canRun(13, configManager))
      {
        logger.warn("The " + Configuration.CONFIGURATION_FILE_NAME + " in "
            + Configuration.CONFIGURATION_PROPERTY_FILE_DIR + " contains false for instants mail");
      }

      if (!Common.canRun(14, configManager))
      {
        logger.warn("The " + Configuration.CONFIGURATION_FILE_NAME + " in "
            + Configuration.CONFIGURATION_PROPERTY_FILE_DIR + " contains false for daily mail");
      }

    }
    catch (InterruptedException e)
    {
      logger.error("Error in Application Thread Interrruption", e);
    }
    catch (ExecutionException e)
    {
      logger.error("Error in Application Thread Execution", e);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }

    TaskManager.configUpdateManager.updateConfigFile(Configuration.CONFIG_PROPERTY_FILE_KEYS[0],
        TimeProperty.getCurrentSystemTime());
    logger.info("----------------" + "LogAnalyser Finished" + "----------------");
  }

}
