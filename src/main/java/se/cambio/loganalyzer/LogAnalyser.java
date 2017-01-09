package se.cambio.loganalyzer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import se.cambio.loganalyzer.common.DataManagement;
import se.cambio.loganalyzer.entities.ErrorLog;
import se.cambio.loganalyzer.entities.LogLine;
import se.cambio.loganalyzer.file.FileContentReader;

public class LogAnalyser implements Runnable
{
  public String analysing_log_file = "";

  public String last_file_pointer = "";

  public int log_file_id;
  
  public int environment_id;

  public void run()
  {
    try
    {
      arrangeFileContents();
      //printLogs(DataManagement.errorLogs);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void arrangeFileContents() throws IOException
  {
    FileContentReader contentReader = new FileContentReader(this.log_file_id,this.last_file_pointer);
    List<String> file_contents = contentReader.readTextFile(analysing_log_file);
    LogLine logLine = new LogLine();
    ErrorLog errorLog = new ErrorLog();
    if (file_contents != null)
    {
      int indexOfFileContent = 0;
      for (String content : file_contents)
      {
        if (logLine.isContainedError(content))
        {
          logLine.setNewLineNo(indexOfFileContent, file_contents);
          List<String> particularErrorLinse = logLine.getLinesListFor(logLine.startPosition, logLine.endPosition,
              file_contents);
          errorLog.errorDescriptions = particularErrorLinse;
          errorLog.errorName = logLine.matchinErrorName;
          errorLog.errorID = errorLog.errorName + LogLine.getErrorDate(file_contents.indexOf(content), file_contents);
          errorLog.file_location = analysing_log_file;
          DataManagement.appendToMap(errorLog, Integer.toString(environment_id));
          errorLog = new ErrorLog();
        }
        indexOfFileContent++;
      }
    }
  }

  public void printLogs(Map<String, Map<String, List<ErrorLog>>> errorLogs)
  {
    if (errorLogs == null || errorLogs.isEmpty())
      return;
    for (Map.Entry<String, Map<String, List<ErrorLog>>> entries : errorLogs.entrySet())
    {
      Map<String, List<ErrorLog>> logs = entries.getValue();
      System.out.println("--------- File Location:-" + entries.getKey() + "--------");
      for (Map.Entry<String, List<ErrorLog>> errorContens : logs.entrySet())
      {
        for (ErrorLog log : errorContens.getValue())
        {
          System.out.println("--------- Particular Log Position For Error " + errorContens.getKey() + " "
              + log.error_count + " in times occured in" + entries.getKey() + " --------------");
          System.out.println(log.getDescString());

        }

      }
      System.out.println("\n\n");
    }
  }

}
