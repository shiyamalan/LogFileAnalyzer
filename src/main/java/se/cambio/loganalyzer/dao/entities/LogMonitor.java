package se.cambio.loganalyzer.dao.entities;

import java.util.List;

import se.cambio.loganalyzer.file.FileContentReader;

public class LogMonitor extends Entity
{
  public int env_id;

  public String location;

  public String file_prefix;

  public String last_line_file_pointer;

  public LogMonitor(int id, int env_id, String location, String file_prefix, int status,String last_pointer)
  {
    super(id, status);
    this.env_id = env_id;
    this.location = location;
    this.file_prefix = file_prefix;
    this.last_line_file_pointer = last_pointer;
  }

  public LogMonitor()
  {

  }

  @Override
  public void assignValues()
  {
    String pairs[] = this.attributes_value_pair.split(",");
    this.id = Integer.parseInt(pairs[0]);
    this.env_id = Integer.parseInt(pairs[1]);
    this.location = pairs[2];
    this.file_prefix = pairs[3];
    this.last_line_file_pointer = pairs[4];
    this.status = Integer.parseInt(pairs[5]);

  }

  public int getID(List<LogMonitor> logs, String filePath)
  {
    int id = -1;
    for (LogMonitor log : logs)
    {
      String path = log.location + log.file_prefix + "." + FileContentReader.FILE_EXTENSION;
      if (path.equals(filePath))
        return log.id;
    }

    return id;
  }

}
