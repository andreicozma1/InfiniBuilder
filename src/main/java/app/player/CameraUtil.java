package app.player;

import app.GameBuilder;
import app.structures.spawnables.utils.Log;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class CameraUtil {
    private static final String TAG = "CameraUtil";
    private static PerspectiveCamera PERSPECTIVE_CAMERA;
    private final GameBuilder context;
    private final int PROPERTY_ANGLE_BOUND_DOWN = -90; // degrees
    private final int PROPERTY_ANGLE_BOUND_UP = 90; // degrees
    private int PROPERTY_FOV_DEFAULT;
    private double PROPERTY_FOV_RUNNING_MULTIPLIER;
    private double PROPERTY_FOV_TIRED_MULTIPLIER;
    private double ROTATION_X = 0;
    private double ROTATION_Y = 0;

    /**
     * CameraUtil constructor handles everything related to our perspectiveCamera
     * This camera is used to be able to have a perspective-3D view of the objects in the scene
     * @param ctx
     */
    public CameraUtil(GameBuilder ctx) {
        Log.d(TAG, "CONSTRUCTOR");

        context = ctx;
        // create a new perspective camera objects and set it up
        PERSPECTIVE_CAMERA = new PerspectiveCamera(true);
        // having this initial value set to lower than the default below will create a nice 'zoom-in' effect when starting the game
        PERSPECTIVE_CAMERA.setFieldOfView(25); // initial value
        // the nearest and farthest distances of objects that we should render
        PERSPECTIVE_CAMERA.setNearClip(.5);
        PERSPECTIVE_CAMERA.setFarClip(50000);

        // set the defaults on settings
        setFOVdefault(45);
        setFOVrunningMultiplier(1.5);
        setFOVtiredMultiplier(0.8);
    }

    /**
     * Returns an instance of our perspective camera
     * @return
     */
    public PerspectiveCamera getCamera() {
        return PERSPECTIVE_CAMERA;
    }

    /**
     * Main update handler which handles the movement of the camera by the player coordinates
     * for this reason, the playerUtil must be initialized first before this is used
     */
    void update_handler() {
        PERSPECTIVE_CAMERA.getTransforms().clear();

        double height = context.getComponents().getPlayer().getPlayerHeight();
        if (context.getComponents().getPlayer().isCrouching) {
            height /= 2;
            height += height * context.getComponents().getPlayer().PROPERTY_MULTIPLIER_CROUCH_HEIGHT;
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
        if (upDownRot < PROPERTY_ANGLE_BOUND_DOWN){
            ROTATION_Y = PROPERTY_ANGLE_BOUND_DOWN;
        } else if (upDownRot > PROPERTY_ANGLE_BOUND_UP){
            ROTATION_Y = PROPERTY_ANGLE_BOUND_UP;
        } else{
            ROTATION_Y = newroty;
        }

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

    /**
     * Camera reset function which basically aligns it back to the axes
     * This is used in case the camera gets stuck in a weird position
     */
    public void reset() {
        Log.d(TAG, "reset()");
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
