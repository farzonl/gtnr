package com.gtnightrover.dfrduino;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class BatteryController {
	
	public static final boolean TEST_DATA = true;
	public static final String POWER_LOG_FILENAME = "/home/david/EclipseProjects/GTNR/GTNR_web/log/power.log";
	
	private double soc = 100;
	private double v_bat;
	private double v_panel;
	private double c_bat;
	private double c_panel;
	private double w;
	private boolean autoLogging;
	private SerialComm serial;

	public double getSoc() {
		return soc;
	}

	public double getV_bat() {
		return v_bat;
	}

	public double getV_panel() {
		return v_panel;
	}

	public double getC_bat() {
		return c_bat;
	}

	public double getC_panel() {
		return c_panel;
	}

	public double getW() {
		return w;
	}

	public BatteryController(SerialComm lc) {
		this(lc, false);
	}

	public BatteryController(SerialComm lc, boolean autoLogging) {
		serial = lc;
		this.autoLogging = autoLogging;
	}
	
	public boolean update() {
		if (!TEST_DATA) {
			byte[] bytes = " Q  P".getBytes();
			bytes[0] = (byte)0xFF;
			bytes[2] = (byte)0;
			bytes[3] = (byte)0;
			serial.write(bytes);
			while(serial.available() <= 0);
			bytes = new byte[10];
			try {
				int size = serial.read(bytes);
				if (bytes[1] == (byte)SerialProtocol.REQ.ordinal()) {
					soc = bytes[3];
					v_bat = bytes[3];
					v_panel = bytes[3];
					c_bat = bytes[3];
					c_panel = bytes[3];
					w = v_bat * c_bat;
					if (autoLogging)
						log();
					return true;
				}
			} catch (IOException e) {	}
			return false;
		} else {
			soc -= Math.random() * .05;
			v_bat = Math.random() * 1.5 + 13;
			v_panel = Math.random() * 4 + 16;
			double current = Math.random() * 1.5;
			c_panel = Math.random() * current;
			c_bat = current - c_panel;
			w = v_bat * c_bat;
			if (autoLogging)
				log();
			return true;
		}
	}
	
	public boolean log() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(POWER_LOG_FILENAME, false));
			bw.append(soc+"\n");
			bw.append(v_bat+"\n");
			bw.append(v_panel+"\n");
			bw.append(c_bat+"\n");
			bw.append(c_panel+"\n");
			bw.close();
			return true;
		} catch (IOException e) { /* Do nothing */ }
		return false;
	}
}
