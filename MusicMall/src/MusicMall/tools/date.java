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
import java.text.SimpleDateFormat;
import java.util.Date;

public class date
{
   
  public static String getDayofWeek()
  {
    switch (new Date().getDay())
    {
    case 0: 
      return "Sunday";
    case 1: 
      return "Monday";
    case 2: 
      return "Tuesday";
    case 3: 
      return "Wednesday";
    case 4: 
      return "Thursday";
    case 5: 
      return "Friday";
    case 6: 
      return "Saturday";
    }
    return null;
  }
  
  
  public static String getNextDayofWeek()
  {
    
    switch (new Date().getDay() + 1)
    {
    case 0: 
      return "Sunday";
    case 1: 
      return "Monday";
    case 2: 
      return "Tuesday";
    case 3: 
      return "Wednesday";
    case 4: 
      return "Thursday";
    case 5: 
      return "Friday";
    case 6: 
      return "Saturday";
    }
    return null;
  }
  
  public static Date systemDate()
  {
    return new Date(System.currentTimeMillis());
  }
  
  
  public static boolean compareDate(Date d1, Date d2)
  {
    if ((d1.getYear() != d2.getYear()) || (d1.getMonth() != d2.getMonth()) || (d1.getDate() != d2.getDate()) || 
      (d1.getHours() != d2.getHours()) || (d1.getMinutes() != d2.getMinutes()) || 
      (d1.getSeconds() != d2.getSeconds())) {
      return false;
    }
    return true;
  }
  
  
  public static String ddmmyyyy()
  {
    return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
  }
  
  public static String LogTime()
  {
    return new SimpleDateFormat("HH:mm:ss").format(new Date());
  }
}

