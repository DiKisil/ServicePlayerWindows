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
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.AudioFileIO;
import it.sauronsoftware.ftp4j.FTPClient;
import java.io.IOException;
import MusicMall.core.Main;
import MusicMall.net.ftp;
import java.io.File;
import java.util.ArrayList;
import MusicMall.net.JSON;
import MusicMall.core.log;
import java.util.List;

public class Music
{
    public static List<adv> SynchronizeAdv(final String dir, final String json) throws Exception {
        log.writeLog("Synchronizing advertisement started");
        final List<adv> advList = (List<adv>)JSON.parseAdvJSON(ini.getIni(ini.getSettingsIni(), "JSONPath", "Settings"));
        final List<String> files = new ArrayList<String>();
        new File(dir).mkdirs();
        for (int i = 0; i < advList.size(); ++i) {
            advList.get(i).setRemotePath("adv/" + advList.get(i).getName());
            files.add(advList.get(i).getRemotePath());
        }
        if (!ftp.downloadListFromFTP((List)files, dir)) {
            throw new Exception();
        }
        for (int i = 0; i < advList.size(); ++i) {
            advList.get(i).setLocalPath(String.valueOf(dir) + "\\" + advList.get(i).getName());
        }
        try {
            if (!Main.playing) {
                if (advList.isEmpty()) {
                    log.writeLog("Empty adv list");
                    return advList;
                }
                log.writeLog("Synchronizing advertisement OK");
                final File[] listFiles = new File(ini.getFromSettingsIni("AdvPath")).listFiles();
                for (int a = 0; a < listFiles.length; ++a) {
                    final File f = listFiles[a];
                    Boolean del = true;
                    for (int k = 0; k < advList.size(); ++k) {
                        if (advList.get(k).getLocalPath().equals(f.getAbsolutePath())) {
                            del = false;
                            k = advList.size();
                        }
                    }
                    if (del) {
                        f.delete();
                        log.writeLog("Adv " + f.getName().trim() + " deleted.");
                    }
                }
            }
        }
        catch (Exception e) {
            log.writeLog("Error deleting adv");
        }
        return advList;
    }
    
