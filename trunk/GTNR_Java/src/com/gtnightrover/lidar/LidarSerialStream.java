package com.gtnightrover.lidar;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.gtnightrover.visualizer.Graph;

public class LidarSerialStream extends Thread {

	private InputStream serialStream;
	private OutputStream outStream;
	private int[] distances;
	private final boolean DEBUG = false;
	private boolean TEST_DATA = true;
	private long lastTime = System.currentTimeMillis();
	private byte[] currSpeed = new byte[2];
	private LidarComm driver;

	public LidarSerialStream(String usbPort, int speed) throws Exception {
		super(usbPort + ":" + speed);
		if (usbPort != null) {
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier(usbPort);
	
			SerialPort port = (SerialPort) portId.open("serial talk", 4000);
			serialStream = port.getInputStream();
			outStream = port.getOutputStream();
			port.setSerialPortParams(speed, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		}
		distances = new int[360];
	}

	public LidarSerialStream(String usbPort, int speed, LidarComm driver) throws Exception {
		super(usbPort + ":" + speed);
		if (usbPort != null) {
			CommPortIdentifier portId = CommPortIdentifier
					.getPortIdentifier(usbPort);
	
			SerialPort port = (SerialPort) portId.open("serial talk", 4000);
			serialStream = port.getInputStream();
			outStream = port.getOutputStream();
			port.setSerialPortParams(speed, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		}
		distances = new int[360];
		this.driver = null;//driver;
	}

	public int[] getDistances() {
		int[] arr = new int[distances.length];
		synchronized (distances) {
			System.arraycopy(distances, 0, arr, 0, distances.length);
		}
		return arr;
	}
	
	public byte[] getCurrSpeed() {
		byte[] arr = new byte[3];
		arr[0] = (byte)'s';
		synchronized (currSpeed) {
			arr[1] = currSpeed[0];
			arr[2] = currSpeed[1];
		}
		return arr;
	}

	@Override
	public void run() {
		int curr = 0x00;
		long lastTime = System.currentTimeMillis();
		while (true && !TEST_DATA) {
			try {
				if (serialStream.available() > 0) {
					curr = serialStream.read();
					if (curr == 0xFA) {
						int[] arr = getSerialBytes();
						if (arr != null) {
							// outputDump(arr);
							handlePacket(arr);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Automatically take care of speed controller if one exists.
			// TODO: implement with correct protocol.
			if (System.currentTimeMillis() - lastTime > 100) {
				lastTime = System.currentTimeMillis();
				if (driver != null)
					driver.write(getCurrSpeed());
			}
		}
		while (TEST_DATA) {
			for(int i=0;i<distances.length;i++) 
				distances[i] = (int)(5000 + Math.random() * 250);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) { }
		}
	}

	private int[] getSerialBytes() throws IOException {
		int[] arr = new int[22];
		arr[0] = 0xFA;
		arr[1] = serialStream.read();
		if (arr[1] < 0xA0 || arr[1] > 0xF9)
			return null;
		for (int i = 2; i < arr.length; i++)
			arr[i] = readHelper();
		return arr;
	}
	
	private int readHelper() throws IOException {
		while(serialStream.available() <= 0);
		return serialStream.read();
	}

	private void outputDump(int[] arr) throws IOException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++)
			sb.append(toHex(arr[i])).append("  ");
		System.out.println(sb.toString());
	}

	private String toHex(int b) {
		String a = Integer.toHexString(b);
		return a.length() == 1 ? "0" + a : a;
	}

	private void handlePacket(int[] arr) throws IOException {
		if (arr.length != 22)
			return;
		int addr = arr[1];
		synchronized (distances) {
			for (int i = 0; i < 4; i++) {
				
				Distance dist = extractDistance(arr[(i * 4) + 4],
						arr[(i * 4) + 5]);
				distances[((addr - 0xA0) * 4) + i] = (dist.error == 0) ? dist.dist : 0;
				if (DEBUG)
					System.out.print((addr - 0xA0) + "+" + i + ":\t" + dist
							+ "\t");
			}
			if (DEBUG)
				System.out.println();
		}
		// 4A:00 and is happy between around 49:D0 to 4A:2F
		synchronized (currSpeed) {
			currSpeed[0] = (byte)arr[3];
			currSpeed[1] = (byte)arr[4];
		}
		long temp = System.currentTimeMillis();
		if (temp - lastTime > 200) {
			lastTime = temp;
			if (DEBUG)
				System.out.println(Integer.toHexString(arr[3]) + ":" + Integer.toHexString(arr[4]));
		}
	}

	private Distance extractDistance(int dist1, int dist2) throws IOException {
		// System.out.println(toHex(dist1) + " -- " + toHex(dist2));
		Distance dist = new Distance(0,0);
		if ((dist2 & (1 << 8)) > 0)
			dist.error = -1;
		if ((dist2 & (1 << 7)) > 0)
			dist.error = -2;
		dist.dist = ((dist2 & 0x3F) << 8) | dist1;
		return dist;
	}
	
	public boolean write(int i) {
		try {
			outStream.write(i);
			return true;
		} catch (IOException e) { }
		return false;
	}
	
	public boolean write(byte[] barr) {
		if (barr != null)
			try {
				outStream.write(barr);
				return true;
			} catch (IOException e) { }
		return false;
	}
	
	public Point getMaximalPoint() {
		double max_dist = 0;
		int max_index = 0;
		double temp;
		for(double i=0;i<distances.length>>1;i++) {
			temp = distances[(int)i];
			if (temp > max_dist) {
				max_dist = temp;
				max_index = (int)i;
			}
			temp = distances[(int)Math.abs(359-i)];
			if (temp > max_dist) {
				max_dist = temp;
				max_index = (int)i;
			}
		}
		return Graph.toPixel(Math.toRadians(max_index), distances[max_index]);
	}
	
	public Point getWeightedMaximalPoint() {
		double max_dist = 0;
		int max_index = 0;
		double temp;
		for(double i=0;i<distances.length>>1;i++) {
			temp = distances[(int)i] * (2 - i / 180);
			if (temp > max_dist) {
				max_dist = temp;
				max_index = (int)i;
			}
			temp = distances[(int)Math.abs(359-i)] * (2 - i / 180);
			if (temp > max_dist) {
				max_dist = temp;
				max_index = (int)i;
			}
		}
		return Graph.toPixel(Math.toRadians(max_index), distances[max_index]);
	}
	
	private static class Distance {
		public int dist;
		public int error;
		public Distance(int dist, int error) {
			this.dist = dist;
			this.error = error;
		}
		@Override
		public String toString(){
			return dist+"";
		}
	}
}
