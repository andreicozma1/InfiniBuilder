package app.player;

import app.utils.Log;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import app.GameBuilder;

public class CameraUtil {
    private static final String TAG = "CameraUtil";

    private GameBuilder context;
    private static PerspectiveCamera cam;

    private int fov_default;
    private double fov_running_multiplier;
    private double fov_tired_multiplier;

    private final int bound_angle_down = -90; // degrees
    private final int bound_angle_up = 90; // degrees

    private double rotx = 0;
    private double roty = 0;

    public CameraUtil(GameBuilder ctx) {
        Log.p(TAG,"CONSTRUCTOR");

        context = ctx;
        cam = new PerspectiveCamera(true);
        cam.setFieldOfView(25); // initial value
        cam.setNearClip(.5);
        cam.setFarClip(50000);

        setFov_default(45);
        setFov_running_multiplier(1.5);
        setFov_tired_multiplier(0.8);
    }

    public PerspectiveCamera getCamera() {
        return cam;
    }

    void update_handler() {
        cam.getTransforms().clear();

        double height = context.getComponents().getPlayer().getPlayerHeight();
        if(context.getComponents().getPlayer().isCrouching){
            height /= 2;
            height += height * context.getComponents().getPlayer().crouch_multiplier;
        }
        cam.getTransforms().add(new Translate(context.getComponents().getPlayer().getPos_x(), - context.getComponents().getPlayer().getPos_y() - height, context.getComponents().getPlayer().getPos_z()));
        cam.getTransforms().add(new Rotate(rotx % 360, Rotate.Y_AXIS));
        cam.getTransforms().add(new Rotate(roty % 360, Rotate.X_AXIS));

//        System.out.println("RotX: " + Math.sin(context.getComponents().getCamera().getRotateX() * Math.PI/180)+ "   RotY: " +Math.sin((context.getComponents().getCamera().getRotateY() + 90) * Math.PI/180));
    }

    public void rotateX(double val) {
        rotx += val;
    }

    public void rotateY(double val) {
        double newroty = roty + val;

        double upDownRot = newroty % 180; // mod the newroty value with 180 to keep the bounds within that range
        // camera bounds (so that the player can't rotate more than 90deg up or 90deg down

        if (upDownRot < bound_angle_down || upDownRot > bound_angle_up) {
            return;
        }



        roty = newroty;
    }

    public double getRotateX() {
        return rotx;
    }

    public double getRotateY() {
        return roty;
    }
    public void setRotateX(double val) {
        rotx = val;
    }

    public void setRotateY(double val) {
        roty = val;
    }

    public int getFov_default() {
        return fov_default;
    }

    public double getFov_running_multiplier() {
        return fov_running_multiplier;
    }

    public double getFov_tired_multiplier() {
        return fov_tired_multiplier;
    }

    public void setFov_default(int def) {

        try {
            if (def >= 0) {
                fov_default = def; // bound the value given from 0 to 100 to a value reasonable given by the terrain generator
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void setFov_running_multiplier(double mult) {

        try {
            if (mult >= 0) {
                fov_running_multiplier = mult; // bound the value given from 0 to 100 to a value reasonable given by the terrain generator
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public void setFov_tired_multiplier(double mult) {
        try {
            if (mult >= 0) {
                fov_tired_multiplier = mult; // bound the value given from 0 to 100 to a value reasonable given by the terrain generator
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
}
