package com.gtnightrover.Graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import javax.swing.JPanel;

import com.gtnightrover.visualizer.Graph;

public class GraphPanel2 extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int POINT_WIDTH = 5;
	public static final int POINT_HEIGHT = 5;
	
	private Graph graph;
	
	public GraphPanel2(Graph graph) {
		this.graph = graph;
	}
	
	public Color randomColor()
    {
		Random rand = new Random();
		
        return(new Color(rand.nextInt(256), 
                         rand.nextInt(256),
                         rand.nextInt(256)));
    }
	
	public void expand_edges(Graphics img)
	{
		java.util.List<Point> points = graph.getPixelPoints();
		Point robo_pos = new Point(400,300);
		for (int i = 0; i < graph.getPixelPoints().size()-1;i++)
		{
			if(null != points.get(i))
			{
				draw_line(img,robo_pos, 
						points.get(i),randomColor());
			
			
				draw_line(img,points.get(i), 
						points.get(i+1),randomColor());
			
			//System.out.println(depth_pos.get(i).getData().toString());
			}
		}
	}
	
	public void  draw_line(Graphics img, Point p1, Point p2, Color c1)
	{
		img.setColor(c1);
		img.drawLine((int)p1.getX(),(int)p1.getY(),(int)p2.getX(),(int)p2.getY());
	}
	
	@Override
	public void paintComponent(Graphics img)
	{
	    super.paintComponent(img);
	    if (graph != null)
	    	expand_edges(img);

	}
	
	
}