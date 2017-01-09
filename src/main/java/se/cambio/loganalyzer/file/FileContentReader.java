package se.cambio.loganalyzer.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.cambio.loganalyzer.dao.service.UpdatingService;
import se.cambio.loganalyzer.entities.LogLine;
import se.cambio.loganalyzer.main.DataInitializer;
import se.cambio.loganalyzer.taskmgnt.TaskManager;
import se.cambio.loganalyzer.time.TimeComparator;
import se.cambio.loganalyzer.time.TimeProperty;

public class FileContentReader
{

  public final static String FILE_EXTENSION = TaskManager.configUpdateManager.getPorpsValue(17);

  private List<String> file_name_time_stamps;

  private String file_pointer;

  private static File main_file;

  private int id;

  private List<String> fileContents;

  private boolean isDbUpdatedForLastFilePointer;

  private boolean isLastPointerExists = false;

  public FileContentReader(int id, String file_last_pointer)
  {
    fileContents = new ArrayList<String>();
    this.id = id;
    this.file_pointer = file_last_pointer;
  }

  public List<String> readTextFile(File file)
  {

    BufferedReader br = null;
    if (!file.exists())
      return new ArrayList<>();
    try
    {
      String currentLineText;
      Reader reader = new FileReader(file);
      br = new BufferedReader(reader);

      String last_pointer = file_pointer;//ConfigManager.getPorpsValue(1);
      while ((currentLineText = br.readLine()) != null)
      {
        if (!isDbUpdatedForLastFilePointer)
        {
          if (currentLineText.contains(last_pointer))
            isLastPointerExists = true;
        }
        fileContents.add(currentLineText);
      }

      if (!isDbUpdatedForLastFilePointer)
      {
        String log_last_line_date = LogLine.getErrorDate(fileContents.size() - 1, fileContents);
        String date = TimeProperty.getFormatedDateForLogFileLine(log_last_line_date);
        if (date == null)
          date = "";
        new UpdatingService().updatePKData(DataInitializer.TABLE_NAMES[0], Integer.toString(this.id),
            "last_line_file_pointer", date);
        isDbUpdatedForLastFilePointer = true;

        if (!isLastPointerExists)
        {
          readExpandedFiles();
        }
      }

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      try
      {
        if (br != null)
          br.close();
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
    }
    return fileContents;
  }

  private static String getNormalizedFileName(String name, String modifier, String container)
  {
    if (name.contains(container))
    {
      name = name.replace(container, modifier);
    }
    return name;
  }

  private void readExpandedFiles()
  {

    File fileDirs = new File(main_file.getPath().replace(main_file.getName(), ""));
    File files[] = fileDirs.listFiles();
    file_name_time_stamps = new ArrayList<String>();
    for (File last_changed_log_file : files)
    {
      if (last_changed_log_file.isFile())
      {
        String fileName = last_changed_log_file.getName();
        String file_changed_time_stamp = TimeProperty.getFileNameTimeStamp(fileName);
        file_name_time_stamps.add(file_changed_time_stamp);
      }
    }
    file_name_time_stamps.add(TaskManager.configUpdateManager.getPorpsValue(0));

    Collections.sort(file_name_time_stamps, TimeComparator.getTimeStamp(TimeProperty.TIME_STAMP_FORMAT));

    int my_last_anlaysed_position = file_name_time_stamps.indexOf(TaskManager.configUpdateManager.getPorpsValue(0));
    //fileContents = new ArrayList<String>();
    for (int i = my_last_anlaysed_position + 1; i < file_name_time_stamps.size(); i++)
    {
      String nam = file_name_time_stamps.get(i);
      if (nam != null && !nam.equals("") && !nam.isEmpty())
      {
        String tmpName = main_file.getName().substring(0, main_file.getName().lastIndexOf(".")) + "_"
            + file_name_time_stamps.get(i) + "." + FILE_EXTENSION;
        tmpName = getNormalizedFileName(tmpName, ".", ":");
        readTextFile(new File(main_file.getPath().replace(main_file.getName(), ""), tmpName));
      }
    }
    ///fileContents.addAll(readTextFile(main_file, true));

  }

  public List<String> readTextFile(String fileLocation)
  {
    File file = new File(fileLocation);
    main_file = file;
    return readTextFile(file);
  }

  public static void main(String args[])
  {
    //    System.out.println(
    //        new FileContentReader(1, "").readTextFile(new File("E:\\Resources\\LogAnalyser\\res\\tmp\\log.log"), false));
  }

}
