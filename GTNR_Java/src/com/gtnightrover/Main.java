package com.gtnightrover;

import com.gtnightrover.visualizer.ArdionoSerialStream;
import com.gtnightrover.visualizer.Graph;
import com.gtnightrover.visualizer.GraphWindow;
import com.gtnightrover.visualizer.StreamManager;

public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Graph g = new Graph(800,600,-20000,-20000,20000,20000);
		GraphWindow f = new GraphWindow(800, 600, g);
		f.setVisible(true);
//		new StreamManager(g, f, new StreamGen()).start();
		new StreamManager(g, f, new ArdionoSerialStream("/dev/ttyACM0", 115200)).start();
	}

}
