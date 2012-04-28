package com.gtnightrover.dfrduino;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.gtnightrover.lidar.LidarSerialStream;

public class Lidar {

	private LidarSerialStream lss;
	private boolean automaticLogging;
	
	public Lidar(LidarSerialStream lss) {
		this(lss, false);
	}
	
	public Lidar(LidarSerialStream lss, boolean automaticLogging) {
		this.lss = lss;
		this.automaticLogging = automaticLogging;
	}
	
	public int[] getDistances() {
		int[] arr = lss.getDistances();
		if (automaticLogging)
			log(arr);
		return arr;
	}
	
	public Point getMaximalPoint() {
		return lss.getWeightedMaximalPoint();
	}
	
	public void log(int[] arr ) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("/home/david/EclipseProjects/GTNR/GTNR_web/log/dist.log", false));			
			for(int i : arr)
				bw.append(i+"\n");
			bw.close();
		} catch (IOException e) { /* Do nothing */ }
	}
}
