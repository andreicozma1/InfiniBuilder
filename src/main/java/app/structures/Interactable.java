package app.structures;

import app.environment.EnvironmentUtil;
import app.player.PlayerPoint3D;

public interface Interactable {
    void placeObject(EnvironmentUtil e, PlayerPoint3D pos, boolean shouldStack);

    void use();
}
