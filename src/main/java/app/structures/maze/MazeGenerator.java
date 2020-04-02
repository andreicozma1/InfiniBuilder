package app.structures.maze;

import app.algorithms.DisjointSet;

import java.util.*;

public class MazeGenerator {
    private int rows;
    private int cols;
    private long seed;
    private Random ran;
    private List<Wall> maze;
    private List<Wall> deletedWalls;
    private DisjointSet disjointSet;

    public MazeGenerator(int r, int c, long seed) {
        ran = new Random();
        this.cols = c;
        this.rows = r;
        System.out.println(r*c);
        this.seed = seed;
        ran.setSeed(this.seed);
        maze = new ArrayList<Wall>();
        deletedWalls = new ArrayList<Wall>();
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
    public List<Wall> getDeletedWalls() { return deletedWalls; }
    public void resetMaze(){
        ran.setSeed(this.seed);
        disjointSet = new DisjointSet(this.rows * this.cols);
        maze = new ArrayList<Wall>();
        deletedWalls = new ArrayList<Wall>();
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
                deletedWalls.add(maze.get(wallIndex));
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


