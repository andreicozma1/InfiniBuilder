package app.structures;

import app.environment.EnvironmentUtil;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public interface Interactable {
    void placeAtExactPoint(EnvironmentUtil e, Point3D pos);
    void placeOnGround(EnvironmentUtil e, Point3D pos);
    void use();
}
