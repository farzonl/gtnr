package com.gtnightrover.visualizer;

public class DoublePoint {

	public double x, y;
	
	public DoublePoint(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("(");
		return sb.append(x).append(", ").append(y).append(")").toString();
	}
}
