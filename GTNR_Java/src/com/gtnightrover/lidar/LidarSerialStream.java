package com.gtnightrover.lidar;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

public class LidarSerialStream extends Thread {

	private InputStream serialStream;
	private int[] distances;
	private final boolean DEBUG = false;

	public LidarSerialStream(String usbPort, int speed) throws Exception {
		super(usbPort + ":" + speed);
		CommPortIdentifier portId = CommPortIdentifier
				.getPortIdentifier(usbPort);

		SerialPort port = (SerialPort) portId.open("serial talk", 4000);
		serialStream = port.getInputStream();
		port.setSerialPortParams(speed, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		distances = new int[360];
	}

	public int[] getDistances() {
		int[] arr = new int[distances.length];
		synchronized (distances) {
			System.arraycopy(distances, 0, arr, 0, distances.length);
		}
		return arr;
	}

	@Override
	public void run() {
		int curr = 0x00;
		while (true) {
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
		}
	}

	private int[] getSerialBytes() throws IOException {
		int[] arr = new int[22];
		arr[0] = 0xFA;
		arr[1] = serialStream.read();
		if (arr[1] < 0xA0 || arr[1] > 0xF9)
			return null;
		for (int i = 2; i < arr.length; i++)
			arr[i] = serialStream.read();
		return arr;
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
				Distance dist = extractDistance(arr[((i + 1) * 4) + i],
						arr[((i + 1) * 4) + i + 1]);
				distances[((addr - 0xA0) * 4) + i] = (dist.error == 0) ? dist.dist : 0;
				if (DEBUG)
					System.out.print((addr - 0xA0) + "+" + i + ":\t" + dist
							+ "\t");
			}
			if (DEBUG)
				System.out.println();
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

	public static void main(String[] args) throws Exception {
		LidarSerialStream lss = new LidarSerialStream("/dev/ttyACM0", 115200);
		lss.start();
	}
}
