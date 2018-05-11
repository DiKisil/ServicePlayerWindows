package ua.example.json;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import ua.example.download.*;
import ua.example.player.Constants;
import ua.example.player.SongsManager;
 
public class JSONParsingMusic
{
	private LOGupload LOGupload;
    private SongsManager SongManager;
    private MUSICdownload Musicdownload;
    // url to make request
    private  String file = Constants.WAY_MUSIC()+".Music";
    boolean download=false;
    // JSON Node names
    private static final String TAG_MUSIC = "music";
    
    private static final String TAG_DAYS = "days";
    private static final String TAG_TIME_START = "time_start";
    private static final String TAG_TIME_FINISH = "time_finish";
    
    private static final String TAG_MUSIC_VOLUME = "music_volume";
    private static final String TAG_ADV_VOLUME = "adv_volume";

    
    private static final String TAG_TIME_START_1 = "time_start_1";
    private static final String TAG_TIME_END_1 = "time_end_1";
    private static final String TAG_TIME_START_2 = "time_start_2";
    private static final String TAG_TIME_END_2 = "time_end_2";
    private static final String TAG_TIME_START_3 = "time_start_3";
    private static final String TAG_TIME_END_3 = "time_end_3";
    private static final String TAG_TIME_START_4 = "time_start_4";
    private static final String TAG_TIME_END_4 = "time_end_4";
    private static final String TAG_TIME_START_5 = "time_start_5";
    private static final String TAG_TIME_END_5 = "time_end_5";
    
    private static final String TAG_PHOLDER_1 = "folder_1";
    private static final String TAG_PHOLDER_2 = "folder_2";
    private static final String TAG_PHOLDER_3 = "folder_3";
    private static final String TAG_PHOLDER_4 = "folder_4";
    private static final String TAG_PHOLDER_5 = "folder_5";
    
    private static final String TAG_PHOLDER_1_1 = "1_1";
    private static final String TAG_PHOLDER_1_2 = "1_2";
    private static final String TAG_PHOLDER_1_3 = "1_3";
    private static final String TAG_PHOLDER_1_4 = "1_4";
    private static final String TAG_PHOLDER_1_5 = "1_5";
    private static final String TAG_PHOLDER_2_1 = "2_1";
    private static final String TAG_PHOLDER_2_2 = "2_2";
    private static final String TAG_PHOLDER_2_3 = "2_3";
    private static final String TAG_PHOLDER_2_4 = "2_4";
    private static final String TAG_PHOLDER_2_5 = "2_5";
    private static final String TAG_PHOLDER_3_1 = "3_1";
    private static final String TAG_PHOLDER_3_2 = "3_2";
    private static final String TAG_PHOLDER_3_3 = "3_3";
    private static final String TAG_PHOLDER_3_4 = "3_4";
    private static final String TAG_PHOLDER_3_5 = "3_5";
    private static final String TAG_PHOLDER_4_1 = "4_1";
    private static final String TAG_PHOLDER_4_2 = "4_2";
    private static final String TAG_PHOLDER_4_3 = "4_3";
    private static final String TAG_PHOLDER_4_4 = "4_4";
    private static final String TAG_PHOLDER_4_5 = "4_5";
    private static final String TAG_PHOLDER_5_1 = "5_1";
    private static final String TAG_PHOLDER_5_2 = "5_2";
    private static final String TAG_PHOLDER_5_3 = "5_3";
    private static final String TAG_PHOLDER_5_4 = "5_4";
    private static final String TAG_PHOLDER_5_5 = "5_5";
 
