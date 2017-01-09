package se.cambio.loganalyzer.entities;

import java.util.ArrayList;
import java.util.List;

import se.cambio.loganalyzer.dao.entities.LogHistory;

public class ErrorLog
{
  public final String MATCHING_PATTERN_STRING = ">";

  public String errorID;

  public String errorName;

  public List<String> errorDescriptions;

  public int error_count = 1;

  public LogHistory history;

  public String file_location;

  public ErrorLog()
  {
    this.errorDescriptions = new ArrayList<String>();
    this.history = new LogHistory();
  }

  public String getDescString()
  {
    String descString = "";

    for (String desc : errorDescriptions)
    {
      descString += desc + "\n";
    }
    return descString;
  }

  public String getDescString(String errorName)
  {
    String descString = "";
    boolean isMatchedError = false;
    for (String desc : errorDescriptions)
    {
      if (desc.contains(errorName.trim()))
      {
        isMatchedError = true;
      }
      if (isMatchedError)
        descString += desc + "\n";
    }
    return descString;
  }

  public List<String> getListFrom(String errorName, List<String> errorLogs)
  {
    int pos = 0;
    for (String errorLog : errorLogs)
    {
      if (errorLog.contains(errorName))
      {
        pos = errorLogs.indexOf(errorLog);
        break;
      }
    }
    return errorLogs.subList(pos, errorLogs.size());
  }

  public String getDescHTMLString()
  {
    String descString = "";

    for (String desc : errorDescriptions)
    {
      descString += desc + "<br/>";
    }
    return descString;
  }

  public List<String> getNormalizedList()
  {

    int lastPosition = 0;
    List<String> normalizedErrorList = new ArrayList<String>();

    for (String desc : this.errorDescriptions)
    {
      System.out.print(desc.replace(desc, ""));
      if (desc.trim().contains(MATCHING_PATTERN_STRING))
        lastPosition = desc.lastIndexOf(MATCHING_PATTERN_STRING);
      if (lastPosition < 0)
        lastPosition = 0;
      normalizedErrorList.add(desc.substring(lastPosition));

      lastPosition = 0;
    }

    return normalizedErrorList;
  }
}
