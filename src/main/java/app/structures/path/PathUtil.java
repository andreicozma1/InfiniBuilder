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

import java.util.List;


// eventually I want to add the ability to change the distance in between vertexes
public class PathUtil  implements SpawnableStructure {
    public final static String TAG = "PathUtil";

    private int pathRows;
    private int pathCols;
    private int pathWidth;
    private int edgeLength;
    private int startLoc;
    private int endLoc;
    private double cellDim;
    private boolean findShortestPath;
    private Long seed = null;
    private Material pathMaterial;
    private Material shortestPathMaterial;
    private List<Integer> path;
    private MazeGenerator mazeGenerator;
    private GraphUtil graph;

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
        startLoc = 0;
        endLoc = pathRows*pathCols - 1;
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
        startLoc = 0;
        endLoc = pathRows*pathCols - 1;
        findShortestPath = false;
    }

    public void setShortestPathMaterial(Material shortestPathMaterial) {
        this.findShortestPath = true;
        this.shortestPathMaterial = shortestPathMaterial;
    }

    public void setStartEndLocation(int startLoc, int endLoc){
        this.startLoc = startLoc;
        this.endLoc = endLoc;
    }

    @Override
    public void build(GameBuilder context) {
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
            path = graph.Dijkstra(startLoc,endLoc);
            if(path == null)findShortestPath = false;
            for (i = 0 ; i < path.size();i++){
                System.out.println(path.get(i));
            }
        }


        // draw known path sections
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
                    Base_Cube cube = new Base_Cube("Path Block", cellDim, cellDim, cellDim);
                    cube.getShape().setMaterial(pathMaterial);
                    block_map.put(new Point2D(cellX + cellDim * j, cellZ + cellDim * i), cube);
                }
            }

            // if a path has been found draw it
            if(findShortestPath){
                int currIndex = 0;
                int nextIndex = 1;
                i = (2*(startLoc%pathCols));   // big block coordinate
                j = (2*(startLoc/pathCols));
                mi = (i*pathWidth)+(pathWidth/2);   // individual cube coordinates
                mj = (j*pathWidth)+(pathWidth/2);
                int endi = ((2*(endLoc%pathCols))*pathWidth)+(pathWidth/2);
                int endj = ((2*(endLoc/pathCols))*pathWidth)+(pathWidth/2);
                currX = startingX+mi*cellDim;
                currZ = startingZ+mj*cellDim;
                while(true){
                    System.out.println("mi: "+mi+", mj: "+mj+", currI: "+currIndex+", nextI: "+nextIndex +", curr: " + path.get(currIndex)+", next: "+path.get(nextIndex));
                    // draw the cube
                    Base_Cube cube = new Base_Cube("Path Block", cellDim, cellDim, cellDim);
                    cube.getShape().setMaterial(shortestPathMaterial);
                    block_map.put(new Point2D(currX, currZ), cube);

                    // if reached the end of the path
                    if(mi==endj && mj==endi){
                        break;
                    }

                    // set currIndex and next Index
                    System.out.println("i: "+((2*(path.get(nextIndex)%pathCols))*pathWidth+(pathWidth/2))+",j: "+((2*(path.get(nextIndex)/pathCols))*pathWidth+(pathWidth/2)));
                    if( mj==((2*(path.get(nextIndex)%pathCols))*pathWidth)+(pathWidth/2) && mi==((2*(path.get(nextIndex)/pathCols))*pathWidth)+(pathWidth/2) ){
                        currIndex++;
                        nextIndex++;
                    }

                    // change the coordinates to draw next block
                    //up
                    if (path.get(currIndex)==path.get(nextIndex)-pathCols){
                        System.out.println("up");
                        currZ+=cellDim;
                        mi++;

                    }
                    //down
                    else if(path.get(currIndex)==path.get(nextIndex)+pathCols){
                        System.out.println("down");
                        currZ-=cellDim;
                        mi--;

                    }

                    //left
                    else if(path.get(currIndex)==path.get(nextIndex)-1){
                        System.out.println("right");
                        currX+=cellDim;
                        mj++;
                    }
                    //right
                    else if(path.get(currIndex)==path.get(nextIndex)+1){
                        System.out.println("left");
                        currX-=cellDim;
                        mj--;
                    }

                }



            }
        }
    }
}
