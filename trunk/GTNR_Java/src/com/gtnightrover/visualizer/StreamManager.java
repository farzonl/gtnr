package com.gtnightrover.visualizer;

import com.gtnightrover.lidar.LidarComm;
import com.gtnightrover.lidar.LidarSerialStream;

public class StreamManager extends Thread {

	private Graph g;
	private GraphWindow gw;
	private LidarSerialStream lss;
	public LidarComm dfr;
	
	public StreamManager(Graph g, GraphWindow gw, LidarSerialStream lss) {
		super();
		this.g = g;
		this.gw = gw;
		this.lss = lss;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				g.clear();
				g.addAll(lss.getDistances());
				
				g.start = Graph.toPixel(0, 0);
				g.end = lss.getWeightedMaximalPoint();
				
				gw.repaint();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.exit(0);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Graph g = new Graph(800,600,-20000,-20000,20000,20000);
		GraphWindow f = new GraphWindow(800, 600, g);
		f.setVisible(true);
//		new StreamManager(g, f, new StreamGen()).start();
	}
	
	public static class StreamGen implements PointsStream {

		int count = 0;
		int max = 10;
		
		@Override
		public boolean hasPoints() {
			return count++ < max;
		}

		@Override
		public double[][] getPoints() {
			double[][] arr = new double[360][2];
			for (int i=0;i<360;i++) {
				arr[i][0] = i * 2.0 * Math.PI / 360;
				arr[i][1] = Math.random() * 20000;
			}
			return arr;
		}
	}

}
