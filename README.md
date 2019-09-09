# Minimum-Spanning-Tree

This program develops a minimum spanning tree for any acyclic graph, and will return all the neighbors of a given vertice, as well as all other vertices that are connected to it. Utilizing a min heap, the program will go through each neighboring vertice, adding it to the heap. Once a given vertice has no more neighbors, it will dequeue the root until it finds a path that hasn't been used, which gives us the minimum spanning tree without running into loops.

For this project, I implemented all functions within PartialTreeList.java, the java files within the structures package were provided to the class by the professor.
