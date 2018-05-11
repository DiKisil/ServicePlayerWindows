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
import java.io.PrintStream;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import MusicMall.tools.PlayListItem;

public class Play
implements Runnable {
    static Date d = new Date();
    static MediaPlayer mediaPlayer;
    static MediaPlayer advPlayer;
    static Date start_music;
    static Date finish_music;
    static Date adv_time;
    static Date stop_time;
    static Date start;
    static Date Defstop_time;
    static int advCounter = 0;
    private static List<PlayListItem> PlayList;

    

    public Play(List<PlayListItem> PlayList) {
        System.out.println("Play");
        Play.PlayList = PlayList;
    }

     public static void playlist(List<PlayListItem> PlayList) {
        int i = 0;
        for (i = 0; i < PlayList.size(); i++) {
            final PlayListItem pli = (PlayListItem)PlayList.get(i);
            if(d.before(((PlayListItem)PlayList.get(i)).getEnd_play())) {
                final CountDownLatch l;
                if(d.after(((PlayListItem)PlayList.get(i)).getBegin_play())) {
                    l = new CountDownLatch(1);
                    Media media = new Media((new File(pli.getPath())).toURI().toASCIIString());
                    Play.mediaPlayer = new MediaPlayer(media);
                    Play.mediaPlayer.setVolume(pli.getVolume());
                    Play.mediaPlayer.play();
                    log.writeLog("");
                    log.writeLog("Playing " + pli.getType() + "   " + pli.getName());
                    log.writeLog("");
                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                            Play.mediaPlayer.stop();
                            l.countDown();
                        }
                    };
                    Timer t = new Timer();
                    t.schedule(tt, pli.getEnd_play());

                    try {
                        System.out.println("l wait");
                        l.await();
                        log.writeLog("");
                        log.writeLog("End playing " + pli.getName());
                        log.writeLog("");
                    } catch (InterruptedException var8) {
                        var8.printStackTrace();
                    }
                } else {
                    l = new CountDownLatch(1);
                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                Media media = new Media((new File(pli.getPath())).toURI().toASCIIString());
                                Play.mediaPlayer = new MediaPlayer(media);
                                Play.mediaPlayer.setVolume(pli.getVolume());
                                System.out.println("Playing");
                                Play.mediaPlayer.play();
                                log.writeLog("");
                                log.writeLog("Playing " + pli.getType() + "   " + pli.getName());
                                log.writeLog("");
                                TimerTask tt = new TimerTask() {
                                    @Override
                                    public void run() {
                                        Play.mediaPlayer.stop();
                                        System.out.println("Stopped");
                                        l.countDown();
                                    }
                                };
                                Timer t = new Timer();
                                t.schedule(tt, pli.getEnd_play());
                            } catch (Exception var5) {
                                log.writeLog("Error playing " + pli.getType() + "   " + pli.getName());
                                log.writeLog("Exception: " + var5.getMessage());
                                l.countDown();
                            }

                            try {
                                l.await();
                                log.writeLog("");
                                log.writeLog("End playing " + pli.getName());
                                log.writeLog("");
                            } catch (Exception var4) {
                                var4.printStackTrace();
                            }

                        }
                    };
                    Timer t = new Timer();
                    t.schedule(tt, pli.getBegin_play());

                    try {
                        System.out.println("l1 wait");
                        l.await();
                    } catch (InterruptedException var9) {
                        var9.printStackTrace();
                    }
                }
            } else {
                System.out.println("Too late. Next track");
            }
        }

    }

    @Override
    public void run() {
        if (PlayList != null) {
            Main.playing = true;
            playlist(PlayList);
            Main.playing = false;
            return;
        }
        System.out.println("Playlist == null");
    }
}