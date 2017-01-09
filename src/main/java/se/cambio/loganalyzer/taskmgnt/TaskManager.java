package se.cambio.loganalyzer.taskmgnt;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;

import se.cambio.loganalyzer.LogAnalyser;
import se.cambio.loganalyzer.common.DataManagement;
import se.cambio.loganalyzer.config.ConfigManager;
import se.cambio.loganalyzer.config.Configuration;
import se.cambio.loganalyzer.dao.entities.LogMonitor;
import se.cambio.loganalyzer.file.FileContentReader;
import se.cambio.loganalyzer.mail.MailScheduler;
import se.cambio.loganalyzer.time.TimeComparator;
import se.cambio.loganalyzer.time.TimeProperty;

public class TaskManager
{

  public static ConfigManager configUpdateManager = null;

  public static Logger logger = Logger.getLogger(TaskManager.class);

  static
  {
    configUpdateManager = new ConfigManager(Configuration.CONFIGURATION_PROPERTY_FILE_DIR,
        Configuration.INTERNAL_CONFIGURATION_FILE_NAME);
  }

  public static void startingTask(List<LogMonitor> logsList) throws InterruptedException, ExecutionException
  {
    if (logsList == null || logsList.size() <= 0)
      return;
    Thread[] logTasks = new Thread[logsList.size()];
    for (int i = 0; i < logTasks.length; i++)
    {
      LogAnalyser logAnalyser = new LogAnalyser();
      logAnalyser.analysing_log_file = logsList.get(i).location.trim() + logsList.get(i).file_prefix.trim() + "."
          + FileContentReader.FILE_EXTENSION;
      logAnalyser.environment_id = logsList.get(i).env_id;
      logAnalyser.last_file_pointer = logsList.get(i).last_line_file_pointer.trim();
      logAnalyser.log_file_id = logsList.get(i).id;
      logTasks[i] = new Thread(logAnalyser);
      logTasks[i].start();

    }
    for (int i = 0; i < logTasks.length; i++)
      logTasks[i].join();
    sendMail();

  }

  private static void scedhuleToNextDay() throws ParseException
  {
    String current_time = TimeProperty.getSystemCurrentTime();
    if (TimeComparator.isAfter(current_time))
    {
      configUpdateManager.updateConfigFile(Configuration.CONFIG_PROPERTY_FILE_KEYS[5], Boolean.toString(false));
    }

  }

  private static void sendMail()
  {
    try
    {
      logger.info("----------Sending Instatnts Email-------------");
      se.cambio.loganalyzer.mail.MailSender.sentMail(DataManagement.errorLogs);
      logger.info("----------Email Sent-------------");
    }
    catch (IOException e)
    {
      logger.error("Error in sending email : - " + e.getMessage());
    }
  }

  public static void setMailSchduler() throws ParseException
  {
    String current_time = TimeProperty.getSystemCurrentTime();
    String mail_scheduled = TaskManager.configUpdateManager.getPorpsValue(5);
    if (!Boolean.parseBoolean(mail_scheduled) && TimeComparator.isBetween(current_time))
    {
      MailScheduler scheduler = new MailScheduler();
      scheduler.schedule();
      configUpdateManager.updateConfigFile(Configuration.CONFIG_PROPERTY_FILE_KEYS[5], Boolean.toString(true));
    }
    scedhuleToNextDay();
  }
}
