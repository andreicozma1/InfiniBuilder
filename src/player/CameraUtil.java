package player;

import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import utils.WindowUtil;

public class CameraUtil {
    private WindowUtil context;
    public static PerspectiveCamera cam;

    public double rotx = 0;
    public double roty = 0;

    public CameraUtil(WindowUtil ctx) {
        context = ctx;
        cam = new PerspectiveCamera(true);
        cam.setNearClip(1);
        cam.setFarClip(100000);
    }

    public PerspectiveCamera getCamera() {
        return cam;
    }

    void update_handler() {
//        System.out.println("x: " + rotx%360 + " y: "  + roty + " rotY " + (roty%180) );
        cam.getTransforms().clear();
        cam.getTransforms().add(new Translate(context.getPlayer().getX(), -context.getPlayer().player_height- context.getPlayer().getY(), context.getPlayer().getZ()));
        cam.getTransforms().add(new Rotate(rotx % 360, Rotate.Y_AXIS));

        cam.getTransforms().add(new Rotate(roty % 360, Rotate.X_AXIS));

    }


    public void rotateX(double val) {
        rotx += val;
    }

    public void rotateY(double val) {
        double newroty = roty + val;
        // camera bounds
        if ((newroty % 180) < -90 || (newroty % 180) > 60) {
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

}
