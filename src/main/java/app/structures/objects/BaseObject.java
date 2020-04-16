package app.structures.objects;

import app.structures.ObjectBuilder;
import javafx.scene.shape.Shape3D;

public class BaseObject extends ObjectBuilder {
    private Shape3D shape;

    public Shape3D getShape() {
        return shape;
    }

    public void setShape(Shape3D sh) {
        shape = sh;
    }
}
