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
import java.util.Date;

public class Song
{
  protected String name;
  protected String type;
  protected String localPath;
  protected String remotePath;
  protected double lentgh = 0.0D;
  protected double volume;
  protected Date lastPlayed;
  
  public Date getLastPlayed()
  {
    return this.lastPlayed;
  }
  
  public void setLastPlayed(Date lastPlayed)
  {
    this.lastPlayed = lastPlayed;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
  
  public double getLentgh()
  {
    return this.lentgh;
  }
  
  public void setLentgh(double lentgh)
  {
    this.lentgh = lentgh;
  }
  
  public double getVolume()
  {
    return this.volume;
  }
  
  public void setVolume(double volume)
  {
    this.volume = volume;
  }
  
  public String getLocalPath()
  {
    return this.localPath;
  }
  
  public void setLocalPath(String localPath)
  {
    this.localPath = localPath;
  }
  
  public String getRemotePath()
  {
    return this.remotePath;
  }
  
  public void setRemotePath(String remotePath)
  {
    this.remotePath = remotePath;
  }
}

