package sample;

import javafx.scene.Group;

import java.util.List;

public class MazeUtil {
    private WindowUtil context;
    private double startingX;
    private double startingZ;
    private double cellW;
    private double cellL;
    private double cellH;
    private int mazeRows;
    private int mazeCols;
    private long seed;
    private Group mazeGroup;
    private MazeGenerator mazeGenerator;
    private List<Wall> walls;

    public MazeUtil(  WindowUtil context,
                      double startingX,
                      double startingZ,
                      double cellW,
                      double cellL,
                      double cellH,
                      int mazeRows,
                      int mazeCols,
                      long seed ){
        this.context = context;
        this.startingX = startingX;
        this.startingZ = startingZ;
        this.cellW = cellW;
        this.cellL = cellL;
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
    public double getCellL() { return cellL; }
    public double getCellW() { return cellW; }
    public int getMazeCols() { return mazeCols; }
    public int getMazeRows() { return mazeRows; }
    public long getSeed() { return seed; }
    public boolean inFrame( double x, double y){return true;}

    public void draw(){
        int i, j;
        double currX = startingX;
        double currZ = startingZ;
        for( i = 0 ; i < mazeRows*2 + 1 ; i++ ){
            currX =startingX;
            for( j = 0 ; j < mazeCols*2 + 1 ; j++ ) {
                if(inFrame(currX,currZ)){
                    if(i == 0 || i== mazeRows*2 || j == 0 || j == mazeCols*2 || (i%2==1 && j%2==1)){
                        DrawCube cube = new DrawCube(cellW, cellH, cellL);
                        cube.setMaterial(MaterialsUtil.stone);
                        cube.setBoxX(currX);
                        cube.setBoxZ(currZ);
//                        context.getPlayer().placeObject(cube, true);
                    } else {
//                        Wall tmp = new Wall();
                    }

                }
                currX += cellW;
            }
            currZ += cellL;

        }
    }
}

