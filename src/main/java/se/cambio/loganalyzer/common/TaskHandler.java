package se.cambio.loganalyzer.common;

import java.io.File;

import se.cambio.loganalyzer.paths.traverser.PathTraverser;

public class TaskHandler
{
  public static void createThreadTask(Thread[] tasks, File directories[])
  {
    for (int i = 0; i < tasks.length; i++)
    {
      PathTraverser traverser = new PathTraverser();

      int startPos = 0;
      int endPos = 0;

      startPos = i * ElementSize.MAX_THREADS;
      endPos = ElementSize.MAX_THREADS + startPos;
      if (endPos > directories.length)
        endPos = directories.length;
      if (ElementSize.getThreadSize(directories) == directories.length)
      {
        startPos = i;
        endPos = startPos + 1;
      }
      traverser.files = getPartianArray(startPos, endPos, directories);
      tasks[i] = new Thread(traverser);
      tasks[i].start();
    }
  }

  public static void waitForTaskCompletion(Thread[] tasks)
  {
    for (int i = 0; i < tasks.length; i++)
      try
      {
        tasks[i].join();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
  }

  public static File[] getPartianArray(int startPos, int endPos, File directories[])
  {
    int size = ElementSize.getPartianArraySize(directories);
    int index = 0;
    File result[] = new File[size];

    for (int i = startPos; i < endPos; i++)
    {
      result[index++] = directories[i];
    }
    return result;
  }

}
