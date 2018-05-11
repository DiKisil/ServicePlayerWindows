package ua.example.player;

import java.io.File;
import java.io.FilenameFilter;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

import ua.example.ftp.MainActivity;
import ua.example.player.AndroidBuildingMusicPlayerActivity;;

@SuppressWarnings("all")
public class SongsManager
{

	private  AndroidBuildingMusicPlayerActivity PlayerActivity= new AndroidBuildingMusicPlayerActivity();

	final String icon_reklama="icon_reklama";

	private static String /*days, */time_start,time_finish,
			time_start_1, time_end_1, f11,f12,f13,f14,f15,
			time_start_2, time_end_2, f21,f22,f23,f24,f25,
			time_start_3, time_end_3, f31,f32,f33,f34,f35,
			time_start_4, time_end_4, f41,f42,f43,f44,f45,
			time_start_5, time_end_5, f51,f52,f53,f54,f55;
	@SuppressWarnings("unused")
	private  int  s_1_H ,s_1_M ,s_2_H,s_2_M ,s_3_H ,s_3_M ,s_4_H ,s_4_M ,s_5_H ,s_5_M,
			f_1_H ,f_1_M ,f_2_H,f_2_M ,f_3_H ,f_3_M ,f_4_H ,f_4_M ,f_5_H ,f_5_M ;
	//final  String music_volume_,adv_volume_;
	private int mHour;
	//private int mMinute;


	//private String HM;
	private String time_start_1_M,time_finish_1_M,time_start_1_H,time_finish_1_H,
			time_start_2_M,time_finish_2_M,time_start_2_H,time_finish_2_H,
			time_start_3_M,time_finish_3_M,time_start_3_H,time_finish_3_H,
			time_start_4_M,time_finish_4_M,time_start_4_H,time_finish_4_H,
			time_start_5_M,time_finish_5_M,time_start_5_H,time_finish_5_H;

	int time_now_H,time_now_M;
	private int mDayOfWeek;//Sunday,Monday...

	//    private static final Object TAG_DAYS = "days";
	private static final Object TAG_TIME_START = "time_start";
	private static final Object TAG_TIME_FINISH = "time_finish";
	private static final Object TAG_MUSIC_VOLUME = "music_volume";
	private static final Object TAG_ADV_VOLUME = "adv_volume";

	private static final Object TAG_TIME_START_1 = "time_start_1";
	private static final Object TAG_TIME_END_1 = "time_end_1";
	private static final Object TAG_TIME_START_2 = "time_start_2";
	private static final Object TAG_TIME_END_2 = "time_end_2";
	private static final Object TAG_TIME_START_3 = "time_start_3";
	private static final Object TAG_TIME_END_3 = "time_end_3";
	private static final Object TAG_TIME_START_4 = "time_start_4";
	private static final Object TAG_TIME_END_4 = "time_end_4";
	private static final Object TAG_TIME_START_5 = "time_start_5";
	private static final Object TAG_TIME_END_5 = "time_end_5";

	private static final Object TAG_PHOLDER_1_1 = "1_1";
	private static final Object TAG_PHOLDER_1_2 = "1_2";
	private static final Object TAG_PHOLDER_1_3 = "1_3";
	private static final Object TAG_PHOLDER_1_4 = "1_4";
	private static final Object TAG_PHOLDER_1_5 = "1_5";
	private static final Object TAG_PHOLDER_2_1 = "2_1";
	private static final Object TAG_PHOLDER_2_2 = "2_2";
	private static final Object TAG_PHOLDER_2_3 = "2_3";
	private static final Object TAG_PHOLDER_2_4 = "2_4";
	private static final Object TAG_PHOLDER_2_5 = "2_5";
	private static final Object TAG_PHOLDER_3_1 = "3_1";
	private static final Object TAG_PHOLDER_3_2 = "3_2";
	private static final Object TAG_PHOLDER_3_3 = "3_3";
	private static final Object TAG_PHOLDER_3_4 = "3_4";
	private static final Object TAG_PHOLDER_3_5 = "3_5";
	private static final Object TAG_PHOLDER_4_1 = "4_1";
	private static final Object TAG_PHOLDER_4_2 = "4_2";
	private static final Object TAG_PHOLDER_4_3 = "4_3";
	private static final Object TAG_PHOLDER_4_4 = "4_4";
	private static final Object TAG_PHOLDER_4_5 = "4_5";
	private static final Object TAG_PHOLDER_5_1 = "5_1";
	private static final Object TAG_PHOLDER_5_2 = "5_2";
	private static final Object TAG_PHOLDER_5_3 = "5_3";
	private static final Object TAG_PHOLDER_5_4 = "5_4";
	private static final Object TAG_PHOLDER_5_5 = "5_5";
	private static final String TAG_ADV_NAME = "adv_name";

	private static  String folder_adv="adv", folder1="start",folder2,folder3,folder4,folder5;

	String way=Constants.WAY_MUSIC() ;

	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	static boolean getTime;

	static ArrayList<HashMap<String, String>> ADV = new ArrayList<HashMap<String, String>>();

	private static final String TAG = "MainActivity";

	private static String music_volume,adv_volume;



