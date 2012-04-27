/**
 * 
 */
package com.gtnightrover.Graph;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;

import com.gtnightrover.serial.SerialComm;

/**
 * @author farzon
 *
 */
public class PathBuilder2 {

	public ListenableUndirectedWeightedGraph<Point, DefaultWeightedEdge> lGraph;
	private ArrayList<Point> points;
	private Point robo_pos;
	private int[] depth_arr;
	private int[] gDistance = {0,0};
	private DijkstraShortestPath DP;
	public PathBuilder2(int[] depth_arr)
	{
		this.depth_arr = depth_arr;
		lGraph = new ListenableUndirectedWeightedGraph<Point, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		points = new ArrayList<Point>(depth_arr.length);
		this.robo_pos = construct_point(0,0);
		lGraph.addVertex(robo_pos);
		for(int i = 0; i < depth_arr.length;i++)
		{	
			if(depth_arr[i] > gDistance[1])
			{
				gDistance[1] = depth_arr[i];
				gDistance[0] = i;
			}
			Point newPoint = construct_point(depth_arr[i],i);
			System.out.println("degree: "+i+"\t"+newPoint.toString()+"\tDepth: "+depth_arr[i]);
			points.add(newPoint);
			lGraph.addVertex(newPoint);
			lGraph.addEdge(robo_pos, newPoint);
			lGraph.setEdgeWeight(lGraph.getEdge(robo_pos, points.get(i)), depth_arr[i]);
		}
		
		for(int i = 0; i < depth_arr.length-1;i++)
		{
			
			lGraph.addEdge(points.get(i), points.get(i+1));
			lGraph.setEdgeWeight(lGraph.getEdge(points.get(i), points.get(i+1)), 
					distance(points.get(i),points.get(i+1)));
		}
		this.DP = new DijkstraShortestPath(lGraph,this.robo_pos,points.get(gDistance[0]) ); 
		System.out.println(DP.getPathEdgeList());
		System.out.println(DP.getPath());
		
	}
	
	public  ListenableUndirectedWeightedGraph<Point, DefaultWeightedEdge> getlGraph() {
		return lGraph;
	}

	public void setlGraph(
			 ListenableUndirectedWeightedGraph<Point, DefaultWeightedEdge> lGraph) {
		this.lGraph = lGraph;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}

	public Point getRobo_pos() {
		return robo_pos;
	}

	public void setRobo_pos(Point robo_pos) {
		this.robo_pos = robo_pos;
	}

	public int[] getDepth_arr() {
		return depth_arr;
	}

	public void setDepth_arr(int[] depth_arr) {
		this.depth_arr = depth_arr;
	}

	public int[] getgDistance() {
		return gDistance;
	}

	public void setgDistance(int[] gDistance) {
		this.gDistance = gDistance;
	}

	public int distance(Point p1, Point p2)
	{
		
		return (int) Math.sqrt(Math.pow(p2.getX()-p1.getX(), 2)+Math.pow(p2.getY()-p1.getY(), 2));
	}
	
	public Point construct_point(int depth, int degree)
	{
		double rad = (Math.PI*degree)/180;
		int x = (int) (depth*Math.cos(rad));
		int y = (int) (depth*Math.sin(rad));
		return new Point(x,y);
	}

	public DijkstraShortestPath getDP() {
		return DP;
	}

	public void setDP(DijkstraShortestPath dP) {
		DP = dP;
	}
	
	public static void main(String[] args) 
	{
		PathBuilder2 path = null;
		int[] depth_arr = new int[360];
		 Random rand = new Random(); 
		for(int i = 0; i < depth_arr.length; i++)
		{
			depth_arr[i] =  Math.abs(rand.nextInt())%100;
		}
		//System.out.println(path.getDP().getPath().toString());
		path = new PathBuilder2(depth_arr);
		//System.out.println(path.graph.toString());
		JFrame frame = new JFrame("Graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add( new PathDrawer2(path.getRobo_pos(),path.getPoints()));
		frame.pack(); 
		frame.setVisible(true);
	}
}
