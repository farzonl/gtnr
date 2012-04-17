package com.gtnightrover.lidar;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class LidarComm extends Thread {

	private InputStream serialStream;
	private OutputStream outStream;
	private int[] distances;
	private final boolean DEBUG = false;
	private long lastTime = System.currentTimeMillis();
	private byte[] currSpeed = new byte[2];

	public LidarComm(String usbPort, int speed) throws Exception {
		super(usbPort + ":" + speed);
		CommPortIdentifier portId = CommPortIdentifier
				.getPortIdentifier(usbPort);

		SerialPort port = (SerialPort) portId.open("serial talk", 4000);
		serialStream = port.getInputStream();
		outStream = port.getOutputStream();
		port.setSerialPortParams(speed, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		distances = new int[360];
	}

	public int available() {
		try {
			return serialStream.available();
		} catch (IOException e) { }
		return -1;
	}

	public byte[] read() throws IOException {
		List<Byte> arr = new ArrayList<Byte>();
		while(serialStream.available() > 0)
			arr.add(readHelper());
		byte[] rtn = new byte[arr.size()];
		for(int i=0;i<arr.size();i++)
			rtn[i] = arr.get(i);
		return rtn;
	}


	public int read(byte[] arr) throws IOException {
		int count = 0;
		while (count<arr.length && serialStream.available() > 0)
			arr[count++] = readHelper();
		return count;
	}

	private byte readHelper() throws IOException {
		while (serialStream.available() <= 0);
		return (byte)serialStream.read();
	}

	public boolean write(int i) {
		try {
			outStream.write(i);
			return true;
		} catch (IOException e) {
		}
		return false;
	}

	public boolean write(byte[] barr) {
		if (barr != null)
			try {
				outStream.write(barr);
				return true;
			} catch (IOException e) {
			}
		return false;
	}
}
