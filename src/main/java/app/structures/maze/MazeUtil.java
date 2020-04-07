package app.structures.maze;

import app.structures.SpawnableStructure2D;
import app.structures.StructureBuilder;
import javafx.geometry.Point2D;
import app.structures.objects.Base_Cube;
import app.GameBuilder;
import javafx.scene.paint.Material;
import org.apache.commons.collections4.MultiValuedMap;


public class MazeUtil implements SpawnableStructure2D {
    private int cellWidth;
    private double cellDim;
    private int mazeRows;
    private int mazeCols;
    private long seed;
    private boolean isTrapped;
    private boolean isRandomized;
    private Material mazeMaterial;
    private MazeGenerator mazeGenerator;

    public MazeUtil(  double cellDim,
                      int mazeRows,
                      int mazeCols,
                      int cellWidth,
                      Material mazeMaterial){
        this.mazeMaterial = mazeMaterial;
        this.cellDim = cellDim;
        this.cellWidth = cellWidth;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.seed = System.currentTimeMillis();
        this.isTrapped = false;
        this.isRandomized = true;

        mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, this.seed);
    }

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
        this.isRandomized = false;

        mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, this.seed);
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
        this.isRandomized = false;


        mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, this.seed);
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

    public boolean isRandomized() { return isRandomized; }

    public MultiValuedMap<Point2D, StructureBuilder> getMaze_map_block() {
        return block_map;
    }

    public void setMazeMaterial(Material mazeMaterial) {
        this.mazeMaterial = mazeMaterial;
    }

    public void setRandomized(boolean randomized) { isRandomized = randomized; }

    @Override
    public void build(GameBuilder context) {
        block_map.clear();

        if(isRandomized){
//            seed =  System.currentTimeMillis();
//            mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, this.seed);
//            mazeCols = mazeGenerator.getCols();
//            mazeRows = mazeGenerator.getRows();
        }

//        mazeGenerator.printWalls();

        System.out.println("Building Maze::: SEED = "+ seed);
        Point2D pos  = context.getPlayer().getPoint2D();
        double startingX = pos.getX() - (cellDim*cellWidth);
        double startingZ = pos.getY() - (cellDim*cellWidth);


        int i, j, mi, mj;
        double currX;
        double currZ;
        double cellX;
        double cellZ;
        int xindex1;
        int zindex1;
        int xindex2;
        int zindex2;





        // add known maze walls to the map
        currZ = startingZ;
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
//                    System.out.println("create outer wall");
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
        for (Wall w : mazeGenerator.getWalls()) {

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
            cellX = (cellX-startingX)*cellWidth + startingX;
            cellZ = (cellZ-startingZ)*cellWidth + startingZ;

            // loops here are for when the cells are thicker than one block
            for (i = 0; i < cellWidth; i++) {
                for (j = 0; j < cellWidth; j++) {
                    System.out.println("create wall");

                    Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
                    cube.getShape().setMaterial(mazeMaterial);
                    block_map.put(new Point2D(cellX + cellDim *j, cellZ + cellDim * i), cube);
                }
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

            cellX = (cellX-startingX)*cellWidth + startingX;
            cellZ = (cellZ-startingZ)*cellWidth + startingZ;

            currZ = cellZ;

            for (i = 0; i < cellWidth; i++) {
                currX = cellX;
                for (j = 0; j < cellWidth; j++) {
                    context.getEnvironment().clearSpot(new Point2D(cellX + cellDim *j, cellZ + cellDim * i));
                    currX += cellDim;
                }
                currZ += cellDim;
            }
        }


        // clear each empty cell where there could never be a wall
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