package app.structures.maze;

import app.structures.SpawnableStructure2D;
import app.structures.SpawnableStructure3D;
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

public class MazeUtil implements SpawnableStructure2D {
    private int cellWidth;
    private double cellDim;
    private int mazeRows;
    private int mazeCols;
    private long seed;
    private boolean isTrapped;
    private Material mazeMaterial;
    private MazeGenerator mazeGenerator;
    private List<Wall> walls;


    public MazeUtil(  double cellDim,
                      int mazeRows,
                      int mazeCols,
                      int cellWidth,
                      Material mazeMaterial,
                      long seed ){
        this.mazeMaterial = mazeMaterial;
        this.cellDim = cellDim;
        this.cellWidth = cellWidth;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.seed = seed;
        this.isTrapped = false;

        mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, this.seed);
        walls = mazeGenerator.getWalls();
    }

    public MazeUtil(  double cellDim,
                      int mazeRows,
                      int mazeCols,
                      int cellWidth,
                      Material mazeMaterial,
                      long seed ,
                      boolean isTrapped){
        this.mazeMaterial = mazeMaterial;
        this.cellDim = cellDim;
        this.cellWidth = cellWidth;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.seed = seed;
        this.isTrapped = isTrapped;

        mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, this.seed);
        walls = mazeGenerator.getWalls();
    }

    public double getCellDim() {
        return cellDim;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public int getMazeCols() {
        return mazeCols;
    }

    public int getMazeRows() {
        return mazeRows;
    }

    public long getSeed() {
        return seed;
    }

    public Map<Point2D, StructureBuilder> getMaze_map_block() {
        return block_map;
    }

    public void setMazeMaterial(Material mazeMaterial) {
        this.mazeMaterial = mazeMaterial;
    }

    @Override
    public void build(GameBuilder context) {

        System.out.println("Building");
        Point2D pos  = context.getPlayer().getPoint2D();
        double startingX = pos.getX() - (cellDim*cellWidth);
        double startingZ = pos.getY() - (cellDim*cellWidth);


        int i, j, mi, mj;
        double currX;
        double currZ = startingZ;
        double cellX;
        double cellZ;
        int xindex1;
        int zindex1;
        int xindex2;
        int zindex2;



        block_map.clear();


        // add known maze walls to the map
        for (i = 0; i < (mazeRows * 2 + 1) * cellWidth; i++) {
            currX = startingX;
            mi = i / cellWidth;
            for (j = 0; j < (mazeCols * 2 + 1) * cellWidth; j++) {
                mj = j / cellWidth;
                if ( !isTrapped &&((mi == 1 && mj == 0) )) {
                    context.getEnvironment().clearSpot(new Point2D(currX, currZ));
                }else if (mi == mazeRows * 2 - 1 && mj == mazeCols * 2){
                    context.getEnvironment().clearSpot(new Point2D(currX, currZ));
                } else if (mi == 0 || mi == mazeRows * 2 || mj == 0 || mj == mazeCols * 2 || (mi % 2 == 0 && mj % 2 == 0)) {
                    System.out.println("create wall");
                    Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
                    cube.getShape().setMaterial(mazeMaterial);
                    block_map.put(new Point2D(currX, currZ), cube);
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
        for (Wall w : walls) {

            xindex1 = 1 + (2 * (w.cell1 % mazeCols));
            zindex1 = 1 + (2 * (w.cell1 / mazeCols));
            xindex2 = 1 + (2 * (w.cell2 % mazeCols));
            zindex2 = 1 + (2 * (w.cell2 / mazeCols));
            // wall separating vertical cells
            if (xindex1 == xindex2) {
                // find vertical wall coordinate
                if (zindex1 < zindex2) {
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
                if (xindex1 < xindex2) {
                    cellX = startingX + ((xindex1 + 1) * cellDim);
                    cellZ = startingZ + (zindex1 * cellDim);
                } else {
                    cellX = startingX + ((xindex2 + 1) * cellDim);
                    cellZ = startingZ + (zindex1 * cellDim);
                }
            }
            cellX = (cellX-cellDim)*cellWidth + startingX;
            cellZ = (cellZ-cellDim)*cellWidth+ startingZ;


            currZ = cellZ;

            for (i = 0; i < cellWidth; i++) {
                currX = cellX;
                for (j = 0; j < cellWidth; j++) {
                    Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
                    cube.getShape().setMaterial(mazeMaterial);
                    block_map.put(new Point2D(currX, currZ), cube);
                    currX += cellDim;
                }
                currZ += cellDim;
            }
        }

//        System.out.println(block_map.size());


        // clear the spots where there are no walls generated
        for (Wall w : mazeGenerator.getDeletedWalls()) {

            xindex1 = 1 + (2 * (w.cell1 % mazeCols));
            zindex1 = 1 + (2 * (w.cell1 / mazeCols));
            xindex2 = 1 + (2 * (w.cell2 % mazeCols));
            zindex2 = 1 + (2 * (w.cell2 / mazeCols));

            // wall separating vertical cells
            if (xindex1 == xindex2) {
                // find vertical wall coordinate
                if (zindex1 < zindex2) {
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
                if (xindex1 < xindex2) {
                    cellX = startingX + ((xindex1 + 1) * cellDim);
                    cellZ = startingZ + (zindex1 * cellDim);
                } else {
                    cellX = startingX + ((xindex2 + 1) * cellDim);
                    cellZ = startingZ + (zindex1 * cellDim);
                }
            }
            cellX = (cellX-cellDim)*cellWidth + startingX;
            cellZ = (cellZ-cellDim)*cellWidth+ startingZ;

            currZ = cellZ;

            for (i = 0; i < cellWidth; i++) {
                currX = cellX;
                for (j = 0; j < cellWidth; j++) {
                    context.getEnvironment().clearSpot(new Point2D(currX, currZ));
                    currX += cellDim;
                }
                currZ += cellDim;
            }
        }


        // add known maze walls to the map
        currZ = startingZ;
        for (i = 0; i < (mazeRows * 2 + 1) * cellWidth; i++) {
            currX = startingX;
            mi = i / cellWidth;
            for (j = 0; j < (mazeCols * 2 + 1) * cellWidth; j++) {
                mj = j / cellWidth;
                if (mi % 2 == 1 && mj % 2 == 1) {
                    context.getEnvironment().clearSpot(new Point2D(currX, currZ));

                }
                currX += cellDim;
            }
            currZ += cellDim;
        }

    }
}