	public void musicList(ArrayList<HashMap<String, String>> musicList)
	{


		final HashMap<String, String> mon=musicList.get(0);//Monday
		final HashMap<String, String> tue=musicList.get(1);
		final HashMap<String, String> wed=musicList.get(2);
		final HashMap<String, String> thu=musicList.get(3);
		final HashMap<String, String> fri=musicList.get(4);
		final HashMap<String, String> sat=musicList.get(5);
		final HashMap<String, String> sun=musicList.get(6);//Sunday

		Calendar c = Calendar.getInstance();

		mHour = c.get(Calendar.HOUR_OF_DAY);

		mDayOfWeek =  c.get(Calendar.DAY_OF_WEEK);

		if(mDayOfWeek==Calendar.MONDAY)
		{
			//days=mon.get(TAG_DAYS);
			time_start=new String(mon.get(TAG_TIME_START));
			time_finish=new String(mon.get(TAG_TIME_FINISH));
			music_volume =new String(mon.get(TAG_MUSIC_VOLUME));
			adv_volume =new String(mon.get(TAG_ADV_VOLUME));


			time_start_1=new String(mon.get(TAG_TIME_START_1));time_end_1=new String(mon.get(TAG_TIME_END_1));
			time_start_2=new String(mon.get(TAG_TIME_START_2));time_end_2=new String(mon.get(TAG_TIME_END_2));
			time_start_3=new String(mon.get(TAG_TIME_START_3));time_end_3=new String(mon.get(TAG_TIME_END_3));
			time_start_4=new String(mon.get(TAG_TIME_START_4));time_end_4=new String(mon.get(TAG_TIME_END_4));
			time_start_5=new String(mon.get(TAG_TIME_START_5));time_end_5=new String(mon.get(TAG_TIME_END_5));

			f11=new String(mon.get(TAG_PHOLDER_1_1));f12=new String(mon.get(TAG_PHOLDER_1_2));f13=new String(mon.get(TAG_PHOLDER_1_3));f14=new String(mon.get(TAG_PHOLDER_1_4));f15=new String(mon.get(TAG_PHOLDER_1_5));
			f21=new String(mon.get(TAG_PHOLDER_2_1));f22=new String(mon.get(TAG_PHOLDER_2_2));f23=new String(mon.get(TAG_PHOLDER_2_3));f24=new String(mon.get(TAG_PHOLDER_2_4));f25=new String(mon.get(TAG_PHOLDER_2_5));
			f31=new String(mon.get(TAG_PHOLDER_3_1));f32=new String(mon.get(TAG_PHOLDER_3_2));f33=new String(mon.get(TAG_PHOLDER_3_3));f34=new String(mon.get(TAG_PHOLDER_3_4));f35=new String(mon.get(TAG_PHOLDER_3_5));
			f41=new String(mon.get(TAG_PHOLDER_4_1));f42=new String(mon.get(TAG_PHOLDER_4_2));f43=new String(mon.get(TAG_PHOLDER_4_3));f44=new String(mon.get(TAG_PHOLDER_4_4));f45=new String(mon.get(TAG_PHOLDER_4_5));
			f51=new String(mon.get(TAG_PHOLDER_5_1));f52=new String(mon.get(TAG_PHOLDER_5_2));f53=new String(mon.get(TAG_PHOLDER_5_3));f54=new String(mon.get(TAG_PHOLDER_5_4));f55=new String(mon.get(TAG_PHOLDER_5_5));

		}
		else
		if(mDayOfWeek==Calendar.TUESDAY)
		{
			//days=tue.get(TAG_DAYS);
			time_start=new String(tue.get(TAG_TIME_START));
			time_finish=new String(tue.get(TAG_TIME_FINISH));
			music_volume =new String(tue.get(TAG_MUSIC_VOLUME));
			adv_volume =new String(tue.get(TAG_ADV_VOLUME));


			time_start_1=new String(tue.get(TAG_TIME_START_1));time_end_1=new String(tue.get(TAG_TIME_END_1));
			time_start_2=new String(tue.get(TAG_TIME_START_2));time_end_2=new String(tue.get(TAG_TIME_END_2));
			time_start_3=new String(tue.get(TAG_TIME_START_3));time_end_3=new String(tue.get(TAG_TIME_END_3));
			time_start_4=new String(tue.get(TAG_TIME_START_4));time_end_4=new String(tue.get(TAG_TIME_END_4));
			time_start_5=new String(tue.get(TAG_TIME_START_5));time_end_5=new String(tue.get(TAG_TIME_END_5));

			f11=new String(tue.get(TAG_PHOLDER_1_1));f12=new String(tue.get(TAG_PHOLDER_1_2));f13=new String(tue.get(TAG_PHOLDER_1_3));f14=new String(tue.get(TAG_PHOLDER_1_4));f15=new String(tue.get(TAG_PHOLDER_1_5));
			f21=new String(tue.get(TAG_PHOLDER_2_1));f22=new String(tue.get(TAG_PHOLDER_2_2));f23=new String(tue.get(TAG_PHOLDER_2_3));f24=new String(tue.get(TAG_PHOLDER_2_4));f25=new String(tue.get(TAG_PHOLDER_2_5));
			f31=new String(tue.get(TAG_PHOLDER_3_1));f32=new String(tue.get(TAG_PHOLDER_3_2));f33=new String(tue.get(TAG_PHOLDER_3_3));f34=new String(tue.get(TAG_PHOLDER_3_4));f35=new String(tue.get(TAG_PHOLDER_3_5));
			f41=new String(tue.get(TAG_PHOLDER_4_1));f42=new String(tue.get(TAG_PHOLDER_4_2));f43=new String(tue.get(TAG_PHOLDER_4_3));f44=new String(tue.get(TAG_PHOLDER_4_4));f45=new String(tue.get(TAG_PHOLDER_4_5));
			f51=new String(tue.get(TAG_PHOLDER_5_1));f52=new String(tue.get(TAG_PHOLDER_5_2));f53=new String(tue.get(TAG_PHOLDER_5_3));f54=new String(tue.get(TAG_PHOLDER_5_4));f55=new String(tue.get(TAG_PHOLDER_5_5));
		}
		else
		if(mDayOfWeek==Calendar.WEDNESDAY)
		{
			//days=wed.get(TAG_DAYS);
			time_start=new String(wed.get(TAG_TIME_START));
			time_finish=new String(wed.get(TAG_TIME_FINISH));
			music_volume =new String(wed.get(TAG_MUSIC_VOLUME));
			adv_volume =new String(wed.get(TAG_ADV_VOLUME));


			time_start_1=new String(wed.get(TAG_TIME_START_1));time_end_1=new String(wed.get(TAG_TIME_END_1));
			time_start_2=new String(wed.get(TAG_TIME_START_2));time_end_2=new String(wed.get(TAG_TIME_END_2));
			time_start_3=new String(wed.get(TAG_TIME_START_3));time_end_3=new String(wed.get(TAG_TIME_END_3));
			time_start_4=new String(wed.get(TAG_TIME_START_4));time_end_4=new String(wed.get(TAG_TIME_END_4));
			time_start_5=new String(wed.get(TAG_TIME_START_5));time_end_5=new String(wed.get(TAG_TIME_END_5));

			f11=new String(wed.get(TAG_PHOLDER_1_1));f12=new String(wed.get(TAG_PHOLDER_1_2));f13=new String(wed.get(TAG_PHOLDER_1_3));f14=new String(wed.get(TAG_PHOLDER_1_4));f15=new String(wed.get(TAG_PHOLDER_1_5));
			f21=new String(wed.get(TAG_PHOLDER_2_1));f22=new String(wed.get(TAG_PHOLDER_2_2));f23=new String(wed.get(TAG_PHOLDER_2_3));f24=new String(wed.get(TAG_PHOLDER_2_4));f25=new String(wed.get(TAG_PHOLDER_2_5));
			f31=new String(wed.get(TAG_PHOLDER_3_1));f32=new String(wed.get(TAG_PHOLDER_3_2));f33=new String(wed.get(TAG_PHOLDER_3_3));f34=new String(wed.get(TAG_PHOLDER_3_4));f35=new String(wed.get(TAG_PHOLDER_3_5));
			f41=new String(wed.get(TAG_PHOLDER_4_1));f42=new String(wed.get(TAG_PHOLDER_4_2));f43=new String(wed.get(TAG_PHOLDER_4_3));f44=new String(wed.get(TAG_PHOLDER_4_4));f45=new String(wed.get(TAG_PHOLDER_4_5));
			f51=new String(wed.get(TAG_PHOLDER_5_1));f52=new String(wed.get(TAG_PHOLDER_5_2));f53=new String(wed.get(TAG_PHOLDER_5_3));f54=new String(wed.get(TAG_PHOLDER_5_4));f55=new String(wed.get(TAG_PHOLDER_5_5));
		}
		else
		if(mDayOfWeek==Calendar.THURSDAY)
		{
			//days=thu.get(TAG_DAYS);
			time_start=new String(thu.get(TAG_TIME_START));
			time_finish=new String(thu.get(TAG_TIME_FINISH));
			music_volume =new String(thu.get(TAG_MUSIC_VOLUME));
			adv_volume =new String(thu.get(TAG_ADV_VOLUME));


			time_start_1=new String(thu.get(TAG_TIME_START_1));time_end_1=new String(thu.get(TAG_TIME_END_1));
			time_start_2=new String(thu.get(TAG_TIME_START_2));time_end_2=new String(thu.get(TAG_TIME_END_2));
			time_start_3=new String(thu.get(TAG_TIME_START_3));time_end_3=new String(thu.get(TAG_TIME_END_3));
			time_start_4=new String(thu.get(TAG_TIME_START_4));time_end_4=new String(thu.get(TAG_TIME_END_4));
			time_start_5=new String(thu.get(TAG_TIME_START_5));time_end_5=new String(thu.get(TAG_TIME_END_5));

			f11=new String(thu.get(TAG_PHOLDER_1_1));f12=new String(thu.get(TAG_PHOLDER_1_2));f13=new String(thu.get(TAG_PHOLDER_1_3));f14=new String(thu.get(TAG_PHOLDER_1_4));f15=new String(thu.get(TAG_PHOLDER_1_5));
			f21=new String(thu.get(TAG_PHOLDER_2_1));f22=new String(thu.get(TAG_PHOLDER_2_2));f23=new String(thu.get(TAG_PHOLDER_2_3));f24=new String(thu.get(TAG_PHOLDER_2_4));f25=new String(thu.get(TAG_PHOLDER_2_5));
			f31=new String(thu.get(TAG_PHOLDER_3_1));f32=new String(thu.get(TAG_PHOLDER_3_2));f33=new String(thu.get(TAG_PHOLDER_3_3));f34=new String(thu.get(TAG_PHOLDER_3_4));f35=new String(thu.get(TAG_PHOLDER_3_5));
			f41=new String(thu.get(TAG_PHOLDER_4_1));f42=new String(thu.get(TAG_PHOLDER_4_2));f43=new String(thu.get(TAG_PHOLDER_4_3));f44=new String(thu.get(TAG_PHOLDER_4_4));f45=new String(thu.get(TAG_PHOLDER_4_5));
			f51=new String(thu.get(TAG_PHOLDER_5_1));f52=new String(thu.get(TAG_PHOLDER_5_2));f53=new String(thu.get(TAG_PHOLDER_5_3));f54=new String(thu.get(TAG_PHOLDER_5_4));f55=new String(thu.get(TAG_PHOLDER_5_5));
		}
		else
		if(mDayOfWeek==Calendar.FRIDAY)
		{
			//days=fri.get(TAG_DAYS);
			time_start=new String(fri.get(TAG_TIME_START));
			time_finish=new String(fri.get(TAG_TIME_FINISH));
			music_volume =new String(fri.get(TAG_MUSIC_VOLUME));
			adv_volume =new String(fri.get(TAG_ADV_VOLUME));


			time_start_1=new String(fri.get(TAG_TIME_START_1));time_end_1=new String(fri.get(TAG_TIME_END_1));
			time_start_2=new String(fri.get(TAG_TIME_START_2));time_end_2=new String(fri.get(TAG_TIME_END_2));
			time_start_3=new String(fri.get(TAG_TIME_START_3));time_end_3=new String(fri.get(TAG_TIME_END_3));
			time_start_4=new String(fri.get(TAG_TIME_START_4));time_end_4=new String(fri.get(TAG_TIME_END_4));
			time_start_5=new String(fri.get(TAG_TIME_START_5));time_end_5=new String(fri.get(TAG_TIME_END_5));

			f11=new String(fri.get(TAG_PHOLDER_1_1));f12=new String(fri.get(TAG_PHOLDER_1_2));f13=new String(fri.get(TAG_PHOLDER_1_3));f14=new String(fri.get(TAG_PHOLDER_1_4));f15=new String(fri.get(TAG_PHOLDER_1_5));
			f21=new String(fri.get(TAG_PHOLDER_2_1));f22=new String(fri.get(TAG_PHOLDER_2_2));f23=new String(fri.get(TAG_PHOLDER_2_3));f24=new String(fri.get(TAG_PHOLDER_2_4));f25=new String(fri.get(TAG_PHOLDER_2_5));
			f31=new String(fri.get(TAG_PHOLDER_3_1));f32=new String(fri.get(TAG_PHOLDER_3_2));f33=new String(fri.get(TAG_PHOLDER_3_3));f34=new String(fri.get(TAG_PHOLDER_3_4));f35=new String(fri.get(TAG_PHOLDER_3_5));
			f41=new String(fri.get(TAG_PHOLDER_4_1));f42=new String(fri.get(TAG_PHOLDER_4_2));f43=new String(fri.get(TAG_PHOLDER_4_3));f44=new String(fri.get(TAG_PHOLDER_4_4));f45=new String(fri.get(TAG_PHOLDER_4_5));
			f51=new String(fri.get(TAG_PHOLDER_5_1));f52=new String(fri.get(TAG_PHOLDER_5_2));f53=new String(fri.get(TAG_PHOLDER_5_3));f54=new String(fri.get(TAG_PHOLDER_5_4));f55=new String(fri.get(TAG_PHOLDER_5_5));
		}

		else
		if(mDayOfWeek==Calendar.SATURDAY)
		{
			//days=sat.get(TAG_DAYS);
			time_start=new String(sat.get(TAG_TIME_START));
			time_finish=new String(sat.get(TAG_TIME_FINISH));
			music_volume =new String(sat.get(TAG_MUSIC_VOLUME));
			adv_volume =new String(sat.get(TAG_ADV_VOLUME));


			time_start_1=new String(sat.get(TAG_TIME_START_1));time_end_1=new String(sat.get(TAG_TIME_END_1));
			time_start_2=new String(sat.get(TAG_TIME_START_2));time_end_2=new String(sat.get(TAG_TIME_END_2));
			time_start_3=new String(sat.get(TAG_TIME_START_3));time_end_3=new String(sat.get(TAG_TIME_END_3));
			time_start_4=new String(sat.get(TAG_TIME_START_4));time_end_4=new String(sat.get(TAG_TIME_END_4));
			time_start_5=new String(sat.get(TAG_TIME_START_5));time_end_5=new String(sat.get(TAG_TIME_END_5));

			f11=new String(sat.get(TAG_PHOLDER_1_1));f12=new String(sat.get(TAG_PHOLDER_1_2));f13=new String(sat.get(TAG_PHOLDER_1_3));f14=new String(sat.get(TAG_PHOLDER_1_4));f15=new String(sat.get(TAG_PHOLDER_1_5));
			f21=new String(sat.get(TAG_PHOLDER_2_1));f22=new String(sat.get(TAG_PHOLDER_2_2));f23=new String(sat.get(TAG_PHOLDER_2_3));f24=new String(sat.get(TAG_PHOLDER_2_4));f25=new String(sat.get(TAG_PHOLDER_2_5));
			f31=new String(sat.get(TAG_PHOLDER_3_1));f32=new String(sat.get(TAG_PHOLDER_3_2));f33=new String(sat.get(TAG_PHOLDER_3_3));f34=new String(sat.get(TAG_PHOLDER_3_4));f35=new String(sat.get(TAG_PHOLDER_3_5));
			f41=new String(sat.get(TAG_PHOLDER_4_1));f42=new String(sat.get(TAG_PHOLDER_4_2));f43=new String(sat.get(TAG_PHOLDER_4_3));f44=new String(sat.get(TAG_PHOLDER_4_4));f45=new String(sat.get(TAG_PHOLDER_4_5));
			f51=new String(sat.get(TAG_PHOLDER_5_1));f52=new String(sat.get(TAG_PHOLDER_5_2));f53=new String(sat.get(TAG_PHOLDER_5_3));f54=new String(sat.get(TAG_PHOLDER_5_4));f55=new String(sat.get(TAG_PHOLDER_5_5));
		}
		else
		if(mDayOfWeek==Calendar.SUNDAY)
		{
			//days=sun.get(TAG_DAYS);
			time_start=new String(sun.get(TAG_TIME_START));
			time_finish=new String(sun.get(TAG_TIME_FINISH));
			music_volume =new String(sun.get(TAG_MUSIC_VOLUME));
			adv_volume =new String(sun.get(TAG_ADV_VOLUME));


			time_start_1=new String(sun.get(TAG_TIME_START_1));time_end_1=new String(sun.get(TAG_TIME_END_1));
			time_start_2=new String(sun.get(TAG_TIME_START_2));time_end_2=new String(sun.get(TAG_TIME_END_2));
			time_start_3=new String(sun.get(TAG_TIME_START_3));time_end_3=new String(sun.get(TAG_TIME_END_3));
			time_start_4=new String(sun.get(TAG_TIME_START_4));time_end_4=new String(sun.get(TAG_TIME_END_4));
			time_start_5=new String(sun.get(TAG_TIME_START_5));time_end_5=new String(sun.get(TAG_TIME_END_5));

			f11=new String(sun.get(TAG_PHOLDER_1_1));f12=new String(sun.get(TAG_PHOLDER_1_2));f13=new String(sun.get(TAG_PHOLDER_1_3));f14=new String(sun.get(TAG_PHOLDER_1_4));f15=new String(sun.get(TAG_PHOLDER_1_5));
			f21=new String(sun.get(TAG_PHOLDER_2_1));f22=new String(sun.get(TAG_PHOLDER_2_2));f23=new String(sun.get(TAG_PHOLDER_2_3));f24=new String(sun.get(TAG_PHOLDER_2_4));f25=new String(sun.get(TAG_PHOLDER_2_5));
			f31=new String(sun.get(TAG_PHOLDER_3_1));f32=new String(sun.get(TAG_PHOLDER_3_2));f33=new String(sun.get(TAG_PHOLDER_3_3));f34=new String(sun.get(TAG_PHOLDER_3_4));f35=new String(sun.get(TAG_PHOLDER_3_5));
			f41=new String(sun.get(TAG_PHOLDER_4_1));f42=new String(sun.get(TAG_PHOLDER_4_2));f43=new String(sun.get(TAG_PHOLDER_4_3));f44=new String(sun.get(TAG_PHOLDER_4_4));f45=new String(sun.get(TAG_PHOLDER_4_5));
			f51=new String(sun.get(TAG_PHOLDER_5_1));f52=new String(sun.get(TAG_PHOLDER_5_2));f53=new String(sun.get(TAG_PHOLDER_5_3));f54=new String(sun.get(TAG_PHOLDER_5_4));f55=new String(sun.get(TAG_PHOLDER_5_5));
		}
		try
		{


			if(!time_start_1.equals("null"))
			{time_start_1_H=time_start_1.substring(0, 2);time_start_1_M=time_start_1.substring(3, 5);}

			if(!time_start_2.equals("null"))
			{time_start_2_H=time_start_2.substring(0, 2);time_start_2_M=time_start_2.substring(3, 5);}

			if(!time_start_3.equals("null"))
			{time_start_3_H=time_start_3.substring(0, 2);time_start_3_M=time_start_3.substring(3, 5);}

			if(!time_start_4.equals("null"))
			{  time_start_4_H=time_start_4.substring(0, 2);time_start_4_M=time_start_4.substring(3, 5);}

			if(!time_start_5.equals("null"))
			{ time_start_5_H=time_start_5.substring(0, 2);time_start_5_M=time_start_5.substring(3, 5);}

			if(!time_end_1.equals("null"))
			{ time_finish_1_H=time_end_1.substring(0, 2);time_finish_1_M=time_end_1.substring(3, 5);}

			if(!time_end_2.equals("null"))
			{time_finish_2_H=time_end_2.substring(0, 2);time_finish_2_M=time_end_2.substring(3, 5);}

			if(!time_end_3.equals("null"))
			{time_finish_3_H=time_end_3.substring(0, 2);time_finish_3_M=time_end_3.substring(3, 5);}

			if(!time_end_4.equals("null"))
			{ time_finish_4_H=time_end_4.substring(0, 2);time_finish_4_M=time_end_4.substring(3, 5);}

			if(!time_end_5.equals("null"))
			{ time_finish_5_H=time_end_5.substring(0, 2);time_finish_5_M=time_end_5.substring(3, 5);}
			if(time_start_1_H!=null)
			{s_1_H = Integer.parseInt(time_start_1_H);s_1_M = Integer.parseInt(time_start_1_M);}
			if(time_start_2_H!=null)
			{s_2_H = Integer.parseInt(time_start_2_H);s_2_M = Integer.parseInt(time_start_2_M);}
			if(time_start_3_H!=null)
			{s_3_H = Integer.parseInt(time_start_3_H);s_3_M = Integer.parseInt(time_start_3_M);}
			if(time_start_4_H!=null)
			{s_4_H = Integer.parseInt(time_start_4_H);s_4_M = Integer.parseInt(time_start_4_M);}
			if(time_start_5_H!=null)
			{s_5_H = Integer.parseInt(time_start_5_H);s_5_M = Integer.parseInt(time_start_5_M);}


			if(time_finish_1_H!=null)
			{f_1_H = Integer.parseInt(time_finish_1_H);f_1_M = Integer.parseInt(time_finish_1_M);}
			if(time_finish_2_H!=null)
			{f_2_H = Integer.parseInt(time_finish_2_H);f_2_M = Integer.parseInt(time_finish_2_M);}
			if(time_finish_3_H!=null)
			{f_3_H = Integer.parseInt(time_finish_3_H);f_3_M = Integer.parseInt(time_finish_3_M);}
			if(time_finish_4_H!=null)
			{f_4_H = Integer.parseInt(time_finish_4_H);f_4_M = Integer.parseInt(time_finish_4_M);}
			if(time_finish_5_H!=null)
			{f_5_H = Integer.parseInt(time_finish_5_H);f_5_M = Integer.parseInt(time_finish_5_M);}
		}
		catch(Exception e)
		{
			Log.d(TAG, "Error convert String to int:"+e);
		}

		try
		{
			// if(HM.equals(time_start_1))
			if(((mHour>=s_1_H-1)&&(mHour<f_1_H)))
			//	if(((mHour>=s_1_H)&&(mMinute>=s_1_M))&&((mMinute>=s_1_M)&&(mMinute<=f_1_M)))
			{
				folder1=new String(f11);
				folder2=new String(f12);
				folder3=new String(f13);
				folder4=new String(f14);
				folder5=new String(f15);
			}


			//if(HM.equals(time_start_2))
			if((mHour>=s_2_H-1)&&(mHour<f_2_H))
			{
				folder1=new String(f21);
				folder2=new String(f22);
				folder3=new String(f23);
				folder4=new String(f24);
				folder5=new String(f25);
			}

			//if(HM.equals(time_start_3))
			if((mHour>=s_3_H-1)&&(mHour<f_3_H))
			{
				folder1=new String(f31);
				folder2=new String(f32);
				folder3=new String(f33);
				folder4=new String(f34);
				folder5=new String(f35);

			}
			//if(HM.equals(time_start_4))
			if((mHour>=s_4_H-1)&&(mHour<f_4_H))
			{
				folder1=new String(f41);
				folder2=new String(f42);
				folder3=new String(f43);
				folder4=new String(f44);
				folder5=new String(f45);

			}
			//if(HM.equals(time_start_5))
			if((mHour>=s_5_H-1)&&(mHour<f_5_H))
			{
				folder1=new String(f51);
				folder2=new String(f52);
				folder3=new String(f53);
				folder4=new String(f54);
				folder5=new String(f55);

			}
		}
		catch(Exception ex)
		{
			Log.d(TAG, "Error time limit:"+ex);
		}

		PlayerActivity.getString(time_start,time_finish);

	}

