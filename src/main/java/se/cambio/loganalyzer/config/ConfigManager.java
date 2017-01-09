package se.cambio.loganalyzer.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager
{
  public Properties properties;

  public String filePath = "";

  public String file_name = "";

  public ConfigManager(String filePath, String file_name)
  {
    this.filePath = filePath;
    this.file_name = file_name;
    try
    {
      properties = readPropertyFile();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

  }

  public Properties readPropertyFile() throws IOException
  {
    properties = new Properties();
    InputStream input;

    File file = new File(getFilePath(filePath, file_name));
    if (!file.exists())
    {
      throw new FileNotFoundException("The Property File " + file_name + " is not in " + filePath);
    }
    input = new FileInputStream(file);
    properties.load(input);

    return properties;
  }

  public String getFilePath(String filePath, String file_name)
  {
    if (filePath.lastIndexOf(File.separator) != filePath.length() - 1)
      return filePath + File.separator + getFileName(file_name);
    else
      return filePath + getFileName(file_name);
  }

  public String getPorpsValue(int i)
  {
    if (properties.containsKey(Configuration.CONFIG_PROPERTY_FILE_KEYS[i]))
      return properties.get(Configuration.CONFIG_PROPERTY_FILE_KEYS[i]).toString();
    else
      return "";
  }

  public String getFileName(String file_name)
  {
    return file_name.equals("") || file_name.equals(null) ? Configuration.CONFIGURATION_FILE_NAME : file_name;
  }

  public void updateConfigFile(String keyName, String keyValue)
  {
    FileOutputStream out = null;

    try
    {
      out = new FileOutputStream(filePath + File.separator + file_name);
      properties.setProperty(keyName, keyValue);
      properties.store(out, null);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (out != null)
        try
        {
          out.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
    }
  }
}
