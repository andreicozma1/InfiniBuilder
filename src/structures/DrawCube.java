package structures;

import javafx.scene.paint.Material;
import javafx.scene.shape.Box;

public class DrawCube extends StructureBuilder {
    private Box box;
    private Material material;

    public DrawCube(double width, double height, double depth) {
        box = new Box(1, 1, 1);
        super.getChildren().add(box);
        super.setScaleXYZ(width,height,depth);
    }
    public DrawCube() {
        box = new Box(1, 1, 1);
        super.getChildren().add(box);
    }


    public Box getBox(){
        return box;
    }

}