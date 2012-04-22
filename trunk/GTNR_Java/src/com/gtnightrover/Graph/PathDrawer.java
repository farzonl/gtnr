package com.gtnightrover.Graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.JPanel;/**
 * 
 */

/**
 * @author farzon
 *
 */
public class PathDrawer extends JPanel {
	
	private Vertex robo_pos, dest_vertex;
	private ArrayList<Vertex> depth_pos;
	private boolean path;
	PathDrawer(Vertex robo_pos, ArrayList<Vertex> depth_pos)
	{
		this.robo_pos = robo_pos;
		this.depth_pos = depth_pos;
		setPreferredSize (new Dimension(800, 800));
	     setBackground (Color.WHITE);
	     
	}
	
	public PathDrawer(Vertex robo_pos, ArrayList<Vertex> wPath, Vertex vertex) {
		// TODO Auto-generated constructor stub
		this(robo_pos,wPath);
		this.dest_vertex = vertex;
		path = true;
		
		
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

		for(int i = 0; i < depth_pos.size()-1;i++)
		{
			draw_line(img,(Point)robo_pos.getData(), 
					(Point)depth_pos.get(i).getData(),randomColor());
			
			
			draw_line(img,(Point)depth_pos.get(i).getData(), 
					(Point)depth_pos.get(i+1).getData(),randomColor());
			
			//System.out.println(depth_pos.get(i).getData().toString());
				
		}
		if(path)
		{
			draw_circle(img,(Point)robo_pos.getData(),Color.RED, 10);
			draw_circle(img,(Point)dest_vertex.getData(),Color.BLUE, 10);
		}
	}
	
	public void draw_circle(Graphics img, Point p1,Color c1, int radius)
	{
		img.setColor(c1);
		img.fillOval(p1.getX()+400,p1.getY()+400 ,radius,radius);
	}
	
	public void  draw_line(Graphics img, Point p1, Point p2, Color c1)
	{
		img.setColor(c1);
		img.drawLine(p1.getX()+400,p1.getY()+400,p2.getX()+400,p2.getY()+400);
	}
	
	public void paintComponent(Graphics img)
	{
	    super.paintComponent(img);
	    expand_edges(img);
	}

}
