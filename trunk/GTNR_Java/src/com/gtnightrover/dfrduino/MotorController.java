package com.gtnightrover.dfrduino;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.gtnightrover.lidar.LidarComm;

public class MotorController {
	
	public static final boolean TEST_DATA = true;
	public static final String SENSORS_LOG_FILENAME = "/home/david/EclipseProjects/GTNR/GTNR_web/log/sensors.log";

	private int[] ir;
	private int[] photocells;
	private LidarComm serial;
	private boolean autoLogging;
	
	public int[] getIR() {
		int[] arr = new int[ir.length];
		for(int i=0;i<ir.length;i++)
			arr[i] = ir[i];
		return arr;
	}
	
	public int[] getPhotocells() {
		int[] arr = new int[photocells.length];
		for(int i=0;i<photocells.length;i++)
			arr[i] = photocells[i];
		return arr;
	}

	public MotorController(LidarComm lc) {
		this(lc, false);
	}

	public MotorController(LidarComm lc, boolean autoLogging) {
		serial = lc;
		ir = new int[2];
		photocells = new int[4];
		this.autoLogging = autoLogging;
	}
	
	public boolean update() {
		if (!TEST_DATA) {
			byte[] bytes = " Q  M".getBytes();
			bytes[0] = (byte)0xFF;
			bytes[2] = (byte)0;
			bytes[3] = (byte)0;
			serial.write(bytes);
			while(serial.available() <= 0);
			bytes = new byte[10];
			try {
				int size = serial.read(bytes);
				if (bytes[1] == (byte)SerialProtocol.REQ.ordinal()) {
					int dataStart = 3;
					for(int i=dataStart;i<ir.length+dataStart;i++)
						ir[i] = bytes[i+dataStart];
					for(int i=dataStart+ir.length;i<photocells.length+ir.length+dataStart;i++)
						ir[i] = bytes[i+ir.length+dataStart];
					if (autoLogging)
						log();
					return true;
				}
			} catch (IOException e) {	}
			return false;
		} else {
			for(int i=0;i<ir.length;i++)
				ir[i] = (int)(Math.random() * 100) + 1200;
			for(int i=0;i<photocells.length;i++)
				photocells[i] = (int)(Math.random()*250);
			if (autoLogging)
				log();
			return true;
		}
	}
	
	public boolean log() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(SENSORS_LOG_FILENAME, false));		
			for(int i : ir)
				bw.append(i+"\n");		
			for(int i : photocells)
				bw.append(i+"\n");
			bw.close();
			return true;
		} catch (IOException e) { /* Do nothing */ }
		return false;
	}
	
}
