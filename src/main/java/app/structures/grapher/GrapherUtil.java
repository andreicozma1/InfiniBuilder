package app.structures.grapher;

import app.GameBuilder;
import app.structures.SpawnableStructure;
import app.structures.objects.Base_Cube;
import javafx.geometry.Point2D;
import javafx.scene.paint.Material;

public class GrapherUtil implements SpawnableStructure {
    private double cellDim;
    private int xAxisSize;
    private int yAxisSize;
    private double xAxisScalePerBlock;
    private double yAxisScalePerBlock;
    private Function function;
    private Material axisMaterial;
    private Material functionMaterial;
    public GrapherUtil(double cellDim,
                       int xAxisSize,
                       int yAxisSize,
                       double xAxisScalePerBlock,
                       double yAxisScalePerBlock,
                       Function function,
                       Material axisMaterial,
                       Material functionMaterial){
        this.cellDim = cellDim;
        this.xAxisSize = xAxisSize;
        this.yAxisSize = yAxisSize;
        this.yAxisScalePerBlock = yAxisScalePerBlock;
        this.xAxisScalePerBlock = xAxisScalePerBlock;
        this.function = function;
        this.axisMaterial = axisMaterial;
        this.functionMaterial = functionMaterial;
    }

    public int getXAxisSize() { return xAxisSize; }
    public int getYAxisSize() { return yAxisSize; }
    public double getXAxisScale() { return xAxisScalePerBlock; }
    public double getYAxisScale() { return yAxisScalePerBlock; }
    public Function getFunction() { return function; }

    public void setXAxisSize(int xAxisSize) { this.xAxisSize = xAxisSize; }
    public void setYAxisSize(int yAxisSize) { this.yAxisSize = yAxisSize; }
    public void setXAxisScale(double xAxisScale) { this.xAxisScalePerBlock = xAxisScale; }
    public void setYAxisScale(double yAxisScale) { this.yAxisScalePerBlock = yAxisScale; }
    public void setFunction(Function function) { this.function = function; }

    @Override
    public void build(GameBuilder context) {
        Point2D pos = context.getComponents().getPlayer().getPoint2D();
        double startingX = pos.getX();
        double startingZ = pos.getY();
        double currZ;
        double currX;
        int i,j;

        double x, y;
        block_map.clear();



        for (int h = 0; h < 1; h++) {
//            // draw axis and box around graph
//            currZ = startingZ - yAxisSize*cellDim;
//            for(i = -yAxisSize-1; i <= yAxisSize+1;i++) {
//                currX = startingX - xAxisSize*cellDim;
//                for(j = -xAxisSize-1; j <= xAxisSize+1;j++) {
//                    if(     j == 0 ||                       // y axis
//                            i == 0 ||                       // x axis
//                            j == -xAxisSize-1 ||            // left side box
//                            j == xAxisSize+1 ||               // right side box
//                            i == -yAxisSize-1  ||           // top side box
//                            i == yAxisSize+1                  // bottom side box
//                    ) {
//                        Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
//                        cube.getShape().setMaterial(axisMaterial);
//                        block_map.put(new Point2D(currX, currZ), cube);
//                    }
//                    currX += cellDim;
//                }
//                currZ+=cellDim;
//            }



            currZ = startingZ - yAxisSize*cellDim;
            for(i = -yAxisSize; i < yAxisSize;i++) {
                Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
                cube.getShape().setMaterial(axisMaterial);
                block_map.put(new Point2D(startingX, currZ), cube);
                currZ+=cellDim;
            }
            // draw x axis
            currX = startingX - xAxisSize*cellDim;
            for(i = -xAxisSize; i < xAxisSize;i++) {
                Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
                cube.getShape().setMaterial(axisMaterial);
                block_map.put(new Point2D(currX, startingZ), cube);
                currX+=cellDim;
            }
            // draw function
            //draw graph
            for(x = -xAxisSize*xAxisScalePerBlock; x <= xAxisSize*xAxisScalePerBlock ; x+=xAxisScalePerBlock){
                y = function.compute(x);
                System.out.println("F("+x+") = "+y);
                Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
                cube.getShape().setMaterial(functionMaterial);
                block_map.put(new Point2D((x/xAxisScalePerBlock)*cellDim+startingX,  y*cellDim+startingZ), cube);

            }
        }
    }
}