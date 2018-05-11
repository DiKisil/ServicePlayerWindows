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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.MappingJsonFactory;
import MusicMall.tools.ScheduleBlock;
import MusicMall.tools.adv;

public class JSON
{
   
    
  public static boolean getTime(String json)
    throws JsonParseException, IOException
  {
    JsonFactory f = new MappingJsonFactory();
    JsonParser jp = f.createJsonParser(new File(json + "\\Advertisement.json"));
    JsonNode node = jp.readValueAsTree();
    
    return node.get("getTime").getValueAsBoolean();
  }
  
  
  
  public static List<ScheduleBlock> parseMusicJSON(String json, String day)
    throws JsonParseException, IOException
  {
    System.out.println(day);
    JsonFactory f = new MappingJsonFactory();
    JsonParser jp = f.createJsonParser(new File(json + "\\Music.json"));
    JsonToken current = jp.nextToken();
    List<ScheduleBlock> list = new ArrayList();
    int block = 1;
    while (jp.nextToken() != JsonToken.END_OBJECT)
    {
      current = jp.nextToken();
      if (current == JsonToken.START_ARRAY) {
        while (jp.nextToken() != JsonToken.END_ARRAY)
        {
          JsonNode node = jp.readValueAsTree();
          if (node.get("days").getValueAsText().equals(day))
          {
            System.out.println("Yes");
            while (block <= 5)
            {
              ScheduleBlock sb = new ScheduleBlock();
              String folder = "folder_" + block;
              for (int i = 1; i <= 5; i++)
              {
                String temp = null;
                temp = node.get(folder).get(block + "_" + i).getValueAsText();
                if ((temp != null) && (!temp.equals("null")))
                {
                  sb.addFolders(temp);
                  System.out.println("Block  " + block + ";  Folder  " + i + "   " + temp);
                  
                }
               
              }
              if (sb.getFolders().size() != 0) {
                list.add(sb);
               
              }
              block++;
            
            }
          }
        }
      } else {
        jp.skipChildren();
      }
    }
    return list;
  }
  
  public static double parseMusicVolumeJSON(String json, String day)
    throws JsonParseException, IOException
  {
    JsonFactory f = new MappingJsonFactory();
    JsonParser jp = f.createJsonParser(new File(json + "\\Music.json"));
    JsonToken current = jp.nextToken();
    while (jp.nextToken() != JsonToken.END_OBJECT)
    {
      current = jp.nextToken();
      if (current == JsonToken.START_ARRAY)
      {
        if (jp.nextToken() != JsonToken.END_ARRAY)
        {
          JsonNode node = jp.readValueAsTree();
          return node.get("music_volume").getValueAsDouble();
        }
      }
      else {
        jp.skipChildren();
      }
    }
    return 0.0D;
  }
  
  public static String getMusicVolumeJSONParam(String json, String Day, String param)
    throws JsonParseException, IOException
  {
    JsonFactory f = new MappingJsonFactory();
    JsonParser jp = f.createJsonParser(new File(json + "\\Music.json"));
    JsonToken current = jp.nextToken();
    while (jp.nextToken() != JsonToken.END_OBJECT)
    {
      current = jp.nextToken();
      if (current == JsonToken.START_ARRAY) {
        while (jp.nextToken() != JsonToken.END_ARRAY)
        {
          JsonNode node = jp.readValueAsTree();
          if (node.get("days").getValueAsText().trim().equals(Day.trim())) {
            return node.get(param.trim()).getValueAsText();
          }
        }
      } else {
        jp.skipChildren();
      }
    }
    return null;
  }
  
  public static List<adv> parseAdvJSON(String json)
    throws JsonParseException, IOException
  {
    List<adv> advList = new ArrayList();
    JsonFactory f = new MappingJsonFactory();
    JsonParser jp = f.createJsonParser(new File(json + "\\Advertisement.json"));
    JsonToken current = jp.nextToken();
    while (jp.nextToken() != JsonToken.END_OBJECT)
    {
      current = jp.nextToken();
      if (current == JsonToken.START_ARRAY) {
        while (jp.nextToken() != JsonToken.END_ARRAY) {
          try
          {
            JsonNode node = jp.readValueAsTree();
            adv adv = new adv();
            adv.setName(node.get("adv_name").getValueAsText());
            
            String beginDateString = node.get("data_begin").getValueAsText().trim();
            String endDateString = node.get("data_end").getValueAsText().trim();
            
            Calendar beginDate = Calendar.getInstance();
            Calendar endDate = Calendar.getInstance();
           
            
            beginDate.set(
              Integer.parseInt(beginDateString.substring(beginDateString.lastIndexOf(".") + 1, 
              beginDateString.length())), 
              Integer.parseInt(beginDateString.substring(beginDateString.indexOf(".") + 1, 
              beginDateString.lastIndexOf("."))) - 1, 
              Integer.parseInt(beginDateString.substring(0, beginDateString.indexOf("."))));
            
            endDate.set(
              Integer.parseInt(endDateString.substring(endDateString.lastIndexOf(".") + 1, 
              endDateString.length())), 
              Integer.parseInt(endDateString.substring(endDateString.indexOf(".") + 1, 
              endDateString.lastIndexOf("."))) - 1, 
              Integer.parseInt(endDateString.substring(0, endDateString.indexOf("."))));
            
            Calendar currentDayStart = Calendar.getInstance();
            Calendar currentDayEnd = Calendar.getInstance();
            if ((currentDayStart.after(beginDate) & currentDayEnd.before(endDate) | beginDate.compareTo(Calendar.getInstance()) == 0 | endDate.compareTo(Calendar.getInstance()) == 0))
            {
              String beginTime = node.get("time_begin").getValueAsText();
              String endTime = node.get("time_end").getValueAsText();
              for (int b = 0; b < 12; b++) {
                adv.getCount().set(b, Boolean.valueOf(node.get("count" + (b + 1)).getValueAsInt() == 1));
              }
              Date begin = new Date();
              
              begin.setHours(Integer.parseInt(beginTime.substring(0, beginTime.indexOf(":"))));
              begin.setMinutes(
                Integer.parseInt(beginTime.substring(beginTime.indexOf(":") + 1, beginTime.length())));
              begin.setSeconds(0);
              
              Date end = new Date();
              
              end.setHours(Integer.parseInt(endTime.substring(0, endTime.indexOf(":"))));
              end.setMinutes(
                Integer.parseInt(endTime.substring(endTime.indexOf(":") + 1, endTime.length())));
              end.setSeconds(0);
              
              adv.setBegin(begin);
              adv.setEnd(end);
              
              advList.add(adv);
            }
            else
            {
              System.out.println("Wrong adv date");
            }
          }
          catch (Exception e)
          {
            System.out.println("Error parsing adv");
            e.printStackTrace();
          }
        }
      } else {
        jp.skipChildren();
      }
    }
    return advList;
  }
}

