package task3;

public class Node {

	private int source;			// location ID
	private Node previous;	// predecessor location for optimal path to this instance
	private int value;		// B(v) value of this location
	
	public Node(int source) {
		this.source = source;
		value = Integer.MAX_VALUE;
		previous = null;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

	public void addToPath(Node previous, int value) {
		this.previous = previous;
		this.value = value;
	}
	
	public String getPath() {
		if (previous != null) {
			return previous.getPath() + " -> " + source;
		} else {
			return source + "";
		}
	}
	
	public int getSource() {
		return source;
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		String s = source + "";
		return s;
	}

} // end Location class