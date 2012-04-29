package com.gtnightrover.dfrduino;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Atom {
	
	public static final boolean TEST_DATA = true;
	public static final String ATOM_LOG_FILENAME = "/home/david/EclipseProjects/GTNR/GTNR_web/log/atom.log";
	
	private double cpu1;
	private double cpu2;
	private double memory;
	private double alive;
	private double sleep;
	private double io;
	private boolean autoLogging;

	public double getCpu1() {
		return cpu1;
	}

	public double getCpu2() {
		return cpu2;
	}

	public double getMemory() {
		return memory;
	}

	public double getAlive() {
		return alive;
	}

	public double getSleep() {
		return sleep;
	}

	public double getIo() {
		return io;
	}

	public Atom() {
		this(false);
	}

	public Atom(boolean autoLogging) {
		this.autoLogging = autoLogging;
	}
	
	public boolean update() {
		if (!TEST_DATA) {
			// TODO: get values through command line 
			return false;
		} else {
			cpu1 = Math.random() * 100;
			cpu2 = Math.random() * cpu1;
			memory = Math.random() * 50;
			sleep = Math.random()* 10 + 90;
			alive = Math.random() * (100-sleep);
			io = Math.random() * (100 - sleep - alive);
			if (autoLogging)
				log();
			return true;
		}
	}
	
	public boolean log() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(ATOM_LOG_FILENAME, false));
			bw.append(cpu1+"\n");
			bw.append(cpu2+"\n");
			bw.append(memory+"\n");
			bw.append(alive+"\n");
			bw.append(sleep+"\n");
			bw.append(io+"\n");
			bw.close();
			return true;
		} catch (IOException e) { /* Do nothing */ }
		return false;
	}

}
