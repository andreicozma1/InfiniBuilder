package app.algorithms;

import app.structures.spawnables.utils.Log;

import java.util.*;

/**
 * Graph util is an implementation of an adjacency list.
 */
public class GraphUtil {
    private static final String TAG = "GraphUtil";

    //global variables
    private final int V;                              // Number of nodes
    private final List<Integer> w;                    // Weights when leaving each node
    private final HashMap<Integer, List<Edge>> e;      // Edges(v1->v2) indexed by the v1


    // if there are no weights given in the constructor each node is given a weight value of 1
    // previous nodes are all set to -1

    /**
     * Constructor takes in the number of nodes and sets each node's weight to 1.
     *
     * @param v
     */
    public GraphUtil(int v) {
        // initialize global variables
        this.V = v;
        w = new ArrayList<Integer>(Collections.nCopies(V, 1));
        e = new HashMap<>();
        for (int i = 0; i < V; i++) e.put(i, new ArrayList<>());
    }

    /**
     * Constructor takes in a list of weights that corresponds to each node's weight.
     *
     * @param weights
     */
    public GraphUtil(List<Integer> weights) {
        // initialize global variables
        this.V = weights.size();
        w = weights;
        e = new HashMap<>();
        for (int i = 0; i < V; i++) e.put(i, new ArrayList<>());
    }

    // Getters
    public int getNumVertices() {
        return V;
    }

    public List<Integer> getWeights() {
        return w;
    }

    /**
     * Add a directed edge from v1 to v2
     *
     * @param edge
     */
    public void addEdge(Edge edge) {
        if (edge.v1 < 0 || edge.v2 < 0 || edge.v1 >= V || edge.v2 >= V) {
            Log.d(TAG, "Bad Edge");
            return;
        }
        e.get(edge.v1).add(edge);
    }

    //********************************************************************************

    /**
     * Depth First Search to find a path from the start pos to the end pos.
     *
     * @param start
     * @param end
     * @return
     */
    public List<Integer> DFS(int start, int end) {
        // declare variables
        int i;
        int cV;                                                                         // current Vertex
        boolean foundPath = false;                                                      // true if finds path from start to end. false otherwise.
        List<Integer> path = new LinkedList<>();                                        // stores the found path
        List<Boolean> visited = new ArrayList<>(Collections.nCopies(V, false));     // keeps track of the visited vertices
        List<Integer> prev = new ArrayList<Integer>(Collections.nCopies(V, -1));          // keeps track of a vertex's previous vertice
        Stack<Integer> frontier = new Stack<>();                                       // stack to keep track of what the depth first search needs to visit next

        // error checking
        if (start < 0 || end < 0 || start >= V || end >= V) {
            Log.d(TAG, "start and end position must be in bounds");
            return null;
        } else if (start == end) {
            Log.d(TAG, "start position is equal to the end position");
            return null;
        }

        // set up starting node
        visited.set(start, true);
        frontier.push(start);

        // iterate until find end or visited all nodes it could
        while (!frontier.isEmpty()) {
            // pop from the frontier
            cV = frontier.pop();

            for (i = 0; i < e.get(cV).size(); i++) {
                // if reaches the ending node

                if (e.get(cV).get(i).v2 == end) {
                    prev.set(e.get(cV).get(i).v2, e.get(cV).get(i).v1);
                    frontier.clear();
                    foundPath = true;
                    break;
                }
                // add the adjacent nodes to the
                else if (!visited.get(e.get(cV).get(i).v2)) {
                    prev.set(e.get(cV).get(i).v2, e.get(cV).get(i).v1);
                    visited.set(e.get(cV).get(i).v2, true);
                    frontier.add(e.get(cV).get(i).v2);
                }
            }
        }

        // if could not find path
        if (!foundPath) {
            Log.d(TAG, "Could not find a path from " + start + " to " + end);
            return null;
        }
        Log.d(TAG, "Found a path from " + start + " to " + end);

        // backtrace to find the path
        cV = end;
        while (cV != -1) {
            path.add(0, cV);
            cV = prev.get(cV);
        }

        return path;
    }

    //********************************************************************************

