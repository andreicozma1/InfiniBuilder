package player;

import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import utils.WindowUtil;

public class CameraUtil {
    private WindowUtil context;
    public static PerspectiveCamera cam;

    public final int fov_default = 45;
    private int bound_angle_down = -90; // degrees
            private int bound_angle_up = 90; // degrees

    private double rotx = 0;
    private double roty = 0;

    public CameraUtil(WindowUtil ctx) {
        context = ctx;
        cam = new PerspectiveCamera(true);
        cam.setFieldOfView(fov_default);
        cam.setNearClip(1);
        cam.setFarClip(3000);
    }

    public PerspectiveCamera getCamera() {
        return cam;
    }

    void update_handler() {
        cam.getTransforms().clear();
        cam.getTransforms().add(new Translate(context.getPlayer().getX(), -context.getPlayer().player_height- context.getPlayer().getY(), context.getPlayer().getZ()));
        cam.getTransforms().add(new Rotate(rotx % 360, Rotate.Y_AXIS));
        cam.getTransforms().add(new Rotate(roty % 360, Rotate.X_AXIS));

        System.out.println("RotX: " + Math.sin(context.getCamera().getRotateX() * Math.PI/180) + "   RotY: " + Math.sin((context.getCamera().getRotateY()+90) * Math.PI/180));
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
}
