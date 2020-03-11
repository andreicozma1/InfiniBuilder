package sample;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class PlayerUtil {
    public WindowUtil context;
    private Group player_group;

    private Sphere playerHead;
    private Cylinder playerNeck;
    private Cylinder playerBody;
    private Cylinder playerLeftArm;
    private Cylinder playerRightArm;
    private Cylinder playerLeftLeg;
    private Cylinder playerRightLeg;

    public double x = 0;
    public double y = 0;
    public double z = 0;

    public Rotate rotx;
    public Rotate roty;
    public Rotate rotz;

    public int speedForward = 10;
    public int speedBackward = 10;
    public int speedSide = 2;
    public int speedFly = 5;

    private boolean onGround = true;
    private boolean aboveGround = true;

    PlayerUtil(WindowUtil ctx) {
        context = ctx;
        player_group = new Group();
    }

    public void showModel(boolean state) {
        if (state) {
            addModelComponents();
        } else {
            player_group.getChildren().removeAll();
        }
    }

    private void addModelComponents() {
        // draw player head
        playerHead = new Sphere(6.5);
        playerHead.setMaterial(MaterialsUtil.blue);
        playerHead.setTranslateY(-38);

        // draw player neck
        playerNeck = new Cylinder(2.0, 2);
        playerNeck.setMaterial(MaterialsUtil.purple);
        playerNeck.setTranslateY(-31);

        // draw player body
        playerBody = new Cylinder(7.5, 20);
        playerBody.setMaterial(MaterialsUtil.red);
        playerBody.setTranslateY(-20);


        // draw player left arm
        playerLeftArm = new Cylinder(2.25, 8);
        playerLeftArm.setMaterial(MaterialsUtil.purple);
        playerLeftArm.setRotationAxis(Rotate.Z_AXIS);
        playerLeftArm.setRotate(90);
        playerLeftArm.setTranslateY(-25);
        playerLeftArm.setTranslateX(-11);

        // draw player right arm
        playerRightArm = new Cylinder(2.25, 8);
        playerRightArm.setMaterial(MaterialsUtil.purple);
        playerRightArm.setRotationAxis(Rotate.Z_AXIS);
        playerRightArm.setRotate(90);
        playerRightArm.setTranslateY(-25);
        playerRightArm.setTranslateX(11);


        // draw player left leg
        playerLeftLeg = new Cylinder(2.5, 10);
        playerLeftLeg.setMaterial(MaterialsUtil.purple);
        playerLeftLeg.setTranslateY(-5);
        playerLeftLeg.setTranslateX(-5);

        // draw player right leg
        playerRightLeg = new Cylinder(2.5, 10);
        playerRightLeg.setMaterial(MaterialsUtil.purple);
        playerRightLeg.setTranslateY(-5);
        playerRightLeg.setTranslateX(5);

//        model = new Sphere(radius);
//        model.setMaterial(MaterialsUtil.stone);
//        model.setTranslateY(-radius);
        rotx = new Rotate(0, new Point3D(1, 0, 0));
        roty = new Rotate(0, new Point3D(0, 1, 0));
        rotz = new Rotate(0, new Point3D(0, 0, 1));
//        model.getTransforms().setAll(rotx, roty, rotz);
//        player_group.getChildren().setAll(model);

        player_group.getChildren().setAll(playerRightLeg, playerLeftLeg, playerRightArm, playerLeftArm, playerNeck, playerBody, playerHead);

    }

    public Group getGroup() {
        return player_group;
    }

    public void moveForward(int val) {
//        System.out.println("Move Forward");
        this.z += val;
    }

    public void moveBackward(int val) {
//        System.out.println("Move Backward");
        this.z -= val;
    }

    public void moveLeft(int val) {
//        System.out.println("Move Left");
        this.x -= val;
    }

    public void moveRight(int val) {
//        System.out.println("Move Right");
        this.x += val;
    }

    public void moveUp(int val) {
//        System.out.println("Move Up");
        this.y += val;
        onGround = false;
    }

    public void moveDown(int val) {
        EnvironmentUtil env = context.getEnvironment();

//        System.out.println(env.getSimplexHeight(context.getPlayer().x / env.chunk_width, context.getPlayer().z / env.chunk_depth) * 20);
        if (y > 0) {
            y -= val;
        } else {
            onGround = true;
        }
    }

    public double getX() {
        return Math.round(x / context.getEnvironment().chunk_width);
    }

    public double getY() {
        return Math.round(y / context.getEnvironment().chunk_height);
    }

    public double getZ() {
        return Math.round(z / context.getEnvironment().chunk_depth);
    }


    public boolean isOnGround() {
        return onGround;
    }

    public boolean isAboveGround() {
//        System.out.println(EnvironmentUtil.chunks.toString());
//        System.out.println("Player X: " + Player.x + " Y: " + Player.y + " Z: " + Player.z + " isFlying: " + Player.isFlying + " onGround: " + Player.onGround);

        boolean result = false;
        if (y > 0) {
            double curr_chunk_x = Math.floor((this.x + context.getEnvironment().chunk_width / 2) / context.getEnvironment().chunk_width);
            double curr_chunk_z = Math.floor((this.z + context.getEnvironment().chunk_depth / 2) / context.getEnvironment().chunk_depth);
            if (context.getEnvironment().getChunks().contains(new Point2D(curr_chunk_x, curr_chunk_z))) {
                aboveGround = true;
            } else {
                aboveGround = false;
            }
        }
        return result;
    }

    public void setPosition(double newx, double newy, double newz) {
        x = newx;
        y = newy;
        z = newz;
    }

}