	public void advList(ArrayList<HashMap<String, String>> advList, boolean gettime)
	{
		getTime = new Boolean(gettime);
		ADV = advList;

	}
	/**
	 * Function to read all mp3 files from sdcard
	 * and store the details in ArrayList
	 * */


	public ArrayList<HashMap<String, String>> getAdvList()
	{

		final String MEDIA_PATH_ADV = new String(way+folder_adv);

		File home_adv = new File(MEDIA_PATH_ADV);
		home_adv.mkdirs();

		String COUNT= new String("count");
		songsList.clear();
		songsList = getPlayList();

		//Play music and play adv in SetTime
		if(getTime){
			//create List , but not adv data == null

			return  AdvList();

		}


		ArrayList<HashMap<String, String>> AdvList = new ArrayList<HashMap<String, String>>();


		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

		Date data_begin = null;
		Date data_end = null;
		//Date data_now = null;
		data_begin = Calendar.getInstance().getTime();
		data_end = Calendar.getInstance().getTime();
		Date data_now = new Date(date.systemDate().getTime());
		//add
		data_now.setHours(0);
		data_now.setMinutes(0);
		data_now.setSeconds(0);

		if(songsList.size()<17) //17
		{

			return getPlayList();
		}
		Log.d("Реклама", "Размер ADV "+ADV.size());
		for(int i=0;i<ADV.size();i++)

		{
			try {

				data_begin = sdf.parse(ADV.get(i).get("data_begin"));
				data_end = sdf.parse(ADV.get(i).get("data_end"));
				Log.d("Реклама", "data_begin "+data_begin+"  data_end "+data_end);
			}
			catch(Exception e)
			{
				e.getMessage();
			};
			data_end.setHours(23);//23
			data_end.setMinutes(59); //59
			data_end.setSeconds(50); //50

			if(!(data_now.after(data_begin)&&data_now.before(data_end)))
			{
				Log.d("Реклама", "После и До" +data_now.after(data_begin)+"&&"+data_now.before(data_end));
				ADV.remove(i);
				i--;
			}else
			{
				Log.d("Реклама", "После и До" +data_now.after(data_begin)+"&&"+data_now.before(data_end));
				Log.d("Реклама", "Название " + ADV.get(i).get("adv_name") );
			}
		}
		for(int i=0;i<ADV.size();i++)
		{
			Log.d("Реклама", "после проверки на дату "+ ADV.get(i).get("adv_name"));
		}
		int time_begin_h;
		int time_end_h;

		Calendar c = Calendar.getInstance();
		time_now_H = c.get(Calendar.HOUR_OF_DAY)-1;
		// Calendar.getInstance();
		time_now_M= c.get(Calendar.MINUTE);

		for(int i=0;i<ADV.size();i++)
		{
			time_begin_h = Integer.parseInt(ADV.get(i).get("time_begin").toString().substring(0, 2))-1 ;
			time_end_h = Integer.parseInt(ADV.get(i).get("time_end").toString().substring(0, 2))-1;

			Log.d("Реклама", "время начала:конца "+time_begin_h+":"+time_end_h);

			if(!((time_begin_h<=time_now_H)&&(time_now_H<time_end_h)))
			{
				ADV.remove(i);
				i--;
			}

		}

		/////время
		if((folder_adv!="null"||folder_adv!=null)||home_adv.length()>0)
		{
			int i=0;
			boolean flag=false;
			int k=0;

			if (home_adv.listFiles(new FileExtensionFilter()).length > 0)
			{
				for(i=1;i<13;i++) //13
				{
					String COUNTF=COUNT+i;
					flag=false;

				 	for(int j=0;j<ADV.size();j++)
					{
						if(ADV.get(j).get(COUNTF).equals("1"))
						{
							HashMap<String, String> reklama = new HashMap<String, String>();
							reklama.put("songTitle", "adv_"+ADV.get(j).get(TAG_ADV_NAME).substring(0, (ADV.get(j).get(TAG_ADV_NAME).length()-4)));
							reklama.put("songPath", MEDIA_PATH_ADV + File.separator+ADV.get(j).get(TAG_ADV_NAME));
							reklama.put("songVolume",adv_volume);
							reklama.put("img", way+"img/"+icon_reklama+".png");
							AdvList.add(reklama);
							flag=true;
						}
					}

					int s=(i)%2;

					if(!flag&&s==0)
					{
						AdvList.add(songsList.get(k));
						k=k+1;
						AdvList.add(songsList.get(k));

					}
					else
					{
						AdvList.add(songsList.get(k));

					}
					k++;
				}
			}
			else
				Log.e(TAG,"Рекламы нет");

			for(i=k;i<30;i++) //30
			{
				if(songsList.size()>i)
				{
					AdvList.add(songsList.get(i));
				}
			}
			return AdvList;
		}
		else
			return getPlayList();


	}

