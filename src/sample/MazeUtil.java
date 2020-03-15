package sample;

import java.util.List;

public class MazeUtil {
    private EnvironmentUtil environmentUtil;
    private double startingX;
    private double startingY;
    private double cellW;
    private double cellL;
    private double cellH;
    private int mazeRows;
    private int mazeCols;
    private long seed;
    private MazeGenerator mazeGenerator;
    private List<Wall> walls;

    public MazeUtil(  EnvironmentUtil environmentUtil,
                      double startingX,
                      double startingY,
                      double cellW,
                      double cellL,
                      double cellH,
                      int mazeRows,
                      int mazeCols,
                      long seed ){
        this.environmentUtil = environmentUtil;
        this.startingX = startingX;
        this.startingY = startingY;
        this.cellW = cellW;
        this.cellL = cellL;
        this.cellH = cellH;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        this.seed = seed;

        mazeGenerator = new MazeGenerator(this.mazeRows, this.mazeCols, this.seed);
        walls = mazeGenerator.getWalls();
    }

    public double getStartingX() { return startingX; }
    public double getStartingY() { return startingY; }
    public double getCellH() { return cellH; }
    public double getCellL() { return cellL; }
    public double getCellW() { return cellW; }
    public int getMazeCols() { return mazeCols; }
    public int getMazeRows() { return mazeRows; }
    public long getSeed() { return seed; }

}

