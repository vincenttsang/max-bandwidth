package edu.tamu.algo;

import java.time.chrono.MinguoChronology;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Graph {
	private int[][] matrix;
	
	public Graph(int[][] matrix){
		this.matrix = matrix;
	}
	
	public static void main(String[] args){
		Graph randG = Graph.randomGraph(6, 3);
		randG.printAdj();
		randG.printMat();
		
		ArrayList<Integer> path = randG.dijkstra(0, 5);
		printReversePath(path);
	}
	
	public void printAdj(){
		for (int i = 0; i < matrix.length; i++)
		{
			System.out.print("Node " + i + ": ");
			for (int j = 0; j < matrix.length; j++)
			{
				if (matrix[i][j] != -1)
				{
					System.out.print(j + " ");
				}
			}
			System.out.println();
		}
	}
	
	public void printMat(){
		for (int i = 0; i < matrix.length; i++)
		{
			System.out.print("Node " + i + ": ");
			for (int j = 0; j < matrix.length; j++)
			{
				if (matrix[i][j] != -1)
				{
					System.out.print(matrix[i][j] + " ");
				}
				else
				{
					System.out.print(-1 + " ");;
				}
			}
			System.out.println();
		}
	}
	
	public static Graph randomGraph(int nodeCount, int degree){
		int[][] matrix = new int[nodeCount][nodeCount];
		
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		for (int i = 0; i < nodeCount; i++) {
			nodes.add(i);
			for (int j = 0; j < nodeCount; j++) {
				matrix[i][j] = -1;
			}
		}
		
		Collections.shuffle(nodes);
		Random random = new Random(System.currentTimeMillis());
		
		for (int i = 0; i < nodeCount; i ++){
			int range = degree / 2;
			for (int j = i - range; j <= i + range; j++)
			{
				int index = -1; // used to go to the end of array and locate the corresponding nodes
				if (i == j)
				{
					continue;
				}
				else if (j < 0)
				{
					index = nodeCount + j;
				}
				else if (j >= nodeCount) {
					index = j % nodeCount;
				}
				else
				{
					index = j;
				}
				int randWeight = random.nextInt(50);
				matrix[nodes.get(i)][nodes.get(index)] = randWeight;
				matrix[nodes.get(index)][nodes.get(i)] = randWeight;
			}
		}
		
		return new Graph(matrix);
	}
	
	public static void printReversePath(ArrayList<Integer> path){
		for (int i = path.size() - 1; i > 0; i--) {
			System.out.print(path.get(i) + "->");
		}
		System.out.println(path.get(0));
	}
	
//	public ArrayList<Integer> dijkstra(int srcId, int destId){
//		int dist[] = new int[matrix.length];
//		int prev[] = new int[matrix.length];
//		
//		Arrays.fill(dist, Integer.MAX_VALUE);
//		Arrays.fill(prev, -1);
//		
//		dist[srcId] = 0;
//		
//		Set<Integer> unvisited = new HashSet<Integer>();
//		for (int i = 0; i < matrix.length; i++) {
//			unvisited.add(i);
//		}
//		
//		while (!unvisited.isEmpty()) {
//			int minDist = Integer.MAX_VALUE, minNode = -1;
//			// iterate all nodes in unvisited to find the least cost one
//			for (Integer node : unvisited) {
//				if (minDist > dist[node]) {
//					minDist = dist[node];
//					minNode = node;
//				}
//			}
//			// relax every neighbor
//			for (int i = 0; i < matrix.length; i++) {
//				if (i != minNode && matrix[minNode][i] >= 0) {
//					// alternative path to the neighbor
//					int altDist = dist[minNode] + matrix[minNode][i];
//					if (altDist < dist[i]) {
//						dist[i] = altDist;
//						prev[i] = minNode;
//					}
//				}
//			}
//			unvisited.remove(minNode);
//		}
//		
//		if (dist[destId] >= Integer.MAX_VALUE) {
//			return null;
//		}
//		
//		ArrayList<Integer> path = new ArrayList<Integer>();
//		int parent = destId;
//		do {
//			path.add(parent);
//			parent = prev[parent];
//		}while (parent != srcId);
//		path.add(parent);
//		
//		return path;
//	}
	
	public ArrayList<Integer> dijkstra(int srcId, int destId){
		int dist[] = new int[matrix.length];
		int prev[] = new int[matrix.length];
		
		Arrays.fill(dist, Integer.MAX_VALUE);
		Arrays.fill(prev, -1);
		
		dist[srcId] = 0;

		Set<Integer> unseen = new HashSet<Integer>(), 
				intree = new HashSet<Integer>(), fringe = new HashSet<Integer>();
		for (int i = 0; i < matrix.length; i++) {
			unseen.add(i);
		}
		// pick src as the start vertex, mark it in the intree set
		intree.add(srcId);
		unseen.remove(srcId);
		dist[srcId] = 0;
		prev[srcId] = -1;
		// for each edge outgoing, marke the dest node as fringe, thier prev as the current vertex,
		// distance as the weight between src and them
		for (int i = 0; i < matrix.length; i++) {
			if (i != srcId && matrix[srcId][i] > 0) {
				unseen.remove(i);
				fringe.add(i);
				prev[i] = srcId;
				dist[i] = matrix[srcId][i];
			}
		}
		
		while (fringe.size() > 0) {
			int curMax = Integer.MIN_VALUE, maxNid = -1;
			// find the node in fringe set with largest distance
			for (Integer node : fringe) {
				if (dist[node] > curMax) {
					curMax = dist[node];
					maxNid = node;
				}
			}
			// mark it as intree
			fringe.remove(maxNid);
			intree.add(maxNid);
			
			// mark neighbors of it
			for(int i = 0; i < matrix.length; i++){
				// skip unconnected vertices
				if (matrix[maxNid][i] <= 0) {
					continue;
				}
				int tmpMin = dist[maxNid] < matrix[maxNid][i] ? dist[maxNid]:matrix[maxNid][i];
				if (unseen.contains(i)) {
					fringe.add(i);
					unseen.remove(i);
					prev[i] = maxNid;
					dist[i] = tmpMin;
				}
				else if (fringe.contains(i) && (dist[i] < tmpMin)) {
					dist[i] = tmpMin;
					prev[i] = maxNid;
				}
			}
		}
		
		ArrayList<Integer> path = new ArrayList<Integer>();
		int parent = destId;
		do {
			path.add(parent);
			parent = prev[parent];
		}while (parent != srcId);
		path.add(parent);
		
		return path;
	}
	
//	// mark out going edge from node id as fringes or relax those fringes
//	private void relaxFringes(int id, Set<Integer> fringe, Set<Integer> unseen, int[] prev){
//		for (int i = 0; i < matrix.length; i++) {
//			if (i == id) {
//				continue;
//			}
//			if (matrix[id][i] > 0) {
//				if (unseen.contains(i)) {
//					unseen.remove(i);
//					fringe.add(i);
//					prev[i] = id;
//				}
//				else if (fringe.contains(i)) {
//					
//				}
//			}
//		}
//	}
//	
//	// mark all out going edge from node id as fringes and remove them from unseen, and add parents for them
//	private void markFringes(int id, Set<Integer> fringe, Set<Integer> unseen, int[] prev){
//		for (int i = 0; i < matrix.length; i++) {
//			if (i != id && matrix[id][i] > 0) {
//				unseen.remove(i);
//				fringe.add(i);
//				prev[i] = id;
//			}
//		}
//	}
	
	public ArrayList<Integer> heapDijkstra(int srcId, int destId){
		int dist[] = new int[matrix.length];
		int prev[] = new int[matrix.length];
		
		Arrays.fill(dist, Integer.MAX_VALUE);
		Arrays.fill(prev, -1);
		
		dist[srcId] = 0;
		
		Set<Integer> unvisited = new HashSet<Integer>();
		for (int i = 0; i < matrix.length; i++) {
			unvisited.add(i);
		}
		
		while (!unvisited.isEmpty()) {
			int minDist = Integer.MAX_VALUE, minNode = -1;
			// iterate all nodes in unvisited to find the least cost one
			for (Integer node : unvisited) {
				if (minDist > dist[node]) {
					minDist = dist[node];
					minNode = node;
				}
			}
			// relax every neighbor
			for (int i = 0; i < matrix.length; i++) {
				if (i != minNode && matrix[minNode][i] >= 0) {
					// alternative path to the neighbor
					int altDist = dist[minNode] + matrix[minNode][i];
					if (altDist < dist[i]) {
						dist[i] = altDist;
						prev[i] = minNode;
					}
				}
			}
			unvisited.remove(minNode);
		}
		
		if (dist[destId] >= Integer.MAX_VALUE) {
			return null;
		}
		
		ArrayList<Integer> path = new ArrayList<Integer>();
		int parent = destId;
		do {
			path.add(parent);
			parent = prev[parent];
		}while (parent != srcId);
		path.add(parent);
		
		return path;
	}
}
