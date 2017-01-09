package se.cambio.loganalyzer.common;

import java.io.File;

public class ElementSize
{
  public static int total_array_size = 0;
  
  public static final int MAX_THREADS = 100;
  
  public static int getThreadSize(File directories[])
  {
    int size = 0;
    if (MAX_THREADS > directories.length)
      size = directories.length;
    else
    {
      size = (directories.length % MAX_THREADS == 0) ? directories.length / MAX_THREADS
          : directories.length / MAX_THREADS + 1;
    }

    return size;
  }

  public static int getPartianArraySize(File directories[])
  {
    int size = 0;
    if (total_array_size == directories.length)
      return size;
    if (MAX_THREADS > directories.length)
      size = 1;
    else
    {
      size = MAX_THREADS;
      total_array_size += size;

      if (total_array_size >= directories.length)
      {
        total_array_size -= MAX_THREADS;
        size = directories.length - total_array_size;
        total_array_size += size;
      }

    }

    return size;
  }
}
