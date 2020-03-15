package sample;

import java.util.*;

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
    private List<Wall> maze;
    private List<Wall> innerWalls;
    private DisjointSet disjointSet;

    public MazeGenerator(int r, int c, long seed) {
        ran = new Random();
        this.cols = c;
        this.rows = r;
        System.out.println(r*c);
        this.seed = seed;
        ran.setSeed(this.seed);
        maze = new ArrayList<Wall>();
        innerWalls = new ArrayList<Wall>();
        disjointSet = new DisjointSet(this.rows * this.cols);

        generateWalls();
        breakWalls();
        makeMaze();

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

        int r, c;

        // generate horizontal walls
        for ( r = 0 ; r < rows ; r++ ) {
            for ( c = 0 ; c < cols-1 ; c++ ) {
                Wall tmpWall = new Wall(r*cols +c,r*cols +c+1);
                maze.add(tmpWall);
//                if ( (r*cols + c) >= cols && (r*cols + c) < (rows*cols) - cols ) {
//                    innerWalls.add(tmpWall);
//                }
            }
        }

        // generate vertical walls
        for ( c = 0 ; c < cols ; c++ ){
            for ( r = 0 ; r < rows-1 ; r++ ) {
                Wall tmpWall = new Wall(r*cols +c,r*cols +c+cols);
                maze.add(tmpWall);
//                if ( (r*cols + c) % cols != 0 && (r*cols + c + 1) % cols != 0 ) {
//                    innerWalls.add(tmpWall);
//                }
            }
        }

    }

    private void breakWalls() {
//        while( disjointSet.Find(0) != disjointSet.Find(rows*cols-1)){
        while( disjointSet.getSize(disjointSet.Find(0))!=rows*cols){
//            System.out.println(maze.size());

            int wallIndex = ran.nextInt(maze.size());
            int c1 = maze.get(wallIndex).cell1;
            int c2 = maze.get(wallIndex).cell2;
            int s1 = disjointSet.Find(c1);
            int s2 = disjointSet.Find(c2);
            if ( s1 != s2 ){
                maze.remove(wallIndex);
                disjointSet.Union(s1,s2);
            }
        }
    }

    private void makeMaze(){

    }

    public void printWalls(){
        for(Wall w : maze){
            System.out.println("MAZE WALLS "+w.cell1+" "+w.cell2);
        }
//        for(Wall w : innerWalls){
//            System.out.println("INNER WALLS "+w.cell1+" "+w.cell2);
//        }
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