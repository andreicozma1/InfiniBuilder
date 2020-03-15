package sample;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;

public class PlayerUtil {
    public WindowUtil context;
    private Group player_group;

    public double x = 0;
    public double y = 0;
    public double z = 0;

    public double runMultiplier = 1.5;
    public double speedForward = 3;
    public double speedBackward = 3;
    public double speedSide = 1.5;
    public double speedFly = 1.5;
    private double fallSpeed = 0; // Original speed before gravity is applied;

    double jump_start_height;
    private int jumpHeight = 30;
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


    PlayerUtil(WindowUtil ctx) {
        context = ctx;
        player_group = new Group();

    }

    public void handle() {
//        System.out.println("Player X: " + getX() + " Y: " + getY() + " Z: " + getZ()  + " onGround: " +  isOnGround() + " aboveGround: " + isAboveGround());
//        System.out.println("isJumping: " + isJumping + " canJump: " + canJump);
        context.getCamera().handle();

        player_group.setTranslateX(getX());
        player_group.setTranslateY(-getY() - player_height * 2);
        player_group.setTranslateZ(getZ());


        // Running mechanism. Changes camera FOV incrementally from 45 to 60 when running and from 60 to 45 when not running
        double curr_fov = context.getCamera().getCamera().getFieldOfView();
        if (isRunning) {
            if (curr_fov < 60) {
                context.getCamera().getCamera().setFieldOfView(curr_fov + 1);
            }
        } else {
            if (curr_fov > 45) {
                context.getCamera().getCamera().setFieldOfView(curr_fov - 1);
            }
        }

        context.getEnvironment().generateChunks(getX(), getZ());
        context.getEnvironment().showChunksAroundPlayer(getX(), getZ());

        // Jumping Mechanism. As long as player is not in fly mode, execute mechanism
        if (!isFlyMode) {
            // If the player initiated a jump and hasn't reached the top, move the player up
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
        double ground_level = -context.getEnvironment().getTerrainHeight(x, z) + player_height;

        // if the player is above ground level, let the player fall
        if (getY() > ground_level || isClipMode) {
            System.out.println("Above ground");
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

    public void placeObject(){
        Box b = new Box(50,50,50);
        b.setMaterial(MaterialsUtil.stone);

        StructureBuilder str = new StructureBuilder(x,y + 50,z);
        str.addMember(b);

        context.getEnvironment().addMember(str);
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

