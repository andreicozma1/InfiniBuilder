package app.structures;

import app.environment.EnvironmentUtil;
import javafx.geometry.Point2D;

public interface Interactable {
    void place(EnvironmentUtil e, Point2D pos);
    void use();
}