    // contacts JSONArray
    JSONArray music = null;
    public JSONParsingMusic(Context cntx,String sftp, String sobject, String sid, String spassword, boolean download_music)
    {
    	SongManager = new SongsManager();
    	Musicdownload = new MUSICdownload(cntx, sftp, sobject, sid, spassword);
    	LOGupload = new LOGupload(cntx, sftp, sobject, sid, spassword);
    	download=download_music;
    }
    public  void AndroidJSONPars() 
    {
    	Log.i("MainActivity", "MUsic");
    	Calendar c = Calendar.getInstance();
    
	    int mHour = c.get(Calendar.HOUR_OF_DAY);
    	// Hashmap for ListView
        ArrayList<HashMap<String, String>> musicList = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> musicc = new ArrayList<HashMap<String, String>>();
        // Creating JSON Parser instance
        JSONParser jParser = new JSONParser();
 
        // getting JSON string from URL
        Log.d("WAY", "ПУть в парсере\n" +file);
        JSONObject json = jParser.getJSONFromUrl(file);
 
        try {
            // Getting Array of Contacts
            music = json.getJSONArray(TAG_MUSIC);
 
            // looping through All Contacts
            for(int i = 0; i < music.length(); i++)
            {
                JSONObject m = music.getJSONObject(i);
 
                // Storing each json item in variable
                String days = m.getString(TAG_DAYS);
                String time_start = m.getString(TAG_TIME_START);
                String time_finish = m.getString(TAG_TIME_FINISH);
                String music_volume = m.getString(TAG_MUSIC_VOLUME);
                String adv_volume = m.getString(TAG_ADV_VOLUME);
                
                //////////////////////////////////////////////////////
                String time_start_1 = m.getString(TAG_TIME_START_1);
                String time_end_1 = m.getString(TAG_TIME_END_1);
 
                // Phone number is agin JSON Object
                JSONObject folder_1 = m.getJSONObject(TAG_PHOLDER_1);
                
                String f11 = folder_1.getString(TAG_PHOLDER_1_1);
                String f12 = folder_1.getString(TAG_PHOLDER_1_2);
                String f13 = folder_1.getString(TAG_PHOLDER_1_3);
                String f14 = folder_1.getString(TAG_PHOLDER_1_4);
                String f15 = folder_1.getString(TAG_PHOLDER_1_5);
                //////////////////////////////////////////////////////
                String time_start_2 = m.getString(TAG_TIME_START_2);
                String time_end_2 = m.getString(TAG_TIME_END_2);
 
                // Phone number is agin JSON Object
                JSONObject folder_2 = m.getJSONObject(TAG_PHOLDER_2);
                
                String f21 = folder_2.getString(TAG_PHOLDER_2_1);
                String f22 = folder_2.getString(TAG_PHOLDER_2_2);
                String f23 = folder_2.getString(TAG_PHOLDER_2_3);
                String f24 = folder_2.getString(TAG_PHOLDER_2_4);
                String f25 = folder_2.getString(TAG_PHOLDER_2_5);
                //////////////////////////////////////////////////////
                String time_start_3 = m.getString(TAG_TIME_START_3);
                String time_end_3 = m.getString(TAG_TIME_END_3);
 
                // Phone number is agin JSON Object
                JSONObject folder_3 = m.getJSONObject(TAG_PHOLDER_3);
                
                String f31 = folder_3.getString(TAG_PHOLDER_3_1);
                String f32 = folder_3.getString(TAG_PHOLDER_3_2);
                String f33 = folder_3.getString(TAG_PHOLDER_3_3);
                String f34 = folder_3.getString(TAG_PHOLDER_3_4);
                String f35 = folder_3.getString(TAG_PHOLDER_3_5);
				//////////////////////////////////////////////////////
				String time_start_4 = m.getString(TAG_TIME_START_4);
				String time_end_4 = m.getString(TAG_TIME_END_4);
				
				// Phone number is agin JSON Object
				JSONObject folder_4 = m.getJSONObject(TAG_PHOLDER_4);
				
				String f41 = folder_4.getString(TAG_PHOLDER_4_1);
				String f42 = folder_4.getString(TAG_PHOLDER_4_2);
				String f43 = folder_4.getString(TAG_PHOLDER_4_3);
				String f44 = folder_4.getString(TAG_PHOLDER_4_4);
				String f45 = folder_4.getString(TAG_PHOLDER_4_5);
				//////////////////////////////////////////////////////
				String time_start_5 = m.getString(TAG_TIME_START_5);
				String time_end_5 = m.getString(TAG_TIME_END_5);
				
				// Phone number is agin JSON Object
				JSONObject folder_5 = m.getJSONObject(TAG_PHOLDER_5);
				
				String f51 = folder_5.getString(TAG_PHOLDER_5_1);
				String f52 = folder_5.getString(TAG_PHOLDER_5_2);
				String f53 = folder_5.getString(TAG_PHOLDER_5_3);
				String f54 = folder_5.getString(TAG_PHOLDER_5_4);
				String f55 = folder_5.getString(TAG_PHOLDER_5_5);
             // creating new HashMap
            
                HashMap<String, String> map = new HashMap<String, String>();
                HashMap<String, String> folder = new HashMap<String, String>();
 
                // adding each child node to HashMap key => value
                map.put(TAG_DAYS, days);
                map.put(TAG_TIME_START, time_start);
                map.put(TAG_TIME_FINISH, time_finish);
                
                map.put(TAG_MUSIC_VOLUME, music_volume);
                map.put(TAG_ADV_VOLUME, adv_volume);
                
                map.put(TAG_TIME_START_1, time_start_1);
                map.put(TAG_TIME_END_1, time_end_1);
                
                map.put(TAG_PHOLDER_1_1, f11);
                map.put(TAG_PHOLDER_1_2, f12);
                map.put(TAG_PHOLDER_1_3, f13);
                map.put(TAG_PHOLDER_1_4, f14);
                map.put(TAG_PHOLDER_1_5, f15);
                
                folder.put(TAG_PHOLDER_1_1, f11);
                folder.put(TAG_PHOLDER_1_2, f12);
                folder.put(TAG_PHOLDER_1_3, f13);
                folder.put(TAG_PHOLDER_1_4, f14);
                folder.put(TAG_PHOLDER_1_5, f15);
                
                map.put(TAG_TIME_START_2, time_start_2);
                map.put(TAG_TIME_END_2, time_end_2);
                
                map.put(TAG_PHOLDER_2_1, f21);
                map.put(TAG_PHOLDER_2_2, f22);
                map.put(TAG_PHOLDER_2_3, f23);
                map.put(TAG_PHOLDER_2_4, f24);
                map.put(TAG_PHOLDER_2_5, f25);
                
                folder.put(TAG_PHOLDER_2_1, f21);
                folder.put(TAG_PHOLDER_2_2, f22);
                folder.put(TAG_PHOLDER_2_3, f23);
                folder.put(TAG_PHOLDER_2_4, f24);
                folder.put(TAG_PHOLDER_2_5, f25);
                
                map.put(TAG_TIME_START_3, time_start_3);
                map.put(TAG_TIME_END_3, time_end_3);
                
                map.put(TAG_PHOLDER_3_1, f31);
                map.put(TAG_PHOLDER_3_2, f32);
                map.put(TAG_PHOLDER_3_3, f33);
                map.put(TAG_PHOLDER_3_4, f34);
                map.put(TAG_PHOLDER_3_5, f35);
                
                folder.put(TAG_PHOLDER_3_1, f31);
                folder.put(TAG_PHOLDER_3_2, f32);
                folder.put(TAG_PHOLDER_3_3, f33);
                folder.put(TAG_PHOLDER_3_4, f34);
                folder.put(TAG_PHOLDER_3_5, f35);
                
                
                map.put(TAG_TIME_START_4, time_start_4);
                map.put(TAG_TIME_END_4, time_end_4);
                
                map.put(TAG_PHOLDER_4_1, f41);
                map.put(TAG_PHOLDER_4_2, f42);
                map.put(TAG_PHOLDER_4_3, f43);
                map.put(TAG_PHOLDER_4_4, f44);
                map.put(TAG_PHOLDER_4_5, f45);
                
                folder.put(TAG_PHOLDER_4_1, f41);
                folder.put(TAG_PHOLDER_4_2, f42);
                folder.put(TAG_PHOLDER_4_3, f43);
                folder.put(TAG_PHOLDER_4_4, f44);
                folder.put(TAG_PHOLDER_4_5, f45);
                
                map.put(TAG_TIME_START_5, time_start_5);
                map.put(TAG_TIME_END_5, time_end_5);
                
                map.put(TAG_PHOLDER_5_1, f51);
                map.put(TAG_PHOLDER_5_2, f52);
                map.put(TAG_PHOLDER_5_3, f53);
                map.put(TAG_PHOLDER_5_4, f54);
                map.put(TAG_PHOLDER_5_5, f55);
                
                folder.put(TAG_PHOLDER_5_1, f51);
                folder.put(TAG_PHOLDER_5_2, f52);
                folder.put(TAG_PHOLDER_5_3, f53);
                folder.put(TAG_PHOLDER_5_4, f54);
                folder.put(TAG_PHOLDER_5_5, f55);
                
                // adding HashList to ArrayList
                musicList.add(map);
                musicc.add(folder);
            }
            Log.d("MainActivity", "/**переход в СонгМенеджер*/");
            /**������� � ������������*/
            SongManager.musicList(musicList);
            
            if(download)
            {	        
	            if(mHour==0)
	            {
	            	Musicdownload.music(musicc);
	            	LOGupload.main();
	            }
            }
            else
            {
            	Musicdownload.music(musicc);
            	 if(mHour==0)
 	            {
 	            	LOGupload.main();
 	            }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
 

      
    }
    }