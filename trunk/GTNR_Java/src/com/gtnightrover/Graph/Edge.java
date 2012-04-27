package com.gtnightrover.Graph;

/*
 * Written by Farzon Lotfi
 * 
 * Edge.java
 * */

/**
 * @author Farzon Lotfi
 * @version 1.0
 *a class that lets you encapsulate the data regarding the edge
 */
public class Edge implements Comparable{

	private int distance;

	private String edgeName;

	private Vertex destination;
	
	//private Vertex origin;

	/**
	 *constructor that encapsulates the data regarding the edge
	 *@param int distance - the length of the edge
	 *@param String edgeName - the name of the edge
	 *@param Vertex destination - the destination of the edge
	 */
	//public Edge(int distance, String edgeName,Vertex origin, Vertex destination) {
	public Edge(int distance, String edgeName, Vertex destination) {
		this.setDestination(destination);
		//this.setOrigin(origin);
		this.distance = distance;
		this.edgeName = edgeName;
	}

	/**
	 *a getter for the distance
	 *@return int distance - length of the edge
	 */
	public int getDistance() {
		return distance;
	}


	/**
	 *a setter for the distance
	 *@param int distance - length of the edge
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 *a getter for the edgeName
	 *@return String edgeName - the name of the edge
	 */
	public String getEdgeName() {
		return edgeName;
	}

	/**
	 *a setter for the edgeName
	 *@param String edgeName - the name of the edge
	 */
	public void setEdgeName(String edgeName) {
		this.edgeName = edgeName;
	}

	/**
	 *a setter for the destination
	 *@param Vertex destination - vertex the edge is headed towards
	 */
	public void setDestination(Vertex destination) {
		this.destination = destination;
	}


	/**
	 *a getter for the destination
	 *@return Vertex destination - vertex the edge is headed towards
	 */
	public Vertex getDestination() {
		return destination;
	}	

	/**
	 *the method that makes the edges comparable so that they can be used
	 *in the priorityQueue
	 *@param Object arg0- the edge you want compared
	 *@return int - lets the priorityQueue know where to put the value
	 */
	@Override
	public int compareTo(Object arg0) {
		Edge val =(Edge)(arg0);
		return this.getDistance() - val.getDistance();
	}

	/**
	 * @param origin the origin to set
	 */
/*	public void setOrigin(Vertex origin) {
		this.origin = origin;
	}*/

	/**
	 * @return the origin
	 */
/*	public Vertex getOrigin() {
		return origin;
	}*/


}