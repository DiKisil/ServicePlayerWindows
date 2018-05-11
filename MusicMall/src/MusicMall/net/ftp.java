/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicMall.net;

/**
 *
 * @author Diana
 */
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import MusicMall.core.Main;
import MusicMall.core.log;

public class ftp {
    protected static final String ftpServer = "musicmall.com.ua";
    protected static final String ftpUser = "mm_ce";
    protected static final String ftpPass = "mm_ce2013";
    protected static final int ftpPort = 21;

    public ftp() {
    }

   public static FTPClient ftpConnect() throws Exception {
        FTPClient client = new FTPClient();
        try {
            client.connect("musicmall.com.ua", 21);
            client.login("mm_ce", "mm_ce2013");
            System.out.println("Connected to the ftp server   musicmall.com.ua  21");
        }
        catch (Exception e) {
            System.out.println("Ftp connect error");
            throw new Exception();
        }
        return client;
    }

    public static List<String> listFilesOnServer(FTPClient client, String path) throws Exception {
        System.out.println("Listing files in path:  " + path);
        if (path == null) {
            return Arrays.asList(client.listNames());
        }
        FTPFile[] lists = client.list(path);
        ArrayList<String> list = new ArrayList<String>();
        int i = 0;
        while (i < lists.length) {
            list.add(String.valueOf(path) + lists[i].getName());
            ++i;
        }
        return list;
    }

    public static boolean downloadFromFTP(FTPClient ftpClient, String fileName, String remotePath, String localPath) throws Exception {
        new File(String.valueOf(localPath) + "\\").mkdirs();
        File f = new File(String.valueOf(localPath) + "\\" + fileName);
        try {
            ftpClient.download(String.valueOf(remotePath) + "/" + fileName, f);
            log.writeLog((String)("File succesfully downloaded. Name: " + fileName));
        }
        catch (Exception e) {
            log.writeLog((String)("File download error. Name: " + fileName));
            return false;
        }
        return true;
    }

    public static boolean downloadListFromFTP(List<String> files, String localPath)
  {
    try
    {
      it.sauronsoftware.ftp4j.FTPClient ftpClient = null;
      Boolean OfflineMode = Boolean.valueOf(true);
      if (!Main.checkInternetConnection())
      {
        OfflineMode = Boolean.valueOf(true);
      }
      else
      {
        ftpClient = ftpConnect();
        OfflineMode = Boolean.valueOf(false);
      }
      for (int i = 0; i < files.size(); i++)
      {
        File f = new File(localPath + "\\" + ((String)files.get(i)).substring(((String)files.get(i)).lastIndexOf("/")));
        if (!f.exists())
        {
          if (OfflineMode.booleanValue()) {
            return false;
          }
          try
          {
            ftpClient.download((String)files.get(i), f);
            log.writeLog("File succesfully downloaded. Name: " + 
              ((String)files.get(i)).substring(((String)files.get(i)).lastIndexOf("/")));
          }
          catch (Exception e)
          {
            log.writeLog(
              "File download error. Name: " + ((String)files.get(i)).substring(((String)files.get(i)).lastIndexOf("/")));
          }
        }
        else if (f.length() != 0L)
        {
          if (!OfflineMode.booleanValue())
          {
            Long reply = null;
            try
            {
              reply = Long.valueOf(ftpClient.fileSize((String)files.get(i)));
            }
            catch (Exception e2) {}
            while (reply == null) {
              try
              {
                ftpClient = ftpConnect();
                reply = Long.valueOf(ftpClient.fileSize((String)files.get(i)));
              }
              catch (Exception e)
              {
                System.out.println("Error connecting");
                return false;
              }
            }
            if (reply.longValue() != f.length())
            {
              System.out.println("Incorrect file already exists. Name: " + 
                ((String)files.get(i)).substring(((String)files.get(i)).lastIndexOf("/")));
              try
              {
                ftpClient.download((String)files.get(i), f);
                log.writeLog("File succesfully downloaded. Name: " + 
                  ((String)files.get(i)).substring(((String)files.get(i)).lastIndexOf("/")));
              }
              catch (Exception e)
              {
                log.writeLog(
                  "File download error. Name: " + ((String)files.get(i)).substring(((String)files.get(i)).lastIndexOf("/")));
                return false;
              }
            }
            else
            {
              System.out.println("File already exists. Name: " + 
                ((String)files.get(i)).substring(((String)files.get(i)).lastIndexOf("/")));
            }
          }
        }
        else if (!OfflineMode.booleanValue())
        {
          try
          {
            ftpClient.download((String)files.get(i), f);
            log.writeLog("File succesfully downloaded. Name: " + 
              ((String)files.get(i)).substring(((String)files.get(i)).lastIndexOf("/")));
          }
          catch (Exception e)
          {
            System.out.println("File download error. Name: " + 
              ((String)files.get(i)).substring(((String)files.get(i)).lastIndexOf("/")));
            return false;
          }
        }
        else
        {
          return false;
        }
        Thread.sleep(10L);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return false;
    }
    return true;
  }

     public static boolean SynchronizeSettings(String dir, int ClientID) throws Exception {
        log.writeLog((String)"Synchronizing settings started");
        String remotePath = "/set/" + ClientID;
        FTPClient client = ftp.ftpConnect();
        if (!ftp.downloadFromFTP(client, "Advertisement.json", remotePath, dir) || !ftp.downloadFromFTP(client, "Music.json", remotePath, dir)) {
            log.writeLog((String)"Synchronizing settings ERROR");
            return false;
        }
        log.writeLog((String)"Synchronizing settings OK");
        return true;
    }
  
  public static boolean uploadFile(File f, String remotePath)
  {
    log.writeLog("Uploading file. Name: " + f.getName());
    try
    {
      org.apache.commons.net.ftp.FTPClient client = new org.apache.commons.net.ftp.FTPClient();
      client.connect("musicmall.com.ua", 21);
      client.login("mm_ce", "mm_ce2013");
      client.enterLocalPassiveMode();
      client.setFileType(2);
      
      InputStream local = new FileInputStream(f);
      if (!client.storeFile(remotePath + "//" + f.getName(), local))
      {
        log.writeLog("File upload error. Name: " + f.getName());
        return false;
      }
      local.close();
      client.disconnect();
      log.writeLog("File succesfully uploaded. Name: " + f.getName());
    }
    catch (Exception e)
    {
      log.writeLog("File upload error. Name: " + f.getName());
      return false;
    }
    return true;
  }

    public static String getFtpserver() {
        return "musicmall.com.ua";
    }

    public static String getFtpuser() {
        return "mm_ce";
    }

    public static String getFtppass() {
        return "mm_ce2013";
    }

    public static int getFtpport() {
        return 21;
    }
}


