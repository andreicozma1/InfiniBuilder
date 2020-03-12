package sample;

import java.util.ArrayList;

public class MazeGenerator {
    private int row;
    private int col;
    private int seed;
    private int startingXCoord = 0 ;
    private int startingYCoord = 0 ;
    private int startingZCoord = 0 ;
    private int finalXCoord = 0 ;
    private int finalYCoord = 0 ;
    private int finalZCoord = 0 ;
    private ArrayList<ArrayList<Boolean>> maze;

    public MazeGenerator(int row, int col, int seed){
        setRow(row);
        setCol(col);
        setSeed(seed);
        maze = new ArrayList<ArrayList<Boolean>>();

        for( int i = 0 ; i < row ; i++ ){
            ArrayList<Boolean>tmp = new ArrayList<Boolean>(col);
            maze.add(tmp);
        }
    }

    public void setCol(int col) { this.col = col; }
    public void setRow(int row) { this.row = row; }
    public void setSeed(int seed) { this.seed = seed; }

    public int getCol() { return col; }
    public int getRow() { return row; }
    public int getSeed() { return seed; }
}
