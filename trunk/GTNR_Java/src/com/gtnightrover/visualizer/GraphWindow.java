package com.gtnightrover.visualizer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class GraphWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GraphWindow(int width, int height, Graph g) {
		setTitle("My Empty Frame");
		setSize(width, height); // default size is 0,0
		setLocation(10, 200); // default is 0,0 (top left corner)
		getContentPane().add(new GraphPanel(g));
		addWindowListener(new WindowAdapter() {
		  	public void windowClosing(WindowEvent e) {
			   System.exit(0);
		  	}
		} );
	}

	public static void main(String[] args) {
		Graph g = new Graph(800,600,-20000,-20000,20000,20000);
		for (int i=0;i<360;i++)
			g.addPoint(g.toPixel(i, (Math.random() * 40000)-20000));
		GraphWindow f = new GraphWindow(800, 600, g);
		f.setVisible(true);
	}
}
