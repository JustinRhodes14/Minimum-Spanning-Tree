package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.Graph;
import structures.PartialTree;
import structures.Vertex;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) { //Steps 1 & 2
		
		/* COMPLETE THIS METHOD */
		
		//need to associate P with each respective partial tree T (there is a merge method, although not sure
		//how to change a PQ's heap
		//use getArcs method to alter each trees heap respectively (getArcs.insert)
		//the way to mark v as belonging to T is by associating its root as the parent as well
		// PT = Partial Tree, PQ = Priority Queue, EX.) PQX = Priority Queue X, PTY = Priority Tree Y
		PartialTreeList L = new PartialTreeList();
		for (Vertex v : graph.vertices) {
			v.parent = v;
			PartialTree T = new PartialTree(v);
				while (v.neighbors != null) {
					Arc temp = new Arc(v,v.neighbors.vertex,v.neighbors.weight);
					T.getArcs().insert(temp);
					v.neighbors.vertex.parent = v;
					if (v.neighbors.next == null) {
						break;
					}
					v.neighbors = v.neighbors.next;
					if (v.neighbors.vertex == v) {
						v.neighbors = v.neighbors.next;
					}
				}
				L.append(T);
		}
		Iterator<PartialTree> iter = L.iterator();
		while (iter.hasNext()) {
			PartialTree pt = iter.next();
			System.out.println();
			System.out.println("Neighbors being listed...");
			System.out.println(pt.toString());
		}
		System.out.println();
		return L;
	}
	
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<Arc> execute(PartialTreeList ptlist) {
		/* COMPLETE THIS METHOD */
		ArrayList<Arc> MST = new ArrayList<Arc>();
		while (ptlist.size() > 1) {
			PartialTree PTX = ptlist.remove();
			
			Arc arcX = PTX.getArcs().deleteMin();
			Vertex v1 = PTX.getRoot();
			Vertex v2 = arcX.getv2();
				
			if (v1 == v2 || v1 == v2.parent) {
				arcX = PTX.getArcs().deleteMin();
				v2 = arcX.getv2().parent;
			}
				
			MST.add(arcX);
			PartialTree PTY = ptlist.removeTreeContaining(v2);
			
			if (PTY == null) {
				continue;
			}
			PTY.getRoot().parent = PTX.getRoot();
			PTY.merge(PTX);
			ptlist.append(PTY);
			}
		System.out.println("Generating Minimum Spanning Tree...");
			for (int i = 0; i < MST.size(); i++) {
				System.out.println(MST.get(i));
				}
		return MST;
	}
	
    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
    		/* COMPLETE THIS METHOD */
    	if (size == 0) {
    		throw new NoSuchElementException("No items in the list.");
    	}
    	
    	if (size == 1) {
    		if (vertex.equals(rear.tree.getRoot())) {
    			PartialTree removed = rear.tree;
    			rear = null;
    			size--;
    			return removed;
    		}
    	}
    	
    	Node ptr = rear.next;
    	Node prev = rear;
    	
    	do {
    		
    		if (vertex.equals(ptr.tree.getRoot())) {
    			PartialTree current = ptr.tree;
    			
    			prev.next = ptr.next;
    			size--;
    			
    			if (ptr == rear) {
    				rear = prev;
    			}
    			
    			return current;
    		}
    		
    		prev = ptr;
    		ptr = ptr.next;
    		
    	} while (ptr != rear);
    	
    	if (ptr == rear && prev == rear) {
    		throw new NoSuchElementException("Tree containing the vertex not found");
    	}
    	
    	PartialTree result = rear.tree;
    	size--;
    	
    	return result;
     }
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


