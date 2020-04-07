package app.structures.pyramid;

import app.GameBuilder;
import app.structures.SpawnableStructure;
import app.structures.objects.Base_Cube;
import app.utils.Log;
import javafx.geometry.Point2D;
import javafx.scene.paint.Material;


public class PyramidUtil implements SpawnableStructure {
    private static final String TAG = "PyramidUtil";

    private final double cellDim;
    private final int baseSideSize;
    private final Material pyramidMaterial;

    public PyramidUtil(double cellDim,
                       int baseSideSize,
                       Material pyramidMaterial) {
        Log.p(TAG,"CONSTRUCTOR");

        this.cellDim = cellDim;
        this.baseSideSize = baseSideSize;
        this.pyramidMaterial = pyramidMaterial;
    }


    @Override
    public void build(GameBuilder context) {
        int i, j, k;
        Point2D pos = context.getComponents().getPlayer().getPoint2D();
        double height;
        double startingX = pos.getX();
        double startingZ = pos.getY();

        block_map.clear();

        if (baseSideSize % 2 == 1) height = (baseSideSize / 2.0) + 1;
        else height = (baseSideSize / 2.0);


        double min_height = Double.MAX_VALUE;
        double max_height = Double.MIN_VALUE;

        for (j = 0; j < baseSideSize; j++) {
            for (k = 0; k < baseSideSize; k++) {
                double x_pos = startingX + j * cellDim;
                double y_pos = startingZ + k * cellDim;
                double new_height = context.getComponents().getEnvironment().getTerrainYfromPlayerXZ(x_pos, y_pos);
                if (new_height < min_height) {
                    min_height = new_height;
                }
                if(new_height > max_height){
                    max_height = new_height;
                }

            }
        }

        System.out.println("MIN HEIGHT " + min_height);
        System.out.println("MAX HEIGHT " + max_height);


        //draw pyramid
        for (i = 0; i < height; i++) {
            System.out.println("\n\nBUILDING Layer " + i);
            for (j = i; j < baseSideSize - i; j++) {
                for (k = i; k < baseSideSize - i; k++) {
                    double x_pos = startingX + j * cellDim;
                    double y_pos = startingZ + k * cellDim;
                    double hi = context.getComponents().getEnvironment().getTerrainYfromPlayerXZ(x_pos, y_pos);

                    System.out.println(j + "  " + k + " CURRENT " + hi + "   DIFF " + (min_height - (i+1) * cellDim));

                    if(hi >  max_height - (i + 1) * cellDim) {
                        System.out.println("PLACED");
                        Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
                        cube.getProps().setPROPERTY_DESTRUCTIBLE(false);
                        cube.getShape().setMaterial(pyramidMaterial);
                        block_map.put(new Point2D(x_pos, y_pos), cube);
                    } else{
                        System.out.println("DISCARDED");

                    }

                }
            }
        }
    }
}

