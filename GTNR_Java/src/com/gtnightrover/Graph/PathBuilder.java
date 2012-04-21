package com.gtnightrover.Graph;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;



/**
 * 
 */

/**
 * @author farzon
 *
 */
public class PathBuilder
{

	private int[] depth_arr;
	private Vertex robo_pos;
	private ArrayList<Vertex> depth_pos;
	private ArrayList<Point> points;
	private AdjacencyListGraph graph;
	private int[] gDistance = {0,0,0};
	public PathBuilder(int[] depth_arr)
	{
		this.depth_arr = depth_arr;
		
		//initialize
		graph = new AdjacencyListGraph();
		points = new ArrayList<Point>(depth_arr.length);
		depth_pos = new ArrayList<Vertex>(depth_arr.length);
		robo_pos = new Vertex(null);
		robo_pos.setData(construct_point(0,0));
		robo_pos.setName(robo_pos.getData().toString());
		
		//set data
		for(int i = 0; i < depth_arr.length;i++)
		{	
			//TODO make this into logic with weight, right new path picking algorithm that takes this into account.
			if(depth_arr[i] > gDistance[1])
			{
				gDistance[1] = depth_arr[i];
				gDistance[0] = i;
				if(i>0 && i <= 90) gDistance[2] = depth_arr[i]+i;
				else if(i>90 && i <= 180) gDistance[2] = depth_arr[i]+90-i;
				else
					gDistance[2] = 0;
			}
			
			Point newPoint = construct_point(depth_arr[i],i);
			System.out.println("degree: "+i+"\t"+newPoint.toString());
			points.add(newPoint);
			depth_pos.add(construct_vertex(newPoint,depth_arr[i]));
			depth_pos.get(i).setData(points.get(i));
		}
		
		//set graph
		for(int i = 0; i < depth_pos.size()-1;i++)
		{
			graph.addEdge(robo_pos, depth_pos.get(i), depth_arr[i]);
			graph.addEdge(depth_pos.get(i),depth_pos.get(i+1), 1);
		}
		graph.computePaths(robo_pos);
	}
	
	public Point construct_point(int depth, int degree)
	{
		double rad = (Math.PI*degree)/180;
		int x = (int) (depth*Math.cos(rad));
		int y = (int) (depth*Math.sin(rad));
		return new Point(x,y);
	}
	
	public Vertex construct_vertex(Point point, int depth)
	{
		return new Vertex(point.toString()+"Depth: "+depth);
	}
	
	
	
	public AdjacencyListGraph getGraph() {
		return graph;
	}

	public void setGraph(AdjacencyListGraph graph) {
		this.graph = graph;
	}

	public int[] getDepth_arr() {
		return depth_arr;
	}

	public void setDepth_arr(int[] depth_arr) {
		this.depth_arr = depth_arr;
	}

	public Vertex getRobo_pos() {
		return robo_pos;
	}

	public void setRobo_pos(Vertex robo_pos) {
		this.robo_pos = robo_pos;
	}

	public ArrayList<Vertex> getDepth_pos() {
		return depth_pos;
	}

	public void setDepth_pos(ArrayList<Vertex> depth_pos) {
		this.depth_pos = depth_pos;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	public static void main(String[] args) 
	{
		PathBuilder path = null;
		int[] depth_arr = new int[360];
		 Random rand = new Random(); 
		for(int i = 0; i < depth_arr.length; i++)
		{
			depth_arr[i] =  Math.abs(rand.nextInt())%100;
		}
		//System.out.println(path.getDP().getPath().toString());
		path = new PathBuilder(depth_arr);
		//System.out.println(path.graph.toString());
		JFrame frame = new JFrame("Graph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add( new PathDrawer(path.getRobo_pos(),path.getDepth_pos()));
		frame.pack(); 
		frame.setVisible(true);
		
		
		
		//ArrayList<Vertex> dfs = path.getGraph().dfs(path.getRobo_pos(),path.getDepth_pos().get(path.getgDistance()[0]));
		ArrayList<Vertex>   spanning_tree = (ArrayList<Vertex>) path.getGraph().getShortestPathTo(path.getDepth_pos().get(path.getgDistance()[0]));
		
		JFrame f = new JFrame("Path");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add( new PathDrawer(path.getRobo_pos(),spanning_tree));
		f.pack(); 
		f.setVisible(true);
	}

	public int[] getgDistance() {
		return gDistance;
	}

	public void setgDistance(int[] gDistance) {
		this.gDistance = gDistance;
	}

	
}
