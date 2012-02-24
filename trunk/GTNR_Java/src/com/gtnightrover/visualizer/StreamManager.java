package com.gtnightrover.visualizer;

public class StreamManager extends Thread {

	private Graph g;
	private GraphWindow gw;
	private PointsStream ps;
	
	public StreamManager(Graph g, GraphWindow gw, PointsStream ps) {
		super();
		this.g = g;
		this.gw = gw;
		this.ps = ps;
	}
	
	@Override
	public void run() {
		int count = 0;
		while (ps.hasPoints()) {
			g.clear();
			g.addAll(ps.getPoints());
			gw.repaint();
			System.out.println("Update: " + (count++));
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.exit(0);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Graph g = new Graph(800,600,-20000,-20000,20000,20000);
		GraphWindow f = new GraphWindow(800, 600, g);
		f.setVisible(true);
		new StreamManager(g, f, new StreamGen()).start();
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
