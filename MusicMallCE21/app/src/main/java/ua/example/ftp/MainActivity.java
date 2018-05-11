package ua.example.ftp;


import java.io.File;
import java.io.OutputStreamWriter;




import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.commons.io.FileUtils;


import it.sauronsoftware.ftp4j.FTPClient;
import ua.example.player.Constants;
import ua.player.musicmallce.R;



public class MainActivity {

	private static final String TAG = "MainActivity";
	MyFTPClient ftpclient = null;

	private int mYear;
	private int mMonth;
	private int mDay;
	//int mDay2;
	private int mHour;
	private int mMinute;
	private int mSecond;
	private  String file_content;
	private String mDataSend,mDataName;
	//String mDataName2;
	boolean statuss;
	private String Sftp,Sobject,Sid,Spassword;
	private   String way =Constants.WAY_MUSIC();
	
	public MainActivity( Context cntx,String sftp, String sobject, String sid, String spassword)
	{
        ftpclient = new MyFTPClient();
         Sftp=sftp;
         Sobject=sobject;
         Sid=sid;
         Spassword=spassword;
 
		
	}



	public void createDummyFile(String songTitle,Context cntx)  {
		//FileOutputStream fos=null;
		OutputStreamWriter osw = null ;
		/* Charset.forName("UTF-8").encode(songTitle);
		 Log.e(TAG, songTitle);*/


		try {

			  Calendar c = Calendar.getInstance();
		        mYear = c.get(Calendar.YEAR);
		        mMonth = c.get(Calendar.MONTH)+1; //+1
		        mDay = c.get(Calendar.DAY_OF_MONTH);
		        //mDay2 = c.get(Calendar.DAY_OF_MONTH)-1;
		        mHour = c.get(Calendar.HOUR_OF_DAY);
		        mMinute = c.get(Calendar.MINUTE);
		        mSecond= c.get(Calendar.SECOND);

		        
		        mDataName=((pad(mDay))+("-")+(pad(mMonth))+("-")+(pad(mYear))+(" "));
		        //mDataName2=((pad(mDay2))+("-")+(pad(mMonth))+("-")+(pad(mYear))+(" "));
		    	mDataSend=(mDataName+((pad(mHour))+(":")+(pad(mMinute))+(":")+(pad(mSecond))));
		    	    
		    	file_content = "\n"+songTitle+" "+mDataSend;
		    	
		   // fos = cntx.openFileOutput("Log_"+mDataName+".txt", Context.MODE_APPEND);
		    
		    
		//	fos.write(file_content.getBytes()); 
			
			String str = new String(file_content.getBytes(),"UTF-8");
			 Log.i(TAG, str);
			 osw = new OutputStreamWriter(
					cntx.openFileOutput("Log_"+mDataName+".txt", Context.MODE_APPEND),
			    Charset.forName("windows-1251").newEncoder()
			);
			//Charset.forName("UTF-8").encode(file_content);
			osw.write(str);
			
		}
		catch(Exception e)
		{
			e.getMessage();
		}
		finally {
             try {
            	// fos.close();
            		 osw.close();
             } catch (IOException ex) {
                 ex.printStackTrace();
             }
         }
		try
		{
			File dir_check= new File(way+"log/");
			dir_check.mkdir();
			FileUtils.writeStringToFile(new File(way+"log/"+"Log_"+mDataName+".txt"), file_content, true);
		}
		catch(Exception e)
		{
			e.getMessage();
		}

	}

public void enter_ftp(final Context cntx)
	{
	
			
			onCheck(cntx);
			
			
			int count=0,retry=5;
			// Replace your UID & PW here
			while(count<retry)
			{
				count+=1;
					statuss = ftpclient.ftpConnect(Sftp, Sid, Spassword, 21);
					
					if(statuss==true)
					{
					Log.d(TAG, "Connection Success");
					break;
					}
					else
						if(statuss==false)
						{
						Log.d(TAG,"Connection failed - ждем 1 сек");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							
							e.printStackTrace();
						}
						}	
			}
			createDummyFile(cntx.getString(R.string.connection)+" "+statuss,cntx);
		
	}

