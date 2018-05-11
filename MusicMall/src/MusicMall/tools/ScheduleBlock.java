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
import java.util.ArrayList;
import java.util.List;

public class ScheduleBlock
{
  protected int Number;
  protected List<String> folders = new ArrayList();
  
  public int getNumber()
  {
    return this.Number;
  }
  
  public void setNumber(int number)
  {
    this.Number = number;
  }
  
  public List<String> getFolders()
  {
    return this.folders;
  }
  
  public void setFolders(List<String> folders)
  {
    this.folders = folders;
  }
  
  public void addFolders(String folder)
  {
    this.folders.add(folder);
  }
  
}
