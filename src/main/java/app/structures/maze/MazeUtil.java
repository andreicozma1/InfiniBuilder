package app.structures.maze;

import app.algorithms.Edge;
import app.structures.SpawnableStructure;
import app.utils.Log;
import javafx.geometry.Point2D;
import app.structures.objects.Base_Cube;
import app.GameBuilder;
import javafx.scene.paint.Material;


public class MazeUtil implements SpawnableStructure {
    private static final String TAG = "MazeUtil";

    private final int cellWidth;
    private final double cellDim;
    private final int mazeRows;
    private final int mazeCols;
    private Long seed = null;
    private final boolean isTrapped;
    private final Material mazeMaterial;
    private MazeGenerator mazeGenerator;
    private final int height;

    public static Long GENERATOR_RANDOM_SEED = null;

    public MazeUtil(double cellDim,
                    int mazeRows,
                    int mazeCols,
                    int mazeHeight,
                    int cellWidth,
                    Material mazeMaterial) {
        Log.p(TAG,"CONSTRUCTOR");

        this.mazeMaterial = mazeMaterial;
        this.cellDim = cellDim;
        this.cellWidth = cellWidth;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.isTrapped = false;
        this.height = mazeHeight;
    }

    public MazeUtil(double cellDim,
                    int mazeRows,
                    int mazeCols,
                    int cellWidth,
                    int mazeHeight,
                    Material mazeMaterial,
                    Long seed) {
        Log.p(TAG,"CONSTRUCTOR");

        this.mazeMaterial = mazeMaterial;
        this.cellDim = cellDim;
        this.cellWidth = cellWidth;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.seed = seed;
        this.isTrapped = false;
        this.height = mazeHeight;
    }

    public MazeUtil(double cellDim,
                    int mazeRows,
                    int mazeCols,
                    int cellWidth,
                    int mazeHeight,
                    Material mazeMaterial,
                    Long seed,
                    boolean isTrapped) {
        Log.p(TAG,"CONSTRUCTOR");

        this.mazeMaterial = mazeMaterial;
        this.cellDim = cellDim;
        this.cellWidth = cellWidth;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.seed = seed;
        this.isTrapped = isTrapped;
        this.height = mazeHeight;

    }


    @Override
    public void build(GameBuilder context) {
        block_map.clear();

        if (this.seed == null) {
            mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, context.time_current);
        } else {
            mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, this.seed);
        }


        Point2D pos = context.getComponents().getPlayer().getPoint2D();
        double startingX = pos.getX() - (cellDim * cellWidth);
        double startingZ = pos.getY() - (cellDim * cellWidth);


        int i, j, mi, mj;
        double currX;
        double currZ;
        double cellX;
        double cellZ;
        int xindex1;
        int zindex1;
        int xindex2;
        int zindex2;

        for (int h = 0; h < height; h++) {
            // add known maze walls to the map
            currZ = startingZ;
            for (i = 0; i < (mazeRows * 2 + 1) * cellWidth; i++) {
                currX = startingX;
                mi = i / cellWidth;
                for (j = 0; j < (mazeCols * 2 + 1) * cellWidth; j++) {
                    mj = j / cellWidth;
                    if (!isTrapped && ((mi == 1 && mj == 0))) {
//                        context.getComponents().getEnvironment().clearSpot(new Point2D(currX, currZ));
                    } else if (mi == mazeRows * 2 - 1 && mj == mazeCols * 2) {
//                        context.getComponents().getEnvironment().clearSpot(new Point2D(currX, currZ));
                    } else if (mi == 0 || mi == mazeRows * 2 || mj == 0 || mj == mazeCols * 2 || (mi % 2 == 0 && mj % 2 == 0)) {
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
            for (Edge w : mazeGenerator.getWalls()) {

                xindex1 = 1 + (2 * (w.v1 % mazeCols));
                zindex1 = 1 + (2 * (w.v1 / mazeCols));
                xindex2 = 1 + (2 * (w.v2 % mazeCols));
                zindex2 = 1 + (2 * (w.v2 / mazeCols));
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
                cellX = (cellX - startingX) * cellWidth + startingX;
                cellZ = (cellZ - startingZ) * cellWidth + startingZ;

                // loops here are for when the cells are thicker than one block
                for (i = 0; i < cellWidth; i++) {
                    for (j = 0; j < cellWidth; j++) {
                        Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
                        cube.getShape().setMaterial(mazeMaterial);
                        block_map.put(new Point2D(cellX + cellDim * j, cellZ + cellDim * i), cube);
                    }
                }
            }

        }

        // clear the spots where there are no walls generated
        for (Edge w : mazeGenerator.getDeletedWalls()) {

            xindex1 = 1 + (2 * (w.v1 % mazeCols));
            zindex1 = 1 + (2 * (w.v1 / mazeCols));
            xindex2 = 1 + (2 * (w.v2 % mazeCols));
            zindex2 = 1 + (2 * (w.v2 / mazeCols));

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

            cellX = (cellX - startingX) * cellWidth + startingX;
            cellZ = (cellZ - startingZ) * cellWidth + startingZ;

            currZ = cellZ;

            for (i = 0; i < cellWidth; i++) {
                currX = cellX;
                for (j = 0; j < cellWidth; j++) {
//                    context.getComponents().getEnvironment().clearSpot(new Point2D(cellX + cellDim * j, cellZ + cellDim * i));
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
//                    context.getComponents().getEnvironment().clearSpot(new Point2D(currX, currZ));

                }
                currX += cellDim;
            }
            currZ += cellDim;
        }

    }
}