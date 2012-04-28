package com.gtnightrover.visualizer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

public class GraphPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int POINT_WIDTH = 5;
	public static final int POINT_HEIGHT = 5;
	
	private Graph graph;
	
	public GraphPanel(Graph graph) {
		this.graph = graph;
	}
	
	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.drawLine(0, this.getHeight()>>1, this.getWidth(), this.getHeight()>>1);
		g.drawLine(this.getWidth()>>1, 0, this.getWidth()>>1, this.getHeight());
		
		if (graph != null) {
			for (Point p : graph.getPixelPoints())
				g.fillOval(p.x-2, p.y-20, POINT_WIDTH, POINT_HEIGHT);
					g.setColor(Color.red);
			if (graph.start != null) {
				g.fillOval(graph.start.x-2, graph.start.y-20, POINT_WIDTH, POINT_HEIGHT);
				g.fillOval(graph.end.x-2, graph.end.y-20, POINT_WIDTH, POINT_HEIGHT);
				g.drawLine(graph.start.x-1, graph.start.y-19, graph.end.x-1, graph.end.y-19);
			}
			g.setColor(Color.black);
		}
	  }
}
