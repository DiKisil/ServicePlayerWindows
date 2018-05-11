package ua.example.download;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;

import android.content.Context;

import android.util.Log;

import ua.example.player.Constants;

import ua.example.ftp.MainActivity;
import ua.example.json.JSONParsingAdv;
import ua.player.musicmallce.R;

public class ADVdownload
{
	private static final String TAG = "Закачка";
	public static MainActivity MainActivity;
	private static AffableThread mSecondThread;
	public static String ftp;
	public static String object;
	public static String id;
	public static String password;
	public static ArrayList<HashMap<String, String>> ADVList;
	public FTPClient mFTPClient = null;
	public static Context cntxx;

	public ADVdownload(Context cntx, String sftp, String sobject, String sid, String spassword)
	{
		cntxx = cntx;
		ftp = new String(sftp);
		object = new String(sobject);
		id = new String(sid);
		password = new String(spassword);
		MainActivity = new MainActivity(cntx, sftp, sobject, sid, spassword);
	}

	public  void main(ArrayList<HashMap<String, String>> ADV)
	{
		mSecondThread = new AffableThread();
		ADVList= ADV;
		Log.i("MainActivity","Поток. Status: : "+AffableThread.download_start);
		if(!AffableThread.download_start)
		{
			Log.i("MainActivity","Поток. Start");

			mSecondThread.start();

		}
		else
			Log.i("MainActivity","Поток что уже работает. Status:"+AffableThread.download_start);
		Log.d(TAG,"Главный поток рекламы завершен...");


	}
}



class AffableThread extends Thread
{

	static boolean download_start= false;
	private static final String TAG = "Закачка";
	public FTPClient mFTPClient = null;
	public JSONParsingAdv JSONParsingAdv;
	private String adv_folder = Constants.WAY_MUSIC()+"adv"+File.separator;
	private String adv_name;
	private ArrayList<HashMap<String, String>> adv = new ArrayList();
	//private  ArrayList<HashMap<String, String>> adv = new ArrayList<HashMap<String, String>>();
	long size_ftp;

