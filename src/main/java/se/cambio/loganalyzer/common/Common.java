package se.cambio.loganalyzer.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

import se.cambio.loganalyzer.config.ConfigManager;

public class Common
{
  private static Common instance = null;

  private Common()
  {

  }

  private static Common getInstance()
  {
    if (instance == null)
      instance = new Common();
    return instance;
  }

  public static boolean canRun(int keyID,ConfigManager configManager)
  {
    boolean isActive = false;
    boolean canRun = false;
    if (configManager.getPorpsValue(keyID) == null || configManager.getPorpsValue(keyID).equals(""))
      isActive = true;
    if (isActive || Boolean.parseBoolean(configManager.getPorpsValue(keyID)))
      canRun = true;
    return canRun;
  }

  public static String setUpTempDirs(String fileName)
  {
    String tempDir = System.getProperty("java.io.tmpdir");
    File file = new File(tempDir, fileName);

    ClassLoader classLoader = getInstance().getClass().getClassLoader();
    File fileSource = new File(classLoader.getResource(fileName).getFile());
    InputStream is = null;

    System.out.println(
        "------------------------------Location Befaore:- " + fileSource.getPath() + " -----------------------");
    try
    {
      fileSource = getJarDir(Common.class);
      File fileee = new File(fileSource, fileName);
      System.out
          .println("------------------------------Location:- " + fileSource.getPath() + " -----------------------");
      System.out.println("------------------------------Exists:- " + fileee.getPath() + "  " + fileee.exists()
          + " -----------------------");
      is = new FileInputStream(fileSource);
    }
    catch (FileNotFoundException e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    try
    {
      Files.copy(is, file.getAbsoluteFile().toPath());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return tempDir;
  }

  public static File getJarDir(@SuppressWarnings("rawtypes") Class aclass)
  {
    URL url;
    String extURL; //  url.toExternalForm();

    // get an url
    try
    {
      url = aclass.getProtectionDomain().getCodeSource().getLocation();

    }
    catch (SecurityException ex)
    {
      url = aclass.getResource(aclass.getSimpleName() + ".class");
      // url is in one of two forms, both ending "/com/physpics/tools/ui/PropNode.class"
      //          file:/U:/Fred/java/Tools/UI/build/classes
      //          jar:file:/U:/Fred/java/Tools/UI/dist/UI.jar!
    }

    // convert to external form
    extURL = url.toExternalForm();

    try
    {
      url = new URL(extURL);
    }
    catch (MalformedURLException mux)
    {
      // leave url unchanged; probably does not happen
    }

    // convert url to File
    try
    {
      return new File(url.toURI());
    }
    catch (URISyntaxException ex)
    {
      return new File(url.getPath());
    }
  }

  public static void main(String args[])
  {
    System.out.println("File Dir:-" + setUpTempDirs("config.properties"));
  }
}
