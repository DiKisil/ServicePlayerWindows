package ua.example.player;

import ua.player.musicmallce.R;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class Constants {
	static Context context;
 
    public static String WAY_MUSIC()
    {
    	 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context); 
    	String Sdir = new String( prefs.getString(context.getString(R.string.pref_way), "/mnt/sdcard"));		
    			return Sdir+"/MusicMall/";
    }
    public Constants(Context cntx) {
    	context=cntx;
     }
}