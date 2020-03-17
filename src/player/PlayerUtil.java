package player;

import environment.MaterialsUtil;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import objects.DrawCube;
import utils.PhysicsUtil;
import environment.StructureBuilder;
import utils.WindowUtil;

public class PlayerUtil {
    public WindowUtil context;
    private Group player_group;

    private int player_width = 10;
    public int player_height = 60;

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
    private double jumpHeight = player_height;
    public boolean canJump = true;
    public boolean isJumping = false;
    public boolean isRunning = false;

    public boolean isCrouching = false;
    public double crouch_multiplier = .4;

    public boolean isClipMode = false;
    public boolean isFlyMode = false;

    private boolean onGround = true;
    private boolean aboveGround = true;




    public PlayerUtil(WindowUtil ctx) {
        context = ctx;
        player_group = new Group();

    }

    public void update_handler() {
//        System.out.println("Player X: " + getX() + " Y: " + getY() + " Z: " + getZ()  + " onGround: " +  isOnGround() + " aboveGround: " + isAboveGround());
//        System.out.println("isJumping: " + isJumping + " canJump: " + canJump);
        context.getCamera().update_handler();

//        player_group.setTranslateX(getX());
//
//        double height = player_height;
//        if(isCrouching){
//            System.out.println("HEREEE");
//            height *= crouch_multiplier;
//        }
//        player_group.setTranslateY(-getY() + height);
//        player_group.setTranslateZ(getZ());


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
        if(isCrouching) val*=crouch_multiplier;

        double new_x = this.x + Math.sin(context.getCamera().getRotateX() / 57.3) * val;
        double new_z = this.z + Math.cos(context.getCamera().getRotateX() / 57.3) * val;

        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(new_x, new_z);
        if(getY() > ground_level || isClipMode || !isFlyMode){
            this.x = new_x;
            this.z = new_z;
        }

    }

    public void moveBackward(double val) {
        if(isCrouching) val*=crouch_multiplier;

        double new_x = this.x - Math.sin(context.getCamera().getRotateX() / 57.3) * val;
        double new_z = this.z - Math.cos(context.getCamera().getRotateX() / 57.3) * val;

        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(new_x, new_z);
        if(getY() > ground_level || isClipMode || !isFlyMode) {
            this.x = new_x;
            this.z = new_z;
        }
    }

    public void moveLeft(double val) {
        if(isCrouching) val*=crouch_multiplier;

        double new_z = this.z + Math.sin(context.getCamera().getRotateX() / 57.3) * val;
        double new_x = this.x - Math.cos(context.getCamera().getRotateX() / 57.3) * val;
        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(new_x, new_z);
        if(getY() > ground_level || isClipMode || !isFlyMode) {
            this.x = new_x;
            this.z = new_z;
        }
    }

    public void moveRight(double val) {
        if(isCrouching) val*=crouch_multiplier;

        double new_x = this.x + Math.cos(context.getCamera().getRotateX() / 57.3) * val;
        double new_z = this.z - Math.sin(context.getCamera().getRotateX() / 57.3) * val;
        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(new_x, new_z);
        if(getY() > ground_level || isClipMode || !isFlyMode) {
            this.x = new_x;
            this.z = new_z;
        }
    }

    public void moveUp(double val) {
        this.y += val;
        onGround = false;
    }


    public void moveDown(double val) {
        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(x, z);

        // if the player is above ground level, let the player fall
        if (getY() > ground_level || isClipMode) {
//            System.out.println("Above ground");
            y -= val;
        } else {
            // once the player fell enough to hit ground, set onGround to true
            onGround = true;
            // reposition the player back to above ground

//            y = ground_level;

            // CURRENTLY UNUSED IMPLEMENTATION WHERE THE PLAYER JUMPS IF HE HAS TO CLIMP MORE THAN A SET HEIGHT
            System.out.println(ground_level - y);
            if(ground_level - y < context.getEnvironment().getBlockDim()){
                y = ground_level;
            } else{
                jump();
            }


            // reset the "current" fall speed back to 0 since the player is now on ground.
            // Next time player is above ground the gravity will keep on getting added to the fall speed, simulating the effects of gravity
            fallSpeed = 0;
        }

        if (y < -5000) {
            reset();
        }
    }

    public void placeObject() {

        DrawCube box = new DrawCube();
        box.setScale(context.getEnvironment().getBlockDim());
        box.getBox().setMaterial(MaterialsUtil.stone);


        /*
        Point3D pos = getPoint3D();


        double zmult = Math.sin((context.getCamera().getRotateY() + 90) * Math.PI/180);
        double xmult = Math.sin(context.getCamera().getRotateX() * Math.PI/180);


        Point3D newPOS = new Point3D(pos.getX() + xmult,pos.getY(),pos.getZ() + zmult);

         */

        context.getEnvironment().placeObject(getPoint2D(),box, true);
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

    /**
     * Returns the 3D placement of the character in the world (X, Y, Z coords)
     * @return
     */
    public Point3D getPoint3D() {
        return new Point3D(getX(), -getY(), getZ());
    }

    /**
     * Returns the 2D placement of the character in the world (X and Z coords)
     * @return
     */
    public Point2D getPoint2D(){
        return new Point2D(getX(),getZ());
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
        context.getCamera().setRotateX(0);
        context.getCamera().setRotateY(0);
        isClipMode = false;
        isRunning = false;
        isFlyMode = false;
    }

}

