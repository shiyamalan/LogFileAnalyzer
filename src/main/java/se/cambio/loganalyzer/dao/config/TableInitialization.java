package se.cambio.loganalyzer.dao.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import se.cambio.loganalyzer.main.DataInitializer;

public class TableInitialization
{
  public static Map<String, Table> tables;

  static
  {
    tables = new HashMap<String, Table>();
    init();
  }

  public static void init()
  {
    Table logmonitorTable = new Table();
    logmonitorTable.tableName = DataInitializer.TABLE_NAMES[0];
    logmonitorTable.attributes = new ArrayList<String>(
        Arrays.asList("id", "env_id", "location", "file_name_prefix","last_line_file_pointer", "status"));

    Table issuesTable = new Table();
    issuesTable.tableName = "issues";
    issuesTable.attributes = new ArrayList<String>(Arrays.asList("id", "type", "status"));

    Table environmentTable = new Table();
    environmentTable.tableName = "environments";
    environmentTable.attributes = new ArrayList<String>(Arrays.asList("id", "name", "status"));

    Table skipMessageTable = new Table();
    skipMessageTable.tableName = "skipmessages";
    skipMessageTable.attributes = new ArrayList<String>(Arrays.asList("id", "message", "status"));

    Table loghistoryTable = new Table();
    loghistoryTable.tableName = "log_history";
    loghistoryTable.attributes = new ArrayList<String>(
        Arrays.asList("id", "issue_id", "error_msg", "times_occured", "sent_time", "status"));

    Table mailusersTable = new Table();
    mailusersTable.tableName = "mail_users";
    mailusersTable.attributes = new ArrayList<String>(
        Arrays.asList("id", "mail_id", "user_name", "status"));
    
    tables.put(logmonitorTable.tableName, logmonitorTable);
    tables.put(issuesTable.tableName, issuesTable);
    tables.put(environmentTable.tableName, environmentTable);
    tables.put(skipMessageTable.tableName, skipMessageTable);
    
    tables.put(loghistoryTable.tableName, loghistoryTable);
    tables.put(mailusersTable.tableName, mailusersTable);
  }

  public static Map<String, Table> getTables()
  {
    return tables;
  }
}
