package task3;

/**
 * @author Henry Li, Michael Tanel, Ross Vrana-Godwin
 * Task 3 Edge
 * This class represents the edges between nodes. Each edge has a source,
 * destination, and a weight (which we consider to be a "time unit"). 
 */
public class Edge {
	private int source;
	private int dest;
	private int weight;
	
	/**
	 * Constructor for the edge class
	 * @param source Source node.
	 * @param dest Destination node.
	 * @param weight Cost to get from source to destination.
	 */
	public Edge(int source, int dest, int weight) {
		this.source = source;
		this.dest = dest;
		this.weight = weight;
	}

	/**
	 * @return Returns the source node.
	 */
	public int getSource() {
		return source;
	}

	/**
	 * @return Returns the destination node.
	 */
	public int getDest() {
		return dest;
	}

	/**
	 * @return Returns the weight between the source and destination.
	 */
	public int getWeight() {
		return weight;
	}
	
	/**
	 * @return Returns a string representation of the edge.
	 */
	public String toString() {
		String str = source + "---" + dest;
		return str;
	}
	
}
