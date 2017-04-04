package edu.tamu.algo;

public class Node implements Comparable<Node> {
	private int id;
	private int weight;
	
	public Node(int id, int weight) {
		super();
		this.id = id;
		this.weight = weight;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}

	@Override
	public int compareTo(Node node) {
		// TODO Auto-generated method stub
		int nodeWeight = node.getWeight();
		if (this.weight == nodeWeight)
			return 0;
		else if (this.weight > nodeWeight) 
			return 1;
		else
			return -1;
	}
}
