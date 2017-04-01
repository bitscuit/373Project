package task3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Henry Li, Michael Tanel, Ross Vrana-Godwin
 * Task 3 Dijkstra
 * This class implements Dijkstra's Algorithm for finding the shortest path
 * in the network from source node to destination node.
 */
public class Dijkstra {
	
	public static String nodeFile = "nodes.txt";
	public static String edgeFile = "edges.txt";
	
	public static List<Node> nodes = new ArrayList<Node>();
	public static List<Edge> edges = new ArrayList<Edge>();
	
	public static void main(String[] args) {
		
		// read file to get list of nodes and edges, i.e. the graph
		String nodesInFile = readFile(nodeFile);
		getAllNodes(nodesInFile);
		String edgesInFile = readFile(edgeFile);
		getAllEdges(edgesInFile);
		
		// get source and destination fomr user
		Scanner user = new Scanner(System.in);
		print("Enter source: ");
		String source = get(user);
		print("Enter destination: ");
		String dest = get(user);
		Node sourceNode = null;
		Node destNode = null;
		
		for (Node n : nodes) {
			if (n.getSource() == Integer.valueOf(source)) {
				sourceNode = n;
				sourceNode.setValue(0);
			}
			if (n.getSource() == Integer.valueOf(dest)) {
				destNode = n;
			}
		}
		callDijkstra(sourceNode, destNode);
			
	}
	
	/**
	 * Reads from the file to get all of the nodes.
	 * @param s File contents.
	 */
	public static void getAllNodes(String s) {
		String[] lines = s.split("\\r?\\n"); // split by line break
		for (String line : lines) {
			String id = line.trim();
			nodes.add(new Node(Integer.valueOf(id)));
		}
	}
	
	/**
	 * Reads from the file to get all of the edges.
	 * @param s File contents.
	 */
	public static void getAllEdges(String s) {
		String[] lines = s.split("\\r?\\n");
		String[] cols;
		for (String line : lines) {
			cols = line.split("\\s+"); // split by spaces/tabs
			int source = Integer.valueOf(cols[0]);
			int dest = Integer.valueOf(cols[1]);
			int weight = Integer.valueOf(cols[2]);
			edges.add(new Edge(source, dest, weight));
		}
	}
	
	/**
	 * Implementation of Dijkstra's Algorithm.
	 */
	public static void callDijkstra(Node source, Node dest) {
		
		// reached list contains nodes whose shortest paths have been computed
		ArrayList<Node> R = new ArrayList<Node>(); // reached, i.e. found shortest path
		
		// candidate list contains discovered nodes whose shortest paths have
		// not been computed
		ArrayList<Node> C = new ArrayList<Node>(); // nodes to be processed
		
		// initially add source node to candidate list
		C.add(source);
		Node X; // current node
		
		// while there are still nodes in the candidate list
		// algorithm not done
		while (!(C.isEmpty())) {
			
			// retrieve from the candidate list the node which has the lowest
			// value, i.e., the node which has the shortest path length from the source
			// this node will become the current node
			X = C.get(0);
			for (Node n : C) {
				if (n.getValue() < X.getValue())
					X = n;
			}
			
			R.add(X); // add current node to reached list because shortest path has been found
			C.remove(X); // remove from candidate list since shortest path has been found for node
			
			// logic to find the shortest path to the neighbours from current node
			// Node y will essentially become the neighbour node if the conditions are met
			for (Node y : nodes) {
				
				// condition to exclude the processing of nodes in the
				// reached list
				if (R.contains(y))
					continue;
				
				// loops through the list of edges to find those that connect node X with node Y
				// i.e. finds the neighbours of X
				for (Edge edge : edges) {
					if ((edge.getSource() != X.getSource()) || (edge.getDest() != y.getSource())) {
						continue;
					}
					if (X.getValue() + edge.getWeight() < y.getValue()) {
						// check if this neighbour node has been discovered yet
						// if not, add it to the candidate list for later processing
						if (y.getValue() == Integer.MAX_VALUE) {
							C.add(y);
						}
						y.addToPath(X, X.getValue() + edge.getWeight());
					}
				} 
			}
		}
		
		// If arrival time to the destination location is infinity, it cannot be reached
		if (dest.getValue() == Integer.MAX_VALUE) {
			print("There is no possible path from " + source.getSource() + " to " + dest.getSource());
		} else {
			print("Path: " + dest.getPath());
			print("Shortest path time: " + dest.getValue());
		} // end if-else statement
		
	} // end dijkstras method
	
	/**
	 * Parses the inputted Filename s.
	 * @param s File name.
	 * @return Contents of file.
	 */
	private static String readFile(String s) {
		String contents = "";
		try (BufferedReader reader =
				new BufferedReader(new FileReader(s))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				contents += line + System.lineSeparator();
			}
		} catch (IOException e) {
			System.err.println("Could not read file");
		}
		return contents;
	}
	
	private static void print(String s) {
		System.out.println(s);
	}
	
	private static String get(Scanner in) {
		String input = in.nextLine();
		return input;
	}
	
}