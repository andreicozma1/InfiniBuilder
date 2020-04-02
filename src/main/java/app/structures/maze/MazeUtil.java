package app.structures.maze;

import app.structures.StructureBuilder;
import app.utils.ResourcesUtil;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import app.structures.objects.Base_Cube;
import app.GameBuilder;
import javafx.scene.paint.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MazeUtil {
    private GameBuilder context;
    private double startingX;
    private double startingZ;
    private double cellDim;
    private int cellWidth;
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
                      double cellDim,
                      int cellWidth,
                      int mazeRows,
                      int mazeCols,
                      long seed ){
        this.context = context;
        this.startingX = startingX;
        this.startingZ = startingZ;
        this.cellDim = cellDim;
        this.cellWidth = cellWidth;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.seed = seed;

        mazeGroup = new Group();
        mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, this.seed);
        walls = mazeGenerator.getWalls();
    }

    public double getStartingX() { return startingX; }
    public double getStartingZ() { return startingZ; }
    public double getCellDim() { return cellDim; }
    public int getMazeCols() { return mazeCols; }
    public int getMazeRows() { return mazeRows; }
    public long getSeed() { return seed; }
    public Map<Point2D, StructureBuilder> getMaze_map_block() { return maze_map_block; }

    public void setMazeMaterial(Material mazeMaterial) { this.mazeMaterial = mazeMaterial; }

    public void createBlockMap(){
        int i, j, mi, mj;
        double currX;
        double currZ = startingZ;
        maze_map_block.clear();

        // add known maze walls to the map
        for( i = 0 ; i < (mazeRows*2 + 1) * cellWidth ; i++ ){
            currX =startingX;
            mi = i / cellWidth;
            for( j = 0 ; j < (mazeCols*2 + 1) * cellWidth ; j++ ) {
                mj = j/cellWidth;
                if( (mi==1&&mj==0) || (mi== mazeRows*2-1&&mj==mazeCols*2) ){
                    context.getEnvironment().clearSpot(new Point2D(currX, currZ));
                } else if(mi == 0 || mi== mazeRows*2 || mj == 0 || mj == mazeCols*2 || (mi%2==0 && mj%2==0)) {
                    System.out.println("create wall");
                    Base_Cube cube = new Base_Cube("Maze Wall",cellDim, cellDim, cellDim);
                    cube.setMaterial(mazeMaterial);
                    maze_map_block.put(new Point2D(currX, currZ), cube);
                }
                currX += cellDim;
            }
            currZ += cellDim;
        }

        // add vertical and horizontal generated walls to the map
        // wall is vertical if xindex of two walls are equal
        // wall is horizontal if xindex of two walls are not equal

        // to find x index := 1 + [ 2 * ( mazeIndex % cols) ]
        // to find z index := 1 + [ 2 * ( mazeIndex / cols) ]
        for (Wall w : walls){

            int xindex1 = 1 + ( 2 * (w.cell1 % mazeCols) );
            int zindex1 = 1 + ( 2 * (w.cell1 / mazeCols) );
            int xindex2 = 1 + ( 2 * (w.cell2 % mazeCols) );
            int zindex2 = 1 + ( 2 * (w.cell2 / mazeCols) );
            double cellX;
            double cellZ;
            // wall separating vertical cells
            if ( xindex1 == xindex2 ) {
                // find vertical wall coordinate
                if ( zindex1 < zindex2 ) {
                    cellX = startingX + (xindex1 * cellDim);
                    cellZ = startingZ + ((zindex1 + 1) * cellDim);
                } else {
                    cellX = startingX + (xindex1 * cellDim);
                    cellZ = startingZ + ((zindex2 + 1) * cellDim);
                }
            }
            // wall separating horizontal cells
            else {
                // find horizontal wall coordinate
                if ( xindex1 < xindex2 ) {
                    cellX = startingX + ((xindex1 + 1) * cellDim);
                    cellZ = startingZ + (zindex1 * cellDim);
                } else {
                    cellX = startingX + ((xindex2 + 1) * cellDim);
                    cellZ = startingZ + (zindex1 * cellDim);
                }
            }
            cellX*=cellWidth;
            cellZ*=cellWidth;

            currZ = cellZ;

            for( i = 0 ; i < cellWidth ; i++){
                currX = cellX;
                for( j = 0 ; j < cellWidth ; j++) {
                    Base_Cube cube = new Base_Cube("Maze Wall", cellDim,cellDim,cellDim);
                    cube.setMaterial(mazeMaterial);
                    maze_map_block.put(new Point2D(currX,currZ), cube);
                    currX+=cellDim;
                }
                currZ+=cellDim;
            }
        }

        System.out.println(maze_map_block.size());
    }


    public void draw(){
        int i, j,mi,mj;
        double currX;
        double currZ;
        double cellX;
        double cellZ;
        int xindex1;
        int zindex1;
        int xindex2;
        int zindex2;

        // clear the spots where there are no walls generated
        for (Wall w : mazeGenerator.getDeletedWalls()){

            xindex1 = 1 + ( 2 * (w.cell1 % mazeCols) );
            zindex1 = 1 + ( 2 * (w.cell1 / mazeCols) );
            xindex2 = 1 + ( 2 * (w.cell2 % mazeCols) );
            zindex2 = 1 + ( 2 * (w.cell2 / mazeCols) );

            // wall separating vertical cells
            if ( xindex1 == xindex2 ) {
                // find vertical wall coordinate
                if ( zindex1 < zindex2 ) {
                    cellX = startingX + (xindex1 * cellDim);
                    cellZ = startingZ + ((zindex1 + 1) * cellDim);
                } else {
                    cellX = startingX + (xindex1 * cellDim);
                    cellZ = startingZ + ((zindex2 + 1) * cellDim);
                }
            }
            // wall separating horizontal cells
            else {
                // find horizontal wall coordinate
                if ( xindex1 < xindex2 ) {
                    cellX = startingX + ((xindex1 + 1) * cellDim);
                    cellZ = startingZ + (zindex1 * cellDim);
                } else {
                    cellX = startingX + ((xindex2 + 1) * cellDim);
                    cellZ = startingZ + (zindex1 * cellDim);
                }
            }
            cellX*=cellWidth;
            cellZ*=cellWidth;

            currZ = cellZ;

            for( i = 0 ; i < cellWidth ; i++){
                currX = cellX;
                for( j = 0 ; j < cellWidth ; j++) {
                    context.getEnvironment().clearSpot(new Point2D(currX,currZ));
                    currX+=cellDim;
                }
                currZ+=cellDim;
            }
        }



        // add known maze walls to the map
        currZ = startingZ;
        for( i = 0 ; i < (mazeRows*2 + 1) * cellWidth ; i++ ){
            currX =startingX;
            mi = i / cellWidth;
            for( j = 0 ; j < (mazeCols*2 + 1) * cellWidth ; j++ ) {
                mj = j/cellWidth;
                if( mi%2==1 && mj%2==1 ) {
                    context.getEnvironment().clearSpot(new Point2D(currX, currZ));

                }
                currX += cellDim;
            }
            currZ += cellDim;
        }


        for(i = 0; i < wall_height; i++){
            createBlockMap();
            for (Map.Entry<Point2D, StructureBuilder> point2DStructureBuilderEntry : maze_map_block.entrySet()) {
                context.getEnvironment().placeObject((Point2D) ((Map.Entry) point2DStructureBuilderEntry).getKey(), (StructureBuilder) ((Map.Entry) point2DStructureBuilderEntry).getValue(), true);
            }
        }
    }
}