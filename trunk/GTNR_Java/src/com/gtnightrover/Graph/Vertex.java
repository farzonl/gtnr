package com.gtnightrover.Graph;

import java.util.ArrayList;

/**
 * 
 * @author Farzon Lotfi
 * @version 1.0
 *
 */
public class Vertex {

	private String name;
	private ArrayList<Edge> neighborList;
	private Object data;
	
	/**
	 * 
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
	 * 
	 * @return String
	 */
	public String toString() {
		return "Vertex [name=" + name + ", neighborList=" + neighborList.toString() + "]";
	}




	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Edge> getNeighborList() {
		return neighborList;
	}
	
	public int contains(ArrayList<Edge> list, Vertex vertex){
		  for(int i = 0;i< list.size(); i++) {
		    if(list.get(i).getDestination().getName().equals(vertex.getName()))
		      return i;
		  }
		  
		  return -1;
		 }
	
	public ArrayList<Edge> addNeighbor(Edge e) {
		if(contains(neighborList,e.getDestination())==-1)
			neighborList.add(e);
		
		return neighborList;
	}
	
	/**
	 * 
	 * @param neighborList
	 */
	public void setNeighborList(ArrayList<Edge> neighborList) {
		this.neighborList = neighborList;
	}

	

}