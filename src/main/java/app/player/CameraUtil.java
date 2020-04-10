package app.player;

import app.utils.Log;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import app.GameBuilder;

public class CameraUtil {
    private static final String TAG = "CameraUtil";

    private final GameBuilder context;
    private static PerspectiveCamera PERSPECTIVE_CAMERA;

    private int PROPERTY_FOV_DEFAULT;
    private double PROPERTY_FOV_RUNNING_MULTIPLIER;
    private double PROPERTY_FOV_TIRED_MULTIPLIER;

    private final int PROPERTY_ANGLE_BOUND_DOWN = -90; // degrees
    private final int PROPERTY_ANGLE_BOUND_UP = 90; // degrees

    private double ROTATION_X = 0;
    private double ROTATION_Y = 0;

    public CameraUtil(GameBuilder ctx) {
        Log.p(TAG,"CONSTRUCTOR");

        context = ctx;
        PERSPECTIVE_CAMERA = new PerspectiveCamera(true);
        PERSPECTIVE_CAMERA.setFieldOfView(25); // initial value
        PERSPECTIVE_CAMERA.setNearClip(.5);
        PERSPECTIVE_CAMERA.setFarClip(50000);

        setFOVdefault(45);
        setFOVrunningMultiplier(1.5);
        setFOVtiredMultiplier(0.8);
    }

    public PerspectiveCamera getCamera() {
        return PERSPECTIVE_CAMERA;
    }

    void update_handler() {
        PERSPECTIVE_CAMERA.getTransforms().clear();

        double height = context.getComponents().getPlayer().getPlayerHeight();
        if(context.getComponents().getPlayer().isCrouching){
            height /= 2;
            height += height * context.getComponents().getPlayer().crouch_multiplier;
        }
        PERSPECTIVE_CAMERA.getTransforms().add(new Translate(context.getComponents().getPlayer().getPositionX(), context.getComponents().getPlayer().getPositionYnoHeight() - height, context.getComponents().getPlayer().getPositionZ()));
        PERSPECTIVE_CAMERA.getTransforms().add(new Rotate(ROTATION_X % 360, Rotate.Y_AXIS));
        PERSPECTIVE_CAMERA.getTransforms().add(new Rotate(ROTATION_Y % 360, Rotate.X_AXIS));
    }

    public void rotateX(double val) {
        ROTATION_X += val;
    }

    public void rotateY(double val) {
        double newroty = ROTATION_Y + val;

        double upDownRot = newroty % 180; // mod the newroty value with 180 to keep the bounds within that range
        // camera bounds (so that the player can't rotate more than 90deg up or 90deg down

        if (upDownRot < PROPERTY_ANGLE_BOUND_DOWN || upDownRot > PROPERTY_ANGLE_BOUND_UP) {
            return;
        }

        ROTATION_Y = newroty;
    }

    public int getFOVdefault() {
        return PROPERTY_FOV_DEFAULT;
    }
    public void setFOVdefault(int def) {
        try {
            if (def >= 0) {
                PROPERTY_FOV_DEFAULT = def; // bound the value given from 0 to 100 to a value reasonable given by the terrain generator
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public double getFOVrunningMultiplier() {
        return PROPERTY_FOV_RUNNING_MULTIPLIER;
    }
    public void setFOVrunningMultiplier(double mult) {
        try {
            if (mult >= 0) {
                PROPERTY_FOV_RUNNING_MULTIPLIER = mult; // bound the value given from 0 to 100 to a value reasonable given by the terrain generator
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public double getFOVtiredMultiplier() {
        return PROPERTY_FOV_TIRED_MULTIPLIER;
    }
    public void setFOVtiredMultiplier(double mult) {
        try {
            if (mult >= 0) {
                PROPERTY_FOV_TIRED_MULTIPLIER = mult; // bound the value given from 0 to 100 to a value reasonable given by the terrain generator
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void reset(){
        setRotateX(0);
        setRotateY(0);
    }

    public double getRotateX() {
        return ROTATION_X;
    }
    public void setRotateX(double val) {
        ROTATION_X = val;
    }

    public double getRotateY() {
        return ROTATION_Y;
    }
    public void setRotateY(double val) {
        ROTATION_Y = val;
    }
}
