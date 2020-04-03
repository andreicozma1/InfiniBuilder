package app.structures.pyramid;

import app.GameBuilder;
import app.structures.SpawnableStructure2D;
import app.structures.StructureBuilder;
import app.structures.objects.Base_Cube;
import javafx.geometry.Point2D;
import javafx.scene.paint.Material;


public class PyramidUtil implements SpawnableStructure2D {
    private double cellDim;
    private int baseSideSize;
    private Material pyramidMaterial;

    public PyramidUtil(double cellDim,
                       int baseSideSize,
                       Material pyramidMaterial) {
        this.cellDim = cellDim;
        this.baseSideSize = baseSideSize;
        this.pyramidMaterial = pyramidMaterial;
    }


    @Override
    public void build(GameBuilder context) {
        int i, j, k;
        Point2D pos = context.getPlayer().getPoint2D();
        double height;
        double startingX = pos.getX();
        double startingZ = pos.getY();
        double currX;
        double currZ;

        block_map.clear();


        if (baseSideSize % 2 == 1) height = (baseSideSize / 2) + 1;
        else height = (baseSideSize / 2);

        //draw pyramid
        for (i = 0; i < height; i++) {
            System.out.println("BUILDING Layer " + i);
            for (j = i; j < baseSideSize - i; j++) {
                for (k = i; k < baseSideSize - i; k++) {
                    System.out.println(j + "  " + k);
                    Base_Cube cube = new Base_Cube("Maze Wall", cellDim, cellDim, cellDim);
                    cube.getProps().setPROPERTY_DESTRUCTIBLE(false);
                    cube.getShape().setMaterial(pyramidMaterial);
                    block_map.put(new Point2D(j*cellDim,k*cellDim), cube);
                }
            }
        }
    }
}

