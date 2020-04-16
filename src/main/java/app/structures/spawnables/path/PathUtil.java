package app.structures.spawnables.path;


import app.GameBuilder;
import app.algorithms.Edge;
import app.algorithms.GraphUtil;
import app.structures.spawnables.SpawnableStructureBuilder;
import app.structures.StructureProperties;
import app.structures.spawnables.maze.MazeGenerator;
import app.structures.objects.BaseCube;
import app.structures.objects.BaseCylinder;
import app.structures.objects.BaseSphere;
import app.structures.objects.BaseStructure;
import app.utils.Log;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;

import java.util.List;


/**
 * Path util will draw a random set of paths and then load the paths into
 * an adjacency list and find the shortest path through the path.
 */
public class PathUtil extends SpawnableStructureBuilder {
    // global variables
    public final static String TAG = "PathUtil";
    private final int pathRows;
    private final int pathCols;
    private final int pathWidth;
    private final double cellDim;
    private final Material pathMaterial;
    private int startLoc;
    private int endLoc;
    private boolean findShortestPath;
    private Long seed = null;
    private Color startColor;
    private Color endColor;
    private List<Integer> path;
    private MazeGenerator mazeGenerator;
    private GraphUtil graph;

    // CONSTRUCTORS
    // one takes a seed the other will generate a random path based on the time

    public PathUtil(double cellDim,
                    int pathRows,
                    int pathCols,
                    int pathWidth,
                    Material pathMaterial) {
        Log.d(TAG, "CONSTRUCTOR");

        this.cellDim = cellDim;
        this.pathRows = pathRows;
        this.pathCols = pathCols;
        this.pathWidth = pathWidth;
        this.pathMaterial = pathMaterial;
        startLoc = 0;
        endLoc = pathRows * pathCols - 1;
        findShortestPath = false;
    }

    public PathUtil(double cellDim,
                    int pathRows,
                    int pathCols,
                    int pathWidth,
                    Material pathMaterial,
                    Long seed) {
        Log.d(TAG, "CONSTRUCTOR");

        this.cellDim = cellDim;
        this.pathRows = pathRows;
        this.pathCols = pathCols;
        this.pathWidth = pathWidth;
        this.pathMaterial = pathMaterial;
        this.seed = seed;
        startLoc = 0;
        endLoc = pathRows * pathCols - 1;
        findShortestPath = false;
    }

    // this will enable the shortest path to be drawn over the maze
    public void setShortestPathMaterial(Color startColor, Color endColor) {
        this.findShortestPath = true;
        this.startColor = startColor;
        this.endColor = endColor;
    }

    // this will change the starting and ending location of the shortest path
    public void setStartEndLocation(int startLoc, int endLoc) {
        this.startLoc = startLoc;
        this.endLoc = endLoc;
    }

