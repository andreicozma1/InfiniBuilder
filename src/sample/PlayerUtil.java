package sample;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class PlayerUtil {
    public static Group player_group;
    public static Sphere model;

    private int radius = 15;

    public static double x = 0;
    public static double y = 0;
    public static double z = 0;

    public static Rotate rotx;
    public static Rotate roty;
    public static Rotate rotz;

    public static int speedForward = 10;
    public static int speedBackward = 10;
    public static int speedSide = 2;
    public static int speedFly = 5;

    public static boolean isFlying = false;
    public static boolean onGround = true;
    public static boolean aboveGround = true;

    PlayerUtil() {
        player_group = new Group();

        model = new Sphere(radius);
        model.setMaterial(MaterialsUtil.stone);
        model.setTranslateY(-radius);
        rotx = new Rotate(0, new Point3D(1, 0, 0));
        roty = new Rotate(0, new Point3D(0, 1, 0));
        rotz = new Rotate(0, new Point3D(0, 0, 1));
        model.getTransforms().setAll(rotx, roty, rotz);

        player_group.getChildren().setAll(model);
    }

    public static Group getGroup() {
        return player_group;
    }

    public static void moveForward(int val) {
//        System.out.println("Move Forward");
        PlayerUtil.z += val;
    }

    public static void moveBackward(int val) {
//        System.out.println("Move Backward");
        PlayerUtil.z -= val;
    }

    public static void moveLeft(int val) {
//        System.out.println("Move Left");
        PlayerUtil.x -= val;
    }

    public static void moveRight(int val) {
//        System.out.println("Move Right");
        PlayerUtil.x += val;
    }

    public static void moveUp(int val) {
//        System.out.println("Move Up");
        PlayerUtil.y += val;
        onGround = false;
    }

    public static void moveDown(int val) {
        System.out.println("Move Down");

        if (!aboveGround || y > 0) {
            y -= val;
        }
    }

    public static boolean isAboveGround() {
//        System.out.println(EnvironmentUtil.chunks.toString());
//        System.out.println("Player X: " + Player.x + " Y: " + Player.y + " Z: " + Player.z + " isFlying: " + Player.isFlying + " onGround: " + Player.onGround);
        boolean result = false;
        if (y > 0) {
            double curr_chunk_x = Math.floor((PlayerUtil.x + EnvironmentUtil.chunk_width / 2) / EnvironmentUtil.chunk_width);
            double curr_chunk_z = Math.floor((PlayerUtil.z + EnvironmentUtil.chunk_depth / 2) / EnvironmentUtil.chunk_depth);
            if (EnvironmentUtil.chunks.contains(new Point2D(curr_chunk_x, curr_chunk_z))) {
                System.out.println("ON GROUND");
                aboveGround = true;
            } else {
                System.out.println("NOT ON GROUND");
                aboveGround = false;
            }
        }
        return result;
    }
}