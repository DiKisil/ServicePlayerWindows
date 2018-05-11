package ua.example.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import ua.example.download.IMGdownload;
import ua.example.download.MUSICdownload;
import ua.example.ftp.*;
import ua.example.settings.DirectoryChooserDialog;
import ua.example.settings.Setting;
import ua.example.json.*;
import ua.player.musicmallce.R;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import android.app.ListActivity;

import static android.app.Service.START_STICKY;


@SuppressWarnings("all")
public class AndroidBuildingMusicPlayerActivity extends ListActivity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener {

	private static final String TAG = "MainActivity";
	private ImageButton btnSett;
	private ImageButton btnPlay;
	private ImageButton Save;


	private SeekBar songProgressBar;

	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;    // Media Player
	private TextView ObjInfo, Version;
	private TextView statusss;

	private MediaPlayer mp;
	private AudioManager volume;

	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();
	private Handler mHandlerAdv = new Handler();
	private Handler mUpdateAdsHandler = new Handler();
	boolean running = false;

	private SongsManager songManager;
	private Utilities utils;
	private MainActivity ftp;
	private JSONParsingMusic JSONParsingMusic;
	private JSONParsingAdv JSONParsingAdv;
	private Directory der_created;
	private IMGdownload IMGdownload;



	private int currentSongIndex = 0;
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	private int currentSongIndexAdv = 0;
	private ArrayList<HashMap<String, String>> songsListAdv = new ArrayList<HashMap<String, String>>();
	private ArrayList<HashMap<String, String>> ListPlayAdv = new ArrayList<HashMap<String, String>>();

	private String Sobject, Sname, Sftp, Sid, Spassword, Srefresh, Sdir;
	private boolean download_music, stop_settings, start_settings;
	boolean first_start;
	boolean getTime;

	private int maxMusicVolume;
	private double procentMusic = 0;
	private int indexVolumeMusic = 0;
	private int valueMusic = 0;


	private Context cntx = null;
	private Handler SongsList = new Handler();
	private boolean newlist;

	private Calendar c;
	private String Minute;
	private static String Start, Stop;
	private SharedPreferences prefs;

	private WifiLock wifiLock;
	private WakeLock fullWakeLock;
	private WakeLock partialWakeLock;

	private Intent intent;
	private PendingIntent pendingIntent;

	ProgressDialog progressDialog;


