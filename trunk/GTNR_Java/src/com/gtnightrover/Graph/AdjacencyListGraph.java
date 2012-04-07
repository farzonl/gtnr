package com.gtnightrover.Graph;

/*
 * Written by Farzon Lotfi
 * 
 * AdjacencyListGraph.java
 * 
 * */

//import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;



//contain data and methods necessary to represent a graph
// method to easily populate the graph with correct vertices & edges
// methods to add vertices and set adjacency between vertices
// needs toString method

/**
 * @author Farzon Lotfi
 * @version 1.0
 * class that creates a graph and also allows you to perform
 * prims algorithm and dfs on the graph
 */
public class AdjacencyListGraph {
	static int integerEdge = 0;
	static int integerVertex = 0;

	private ArrayList<Vertex> adjacencyList;
	//private HashMap<String,Vertex> adjacencyList;

	/**
	 *constructor that creates the arraylist of vertex you will
	 *use to store your data
	 */
	public AdjacencyListGraph() {
		//adjacencyList = new HashMap<String,Vertex>();
		adjacencyList = new ArrayList<Vertex>();
	}

	/**
	 *getter for the adjacencyList
	 *@return ArrayList<Vertex> - the adjacency list
	 */
	public ArrayList<Vertex> getAdjacencyList() {
		return adjacencyList;
	}

	/**
	 * setter for the adjacency list, allows to to reset the adjacecny list
	 * to whatever i want
	 * @param neighborList
	 */
	public void setAdjacencyList(ArrayList<Vertex> adjacencyList) {
		this.adjacencyList = adjacencyList;
	}


	/**
	 * creates an incrementing name for me to use
	 * @return String vertexName
	 */
	public String vertexName(){
		String vertexName = "vertex"+integerVertex;
		integerVertex++;
		return vertexName;
	}
	/**
	 * creates an incrementing name for me to use
	 * @return String edgeName
	 */
	public String edgeName(){
		String edgeName = "edge"+integerEdge;
		integerEdge++;
		return edgeName;
	}

	/**
	 * custom contains method used to prevent duplicate vertexes
	 * @param ArrayList<Vertex> list - adjacency list to compare to
	 * @param Vertex vertex - vertex you want checked
	 * @return int - location of the value if it exists
	 */
	public int contains(ArrayList<Vertex> list, Vertex vertex){
		for(int i = 0;i< list.size(); i++) {
			if(list.get(i).getName().equals(vertex.getName()))
				return i;
		}

		return -1;
	}

	/**
	 * custom contains method used to prevent duplicate vertexes
	 * use when you know you will use the default adjacency list
	 * @param Vertex vertex - vertex you want checked
	 * @return int - location of the value if it existsn 
	 */
	public int contains(Vertex vertex){

		return contains(adjacencyList, vertex);
	}

	/**
	 * method that lets you add vertexes 
	 * @param Vertex vertex- vertex you want added
	 * @return boolean -if succesful returns true if it fails it means the vertex is
	 * a duplicate
	 */
	public boolean addVertex(Vertex vertex) {
		if(contains(adjacencyList, vertex) == -1) {
			adjacencyList.add(vertex);
			return true;
		}
		return false;
	}

	/**
	 * specialized getMethod lets you index into the adjacency list
	 * @param int index - the index of the vertex you want
	 * @return Vertex the vertex you want
	 */
	public Vertex getVertexAt(int index) {
		return adjacencyList.get(index);
	}

