package se.cambio.loganalyzer.paths.traverser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import se.cambio.loganalyzer.common.DataManagement;
import se.cambio.loganalyzer.file.FileContentReader;

public class PathTraverser implements Runnable
{
  public static final String TRAVISING_STOPPED_POINTER_NAME ="errorlog" +"." +FileContentReader.FILE_EXTENSION;

  public ArrayList<String> logsFileLocations;

  public boolean isFound = false;

  public File []files;
  public PathTraverser()
  {

    logsFileLocations = DataManagement.logsLocation;
  }

  private boolean canStop(File file)
  {
    boolean canStop = false;
    if (TRAVISING_STOPPED_POINTER_NAME.contains("."))
    {
      if (file.getName().equals(TRAVISING_STOPPED_POINTER_NAME))
        return true;
    }
    if (file.getName().equals(TRAVISING_STOPPED_POINTER_NAME))
      canStop = true;
    return canStop;
  }

  private String getFileLocation(File file)
  {
    if (TRAVISING_STOPPED_POINTER_NAME.equals("."))
    {
      //return file.getAbsolutePath().replace(file.getName(), "");
      return file.getAbsolutePath();
    }
    else
    {
      return file.getAbsolutePath();
    }
  }

  public String findDir(File root)
  {
    if (canStop(root))
    {
      synchronized (PathTraverser.class)
      {
        System.out.println("Locationg For Log File " + getFileLocation(root));
        logsFileLocations.add(getFileLocation(root));
      }
      isFound = true;
    }

    File[] files = root.listFiles();

    if (files != null)
    {
      for (File f : files)
      {
        String myResult = findDir(f);
        if (myResult == null)
        {
          continue;
        }
        else
        {
          return myResult;
        }

      }
    }
    return null;
  }

  public List<String> getCutsLocations()
  {
    return this.logsFileLocations;
  }

  public String getLocation(int position)
  {
    if (position >= 0 && position >= logsFileLocations.size())
      return this.logsFileLocations.get(position);
    return null;
  }

  public static void main(String args[])
  {
    PathTraverser traverser = new PathTraverser();
    traverser.findDir(new File("\\\\cwlk-srishiyama\\codebrag\\CUTFiles"));
    System.out.println(traverser.logsFileLocations);
  }

  @Override
  public void run()
  {
    for(File file :files)
    {
      findDir(file);
    }
    
  }
}
