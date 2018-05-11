package ua.example.download;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;

import android.content.Context;

import android.util.Log;

import ua.example.ftp.MainActivity;
import ua.example.player.Constants;
import ua.player.musicmallce.R;


public class IMGdownload
{
	private static final String TAG = "Закачка";
    private static AffableThread3 mSecondThread;
	public static String ftp,object, id,password;
    public FTPClient mFTPClient ; 
    public static MainActivity MainActivity;
   
    public static  Context cntxx;
	public IMGdownload(Context cntx, String sftp, String sobject, String sid,String spassword)
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
    	
        mSecondThread = new AffableThread3();	//�������� ������
        mSecondThread.start();					//������ ������
        Log.d(TAG,"Главный поток рекламы завершён...");
  
    }
}


/////////////////////////////////////////////////////////////////////////////////


class AffableThread3 extends Thread
{
	
	private static final String TAG = "Закачка";
	long size_ftp;
    String name;

    @Override
    public void run()	//���� ����� ����� �������� � �������� ������
    {	
    	 Log.d(TAG,"Побочный поток img ");
    	
    	FTPClient client = new FTPClient( );
        OutputStream outStream = null;
        
        
        try {
        	 boolean qwertyu = false;
    		 boolean resultt = false;
    		 int popitka=1;
    		 /*Connect*/
     		while(!qwertyu&&popitka<10)
     		{		
     		try
     		{
     			client.connect(IMGdownload.ftp, 21);
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
            	resultt = client.login(IMGdownload.id, IMGdownload.password);
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
          client.changeWorkingDirectory("/set/img/");
          
          FTPFile[] files =  client.listFiles(); 
          
          System.out.println("Files on Ftp Server : ");
          for (FTPFile file : files) 
          {
                  if (file.isFile())
                  {
                  	name=file.getName().toString(); 
                  	size_ftp = file.getSize();
                  	Log.i(TAG, "IMG "+name+"|"+size_ftp);
                  	File img_sd = null;
                  	try{
                  		 img_sd=new File(Constants.WAY_MUSIC()+"/img/"+name);
                  	}catch(Exception img_file){img_file.getMessage();}
                  	Log.i(TAG, "IMG "+name+"|"+size_ftp+"=="+img_sd.length());
                  	if(!img_sd.exists()||img_sd.length()!=size_ftp)
                  	{
                  		Log.i(TAG, " качаем IMG "+name+"|"+size_ftp);
                  		IMGdownload.MainActivity.createDummyFile(IMGdownload.cntxx.getString(R.string.download_img)+" "+"img"+File.separator+name+IMGdownload.cntxx.getString(R.string.size_music)+humanReadableByteCount(size_ftp, false), IMGdownload.cntxx);
                  		String remoteFile = "/set/img/"+name;
                        outStream = new FileOutputStream(Constants.WAY_MUSIC()+"/img/"+name );

                        client.retrieveFile(remoteFile, outStream);
                        
                  	}
                  }
                  
          }
          

        } catch(IOException ioe) {
          ioe.printStackTrace();
          System.out.println( "Error communicating with FTP server." );
        } catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
          try {
            client.disconnect( );
           // outStream.close();
          } catch (IOException e) {
            System.out.println( "Problem disconnecting from FTP server" );
          }
        }
       Log.d(TAG,"Побочный поток img завершён ...");
       
       /****************************************/
      /* String folder="start";
       name="";
       Log.d(TAG,"Побочный поток start ");
///////////////////�������//////////////////////////   
	      		 //if(mHour!=0)
	      		  {
	      		 Log.d(TAG, "//Загрузка всех папок //");
	      		 {
	      		 Log.d(TAG," folder download:"+folder);
	      		
	            try
	            {
	            	 boolean result = false;
	            	try
		            {
	            		boolean qwertyu = false;
	            		int popitka=1;
	            		while(!qwertyu&&popitka<10)
	            		{		
	            		try
	            		{
	            			client.connect(IMGdownload.ftp, 21);
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
	            		///////////////////////////////////
	            		 popitka=1;
	            		while(!result&&popitka<10)
	            		{
		            	result = client.login(IMGdownload.id, IMGdownload.password);
		            	Log.d(TAG, "Join Popitka #" +popitka+" Status:"+result);
		            	if(!result)
		            	{
		            		Log.d(TAG, "Пауза 1.5 секунды");
		            		Thread.sleep(1500);
		            	}
		            	
		            	popitka++;
	            		}
	            		client.enterLocalPassiveMode();
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
                ///////////////////////////////////////////////////////////// ************************************
                 try
                 {
                 boolean success = client.changeWorkingDirectory("/set/"+folder);
                 
                 
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
                 FTPFile[] files =  client.listFiles();
                 
                 
                 System.out.println("Files on Ftp Server : ");
                 for (FTPFile file : files) 
                 {
                         if (file.isFile())
                         {
                         	size_ftp=file.getSize();
                               //  System.out.println("File name : " + file.getName()+" "+file.getSize());
                              //   Log.d(TAG,"File name : " + file.getName());
                                 name=file.getName().toString();
                        
         
	      
	            String remoteFile = "/set/"+folder+File.separator+name;
	            String Path = Constants.WAY_MUSIC()+folder+File.separator;
	            String savePath = Path+name;
	            File music_path = new File(Constants.WAY_MUSIC()+folder+File.separator+name);
	            
	            if(music_path.length()!=size_ftp)
	            	 System.out.println("РАЗМЕР НЕ РАВЕН");

	            else
	            	 System.out.println("РАЗМЕР  РАВЕН!!!!");
	            
	      if((!(music_path.exists())||music_path.length()<=0||music_path.length()!=size_ftp)&&result)
	          { 
	    	  Log.d(TAG,"Загрузка "+folder+File.separator+name+"\n"+" size: "+humanReadableByteCount(size_ftp, false));
	    	  
	    	  IMGdownload.MainActivity.createDummyFile(IMGdownload.cntxx.getString(R.string.download_start)+" "+folder+File.separator+name+IMGdownload.cntxx.getString(R.string.size_music)+humanReadableByteCount(size_ftp, false), IMGdownload.cntxx);
	            File File = new File(Path);
	            File.mkdirs();
	            
	            File downloadFile = new File(savePath);
	            
	            	  try
	             {
	            		   boolean qwertyu = false;
		            		int popitka=1;
		            		while(!qwertyu&&popitka<10)
		            		{		
		            		try
		            		{
		            			client.connect(IMGdownload.ftp, 21);
		            		}
		            		catch(Exception rr)
		            		{
		            			rr.getMessage();
		            		}
			            	qwertyu=client.isConnected();
			            	Log.d(TAG, "Connect download Popitka #" +popitka+" Status:"+qwertyu);
			            	if(!qwertyu)
			            	{
			            		Log.d(TAG, "Пауза 1.5 секунды");
			            		Thread.sleep(1500);
			            	}
			            	popitka++;
		            		}
		            		///////////////////////////////////
		            		 popitka=1;
		            		 qwertyu=false;
		            		while(!qwertyu&&popitka<10)
		            		{
		            			try
		            			{
		            				qwertyu = client.login(IMGdownload.id, IMGdownload.password);
		            			}catch(Exception rr)
		            			{
		            				Log.d(TAG, "Error login download: "+rr);
		            				rr.getMessage();
		            			}
			            	
			            	Log.d(TAG, "Join download Popitka #" +popitka+" Status:"+qwertyu);
			            	if(!qwertyu)
			            	{
			            		Log.d(TAG, "Пауза 1.5 секунды");
			            		Thread.sleep(1500);
			            	}
			            	
			            	
			            	popitka++;
		            		}
	               	
	               	//////////////////////////////////////////////////////////////
		            		client.enterLocalPassiveMode();
		            		client.setFileType(FTP.BINARY_FILE_TYPE);
		            		client.setBufferSize(1024*512);
	         	   
	         	 
	            

	            
	            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
	        
	            InputStream inputStream = client.retrieveFileStream(remoteFile);
	            byte[] bytesArray = new byte[4096];
	            int bytesRead = -1;
	            while ((bytesRead = inputStream.read(bytesArray)) != -1)
	            {
	                outputStream.write(bytesArray, 0, bytesRead);
	            }
	            
	            //�������� �� 5-15 ��� 
	           boolean successs = mFTPClient.completePendingCommand();
	            if (successs)
	            {
	         	   Log.d(TAG,"Download success");
	            }
	            else
	            	Log.d(TAG,"Download failed");
	            outputStream.close();
	            inputStream.close();
	            
	         }
	             catch (IOException ex) {
	                 System.out.println("Error: " + ex.getMessage());
	                 ex.printStackTrace();
	             } finally {
	                 try {
	                	 
	                     if (client.isConnected()) 
	                     {
	                    	 client.logout();
	                    	 client.disconnect();
	                     }
	                 } catch (IOException ex) {
	                     ex.printStackTrace();
	                 }
	             }
	            }// check download   
	            else
	            {
	            	Log.i(TAG, "файл музыки есть и не ноль");
	            }
	      
                 }
                 }
	            }
             catch(Exception qqq)
             {
             	qqq.getMessage();
             }

             } 
	            //catch (FTPConnectionClosedException e) {
            //         System.out.println(e);
             //} 
	      		 finally {
                     try {
                    	 client.disconnect();
                     } catch (FTPConnectionClosedException e) {
                             System.out.println(e);
                     } catch (IOException e) {
						e.printStackTrace();
					}
             }
	      		 }
	      		 Log.d(TAG, "//Загрузка всех папок завершенно....//");
	      		 }
	      		 ///////////////////////////////��������////////////////////
	      		// if(mHour==0||mHour==1)
	      		 {
	      			// Log.d(TAG, "/*Удаление  папки ");
	      		 Log.d("Проверка", "В folder:"+folder);
	      		   

		      		 // if(retry_folder)
		      		  {
		      			
	      		   
	      		   File fold=new File(Constants.WAY_MUSIC()+folder+File.separator);
	      		   ///////////////////////////////////////////////////
	      		   try
	      		   {
	      			 boolean qwertyu = false;
	            		int popitka=1;
	            		while(!qwertyu&&popitka<10)
	            		{		
	            		try
	            		{
	            			client.connect(IMGdownload.ftp, 21);
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
	            		/////////////
                 boolean result = false ;
                 popitka=1;
         		while(!result&&popitka<10)
         		{
	            	result = client.login(IMGdownload.id, IMGdownload.password);
	            	Log.d(TAG, "Join Popitka #" +popitka+" Status:"+result);
	            	if(!result)
	            	{
	            		Log.d(TAG, "Пауза 1.5 секунды");
	            		Thread.sleep(1500);
	            	}
	            	
	            	popitka++;
         		}
         		client.enterLocalPassiveMode();
                 if (result == true) {
                         System.out.println("User successfully logged in.");
                 } else {
                         System.out.println("Login failed!");
                         return;
                 }
                 // Changes working directory

                 boolean successss = client.changeWorkingDirectory("/set/"+folder);
                 if (successss) {
                     System.out.println("Successfully changed working directory.");
                      } else {
                      System.out.println("Failed to change working directory. See server's reply.");
                     }
	      		   }
	      		   catch(Exception ee)
	      		   {
	      			   Log.i(TAG, "Erorr ftp delete"+ee);
	      		   }
	      		/////////////////////////////////////////////////////
	      		   try
	      		   {
	      			   
	      		 FTPFile[] filess =  client.listFiles();
	      		 
	      		 	String name_ftp;
	      		   String qwerty[]=new String[filess.length];
	      		   
	      		int qw=0;
                 for (FTPFile filee : filess) 
                 {
                 	
                         if (filee.isFile())
                         {                                      
                                 name_ftp=filee.getName().toString();
                                 qwerty[qw]=name_ftp;  
                                 qw++;
                         }
                         
                 }

	      			if(fold.exists())
	      			{
	      				File[] files =  fold.listFiles();
	      				
			      		    for (File file : files) 
			      		    {
			      		    	
			      		            if (file.isFile())
			      		            {
			      		            	String name_sd=file.getName().toString();

				                        boolean statt = false;
				                        for(int qwe=0;qwe<qwerty.length;qwe++)
				                        {
				                        		if(name_sd.equals(qwerty[qwe].toString()))//qwerty[qwe] -- name ftp
				                        		{
				                        			statt = false;				                        	
				                        			break;
				                        		}
				                        		else
				                        			statt = true;
				                        			
				                        }
				                        if(statt)
				                        {
				                        	Log.d(TAG, "DELETE !!! имя файла sd:"+name_sd);
				                        	IMGdownload.MainActivity.createDummyFile(IMGdownload.cntxx.getString(R.string.delete_start)+" "+name_sd, IMGdownload.cntxx);
				                        	file.delete();
				                        }
				                        else
				                        	Log.d(TAG, "оставили файл sd:"+name_sd);
			      		            	
			      		            	
			      		            }//if fine file close
			      		         
			      		    }
		      		  }//if 0 file
	      		 }
	      		   catch(Exception wer)
	      		   {
	      			   Log.e(TAG, "Error delete list parse: "+wer);
	      		   }
	      		
		      		  }//�������� �� ���� ���� 
	      		 }  
	      		// Log.d(TAG, "/*Удаление папки завершенно....");
	            
	     	Log.d(TAG,"Побочный поток start завершенно... ");*/
	          
       
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
