package app.algorithms;

import java.awt.geom.Point2D;
import java.util.*;

public class GraphUtil {
    private List<Integer> W;                 // Weights when leaving each node
    private List<Integer> P;                 // Previous nodes
    private Set<Integer> V;                  // Visited nodes
    private Map<Integer,List<Edge>> E;       // Edges(v1->v2) indexed by the v1



    // if there are no weights given in the constructor each node is given a weight value of 1
    // previous nodes are all set to -1
    public GraphUtil(int numVertices){
        P = new ArrayList<Integer>(Collections.nCopies(numVertices, -1));
        W = new ArrayList<Integer>(Collections.nCopies(numVertices, 1));
        V = new HashSet<>();
        E = new HashMap<>();
    }

    public GraphUtil(List<Integer> weights){
        P = new ArrayList<Integer>(Collections.nCopies(weights.size(), -1));
        W = weights;
        V = new HashSet<>();
        E = new HashMap<>();
    }

    public List<Integer> getWeights() { return W; }

    public void addEdge(Edge edge){
        // if the v1 does not exist in the edge map add it
        if(!E.containsKey(edge.v1)) E.put(edge.v1,new ArrayList<>());
        E.get(edge.v1).add(edge);
    }

    public List<Integer> DFS(int start, int end){
        List<Integer> path = new ArrayList<>();

        return path;
    }

    public List<Integer> BFS(int start, int end){
        List<Integer> path = new ArrayList<>();

        return path;
    }

    public List<Integer> Dijkstra(int start, int end){
        List<Integer> path = new ArrayList<>();

        return path;
    }

    public void print(){
        int i,j;
        for( i = 0 ; i < W.size() ; i++ ){
            System.out.print("V " + i +" ("+W.get(i)+ ") : ");
            if(E.containsKey(i)){
                for( j = 0 ; j < E.get(i).size() ; j++ ) {
                    System.out.print(E.get(i).get(j).v2+" ");
                }
            }
            System.out.println();
        }
    }
}


