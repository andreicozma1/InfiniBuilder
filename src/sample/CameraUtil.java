package sample;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Window;

public class CameraUtil {
    private WindowUtil context;
    public static Camera cam;


    public double rotx = 0;
    public double roty = 0;
//    public static Rotate rotx;
//    public static Rotate roty;


    public static boolean isCentered = true;
    private static double recenterSpeed = 50;

    /**
     * This is a Constructor
     *
     * @param ctx
     */
    CameraUtil(WindowUtil ctx) {
        context = ctx;
//        rotx = new Rotate(0, Rotate.X_AXIS);
//        roty = new Rotate(0, Rotate.Y_AXIS);

        cam = new PerspectiveCamera(true);
        cam.setNearClip(1);
        cam.setFarClip(100000);

    }

    Camera getCamera() {
        return cam;
    }

    void handle() {
//        System.out.println("x: " + rotx%360 + " y: "  + roty + " rotY " + (roty%180) );
        cam.getTransforms().clear();
        cam.getTransforms().add(new Translate(0 + context.getPlayer().getX(), -50, context.getPlayer().getZ()));
        cam.getTransforms().add(new Rotate(rotx%360, Rotate.Y_AXIS));

            cam.getTransforms().add(new Rotate(roty%360, Rotate.X_AXIS));

//        cam.getTransforms().add(new Rotate(rotz, Rotate.X_AXIS));


    }


    public void rotateX(double val) {
        rotx += val;
    }

    public void rotateY(double val) {
        double newroty = roty + val;
        // camera bounds
        if((newroty % 180) < -35 || (newroty % 180) > 60 ){
           return;
        }
        roty = newroty;
    }


    public void setRotate(double newx, double newy) {
        rotateX(newx);
        rotateY(newy);
    }

    public double getRotateX() {
        return rotx;
    }

    public double getRotateY() {
        return roty;
    }


}
