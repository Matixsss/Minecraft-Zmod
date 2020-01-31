package com.DystryktZ;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ZmodJsonClient {
	
	public static boolean notification = true;
	public static boolean blockinfo = false;
	public static boolean entityinfo = false;
	
	private static String path;
	
	public static void setPath(String p)
	{
		path = p;
	}
	
	public static void loadConfigs()
	{
		if(!Files.exists(Paths.get(path+File.separator+"PlayerSettings.json")))
    	{
			saveConfigs();
    	}
		
		 try(BufferedReader f = new BufferedReader(new FileReader(path+File.separator+"PlayerSettings.json")))
		 {
			 String s;
			 while((s=f.readLine()) != null)
			 {
				 if(s.substring(0,1).equals("#"))
				 {
					 continue;
				 }
				 else
				 {
					 if(s.contains("Notification:"))
					 {
						 if(s.contains("true"))
						 {
							 notification = true;
						 }
						 else if (s.contains("false"))
						 {
							 notification = false;
						 }
						 continue;
					 }
					 
					 if(s.contains("Blockinfo:"))
					 {
						 if(s.contains("true"))
						 {
							 blockinfo = true;
						 }
						 else if (s.contains("false"))
						 {
							 blockinfo = false;
						 }
						 continue;
					 }
					 
					 if(s.contains("Entityinfo:"))
					 {
						 if(s.contains("true"))
						 {
							 entityinfo = true;
						 }
						 else if (s.contains("false"))
						 {
							 entityinfo = false;
						 }
						 continue;
					 }
				 }

			 }
			 
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	}
	
	public static void saveConfigs() //default settings
	{
		String[] lines = new String[4];
		lines[0] = "#Player settings:"+System.lineSeparator();
		lines[1] = "Notification:"+notification+System.lineSeparator();
		lines[2] = "Blockinfo:"+blockinfo+System.lineSeparator();
		lines[3] = "Entityinfo:"+entityinfo;
	
		 try(FileWriter f = new FileWriter(path+File.separator+"PlayerSettings.json"))
		 {
			for(String l : lines)
			{
				f.write(l);
			}
			f.flush();
		 }
		 catch(Exception ex)
		 {
			 ex.printStackTrace();
		 }
	}

}
