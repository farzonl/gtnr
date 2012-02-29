package com.gtnightrover.visualizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ArdionoSerialStream implements PointsStream {

	private List<String> lines;
	private File streamFileName;

	public ArdionoSerialStream(String usbPort) throws FileNotFoundException {
		lines = new ArrayList<String>();
		File f = new File(usbPort);
		if (!f.exists())
			throw new FileNotFoundException();
	}

	@Override
	public boolean hasPoints() {
		Scanner serialStream = null;
		try {
			serialStream = new Scanner(streamFileName);
			while (serialStream.hasNextLine())
				lines.add(serialStream.nextLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
