package app.structures.pyramid;

/*
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
                double new_height = context.getComponents().getEnvironment().getTerrainYfromPlayerXYZ(x_pos, y_pos);
                if (new_height < min_height) {
                    min_height = new_height;
                }
                if(new_height > max_height){
                    max_height = new_height;
                }

            }
        }

        Log.p(TAG,"MIN HEIGHT " + min_height);
        Log.p(TAG,"MAX HEIGHT " + max_height);


        //draw pyramid
        for (i = 0; i < height; i++) {
            Log.p(TAG,"\n\nBUILDING Layer " + i);
            for (j = i; j < baseSideSize - i; j++) {
                for (k = i; k < baseSideSize - i; k++) {
                    double x_pos = startingX + j * cellDim;
                    double y_pos = startingZ + k * cellDim;
                    double hi = context.getComponents().getEnvironment().getTerrainYfromPlayerXYZ(x_pos, y_pos);

                    Log.p(TAG,j + "  " + k + " CURRENT " + hi + "   DIFF " + (min_height - (i+1) * cellDim));

                    if(hi >  max_height - (i + 1) * cellDim) {
                        Log.p(TAG,"PLACED");
                        Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
                        cube.getProps().setPROPERTY_DESTRUCTIBLE(false);
                        cube.getShape().setMaterial(pyramidMaterial);
                        block_map.put(new Point2D(x_pos, y_pos), cube);
                    } else{
                        Log.p(TAG,"DISCARDED");

                    }

                }
            }
        }
    }
}
*/