/* public void send_ftp( final Context cntx)
	{

		new Thread(new Runnable()


	 {

		public void run()
		{

			int count=0,retry=5;
			boolean status = false;
			new_ftp();
			enter_ftp(cntx);
			count=0;retry=10;
			while (count<retry)
			{
			count+=1;
			status = ftpclient.ftpUpload("Log_"+mDataName+".txt","Log_"+mDataName+".txt", "/log/"+Sobject+"/", cntx);
			if (status == true) 
			{
				Log.d(TAG, "Upload success");
				//break;
			} 
			else {
				Log.d(TAG, "Upload failed ");
				try {

					enter_ftp(cntx);

					Thread.sleep(300000L);
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			}

			createDummyFile("Upload "+status,cntx);

		}

	}).start();


	}
/*
 public void exit_ftp( final Context cntx)
{
	//new Thread(new Runnable() 
	//{
	//	public void run()
	//	{
			boolean status=false;
			status=ftpclient.ftpDisconnect();
			if (status == true) 
			{
				Log.d(TAG, "Disconnect success");
				createDummyFile("Disconnect "+status,cntx);
			
			} 
			else {
				Log.d(TAG, "Disconnect failed");
				createDummyFile("Disconnect "+status,cntx);
			}
		}
//	}).start();
}
public void download_ftp(final Context cntx)
{
	new Thread(new Runnable() 
	{
		public void run()
		{
			String adv = null;
			String music = null;
			boolean status2=false,status3 = false;
			new_ftp();
			enter_ftp(cntx);
			int count=0,retry=10;
			
			try {
				adv = FileUtils.readFileToString(new File(way+"Advertisement.json"), "UTF-8");
				music = FileUtils.readFileToString(new File(way+"Music.json"), "UTF-8");
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			while(count<retry)
			{
				count+=1;
				Log.d(TAG, "Count :"+count);
				
				//if(statuss)
				{
					Log.d(TAG, "Start download");
			status3 =ftpclient.ftpDownload("/set/"+Sobject+"/Advertisement.json", way+"Advertisement.json");
			status2 =ftpclient.ftpDownload("/set/"+Sobject+"/Music.json", way+"Music.json");
			Log.d("MainActivity", "After ftpDownload \n " +status2 +"   "+status3+"   ");
				}
			//	else
				{
					Log.d(TAG, "statuss false!!!");
					try {
						//exit_ftp(cntx);
						//Thread.sleep(1000);
						//enter_ftp(cntx);
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
		
			if((status2&&status3)==true)
			{
			Log.d(TAG, "download all success");
			adv = null;
			music = null;
			break;
			}
			 else
				{
				Log.d(TAG,"download all failed");
				}
			}//end while
			createDummyFile("Download config adv.json: "+status3+"; Download config music.json: "+status2,cntx);
			
			if(!(status2&&status3))
			{
				try {
					FileUtils.writeStringToFile(new File(way+"Advertisement.json"), adv, false);
					FileUtils.writeStringToFile(new File(way+"Music.json"), music, false);
					//Log.d(TAG,"������������ ������ ����!!! ");
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			
		}
	}).start();

}*/


