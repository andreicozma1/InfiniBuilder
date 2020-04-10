package app.structures;

import app.environment.EnvironmentUtil;
import app.player.PlayerPoint3D;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public interface Interactable {
    void placeAtExactPoint(EnvironmentUtil e, PlayerPoint3D pos, boolean shouldStack);
    void use();
}
