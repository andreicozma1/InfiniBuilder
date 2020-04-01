package app.structures.maze;

import app.structures.StructureBuilder;
import app.utils.ResourcesUtil;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import app.structures.objects.Base_Cube;
import app.GameBuilder;
import javafx.scene.paint.Material;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MazeUtil {
    private GameBuilder context;
    private double startingX;
    private double startingZ;
    private double cellW;
    private double cellD;
    private double cellH;
    private int mazeRows;
    private int mazeCols;
    private long seed;
    private Material mazeMaterial = ResourcesUtil.metal;
    private Group mazeGroup;
    private MazeGenerator mazeGenerator;
    private List<Wall> walls;
    public static Map<Point2D, StructureBuilder> maze_map_block = new HashMap<Point2D,StructureBuilder>();
    private int wall_height = 4;

    /**
     * Constructor for MazeUtil
     * @param context,startingX,startingZ,cellW,cellD,cellH,mazeRows,mazeCols,seed
     */
    public MazeUtil(  GameBuilder context,
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
    public Map<Point2D, StructureBuilder> getMaze_map_block() { return maze_map_block; }

    public void setMazeMaterial(Material mazeMaterial) { this.mazeMaterial = mazeMaterial; }

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
//                    System.out.println("create wall");
                    Base_Cube cube = new Base_Cube("Maze Wall",20, 20, 20);
                    cube.setMaterial(mazeMaterial);
                    maze_map_block.put(new Point2D(currX, currZ), cube);
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
            Base_Cube cube = new Base_Cube("Maze Wall", cellW,cellH,cellD);
            cube.setMaterial(mazeMaterial);
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

            maze_map_block.put(point, cube);
        }

        System.out.println(maze_map_block.size());
    }

    public void draw(){
        for(int i = 0; i < wall_height; i++){
            createBlockMap();
            for (Map.Entry<Point2D, StructureBuilder> point2DStructureBuilderEntry : maze_map_block.entrySet()) {
                context.getEnvironment().placeObject((Point2D) ((Map.Entry) point2DStructureBuilderEntry).getKey(), (StructureBuilder) ((Map.Entry) point2DStructureBuilderEntry).getValue(), true);
            }
        }
    }
}