public void DU_ftp( final Context cntx)
{

	 new Thread(new Runnable()
	 {

////////////////upload/////////
        @Override
		public void run()
		{
			int count=0;
			boolean status = false;
			enter_ftp(cntx);
			new_ftp();	
////////////*Upload

			while(count<10)
			{
			count+=1;
			status = ftpclient.ftpUpload("Log_"+mDataName+".txt","Log_"+mDataName+".txt", "/log/"+Sobject+"/", cntx);
			if (status == true) 
			{
				Log.d(TAG, "Try №"+count+" Upload success");
				break;
			} 
			else {
				Log.d(TAG, "Try №"+count+" Upload failed - Ждем 2  сек");
				try {

					enter_ftp(cntx);

					Thread.sleep(1000);
					
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
			}
			createDummyFile(cntx.getString(R.string.upload)+" "+status,cntx);

///////////////Download
			String adv = null;
			String music = null;
			boolean status2=false,status3 = false;


			try {
				adv = FileUtils.readFileToString(new File(way+".Advertisement"), "UTF-8");
				music = FileUtils.readFileToString(new File(way+".Music"), "UTF-8");
			} catch (IOException e) 
			{
				e.printStackTrace();
			}


			count=0;
			while(count<10&&status)
			{
				count+=1;
			Log.d(TAG, "Попытка(Download) №"+count);
			status3 =ftpclient.ftpDownload("/set/"+Sobject+"/Advertisement.json", way+".Advertisement"); //Advertisement
			status2 =ftpclient.ftpDownload("/set/"+Sobject+"/Music.json", way+".Music");
			Log.d("MainActivity", "After ftpDownload \n " +status2 +"   "+status3+"   ");

			if((status2&&status3)==true)
			{
			Log.d(TAG, "download all success");
			adv = null;
			music = null;
			break;
			}
			 else
				{
				Log.d(TAG,"download all failed,wait 5 sec!");
				try {

					//sync.countDown();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				}


			}//end while

			createDummyFile(cntx.getString( R.string.download)+" adv.json: "+status3+"; "+ cntx.getString(R.string.download)+" music.json: "+status2,cntx);
			
			if(!(status2&&status3))
			{
				try {
					FileUtils.writeStringToFile(new File(way+".Advertisement"), adv, false);
					FileUtils.writeStringToFile(new File(way+".Music"), music, false);
					Log.d(TAG,"Перезаписано старый файл!!! ");
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}/////////////////////




		}
	}).start();



}

private static String pad(int c) 
{
if (c >= 10)
    return String.valueOf(c);
else
    return "0" + String.valueOf(c);
 }

/*
public void connect_ftp( final Context cntx)
{
	new Thread(new Runnable() 
	{
		public void run()
		{
			Log.d(TAG,"Settings"+Sftp+Sobject+Sid+Spassword);
			boolean status = false;
			// Replace your UID & PW here
			int count=0,retry=5;
			while(count<retry)
			{
					status = ftpclient.ftpConnect(Sftp, Sid, Spassword, 21);
					
					if(status==true)
					{
					Log.d(TAG, "Connection Success");
					break;
					}
					else
						{
						Log.d(TAG,"Connection failed");
						}	
	
			}
			createDummyFile("Connection "+status,cntx);
		}
}).start();
}
*/
public void new_ftp()
{	
	ftpclient.ftpMakeDirectory("/log/"+Sobject+"/");

}

public void onCheck(Context context) {
    Log.d(TAG, "Интернет соединение (RECEIVED) getMobileDataEnabled: " + getMobileDataEnabled(context));
    createDummyFile("getMobileDataEnabled: " + getMobileDataEnabled(context),context);
    if (!isOnline(context)) {
        Log.d(TAG, "no mobile internet");
        createDummyFile("no mobile internet",context);
        if (turnOnInet(context)) {
            Log.d(TAG, "mobile internet is on");
            createDummyFile("mobile internet is on",context);
        }
    }

            Log.d(TAG, "Проверка (RECEIVED) FINISHED");

}

public boolean isOnline(Context context) {
    ConnectivityManager cm = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null){
        Log.d(TAG, "isAvailable: "+netInfo.isAvailable());
    }
    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
        return true;
    }
    return false;
}

public boolean turnOnInet(Context context) {
    ConnectivityManager mgr = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    if (mgr == null) {
        Log.d(TAG, "ConnectivityManager == NULL");
        return false;
    }
    try {
        Method setMobileDataEnabledMethod = mgr.getClass().getDeclaredMethod("setMobileDataEnabled", boolean.class);
        if (null == setMobileDataEnabledMethod) {
            Log.d(TAG, "setMobileDataEnabledMethod == null");
            return false;
        }    
        setMobileDataEnabledMethod.invoke(mgr, true);
    }
    catch(Exception e) {
        Log.e(TAG, TAG, e);
        return false;
    }
    return true;
}   


private boolean getMobileDataEnabled(Context context) {
    ConnectivityManager mgr = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    if (mgr == null) {
        Log.d(TAG, "getMobileDataEnabled ConnectivityManager == null");
        return false;
    }
    try {
        Method method = mgr.getClass().getMethod("getMobileDataEnabled");
        return (Boolean) method.invoke(mgr);
    } catch (Exception e) {
        Log.e(TAG, TAG, e);
        return false;
    }
}


}



