/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicMall.Window;

/**
 *
 * @author Diana
 */
import java.net.URL;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;
import org.ini4j.Ini;
import MusicMall.core.Main;
import MusicMall.core.Schedule;
import MusicMall.core.log;
import MusicMall.tools.ini;

public class MainWindow
  extends Application
  implements Runnable
{
  protected static Label Synchronize;
    protected static Label WaitCon;
    protected static ProgressIndicator Progress;
    protected static Text IDField;
    protected static Boolean SynchronizeStatus;
    protected static Boolean WaitConStatus;
    protected static Boolean ProgressStatus;

    static {
        SynchronizeStatus = false;
        WaitConStatus = false;
        ProgressStatus = false;
    }
  
  @Override
  public void start(Stage primaryStage)
    throws Exception
  {
    Parent panel = (Parent)FXMLLoader.load(MainWindow.class.getResource("/MusicMall/fxml/MainWindow.fxml"));
    Scene scene = new Scene(panel);
    primaryStage.setResizable(false);
    primaryStage.setTitle("Music Mall");
    primaryStage.setScene(scene);
    primaryStage.getIcons()
      .add(new Image(MainWindow.class.getResource("/MusicMall/res/ic_launcher.png").openStream()));
    Synchronize = (Label)scene.lookup("#Synchronize");
    WaitCon = (Label)scene.lookup("#WaitCon");
    Progress = (ProgressIndicator)scene.lookup("#SynchronizeProgress");
    IDField = (Text)scene.lookup("#IDField");
    MainWindow.updateINFO(ini.getFromSettingsIni((String)"ClientID"), ini.getFromSettingsIni((String)"ClientName"));
    
    Thread UpdateGUI = new Thread(new Runnable()
    {
      @Override  
      public void run()
      {
        MainWindow.WaitCon.setVisible(MainWindow.WaitConStatus.booleanValue());
        MainWindow.Synchronize.setVisible(MainWindow.SynchronizeStatus.booleanValue());
        MainWindow.Progress.setVisible(MainWindow.ProgressStatus.booleanValue());
        try
        {
          Thread.sleep(1000L);
        }
        catch (InterruptedException e)
        {
          System.out.println("Error updating gui");
          e.printStackTrace();
          Main.FatalError();
        }
      }
    });
    primaryStage.show();
    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
    {
      @Override
      public void handle(WindowEvent event)
      {
        event.consume();
      }
    });
    UpdateGUI.start();
    
    Ini inI = ini.getSettingsIni();
    if (inI.get((Object)"Settings", (Object)"FolderPath") == null || inI.get((Object)"Settings", (Object)"ClientID") == null || inI.get((Object)"Settings", (Object)"ClientName") == null && inI.get((Object)"Settings", (Object)"ClientName").length() == 0 || inI.get((Object)"Settings", (Object)"ClientID").length() == 0 || inI.get((Object)"Settings", (Object)"FolderPath").length() == 0) 
    {
      JOptionPane.showMessageDialog(null, "Не заданы начальные настройки. Обратитесь к администратору", 
        "Ошибка", 0);
      System.exit(0);
    }
    else
    {
      inI.load();
    }
    log.writeLog((String)("Client ID: " + ini.getFromSettingsIni((String)"ClientID") + ". Client Name: " + ini.getFromSettingsIni((String)"ClientName") + "."));
        new Thread((Runnable)new log(ini.getFromSettingsIni((String)"LogPath"), ini.getFromSettingsIni((String)"ClientID"))).start();
        Thread Schedule = new Thread((Runnable)new Schedule());
        Schedule.start();
  }
  
 public static void setWaitCon(final boolean WaitCon) {
        System.out.println("Toggle WaitCon");
        MainWindow.SynchronizeStatus = false;
        MainWindow.WaitConStatus = WaitCon;
        MainWindow.ProgressStatus = WaitCon;
        MainWindow.Synchronize.setVisible(false);
        MainWindow.WaitCon.setVisible(WaitCon);
        MainWindow.Progress.setVisible(WaitCon);
    }
    
    public static void setSynchronize(final boolean Synchronize) {
        System.out.println("Toggle Synchronize");
        MainWindow.WaitConStatus = false;
        MainWindow.SynchronizeStatus = Synchronize;
        MainWindow.ProgressStatus = Synchronize;
        MainWindow.WaitCon.setVisible(false);
        MainWindow.Synchronize.setVisible(Synchronize);
        MainWindow.Progress.setVisible(Synchronize);
    }
    
    public static void updateINFO(final String ClientID, final String ClientName) {
        if (MainWindow.IDField != null && ClientID != null && ClientName != null) {
            String temp = ClientName;
            if (temp.length() >= 40) {
                temp = temp.substring(0, 40);
                temp = String.valueOf(temp) + "...";
                System.out.println(temp);
            }
            MainWindow.IDField.setText(String.valueOf(ClientID) + "  " + temp);
        }
    }
    
    public static URL getResourse() {
        return MainWindow.class.getResource("../fxml/MainWindow.fxml");
    }
    
  @Override
    public void run() {
        launch(new String[0]);
    }
}