	public ArrayList<HashMap<String, String>> AdvList() {

		final String MEDIA_PATH_ADV = new String(way+folder_adv);

		File home_adv = new File(MEDIA_PATH_ADV);
		home_adv.mkdirs();

		String COUNT= new String("count");
		songsList.clear();
		songsList = getPlayList();


		ArrayList<HashMap<String, String>> AdvList = new ArrayList<HashMap<String, String>>();

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date data_begin = null;
		Date data_end = null;
		Date data_now = null;
		data_now = Calendar.getInstance().getTime();

		if(songsList.size()<17) //17
		{

			return getPlayList();
		}
		Log.d("Реклама", "размер ADV "+ADV.size());
		for(int i=0;i<ADV.size();i++)
		{
			try {
				data_begin = sdf.parse(ADV.get(i).get("data_begin") );
				data_end = sdf.parse(ADV.get(i).get("data_end"));
				Log.d("Реклама", "data_begin "+data_begin+"  data_end "+data_end);
			}
			catch(Exception e)
			{
				e.getMessage();
			};
			data_end.setHours(0);
			data_end.setMinutes(0);
			data_end.setSeconds(0);

			if(!(data_now.after(data_begin)&&data_now.before(data_end)))
			{
				Log.d("Реклама", "После и До" +data_now.after(data_begin)+"&&"+data_now.before(data_end));
				ADV.remove(i);
				i--;
			}else
			{
				Log.d("Реклама", "После и До" +data_now.after(data_begin)+"&&"+data_now.before(data_end));
				Log.d("Реклама", "Название " + ADV.get(i).get("adv_name") );
			}
		}
		for(int i=0;i<ADV.size();i++)
		{
			Log.d("Реклама", "После проверки на дату "+ ADV.get(i).get("adv_name"));
		}
		int time_begin_h ;
		int time_end_h;
		Calendar c = Calendar.getInstance();
		time_now_H = c.get(Calendar.HOUR_OF_DAY)-1;
		// Calendar.getInstance();
		time_now_M= c.get(Calendar.MINUTE);

		for(int i=0;i<ADV.size();i++)
		{
			time_begin_h = Integer.parseInt(ADV.get(i).get("time_begin").toString().substring(0, 2))-1 ;
			time_end_h = Integer.parseInt(ADV.get(i).get("time_end").toString().substring(0, 2))-1;

			Log.d("Реклама", "Время начала:конца "+time_begin_h+":"+time_end_h);

			if(!((time_begin_h<=time_now_H)&&(time_now_H<time_end_h)))
			{
				ADV.remove(i);
				i--;
			}

		}

		/////время
		if((folder_adv!="null"||folder_adv!=null)||home_adv.length()>0)
		{
			int i=0;
			boolean flag=false;
			int k=0;

			if (home_adv.listFiles(new FileExtensionFilter()).length > 0)
			{
				for(i=1;i<13;i++)
				{
					String COUNTF=COUNT+i;
					flag=false;

					for(int j=0;j<ADV.size();j++)
					{
						if(ADV.get(j).get(COUNTF).equals("1"))
						{
							HashMap<String, String> reklama = new HashMap<String, String>();
							reklama.put("songTitle", "adv_"+ADV.get(j).get(TAG_ADV_NAME).substring(0, (ADV.get(j).get(TAG_ADV_NAME).length()-4)));
							reklama.put("songPath", null);
							reklama.put("songVolume",adv_volume);
							reklama.put("img", way+"img/"+icon_reklama+".png");
							AdvList.add(reklama);
							flag=true;
						}
					}

					int s=(i)%2;

					if(!flag&&s==0)
					{
						AdvList.add(songsList.get(k));
						k=k+1;
						AdvList.add(songsList.get(k));

					}
					else
					{
						AdvList.add(songsList.get(k));

					}
					k++;
				}
			}
			else
				Log.e(TAG,"Рекламы нет");

			for(i=k;i<30;i++)
			{
				if(songsList.size()>i)
				{
					AdvList.add(songsList.get(i));
				}
			}
			return AdvList;
		}
		else
			return getPlayList();

	}

