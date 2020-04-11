package app.structures;

import app.environment.EnvironmentUtil;
import app.player.AbsolutePoint3D;

public interface Interactable {
    void placeObject(EnvironmentUtil e, AbsolutePoint3D pos, boolean shouldStack);

    void use();
}
