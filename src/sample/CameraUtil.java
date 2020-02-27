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

    CameraUtil(WindowUtil ctx) {
        context = ctx;
        rotx = new Rotate(0, new Point3D(1, 0, 0));
        roty = new Rotate(0, new Point3D(0, 1, 0));

        cam = new PerspectiveCamera(true);
        cam.setNearClip(1);
        cam.setFarClip(100000);
        cam.getTransforms().add(new Translate(0, -50, -200));
        cam.getTransforms().addAll(rotx, roty);
    }

    Camera getCamera() {
        return cam;
    }


    public static void rotateX(double val) {
        System.out.println("RotateX: " + val);
        if (Math.abs(val) < 20) {
            rotx.setAngle(val);
        }

    }

    public static void rotateY(double val) {
        System.out.println("RotateY: " + val);
        if (Math.abs(val) < 10) {
            roty.setAngle(val);
        }
    }

    public static void resetCenter(){

        if(!ControlsUtil.rotating){
            rotx.setAngle(rotx.getAngle() -rotx.getAngle()/recenterSpeed);
            roty.setAngle(roty.getAngle() -roty.getAngle()/recenterSpeed);
        }
    }

}