    // this will draw the structure into the environment at the players location
    @Override
    public void build(GameBuilder context) {
        // declare variables
        Point2D pos = context.getComponents().getPlayer().getPlayerPoint2D();
        double startingX = pos.getX();
        double startingZ = pos.getY();
        int i, j, mi, mj;
        int currIndex;
        int nextIndex;
        double cellX;
        double cellZ;
        int xindex1;
        int zindex1;
        int xindex2;
        int zindex2;
        double currX;
        double currZ;

        // clear the block map
        block_map.clear();

        // randomizes the seed if the user wants it to be randomized
        if (this.seed == null) {
            mazeGenerator = new MazeGenerator(this.pathRows, this.pathCols, context.time_current);
        } else {
            mazeGenerator = new MazeGenerator(this.pathRows, this.pathCols, this.seed);
        }

        // if the user wants to show the shortest path on the path
        if (this.findShortestPath) {
            // read the deleted edges into the graph
            graph = new GraphUtil(pathRows * pathCols);
            for (Edge w : mazeGenerator.getDeletedWalls()) {
                graph.addEdge(new Edge(w.v1, w.v2));
                graph.addEdge(new Edge(w.v2, w.v1));
            }
            path = graph.Dijkstra(startLoc, endLoc);
            if (path == null) findShortestPath = false;
        }


        // draw known path sections
        currZ = startingZ;
        for (i = 0; i < (pathRows * 2 - 1) * pathWidth; i++) {
            mi = i / pathWidth;
            currX = startingX;
            for (j = 0; j < (pathCols * 2 - 1) * pathWidth; j++) {
                mj = j / pathWidth;
                if (mi % 2 == 0 && mj % 2 == 0) {
                    // place down the correct base structure
                    BaseStructure item;
                    switch (getProps().getPROPERTY_OBJECT_TYPE()){
                        case StructureProperties.OBJECT_TYPE_CYLINDER:
                            item = new BaseCylinder("Path",cellDim/2,cellDim);
                            break;
                        case StructureProperties.OBJECT_TYPE_SPHERE:
                            item = new BaseSphere("Path",(float)cellDim/2);
                            break;
                        default:
                            item = new BaseCube("Path", cellDim, cellDim, cellDim);
                            break;
                    }
                    item.getShape().setMaterial(pathMaterial);
                    block_map.put(new Point2D(currX, currZ), item);
                }
                currX += cellDim;

            }
            currZ += cellDim;
        }

        //draw generated path sections
        for (Edge w : mazeGenerator.getDeletedWalls()) {

            xindex1 = (2 * (w.v1 % pathCols));
            zindex1 = (2 * (w.v1 / pathCols));
            xindex2 = (2 * (w.v2 % pathCols));
            zindex2 = (2 * (w.v2 / pathCols));
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
            cellX = (cellX - startingX) * pathWidth + startingX;
            cellZ = (cellZ - startingZ) * pathWidth + startingZ;

            // loops here are for when the cells are thicker than one block
            for (i = 0; i < pathWidth; i++) {
                for (j = 0; j < pathWidth; j++) {
                    // place down the correct base structure
                    BaseStructure item;
                    switch (getProps().getPROPERTY_OBJECT_TYPE()){
                        case StructureProperties.OBJECT_TYPE_CYLINDER:
                            item = new BaseCylinder("Path",cellDim/2,cellDim);
                            break;
                        case StructureProperties.OBJECT_TYPE_SPHERE:
                            item = new BaseSphere("Path",(float)cellDim/2);
                            break;
                        default:
                            item = new BaseCube("Path", cellDim, cellDim, cellDim);
                            break;
                    }
                    item.getShape().setMaterial(pathMaterial);
                    block_map.put(new Point2D(cellX + cellDim * j, cellZ + cellDim * i), item);
                }
            }
        }


        // if a path has been found draw it
        if (findShortestPath) {

            currIndex = 0;
            nextIndex = 1;
            mi = ((2 * (startLoc % pathCols)) * pathWidth) + (pathWidth / 2);
            mj = ((2 * (startLoc / pathCols)) * pathWidth) + (pathWidth / 2);
            i = ((2 * (endLoc % pathCols)) * pathWidth) + (pathWidth / 2);
            j = ((2 * (endLoc / pathCols)) * pathWidth) + (pathWidth / 2);
            currX = startingX + mi * cellDim;
            currZ = startingZ + mj * cellDim;
            while (true) {
                // place down the correct base structure
                BaseStructure item;
                switch (getProps().getPROPERTY_OBJECT_TYPE()){
                    case StructureProperties.OBJECT_TYPE_CYLINDER:
                        item = new BaseCylinder("Path",cellDim/2,cellDim);
                        break;
                    case StructureProperties.OBJECT_TYPE_SPHERE:
                        item = new BaseSphere("Path",(float)cellDim/2);
                        break;
                    default:
                        item = new BaseCube("Path", cellDim, cellDim, cellDim);
                        break;
                }
                PhongMaterial shortestPathMaterial= new PhongMaterial();
                Color interpColor = startColor.interpolate(endColor,((double)currIndex)/path.size());
                shortestPathMaterial.setDiffuseColor(interpColor);
                shortestPathMaterial.setSpecularColor(interpColor);
                item.getShape().setMaterial(shortestPathMaterial);
                block_map.put(new Point2D(currX, currZ), item);

                // if reached the end of the path
                if (mj == j && mi == i) {
                    break;
                }

                // set currIndex and next Index
                if (mj == ((2 * (path.get(nextIndex) % pathCols)) * pathWidth) + (pathWidth / 2) && mi == ((2 * (path.get(nextIndex) / pathCols)) * pathWidth) + (pathWidth / 2)) {
                    currIndex++;
                    nextIndex++;
                }

                // change the coordinates to draw next block
                //up
                if (path.get(currIndex) == path.get(nextIndex) - pathCols) {
                    Log.d(TAG,"up");
                    currZ += cellDim;
                    mi++;

                }
                //down
                else if (path.get(currIndex) == path.get(nextIndex) + pathCols) {
                    Log.d(TAG,"down");
                    currZ -= cellDim;
                    mi--;

                }
                //left
                else if (path.get(currIndex) == path.get(nextIndex) - 1) {
                    Log.d(TAG,"right");
                    currX += cellDim;
                    mj++;
                }
                //right
                else if (path.get(currIndex) == path.get(nextIndex) + 1) {
                    Log.d(TAG,"left");
                    currX -= cellDim;
                    mj--;
                }
            }

        }

    }


}
