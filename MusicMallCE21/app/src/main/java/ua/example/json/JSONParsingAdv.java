package ua.example.json;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import ua.example.download.ADVdownload;
import ua.example.player.Constants;
import ua.example.player.SongsManager; 
 
public class JSONParsingAdv {

	private SongsManager SongManager;
	 private ADVdownload ADVdownload;
    // url to make request
    private  String file =Constants.WAY_MUSIC()+".Advertisement";

 
    // JSON Node names
    private static final String TAG_ADV = "adv";
    
    private static final String TAG_CHECK = "getTime";
    
    private static final String TAG_ADV_NAME = "adv_name";
    private static final String TAG_DATA_BEGIN = "data_begin";
    private static final String TAG_DATA_END = "data_end";
 
    private static final String TAG_TIME_BEGIN = "time_begin";
    private static final String TAG_TIME_END = "time_end";
    private static final String TAG_COUNT1 = "count1";
    private static final String TAG_COUNT2 = "count2";
    private static final String TAG_COUNT3 = "count3";
    private static final String TAG_COUNT4 = "count4";
    private static final String TAG_COUNT5 = "count5";
    private static final String TAG_COUNT6 = "count6";
    private static final String TAG_COUNT7 = "count7";
    private static final String TAG_COUNT8 = "count8";
    private static final String TAG_COUNT9 = "count9";
    private static final String TAG_COUNT10 = "count10";
    private static final String TAG_COUNT11 = "count11";
    private static final String TAG_COUNT12 = "count12";

    // contacts JSONArray
    JSONArray adv = null;
    public JSONParsingAdv(Context cntx,String sftp, String sobject, String sid, String spassword )
    {
    	SongManager = new SongsManager();
    	ADVdownload = new ADVdownload(cntx, sftp, sobject, sid, spassword);
    }
    public  void AndroidJSONPars() 
    {
    	Log.i("MainActivity", "ADV");

        Calendar c = Calendar.getInstance();

        int mHour = c.get(Calendar.HOUR_OF_DAY);

    	// Hashmap for ListView
        ArrayList<HashMap<String, String>> advList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> advv = new ArrayList<HashMap<String, String>>();
        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();
 
        // getting JSON string from URL
        JSONObject json = jParser.getJSONFromUrl(file);
 
        try {
        	 boolean getTime = Boolean.valueOf(json.getString(TAG_CHECK));
        	 Log.i("Main", "JSON inTime "+getTime);
        	 
            // Getting Array of Contacts
        	adv = json.getJSONArray(TAG_ADV);

       	
       	
            // looping through All Contacts
            for(int i = 0; i < adv.length(); i++)
            {
                JSONObject a = adv.getJSONObject(i);
 
                // Storing each json item in variable
                String adv_name = a.getString(TAG_ADV_NAME);
                String data_begin = a.getString(TAG_DATA_BEGIN);
                String data_end = a.getString(TAG_DATA_END);
                String time_begin = a.getString(TAG_TIME_BEGIN);
                String time_end = a.getString(TAG_TIME_END);
                String count1 = a.getString(TAG_COUNT1);
                String count2 = a.getString(TAG_COUNT2);
                String count3 = a.getString(TAG_COUNT3);
                String count4 = a.getString(TAG_COUNT4);
                String count5 = a.getString(TAG_COUNT5);
                String count6 = a.getString(TAG_COUNT6);
                String count7 = a.getString(TAG_COUNT7);
                String count8 = a.getString(TAG_COUNT8);
                String count9 = a.getString(TAG_COUNT9);
                String count10 = a.getString(TAG_COUNT10);
                String count11= a.getString(TAG_COUNT11);
                String count12 = a.getString(TAG_COUNT12);
                
      
             // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
                HashMap<String, String> map2 = new HashMap<String, String>();
 
                // adding each child node to HashMap key => value
                map.put(TAG_ADV_NAME, adv_name);
                map2.put(TAG_ADV_NAME, adv_name);
                map.put(TAG_DATA_BEGIN, data_begin);
                map.put(TAG_DATA_END, data_end);
                map.put(TAG_TIME_BEGIN, time_begin);
                map.put(TAG_TIME_END, time_end);
                map.put(TAG_COUNT1, count1);
                map.put(TAG_COUNT2, count2);
                map.put(TAG_COUNT3, count3);
                map.put(TAG_COUNT4, count4);
                map.put(TAG_COUNT5, count5);
                map.put(TAG_COUNT6, count6);
                map.put(TAG_COUNT7, count7);
                map.put(TAG_COUNT8, count8);
                map.put(TAG_COUNT9, count9);
                map.put(TAG_COUNT10, count10);
                map.put(TAG_COUNT11, count11);
                map.put(TAG_COUNT12, count12);
                
                
                // adding HashList to ArrayList
                advList.add(map);
                advv.add(map2);
            }
            

        	//Sort the filenames by songTitle
        	  Collections.sort(advList,new Comparator<HashMap<String,String>>(){
        	      public int compare(HashMap<String,String> mapping1,HashMap<String,String> mapping2){
        	          return mapping1.get("adv_name").compareTo(mapping2.get("adv_name"));
        	      }
        	  });

            /**переход в сонг менеджер*/
            if(getTime)
            	SongManager.advList(advList,true);
            else
            	SongManager.advList(advList, false); //false

            ADVdownload.main(advv);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
 

      
    }
    }