      public static List<List<Song>> SynchronizeMusic(final String dir, final String json) throws Exception {
        log.writeLog("Synchronizing music started");
        System.out.println("DIR:  " + dir);
        final List<List<Song>> blocks = new ArrayList<List<Song>>();
        List<ScheduleBlock> list = new ArrayList<ScheduleBlock>();
        try {
            list = (List<ScheduleBlock>)JSON.parseMusicJSON(json, date.getDayofWeek());
        }
        catch (IOException e) {
            System.out.println("Error parsing Music JSON");
            e.printStackTrace();
        }
        if (list.isEmpty()) {
            System.out.println("No blocks for this day");
            return blocks;
        }
        double musicVolume;
        try {
            musicVolume = JSON.parseMusicVolumeJSON(json, date.getDayofWeek());
        }
        catch (IOException e2) {
            e2.printStackTrace();
            musicVolume = 100.0D;
        }
        try {
            final FTPClient ftpclient = ftp.ftpConnect();
            final List<String> downloaded = new ArrayList<String>();
            final List<String> localMusic = new ArrayList<String>();
            for (int f = 0; f < list.size(); ++f) {
                final ScheduleBlock musicList = list.get(f);
                final List<Song> songs = new ArrayList<Song>();
                final List<String> files = new ArrayList<String>();
                for (int i = 0; i < musicList.getFolders().size(); ++i) {
                    final List<String> temp = (List<String>)ftp.listFilesOnServer(ftpclient, "music/" + musicList.getFolders().get(i) + "/");
                    for (int q = 0; q < temp.size(); ++q) {
                        final String item = temp.get(q);
                        final Song s = new Song();
                        s.setName(temp.get(q).substring(item.lastIndexOf("/") + 1, item.length()));
                        s.setVolume(musicVolume);
                        s.setType((String)musicList.getFolders().get(i));
                        s.setLocalPath(String.valueOf(dir) + "\\" + musicList.getFolders().get(i) + "\\" + temp.get(q).substring(item.lastIndexOf("/") + 1, item.length()));
                        localMusic.add(new File(s.getLocalPath()).getAbsolutePath());
                        s.setRemotePath((String)temp.get(q));
                        songs.add(s);
                        boolean down = false;
                        for (int h = 0; h < downloaded.size(); ++h) {
                            if (downloaded.get(h).equals(temp.get(q))) {
                                down = true;
                                h = downloaded.size();
                            }
                        }
                        if (!down) {
                            downloaded.add(temp.get(q));
                            files.add(temp.get(q));
                        }
                    }
                    new File(String.valueOf(dir) + "\\" + musicList.getFolders().get(i)).mkdirs();
                    System.out.println(files.size());
                    if (!ftp.downloadListFromFTP((List)files, String.valueOf(dir) + "\\" + musicList.getFolders().get(i))) {
                        throw new Exception();
                    }
                    files.clear();
                    log.writeLog("Synchronizing folder " + musicList.getFolders().get(i) + " OK");
                }
                if (!ftp.downloadListFromFTP((List)files, dir)) {
                    throw new Exception();
                }
                if (!songs.isEmpty()) {
                    blocks.add(songs);
                }
                else {
                    try {
                        log.writeLog("Synchronizing music ERROR");
                        throw new Exception();
                    }
                    catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            }
            log.writeLog("Synchronizing music OK");
            try {
                if (!Main.playing) {
                    final File[] listFolders = new File(ini.getFromSettingsIni("MusicPath")).listFiles();
                    for (int a = 0; a < listFolders.length; ++a) {
                        final File f2 = listFolders[a];
                        if (f2.isDirectory()) {
                            final File[] listFiles = f2.listFiles();
                            for (int e4 = 0; e4 < listFiles.length; ++e4) {
                                final File f3 = listFiles[e4];
                                Boolean del = true;
                                for (int k = 0; k < localMusic.size(); ++k) {
                                    if (localMusic.get(k).equals(f3.getAbsolutePath())) {
                                        del = false;
                                        k = localMusic.size();
                                    }
                                }
                                if (del) {
                                    f3.delete();
                                    log.writeLog("Song " + f3.getName().trim() + " deleted.");
                                }
                            }
                            if (f2.listFiles().length == 0) {
                                f2.delete();
                            }
                        }
                    }
                    return blocks;
                }
                return blocks;
            }
            catch (IOException e5) {
                e5.printStackTrace();
                return blocks;
            }
        }
        catch (Exception e6) {
            System.out.println("Offline synchronysing music");
            for (int j = 0; j < list.size(); ++j) {
                final List<String> folderToAdd = new ArrayList<String>();
                final ScheduleBlock temp2 = list.get(j);
                for (int q2 = 0; q2 < temp2.getFolders().size(); ++q2) {
                    for (int y = 0; y < temp2.getFolders().size(); ++y) {
                        final String curFolder = temp2.getFolders().get(y).trim();
                        Boolean no = true;
                        for (int u = 0; u < new File(dir).listFiles().length; ++u) {
                            if (curFolder.equals(new File(dir).listFiles()[u].getName().trim())) {
                                Boolean noFolder = true;
                                for (int r = 0; r < folderToAdd.size(); ++r) {
                                    if (folderToAdd.get(r).trim().equals(curFolder)) {
                                        noFolder = false;
                                        r = folderToAdd.size();
                                    }
                                }
                                if (noFolder) {
                                    folderToAdd.add(curFolder);
                                }
                                no = false;
                            }
                        }
                        if (no) {
                            throw new Exception();
                        }
                    }
                }
                if (!Main.playing) {
                    for (int b = 0; b < folderToAdd.size(); ++b) {
                        final File[] filesToAdd = new File(String.valueOf(dir) + "\\" + folderToAdd.get(b)).listFiles();
                        final List<Song> tempSongs = new ArrayList<Song>();
                        for (int ru = 0; ru < filesToAdd.length; ++ru) {
                            final File f4 = filesToAdd[ru];
                            final Song s2 = new Song();
                            s2.setName(f4.getName());
                            s2.setVolume(musicVolume);
                            s2.setType((String)folderToAdd.get(b));
                            s2.setLocalPath(f4.getAbsolutePath());
                            tempSongs.add(s2);
                        }
                        blocks.add(tempSongs);
                    }
                }
            }
            System.out.println("Offline synchronysing music OK");
        }
        return blocks;
    }

    public static double getmp3Lentgh(File f) {
        double duration = 0.0D;

        try {
            AudioFile audioFile = AudioFileIO.read(f);
            duration = (double)audioFile.getAudioHeader().getTrackLength();
        } catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException | CannotReadException var5) {
            log.writeLog("Incorrect file");
        }

        return duration;
    } 
}
   