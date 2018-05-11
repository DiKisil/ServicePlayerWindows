package ua.example.player;

import java.io.File;


public class Directory {

	public  void Created_directory(String dir)
	    {
		

			File created = new File(dir+"/MusicMall/");
			created.mkdirs();
			
			Creted_other(dir+"/MusicMall/");
			
			
				
	    }
	 
	 private void Creted_other(String way) {
		 
		 File new_dir=new File(way+"img/");
		 new_dir.mkdirs();
		 //new_dir=new File(way+"img/");
		 //new_dir.mkdirs();
		 
		
	}

}
