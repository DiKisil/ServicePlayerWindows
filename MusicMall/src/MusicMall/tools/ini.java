/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicMall.tools;

/**
 *
 * @author Diana
 */
import java.io.File;
import java.io.IOException;
import org.ini4j.Ini;
import MusicMall.core.Main;

public class ini
{
  public static Ini createIni(File dir, String name)
    throws IOException
  {
    File inif = new File(dir.getAbsolutePath() + "/" + name + ".ini");
    inif.createNewFile();
    
    return new Ini(inif);
  }
  
  public static Ini getSettingsIni()
    throws Exception
  {
    return createIni(Main.getMusicMallPath(), ".MusicMall");
  }
  
  public static void putIni(Ini ini, String group, String key, String value)
    throws IOException
  {
    ini.put(group, key, value);
    ini.store();
  }
  
  public static String getIni(Ini ini, String optionName, String sectionName)
  {
    return ini.get(sectionName, optionName);
  }
  
  public static String getFromSettingsIni(String optionName)
    throws Exception
  {
    return getIni(getSettingsIni(), optionName, "Settings");
  }
  
}
