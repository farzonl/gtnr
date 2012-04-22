package com.gtnightrover.log;

import java.io.BufferedWriter;
import java.io.FileWriter;

import com.gtnightrover.lidar.LidarSerialStream;

public class LidarWrite extends Thread  {

	LidarSerialStream ls_stream;
	public LidarWrite(LidarSerialStream ls_stream)
	{
		this.ls_stream = ls_stream;
	}
	
	

		@Override
		public void run() {
			try{
				  // Create file 
				  int[] depth_arr    = ls_stream.getDistances();
				  FileWriter fstream = new FileWriter("lidar.log");
				  BufferedWriter out = new BufferedWriter(fstream);
				  for(int i = 0; i < depth_arr.length; i++)
					  out.write(depth_arr[i]+"\n");
				  //Close the output stream
				  out.close();
				  }catch (Exception e){//Catch exception if any
				  System.err.println("Error: " + e.getMessage());
				  }
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}