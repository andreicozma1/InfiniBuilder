package app.structures.spawnables.maze;

import app.GameBuilder;
import app.algorithms.Edge;
import app.structures.spawnables.SpawnableStructureBuilder;
import app.structures.StructureProperties;
import app.structures.objects.BaseCube;
import app.structures.objects.BaseCylinder;
import app.structures.objects.BaseSphere;
import app.structures.objects.BaseStructure;
import app.utils.Log;
import javafx.geometry.Point2D;
import javafx.scene.paint.Material;


public class MazeUtil extends SpawnableStructureBuilder {
    private static final String TAG = "MazeUtil";
    public static Long GENERATOR_RANDOM_SEED = null;
    private int cellWidth;
    private final double cellDim;
    private int mazeRows;
    private int mazeCols;
    private final boolean isTrapped;
    private final Material mazeMaterial;
    private final int height;
    private Long seed;
    private MazeGenerator mazeGenerator;

    public MazeUtil(double cellDim,
                    int mazeRows,
                    int mazeCols,
                    int cellWidth,
                    int mazeHeight,
                    Material mazeMaterial) {
        Log.d(TAG, "CONSTRUCTOR");

        this.mazeMaterial = mazeMaterial;
        this.cellDim = cellDim;
        this.cellWidth = cellWidth;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.isTrapped = false;
        this.height = mazeHeight;
        this.seed = GENERATOR_RANDOM_SEED;
    }

    public MazeUtil(double cellDim,
                    int mazeRows,
                    int mazeCols,
                    int cellWidth,
                    int mazeHeight,
                    Material mazeMaterial,
                    Long seed) {
        Log.d(TAG, "CONSTRUCTOR");

        this.mazeMaterial = mazeMaterial;
        this.cellDim = cellDim;
        this.cellWidth = cellWidth;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.isTrapped = false;
        this.height = mazeHeight;
        this.seed = seed;
    }

    public MazeUtil(double cellDim,
                    int mazeRows,
                    int mazeCols,
                    int cellWidth,
                    int mazeHeight,
                    Material mazeMaterial,
                    Long seed,
                    boolean isTrapped) {
        Log.d(TAG, "CONSTRUCTOR");

        this.mazeMaterial = mazeMaterial;
        this.cellDim = cellDim;
        this.cellWidth = cellWidth;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.isTrapped = isTrapped;
        this.height = mazeHeight;
        this.seed = seed;
    }

    public MazeUtil(double cellDim,
                    int mazeRows,
                    int mazeCols,
                    int cellWidth,
                    int mazeHeight,
                    Material mazeMaterial,
                    boolean isTrapped) {
        Log.d(TAG, "CONSTRUCTOR");

        this.mazeMaterial = mazeMaterial;
        this.cellDim = cellDim;
        this.cellWidth = cellWidth;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.isTrapped = isTrapped;
        this.height = mazeHeight;
        this.seed = GENERATOR_RANDOM_SEED;
    }

    public void setCellWidth(int cellWidth) { this.cellWidth = cellWidth; }

    public int getCellWidth() {
        return cellWidth;
    }

    public double getCellDim() {
        return cellDim;
    }

    public int getMazeRows() {
        return mazeRows;
    }

    public void setMazeRows(int mazeRows) {
        this.mazeRows = mazeRows;
    }

    public int getMazeCols() {
        return mazeCols;
    }

    public void setMazeCols(int mazeCols) {
        this.mazeCols = mazeCols;
    }

    public boolean isTrapped() {
        return isTrapped;
    }

    public Material getMazeMaterial() {
        return mazeMaterial;
    }

    public int getHeight() {
        return height;
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    @Override
    public void build(GameBuilder context) {
        block_map.clear();

        if (this.seed == null) {
            mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, context.time_current);
        } else {
            mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, this.seed);
        }

        Point2D pos = context.getComponents().getPlayer().getPlayerPoint2D();
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
                        BaseStructure item;
                        switch (getProps().getPROPERTY_OBJECT_TYPE()){
                            case StructureProperties.OBJECT_TYPE_CYLINDER:
                                item = new BaseCylinder("Maze Wall",cellDim/2,cellDim);
                                break;
                            case StructureProperties.OBJECT_TYPE_SPHERE:
                                item = new BaseSphere("Maze Wall",(float)cellDim/2);
                                break;
                            default:
                                item = new BaseCube("Maze Wall", cellDim, cellDim, cellDim);
                                break;
                        }
                        item.getShape().setMaterial(mazeMaterial);
                        block_map.put(new Point2D(currX, currZ), item);
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
                        BaseStructure item;
                        switch (getProps().getPROPERTY_OBJECT_TYPE()){
                            case StructureProperties.OBJECT_TYPE_CYLINDER:
                                item = new BaseCylinder("Maze Wall",cellDim/2,cellDim);
                                break;
                            case StructureProperties.OBJECT_TYPE_SPHERE:
                                item = new BaseSphere("Maze Wall",(float)cellDim/2);
                                break;
                            default:
                                item = new BaseCube("Maze Wall", cellDim, cellDim, cellDim);
                                break;
                        }
                        item.getShape().setMaterial(mazeMaterial);
                        block_map.put(new Point2D(cellX + cellDim * j, cellZ + cellDim * i), item);
                    }
                }
            }

        }

        /*
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
*/
    }

}