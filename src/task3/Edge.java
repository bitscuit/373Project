package task3;

public class Edge {
	private int source;
	private int dest;
	private int weight;
	
	public Edge(int source, int dest, int weight) {
		this.source = source;
		this.dest = dest;
		this.weight = weight;
	}

	public int getSource() {
		return source;
	}

	public int getDest() {
		return dest;
	}
	
	public int getWeight() {
		return weight;
	}
	
	public String toString() {
		String str = source + "---" + dest;
		return str;
	}
	
}
