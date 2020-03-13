package sample;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class MazeGenerator {
    private int rows;
    private int cols;
    private long seed;
    private int startingXCoord = 0;
    private int startingYCoord = 0;
    private int startingZCoord = 0;
    private int finalXCoord = 0;
    private int finalYCoord = 0;
    private int finalZCoord = 0;
    private Random ran;
    private ArrayList<Boolean> maze;
    private ConcurrentSkipListMap<Double, Integer> walls;
    private DisjointSet disjointSet;

    public MazeGenerator(int row, int col, long seed) {
        ran = new Random();
        setRows(row);
        setCols(col);
        setSeed(seed);
        maze = new ArrayList<Boolean>();
        walls = new ConcurrentSkipListMap<Double, Integer>();
        disjointSet = new DisjointSet(this.rows * this.cols);

        generateWalls();
        breakWalls();
        makeMaze();

    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        ran.setSeed(this.seed);
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public long getSeed() {
        return seed;
    }

    private void generateWalls() {

        int r, c, c1;

        // generate walls that separate vertical cells
        for (r = 0; r < rows - 1; r++) {
            for (c = 0; c < cols; c++) {
                c1 = r * cols + c;
                walls.put(ran.nextDouble(), c1);
            }
        }

        // generate walls that separate horizontal cells
        for (r = 0; r < rows; r++) {
            for (c = 0; c < cols - 1; c++) {
                c1 = (r * cols + c) + rows * cols;
                walls.put(ran.nextDouble(), c1);
            }
        }
    }

    private void breakWalls() {

        int c1, c2, ncomp, s1, s2;
        Map.Entry wallEntry;

        ncomp = rows * cols;
        wallEntry = walls.firstEntry();
        while (ncomp > 1) {
            c1 = (int) wallEntry.getValue();
            // wall separating vertical cells
            if (c1 < rows * cols) {
                c2 = c1 + cols;
            }
            // wall separating horizontal cells
            else {
                c1 -= rows * cols;
                c2 = c1 + 1;
            }

            s1 = disjointSet.Find(c1);
            s2 = disjointSet.Find(c2);

             // test for different connected parts
            if (s1 != s2) {
                disjointSet.Union(s1, s2);
                wallEntry = walls.pollFirstEntry();
                ncomp--;
            } else {
                wallEntry = walls.higherEntry((Double) wallEntry.getKey());
            }
        }
    }

    private void makeMaze(){

    }

    public void printWalls(){

        int c1, c2;

        System.out.println("ROWS " + rows + " COLS " + cols);

        for(Integer n : walls.values()){
            c1 = n;
            if( c1 < rows*cols){
                c2 = c1 + cols;
            } else {
                c1 -= rows*cols;
                c2 = c1 + 1;
            }
            System.out.println("WALL "+ c1 + " " + c2);
        }
    }
}