	/**
	 * adds the edges that will be traversed to get from one vertex to another
	 * @param Vertex origin - the first vertex
	 * @param Vertex destination - the vertex you are connecting
	 * @param int distance - the distance between the vertexes
	 * @return boolean - returns true if successful (will always be successful)
	 */
	public boolean addEdge(Vertex origin, Vertex destination, int distance) {
		int originIndex = contains(adjacencyList, origin);
		if(originIndex == -1)
			addVertex(origin);
		originIndex = contains(adjacencyList, origin);

		int destinationIndex = contains(adjacencyList, destination);
		if(destinationIndex == -1)
			addVertex(destination);
		destinationIndex = contains(adjacencyList, destination);

		origin = adjacencyList.get(originIndex);
		destination = adjacencyList.get(destinationIndex);
		String edgeName = edgeName();
		Edge originEdge = new Edge(distance,edgeName,destination);
		Edge destinationEdge = new Edge(distance,edgeName,origin);
		//Edge edge = new Edge(distance,edgeName,origin, destination);
		origin.addNeighbor(originEdge);
		destination.addNeighbor(destinationEdge);
		//origin.addNeighbor(edge);
		//destination.addNeighbor(edge);
		return true;
	}

	/**
	 * specialized toString() method you guys asked for
	 * @return returnString - the correctly formated data
	 */
	public String toString() {
		String returnString ="";
		for(int i = 0;i< adjacencyList.size(); i++) {
			Vertex currentVertex = adjacencyList.get(i);
			returnString += "Vertex: "+currentVertex.getName()+" Adjacent Vertices: ";
			for(int j = 0; j <currentVertex.getNeighborList().size();j++) {
				Edge currentEdge = currentVertex.getNeighborList().get(j);
				returnString += "("+currentEdge.getDestination().getName()+", "
				+currentEdge.getDistance()+")";
			}
			returnString += "\n";
		}
		return returnString;
	}

	/**
	 * dfs traverses the graph using depth first search
	 * @param Vertex origin - the start vertex of the traversal
	 * @param Vertex destination - the end vertex of the traversal
	 * @return boolean - returns true if successfully completed
	 */
	public boolean dfs(Vertex origin, Vertex destination) {
		ArrayList<Vertex> visitedList = new ArrayList<Vertex>();
		return dfsHelper(origin, destination, visitedList);
	}

	/**
	 * dfs traverses the graph using depth first search
	 * recursive version of the algorithm that adds visited nodes to an 
	 * arraylist
	 * @param Vertex origin - start vertex
	 * @param Vertex destination end vertex
	 * @param ArrayList<Vertex> visitedList - keeps track of visited nodes so they are not revisited
	 * @return boolean - returns true if successfully completed
	 */
	public boolean dfsHelper(Vertex origin, Vertex destination, ArrayList<Vertex> visitedList){
		if(visitedList.contains(destination))
			return true;

		visitedList.add(origin);
		System.out.println("Visited "+origin.toString());

		Iterator<Edge> iterator = origin.getNeighborList().iterator();


		while(iterator.hasNext()){
			Edge current = iterator.next();
			if(!visitedList.contains(current.getDestination()))
				dfsHelper(current.getDestination(),destination, visitedList);
		}

		return false;
	}

	/**
	 * a specialized contains method to compare the edges as all my edges
	 * are new objects but have the same name
	 * @param ArrayList<Edge> list- the list you want the edge compared against
	 * @param Edge compareEdge -the edge you want compared
	 * @return boolean - to let prim() know wheter to remove or not
	 */
	public int containsNeigibor(ArrayList<Edge> list, Edge compareEdge){
		for(int i = 0;i< list.size(); i++) {
			if(list.get(i).getEdgeName().equals(compareEdge.getEdgeName()))
				return i;
		}

		return -1;
	}


	/**
	 * prim() - calls prim() helper to traverse the edges in the shortest path
	 * then fixes the nehibor list by using the remaining edges in the priority queue
	 * @return AdjacencyListGraph - the minimum spanning tree
	 */
	public AdjacencyListGraph prim() {
		AdjacencyListGraph spanningTree = new AdjacencyListGraph();


		PriorityQueue<Edge> list = new PriorityQueue<Edge>();
		spanningTree.addVertex(adjacencyList.get(0));
		spanningTree = primHelper(adjacencyList.get(0),spanningTree,list);

		for(int i = 0;i< adjacencyList.size(); i++) {
			ArrayList<Edge> fixEdge = spanningTree.getAdjacencyList().get(i).getNeighborList();
			Iterator<Edge> iterator = list.iterator();
			//Iterator<Edge> fixIter = fixEdge.iterator();
			while(iterator.hasNext()){
				Edge current = iterator.next();
				//Edge fix = fixIter.next();
				//if(fix.getEdgeName().equals(current.getEdgeName())) {
				int removeIndex = containsNeigibor(fixEdge, current);
				if(removeIndex != -1){
				//if(fixEdge.contains(current)) {
					fixEdge.remove(removeIndex);
				}

			}

		}

		System.out.println(spanningTree.toString());

		return spanningTree;
	}