	public ArrayList<HashMap<String, String>> CreateAdvList(){
		final String MEDIA_PATH_ADV = new String(way+folder_adv);

		File home_adv = new File(MEDIA_PATH_ADV);
		home_adv.mkdirs();

		String COUNT= new String("count");
		songsList.clear();
		songsList = getPlayList();

		ArrayList<HashMap<String, String>> AdvList = new ArrayList<HashMap<String, String>>();

		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		Date data_begin = null;
		Date data_end = null;
		Date data_now = null;
		data_now = Calendar.getInstance().getTime();

		if(songsList.size()<17) //17 songList
		{
			return getPlayList();
		}
		Log.d("Реклама", "размер ADV "+ADV.size());
		for(int i=0;i<ADV.size();i++)
		{
			try {
				data_begin = sdf.parse(ADV.get(i).get("data_begin") );
				data_end = sdf.parse(ADV.get(i).get("data_end"));
				Log.d("Реклама", "data_begin "+data_begin+"  data_end "+data_end);
			}
			catch(Exception e)
			{
				e.getMessage();
			};
			data_end.setHours(0);
			data_end.setMinutes(0);
			data_end.setSeconds(0);

			if(!(data_now.after(data_begin)&&data_now.before(data_end)))
			{
				Log.d("Реклама", "После и До" +data_now.after(data_begin)+"&&"+data_now.before(data_end));
				ADV.remove(i);
				i--;
			}else
			{
				Log.d("Реклама", "После и До" +data_now.after(data_begin)+"&&"+data_now.before(data_end));
				Log.d("Реклама", "Название " + ADV.get(i).get("adv_name") );
			}
		}
		for(int i=0;i<ADV.size();i++)
		{
			Log.d("Реклама", "После проверки на дату "+ ADV.get(i).get("adv_name"));
		}
		int time_begin_h ;
		int time_end_h;
		Calendar c = Calendar.getInstance();
		time_now_H = c.get(Calendar.HOUR_OF_DAY)-1;
		// Calendar.getInstance();
		time_now_M= c.get(Calendar.MINUTE);

		for(int i=0;i<ADV.size();i++)
		{
			time_begin_h = Integer.parseInt(ADV.get(i).get("time_begin").toString().substring(0, 2))-1 ;
			time_end_h = Integer.parseInt(ADV.get(i).get("time_end").toString().substring(0, 2))-1;

			Log.d("Реклама", "Время начала:конца "+time_begin_h+":"+time_end_h);

			if(!((time_begin_h<=time_now_H)&&(time_now_H<time_end_h)))
			{
				ADV.remove(i);
				i--;
			}

		}

		/////время
		if((folder_adv!="null"||folder_adv!=null)||home_adv.length()>0)
		{
			int i=0;

			if (home_adv.listFiles(new FileExtensionFilter()).length > 0)
			{
				for(i=1;i<13;i++)
				{
					String COUNTF=COUNT+i;


					for(int j=0;j<ADV.size();j++)
					{
						if(ADV.get(j).get(COUNTF).equals("1"))
						{
							HashMap<String, String> reklama = new HashMap<String, String>();
							reklama.put("songTitle", "adv_"+ADV.get(j).get(TAG_ADV_NAME).substring(0, (ADV.get(j).get(TAG_ADV_NAME).length()-4)));
							reklama.put("songPath", MEDIA_PATH_ADV + File.separator+ADV.get(j).get(TAG_ADV_NAME));
							reklama.put("songVolume",adv_volume);
							reklama.put("block_time", COUNTF);
							AdvList.add(reklama);

						}
					}


				}
			}
			else
				Log.e(TAG,"Рекламы нет");


			//Sort the filenames by songTitle
			Collections.sort(AdvList,new Comparator<HashMap<String,String>>(){
				public int compare(HashMap<String,String> mapping1,HashMap<String,String> mapping2){
					return mapping1.get("songTitle").compareTo(mapping2.get("songTitle"));
				}
			});

			return AdvList;
		}
		else
			return AdvList;

	}

