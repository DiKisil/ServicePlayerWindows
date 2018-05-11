/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicMall.core;

/**
 *
 * @author Diana 2017
 */
import it.sauronsoftware.ftp4j.FTPClient;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javax.swing.JOptionPane;
import MusicMall.Window.MainWindow;
import MusicMall.net.ftp;
import MusicMall.tools.LastPlayedList;
import MusicMall.tools.Song;
import MusicMall.tools.adv;
import MusicMall.tools.ini;



public class Main
{
  protected static List<List<Song>> songs;
  protected static List<adv> advList;
  public static LastPlayedList lpl = new LastPlayedList();
  protected static String root = System.getProperty("user.dir");
  public static boolean playing = false;
  public static boolean update = false;
  static final String version = "1.00";
  

  
  
  public static void main(String[] args)
    throws IOException
  {
    try
    {
      if ((ini.getFromSettingsIni("FirstStart") == null) || (ini.getFromSettingsIni("FirstStart").equals("1"))) //this 
      {
        JOptionPane.showMessageDialog(null, "Не заданы начальные настройки. Обратитесь к администратору.", 
          "Ошибка", 0);
        System.exit(0);
      }
    }
    catch (Exception e1)
    {
      e1.printStackTrace();
      System.exit(0);
    }
   // log.writeLog("");
    //log.writeLog("---------------------------------------------------------------------");
    //log.writeLog("---------------------------------------------------------------------");
    //log.writeLog("");
    
    log.writeLog("Player started");
    try
    {
      Runtime.getRuntime().exec("netsh advfirewall set global StatefulFTP disable");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("Error changing firewall rules");
    }
    Thread fx = new Thread(new MainWindow());
    fx.start();
   
  }
  
  public static void FatalError()
  {
    log.writeLog("FATAL ERROR. PROGRAMM WAS CRASHED");
    if (checkInternetConnection()) {
      log.synchronizeLogwithServer();
    }
    System.exit(0);
  }
  
  public static void deleteDirectory(File dir)
  {
    if (dir.isDirectory())
    {
      String[] children = dir.list();
      for (int i = 0; i < children.length; i++)
      {
        File f = new File(dir, children[i]);
        deleteDirectory(f);
      }
      dir.deleteOnExit();
    }
    else
    {
      dir.deleteOnExit();
    }
  }
  
  public static boolean checkInternetConnection()
  {
    System.out.println("Checking connection");
    HttpURLConnection con = null;
    try
    {
      con = (HttpURLConnection)new URL("http://google.com/").openConnection();
      con.setRequestMethod("HEAD");
      if (con.getResponseCode() != 200) {
        return false;
      }
    }
    catch (Exception e)
    {
      return false;
    }
    finally
    {
      if (con != null) {
        try
        {
          con.disconnect();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
    if (con != null) {
      try
      {
        con.disconnect();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    try
    {
      FTPClient client = new FTPClient();
      client.setPassive(true);
      client.connect(ftp.getFtpserver(), ftp.getFtpport());
      client.login(ftp.getFtpuser(), ftp.getFtppass());
      if (client.listNames().length == 0) {
        return false;
      }
    }
    catch (Exception e)
    {
      log.writeLog("Problems with internet connection. Cant connect to ftp server.");
      
      return false;
    }
    return true;
  }
  
  public static File getMusicMallPath()
  {
    File x64 = new File(System.getenv("SystemDrive") + "//Program Files (x86)//MusicMall");
    File x86 = new File(System.getenv("SystemDrive") + "//Program Files//MusicMall");
    if (x64.exists()) {
      return x64;
    }
    if (x86.exists()) {
      return x86;
    }
    JOptionPane.showMessageDialog(null, "Не найдена папка установки программы.", "Ошибка", 0);
    
    return null;
  }
  
  
  public static List<List<Song>> getSongs()
  {
    return songs;
  }
  
  public static void setSongs(List<List<Song>> songs)
  {
    Main.songs = songs;
  }
  
  public static List<adv> getAdvList()
  {
    return advList;
  }
  
  public static void setAdvList(List<adv> advList)
  {
    Main.advList = advList;
  }
  
}