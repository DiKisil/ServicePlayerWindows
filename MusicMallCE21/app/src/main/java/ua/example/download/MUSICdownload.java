package ua.example.download;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;


import android.content.Context;
import android.util.Log;


import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;

import ua.example.player.Constants;
import ua.example.ftp.MainActivity;
import ua.player.musicmallce.R;

public class MUSICdownload
{
	private static final String TAG = "Закачка";
	public static MainActivity MainActivity;
    private static AffableThread2 mSecondThread;
	public static String ftp,object, id,password;
    public FTPClient mFTPClient = null; 
    public static ArrayList<HashMap<String, String>> Music;
    public static Context cntxx;
    Cipher cipher;

	public MUSICdownload(Context cntx, String sftp, String sobject, String sid,String spassword)
	{
		cntxx=cntx;
		ftp=new String(sftp);
		object=new String(sobject);
		id=new String(sid);
		password=new String(spassword);
		MainActivity= new MainActivity(cntx, sftp, sobject, sid, spassword); 
	}



    public  void music(ArrayList<HashMap<String, String>> List) 
    { 
    	mSecondThread = new AffableThread2();

    	Music=List;
    	Log.i("MainActivity","Поток. Status: : "+AffableThread2.download_start);
    	if(!AffableThread2.download_start)
    	{
    		Log.i("MainActivity","Поток Start");
       	//�������� ������
        mSecondThread.start();
    	}
    	else
    		Log.i("MainActivity","Поток что уже работает. Status:"+AffableThread2.download_start);
        //mSecondThread.setPriority(Thread.MAX_PRIORITY);//������ ������
        Log.d(TAG,"Главный поток музыки завершён...");
  
    }
  
}


/////////////////////////////////////////////////////////////////////////////////


class AffableThread2 extends Thread
{
	static boolean download_start= false;
	private static final String TAG = "Download";
	public FTPClient mFTPClient = null;


