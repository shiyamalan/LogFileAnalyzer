package se.cambio.loganalyzer.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeProperty
{
  public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH.mm.ss";//file name format
  
  public static final String TIME_FORMAT ="HH.mm.ss";

  public static final String FILE_TIME_SEPARATOR = ".";

  public static final String FILE_DATE_SEPARATOR = "-";

  public static final String LOG_FILE_TIME_STAMP_FORMAT = "yyyy.MM.dd HH:mm:ss";//log file content format

  public static final String DATE_FORMAT = "yyyy-MM-dd";

  public static final SimpleDateFormat format = new SimpleDateFormat(TIME_STAMP_FORMAT);

  public static final SimpleDateFormat date_format = new SimpleDateFormat(DATE_FORMAT);
  
  public static final SimpleDateFormat time_format = new SimpleDateFormat(TIME_FORMAT);
  static
  {

  }

  public static String getCurrentSystemTime()
  {
    String currentDate = "";
    currentDate = format.format(new Date());
    return currentDate;
  }
  
  
  public static String getSystemCurrentTime()
  {
    return time_format.format(new Date());
  }
  public static String getSystemCurrentDate()
  {
    return date_format.format(new Date());
  }
  
  public static Date getSystemDate()
  {
    return new Date();
  }
  public static String getNextDay(Date currentDate)
  {
    long milliseconds =0;//(long) 24 * 60 * 60 * 1000;

    Date oneDayAfter = new Date(currentDate.getTime() + milliseconds);
    return date_format.format(oneDayAfter);

  }

  public static String getFormatedDate(String date)
  {
    if (date.equals(""))
      return "";
    return format.format(validateDateFormat(date, LOG_FILE_TIME_STAMP_FORMAT));
  }
  
  public static String getFormatedDateForLogFileLine(String date)
  {
    if (date.equals(""))
      return "";
    SimpleDateFormat date_format_log_file = new SimpleDateFormat(LOG_FILE_TIME_STAMP_FORMAT);
    
    try
    {
      return date_format_log_file.format(validateDateFormat(date, LOG_FILE_TIME_STAMP_FORMAT));
    }
    catch(Exception e)
    {
      return "";
    }

  }

  public static String getFileNameTimeStamp(String fileName)
  {
    String time_stamp = "";
    String fileNameParts[] = null;
    if (fileName.contains("."))
      fileName = fileName.substring(0, fileName.lastIndexOf("."));
    if (fileName.contains("_"))
    {
      fileNameParts = fileName.split("_");
      for (String part : fileNameParts)
      {
        if (part != null && validateDateFormat(part, TIME_STAMP_FORMAT) != null)
        {
          time_stamp = part;
          break;
        }
      }
    }
    return time_stamp;
  }

  public static Date validateDateFormat(String dateToValdate, String formaterStr)
  {

    SimpleDateFormat formatter = new SimpleDateFormat(formaterStr);
    formatter.setLenient(false);
    Date parsedDate = null;
    try
    {
      parsedDate = formatter.parse(dateToValdate.trim());

    }
    catch (ParseException e)
    {
      return null;
    }
    return parsedDate;
  }
}
