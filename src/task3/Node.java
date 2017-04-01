package task3;

/**
 * @author Henry Li, Michael Tanel, Ross Vrana-Godwin
 * Task 3 Node
 * This class represents the nodes in the network. Each nodes has a source (id),
 * previous node, and value (cost to get to this node from the source).
 */
public class Node {

	private int source;			// location ID
	private Node previous;		// predecessor location for optimal path to this instance
	private int value;			// B(v) value of this location
	
	/**
	 * Constructor for the Node class.
	 * @param source Id for the node.
	 */
	public Node(int source) {
		this.source = source;
		value = Integer.MAX_VALUE;
		previous = null;
	}
	
	/**
	 * Sets the nodes value.
	 * @param value B(v) value of this node.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Adds a node to the shortest path.
	 * @param previous Previous node.
	 * @param value B(v) value of this node.
	 */
	public void addToPath(Node previous, int value) {
		this.previous = previous;
		this.value = value;
	}
	
	/**
	 * @return Returns the shortest path to the present node.
	 */
	public String getPath() {
		if (previous != null) {
			return previous.getPath() + " -> " + source;
		} else {
			return source + "";
		}
	}
	
	/**
	 * @return Returns the source (id) of the node.
	 */
	public int getSource() {
		return source;
	}
	
	/**
	 * @return Returns the B(v) value of this node.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * @return Returns a string representation of the node.
	 */
	@Override
	public String toString() {
		String s = source + "";
		return s;
	}

}