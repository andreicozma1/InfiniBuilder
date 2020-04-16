package app.structures.spawnables.maze;

import app.algorithms.DisjointSet;
import app.algorithms.Edge;
import app.utils.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Maze Generator will randomly create a set of walls that define the walls of a maze.
 */
public class MazeGenerator {
    // global variables
    private static final String TAG = "MazeGenerator";
    private final Random ran;
    private int rows;
    private int cols;
    private long seed;
    private List<Edge> maze;
    private List<Edge> deletedEdges;
    private DisjointSet disjointSet;

    /**
     * Constructor takes in everything it needs to create a random maze.
     * @param r
     * @param c
     * @param seed
     */
    public MazeGenerator(int r, int c, long seed) {
        ran = new Random();
        this.cols = c;
        this.rows = r;
        Log.d(TAG,String.valueOf(r * c));
        this.seed = seed;
        ran.setSeed(this.seed);
        maze = new ArrayList<Edge>();
        deletedEdges = new ArrayList<Edge>();
        disjointSet = new DisjointSet(this.rows * this.cols);

        // generate then break the walls randomly
        generateWalls();
        breakWalls();
    }

    // getters
    public int getCols() { return cols; }
    public int getRows() { return rows; }
    public long getSeed() { return seed; }
    public List<Edge> getWalls() { return maze; }
    public List<Edge> getDeletedWalls() { return deletedEdges; }

    // setters
    public void setCols(int cols) { this.cols = cols; }
    public void setRows(int rows) { this.rows = rows; }
    public void setSeed(long seed) { this.seed = seed; }

    // this will create a new maze for the current class to hold
    public void resetMaze() {
        ran.setSeed(this.seed);
        disjointSet = new DisjointSet(this.rows * this.cols);
        maze = new ArrayList<Edge>();
        deletedEdges = new ArrayList<Edge>();
        generateWalls();
        breakWalls();
    }

    // this will generate a grid of walls that separate each node of the maze
    private void generateWalls() {

        int r, c;

        // generate horizontal walls
        for (r = 0; r < rows; r++) {
            for (c = 0; c < cols - 1; c++) {
                Edge tmpEdge = new Edge(r * cols + c, r * cols + c + 1);
                maze.add(tmpEdge);
            }
        }

        // generate vertical walls
        for (c = 0; c < cols; c++) {
            for (r = 0; r < rows - 1; r++) {
                Edge tmpEdge = new Edge(r * cols + c, r * cols + c + cols);
                maze.add(tmpEdge);
            }
        }
    }

    // this will break random walls until every node of the maze is connected
    private void breakWalls() {
        //  loops until every index in the disjoint set is joined together
        while (disjointSet.getSize(disjointSet.Find(0)) != rows * cols) {
            int wallIndex = ran.nextInt(maze.size());
            int s1 = disjointSet.Find(maze.get(wallIndex).v1);
            int s2 = disjointSet.Find(maze.get(wallIndex).v2);
            if (s1 != s2) {
                deletedEdges.add(maze.get(wallIndex));
                maze.remove(wallIndex);
                disjointSet.Union(s1, s2);
            }
        }
    }

    // prints out the walls
    public void printWalls() {
        for (Edge w : maze) {
            Log.d(TAG,"MAZE WALLS " + w.v1 + " " + w.v2);
        }
    }

    // prints out the deleted walls
    public void printDeletedWalls() {
        for (Edge w : deletedEdges) {
            Log.d(TAG,"MAZE WALLS " + w.v1 + " " + w.v2);
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


