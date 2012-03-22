package com.gtnightrover.Graph;
//import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;


//contain data and methods necessary to represent a graph
// method to easily populate the graph with correct vertices & edges
// methods to add vertices and set adjacency between vertices
// needs toString method

public class AdjacencyListGraph {
 static int integerEdge = 0;
 static int integerVertex = 0;
 
 private ArrayList<Vertex> adjacencyList;
 //private HashMap<String,Vertex> adjacencyList;
 
 public AdjacencyListGraph() {
  //adjacencyList = new HashMap<String,Vertex>();
  adjacencyList = new ArrayList<Vertex>();
  }
 
 public String vertexName(){
  String vertexName = "vertex"+integerVertex;
  integerVertex++;
  return vertexName;
 }
 
 public String edgeName(){
  String edgeName = "edge"+integerEdge;
  integerEdge++;
  return edgeName;
 }
 
 public int contains(ArrayList<Vertex> list, Vertex vertex){
  for(int i = 0;i< list.size(); i++) {
    if(list.get(i).getName().equals(vertex.getName()))
      return i;
  }
  
  return -1;
 }
 
 public int contains(Vertex vertex){
	 
	 return contains(adjacencyList, vertex);
 }
 
 public boolean addVertex(Vertex vertex) {
  if(contains(adjacencyList, vertex) == -1) {
	  adjacencyList.add(vertex);
	  return true;
  }
  return false;
 }
 
 public Vertex getVertexAt(int index) {
	 return adjacencyList.get(index);
 }
 
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
	 
	 origin.addNeighbor(originEdge);
	 destination.addNeighbor(destinationEdge);
	 return true;
 }
 
 public String toString() {
  String returnString ="";
  for(int i = 0;i< adjacencyList.size(); i++) {
   Vertex currentVertex = adjacencyList.get(i);
   returnString += "Vertex: "+currentVertex.getName()+" Vertices: ";
   for(int j = 0; j <currentVertex.getNeighborList().size();j++) {
    Edge currentEdge = currentVertex.getNeighborList().get(j);
    returnString += "("+currentEdge.getDestination().getName()+", "
    +currentEdge.getDistance()+")";
   }
   returnString += "\n";
  }
  return returnString;
 }
 
 public boolean dfs(Vertex origin, Vertex destination) {
	  ArrayList<Vertex> visitedList = new ArrayList<Vertex>();
	  return dfsHelper(origin, destination, visitedList);
 }
 
 public boolean dfsHelper(Vertex origin, Vertex destination, ArrayList<Vertex> visitedList){
	 if(visitedList.contains(destination))
		 return true;
	
	 visitedList.add(origin);
	 System.out.println(origin.getName());
	 
	 Iterator<Edge> iterator = origin.getNeighborList().iterator();
	 
	 
	 while(iterator.hasNext()){
		 Edge current = iterator.next();
		 if(!visitedList.contains(current.getDestination()))
			 dfsHelper(current.getDestination(),destination, visitedList);
		 }
	 
	 return false;
	 }
	 

 
 
 public static void main(String[] args) {
  AdjacencyListGraph test = new AdjacencyListGraph();
  
  Vertex a = new Vertex(test.vertexName());
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
  
  test.addEdge(df, b, 5);
  
  System.out.println(test.toString());
  
  test.dfs(b,f);
  
  
  
 }
 
 
 
}