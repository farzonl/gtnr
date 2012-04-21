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
public class PathDrawer2 extends JPanel {
	
	private Point robo_pos;
	private ArrayList<Point> depth_pos;
	PathDrawer2(Point robo_pos, ArrayList<Point> depth_pos)
	{
		this.robo_pos = robo_pos;
		this.depth_pos = depth_pos;
		setPreferredSize (new Dimension(800, 800));
	     setBackground (Color.WHITE);
	     
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
			draw_line(img,robo_pos, 
					depth_pos.get(i),randomColor());
			
			
			draw_line(img,depth_pos.get(i), 
					depth_pos.get(i+1),randomColor());
			
			//System.out.println(depth_pos.get(i).getData().toString());
			
		}
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