	  private  Object TAG_FOLDER ;
	 String folder;
	 String dele;
	 String name;
	 String name_ftp;
	 String way=Constants.WAY_MUSIC();
	 long size_ftp;
	 //FileUtils FileUtils;


	 
    @Override
    public void run()
    { 
    	download_start= true;
    	mFTPClient= new FTPClient();
    	//Log.d(TAG,"Привет из побочного потока музыки!");
    	try
    	{
    		String str [] =  new String[176];//175 �� ����� ���� +1 �����
    		String del [] =  new String[176];//175 �� ����� ���� +1 �����
    		//del[0]="start";
    		del[0]="img";
    		del[1]="adv";
    		del[2]="log";
  		    int p=0;
  		    int d=3;
  		    boolean retry_folder=true;
  		    try
  		    {
  		  for(int i=0;i<7;i++)
       	 {
 	      	HashMap<String, String> list=MUSICdownload.Music.get(i);     
 	      	  for(int j=1;j<6;j++)
 	      	  {
 	      		for(int k=1;k<6;k++)
 	        	  {
 	      		 
 		      		 TAG_FOLDER = j+"_"+k;
 		      		 String test=new String(list.get(TAG_FOLDER));
 			      		 if(!test.equals("null"))
 			      		 {
 			      			dele=test; 
 			      				 del[d]=dele;
 			      				 d++;
 			      				 
 
 			      		 }
 	        	  }
 	      	  }
       	 }
  		  Log.d(TAG, "/*Удаление всех папок*/");
  		  
  		File delete_folder=new File(way);
 		 String name_dir = null ;
 		 boolean delete=false;
        for (File file : delete_folder.listFiles()) 
        {
            	if(file.isDirectory())
            		{
	            		name_dir = file.getName();
	            		 for(int t=0;t<del.length;t++)
	             		  {
	             			  if(del[t]==null)  
	             				  break;
			            		if(name_dir.equals(del[t]))
			            		{
			            			delete=false;
			            			break;
			            		}
			            		else
			            			delete=true;
	             		  }
	            		 File delete_folder_name=new File(way+name_dir+File.separator);
	            		 if(delete)
	            		 {
	            			 Log.d(TAG, "Удаление!! "+delete_folder_name);
	            			 FileUtils.deleteDirectory(delete_folder_name); 
	            			 MUSICdownload.MainActivity.createDummyFile(MUSICdownload.cntxx.getString(R.string.delete_folder_music)+" "+name_dir, MUSICdownload.cntxx);
	            		 }
	            		 else
	            			 Log.d(TAG, "Оставляем "+delete_folder_name);
            		}//next name dir
        }
  		    }
  		    catch(Exception e)
  		    {
  		    	e.getMessage();
  		    	Log.d(TAG, e.toString());
  		    }
  		    
  		  Log.d(TAG, "/*Удаление всех папок завершено....*/");

    	for(int i=0;i<7;i++)
      	 {
	      	HashMap<String, String> list=MUSICdownload.Music.get(i);
	      	 Log.d(TAG,"List_"+i+list);
	     
	      	  for(int j=1;j<6;j++)
	      	  {
	      		for(int k=1;k<6;k++)
	        	  {
	      			Log.d(TAG,"  download: "+j+"_"+k);
		      		 TAG_FOLDER = j+"_"+k;
		      		 String test=new String(list.get(TAG_FOLDER));
			      		 if(!test.equals("null"))
			      		 {
			      		   folder = test;
		 
			      		///////////////////�������//////////////////////////   
			      		 //if(mHour!=0)
			      		  {
			      		 Log.d(TAG, "//Загрузка всех папок //");
			      		 {
			      		 Log.d(TAG," folder download:"+folder);
			      		 System.out.println("folder download:"+folder);

			      		
			             
			           
			            try
			            {
			            	 boolean result = false;
			            	try
				            {
			            		boolean qwertyu = false;
			            		int popitka=1;
			            		while(!qwertyu&&popitka<30)
			            		{		
			            		try
			            		{
				            	mFTPClient.connect(MUSICdownload.ftp, 21);
			            		}
			            		catch(Exception rr)
			            		{
			            			rr.getMessage();
			            		}
				            	qwertyu=mFTPClient.isConnected();
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
				            	result = mFTPClient.login(MUSICdownload.id, MUSICdownload.password);
				            	Log.d(TAG, "Join Popitka #" +popitka+" Status:"+result);
				            	if(!result)
				            	{
				            		Log.d(TAG, "Пауза 1.5 секунды");
				            		Thread.sleep(1500);
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
	                       ///////////////////////////////////////////////////////////// ************************************
	                        try
	                        {
	                        boolean success = mFTPClient.changeWorkingDirectory("/music/"+folder);
	                        
	                        
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
	                                	size_ftp=file.getSize();
	                                      //  System.out.println("File name : " + file.getName()+" "+file.getSize());
	                                     //   Log.d(TAG,"File name : " + file.getName());
	                                        name=file.getName().toString();
	                               
	                
			      
			            String remoteFile = "/music/"+folder+File.separator+name;
			            String Path = way+folder+File.separator;
			            String savePath = Path+name;
			            File music_path = new File(way+folder+File.separator+name);
			            
			            if(music_path.length()!=size_ftp)
			            	 System.out.println("РАЗМЕР НЕ РАВЕН");

			            else
			            	 System.out.println("РАЗМЕР  РАВЕН!!!!");
			            
			      if((!(music_path.exists())||music_path.length()<=0||music_path.length()!=size_ftp)&&result)
			          { 
			    	  Log.d(TAG,"Загрузка "+folder+File.separator+name+"\n"+" size: "+humanReadableByteCount(size_ftp, false));
			    	  
			    	  MUSICdownload.MainActivity.createDummyFile(MUSICdownload.cntxx.getString(R.string.download_music)+" "+folder+File.separator+name+MUSICdownload.cntxx.getString(R.string.size_music)+humanReadableByteCount(size_ftp, false), MUSICdownload.cntxx);
			            File File = new File(Path);
			            File.mkdirs();
			            
			            File downloadFile = new File(savePath);
			            
			            	  try
			             {
			            		   boolean qwertyu = false;
				            		int popitka=1;
				            		while(!qwertyu&&popitka<30)
				            		{		
				            		try
				            		{
					            	mFTPClient.connect(MUSICdownload.ftp, 21);
				            		}
				            		catch(Exception rr)
				            		{
				            			rr.getMessage();
				            		}
					            	qwertyu=mFTPClient.isConnected();
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
				            		while(!qwertyu&&popitka<30)
				            		{
				            			try
				            			{
				            				qwertyu = mFTPClient.login(MUSICdownload.id, MUSICdownload.password);
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
			               	mFTPClient.enterLocalPassiveMode();
			             	mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
			             	mFTPClient.setBufferSize(8192*8192);

			            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));
			        
			            InputStream inputStream = mFTPClient.retrieveFileStream(remoteFile);

			            byte[] bytesArray = new byte[8192];
			            int bytesRead;
			            while ((bytesRead = inputStream.read(bytesArray)) != -1)
			            {
			                outputStream.write(bytesArray, 0, bytesRead);

			            }

			            //�������� �� 5-15 ��� 
			     /*       boolean successs = mFTPClient.completePendingCommand();
			            if (successs)
			            {
			         	   Log.d(TAG,"Download success");
			            }
			            else
			            	Log.d(TAG,"Download failed");*/
			            outputStream.close();
			            inputStream.close();
			            
			         }
			             catch (IOException ex) {
			                 System.out.println("Error: " + ex.getMessage());
			                 ex.printStackTrace();
			             } finally {
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
		                        	mFTPClient.disconnect();
		                        } catch (FTPConnectionClosedException e) {
		                                System.out.println(e);
		                        }
		                }
			      		 }
			      		 Log.d(TAG, "//Загрузка всех папок завершено....//");
			      		 }
			      		 ///////////////////////////////��������////////////////////
			      		// if(mHour==0||mHour==1)
			      		 {
			      			 Log.d(TAG, "/*Удаление  папки */");
			      		 Log.d("Проверка", "В folder:"+i+j+k+"_"+folder);
			      		    p++;
			      		  retry_folder=true;
			      		  
			      		    for(int y=1;y<p;y++)
			      		    {
			      		    	//Log.d("��������", "� for�:"+p+" "+y);
			      		    	Log.d("Проверка", "В for элемент:"+str[y]);
			      		    	
			      		    	if(str[y].equals(folder))
			      		    	{
			      		    		Log.d(TAG, "Папка  "+folder+"  была и в проверке не нуждаается!");
			      		    		//retry_folder=false;
			      		    		break;
			      		    		
			      		    	}
			      		    }
			      		  str[p]=folder; 
			      		  
				      		/**�������� ������*/
				      		  if(retry_folder)
				      		  {
				      			
			      		   
			      		   File fold=new File(way+folder+File.separator);
			      		   ///////////////////////////////////////////////////
			      		   try
			      		   {
			      			 boolean qwertyu = false;
			            		int popitka=1;
			            		while(!qwertyu&&popitka<10)
			            		{		
			            		try
			            		{
				            	mFTPClient.connect(MUSICdownload.ftp, 21);
			            		}
			            		catch(Exception rr)
			            		{
			            			rr.getMessage();
			            		}
				            	qwertyu=mFTPClient.isConnected();
				            	Log.d(TAG, "Connect Popitka #" +popitka+" Status:"+qwertyu);
				            	if(!qwertyu)
				            	{
				            		Log.d(TAG, "Пауза 1.5 секунд");
				            		Thread.sleep(1500);
				            	}
				            	popitka++;
			            		}
			            		/////////////
	                        boolean result = false ;
	                        popitka=1;
		            		while(!result&&popitka<30)
		            		{
			            	result = mFTPClient.login(MUSICdownload.id, MUSICdownload.password);
			            	Log.d(TAG, "Join Popitka #" +popitka+" Status:"+result);
			            	if(!result)
			            	{
			            		Log.d(TAG, "Пауза 1.5 секунд");
			            		Thread.sleep(1500);
			            	}
			            	
			            	popitka++;
		            		}
	                        mFTPClient.enterLocalPassiveMode();
	                        if (result == true) {
	                                System.out.println("User successfully logged in.");
	                        } else {
	                                System.out.println("Login failed!");
	                                return;
	                        }
	                        // Changes working directory

	                        boolean successss = mFTPClient.changeWorkingDirectory("/music/"+folder);
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
			      			   
			      		 FTPFile[] filess =  mFTPClient.listFiles();
			      		 
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
						                        	MUSICdownload.MainActivity.createDummyFile(MUSICdownload.cntxx.getString(R.string.delete_music)+" "+name_sd, MUSICdownload.cntxx);
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
			      		 Log.d(TAG, "/*Удаление папки завершено....*/");
			            
			            
			            /////////////////////////////////////////////////
			      		 }//if !null
	      		 
	        	  }  	//for	
	      	  }
	      	  
	     }
    	}
    	catch (Exception e) {
            System.out.println("Error ALL: " + e.getMessage());
            e.printStackTrace();
            Log.e(TAG, "ERORR DOWNLOAD"+e);
            Log.e("MainActivity", "ERORR DOWNLOAD"+e);
        }
    	finally
    	{
    		download_start= false;
    	}
    	
    
    	
    	Log.d(TAG,"Побочный поток музыки завершён...");
}   
    
    public static long folderSize(File directory) {
        long length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }
    public static int folderCount(File directory) 
    {
        int length = 0;
        		if(directory.exists())
        		{
			        for (File file : directory.listFiles()) 
			        {
			            if (file.isFile())
			                length ++; 
			        }
        
        		}
        return length;
    }
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 8192;
	        if (bytes < unit)
	        	return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
