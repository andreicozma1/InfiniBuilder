package app.algorithms;

import app.utils.Log;

import java.util.ArrayList;

// This is a Disjoint set implementation that unions by size and uses path compression
// This is based off Dr. Plank's (UTK) implementation of a disjoint set
public class DisjointSet {
    private static final String TAG = "DisjointSet";

    // Lists that hold each nodes links and sizes
    private final ArrayList<Integer> links;
    private final ArrayList<Integer> sizes;

    /**
     * Disjoint Set constructor. Initializes the Disjoint set with nElements amount of nodes.
     *
     * @param nElements
     */
    public DisjointSet(int nElements) {
        links = new ArrayList<Integer>();
        sizes = new ArrayList<Integer>();

        // sets all links to default and all sizes to 1
        for (int i = 0; i < nElements; i++) {
            links.add(-1);
            sizes.add(1);
        }
    }

    /**
     * getSize returns an the size of the given index.
     *
     * @param index
     * @return
     */
    public int getSize(int index) {
        return sizes.get(index);
    }

    /**
     * Union takes the indexes of two nodes and union them based off their sizes.
     *
     * @param s1
     * @param s2
     * @return
     */
    public int Union(int s1, int s2) {
        // declare variables
        int parent, child;

        // error checks the given input
        if (links.get(s1) != -1 || links.get(s2) != -1) {
            Log.d(TAG,"Must call union on a set, not just an element.");
            return -1;
        }

        // determines which node it the parent and which is the child
        if (sizes.get(s1) > sizes.get(s2)) {
            parent = s1;
            child = s2;
        } else {
            parent = s2;
            child = s1;
        }

        // sets the links and sizes of the nodes
        links.set(child, parent);
        sizes.set(parent, sizes.get(parent) + sizes.get(child));
        return parent;
    }

    /**
     * Find takes an index and returns the indexes root. Uses path compression to help the time complexity.
     *
     * @param element
     * @return
     */
    public int Find(int element) {
        // declare parent and child node variables
        int parent, child;

        // find the root of the tree
        // while finding the root set the parents link to the child's
        child = -1;
        while (links.get(element) != -1) {
            parent = links.get(element);
            links.set(element, child);
            child = element;
            element = parent;
        }

        // travels back to original element
        // set each node link to the root of the tree
        parent = element;
        element = child;
        while (element != -1) {
            child = links.get(element);
            links.set(element, parent);
            element = child;
        }
        return parent;
    }

    /**
     * Print will print out all the disjoint sets information.
     */
    public void Print() {
        int i;

        Log.d(TAG,"\nNode:  ");
        for (i = 0; i < links.size(); i++) Log.d(TAG,i + " ");
        Log.d(TAG,"\nLinks:  ");
        for (i = 0; i < links.size(); i++) Log.d(TAG,links.get(i) + " ");
        Log.d(TAG,"\nSizes:  ");
        for (i = 0; i < links.size(); i++) Log.d(TAG,sizes.get(i) + " ");
        Log.d(TAG,"\n");
    }
}
