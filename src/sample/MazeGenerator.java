package sample;

import java.util.*;

public class MazeGenerator {
    private int rows;
    private int cols;
    private long seed;
    private Random ran;
    private List<Wall> maze;
    private DisjointSet disjointSet;

    public MazeGenerator(int r, int c, long seed) {
        ran = new Random();
        this.cols = c;
        this.rows = r;
        System.out.println(r*c);
        this.seed = seed;
        ran.setSeed(this.seed);
        maze = new ArrayList<Wall>();
        disjointSet = new DisjointSet(this.rows * this.cols);

        generateWalls();
        breakWalls();
    }

    public void setCols(int cols) { this.cols = cols; }
    public void setRows(int rows) { this.rows = rows; }
    public void setSeed(long seed) { this.seed = seed; }

    public int getCols() { return cols; }
    public int getRows() { return rows; }
    public long getSeed() { return seed; }
    public List<Wall> getWalls() { return maze; }

    public void resetMaze(){
        ran.setSeed(this.seed);
        disjointSet = new DisjointSet(this.rows * this.cols);
        maze = new ArrayList<Wall>();
        generateWalls();
        breakWalls();
    }

    private void generateWalls() {

        int r, c;

        // generate horizontal walls
        for ( r = 0 ; r < rows ; r++ ) {
            for ( c = 0 ; c < cols-1 ; c++ ) {
                Wall tmpWall = new Wall(r*cols +c,r*cols +c+1);
                maze.add(tmpWall);
            }
        }

        // generate vertical walls
        for ( c = 0 ; c < cols ; c++ ){
            for ( r = 0 ; r < rows-1 ; r++ ) {
                Wall tmpWall = new Wall(r*cols +c,r*cols +c+cols);
                maze.add(tmpWall);
            }
        }

    }

    private void breakWalls() {
        //  loops until every index in the disjoint set is joined together
        while( disjointSet.getSize(disjointSet.Find(0))!=rows*cols){
            int wallIndex = ran.nextInt(maze.size());
            int s1 = disjointSet.Find(maze.get(wallIndex).cell1);
            int s2 = disjointSet.Find(maze.get(wallIndex).cell2);
            if ( s1 != s2 ){
                maze.remove(wallIndex);
                disjointSet.Union(s1,s2);
            }
        }
    }

    public void printWalls(){
        for(Wall w : maze){
            System.out.println("MAZE WALLS "+w.cell1+" "+w.cell2);
        }
    }

    @Override
    public String toString() {
        return "MazeGenerator{" +
                "rows=" + rows +
                ", cols=" + cols +
                ", seed=" + seed +
                ", ran=" + ran +
                ", maze=" + maze +
                ", disjointSet=" + disjointSet +
                '}';
    }
}


class Wall{
    int cell1;
    int cell2;
    Wall(int c1, int c2){
        cell1 = c1;
        cell2 = c2;
    }
}

class DisjointSet{
    private ArrayList<Integer> links;
    private ArrayList<Integer> sizes;
    public DisjointSet(int nElements){
        links = new ArrayList<Integer>();
        sizes = new ArrayList<Integer>();
        for(int i = 0 ; i < nElements ; i++){
            links.add(-1);
            sizes.add(1);
        }
    }

    public int getSize(int index){
        return sizes.get(index);
    }

    public int Union(int s1, int s2){
        int parent,child;
        if( links.get(s1) != -1 || links.get(s2) != -1 ){
            System.out.println("Must call union on a set, not just an element.");
            return -1;
        }

        if( sizes.get(s1) > sizes.get(s2) ){
            parent = s1;
            child = s2;
        } else {
            parent = s2;
            child = s1;
        }

        links.set(child,parent);
        sizes.set(parent, sizes.get(parent) + sizes.get(child) );
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
        System.out.println("\nSizes:  ");
        for (i = 0; i < links.size(); i++) System.out.println(sizes.get(i) + " ");
        System.out.println("\n");
    }
}