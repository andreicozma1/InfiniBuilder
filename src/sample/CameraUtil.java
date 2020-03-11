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
    public static Rotate rotx;
    public static Rotate roty;

    public static boolean isCentered = true;
    private static double recenterSpeed = 50;

    /**
     * This is a Constructor
     * @param ctx
     */
    CameraUtil(WindowUtil ctx) {
        context = ctx;
        rotx = new Rotate(0, new Point3D(1, 0, 0));
        roty = new Rotate(0, new Point3D(0, 1, 0));

        cam = new PerspectiveCamera(true);
        cam.setNearClip(1);
        cam.setFarClip(100000);
        cam.getTransforms().add(new Translate(0, -50, 0));
        cam.getTransforms().addAll(rotx, roty);
    }

    Camera getCamera() {
        return cam;
    }


    public void rotateX(double val) {
//        System.out.println("RotateX: " + val);
            rotx.setAngle(val);
    }

    public void rotateY(double val) {
//        System.out.println("RotateY: " + val);
            roty.setAngle(val);
    }

    public static void resetCenter(){
        if(!ControlsUtil.rotating){
            rotx.setAngle(rotx.getAngle() -rotx.getAngle()/recenterSpeed);
            roty.setAngle(roty.getAngle() -roty.getAngle()/recenterSpeed);
        }
    }

    public void setRotate(double newx, double newy) {
        rotateX(newx);
        rotateY(newy);
    }

    public double getRotateX(){
        return rotx.getAngle();
    }
    public double getRotateY(){
        return roty.getAngle();
    }
}
