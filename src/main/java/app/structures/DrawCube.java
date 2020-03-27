package app.structures;

import app.environment.EnvironmentUtil;
import javafx.scene.paint.Material;
import javafx.scene.shape.Box;

public class DrawCube extends StructureBuilder {
    private Box box;

    public DrawCube(double width, double height, double depth) {
        box = new Box();
        super.getChildren().add(box);
        super.setScaleXYZ(width,height,depth);
    }
    public DrawCube() {
        box = new Box();
        super.getChildren().add(box);
    }

    public Box getBox(){
        return box;
    }

}