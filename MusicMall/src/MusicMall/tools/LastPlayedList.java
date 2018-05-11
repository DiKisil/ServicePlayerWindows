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

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LastPlayedList
{
  protected static List<String> SongNameList = new ArrayList();
  protected static List<Date> LastPlayedList = new ArrayList();
  protected static int Counter = 0;
  
  public Date isPlayed(String SongName)
  {
    for (int i = 0; i < SongNameList.size(); i++) {
      if (((String)SongNameList.get(i)).equals(SongName)) {
        return (Date)LastPlayedList.get(i);
      }
    }
    return null;
  }
  
  public void addSong(String SongName, Date LastPlayed)
  {
    if (((SongName != null ? 1 : 0) & (LastPlayed != null ? 1 : 0)) != 0)
    {
      for (int i = 0; i < SongNameList.size(); i++) {
        if (((String)SongNameList.get(i)).equals(SongName))
        {
          LastPlayedList.set(i, LastPlayed);
          return;
        }
      }
      SongNameList.add(Counter, SongName);
      LastPlayedList.add(Counter, LastPlayed);
      Counter += 1;
      System.out.println("");
      System.out.println(SongNameList.size());
      System.out.println(SongName);
      System.out.println(LastPlayed.toString());
      System.out.println("");
    }
    System.out.println("LastPlayedSize: " + Counter);
  }
  
  public int getBestSong(List<Song> songs)
  {
    int BestSong = 0;
    for (int i = 0; i < LastPlayedList.size(); i++) {
      if (((Date)LastPlayedList.get(i)).before((Date)LastPlayedList.get(BestSong))) {
        BestSong = i;
      }
    }
    String SongName = (String)SongNameList.get(BestSong);
    System.out.println(SongName);
    for (int t = 0; t < songs.size(); t++) {
      if (((Song)songs.get(t)).getName().equals(SongName)) {
        return t;
      }
    }
    SongNameList.remove(BestSong);
    LastPlayedList.remove(BestSong);
    Counter -= 1;
    System.out.println("LastPlayedSizeRemoved: " + Counter + "   " + SongNameList.size());
    
    return getBestSong(songs);
  }
}