	@Override
	public void run()	//���� ����� ����� �������� � �������� ������
	{
		download_start= true;
		File check=new File(adv_folder);
		check.mkdirs();

		adv=ADVdownload.ADVList;
		//Log.d(TAG,"Adv � ������\n"+adv);
		Log.d(TAG,"Привет из побочного потока рекламы!");

		for(int i=0;i<adv.size();i++ )
		{
			mFTPClient= new FTPClient();

			adv_name=adv.get(i).get("adv_name");
			File adv_path = new File(adv_folder+adv_name);

			//////////////////////////////////////////////////
			//long size_ftp = 0;
			String name;
			boolean result = false;
			try
			{
				boolean qwertyu = false;
				int popitka=1;
				while(!qwertyu&&popitka<30)
				{
					try
					{
						mFTPClient.connect(ADVdownload.ftp, 21);
					}
					catch(Exception rr)
					{
						rr.getMessage();
					}
					qwertyu=mFTPClient.isConnected();
					Log.d(TAG, "Connect Popitka #" +popitka+" Status:"+qwertyu);
					if(!qwertyu)
					{
						Log.d(TAG, "����� 1.5 �������");
						//Thread.sleep(1500);
					}
					popitka++;
				}

				popitka=1;
				while(!result&&popitka<10)
				{
					result = mFTPClient.login(ADVdownload.id, ADVdownload.password);
					Log.d(TAG, "Join Popitka #" +popitka+" Status:"+result);
					if(!result)
					{
						Log.d(TAG, "����� 1.5 �������");
						//Thread.sleep(1500);
					}

					popitka++;
				}
				mFTPClient.enterLocalPassiveMode();
			}
			catch(Exception r)
			{
				r.getStackTrace();
				Log.e(TAG, "download error"+r);

			}

			if (result) {
				System.out.println("User successfully logged in.");
			} else {
				System.out.println("Login failed!");
				// return;
			}
			// Changes working directory
			try
			{
				boolean success = mFTPClient.changeWorkingDirectory("/adv/");


				if (success) {
					System.out.println("Successfully changed working directory.");
				} else {
					System.out.println("Failed to change working directory. See server's reply.");
				}
			}
			catch(Exception er)
			{
				Log.e(TAG, "Change erorr: "+er);
			}

			try
			{
				FTPFile[] files =  mFTPClient.listFiles();


				System.out.println("Files on Ftp Server : ");
				for (FTPFile file : files)
				{
					if (file.isFile())
					{
						name=file.getName().toString();

						if(adv_name.equals(name))
						{
							size_ftp=file.getSize();
							Log.i(TAG, "���: "+adv_name+" ������: "+humanReadableByteCount(size_ftp, false));
							break;
						}

					}
				}
			}
			catch(Exception er)
			{
				Log.e(TAG, "Change erorr: "+er);
			}
			finally {
				try {

					if (mFTPClient.isConnected())
					{
						mFTPClient.logout();
						mFTPClient.disconnect();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			/////////////////////////////////////////////////

			if((!(adv_path.exists())||adv_path.length()!=size_ftp)&&result)
			{
				Log.d(TAG, "������ ���� "+adv_name);
				ADVdownload.MainActivity.createDummyFile(ADVdownload.cntxx.getString(R.string.download_adv)+" "+adv_name+ADVdownload.cntxx.getString(R.string.size_music)+humanReadableByteCount(size_ftp, false), ADVdownload.cntxx);

				String savePath = Constants.WAY_MUSIC()+"adv/"+adv_name;
				try
				{
					boolean qwertyu = false;
					boolean resultt = false;
					int popitka=1;
					while(!qwertyu&&popitka<30)
					{
						try
						{
							mFTPClient.connect(ADVdownload.ftp, 21);
						}
						catch(Exception rr)
						{
							rr.getMessage();
						}
						qwertyu=mFTPClient.isConnected();
						Log.d(TAG, "Connect Popitka #" +popitka+" Status:"+qwertyu);
						if(!qwertyu)
						{
							//Log.d(TAG, "����� 1.5 �������");
							//Thread.sleep(1500);
						}
						popitka++;
					}
					///////////////////////////////////
					popitka=1;
					while(!resultt&&popitka<10)
					{
						resultt = mFTPClient.login(ADVdownload.id, ADVdownload.password);
						Log.d(TAG, "Join Popitka #" +popitka+" Status:"+resultt);
						if(!resultt)
						{
							Log.d(TAG, "����� 1.5 �������");
							//Thread.sleep(1500);
						}

						popitka++;
					}
					mFTPClient.enterLocalPassiveMode();
					mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
					mFTPClient.setBufferSize(1024*512);
					String remoteFile = "/adv/"+adv_name;
					File downloadFile = new File(savePath);
					OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));

					InputStream inputStream = mFTPClient.retrieveFileStream(remoteFile);
					byte[] bytesArray = new byte[1024];
					int bytesRead = -1;
					while ((bytesRead = inputStream.read(bytesArray)) != -1)
					{
						outputStream.write(bytesArray, 0, bytesRead);
					}

					/*boolean success = mFTPClient.completePendingCommand();
					if (success)
					{
						Log.d(TAG,"Download success");
					}*/
					outputStream.close();
					inputStream.close();

				}
				catch (Exception x) {
					System.out.println("Error: " + x.getMessage());
					x.printStackTrace();
				}
				finally {
					try {
						if (mFTPClient.isConnected()) {

							mFTPClient.logout();
							mFTPClient.disconnect();
						}
					} catch (IOException ex) {ex.printStackTrace();}
				}
			}///������ ��
			else
			{
				Log.d(TAG, "���� ������� "+adv_name+"  ���� � �� ����");
			}
		}///������ ����

		/**�������� �������� �������*/
		File folder = new File(adv_folder);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			boolean flag=false;
			for(int i=0;i<adv.size();i++)
			{
				if(adv.get(i).get("adv_name").equals(file.getName()))
				{
					flag=true;
				}
			}
			if(!flag)
			{
				file.delete();
				Log.d(TAG, "������� ���� ������� "+adv_name);
				ADVdownload.MainActivity.createDummyFile(ADVdownload.cntxx.getString(R.string.delete_adv)+" "+adv_name, ADVdownload.cntxx);
			}
		}
		Log.d(TAG,"�������� ����� ������� �������� ...");
	}
	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}


}

