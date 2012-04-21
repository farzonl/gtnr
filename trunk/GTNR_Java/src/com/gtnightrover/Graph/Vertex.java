package com.gtnightrover.Graph;
/*
 * Written by Farzon Lotfi
 * 
 * Vertex.java
 * */

import java.util.ArrayList;

/**
 * 
 * @author Farzon Lotfi
 * @version 1.0
 * class that encapsulates the data regarding the 
 * Vertex
 *
 */
public class Vertex {

	private String name;
	private ArrayList<Edge> neighborList;
	private Object data;
	private int minDistance;
	private Vertex previous;
	
	public Vertex getPrevious() {
		return previous;
	}



	public void setPrevious(Vertex previous) {
		this.previous = previous;
	}



	public int getMinDistance() {
		return minDistance;
	}



	public void setMinDistance(int minDistance) {
		this.minDistance = minDistance;
	}



	/**
	 * constructor that creates the vertex and its
	 * list of edges
	 * @param name
	 */
	public Vertex(String name) {
		this.name = name;
		this.neighborList = new ArrayList<Edge>();
	}
	

	
	public Object getData() {
		return data;
	}



	public void setData(Object data) {
		this.data = data;
	}



	/**
	 * the niffty way of formating the data to suit my needs
	 * @return String returnString
	 */
	public String toString() {
		String returnString ="";
		returnString += "Vertex: "+name+" Adjacent Vertices: ";
		for(int j = 0; j <neighborList.size();j++) {
			Edge currentEdge = neighborList.get(j);
			returnString += "("+currentEdge.getDestination().getName()+", "
			+currentEdge.getDistance()+")";
		}
		return returnString;
	}




	/**
	 * getter for the name
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * setter for the name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * getter for the neighborList
	 * @return
	 */
	public ArrayList<Edge> getNeighborList() {
		return neighborList;
	}

	/**
	 * specialized contains method, makes sure repeats are not occurring
	 * @return int last location accessed
	 */
	public int contains(ArrayList<Edge> list, Vertex vertex){
		for(int i = 0;i< list.size(); i++) {
			if(list.get(i).getDestination().getName().equals(vertex.getName()))
				return i;
		}

		return -1;
	}

	/**
	 * method that makes it easier to 
	 * add new edges to the neighborList
	 * @return ArrayList<Edge> neighborList;
	 */
	public ArrayList<Edge> addNeighbor(Edge e) {
		if(contains(neighborList,e.getDestination())==-1)
			neighborList.add(e);

		return neighborList;
	}

	/**
	 * setter for the neighborList
	 * @param neighborList
	 */
	public void setNeighborList(ArrayList<Edge> neighborList) {
		this.neighborList = neighborList;
	}

}