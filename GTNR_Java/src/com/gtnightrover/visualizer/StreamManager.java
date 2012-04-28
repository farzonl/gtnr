package com.gtnightrover.visualizer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import com.gtnightrover.Graph.PathBuilder;
import com.gtnightrover.Graph.Point;
import com.gtnightrover.lidar.LidarComm;
import com.gtnightrover.lidar.LidarSerialStream;
import com.gtnightrover.serial.SerialWriteRunner;

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
		int count = 0;
		while (true) {
			try {
				g.clear();
				g.addAll(lss.getDistances());
				
				
				
				PathBuilder path = new PathBuilder(lss.getDistances());
				ArrayList<Point> pList = PathBuilder.fromVtoP(path.weighted_path());
				Point p0 = pList.get(0);
				Point p1 = pList.get(pList.size() - 1);
				System.out.println(p1);
				DoublePoint dp0 = new DoublePoint(p0.getX(), p0.getY());
				DoublePoint dp1 = new DoublePoint(p1.getX(), p1.getY());
				System.out.println(dp1);
				java.awt.Point pp0 = g.toPixel(dp0);
				java.awt.Point pp1 = g.toPixel(dp1);
				System.out.println(pp1);
				g.start = pp0;
				g.end = pp1;
				ArrayList<byte[]> Abyte = SerialWriteRunner.convert(PathBuilder.fromVtoP(path.weighted_path()), (byte) 0xFF,(byte)'M');
				for(int i = 0; i < Abyte.size();i++)
					System.out.println("CMD: "+(char)(Abyte.get(i)[1])+"\n"+Arrays.toString(Abyte.get(i)));
					//dfr.write(Abyte.get(i));
					 /**/
				
				
				
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