	public static List<Date> generateAdvList (Date Start_Time, Date Stop_Time) {
		System.out.println("Adv start time  " + Start_Time.toString());
		System.out.println("Adv end time  " + Stop_Time.toString());
		ArrayList<Date> AdvList = new ArrayList<Date>();
		Date temp = new Date(date.systemDate().getTime());
		temp.setHours(0);
		temp.setMinutes(0);
		temp.setSeconds(0);
		int y = 0;
		while (y < 288) {
			Date d = new Date(temp.getTime());
			d.setMinutes(y * 5);
			d.setSeconds(0);
			if (d.after(Start_Time) & d.before(Stop_Time)) {
				AdvList.add(d);
				System.out.println("Adv block  " + d.toString());
			} else if (d.getYear() == Start_Time.getYear() & d.getMonth() == Start_Time.getMonth() & d.getDate() == Start_Time.getDate() & d.getHours() == Start_Time.getHours() & d.getMinutes() == Start_Time.getMinutes() & d.getSeconds() == Start_Time.getSeconds() | d.getYear() == Stop_Time.getYear() & d.getMonth() == Stop_Time.getMonth() & d.getDate() == Stop_Time.getDate() & d.getHours() == Stop_Time.getHours() & d.getMinutes() == Stop_Time.getMinutes() & d.getSeconds() == Stop_Time.getSeconds()) {
				AdvList.add(d);
				System.out.println("Adv block  " + d.toString());
			}
			++y;
		}
		return AdvList;
	}

