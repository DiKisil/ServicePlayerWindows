package ua.example.download;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import org.apache.commons.net.ftp.FTPFile;

import android.content.Context;

import android.util.Log;

import ua.example.ftp.MainActivity;
import ua.example.ftp.MyFTPClient;
import ua.example.player.Constants;



public class LOGupload
{
	private static final String TAG = "Закачка_log";
    private static AffableThread4 mSecondThread;
	public static String ftp,object, id,password;
    public FTPClient mFTPClient ; 
    public static MainActivity MainActivity;
   
    public static  Context cntxx;
	public LOGupload(Context cntx, String sftp, String sobject, String sid,String spassword)
	{
		cntxx=cntx;
		ftp=new String(sftp);
		object=new String(sobject);
		id=new String(sid);
		password=new String(spassword);
		MainActivity= new MainActivity(cntx, sftp, sobject, sid, spassword); 
	}
    public  void main()
    {
    	
        mSecondThread = new AffableThread4();	//�������� ������
        mSecondThread.start();					//������ ������
        Log.d(TAG,"Главный поток log завершён...");
  
    }
}


/////////////////////////////////////////////////////////////////////////////////


class AffableThread4 extends Thread
{
	
	private static final String TAG = "Закачка_log";
	private String way=Constants.WAY_MUSIC();
	FTPClient client = new FTPClient( );
	MyFTPClient ftpclient = new MyFTPClient(); 
    @Override
    public void run()	//���� ����� ����� �������� � �������� ������
    {	
    	 Log.d(TAG,"Побочный поток log ");

        try {
        	 boolean qwertyu = false;
    		 boolean resultt = false;
    		 int popitka=1;
    		 /*Connect*/
     		while(!qwertyu&&popitka<10)
     		{		
     		try
     		{
     			client.connect(LOGupload.ftp, 21);
     		}
     		catch(Exception rr)
     		{
     			rr.getMessage();
     		}
            	qwertyu=client.isConnected();
            	Log.d(TAG, "Connect Popitka #" +popitka+" Status:"+qwertyu);
            	if(!qwertyu)
            	{
            		Log.d(TAG, "Пауза 1.5 секунды");
            		Thread.sleep(1500);
            	}
            	popitka++;
     		}
     		 popitka=1;
     		 /*Authorization*/
     		while(!resultt&&popitka<10)
     		{
            	resultt = client.login(LOGupload.id, LOGupload.password);
            	Log.d(TAG, "Join Popitka #" +popitka+" Status:"+resultt);
            	if(!resultt)
            	{
            		Log.d(TAG, "Пауза 1.5 секунды");
            		Thread.sleep(1500);
            	}
            	
            	popitka++;
     		}
          client.enterLocalPassiveMode();
          client.setFileType(FTP.BINARY_FILE_TYPE);
          client.setBufferSize(1024);
          client.changeWorkingDirectory("/log/"+LOGupload.object);
          
          FTPFile[] files =  client.listFiles(); 
          
          HashMap<String, String> LOGinFTP; 
          ArrayList<HashMap<String, String>> LogInFTP = new ArrayList<HashMap<String, String>>();
          
          System.out.println("Files on Ftp Server : ");
          for (FTPFile file : files) 
          {
                  if (file.isFile())
                  {
                	  LOGinFTP = new HashMap<String, String>();

                  	LOGinFTP.put("NameFile", file.getName().toString());
                  	LOGinFTP.put("SizeFile", String.valueOf(file.getSize()));
                  	
                  	LogInFTP.add(LOGinFTP);               
                  }
                  
          }
          
         
          File file[] = new File(way+"/log/").listFiles();
          //File filff = LOGupload.cntxx.getFilesDir();
        //  File file[] = filff.listFiles();
          boolean uploadLogOnFtp = false ;
          
          for (int i=0; i < file.length; i++)
          {
              Log.d(TAG, "В проверке FileName:" + file[i].getName()+" | "+file[i].length());
              
	              for (int j=0; j < LogInFTP.size(); j++){
	            	  
	            	  uploadLogOnFtp = false;
	            	  
			              if(file[i].getName().equals(LogInFTP.get(j).get("NameFile"))){
			            	  			            	  
			            	  if(file[i].length() - Long.valueOf(LogInFTP.get(j).get("SizeFile")) >= 250){
			            		  Log.i(TAG, "FileName SD: " + file[i].getName()
			            				  +" | lench: "+file[i].length()
			            				  +"\n"
			            				  +"FileName FTP: "
			            				  +LogInFTP.get(j).get("NameFile")
			            				  +" | lench: "
			            				  +LogInFTP.get(j).get("SizeFile")
			            				   +"\n"
			            				  +"Difference: "
			            				  +Long.valueOf(file[i].length() - Long.valueOf(LogInFTP.get(j).get("SizeFile")))
			            				  );
			            		  
			            		  uploadLogOnFtp = true;
			            		  break;
			            	  }
			            	  uploadLogOnFtp = false;
			            	  break;
			            	  
			              }else{
			            	 
			            	  uploadLogOnFtp = true;
			              }
	              }
	              if(uploadLogOnFtp)
	            	  upload(file[i].getName());
          }

          
        } catch(IOException ioe) {
          ioe.printStackTrace();
          System.out.println( "Error communicating with FTP server." );
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
          try {
            client.disconnect();
          } catch (IOException e) {
            System.out.println( "Problem disconnecting from FTP server" );
          }
        }
       Log.d(TAG,"Побочный поток img завершён ...");
       
} 
    void upload(String name){
    	
    	Log.i(TAG, "Загрузка на сервер файла "+ name);
    	
    	
    	enter_ftp();
    	int count=0;
		boolean status = false;
    	/**Upload*/
		while(count<5)
		{
		count+=1;

		status = ftpclient.ftpUpload(name, name, "/log/"+LOGupload.object+"/", LOGupload.cntxx);
		
		if (status == true) 
		{
			Log.d(TAG, "Try №"+count+" Upload success");
			break;
		} 
		else {
			Log.d(TAG, "Try №"+count+" Upload failed - Ждем 4 сек");
			try {
				Thread.sleep(4000);	
				enter_ftp();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		}
		
    }
    public void enter_ftp()
	{
    	boolean statuss;
			int count=0,retry=5;
			// Replace your UID & PW here
			while(count<retry)
			{
				count+=1;
					statuss = ftpclient.ftpConnect(LOGupload.ftp, LOGupload.id, LOGupload.password, 21);
					
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
			
		
	} 
   
}
