package maze;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import objects.DrawCube;
import environment.MaterialsUtil;
import utils.WindowUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MazeUtil {
    private WindowUtil context;
    private double startingX;
    private double startingZ;
    private double cellW;
    private double cellL;
    private double cellH;
    private int mazeRows;
    private int mazeCols;
    private long seed;
    private Group mazeGroup;
    private MazeGenerator mazeGenerator;
    private List<Wall> walls;
    private Map<Point2D, Box> maze_map_block = new HashMap<Point2D,Box>();

    public MazeUtil(  WindowUtil context,
                      double startingX,
                      double startingZ,
                      double cellW,
                      double cellL,
                      double cellH,
                      int mazeRows,
                      int mazeCols,
                      long seed ){
        this.context = context;
        this.startingX = startingX;
        this.startingZ = startingZ;
        this.cellW = cellW;
        this.cellL = cellL;
        this.cellH = cellH;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.seed = seed;

        mazeGroup = new Group();
        mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, this.seed);
        walls = mazeGenerator.getWalls();
    }

    public double getStartingX() { return startingX; }
    public double getStartingZ() { return startingZ; }
    public double getCellH() { return cellH; }
    public double getCellL() { return cellL; }
    public double getCellW() { return cellW; }
    public int getMazeCols() { return mazeCols; }
    public int getMazeRows() { return mazeRows; }
    public long getSeed() { return seed; }
    public Map<Point2D, Box> getMaze_map_block() { return maze_map_block; }
    public boolean inFrame( double x, double y){return true;}

    public void createBlockMap(){
        int i, j;
        double currX = startingX;
        double currZ = startingZ;
        maze_map_block.clear();

        // add known maze walls to the map
        for( i = 0 ; i < mazeRows*2 + 1 ; i++ ){
            currX =startingX;
            for( j = 0 ; j < mazeCols*2 + 1 ; j++ ) {
                if(inFrame(currX,currZ)){
                    if(i == 0 || i== mazeRows*2 || j == 0 || j == mazeCols*2 || (i%2==0 && j%2==0)) {
                        System.out.println("create wall");
                        DrawCube cube = new DrawCube(20, 20, 20);
                        maze_map_block.put(new Point2D(currX, currZ), cube.getBox());
                    }
                }
                currX += cellW;
            }
            currZ += cellL;
        }

        // add vertical generated walls to the map

        // add horizontal generated walls to the map

        System.out.println(maze_map_block.size());

    }

}

