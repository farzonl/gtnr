package com.gtnightrover;

import com.gtnightrover.lidar.LidarComm;
import com.gtnightrover.lidar.LidarSerialStream;
import com.gtnightrover.visualizer.Graph;
import com.gtnightrover.visualizer.GraphWindow;
import com.gtnightrover.visualizer.StreamManager;

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Graph g = new Graph(800,600,-12000,-12000,12000,12000);
		GraphWindow f = new GraphWindow(800, 600, g);
		f.setVisible(true);
//		new StreamManager(g, f, new StreamGen()).start();
//		new StreamManager(g, f, new ArdionoSerialStream("/dev/ttyACM0", 115200)).start();
		LidarSerialStream lss = new LidarSerialStream("/dev/ttyACM0", 115200);
		lss.start();
		LidarComm dfr = new LidarComm("/dev/ttyUSB0", 9600);
		System.out.println("Lidar Serial Stream Started");
		new StreamManager(g, f, lss, dfr).start();
	}

}
