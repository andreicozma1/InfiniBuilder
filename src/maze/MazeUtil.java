package maze;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import objects.DrawCube;
import utils.WindowUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MazeUtil {
    private WindowUtil context;
    private double startingX;
    private double startingZ;
    private double cellW;
    private double cellD;
    private double cellH;
    private int mazeRows;
    private int mazeCols;
    private long seed;
    private Group mazeGroup;
    private MazeGenerator mazeGenerator;
    private List<Wall> walls;
    private Map<Point2D, Box> maze_map_block = new HashMap<Point2D,Box>();

    /**
     * Constructor for MazeUtil
     * @param context,startingX,startingZ,cellW,cellD,cellH,mazeRows,mazeCols,seed
     */
    public MazeUtil(  WindowUtil context,
                      double startingX,
                      double startingZ,
                      double cellW,
                      double cellD,
                      double cellH,
                      int mazeRows,
                      int mazeCols,
                      long seed ){
        this.context = context;
        this.startingX = startingX;
        this.startingZ = startingZ;
        this.cellW = cellW;
        this.cellD = cellD;
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
    public double getCellD() { return cellD; }
    public double getCellW() { return cellW; }
    public int getMazeCols() { return mazeCols; }
    public int getMazeRows() { return mazeRows; }
    public long getSeed() { return seed; }
    public Map<Point2D, Box> getMaze_map_block() { return maze_map_block; }

    public void createBlockMap(){
        int i, j;
        double currX = startingX;
        double currZ = startingZ;
        maze_map_block.clear();

        // add known maze walls to the map
        for( i = 0 ; i < mazeRows*2 + 1 ; i++ ){
            currX =startingX;
            for( j = 0 ; j < mazeCols*2 + 1 ; j++ ) {
                if(i == 0 || i== mazeRows*2 || j == 0 || j == mazeCols*2 || (i%2==0 && j%2==0)) {
                    System.out.println("create wall");
                    DrawCube cube = new DrawCube(20, 20, 20);
                    maze_map_block.put(new Point2D(currX, currZ), cube.getBox());
                }
                currX += cellW;
            }
            currZ += cellD;
        }

        // add vertical and horizontal generated walls to the map
        // wall is vertical if xindex of two walls are equal
        // wall is horizontal if xindex of two walls are not equal

        // to find x index := 1 + [ 2 * ( mazeIndex % cols) ]
        // to find z index := 1 + [ 2 * ( mazeIndex / cols) ]
        for (Wall w : walls){
            DrawCube cube = new DrawCube(cellW,cellH,cellD);

            Point2D point;
            int xindex1 = 1 + ( 2 * (w.cell1 % mazeCols) );
            int zindex1 = 1 + ( 2 * (w.cell1 / mazeCols) );
            int xindex2 = 1 + ( 2 * (w.cell2 % mazeCols) );
            int zindex2 = 1 + ( 2 * (w.cell2 / mazeCols) );

            // wall separating vertical cells
            if ( xindex1 == xindex2 ) {
                // find vertical wall coordinate
                if ( zindex1 < zindex2 ) {
                    point = new Point2D(startingX + (xindex1 * cellW),startingZ + ((zindex1 + 1) * cellD) );
                } else {
                    point = new Point2D(startingX + (xindex1 * cellW),startingZ + ((zindex2 + 1) * cellD) );
                }
            }
            // wall separating horizontal cells
            else {
                // find horizontal wall coordinate
                if ( xindex1 < xindex2 ) {
                    point = new Point2D(startingX + ((xindex1 + 1) * cellW),startingZ + (zindex1 * cellD) );
                } else {
                    point = new Point2D(startingX + ((xindex2 + 1) * cellW), startingZ + (zindex1 * cellD));
                }
            }

            maze_map_block.put(point, cube.getBox());
        }

        System.out.println(maze_map_block.size());

    }

}