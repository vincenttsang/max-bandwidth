package edu.tamu.algo;

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
	
	public ArrayList<Integer> dijkstra(int srcId, int destId){
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
