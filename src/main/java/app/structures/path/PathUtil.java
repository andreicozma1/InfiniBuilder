package app.structures.path;


import app.GameBuilder;
import app.algorithms.Edge;
import app.algorithms.GraphUtil;
import app.structures.SpawnableStructure;
import app.structures.maze.MazeGenerator;
import app.structures.objects.Base_Cube;
import app.utils.Log;
import javafx.geometry.Point2D;
import javafx.scene.paint.Material;



// eventually I want to add the ability to change the distance in between vertexes
public class PathUtil  implements SpawnableStructure {
    public final static String TAG = "PathUtil";

    public int pathRows;
    public int pathCols;
    public int pathWidth;
    public int edgeLength;
    public double cellDim;
    public Long seed = null;
    public boolean findShortestPath;
    public Material pathMaterial;
    public Material shortestPathMaterial;
    public MazeGenerator mazeGenerator;
    public GraphUtil graph;

    public PathUtil(double cellDim,
                    int pathRows,
                    int pathCols,
                    int pathWidth,
                    int edgeLength,
                    Material pathMaterial) {
        Log.p(TAG, "CONSTRUCTOR");

        this.cellDim = cellDim;
        this.pathRows = pathRows;
        this.pathCols = pathCols;
        this.pathWidth = pathWidth;
        this.edgeLength = edgeLength;
        this.pathMaterial = pathMaterial;
        findShortestPath = false;
    }


    public PathUtil(double cellDim,
                    int pathRows,
                    int pathCols,
                    int pathWidth,
                    int edgeLength,
                    Material pathMaterial,
                    Long seed) {
        Log.p(TAG, "CONSTRUCTOR");

        this.cellDim = cellDim;
        this.pathRows = pathRows;
        this.pathCols = pathCols;
        this.pathWidth = pathWidth;
        this.edgeLength = edgeLength;
        this.pathMaterial = pathMaterial;
        this.seed = seed;
        findShortestPath = false;
    }

    public void setShortestPathMaterial(Material shortestPathMaterial) {
        this.findShortestPath = true;
        this.shortestPathMaterial = shortestPathMaterial;
    }

    @Override
    public void build(GameBuilder context) {
        block_map.clear();
        // randomizes the seed if the user wants it to be randomized
        if (this.seed == null) {
            mazeGenerator = new MazeGenerator(this.pathRows, this.pathCols, System.currentTimeMillis());
        } else {
            mazeGenerator = new MazeGenerator(this.pathRows, this.pathCols, this.seed);
        }

        // if the user wants to show the shortest path on the path
        if(this.findShortestPath) {
            // read the deleted edges into the graph
            graph = new GraphUtil(pathRows*pathCols);
            for (Edge w : mazeGenerator.getDeletedWalls()) {
                graph.addEdge(new Edge(w.v1,w.v2));
                graph.addEdge(new Edge(w.v2,w.v1));
            }
            graph.print();
        }

        Point2D pos = context.getComponents().getPlayer().getPoint2D();
        double startingX = pos.getX();
        double startingZ = pos.getY();

        int i, j, mi, mj;
        double cellX;
        double cellZ;
        int xindex1;
        int zindex1;
        int xindex2;
        int zindex2;
        double currX;
        double currZ;

        currZ = startingZ;
        for (i = 0; i < (pathRows * 2 - 1) * pathWidth; i++) {
            mi = i / pathWidth;
            currX = startingX;
            for (j = 0; j < (pathCols * 2 - 1) * pathWidth; j++) {
                mj = j / pathWidth;
                if (mi % 2 == 0 && mj % 2 == 0) {
                    Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
                    cube.getShape().setMaterial(pathMaterial);
                    System.out.println(currX+" "+currZ);
                    block_map.put(new Point2D(startingX + cellDim*j , startingZ + cellDim *i), cube);
                }
                currX += cellDim;

            }
            currZ += cellDim;
        }

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
                    Base_Cube cube = new Base_Cube("Path Block", cellDim, cellDim, cellDim);
                    cube.getShape().setMaterial(pathMaterial);
                    block_map.put(new Point2D(cellX + cellDim * j, cellZ + cellDim * i), cube);
                }
            }
        }
    }
}
