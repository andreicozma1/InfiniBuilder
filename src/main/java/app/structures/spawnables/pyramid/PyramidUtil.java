package app.structures.spawnables.pyramid;

/*
import app.GameBuilder;
import app.player.AbsolutePoint2D;
import app.player.AbsolutePoint3D;
import app.structures.objects.BaseCube;
import app.structures.spawnables.SpawnableStructureBuilder;
import app.utils.Log;
import javafx.geometry.Point2D;
import javafx.scene.paint.Material;

public class PyramidUtil extends SpawnableStructureBuilder {

    private static final String TAG = "PyramidUtil";

    private final double cellDim;
    private final int baseSideSize;
    private final Material pyramidMaterial;

    public PyramidUtil(double cellDim,
                       int baseSideSize,
                       Material pyramidMaterial) {
        Log.d(TAG,"CONSTRUCTOR");

        this.cellDim = cellDim;
        this.baseSideSize = baseSideSize;
        this.pyramidMaterial = pyramidMaterial;
    }



    @Override
    public void build(GameBuilder gameBuilder) {
        int i, j, k;
        Point2D pos = gameBuilder.getComponents().getPlayer().getPlayerPoint2D();
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
                double hi = gameBuilder.getComponents().getEnvironment().getClosestGroundLevel(new AbsolutePoint2D(x_pos, y_pos),true);

                if (new_height < min_height) {
                    min_height = new_height;
                }
                if(new_height > max_height){
                    max_height = new_height;
                }

            }
        }

        Log.d(TAG,"MIN HEIGHT " + min_height);
        Log.d(TAG,"MAX HEIGHT " + max_height);


        //draw pyramid
        for (i = 0; i < height; i++) {
            Log.d(TAG,"\n\nBUILDING Layer " + i);
            for (j = i; j < baseSideSize - i; j++) {
                for (k = i; k < baseSideSize - i; k++) {
                    double x_pos = startingX + j * cellDim;
                    double y_pos = startingZ + k * cellDim;
                    double hi = gameBuilder.getComponents().getEnvironment().getClosestGroundLevel(new AbsolutePoint2D(x_pos, y_pos),true);

                    Log.d(TAG,j + "  " + k + " CURRENT " + hi + "   DIFF " + (min_height - (i+1) * cellDim));

                    if(hi >  max_height - (i + 1) * cellDim) {
                        Log.d(TAG,"PLACED");
                        BaseCube cube = new BaseCube("Maze Wall", cellDim, cellDim, cellDim);
                        cube.getProps().setPROPERTY_DESTRUCTIBLE(false);
                        cube.getShape().setMaterial(pyramidMaterial);
                        block_map.put(new Point2D(x_pos, y_pos), cube);
                    } else{
                        Log.d(TAG,"DISCARDED");

                    }

                }
            }
        }
    }
}
*/

