package se.cambio.loganalyzer.mail;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.cambio.loganalyzer.common.DataManagement;
import se.cambio.loganalyzer.config.Configuration;
import se.cambio.loganalyzer.dao.entities.LogHistory;
import se.cambio.loganalyzer.dao.service.FindingLogHistory;
import se.cambio.loganalyzer.dao.service.UpdatingService;
import se.cambio.loganalyzer.entities.ErrorLog;
import se.cambio.loganalyzer.main.DataInitializer;
import se.cambio.loganalyzer.taskmgnt.TaskManager;

public class HistoricalMailSender implements Runnable
{
  public void run()
  {
    System.out.println("------------------------Daily Mail Sender is sendind Mail-----------------");
    FindingLogHistory findingService = new FindingLogHistory(new LogHistory());
    findingService.findAll(DataInitializer.TABLE_NAMES[4], DataInitializer.TABLE_NAMES[0],
        DataInitializer.TABLE_NAMES[2]);
    try
    {
      if (DataManagement.errorLogsUnsynchronized == null || DataManagement.errorLogsUnsynchronized.isEmpty())
      {
        TaskManager.configUpdateManager.updateConfigFile(Configuration.CONFIG_PROPERTY_FILE_KEYS[5], "false");
        return;
      }
      MailSender.sentDailyMail(DataManagement.errorLogsUnsynchronized);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    DataManagement.errorLogsUnsynchronized = new HashMap<String, Map<String, List<ErrorLog>>>();
    UpdatingService service = new UpdatingService();
    for (ErrorLog log : findingService.logs)
    {
      service.updatePKData(DataInitializer.TABLE_NAMES[4], log.errorID, "status", 1);
    }

    System.out.println("------------------------Daily Mail Sender sent Mail-----------------");
  }

  public static void main(String args[])
  {
    new Thread(new HistoricalMailSender()).start();
  }
}
