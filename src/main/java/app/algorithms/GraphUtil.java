package app.algorithms;

import java.util.*;

public class GraphUtil {
    private int V;
    private List<Integer> w;                 // Weights when leaving each node
    private Map<Integer,List<Edge>> e;       // Edges(v1->v2) indexed by the v1


    // if there are no weights given in the constructor each node is given a weight value of 1
    // previous nodes are all set to -1
    public GraphUtil(int v){
        this.V = v;
        w = new ArrayList<Integer>(Collections.nCopies(V, 1));
        e = new HashMap<>();
        for(int i = 0 ; i < V ; i++)e.put(i,new ArrayList<>());
    }

    public GraphUtil(List<Integer> weights){
        this.V = weights.size();
        w = weights;
        e = new HashMap<>();
        for(int i = 0 ; i < V ; i++)e.put(i,new ArrayList<>());
    }

    public int getNumVertices() { return V; }
    public List<Integer> getWeights() { return w; }

    /**
     * Add a directed edge from v1 to v2
     * @param edge
     */
    public void addEdge(Edge edge){
        if(edge.v1<0 || edge.v2<0 || edge.v1>=V || edge.v2>=V){
            System.out.println("Bad Edge");
            return;
        }
        e.get(edge.v1).add(edge);
    }

    //********************************************************************************
    // Depth First Search to find a path from the start pos to the end pos
    public List<Integer> DFS(int start, int end){
        // declare variables
        int cV, i;
        boolean foundPath = false;
        List<Integer> path = new LinkedList<>();
        List<Boolean> visited = new ArrayList<>(Collections.nCopies(V, false));
        List<Integer> prev = new ArrayList<Integer>(Collections.nCopies(V, -1));
        Stack<Integer> frontier = new Stack<>();

        // error checking
        if( start<0 || end<0 || start>=V || end>=V ){
            System.out.println("start and end position must be in bounds");
            return null;
        }else if( start == end ){
            System.out.println("start position is equal to the end position");
            return null;
        }

        // set up starting node
        visited.set(start,true);
        frontier.push(start);

        // iterate until find end or visited all nodes it could
        while(!frontier.isEmpty()){
            // pop from the frontier
            cV = frontier.pop();

            for(i = 0 ; i < e.get(cV).size() ;i++){
                // if reaches the ending node

                if(e.get(cV).get(i).v2==end){
                    prev.set(e.get(cV).get(i).v2,e.get(cV).get(i).v1);
                    frontier.clear();
                    foundPath = true;
                    break;
                }
                // add the adjacent nodes to the
                else if(!visited.get(e.get(cV).get(i).v2)){
                    prev.set(e.get(cV).get(i).v2,e.get(cV).get(i).v1);
                    visited.set(e.get(cV).get(i).v2,true);
                    frontier.add(e.get(cV).get(i).v2);
                }
            }
        }

        // if could not find path
        if(!foundPath){
            System.out.println("Could not find a path from "+start+" to "+end);
            return null;
        }

        System.out.println("Found a path from "+start+" to "+end);
        // backtrace to find the path
        cV = end;

        while(cV != -1){
            path.add(0,cV);
            cV = prev.get(cV);
        }
        return path;
    }
    //********************************************************************************
    // Breadth First Search to find a path from the start pos to the end pos
    public List<Integer> BFS(int start, int end){
        // declare variables
        int cV, i;
        boolean foundPath = false;
        List<Integer> path = new LinkedList<>();
        List<Boolean> visited = new ArrayList<>(Collections.nCopies(V, false));
        List<Integer> prev = new ArrayList<Integer>(Collections.nCopies(V, -1));
        LinkedList<Integer> frontier = new LinkedList<>();

        // error checking
        if( start<0 || end<0 || start>=V || end>=V ){
            System.out.println("start and end position must be in bounds");
            return null;
        }else if( start == end ){
            System.out.println("start position is equal to the end position");
            return null;
        }

        // set up starting node
        visited.set(start,true);
        frontier.add(start);

        // iterate until find end or visited all nodes it could
        while(!frontier.isEmpty()){
            // pop from the frontier
            cV = frontier.poll();

            for(i = 0 ; i < e.get(cV).size() ;i++){
                // if reaches the ending node

                if(e.get(cV).get(i).v2==end){
                    prev.set(e.get(cV).get(i).v2,e.get(cV).get(i).v1);
                    frontier.clear();
                    foundPath = true;
                    break;
                }
                // add the adjacent nodes to the
                else if(!visited.get(e.get(cV).get(i).v2)){
                    prev.set(e.get(cV).get(i).v2,e.get(cV).get(i).v1);
                    visited.set(e.get(cV).get(i).v2,true);
                    frontier.add(e.get(cV).get(i).v2);
                }
            }
        }

        // if could not find path
        if(!foundPath){
            System.out.println("Could not find a path from "+start+" to "+end);
            return null;
        }

        System.out.println("Found a path from "+start+" to "+end);
        // backtrace to find the path
        cV = end;

        while(cV != -1){
            path.add(0,cV);
            cV = prev.get(cV);
        }
        return path;
    }

    //********************************************************************************
    //dijkstras shortest path algorithm
    public List<Integer> Dijkstra(int start, int end){
        // declare variables
        int cV, aV, aD, i; // current vertex, adj vertex, i
        Entry cN;  // current node
        boolean foundPath = false;
        List<Integer> path = new LinkedList<>();
        List<Integer> dist = new LinkedList<>(Collections.nCopies(V, Integer.MAX_VALUE));
        List<Boolean> visited = new ArrayList<>(Collections.nCopies(V, false));
        List<Integer> prev = new ArrayList<Integer>(Collections.nCopies(V, -1));
        PriorityQueue<Entry> frontier = new PriorityQueue<>(); // holds the values as a (dist,vertex) pair

        // error checking
        if( start<0 || end<0 || start>=V || end>=V ){
            System.out.println("start and end position must be in bounds");
            return null;
        }else if( start == end ){
            System.out.println("start position is equal to the end position");
            return null;
        }

        // set up starting node
        dist.set(start,0);
        frontier.add(new Entry(0,start));

        while(!frontier.isEmpty()) {
            // get current vertex data
            cN = frontier.poll();
            cV = cN.getValue();
            System.out.println("currNode "+cV);

            // current node is marked
            if (visited.get(cV)) continue;

            // if found end
            if (cV == end){
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
        if(!foundPath){
            System.out.println("Could not find a path from "+start+" to "+end);
            return null;
        }

        // if cV
        cV = end;
        while (cV!=-1){
            path.add(0,cV);
            cV = prev.get(cV);
        }
        return path;
    }

    public void print(){
        int i,j;
        for( i = 0 ; i < w.size() ; i++ ){
            System.out.print("V " + i +" ("+w.get(i)+ ") : ");
            if(e.containsKey(i)){
                for( j = 0 ; j < e.get(i).size() ; j++ ) {
                    System.out.print(e.get(i).get(j).v2+" ");
                }
            }
            System.out.println();
        }
    }

    public class Entry implements Comparable<Entry> {
        private int key;
        private int value;

        public Entry(int key, int value) {
            this.key = key;
            this.value = value;
        }

        // getters
        public int getKey() { return key; }
        public int getValue() { return value; }

        @Override
        public int compareTo(Entry other) {
            return this.getKey() - (other.getKey());
        }
    }

}