    /**
     * Breadth First Search to find a path from the start pos to the end pos.
     *
     * @param start
     * @param end
     * @return
     */
    public List<Integer> BFS(int start, int end) {
        // declare variables
        int i;
        int cV;                                                                         // current Vertex
        boolean foundPath = false;                                                      // true if finds path from start to end. false otherwise.
        List<Integer> path = new LinkedList<>();                                        // stores the found path
        List<Boolean> visited = new ArrayList<>(Collections.nCopies(V, false));      // keeps track of the visited vertices
        List<Integer> prev = new ArrayList<Integer>(Collections.nCopies(V, -1));           // keeps track of a vertex's previous vertice
        LinkedList<Integer> frontier = new LinkedList<>();                              // queue to keep track of what the breadth first search needs to visit next

        // error checking
        if (start < 0 || end < 0 || start >= V || end >= V) {
            Log.d(TAG, "start and end position must be in bounds");
            return null;
        } else if (start == end) {
            Log.d(TAG, "start position is equal to the end position");
            return null;
        }

        // set up starting node
        visited.set(start, true);
        frontier.add(start);

        // iterate until find end or visited all nodes it could
        while (!frontier.isEmpty()) {
            // pop from the frontier
            cV = frontier.poll();

            for (i = 0; i < e.get(cV).size(); i++) {
                // if reaches the ending node

                if (e.get(cV).get(i).v2 == end) {
                    prev.set(e.get(cV).get(i).v2, e.get(cV).get(i).v1);
                    frontier.clear();
                    foundPath = true;
                    break;
                }
                // add the adjacent nodes to the
                else if (!visited.get(e.get(cV).get(i).v2)) {
                    prev.set(e.get(cV).get(i).v2, e.get(cV).get(i).v1);
                    visited.set(e.get(cV).get(i).v2, true);
                    frontier.add(e.get(cV).get(i).v2);
                }
            }
        }

        // if could not find path
        if (!foundPath) {
            Log.d(TAG, "Could not find a path from " + start + " to " + end);
            return null;
        }
        Log.d(TAG, "Found a path from " + start + " to " + end);

        // backtrace to find the path
        cV = end;
        while (cV != -1) {
            path.add(0, cV);
            cV = prev.get(cV);
        }
        return path;
    }

    //********************************************************************************

    /**
     * Dijkstras shortest path algorithm to find the shortest path from start to end
     *
     * @param start
     * @param end
     * @return
     */
    public List<Integer> Dijkstra(int start, int end) {
        // declare variables
        int i;
        int cV;                                                                                 // current Vertex
        int aV;                                                                                 // adjacent Vertex
        int aD;                                                                                 // adjacent Distance
        boolean foundPath = false;                                                              // true if finds path from start to end. false otherwise.
        List<Integer> path = new LinkedList<>();                                                // stores the found path
        List<Integer> dist = new LinkedList<>(Collections.nCopies(V, Integer.MAX_VALUE));       // keeps track of the distances to each vertex
        List<Boolean> visited = new ArrayList<>(Collections.nCopies(V, false));              // keeps track of the visited vertices
        List<Integer> prev = new ArrayList<Integer>(Collections.nCopies(V, -1));                   // keeps track of a vertex's previous vertex
        PriorityQueue<Entry<Integer>> frontier = new PriorityQueue<>();                         // holds the values as a (dist,vertex) pair

        // error checking
        if (start < 0 || end < 0 || start >= V || end >= V) {
            Log.d(TAG, "start and end position must be in bounds");
            return null;
        } else if (start == end) {
            Log.d(TAG, "start position is equal to the end position");
            return null;
        }

        // set up starting node
        dist.set(start, 0);
        frontier.add(new Entry(0, start));

        // iterate until find end or visited all nodes it could
        while (!frontier.isEmpty()) {
            // get current vertex data
            cV = frontier.poll().getValue();
            Log.d(TAG, "currNode " + cV);

            // current node is marked
            if (visited.get(cV)) continue;

            // if found end
            if (cV == end) {
                foundPath = true;
                break;
            }

            // set node to marked
            visited.set(cV, true);

            // add adj nodes and fix their total dist
            for (i = 0; i < e.get(cV).size(); i++) {
                // set temp variables
                aV = e.get(cV).get(i).v2;
                aD = dist.get(cV) + w.get(cV);

                // if the adj are not already visited
                if (!visited.get(aV)) {
                    // fix the adj nodes weight
                    if (dist.get(aV) > aD) {
                        // set the nodes prev
                        prev.set(aV, cV);

                        // change the nodes total distance if it needs to be changed
                        dist.set(aV, aD);
                        frontier.add(new Entry(dist.get(aV), aV));
                    }
                }
            }
        }

        // if could not find path
        if (!foundPath) {
            Log.d(TAG, "Could not find a path from " + start + " to " + end);
            return null;
        }

        // backtrace path
        cV = end;
        while (cV != -1) {
            path.add(0, cV);
            cV = prev.get(cV);
        }
        return path;
    }

    /**
     * Prints out each vertex with its weight and each of its adjacent vertexes.
     */
    public void print() {
        int i, j;
        for (i = 0; i < w.size(); i++) {
            String output;
            output = +i + " (" + w.get(i) + ") : ";
            if (e.containsKey(i)) {
                for (j = 0; j < e.get(i).size(); j++) {
                    output = output + e.get(i).get(j).v2 + " ";
                }
            }
            Log.i(TAG, output);
        }
    }

}


