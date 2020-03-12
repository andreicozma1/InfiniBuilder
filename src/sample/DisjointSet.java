package sample;

import java.util.ArrayList;

public class DisjointSet{
    private ArrayList<Integer> links;
    private ArrayList<Integer> ranks;
    public DisjointSet(int nElements){
        links = new ArrayList<Integer>();
        ranks = new ArrayList<Integer>();
        for(int i = 0 ; i < nElements ; i++){
            links.add(-1);
            ranks.add(1);
        }
    }

    public int Union(int s1, int s2){
        int parent,child;

        if( links.get(s1) != -1 || links.get(s2) != -1 ){
            System.out.println("Must call union on a set, not just an element.");
            return -1;
        }
        return -1;
    }

    public int Find(int element){
        return -1;
    }

    public void Print(){

    }
}
