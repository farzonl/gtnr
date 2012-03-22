package com.gtnightrover.Graph;

public class Edge {
	
	private int distance;
	
	private String edgeName;
	
	private Vertex destination;
	
	public Edge(int distance, String edgeName,Vertex destination) {
		this.setDestination(destination);
		this.distance = distance;
		this.edgeName = edgeName;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getEdgeName() {
		return edgeName;
	}

	public void setEdgeName(String edgeName) {
		this.edgeName = edgeName;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(Vertex destination) {
		this.destination = destination;
	}
	
	
	/**
	 * @return the destination
	 */
	public Vertex getDestination() {
		return destination;
	}	
	
}