	///////////////////////////////////////////////////////////////////////
	public ArrayList<HashMap<String, String>> getPlayList()
	{

		songsList.clear();
		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

		ArrayList<HashMap<String, String>> songsList1 = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> songsList2 = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> songsList3 = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> songsList4 = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<String, String>> songsList5 = new ArrayList<HashMap<String, String>>();

		final String MEDIA_PATH1 = new String(way+folder1);
		final String MEDIA_PATH2 = new String(way+folder2);
		final String MEDIA_PATH3 = new String(way+folder3);
		final String MEDIA_PATH4 = new String(way+folder4);
		final String MEDIA_PATH5 = new String(way+folder5);

		File home1 = new File(MEDIA_PATH1);
		File home2 = new File(MEDIA_PATH2);
		File home3 = new File(MEDIA_PATH3);
		File home4 = new File(MEDIA_PATH4);
		File home5 = new File(MEDIA_PATH5);
		int f1l=0,f2l=0,f3l=0,f4l=0,f5l=0;
		try
		{

			if(folder1!="null")
			{
				if (home1.listFiles(new FileExtensionFilter()).length > 0)
				{ f1l=home1.listFiles(new FileExtensionFilter()).length;
					Log.d("WAY", "file1 "+ home1.listFiles(new FileExtensionFilter()).length);
					for (File file : home1.listFiles(new FileExtensionFilter()))
					{
						HashMap<String, String> song = new HashMap<String, String>();
						song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
						song.put("songPath", file.getPath());
						song.put("songVolume",music_volume);
						song.put("img",  way+"img/"+folder1+".png");

						// Adding each song to SongList
						songsList1.add(song);

					}

				}
				else
					Log.d(TAG, "file1 :home1.listFiles(new FileExtensionFilter()).length > 0");
			}
			else
			{
				Log.d(TAG, "file1 null");
			}
		}


		catch (Exception e)
		{
			Log.d(TAG, "Папка отсутствует");

			//return songsList;
		}

		try{

			if(folder2!="null")
			{
				if (home2.listFiles(new FileExtensionFilter()).length > 0)
				{
					f2l=home2.listFiles(new FileExtensionFilter()).length;
					Log.d("WAY", "file2 "+home2.listFiles(new FileExtensionFilter()).length);
					for (File file : home2.listFiles(new FileExtensionFilter()))
					{
						HashMap<String, String>  song2 = new HashMap<String, String>();
						song2.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
						song2.put("songPath", file.getPath());
						song2.put("songVolume",music_volume);
						song2.put("img", way+"img/"+folder2+".png");


						// Adding each song to SongList
						songsList2.add(song2);


					}

				}
				else
					Log.d(TAG, "file2 :home2.listFiles(new FileExtensionFilter()).length > 0");
			}
			else
			{
				Log.d(TAG, "file2 null");
			}
		}


		catch (Exception e)
		{
			Log.d(TAG, "Папка 2 отстутствует");
			//return songsList;
		}

		try
		{
			if(folder3!="null")
			{
				if (home3.listFiles(new FileExtensionFilter()).length > 0)
				{f3l=home3.listFiles(new FileExtensionFilter()).length;
					Log.d(TAG, "file3 ");
					for (File file : home3.listFiles(new FileExtensionFilter()))
					{
						HashMap<String, String>  song3 = new HashMap<String, String>();
						song3.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
						song3.put("songPath", file.getPath());
						song3.put("songVolume",music_volume);
						song3.put("img",  way+"img/"+folder3+".png");

						// Adding each song to SongList
						songsList3.add(song3);
					}
				}
			}
			else
			{
				Log.d(TAG, "file3 null");
			}
		}


		catch (Exception e)
		{
			Log.d(TAG, "Папка 3 отсутствует");
			//return songsList;
		}
		try
		{
			if(folder4!="null")
			{
				if (home4.listFiles(new FileExtensionFilter()).length > 0)
				{f4l=home4.listFiles(new FileExtensionFilter()).length;
					Log.d(TAG, "file4 ");
					for (File file : home4.listFiles(new FileExtensionFilter()))
					{
						HashMap<String, String>  song4 = new HashMap<String, String>();
						song4.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
						song4.put("songPath", file.getPath());
						song4.put("songVolume",music_volume);
						song4.put("img",  way+"img/"+folder4+".png");

						// Adding each song to SongList
						songsList4.add(song4);

					}
				}
			}
			else
			{
				Log.d(TAG, "file4 null");
			}
		}


		catch (Exception e)
		{
			Log.d(TAG, "Папка 4 отсутствует");
			//return songsList;
		}
		try
		{
			if(folder5!="null")
			{
				if (home5.listFiles(new FileExtensionFilter()).length > 0)
				{
					f5l=home5.listFiles(new FileExtensionFilter()).length;
					Log.d(TAG, "file5 ");
					for (File file : home5.listFiles(new FileExtensionFilter()))
					{
						HashMap<String, String> song5 = new HashMap<String, String>();

						song5.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
						song5.put("songPath", file.getPath());
						song5.put("songVolume",music_volume);
						song5.put("img",  way+"img/"+folder5+".png");

						// Adding each song to SongList
						songsList5.add(song5);

					}
				}
			}
			else
			{
				Log.d(TAG, "file5 null");
			}
		}


		catch (Exception e)
		{
			Log.d(TAG, "Папка 5 отсутствует");
			//return songsList;
		}
		try
		{
			Log.d("RAND","файлов в папках "+f1l+" "+f2l+" "+f3l+" "+f4l+" "+f5l);
			int q=0,w=0,e=0,r=0,t=0;
			int[] a=random(f1l);
			int[] b=random(f2l);
			int[] c=random(f3l);
			int[] d=random(f4l);
			int[] f=random(f5l);

			for(int i=0;i<50;i++)
			{

				if(i<f1l)
				{

					songsList.add(songsList1.get(a[q]));
					//Log.d("RAND1","1:"	+songsList1.get(a[q]));
					q++;

				}
				if(i<f2l)
				{

					songsList.add(songsList2.get(b[w]));
					//Log.d("RAND1","2:"	+songsList2.get(b[w]));
					w++;
				}
				if(i<f3l)
				{

					songsList.add(songsList3.get(c[e]));
					//Log.d("RAND1","3:"	+songsList3.get(c[e]));
					e++;
				}
				if(i<f4l)
				{

					songsList.add(songsList4.get(d[r]));
					//Log.d("RAND1","4:"	+songsList4.get(d[r]));
					r++;
				}
				if(i<f5l)
				{

					songsList.add(songsList5.get(f[t]));
					//Log.d("RAND1","5:"	+songsList5.get(f[t]));
					t++;
				}
			}
			//	songsList1.removeAll(songsList1);
			songsList1.clear();
			songsList2.clear();
			songsList3.clear();
			songsList4.clear();
			songsList5.clear();
			//	Random random = new Random();
			//Collections.shuffle(songsList, random);
		}
		catch (Exception e)
		{
			Log.d(TAG, "Exception shuffle:"+e);

		}
		songsList.trimToSize();

		//	Log.d("RAND", "\n"+songsList);
		System.gc();
		return songsList;

	}



	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {

		public boolean accept(File dir, String name)
		{

			return (name.endsWith(".mp3") || name.endsWith(".MP3"));


		}
	}

	private static int[] random(int r)
	{

		int razmer = r;
		int [] mass =new int [razmer] ;
		Random rand = new Random();
		Arrays.fill(mass,-1);
		int j;

		for (int i = mass.length -1 ; i >= 0;)
		{
			j = rand.nextInt(razmer);
			if (mass[j]==-1)
			{
				mass[j]=i;
				int key = 0;
				for (int k = 0; k < mass.length;)
				{
					if (mass[k] == -1)
					{
						i--;
						k = mass.length;
						key=1;
					}
					k++;
				}
				if (key == 0)
				{
					i = -1;
				}

			}
		}
		Log.d("RAND1", "array:"+Arrays.toString(mass));
		return mass;
	}
}

