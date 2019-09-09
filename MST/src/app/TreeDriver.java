package app;

import java.io.IOException;

import structures.*;
public class TreeDriver {
	public static void main(String[] args) throws IOException {
		Graph g = new Graph("graph2.txt");
		PartialTreeList.execute(PartialTreeList.initialize(g));
		
	}
}
