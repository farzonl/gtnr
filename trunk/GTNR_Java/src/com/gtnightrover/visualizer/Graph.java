package com.gtnightrover.visualizer;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Graph {

	private boolean FILE_DUMP = false;
	private String DUMP_DIR = "/home/david/Desktop/Lidar_Dump/";
	
	private static int width, height;
	private static int minX, minY, maxX, maxY;
	private List<Point> points;
	public Point start, end;
	
	public Graph(int width, int height, int minX, int minY, int maxX, int maxY) {
		super();
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException("Width and Height must be positive i.e. * > 0.");
		this.width = width;
		this.height = height;
		if (maxX <= minX)
			throw new IllegalArgumentException("maxX must be greater than minX.");
		this.minX = minX;
		this.maxX = maxX;
		if (maxY <= minY)
			throw new IllegalArgumentException("maxY must be greater than minY.");
		this.minY = minY;
		this.maxY = maxY;
		points = new LinkedList<Point>();
	}
	
	public boolean checkRange(Point p) {
		if (p == null)
			return false;
		if (p.x < minX || p.x >= maxX)
			p.x = maxX;
		if (p.y < minY || p.y >= maxY)
			p.y = maxY;
		return true;
	}
	
	public boolean checkRange(DoublePoint p) {
		if (p == null)
			return false;
		if (p.x < 0 || p.x >= width)
			p.x = width;
		if (p.y < 0 || p.y >= height)
			p.y = height;
		return true;
	}
	
	public Point toPixel(DoublePoint p) {
		if (p == null)
			return null;
		double x = p.x;
		double y = p.y;
		x = x / (maxX - minX);
		y = y / (maxY - minY);
		return new Point((int)(x*width)+width>>1, (int)(y*height)+height>>1);
	}
	
	/**
	 * Expecting degrees in radians!!
	 * @param degree
	 * @param distance
	 * @return
	 */
	public static Point toPixel(double degree, double distance) {
		if (distance < 0)
			return null;
		double y = -distance * Math.cos(degree);
		double x = -distance * Math.sin(degree);
		x = x / (maxX);
		y = y / (maxY);
		Point p = new Point((int)(x*width)+(width>>1), (int)(y*height)+(height>>1));
		return p;
	}
	
	public DoublePoint toEuclidian(Point p) {
		if (p == null)
			return null;
		if(!checkRange(p))
			return null;
		double x = p.x - (width>>1);
		double y = p.y - (height>>1);
		x = x / width;
		y = y / height;
		return new DoublePoint(x*(maxX - minX), y*(maxY - minY));
	}
	
	/**
	 * Expecting degrees in radians!!
	 * @param degree
	 * @param distance
	 * @return
	 */
	public DoublePoint toEuclidian(double degree, double distance) {
		return toEuclidian(toPixel(degree, distance));
	}
	
	public boolean addPoint(DoublePoint p) {
		return addPoint(toPixel(p));
	}
	
	public boolean addPoint(Point p) {
		if (p == null)
			return false;
		if (checkRange(p))
			points.add(p);
		return true;
	}
	
	public void addAll(List<Point> pts) {
		if (pts == null)
			return;
		for (Point p : pts)
			if (checkRange(p))
				points.add(p);
	}
	
	/**
	 * Expecting array to be in the form
	 *    { {deg, dist}, {deg, dist}, ... }
	 * Where deg is in radians!!
	 * @param pts
	 */
	public void addAll(double[][] pts) {
		if (pts == null)
			return;
		for (int i=0;i<pts.length;i++)
			if (pts[i] != null && pts[i].length == 2)
				points.add(toPixel(pts[i][0], pts[i][1]));
	}
	
	public void addAll(int[] pts) {
		BufferedWriter bw = null;
		if (FILE_DUMP)
			try {
				bw = new BufferedWriter(new FileWriter(new File(DUMP_DIR+System.currentTimeMillis()+".txt")));
			} catch (IOException e) { e.printStackTrace(); }
		if (pts == null)
			return;
		for (int i=0;i<pts.length;i++) {
			double rad = (double)i*2*Math.PI / 360;
			points.add(toPixel(rad, pts[i]));
			if (bw != null)
				try {
					bw.append(pts[i]+"\n");
				} catch (IOException e) { }
//			System.out.println(i + "=" + rad + " --> " + pts[i]);
		}
		if (bw != null)
			try {
				bw.close();
			} catch (IOException e) { }
	}
	
	public void clear() {
		this.points.clear();
	}
	
	public List<Point> getPixelPoints() {
		List<Point> rtn = new LinkedList<Point>();
		for (Point p : points)
			if (p != null)
				rtn.add(new Point(p.x, p.y));
		return rtn;
	}
	
	public List<DoublePoint> getEuclidianPoints() {
		List<DoublePoint> rtn = new LinkedList<DoublePoint>();
		for (Point p : points)
			rtn.add(toEuclidian(p));
		return rtn;
	}
}