	private AlarmManager alarm_manager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.player);



		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Loneworker - FULL WAKE LOCK");
		partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Loneworker - PARTIAL WAKE LOCK");
		WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (Build.VERSION.SDK_INT < 16) //12
			wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, "MyWifiLock");
		else
			wifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "MyWifiLock");

		alarm_manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		intent = new Intent(this, AlarmReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);


		c = Calendar.getInstance();

		int mMinute = c.get(Calendar.MINUTE);
		Minute = pad(mMinute);

		// All player buttons
		btnSett = (ImageButton) findViewById(R.id.sett); //add
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		Save = (ImageButton) findViewById(R.id.save);

		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songTitleLabel = (TextView) findViewById(R.id.songTitle);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
		ObjInfo = (TextView) findViewById(R.id.object);
		Version = (TextView) findViewById(R.id.version);
		try {
			Version.setText(getResources().getString(R.string.Version) + " " + this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
		} catch (NameNotFoundException e1) {
			//
			e1.printStackTrace();
		}
		statusss = (TextView) findViewById(R.id.Status);
		volume = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		cntx = this.getBaseContext();
		Constants Constants = new Constants(cntx);
		/*Created DIR*/
		der_created = new Directory();

		Settings();
		if (first_start) {

			new AlertDialog.Builder(AndroidBuildingMusicPlayerActivity.this)
					.setTitle("Выбор карты памяти")
					.setMessage("Укажите путь к карте памяти!")
					.setPositiveButton("Хорошо", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									String m_chosenDir = "";

									// Create DirectoryChooserDialog and register a callback
									DirectoryChooserDialog directoryChooserDialog =
											new DirectoryChooserDialog(AndroidBuildingMusicPlayerActivity.this,
													new DirectoryChooserDialog.ChosenDirectoryListener() {
														@Override
														public void onChosenDir(String chosenDir) {

															Toast.makeText(AndroidBuildingMusicPlayerActivity.this, "Директория: " + chosenDir, Toast.LENGTH_LONG).show();
															prefs.edit().putString(getString(R.string.pref_way), chosenDir).commit();
															Settings();
														}
													});
									directoryChooserDialog.chooseDirectory(m_chosenDir);
								}
							}
					).setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {

				}
			}).show();
			prefs.edit().putBoolean("first_start", false).commit();
		}


		// Mediaplayer
		mp = new MediaPlayer();

		songManager = new SongsManager();
		utils = new Utilities();


		// Listeners
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); // Important
		ftp.createDummyFile(getString(R.string.start_application), cntx);
		statusss.setText("status...");

		// Getting all songs list
		songsList = songManager.getAdvList();
		List();
		Log.i("MainActivity", "Found dir:" + ua.example.player.Constants.WAY_MUSIC());

		/**
		 * Play button click event
		 * plays a song and changes button to pause image
		 * pauses a song and changes button to play image
		 * */

		//add listener

		btnSett.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AndroidBuildingMusicPlayerActivity.this, Setting.class);
				startActivity(intent);

			}
		});

		Save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				progressDialog = new ProgressDialog(AndroidBuildingMusicPlayerActivity.this);
				progressDialog.setMax(100);
				progressDialog.setMessage("Please wait..."); // Setting Message
				progressDialog.setTitle("Download files"); // Setting Title
				progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Horizontal
				progressDialog.show(); // Display Progress Dialog
				progressDialog.setCancelable(false);

				new Thread(new Runnable() {
					public void run() {
						try {
							while (progressDialog.getProgress() <= progressDialog.getMax()) {

								try {
									Thread.sleep(1000);
									JSONParsingMusic.AndroidJSONPars();
									JSONParsingAdv.AndroidJSONPars();
								} catch (Exception e) {
									e.printStackTrace();
								}
								if (progressDialog.getProgress() == progressDialog.getMax()) {
									progressDialog.dismiss();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();

				ftp.DU_ftp(cntx);

				statusss.setText("Download.. ");

				// check for already playing

				/*ftp.DU_ftp(cntx);
				statusss.setText("Download.. ");

				try {
					Thread.sleep(2500);
					JSONParsingMusic.AndroidJSONPars();
					JSONParsingAdv.AndroidJSONPars();
				} catch (Exception e) {
					e.printStackTrace();
				}*/



			}
		});



		btnPlay.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View arg0) {

				// check for already playing
				if (mp.isPlaying()) {
					if (mp != null) {

						//mUpdateAdsHandler.removeCallbacks(mAdvStartInTime);
						mp.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				} else {

					ftp.DU_ftp(cntx);
					statusss.setText("Play ");

					try {
						Thread.sleep(2500);
						JSONParsingMusic.AndroidJSONPars();
						JSONParsingAdv.AndroidJSONPars();
					} catch (Exception e) {
						e.printStackTrace();
					}
					;
					ftp.createDummyFile(getString(R.string.generation_play), cntx);
					newlist = true;
					List();

					// Resume song
					if(mp!=null)
					{
						newlist=false;
						List();
						for(int i=0;i<60; i++) //60
						{

				 			String ii =pad(i);
							if(Minute.equals(ii))
							{
								currentSongIndex= i / 3;
								break;
							}
						}


						playSong(currentSongIndex);
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
				}

			}
		});
		/**
		 * Next button click event
		 * Plays next song by taking currentSongIndex + 1
		 * */


		/**
		 * Back button click event
		 * Plays previous song by currentSongIndex - 1
		 * */


		/** выход рекламы точно по времени*/
		//mUpdateAdsHandler.postDelayed(mAdvStartInTime, 0);
		/**обновления плейдиста раз в час, тоесть 8:58 9:58 ....21:58*/
		SongsList.postDelayed(Update, 0);

	}    ////close OnCreate///////////////////////////////


	void AlertDialog(String Title, String Massage) {
		new AlertDialog.Builder(AndroidBuildingMusicPlayerActivity.this)
				.setTitle(Title)
				.setMessage(Massage)
				.setPositiveButton("Хорошо", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								onDestroy();
								System.exit(0);
							}
						}
				).setOnCancelListener(new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				onDestroy();
				System.exit(0);
			}
		}).show();

	}

	void AlertDialogInfo(String Title, String Massage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(AndroidBuildingMusicPlayerActivity.this);
		builder.setTitle(Title);
		builder.setMessage(Massage);
		builder.setCancelable(true);
		builder.setPositiveButton("Хорошо", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				}
		);

		final AlertDialog dlg = builder.create();

		dlg.show();

		final Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				dlg.dismiss(); // when the task active then close the dialog
				t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
			}
		}, 30 * 1000); // after 20 second (or 20000 miliseconds), the task will be active.

	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mp.isPlaying()) {
			if (mp != null) {

				//mUpdateAdsHandler.removeCallbacks(mAdvStartInTime);
				mp.pause();
				// Changing button image to play button
				btnPlay.setImageResource(R.drawable.btn_play);
			}
		} else {

			ftp.DU_ftp(cntx);
			statusss.setText("Play ");

			try {
				Thread.sleep(2500);
				JSONParsingMusic.AndroidJSONPars();
				JSONParsingAdv.AndroidJSONPars();
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
			ftp.createDummyFile(getString(R.string.generation_play), cntx);
			newlist = true;
			List();

			// Resume song
			if(mp!=null)
			{
				newlist=false;
				List();
				for(int i=0;i<60; i++) //60
				{

					String ii =pad(i);
					if(Minute.equals(ii))
					{
						currentSongIndex= i / 3;
						break;
					}
				}


				playSong(currentSongIndex);
				// Changing button image to pause button
				//btnPlay.setImageResource(R.drawable.btn_pause);
			}
		}
		return START_STICKY;

	}



	private Runnable Update = new Runnable() {

		public void run() {


			c = Calendar.getInstance();

			int mHour = c.get(Calendar.HOUR_OF_DAY);
			int mMinute = c.get(Calendar.MINUTE);
			int mSecond = c.get(Calendar.SECOND);
			Minute = pad(mMinute);

			String mTime = ((pad(mHour)) + (":") + (pad(mMinute)) + (":") + (pad(mSecond)));
			String MinSec = pad(mMinute) + (":") + pad(mSecond);
			String HM = ((pad(mHour)) + (":") + (pad(mMinute)));

			if (MinSec.equals("58:30")) //58:30
			{

				Log.i(TAG, "Time " + HM + Stop + Start);


				try {
					JSONParsingMusic.AndroidJSONPars();
					JSONParsingAdv.AndroidJSONPars();
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Error list parse" + e);
				}
				newlist = true;
				List();
				Log.i(TAG, "List new build:" + mTime);
				ftp.createDummyFile(getString(R.string.generation), cntx);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			//              ���������� ���� ��������, ����� �����          ///

		/*	if(Srefresh.equals("1"))
			{
				//Log.i(TAG, "REFRESHHHH : 1");

				if(  MinSec.equals("55:00") )

				{
					Log.i(TAG,"Update all configs,logs, and Parse new settings"+mTime);
					try {
						ftp.DU_ftp(cntx);

						statusss.setText("play "+mTime);
					}

					catch(Exception e)
					{
						statusss.setText("play "+mTime);
						e.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.gc();

				}
			}*/

			if (Srefresh.equals("30")) {
				if (MinSec.equals("25:00") || MinSec.equals("55:00"))

				{
					Log.i(TAG, "Update all configs,logs, and Parse new settings" + mTime);
					try {
						ftp.DU_ftp(cntx);
						statusss.setText("play " + mTime);
					} catch (Exception e) {
						statusss.setText("play " + mTime);
						e.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.gc();

				}
			}
			if (Srefresh.equals("15")) {
				if (MinSec.equals("00:00") || MinSec.equals("30:00"))

				{
					Log.i(TAG, "Update all configs,logs, and Parse new settings" + mTime);
					try {
						ftp.DU_ftp(cntx);
						statusss.setText("play " + mTime);

					} catch (Exception e) {
						ftp.DU_ftp(cntx); //add
						statusss.setText("play " + mTime);
						e.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					System.gc();
				}
			}
			///             ���� ����          //

			if (HM.equals(Stop) && stop_settings) {
				Log.i(TAG, "Stop");
				ftp.createDummyFile(getString(R.string.stop), cntx);
				mp.stop();
				//mUpdateAdsHandler.removeCallbacks(mAdvStartInTime);
				// Changing button image to play button
				btnPlay.setImageResource(R.drawable.btn_play);

				if (stop_settings)
					reload_actvity();
			}

			if (HM.equals(Start) && start_settings) {

				prefs.edit().putBoolean("stop_play", true).commit();
				prefs.edit().putBoolean("start_play", false).commit();
				stop_settings = prefs.getBoolean("stop_play", true);
				start_settings = prefs.getBoolean("start_play", true);

				ftp.createDummyFile(getString(R.string.play), cntx);
				Log.i(TAG, "Start");
				newlist = false;
				List();


				for (int i = 0; i < 60; i++) {
					String ii = pad(i);
					if (Minute.equals(ii)) {
						currentSongIndex = i / 3;
						break;
					}
				}
				playSong(currentSongIndex);
				// Changing button image to pause button
				btnPlay.setImageResource(R.drawable.btn_pause);
			}


			try
					   {
		 if(running){

				ListPlayAdv.clear();

					   if(Minute.equals("00")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count1")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }
						   } if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }
					   if(Minute.equals("05")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count2")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }

						   }if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }
					   if(Minute.equals("10")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count3")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }

						   } if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }
					   if(Minute.equals("15")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count4")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }

						   } if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }
					   if(Minute.equals("20")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count5")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }

						   }if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }
					   if(Minute.equals("25")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count6")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }

						   } if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }
					   if(Minute.equals("30")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count7")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }

						   }if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }
					   if(Minute.equals("35")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count8")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }

						   }if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }
					   if(Minute.equals("40")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count9")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }

						   }if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }
					   if(Minute.equals("45")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count10")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }

						   }if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }
					   if(Minute.equals("50")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count11")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }

						   }if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }
					   if(Minute.equals("55")){
						   for(int i=0;i<songsListAdv.size(); i++){

							   if(songsListAdv.get(i).get("block_time").equals("count12")){
								   ListPlayAdv.add(songsListAdv.get(i));
							   }

						   }if(ListPlayAdv.size()!=0)
							   playAdv(currentSongIndexAdv);
					   }

	}

				   }
				   catch(Exception time)
				   {
					   time.getMessage();
				   }
			   // Running this thread after 200 milliseconds
			   SongsList.postDelayed(this, 200);

		   }
		};

	void reload_actvity()
	{
		int delay=5000;//5 sec
		prefs.edit().putBoolean("start_play", true).commit();
		prefs.edit().putBoolean("stop_play", false).commit();


		PendingIntent intent = PendingIntent.getActivity(this.getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
		AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		ftp.createDummyFile(getString(R.string.stop_application), cntx);
		manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
		System.exit(2);


	}


	private static String pad(int c)
	{
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}



	public void List()
	{
		getTime = SongsManager.getTime;
		if(getTime){
			try{

				//mUpdateAdsHandler.removeCallbacks(mAdvStartInTime);

				JSONParsingAdv.AndroidJSONPars();

				songsListAdv = songManager.CreateAdvList();
				running = true;


			}catch(Exception adv){
				adv.getMessage();
			}
		}
		ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();


		// get all songs from sdcard
		songsList = songManager.getAdvList();

		// looping through playlist
		for (int i = 0; i < songsList.size(); i++)
		{
			HashMap<String, String> song = songsList.get(i);
			songsListData.add(song);
		}


		// Adding menuItems to ListView
		ListAdapter adapter = new SimpleAdapter(this, songsListData,
				R.layout.playlist_item,
				new String[] {"img", "songTitle" },
				new int[] {R.id.imageView1, R.id.songTitle });

		setListAdapter(adapter);

	}


	/**
	 * Background Runnable thread
	 * */
	/*private Runnable mAdvStartInTime = new Runnable() {

		   public void run() {

				   try
				   {
	 if(running){
			 c = Calendar.getInstance();
		 	int mMinute = c.get(Calendar.MINUTE);
		     Minute=pad(mMinute);

			ListPlayAdv.clear();

				   if(Minute.equals("00")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count1")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }
					   } if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }
				   if(Minute.equals("05")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count2")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }

					   }if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }
				   if(Minute.equals("10")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count3")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }

					   } if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }
				   if(Minute.equals("15")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count4")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }

					   } if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }
				   if(Minute.equals("20")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count5")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }

					   }if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }
				   if(Minute.equals("25")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count6")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }

					   } if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }
				   if(Minute.equals("30")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count7")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }

					   }if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }
				   if(Minute.equals("35")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count8")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }

					   }if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }
				   if(Minute.equals("40")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count9")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }

					   }if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }
				   if(Minute.equals("45")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count10")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }

					   }if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }
				   if(Minute.equals("50")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count11")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }

					   }if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }
				   if(Minute.equals("55")){
					   for(int i=0;i<songsListAdv.size(); i++){

						   if(songsListAdv.get(i).get("block_time").equals("count12")){
							   ListPlayAdv.add(songsListAdv.get(i));
						   }

					   }if(ListPlayAdv.size()!=0)
						   playAdv(currentSongIndexAdv);
				   }

}

			   }
			   catch(Exception time)
			   {
				   time.getMessage();
			   }
			   // Running this thread after 200 milliseconds
			   mUpdateAdsHandler.postDelayed(this, 1000);

		   }
		};*/

	@Override
	public void onLowMemory() {
		Log.e(TAG, "Low Memory!!");
	}

	public void playAdv(int songIndex){
		// Play song
		String songTitle= "";
		try {

			//mUpdateAdsHandler.removeCallbacks(mAdvStartInTime);

			System.gc();

			running = false;
			VolumeAutoDown();
			mp.reset();
			mp.setDataSource(ListPlayAdv.get(songIndex).get("songPath"));
			VolumeAdv(songIndex);
			mp.prepare();

			mp.start();
			// Displaying Song title
			songTitle = ListPlayAdv.get(songIndex).get("songTitle");
			songTitleLabel.setText(songTitle);
			/////////////////////////
			ftp.createDummyFile(songTitle, cntx);
			/////////////////////////////
			// Changing Button Image to pause image
			btnPlay.setImageResource(R.drawable.btn_pause);

			// set Progress bar values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);


			// Updating progress bar
			updateProgressBar();
		}

		catch (IllegalArgumentException e) {
			ftp.createDummyFile(getString(R.string.error)+" "+songTitle+" IllegalArgumentException: "+e.getMessage(), cntx);
			mp.stop();
			// Changing button image to play button
			btnPlay.setImageResource(R.drawable.btn_play);
			Log.e(TAG, "IllegalArgumentException: "+  e.getMessage());
			AlertDialogInfo("Ошибка", "IllegalArgumentException: "+   e.getMessage());

		} catch (IllegalStateException e) {
			ftp.createDummyFile(getString(R.string.error)+" "+songTitle+" IllegalStateException: "+e.getMessage(), cntx);
			mp.stop();
			// Changing button image to play button
			btnPlay.setImageResource(R.drawable.btn_play);
			Log.e(TAG, "IllegalStateException: "+ e.getMessage());
			AlertDialogInfo("Ошибка", "IllegalStateException: "+   e.getMessage());

		} catch (IOException e) {
			ftp.createDummyFile(getString(R.string.error)+" "+songTitle+" IOException: "+e.getMessage(), cntx);
			mp.stop();
			// Changing button image to play button
			btnPlay.setImageResource(R.drawable.btn_play);
			Log.e(TAG, "IOException: "+   e.getMessage());
			AlertDialogInfo("Ошибка", "Файл для воспроизведения не найден или не успел загрузиться!");

		}
		catch(Exception e)
		{
			ftp.createDummyFile(getString(R.string.error)+" "+songTitle+" Exception: "+e.getMessage(), cntx);
			Log.e(TAG, "Exception: "+ e.getMessage());


			mp.stop();
			// Changing button image to play button
			btnPlay.setImageResource(R.drawable.btn_play);
			AlertDialogInfo("Ошибка","Файл был удален из списка воспроизведения или не найден!");
		}

	}
	/**
	 * Function to play a song
	 * @param songIndex - index of song
	 * */
	public void  playSong(int songIndex){
		// Play song
		try{

			while(songsList.get(songIndex).get("songPath") == null)
			{
				if(songIndex < (songsList.size() - 1))
				{
					songIndex ++;
				}
				else{
					songIndex = 0;
				}

			}
			currentSongIndex = songIndex ;

		}catch(Exception e){
			e.printStackTrace();
		}
		String songTitle = "";
		try {


			mp.reset();
			mp.setDataSource(songsList.get(songIndex).get("songPath"));
			Volume(songIndex);
			mp.prepare();

			mp.start();
			// Displaying Song title
			songTitle = songsList.get(songIndex).get("songTitle");
			songTitleLabel.setText(songTitle);
			/////////////////////////
			ftp.createDummyFile(songTitle, cntx);
			/////////////////////////////
			// Changing Button Image to pause image
			btnPlay.setImageResource(R.drawable.btn_pause);

			// set Progress bar values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);

			// Updating progress bar
			updateProgressBar();

		}
		catch (IllegalArgumentException e) {
			ftp.createDummyFile(getString(R.string.error)+" "+songTitle+" IllegalArgumentException: "+e.getMessage(), cntx);
			mp.stop();
			// Changing button image to play button
			btnPlay.setImageResource(R.drawable.btn_play);
			Log.e(TAG, "IllegalArgumentException: "+  e.getMessage());
			AlertDialogInfo("Ошибка", "IllegalArgumentException: "+   e.getMessage());

		} catch (IllegalStateException e) {
			ftp.createDummyFile(getString(R.string.error)+" "+songTitle+" IllegalStateException: "+e.getMessage(), cntx);
			mp.stop();
			// Changing button image to play button
			btnPlay.setImageResource(R.drawable.btn_play);
			Log.e(TAG, "IllegalStateException: "+ e.getMessage());
			AlertDialogInfo("Ошибка", "IllegalStateException: "+   e.getMessage());

		} catch (IOException e) {
			ftp.createDummyFile(getString(R.string.error)+" "+songTitle+" IOException: "+e.getMessage(), cntx);
			mp.stop();
			// Changing button image to play button
			btnPlay.setImageResource(R.drawable.btn_play);
			Log.e(TAG, "IOException: "+   e.getMessage());
			AlertDialogInfo("Ошибка", "Файл для воспроизведения не найден или не успел загрузиться!");

		}
		catch(Exception e)
		{
			ftp.createDummyFile(getString(R.string.error)+" "+songTitle+" Exception: "+e.getMessage(), cntx);
			Log.e(TAG, "Exception: "+ e.getMessage());


			mp.stop();
			// Changing button image to play button
			btnPlay.setImageResource(R.drawable.btn_play);
			AlertDialogInfo("Ошибка","Файл был удален из списка воспроизведения или не найден");
		}

	}


	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			try
			{
				long currentDuration = mp.getCurrentPosition();
				long totalDuration = mp.getDuration();


				// Displaying time completed playing
				songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
				// Displaying Total Duration time
				songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));


				// Updating progress bar
				int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
				//Log.d("Progress", ""+progress);
				songProgressBar.setProgress(progress);
			}
			catch(Exception time)
			{
				time.getMessage();
			}
			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);

		}
	};

	/**
	 *
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

	}

	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	/**
	 * When user stops moving the progress hanlder
	 * */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

		// forward or backward to certain seconds
		mp.seekTo(currentPosition);

		// update timer progress again
		updateProgressBar();
	}

	private void updateAdv() {
		if (mUpdateAdsHandler == null) mUpdateAdsHandler = new Handler();
		int fiveMinute = 5 * 60_000;
		//mUpdateAdsHandler.postDelayed(mAdvStartInTime, fiveMinute);
	}


	/**
	 * On Song Playing completed
	 *
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0)
	{

		if(newlist)
		{
			newlist=false;
			currentSongIndex=0;
			playSong(0);

		}
		else if(ListPlayAdv.size()==0)
		{
			// play next song
			if(currentSongIndex < (songsList.size() - 1))
			{
				playSong(currentSongIndex + 1);
				currentSongIndex = currentSongIndex + 1;
			}
			else{
				// play first song
				playSong(0);
				currentSongIndex = 0;
			}
		}

		if(getTime && ListPlayAdv.size()!=0){
			if(currentSongIndexAdv < (ListPlayAdv.size() - 1 ))
			{
				currentSongIndexAdv ++;
				playAdv(currentSongIndexAdv );

			}else{

				/*currentSongIndex++;
				playSong(currentSongIndex);
				*/
				if(currentSongIndex < (songsList.size() -1 ))
				{
					playSong(currentSongIndex + 1);
					currentSongIndex = currentSongIndex + 1;
				}
				else{
					// play first song
					playSong(0);
					currentSongIndex = 0;
				}
				currentSongIndexAdv = 0;
				ListPlayAdv.clear();



				Timer timer = new Timer();

				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						running = true;
						//mUpdateAdsHandler.postDelayed(mAdvStartInTime, 1000);
					}
				}, 1 * 60 * 1000);
			}
		}
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		mp.release();
		ftp.createDummyFile(getString(R.string.stop_application), cntx);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		menu.add(getString(R.string.settings));
		menu.add(getString(R.string.play_list));
		menu.add(getString(R.string.ftp_conn));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {

		if(item.getTitle()==getString(R.string.settings))
		{
			Intent i = new Intent(getApplicationContext(), Setting.class);
			startActivityForResult(i, 100);
		}
		if(item.getTitle()==getString(R.string.play_list))
		{

			try {

				JSONParsingMusic.AndroidJSONPars();
				JSONParsingAdv.AndroidJSONPars();

				ftp.createDummyFile(getString(R.string.generation), cntx);
				newlist=true;
				List();
			}
			catch(Exception e)
			{
				e.getMessage();
			}


		}
		if(item.getTitle()==getString(R.string.ftp_conn))
		{
			try {
				ftp.DU_ftp(cntx);
				statusss.setText("Success all ");
			}

			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		return super.onOptionsItemSelected(item);
	}




	@Override
	public void onBackPressed()
	{


		new AlertDialog.Builder(this)
				.setTitle(R.string.exit)
				.setMessage(R.string.exit2)
				.setPositiveButton(R.string.yess, new OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1)
					{
						onDestroy();
						System.exit(0);

					}
				})
				.setNegativeButton(R.string.noo, null)
				.create().show();
	}


	public void getString(String time_start, String time_finish)
	{

		Start=new String(time_start);
		Stop=new String (time_finish);
		Log.i(TAG, "Player Config - Start: "+Start+" Stop: "+Stop);

	}



	public void Volume(int songIndex)
	{
		try
		{
			String valueString = songsList.get(songIndex).get("songVolume");

			if(valueString==null)
			{
				valueString="70";
			}
			maxMusicVolume = volume.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			procentMusic= maxMusicVolume/(double)100;
			indexVolumeMusic = Integer.parseInt(valueString);
			valueMusic =(int)(procentMusic*indexVolumeMusic);
			volume.setStreamVolume(AudioManager.STREAM_MUSIC, valueMusic, 0 );

		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}
	public void VolumeAdv(int songIndex)
	{
		try
		{
			String valueString = ListPlayAdv.get(songIndex).get("songVolume");

			if(valueString==null)
			{
				valueString="100";
			}
			maxMusicVolume = volume.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			procentMusic= maxMusicVolume/(double)100;
			indexVolumeMusic = Integer.parseInt(valueString);
			valueMusic =(int)(procentMusic*indexVolumeMusic);
			volume.setStreamVolume(AudioManager.STREAM_MUSIC, valueMusic, 0 );

		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}
	public void VolumeAutoDown()
	{
		try
		{
			maxMusicVolume = volume.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 15
			procentMusic = maxMusicVolume/(double)100;// 15/100 = 0.15 - 1%

			int set  = (int)((valueMusic*100/maxMusicVolume)*procentMusic);
			int find = set;

			for(int i=0;i<set;i++){
				find--;
				volume.setStreamVolume(AudioManager.STREAM_MUSIC, find, 0 );
				if(find<=0)
					break;
				Thread.sleep(40);
			}
		}
		catch(Exception e)
		{
			e.getMessage();
		}
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		Settings();
		ObjInfo.setText(Sname);


	}

	// Called implicitly when device is about to sleep or application is backgrounded
	@Override
	protected void onPause()
	{
		super.onPause();
		partialWakeLock.acquire();
		wifiLock.acquire();

	}
	void Settings()
	{
		/**��������� ������� */
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// ������ ������ ������ �� EditTextPreference
		//download_adv =  prefs.getBoolean("checkboxPref", false);
		download_music =  prefs.getBoolean("checkboxPref", false);
		Sftp = new String( prefs.getString(getString(R.string.pref_ftp), "musicmall.com.ua"));
		Sobject = new String(prefs.getString(getString(R.string.pref_object), "0"));
		Sname =  new String(prefs.getString(getString(R.string.pref_object_name), ""));
		Sid = new String( prefs.getString(getString(R.string.pref_id), "android_test"));
		Spassword = new String(prefs.getString(getString(R.string.pref_password), "android_test"));
		Srefresh = new String( prefs.getString(getString(R.string.pref_refresh), "15"));
		Sdir = new String( prefs.getString(getString(R.string.pref_way), "/mnt/sdcard/"));

		stop_settings=prefs.getBoolean("stop_play", true);
		start_settings=prefs.getBoolean("start_play", true);
		first_start = prefs.getBoolean("first_start", true);

		ftp = new MainActivity(cntx, Sftp,Sobject,Sid,Spassword);
		JSONParsingAdv = new JSONParsingAdv(cntx, Sftp, Sobject, Sid, Spassword);
		JSONParsingMusic = new JSONParsingMusic(cntx, Sftp, Sobject, Sid, Spassword, download_music);
		IMGdownload=new IMGdownload(cntx,Sftp, Sobject, Sid, Spassword);
		try{
			der_created.Created_directory(Sdir);
		}
		catch(Exception created)
		{
			created.getMessage();
		}
		try{
			IMGdownload.main();
			//Toast("�������� img ");
		}
		catch(Exception img)
		{
			img.getLocalizedMessage();
		}
	}
	// Called implicitly when device is about to wake up or foregrounded
	@Override
	protected void onResume()
	{
		super.onResume();

		wakeDevice();

		if(wifiLock.isHeld())
			wifiLock.release();

		if(fullWakeLock.isHeld()){
			fullWakeLock.release();
		}
		if(partialWakeLock.isHeld()){
			partialWakeLock.release();
		}
	}

	// Called whenever we need to wake up the device

	public void wakeDevice() {
		fullWakeLock.acquire();

		KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
		keyguardLock.disableKeyguard();
	}

	public void Toast(String text){
		Toast.makeText(AndroidBuildingMusicPlayerActivity.this, text,Toast.LENGTH_LONG).show();
	}

}