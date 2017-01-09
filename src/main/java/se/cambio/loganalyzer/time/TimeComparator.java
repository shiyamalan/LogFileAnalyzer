package se.cambio.loganalyzer.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import se.cambio.loganalyzer.main.App;

public class TimeComparator
{
  //"yyyy-MM-dd HH.mm.ss"
  public static Comparator<String> getTimeStamp(String format)
  {
    Comparator<String> comp = new Comparator<String>()
    {

      @Override
      public int compare(String timeStmp1, String timeStmp2)
      {
        String startDate = timeStmp1;
        String endDate = timeStmp2;
        int cmp = -1;
        if (startDate.equals("") || endDate.equals(""))
          return cmp;
        try
        {
          Date start = new SimpleDateFormat(format, Locale.ENGLISH).parse(startDate);
          Date end = new SimpleDateFormat(format, Locale.ENGLISH).parse(endDate);

          System.out.println(start);
          System.out.println(end);

          if (start.compareTo(end) > 0)
          {
            cmp = 1;
          }
          else if (start.compareTo(end) < 0)
          {
            cmp = -1;
          }
          else if (start.compareTo(end) == 0)
          {
            System.out.println("start is equal to end");
          }
          else
          {
            cmp = 0;
          }

        }
        catch (ParseException e)
        {
          e.printStackTrace();
        }
        return cmp;
      }
    };
    return comp;
  }

  public static boolean isBetween(String checkTime) throws ParseException
  {
    try
    {
//      ConfigManager configManager = new ConfigManager(Configuration.CONFIGURATION_PROPERTY_FILE_DIR,
//          Configuration.CONFIGURATION_FILE_NAME);
      String interval = App.configManager.getPorpsValue(6);
      String date = TimeProperty.getSystemCurrentDate();

      String string1 = date.trim() + " " + interval.split("-")[0];
      Date time1 = new SimpleDateFormat(TimeProperty.TIME_STAMP_FORMAT).parse(string1);

      String string2 = date.trim() + " " + interval.split("-")[1];
      Date time2 = new SimpleDateFormat(TimeProperty.TIME_STAMP_FORMAT).parse(string2);

      String someRandomTime = date.trim() + " " + checkTime;
      Date d = new SimpleDateFormat(TimeProperty.TIME_STAMP_FORMAT).parse(someRandomTime);

      if (time1.getTime() < d.getTime())
        if (time2.getTime() > d.getTime())
          return true;
        else
          return false;
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }

    return false;
  }
  
  public static boolean isAfter(String checkingTime) throws ParseException
  {
    String interval = App.configManager.getPorpsValue(6);
    String date = TimeProperty.getSystemCurrentDate();
    
    String string2 = date.trim() + " " + interval.split("-")[1];
    Date time1 = new SimpleDateFormat(TimeProperty.TIME_STAMP_FORMAT).parse(string2);
    
    String someRandomTime = date.trim() + " " + checkingTime;
    Date d = new SimpleDateFormat(TimeProperty.TIME_STAMP_FORMAT).parse(someRandomTime);
    if(time1.getTime()<=d.getTime())
      return true;
    else
      return false;
    
  }

  public static void main(String args[])
  
  {
//    try
//    {
//      System.out.println(isLarger("05.02.00"));
//    }
//    catch (ParseException e)
//    {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
  }
}
