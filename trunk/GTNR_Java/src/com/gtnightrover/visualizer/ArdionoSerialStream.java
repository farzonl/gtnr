package com.gtnightrover.visualizer;

import java.util.ArrayList;
import java.util.List;

import com.gtnightrover.serial.SerialComm;

public class ArdionoSerialStream implements PointsStream {

	private List<String> lines;
	private SerialComm serialStream;

	public ArdionoSerialStream(String usbPort, int speed) throws Exception {
		lines = new ArrayList<String>();
		serialStream = new SerialComm(usbPort, speed);
	}

	@Override
	public boolean hasPoints() {
		String curr = "";
		long time = System.currentTimeMillis();
		long lapse = System.currentTimeMillis() - time;
		while (lapse < 100 && !curr.equals("Distances")) {
			if (serialStream.hasNextLine()) {
				curr = serialStream.getNextLine();
				lines.add(curr);
			}
		}
		return lines.size() > 0;
	}

	@Override
	public double[][] getPoints() {
		if (hasPoints()) {
			double[][] arr = new double[lines.size()][2];
			int i = 0;
			for (String s : lines) {
				String[] sarr = s.split("\\s");
				if (sarr.length == 2) {
					try {
						arr[i][1] = Double.parseDouble(sarr[1]);
						arr[i][0] = i * 2.0 * Math.PI / 360;
						i++;
					} catch (Exception e) {
						// ignore
					}
				}
			}
			lines.clear();
			return arr;
		}
		return null;
	}

}
