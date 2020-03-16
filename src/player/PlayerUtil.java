package player;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import utils.PhysicsUtil;
import environment.StructureBuilder;
import utils.WindowUtil;

public class PlayerUtil {
    public WindowUtil context;
    private Group player_group;

    public int fov_running = 70;
    public int fov_tired = 35;

    public double x = 0;
    public double y = 0;
    public double z = 0;

    public double runMultiplier = 1.5;
    public double speedForward = 5;
    public double speedBackward = 5;
    public double speedSide = 3;
    public double speedFly = 3;
    private double fallSpeed = 0; // Original speed before gravity is applied;

    double jump_start_height;
    private int jumpHeight = 50;
    public boolean canJump = true;
    public boolean isJumping = false;
    public boolean isRunning = false;

    public boolean isClipMode = false;
    public boolean isFlyMode = false;

    private boolean onGround = true;
    private boolean aboveGround = true;


    private Box hitbox = new Box();
    private int player_width = 10;
    public int player_height = 30;


    public PlayerUtil(WindowUtil ctx) {
        context = ctx;
        player_group = new Group();

    }

    public void update_handler() {
//        System.out.println("Player X: " + getX() + " Y: " + getY() + " Z: " + getZ()  + " onGround: " +  isOnGround() + " aboveGround: " + isAboveGround());
//        System.out.println("isJumping: " + isJumping + " canJump: " + canJump);
        context.getCamera().update_handler();

        player_group.setTranslateX(getX());
        player_group.setTranslateY(-getY() - player_height * 2);
        player_group.setTranslateZ(getZ());


        // Running mechanism. Changes camera FOV incrementally from 45 to 60 when running and from 60 to 45 when not running
        double curr_fov = context.getCamera().getCamera().getFieldOfView();
        if (isRunning) {

            if (curr_fov < fov_running) {
                context.getCamera().getCamera().setFieldOfView(curr_fov + 1);
            }
        } else {
            if (curr_fov > context.getCamera().fov_default) {
                context.getCamera().getCamera().setFieldOfView(curr_fov - 1);
            }
        }

        context.getEnvironment().generateChunks(getX(), getZ());
        context.getEnvironment().showChunksAroundPlayer(getX(), getZ());

        // Jumping Mechanism. As long as player is not in fly mode, execute mechanism
        if (!isFlyMode) {
            // If the player initiated a jump and hasn't reached the top, move the player up
//            System.out.println(jump_start_height);
            if (isJumping && y < jump_start_height + jumpHeight) {
                moveUp(speedFly);
            } else {
                // if the player reached the top, set isJumping to false, and let the player fall.
                isJumping = false;
                moveDown(fallSpeed);
                // gravity acceleration
                fallSpeed += PhysicsUtil.GRAVITY;
            }
        }
    }

    public void jump() {
        isJumping = true;
        canJump = false;
        jump_start_height = getY();
    }


    public Group getGroup() {
        return player_group;
    }

    public void moveForward(double val) {
        // If the player is running, move forward by the specified runMultiplier amount
        if (isRunning) val *= runMultiplier;

        this.z += Math.cos(context.getCamera().rotx / 57.3) * val;
        this.x += Math.sin(context.getCamera().rotx / 57.3) * val;
    }

    public void moveBackward(double val) {
        this.z -= Math.cos(context.getCamera().rotx / 57.3) * val;
        this.x -= Math.sin(context.getCamera().rotx / 57.3) * val;
    }

    public void moveLeft(double val) {
        this.x -= Math.cos(context.getCamera().rotx / 57.3) * val;
        this.z += Math.sin(context.getCamera().rotx / 57.3) * val;
    }

    public void moveRight(double val) {
        this.x += Math.cos(context.getCamera().rotx / 57.3) * val;
        this.z -= Math.sin(context.getCamera().rotx / 57.3) * val;
    }

    public void moveUp(double val) {
        this.y += val;
        onGround = false;
    }


    public void moveDown(double val) {
        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(x, z) + player_height;

        // if the player is above ground level, let the player fall
        if (getY() > ground_level || isClipMode) {
//            System.out.println("Above ground");
            y -= val;
        } else {
            // once the player fell enough to hit ground, set onGround to true
            onGround = true;
            // reposition the player back to above ground
            y = ground_level;
            // reset the "current" fall speed back to 0 since the player is now on ground.
            // Next time player is above ground the gravity will keep on getting added to the fall speed, simulating the effects of gravity
            fallSpeed = 0;
        }

        if (y < -5000) {
            reset();
        }
    }

    public void placeObject() {
        StructureBuilder tree = context.getEnvironment().getModelUtil().getStructure("plains_oak_tree.3ds");
        tree.setScaleX(15);
        tree.setScaleY(15);
        tree.setScaleZ(15);

        context.getEnvironment().placeObject(getPoint3D(), tree, true);
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Point3D getPoint3D() {
        return new Point3D(getX(), getY(), getZ());
    }

    public boolean isOnGround() {
        return onGround;
    }


    public void setPosition(double newx, double newy, double newz) {
        x = newx;
        y = newy;
        z = newz;
    }

    void reset() {
        setPosition(0, 0, 0);
        context.getCamera().rotx = 0;
        context.getCamera().roty = 0;
        isClipMode = false;
        isRunning = false;
        isFlyMode = false;
    }

}

