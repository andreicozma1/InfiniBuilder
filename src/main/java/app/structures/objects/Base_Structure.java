package app.structures.objects;

import app.structures.StructureBuilder;
import javafx.scene.shape.Shape3D;

public class Base_Structure extends StructureBuilder {
    private Shape3D shape;

    public Shape3D getShape() {
        return shape;
    }

    public void setShape(Shape3D sh) {
        shape = sh;
    }
}
