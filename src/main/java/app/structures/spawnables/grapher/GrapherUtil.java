package app.structures.spawnables.grapher;

import app.GameBuilder;
import app.structures.SpawnableStructureBuilder;
import app.structures.objects.BaseCube;
import app.utils.Log;
import app.utils.ResourcesUtil;
import javafx.geometry.Point2D;
import javafx.scene.paint.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GrapherUtil extends SpawnableStructureBuilder {

    private static final String TAG = "GrapherUtil";

    private final double cellDim;
    private final List<Function> functions = new ArrayList<>();
    private final List<Material> functMats = new ArrayList<Material>(
            Arrays.asList(ResourcesUtil.red, ResourcesUtil.blue, ResourcesUtil.green, ResourcesUtil.purple));
    private final Material axisMaterial;
    private int xAxisSize;
    private int yAxisSize;
    private double xAxisScalePerBlock;
    private double yAxisScalePerBlock;



    public GrapherUtil(double cellDim,
                       int xAxisSize,
                       int yAxisSize,
                       double xAxisScalePerBlock,
                       double yAxisScalePerBlock,
                       Material axisMaterial) {
        this.cellDim = cellDim;
        this.xAxisSize = xAxisSize;
        this.yAxisSize = yAxisSize;
        this.yAxisScalePerBlock = yAxisScalePerBlock;
        this.xAxisScalePerBlock = xAxisScalePerBlock;
        this.axisMaterial = axisMaterial;
    }

    public GrapherUtil(double cellDim,
                       int xAxisSize,
                       int yAxisSize,
                       double xAxisScalePerBlock,
                       double yAxisScalePerBlock,
                       Function function,
                       Material axisMaterial) {
        this.cellDim = cellDim;
        this.xAxisSize = xAxisSize;
        this.yAxisSize = yAxisSize;
        this.yAxisScalePerBlock = yAxisScalePerBlock;
        this.xAxisScalePerBlock = xAxisScalePerBlock;
        functions.add(function);
        this.axisMaterial = axisMaterial;
    }

    public int getXAxisSize() {
        return xAxisSize;
    }

    public void setXAxisSize(int xAxisSize) {
        this.xAxisSize = xAxisSize;
    }

    public int getYAxisSize() {
        return yAxisSize;
    }

    public void setYAxisSize(int yAxisSize) {
        this.yAxisSize = yAxisSize;
    }

    public double getXAxisScale() {
        return xAxisScalePerBlock;
    }

    public void setXAxisScale(double xAxisScale) {
        this.xAxisScalePerBlock = xAxisScale;
    }

    public double getYAxisScale() {
        return yAxisScalePerBlock;
    }

    public void setYAxisScale(double yAxisScale) {
        this.yAxisScalePerBlock = yAxisScale;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public void addFunction(Function function) {
        if (functions.size() != functMats.size()) {
            functions.add(function);
        }
    }

    @Override
    public void build(GameBuilder context) {
        Point2D pos = context.getComponents().getPlayer().getPlayerPoint2D();
        double startingX = pos.getX();
        double startingZ = pos.getY();
        double currZ;
        double currX;
        int i, j;

        double x, y;
        block_map.clear();


        currZ = startingZ - yAxisSize * cellDim;
        for (i = -yAxisSize; i < yAxisSize; i++) {
            BaseCube cube = new BaseCube("Maze Wall", cellDim, cellDim, cellDim);
            cube.getShape().setMaterial(axisMaterial);
            block_map.put(new Point2D(startingX, currZ), cube);
            currZ += cellDim;
        }

        // draw x axis
        currX = startingX - xAxisSize * cellDim;
        for (i = -xAxisSize; i < xAxisSize; i++) {
            if (i != 0) {
                BaseCube cube = new BaseCube("Maze Wall", cellDim, cellDim, cellDim);
                cube.getShape().setMaterial(axisMaterial);
                block_map.put(new Point2D(currX, startingZ), cube);
            }
            currX += cellDim;
        }

        //draw graph
        for (i = 0; i < functions.size(); i++) {
            for (x = -xAxisSize * xAxisScalePerBlock; x <= xAxisSize * xAxisScalePerBlock; x += xAxisScalePerBlock) {
                y = functions.get(i).compute(x);
                Log.d(TAG,"F(" + x + ") = " + y);
                BaseCube cube = new BaseCube("Maze Wall", cellDim, cellDim, cellDim);
                cube.getShape().setMaterial(functMats.get(i));
                block_map.put(new Point2D((x / xAxisScalePerBlock) * cellDim + startingX, y * cellDim + startingZ), cube);

            }
        }
    }

}