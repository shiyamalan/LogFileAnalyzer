package se.cambio.loganalyzer.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.cambio.loganalyzer.dao.entities.Issue;
import se.cambio.loganalyzer.main.App;

public class LogLine
{

  public String matchinErrorName = "";

  public int startPosition = 0;

  public int endPosition = 0;

  private int matching_line = 0;

  private final int MAX_NO_OF_LINES = 15;

  private int MAX_NO_OF_MATCHING_LINES = 5;

  public boolean isContainedError(String content) throws IOException
  {
    boolean isContained = false;
    for (Issue error : App.issuesList)
    {
      System.out.print(error.type.replace(error.type,""));
      if (error.type != null && content.contains(error.type.trim()))
      {
        isContained = true;
        matchinErrorName = error.type;
        break;
      }
    }
    return isContained;
  }

  public boolean isStringContains(List<String> lines, String errorDesc)
  {
  
    for (String line : lines)
    {
      if (errorDesc.contains(line))
        matching_line++;
      if (matching_line == MAX_NO_OF_MATCHING_LINES)
      {
        matching_line = 0;
        return true;
      }
    }
    matching_line = 0;
    return false;
  }

  public int getLinesNumber(int lineNo, boolean isDown)
  {
    int indexPosition = 0;
    if (isDown)
      indexPosition = lineNo + MAX_NO_OF_LINES;
    else
      indexPosition = lineNo - MAX_NO_OF_LINES;
    return indexPosition;
  }

  public synchronized List<String> getLinesListFor(int startIndex, int endIndex, List<String> fileContents)
  {
    // int endIndex = TOTAL_LINE_FACTOR * MAX_NO_OF_LINES;
    List<String> subLineList = null;
    if (startIndex <= fileContents.size() && fileContents.size() >= endIndex)
    {
      subLineList = new ArrayList<String>(fileContents.subList(startIndex, endIndex));
    }
    return subLineList;
  }

  public synchronized void setNewLineNo(int errorFoundPosition, List<String> fileContents)
  {
    startPosition = getLinesNumber(errorFoundPosition, false);
    endPosition = getLinesNumber(errorFoundPosition, true);

    if (startPosition < 0)
    {
      startPosition = 0;
    }
    if (endPosition > fileContents.size())
    {
      endPosition = fileContents.size() - 1;
    }
  }

  public static String getErrorDate(int errorFoundPosition, List<String> fileContets)
  {
    final int DATE_POSITION_IN_ARRAY = 1;
    if(errorFoundPosition<0)
      return "";
    String errorLine = fileContets.get(errorFoundPosition);
    String date = "";
    try
    {
      date = errorLine.split(">")[DATE_POSITION_IN_ARRAY];

    }
    catch (Exception e)
    {
      //      errorLine = fileContets.get(errorFoundPositionm - 1);
      //      date = errorLine.split(">")[DATE_POSITION_IN_ARRAY];
      errorFoundPosition = errorFoundPosition - 1;
      date = getErrorDate(errorFoundPosition, fileContets);
    }

    return date;
  }
}
