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

        if( ranks.get(s1) > ranks.get(s2) ){
            parent = s1;
            child = s2;
        } else {
            parent = s2;
            child = s1;
        }

        links.set(child,parent);
        if(ranks.get(parent) == ranks.get(child)){
            ranks.set(parent,ranks.get(parent)+1);
        }
        return parent;
    }

    public int Find(int element){
        // declare parent and child node variables
        int parent, child;

        // find the root of the tree
        // while finding the root set the parents link to the childs
        child = -1;
        while ( links.get(element) != -1 ){
            parent = links.get(element);
            links.set(element,child);
            child = element;
            element = parent;
        }

        // travels back to original element
        // set each node link to the root of the tree
        parent = element;
        element = child;
        while (element != -1){
            child = links.get(element);
            links.set(element, parent);
            element = child;
        }
        return parent;
    }

    public void Print(){
        int i;

        System.out.println("\nNode:  ");
        for (i = 0; i < links.size(); i++) System.out.println(i + " ");
        System.out.println("\nLinks:  ");
        for (i = 0; i < links.size(); i++) System.out.println(links.get(i) + " ");
        System.out.println("\nRanks:  ");
        for (i = 0; i < links.size(); i++) System.out.println(ranks.get(i) + " ");
        System.out.println("\n");
    }
}
