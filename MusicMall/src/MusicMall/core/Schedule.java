/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MusicMall.core;

/**
 *
 * @author Diana
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import MusicMall.Window.MainWindow;
import MusicMall.net.JSON;
import MusicMall.net.ftp;
import MusicMall.tools.Music;
import MusicMall.tools.PlayListItem;
import MusicMall.tools.Song;
import MusicMall.tools.adv;
import MusicMall.tools.date;
import MusicMall.tools.ini;
import org.ini4j.Ini;

public class Schedule implements Runnable {
    protected static Date start_music;
    protected static Date finish_music;
    protected static Date curBlockTime;
    protected static Date EndPlay;
    protected static List<Date> blocks_start;
    protected static List<Date> blocks_finish;
    static List<PlayListItem> PlayList;

    static {
        blocks_start = new ArrayList<Date>();
        blocks_finish = new ArrayList<Date>();
    }

    public static void updateSettings(String day) throws Exception {
        block12 : {
            System.out.println("Updating settings");
            MainWindow.setSynchronize((boolean)true);
            MainWindow.updateINFO((String)ini.getFromSettingsIni((String)"ClientID"), (String)ini.getFromSettingsIni((String)"ClientName"));
            try {
                if (!ftp.SynchronizeSettings((String)ini.getIni((Ini)ini.getSettingsIni(), (String)"JSONPath", (String)"Settings"), (int)Integer.parseInt(ini.getFromSettingsIni((String)"ClientID")))) {
                    throw new Exception();
                }
            }
            catch (Exception e1) {
                if (new File(String.valueOf(ini.getFromSettingsIni((String)"JSONPath")) + "\\Music.json").exists() && new File(String.valueOf(ini.getFromSettingsIni((String)"JSONPath")) + "\\Advertisement.json").exists()) break block12;
                System.out.println("No JSONs");
                throw new Exception();
            }
        }
        final ArrayList<Date> blocks_start_temp = new ArrayList<Date>();
        final ArrayList<Date> blocks_finish_temp = new ArrayList<Date>();
        int b = 1; 
        while (b <= 5) {
            Date start_music = new Date();
            Date finish_music = new Date();
            if (!JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)("time_start_" + b)).equals("null")) {
                try {
                    start_music.setHours(Integer.parseInt(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)("time_start_" + b)).substring(0, JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)("time_start_" + b)).indexOf(":")).replaceFirst("^0+(?!$)", "")));
                    start_music.setMinutes(Integer.parseInt(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)("time_start_" + b)).substring(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)("time_start_" + b)).indexOf(":") + 1, JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)("time_start_" + b)).length()).replaceFirst("^0+(?!$)", "")));
                    start_music.setSeconds(0);
                    finish_music.setHours(Integer.parseInt(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)("time_end_" + b)).substring(0, JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)("time_end_" + b)).indexOf(":")).replaceFirst("^0+(?!$)", "")));
                    finish_music.setMinutes(Integer.parseInt(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)("time_end_" + b)).substring(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)("time_end_" + b)).indexOf(":") + 1, JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)("time_end_" + b)).length()).replaceFirst("^0+(?!$)", "")));
                    finish_music.setSeconds(0);
                }
                catch (Exception e) {
                    throw new Exception();
                }
            } else {
                start_music = null;
                finish_music = null;
            }
            if (start_music != null & finish_music != null) {
                blocks_start_temp.add(start_music);
                blocks_finish_temp.add(finish_music);
            }
            ++b;
        }
        if (!blocks_start_temp.isEmpty() & !blocks_finish_temp.isEmpty()) {
            System.out.println("Adding blocks schedule");
            blocks_start = blocks_start_temp;
            blocks_finish = blocks_finish_temp;
        } else {
            System.out.println("No blocks");
        }
        System.out.println(String.valueOf(blocks_start.size()) + "  blocks");
        Date start_music_temp = new Date();
        start_music_temp.setHours(Integer.parseInt(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)"time_start").substring(0, JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)"time_start").indexOf(":")).replaceFirst("^0+(?!$)", "")));
        start_music_temp.setMinutes(Integer.parseInt(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)"time_start").substring(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)"time_start").indexOf(":") + 1, JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)"time_start").length()).replaceFirst("^0+(?!$)", "")));
        start_music_temp.setSeconds(0);
        Date finish_music_temp = new Date();
        finish_music_temp.setHours(Integer.parseInt(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)"time_finish").substring(0, JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)"time_finish").indexOf(":")).replaceFirst("^0+(?!$)", "")));
        finish_music_temp.setMinutes(Integer.parseInt(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)"time_finish").substring(JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)"time_finish").indexOf(":") + 1, JSON.getMusicVolumeJSONParam((String)ini.getFromSettingsIni((String)"JSONPath"), (String)day, (String)"time_finish").length()).replaceFirst("^0+(?!$)", "")));
        finish_music_temp.setSeconds(0);
        start_music = new Date(start_music_temp.getTime());
        finish_music = new Date(finish_music_temp.getTime());
        System.out.println("Start_music   " + start_music.toString());
        System.out.println("Finish_music   " + finish_music.toString());
        int i = 0;
        while (i < blocks_start.size() & i < blocks_finish.size()) {
            System.out.println(blocks_start.get(i));
            System.out.println(blocks_finish.get(i));
            ++i;
        }
        MainWindow.setSynchronize((boolean)false);
        System.out.println("Updating settings OK");
    }

    public static void updateMedia() throws Exception {
        System.out.println("Updating media");
        MainWindow.setSynchronize((boolean)true);
        Main.setSongs((List)Music.SynchronizeMusic((String)ini.getIni((Ini)ini.getSettingsIni(), (String)"MusicPath", (String)"Settings"), (String)ini.getIni((Ini)ini.getSettingsIni(), (String)"JSONPath", (String)"Settings")));
        Main.setAdvList((List)Music.SynchronizeAdv((String)ini.getIni((Ini)ini.getSettingsIni(), (String)"AdvPath", (String)"Settings"), (String)ini.getIni((Ini)ini.getSettingsIni(), (String)"JSONPath", (String)"Settings")));
        MainWindow.setSynchronize((boolean)false);
        System.out.println("Updating media OK");
    }
   public static void update(String day)
    {
        for(Boolean UpdateError = Boolean.valueOf(true); UpdateError.booleanValue(); MainWindow.setSynchronize(false))
        {
            MainWindow.setSynchronize(true);
            try
            {
                updateSettings(day);
                updateMedia();
                UpdateError = Boolean.valueOf(false);
            }
            catch(Exception e)
            {
                System.out.println("Error updating");
                if(!Main.checkInternetConnection())
                {
                    MainWindow.setWaitCon(true);
                    while(!Main.checkInternetConnection()) 
                        try
                        {
                            Thread.sleep(2000L);
                        }
                        catch(InterruptedException e1)
                        {
                            e1.printStackTrace();
                            Main.FatalError();
                        }
                    MainWindow.setWaitCon(false);
                }
            }
        }

    }
    @Override
    public void run()
  {
    final CountDownLatch sync = new CountDownLatch(1);
    Thread SynchronizeSettings = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          for (;;)
          {
            if (!Main.update)
            {
              Main.update = true;
              date.LogTime(); 
              MainWindow.setWaitCon(true);
              MainWindow.setSynchronize(true);
              Schedule.update(date.getDayofWeek());             
              sync.countDown();
              Main.update = false;
            }
            Thread.sleep(300000L); 
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
          Main.update = false;
        }
      }
    });
    SynchronizeSettings.start();
    try
    {
      sync.await();
    }
    catch (InterruptedException e2)
    {
      Main.FatalError();
    }
    int g = 0;
    for (;;)
    {
      if ((date.systemDate().before(finish_music) & date.systemDate().after(new Date(start_music.getTime() - 600000L)))) 
      {
        List<PlayListItem> pl = null;
        if (((g < blocks_start.size() ? 1 : 0) & (g < blocks_finish.size() ? 1 : 0)) != 0)
        {
          if ((EndPlay != null) && (EndPlay.after((Date)blocks_finish.get(g))))
          {
            g++;
          }
          else if (date.systemDate().before((Date)blocks_start.get(g)))
          {
            if ((g - 1 >= 0) && 
              (((Date)blocks_finish.get(g - 1)).after(new Date(date.systemDate().getTime() + 300000L))))
            {
              g--;
            }
            else if (start_music.getTime() - date.systemDate().getTime() < 600000L)
            {
              Boolean gplus = Boolean.valueOf(false);
              
              Date start = new Date(((Date)blocks_start.get(g)).getTime());
              System.out.println("Startus" + start.toString());
              if (start.before(start_music)) {
                start = new Date(start_music.getTime());
              }
              Date finish = new Date(((Date)blocks_finish.get(g)).getTime());
              if (start.after(finish))
              {
                gplus = Boolean.valueOf(true);
              }
              else
              {
                if (((Date)blocks_finish.get(g)).getTime() - ((Date)blocks_start.get(g)).getTime() > 1800000L)
                {
                  if (EndPlay != null)
                  {
                    if (EndPlay.after((Date)blocks_start.get(g)))
                    {
                      System.out.println("EndPlay after start  " + EndPlay.toString());
                      start = new Date(EndPlay.getTime() + 1000L);
                      if (finish.getTime() - EndPlay.getTime() > 1800000L) {
                        finish = new Date(start.getTime() + 1800000L);
                      } else {
                        gplus = Boolean.valueOf(true);
                      }
                    }
                    else
                    {
                      start = new Date(((Date)blocks_start.get(g)).getTime());
                      finish = new Date(start.getTime() + 1800000L);
                    }
                  }
                  else
                  {
                    start = new Date(((Date)blocks_start.get(g)).getTime());
                    finish = new Date(start.getTime() + 1800000L);
                  }
                }
                else
                {
                  if ((EndPlay != null) && 
                    (EndPlay.after((Date)blocks_start.get(g))))
                  {
                    System.out.println("EndPlay after start");
                    start = new Date(EndPlay.getTime() + 1000L);
                  }
                  gplus = Boolean.valueOf(true);
                }
                try
                {
                  if ((EndPlay != null) && (EndPlay.after((Date)blocks_finish.get(g))))
                  {
                    gplus = Boolean.valueOf(true);
                  }
                  else
                  {
                    Date CriricalStop = new Date(finish.getTime() + 900000L);
                    System.out.println("Startus1" + start.toString());
                    pl = createPlayList(start, finish, 
                      generateAdvBlockSchedule(start, finish), (List)Main.getSongs().get(g), 
                      Main.getAdvList(), 
                      Boolean.valueOf(JSON.getTime(ini.getFromSettingsIni("JSONPath"))), 
                      Integer.valueOf(Integer.parseInt(JSON.getMusicVolumeJSONParam(
                      ini.getFromSettingsIni("JSONPath"), date.getDayofWeek(), 
                      "adv_volume"))), 
                      CriricalStop);
                  }
                }
                catch (Exception e)
                {
                  log.writeLog("Error creating playlist for block " + (g + 1));
                  e.printStackTrace();
                }
              }
              if (gplus.booleanValue()) {
                g++;
              }
            }
          }
          else
          {
            System.out.println("No before");
            if (date.systemDate().before((Date)blocks_finish.get(g)))
            {
              Boolean gplus = Boolean.valueOf(false);
              Date start = new Date(date.systemDate().getTime() + 30000L);
              if (start.before(start_music)) {
                start = new Date(start_music.getTime());
              }
              Date finish = new Date(((Date)blocks_finish.get(g)).getTime());
              if (start.after(finish)) {
                gplus = Boolean.valueOf(true);
              }
              if (finish.getTime() - start.getTime() > 1800000L)
              {
                if (EndPlay != null)
                {
                  if (EndPlay.after((Date)blocks_start.get(g)))
                  {
                    System.out.println("EndPlay after start");
                    start = new Date(EndPlay.getTime() + 1000L);
                    if (finish.getTime() - EndPlay.getTime() > 1800000L) {
                      finish = new Date(start.getTime() + 1800000L);
                    } else {
                      gplus = Boolean.valueOf(true);
                    }
                  }
                  else
                  {
                    start = new Date(((Date)blocks_start.get(g)).getTime());
                    finish = new Date(start.getTime() + 1800000L);
                  }
                }
                else {
                  finish = new Date(start.getTime() + 1800000L);
                }
              }
              else
              {
                if ((EndPlay != null) && 
                  (EndPlay.after((Date)blocks_start.get(g))))
                {
                  System.out.println("EndPlay after start");
                  start = new Date(EndPlay.getTime() + 1000L);
                }
                gplus = Boolean.valueOf(true);
              }
              try
              {
                if ((EndPlay != null) && (EndPlay.after((Date)blocks_finish.get(g))))
                {
                  gplus = Boolean.valueOf(true);
                }
                else
                {
                  Date CriricalStop = new Date(finish.getTime() + 900000L);
                   pl = createPlayList(start, finish, generateAdvBlockSchedule(start, finish), (List)Main.getSongs().get(g), 
                      Main.getAdvList(), 
                      Boolean.valueOf(JSON.getTime(ini.getFromSettingsIni("JSONPath"))), 
                      Integer.valueOf(Integer.parseInt(JSON.getMusicVolumeJSONParam(
                      ini.getFromSettingsIni("JSONPath"), date.getDayofWeek(), 
                      "adv_volume"))), 
                      CriricalStop);
                }
              }
              catch (Exception e)
              {
                log.writeLog("Error creating playlist for block " + (g + 1));
                e.printStackTrace();
              }
              if (gplus.booleanValue()) {
                g++;
              }
            }
            else
            {
              System.out.println("After end of block");
              pl = null;
              g++;
            }
          }
          if (pl != null) {
            if (!pl.isEmpty())
            {
              if (date.systemDate().before(((PlayListItem)pl.get(pl.size() - 1)).getEnd_play()))
              {
                writeSchedule(pl);
                Thread plThread = new Thread(new Play(pl));
                final CountDownLatch plT = new CountDownLatch(1);
                new Timer().schedule(new TimerTask()
                
                  {
                      
                    @Override
                    public void run()
                    {
                      if (!Main.update)
                      {
                        Main.update = true;
                        try
                        {
                          Schedule.update(date.getDayofWeek());
                        }
                        catch (Exception e)
                        {
                          System.out.println("Update Error");
                          e.printStackTrace();
                        }
                        Main.update = false;
                      }
                      plT.countDown();
                    }
                  }, new Date(((PlayListItem)pl.get(pl.size() - 1)).getEnd_play().getTime() - 600000L));
                plThread.start();
                EndPlay = new Date(((PlayListItem)pl.get(pl.size() - 1)).getEnd_play().getTime());
                try
                {
                  plT.await();
                }
                catch (Exception e)
                {
                  log.writeLog("Error. Exception: " + e.getMessage());
                }
              }
            }
            else
            {
              System.out.println("pl is empty");
              EndPlay = date.systemDate();
            }
          }
        }
        else
        {
          try
          {
            Thread.sleep(10000L);
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
          }
        }
      }
      else
      {
        try
        {
          Thread.sleep(10000L);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
        g = 0;
      }
    }
  }

     private static List<PlayListItem> createPlayList(Date start, Date Defstop_time, List<Date> advBlockSchedule, List<Song> songs, List<adv> advList, Boolean getTime, Integer adv_volume, Date CriricalStop) {
        List<PlayListItem> PlayList = new ArrayList();
        System.out.println("Creating playlist");
        int curBlock = 1;
        Date curLentgh = new Date(start.getTime());
        System.out.println("Start  " + start.toString());
        System.out.println("Stop  " + Defstop_time.toString());
        System.out.println("CrircalStop  " + CriricalStop.toString());
        if(CriricalStop.before(Defstop_time)) {
            return PlayList;
        } else {
            Collections.sort(advList, new Comparator<adv>() {
                @Override
                public int compare(adv a1, adv a2) {
                    return a1.getName().compareTo(a2.getName());
                }
            });

            for(; curBlock <= advBlockSchedule.size() | curLentgh.before(Defstop_time); System.out.println("Adv block is  " + curBlock)) {
                System.out.println("Creating");
                double lentgh = 0.0D;

                Date d;
                int r;
                while(curBlock <= advBlockSchedule.size() && ((Date)advBlockSchedule.get(curBlock - 1)).before(curLentgh) | date.compareDate((Date)advBlockSchedule.get(curBlock - 1), curLentgh)) {
                    d = new Date(((Date)advBlockSchedule.get(curBlock - 1)).getTime());
                    System.out.println("1  " + d.toString());
                    if((curLentgh.after(d) | date.compareDate(curLentgh, d)) & (start.before(d) | date.compareDate(start, d))) {
                        System.out.println("2  " + d.toString());
                        r = 0;

                        for(int q = 0; q < advList.size(); ++q) {
                            adv adv = (adv)advList.get(q);
                            double advlentgh = Music.getmp3Lentgh(new File(adv.getLocalPath()));
                            Date advbegin = new Date();
                            Date advend = new Date();
                            advbegin.setTime(curLentgh.getTime() + 1000L);
                            if(r == 0 & getTime.booleanValue()) {
                                advbegin.setTime(d.getTime() + 1000L);
                            }

                            advend.setTime((long)((double)advbegin.getTime() + advlentgh * 1000.0D) + 1000L);
                            if(((Boolean)((adv)advList.get(q)).getCount().get(((Date)advBlockSchedule.get(curBlock - 1)).getMinutes() / 5)).booleanValue() & (((adv)advList.get(q)).getBegin().before(curLentgh) | date.compareDate(((adv)advList.get(q)).getBegin(), curLentgh)) & ((adv)advList.get(q)).getEnd().after(advend) & advend.before(CriricalStop)) {
                                if(r == 0 & getTime.booleanValue() & PlayList.size() != 0) {
                                    ((PlayListItem)PlayList.get(PlayList.size() - 1)).setEnd_play(new Date(d.getTime() - 1000L));
                                }

                                System.out.println(adv.getName());
                                System.out.println("Get time:  " + getTime);
                                System.out.println(advbegin.toString());
                                System.out.println(advend.toString());
                                PlayList.add(new PlayListItem(adv.getName(), "adv", adv.getLocalPath(), advbegin, advend, advlentgh, (double)adv_volume.intValue()));
                                curLentgh = advend;
                                System.out.println(advend.toString());
                                System.out.println(adv.getName() + "   end");
                                ++r;
                            }
                        }
                    }

                    ++curBlock;
                    System.out.println("CurBlock: " + curBlock);
                }

                System.out.println("curLentgh  " + curLentgh.toString());
                if(curLentgh.before(Defstop_time)) {
                    Collections.shuffle(songs, new Random());
                    System.out.println("Generating song");
                    int bestSong = -1;
                    System.out.println("Songs:" + songs.size());

                    for(r = 0; r < songs.size(); ++r) {
                        if(Main.lpl.isPlayed(((Song)songs.get(r)).getName()) != null) {
                            System.out.println(Main.lpl.isPlayed(((Song)songs.get(r)).getName()));
                        } else {
                            System.out.println("LastPlayed == null");
                        }

                        if(Main.lpl.isPlayed(((Song)songs.get(r)).getName()) == null) {
                            bestSong = r;
                            r = songs.size();
                        }

                        System.out.println(r);
                    }

                    if(bestSong == -1) {
                        bestSong = Main.lpl.getBestSong(songs);
                    }

                    System.out.println("Bestsong is  " + bestSong);
                    Song s = (Song)songs.get(bestSong);
                    lentgh = Music.getmp3Lentgh(new File(s.getLocalPath()));
                    Date begin = date.systemDate();
                    begin.setTime(curLentgh.getTime() + 1000L);
                    Date end = new Date();
                    end.setTime((long)((double)begin.getTime() + lentgh * 1000.0D + 2000.0D));
                    System.out.println("Song start " + begin.toString());
                    System.out.println("Song end " + end.toString());
                    if(!end.before(CriricalStop) && curBlock <= advBlockSchedule.size()) {
                        end.setTime(CriricalStop.getTime());
                    }

                    PlayList.add(new PlayListItem(s.getName(), "Song", s.getLocalPath(), begin, end, lentgh, s.getVolume()));
                    Main.lpl.addSong(s.getName(), end);
                    curLentgh = new Date(end.getTime());
                }

                if(curLentgh.after(Defstop_time)) {
                    d = new Date(((Date)advBlockSchedule.get(advBlockSchedule.size() - 1)).getTime());
                    d.setMinutes(d.getMinutes() + 5);
                    System.out.println(d.toString());
                    if(curLentgh.after(d) | date.compareDate(curLentgh, d)) {
                        advBlockSchedule.add(d);
                        System.out.println("Adv block " + d.toString() + "  added");
                    }
                }
            }

            return PlayList;
        }
    }

    public static void writeSchedule(List<PlayListItem> PlayList) {
        log.writeLog((String)"");
        log.writeLog((String)"---------------------------------------------------------------------");
        log.writeLog((String)"");
        log.writeLog((String)"Schedule:");
        log.writeLog((String)"");
        log.writeLog((String)("Start   " + PlayList.get(0).getBegin_play()));
        log.writeLog((String)("Stop   " + PlayList.get(PlayList.size() - 1).getEnd_play()));
        log.writeLog((String)"");
        int j = 0;
        while (j < PlayList.size()) {
            PlayListItem p = PlayList.get(j);
            log.writeLog((String)"");
            log.writeLog((String)Integer.toString(j));
            log.writeLog((String)(String.valueOf(p.getType()) + "   " + p.getName()));
            log.writeLog((String)("Start time:  " + p.getBegin_play().toString()));
            log.writeLog((String)("End time:  " + p.getEnd_play().toString()));
            ++j;
        }
        log.writeLog((String)"");
        log.writeLog((String)"---------------------------------------------------------------------");
        log.writeLog((String)"");
    }

    public static List<Date> generateAdvBlockSchedule(Date Start_Time, Date Stop_Time) {
        System.out.println("Adv start time  " + Start_Time.toString());
        System.out.println("Adv end time  " + Stop_Time.toString());
        ArrayList<Date> advBlockSchedule = new ArrayList<Date>();
        Date temp = new Date(date.systemDate().getTime());
        temp.setHours(0);
        temp.setMinutes(0);
        temp.setSeconds(0);
        int y = 0;
        while (y < 288) {
            Date d = new Date(temp.getTime());
            d.setMinutes(y * 5);
            d.setSeconds(0);
            if (d.after(Start_Time) & d.before(Stop_Time)) {
                advBlockSchedule.add(d);
                System.out.println("Adv block  " + d.toString());
            } else if (d.getYear() == Start_Time.getYear() & d.getMonth() == Start_Time.getMonth() & d.getDate() == Start_Time.getDate() & d.getHours() == Start_Time.getHours() & d.getMinutes() == Start_Time.getMinutes() & d.getSeconds() == Start_Time.getSeconds() | d.getYear() == Stop_Time.getYear() & d.getMonth() == Stop_Time.getMonth() & d.getDate() == Stop_Time.getDate() & d.getHours() == Stop_Time.getHours() & d.getMinutes() == Stop_Time.getMinutes() & d.getSeconds() == Stop_Time.getSeconds()) {
                advBlockSchedule.add(d);
                System.out.println("Adv block  " + d.toString());
            }
            ++y;
        }
        return advBlockSchedule;
    }
}