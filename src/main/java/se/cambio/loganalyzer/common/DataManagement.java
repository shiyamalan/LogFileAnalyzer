package se.cambio.loganalyzer.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.cambio.loganalyzer.LogAnalyser;
import se.cambio.loganalyzer.entities.ErrorLog;
import se.cambio.loganalyzer.entities.LogLine;

public class DataManagement
{
  public static Map<String, Map<String, List<ErrorLog>>> errorLogs;

  public static Map<String, Map<String, List<ErrorLog>>> errorLogsUnsynchronized;
  
  public static ArrayList<String> logsLocation;
  static
  {
    errorLogs = new HashMap<String, Map<String, List<ErrorLog>>>();
    logsLocation = new ArrayList<String>();

  }

  public static void appendToMap(ErrorLog errorLog, String FILE_ID)
  {
 
    synchronized (LogAnalyser.class)
    {
      Map<String, List<ErrorLog>> errorContentMap;
      if (errorLogs.containsKey(FILE_ID))
      {
        errorContentMap = errorLogs.get(FILE_ID);
        addToList(errorContentMap, errorLog);
      }
      else
      {
        List<ErrorLog> erorList = new ArrayList<ErrorLog>();
        erorList.add(errorLog);
        errorContentMap = new HashMap<String, List<ErrorLog>>();
        errorContentMap.put(errorLog.errorName, erorList);
        errorLogs.put(FILE_ID, errorContentMap);
      }
    }
  }

  public static void appendToMapUnSynchronized(ErrorLog errorLog, String analysing_log_file)
  {
    Map<String, List<ErrorLog>> errorContentMap;

    if (errorLogsUnsynchronized == null)
      errorLogsUnsynchronized = new HashMap<String, Map<String, List<ErrorLog>>>();
    if (errorLogsUnsynchronized.containsKey(analysing_log_file))
    {
      errorContentMap = errorLogsUnsynchronized.get(analysing_log_file);
      addToList(errorContentMap, errorLog);
    }
    else
    {
      List<ErrorLog> erorList = new ArrayList<ErrorLog>();
      erorList.add(errorLog);
      errorContentMap = new HashMap<String, List<ErrorLog>>();
      errorContentMap.put(errorLog.errorName, erorList);
      errorLogsUnsynchronized.put(analysing_log_file, errorContentMap);
    }
  }

  private static void addToList(Map<String, List<ErrorLog>> errorContentMap, ErrorLog errorLog)
  {
    if (errorContentMap.containsKey(errorLog.errorName))
    {
      appendErrorContentsToList(errorContentMap.get(errorLog.errorName), errorLog);
    }
    else
    {
      List<ErrorLog> contents = new ArrayList<ErrorLog>();
      contents.add(errorLog);
      errorContentMap.put(errorLog.errorName, contents);
    }
  }

  private static void appendErrorContentsToList(List<ErrorLog> list, ErrorLog errorLog)
  {
    LogLine logLine = new LogLine();
    for (ErrorLog log : list)
    {
      if (logLine.isStringContains(errorLog.getListFrom(errorLog.errorName, errorLog.getNormalizedList()),
          log.getDescString(errorLog.errorName)))
      {
        log.error_count += errorLog.error_count;
        return;
      }
    }
    list.add(errorLog);
  }
}