	/**
	 * primHelper()- takes in the starting position always the very first vertex
	 * then traverses the adjacencylist and adds values into the spanningTree using
	 * the priorityQueue
	 * @param Vertex - origin
	 * @param AdjacencyListGraph spanningTree
	 * @param PriorityQueue<Edge> list
	 * @return AdjacencyListGraph - the minimum spanning tree
	 */
	public AdjacencyListGraph primHelper(Vertex origin, AdjacencyListGraph spanningTree,
			PriorityQueue<Edge> list) {

		if(logicChecker(spanningTree))
			return spanningTree;


		Iterator<Edge> iterator = origin.getNeighborList().iterator();

		while(iterator.hasNext()){
			Edge current = iterator.next();

			if(!spanningTree.getAdjacencyList().contains(current.getDestination())) {
				list.add(current);
			}
		}
		//Edge nextEdge = list.remove();
		//Vertex nextVertex = nextEdge.getDestination();
		Vertex nextVertex = list.remove().getDestination();
		//origin.setNeighborList(new ArrayList<Edge>());
		//origin.addNeighbor(nextEdge);
		spanningTree.addVertex(nextVertex);
		return primHelper(nextVertex,spanningTree,list); 
	}

	/**
	 * a logic checking step for the base case, makes sure all the vertexes in the 
	 * AdjacencyList appear in the spanningTree
	 * @param AdjacencyListGraph spanningTree
	 * @return boolean retVal - to let the primHelper algorithm know when to stop
	 */
	public boolean logicChecker(AdjacencyListGraph spanningTree){
		boolean retVal = true;
		for(int i = 0;i< adjacencyList.size(); i++) {
			if(!spanningTree.getAdjacencyList().contains(adjacencyList.get(i)))
				return false;
			else
				retVal = true;
		}
		return retVal;
	}





	/**
	 * the main method used hear to instantiate the AdjacencyListGraph class 
	 * and test it
	 * @param String[] args - paramter i would use if i was taking values in from the
	 * command line
	 */
	public static void main(String[] args) {
		AdjacencyListGraph test = new AdjacencyListGraph();

/*		Vertex a = new Vertex(test.vertexName());
		Vertex b = new Vertex(test.vertexName());
		Vertex c = new Vertex(test.vertexName());
		Vertex d = new Vertex(test.vertexName());
		Vertex e = new Vertex(test.vertexName());
		Vertex f = new Vertex(test.vertexName());

		Vertex df = new Vertex("vertex0");
		test.addEdge(a, b, 5);
		test.addEdge(a, c, 15);
		test.addEdge(a, d, 10);
		test.addEdge(b, d, 5);
		test.addEdge(c, e, 15);
		test.addEdge(f, d, 10);
		test.addEdge(df, b, 5);*/
		
		Vertex a = new Vertex("a");
		Vertex b = new Vertex("b");
		Vertex c = new Vertex("c");
		Vertex d = new Vertex("d");
	

		
		test.addEdge(a, d, 2);
		test.addEdge(a, b, 5);
		test.addEdge(b, c, 3);
		test.addEdge(d, c, 1);
		test.addEdge(d, c, 1);
		test.addEdge(b, d, 1);
		

		
		System.out.println(test.toString());

		test.dfs(b,c);

		System.out.println();
				
		test.prim();



	}



}