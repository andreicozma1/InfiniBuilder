package sample;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.Random;

public class MazeGenerator {
    private int row;
    private int col;
    private long seed;
    private int startingXCoord = 0 ;
    private int startingYCoord = 0 ;
    private int startingZCoord = 0 ;
    private int finalXCoord = 0 ;
    private int finalYCoord = 0 ;
    private int finalZCoord = 0 ;
    private Random ran;
    private ArrayList<ArrayList<Boolean>> maze;
    private ConcurrentSkipListMap<Double, Integer> walls;
    private DisjointSet disjointSet;

    public MazeGenerator(int row, int col, long seed){
        ran = new Random();
        setRow(row);
        setCol(col);
        setSeed(seed);
        maze = new ArrayList<ArrayList<Boolean>>();
        walls = new ConcurrentSkipListMap<Double, Integer>();
        disjointSet = new DisjointSet(this.row*this.col);

        generateWalls();
        breakWalls();
    }

    public void setCol(int col) { this.col = col; }
    public void setRow(int row) { this.row = row; }
    public void setSeed(long seed) {
        this.seed = seed;
        ran.setSeed(this.seed);
    }

    public int getCol() { return col; }
    public int getRow() { return row; }
    public long getSeed() { return seed; }

    private void generateWalls(){

        int r, c, c1;

        // generate walls that separate vertical cells
        for( r = 0; r < row-1; r++ ){
            for ( c = 0; c < col; c++ ){
                c1 = r*col + c;
                walls.put(ran.nextDouble(),c1);
            }
        }

        // generate walls that separate horizontal cells
        for( r = 0; r < row; r++ ){
            for ( c = 0; c < col-1; c++ ){
                c1 = (r*col + c) + row*col;
                walls.put(ran.nextDouble(),c1);
            }
        }
    }

    private void breakWalls(){

        int c1, c2, ncomp, s1, s2;
        Map.Entry wallEntry;

        ncomp = row * col;
        wallEntry = walls.pollFirstEntry();
        while( ncomp > 1 ){
            c1 = (int)wallEntry.getValue();
            // wall separating vertical cells
            if ( c1 < row * col ){
                c2 = c1 + col;
            }
            // wall separating horizontal cells
            else {
                c1 -= row * col;
                c2 = c1 +1;
            }
            s1 = disjointSet.Find(c1);
            s2 = disjointSet.Find(c2);
            if(s1 != s2){
                disjointSet.Union(s1,s2);
                //finish this part

                wallEntry = walls.pollFirstEntry();
                ncomp--;
            }else{
                wallEntry = walls.pollFirstEntry();
            }
        }

    }
}
