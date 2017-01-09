package se.cambio.loganalyzer.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import se.cambio.loganalyzer.dao.entities.Entity;
import se.cambio.loganalyzer.dao.entities.Environment;
import se.cambio.loganalyzer.dao.entities.Issue;
import se.cambio.loganalyzer.dao.entities.LogMonitor;
import se.cambio.loganalyzer.dao.entities.Mail_User;
import se.cambio.loganalyzer.dao.service.FindingService;
import se.cambio.loganalyzer.dao.service.InsertingService;
import se.cambio.loganalyzer.entities.ErrorLog;
import se.cambio.loganalyzer.main.App;
import se.cambio.loganalyzer.main.DataInitializer;
import se.cambio.loganalyzer.taskmgnt.TaskManager;
import se.cambio.loganalyzer.time.TimeProperty;
import se.cambio.mailservice.MailBody;

public class MailSender
{

  public static List<Mail_User> mail_users;

  public static String mail_users_string = "";

  private static Logger logger = Logger.getLogger(TaskManager.class);

  public static void getUpdatedUsers()
  {
    mail_users = new ArrayList<Mail_User>();
    mail_users_string = "";
    List<? extends Entity> users = new FindingService(new Mail_User()).findAll(DataInitializer.TABLE_NAMES[5]);

    for (Entity entity : users)
    {
      Mail_User user = (Mail_User) entity;
      if (user.status == 1)
      {
        mail_users.add(user);
        mail_users_string += user.mail_id + ",";
      }
    }
    mail_users_string = mail_users_string.trim();
    if (mail_users_string.length() - 1 == mail_users_string.lastIndexOf(","))
      mail_users_string = mail_users_string.substring(0, mail_users_string.length() - 1);

  }

  public static void sentMail(Map<String, Map<String, List<ErrorLog>>> errorLogs) throws IOException
  {
    String eroror_file_Value = "\t";
    Environment environment = new Environment();
    getMailBody();
    for (Map.Entry<String, Map<String, List<ErrorLog>>> entries : errorLogs.entrySet())
    {

      MailBody body = getMailBody();
      Map<String, List<ErrorLog>> logs = entries.getValue();
      for (Map.Entry<String, List<ErrorLog>> errorContens : logs.entrySet())
      {
        Environment env = environment.getEnvironment(Integer.parseInt(entries.getKey()),
            DataInitializer.environments_list);
        body.msg_subject = "Error Occured for " + errorContens.getKey() + " " + " in Pc Location " + env.name;
        body.attachment_path = entries.getKey();

        String bodyValues = "";
        List<String> bodyTableContents = new ArrayList<String>();
        eroror_file_Value += errorContens.getKey() + " in Machine Location " + env.name + "\n\t";
        for (ErrorLog log : errorContens.getValue())
        {
          bodyValues = "";
          bodyValues += errorContens.getKey() + MailBody.TEXT_SPLITER + log.getDescHTMLString() + MailBody.TEXT_SPLITER
              + log.error_count;
          bodyTableContents.add(bodyValues);
          insertValuesToTable(entries, errorContens, log);
        }
        body.msg_body_lines = bodyTableContents;

        se.cambio.mailservice.MailSender.getInstance().sendMail(body);

      }

    }
    if (errorLogs == null || errorLogs.size() == 0)
    {
      logger.info("There are no issues matched with log files");
      logger.info("Instants Mail not sent to " + mail_users_string);
      return;
    }
    logger.info("Instants Mail sent to " + mail_users_string);
    logger.info("Mail Has the contents \n" + eroror_file_Value);
  }

  public static void sentDailyMail(Map<String, Map<String, List<ErrorLog>>> errorLogs) throws IOException
  {
    MailBody body = null;
    body = getMailBody();
    body.is_item_attched = false;
    body.msg_subject = "Daily Mail For Following Errors" + "\n";
    List<String> bodyTableContents = new ArrayList<String>();
    for (Map.Entry<String, Map<String, List<ErrorLog>>> entries : errorLogs.entrySet())
    {
      Map<String, List<ErrorLog>> logs = entries.getValue();

      for (Map.Entry<String, List<ErrorLog>> errorContens : logs.entrySet())
      {
        String bodyValues = "";
        for (ErrorLog log : errorContens.getValue())
        {
          bodyValues = entries.getKey() + MailBody.TEXT_SPLITER;
          bodyValues += errorContens.getKey() + MailBody.TEXT_SPLITER + log.getDescHTMLString() + MailBody.TEXT_SPLITER
              + log.error_count;
          bodyTableContents.add(bodyValues);
        }
        body.msg_body_lines = (bodyTableContents);
      }
    }
    if (body != null)
      se.cambio.mailservice.MailSender.getInstance().sendMail(body);
  }

  private static void insertValuesToTable(Map.Entry<String, Map<String, List<ErrorLog>>> entries,
                                          Map.Entry<String, List<ErrorLog>> errorContens, ErrorLog log)
  {
    Map<String, Object> attribute_value_pair = new HashMap<String, Object>();
    attribute_value_pair.put("issue_id", new Issue().getID(DataInitializer.issues, errorContens.getKey()));
    attribute_value_pair.put("error_msg", log.getDescString());
    attribute_value_pair.put("times_occured", log.error_count);
    attribute_value_pair.put("sent_time", TimeProperty.getCurrentSystemTime());
    attribute_value_pair.put("status", 0);
    attribute_value_pair.put("log_id", new LogMonitor().getID(DataInitializer.logs, log.file_location));
    new InsertingService().insertTo(DataInitializer.TABLE_NAMES[4], attribute_value_pair);
  }

  private static MailBody getMailBody()
  {
    MailBody body = new MailBody();
    getUpdatedUsers();
    body.to = mail_users_string;
    body.from = App.configManager.getPorpsValue(16);
    body.is_item_attched = false;
    return body;
  }
}
