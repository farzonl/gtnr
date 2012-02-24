package com.gtnightrover;

import com.gtnightrover.visualizer.Graph;
import com.gtnightrover.visualizer.GraphWindow;
import com.gtnightrover.visualizer.StreamManager;
import com.gtnightrover.visualizer.StreamManager.StreamGen;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Graph g = new Graph(800,600,-20000,-20000,20000,20000);
		GraphWindow f = new GraphWindow(800, 600, g);
		f.setVisible(true);
		new StreamManager(g, f, new StreamGen()).start();
	}

}
