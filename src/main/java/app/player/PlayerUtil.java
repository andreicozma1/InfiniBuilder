package app.player;

import app.GUI.HUD.HUDUtil;
import app.GUI.HUD.StatusBar;
import app.structures.StructureBuilder;
import app.structures.objects.Base_Structure;
import app.utils.ProjectileUtil;
import app.utils.ResourcesUtil;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import app.structures.objects.Base_Sphere;
import app.utils.PhysicsUtil;
import app.GameBuilder;

public class PlayerUtil {
    public GameBuilder context;
    private Group player_group;

    private String player_name = "Steve";
    private int player_width = 10;
    private int player_height = 50;

    private boolean resetWorldOnDeath;

    private double pos_x = 0;
    private double pos_y = 0;
    private double pos_z = 0;

    private double staminaRegenSpeed = .05;
    private double staminaDepletionSpeed = 0.2;
    private double healthRegenSpeed = .005;

    private double runMultiplier = 1.25;
    double speedForward = 3;
    double speedBackward = 2;
    double speedSide = 2;
    double speedFly = 5;
    private double speed_fall_initial = 0; // Original speed before gravity is applied;

    private double jump_height_initial;

    private double jump_height_multiplier;

    private double autoJumpCutoffHeight;

    boolean canJump = true;
    private boolean isJumping = false;
    boolean isRunning = false;
    boolean isCrouching = false;
    double crouch_multiplier = .4;

    private boolean isClipMode = false;
    boolean isFlyMode = false;

    private boolean onGround = true;

    private PointLight uv_light;
    private boolean uv_light_state = false;

    public PlayerUtil(GameBuilder ctx) {
        context = ctx;
        player_group = new Group();

        uv_light = new PointLight();
        uv_light.setLightOn(false);
        uv_light.setColor(Color.DARKBLUE);
        player_group.getChildren().add(uv_light);

        setJumpHeightMultiplier(1);
        setAutoJumpCutoffHeight(.5);
        setResetWorldOnDeath(true);
    }

    public void update_handler(double dt) {

//        System.out.println("Player X: " + context.getEnvironment().getTerrainXfromPlayerX(getX()) + " Y: " + getY() + " Z: " + context.getEnvironment().getTerrainZfromPlayerZ(getZ())  + " onGround: " +  isOnGround() + " aboveGround: ");


//        System.out.println("isJumping: " + isJumping + " canJump: " + canJump);
        context.getCamera().update_handler();

        context.getEnvironment().generateChunks(getPos_x(), getPos_z());
        context.getEnvironment().showChunksAroundPlayer(getPos_x(), getPos_z());

        player_group.setTranslateX(getPos_x());
        player_group.setTranslateY(-getPos_y() - player_height);
        player_group.setTranslateZ(getPos_z());

        // Jumping Mechanism. As long as player is not in fly mode, execute mechanism
        if (!isFlyMode) {
            // If the player initiated a jump and hasn't reached the top, move the player up
//            System.out.println(jump_start_height);

            if (isJumping && pos_y < jump_height_initial + player_height * jump_height_multiplier) {

                moveUp(speedFly * dt);
            } else {
                // if the player reached the top, set isJumping to false, and let the player fall.
                isJumping = false;
                moveDown(speed_fall_initial * dt);
                // gravity acceleration
                speed_fall_initial += PhysicsUtil.GRAVITY * dt;
            }
        }

        // Running mechanism. Changes camera FOV incrementally from 45 to 60 when running and from 60 to 45 when not running
        double curr_fov = context.getCamera().getCamera().getFieldOfView();
        if (isRunning) {
            if (curr_fov < context.getCamera().getFov_default() * context.getCamera().getFov_running_multiplier()) {
                context.getCamera().getCamera().setFieldOfView(curr_fov + 1);
            }
        } else {

            if (getStaminaBar().getCurrStatus() < getStaminaBar().getMaxStatus()) {
                getStaminaBar().setCurrStatus(getStaminaBar().getCurrStatus() + staminaRegenSpeed * dt);
            }
            if (isOnGround() && curr_fov > context.getCamera().getFov_default()) {
                context.getCamera().getCamera().setFieldOfView(curr_fov - 5);
            } else if (curr_fov < context.getCamera().getFov_default() - 2) {
                context.getCamera().getCamera().setFieldOfView(curr_fov + 2);
            }
        }

        if(getStaminaBar().getCurrStatus() > getStaminaBar().getMaxStatus()/2 && getHealthBar().getCurrStatus() != getHealthBar().getMaxStatus()){
            // Regenerate health if stamina > half
            getHealthBar().setCurrStatus(getHealthBar().getCurrStatus()+ healthRegenSpeed * dt);
        }

    }


    public void shoot() {
        Base_Sphere sp = new Base_Sphere("Projectile", 5);
        sp.getShape().setMaterial(ResourcesUtil.metal);

        ProjectileUtil proj = new ProjectileUtil(context.getEnvironment(), sp);
        proj.setSpeed(10);
        proj.shoot();
    }

    public void placeObject() {

        Base_Structure inventory_item = ((Inventory) context.getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().popCurrentItem();
        context.getHUD().getElement(HUDUtil.INVENTORY).update();

        System.out.println("placeObject() " + inventory_item.getProps().getPROPERTY_ITEM_TAG() + " " + inventory_item.getScaleX() + " " + inventory_item.getScaleY() + " " + inventory_item.getScaleZ());

        if (inventory_item.getProps().getPROPERTY_ITEM_TAG() != StructureBuilder.UNDEFINED_TAG) {
            switch (inventory_item.getProps().getPROPERTY_ITEM_TYPE()) {
                case StructureBuilder.TYPE_OBJECT:
                    Base_Structure cb = StructureBuilder.resolve(inventory_item);
                    cb.place(context.getEnvironment(), getPoint2D());
                    break;
                default:
                    inventory_item.place(context.getEnvironment(), getPoint2D());
                    break;
            }
        }
    }

    public void jump() {
        isJumping = true;
        canJump = false;
        jump_height_initial = getPos_y();
        isCrouching = false;
    }


    public Group getGroup() {
        return player_group;
    }

    public void setIsRunning(boolean run) {
        StatusBar bar = (StatusBar) context.getHUD().getElement(HUDUtil.STAMINA);

        if (!isRunning && run && bar.getCurrStatus() > bar.getMaxStatus() / 3) {
//            System.out.println("HERE2");
            isRunning = true;
        } else if (isRunning && run && bar.getCurrStatus() > 0) {
//            System.out.println("HERE2");
            isRunning = true;
        } else {
//            System.out.println("HERE3");
            if (isRunning) {
                takeDamage(1);
            }
            isRunning = false;
        }
    }

    public void moveForward(double val) {
        // If the player is running, move forward by the specified runMultiplier amount
        if (isFlyMode) val = speedFly;
        StatusBar bar = (StatusBar) context.getHUD().getElement(HUDUtil.STAMINA);
        if (isRunning) {
            val *= runMultiplier;
            bar.setCurrStatus(bar.getCurrStatus() - staminaDepletionSpeed);
        }

        if (isCrouching) val *= crouch_multiplier;

        double new_x = this.pos_x + Math.sin(context.getCamera().getRotateX() / 57.3) * val;
        double new_z = this.pos_z + Math.cos(context.getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);

    }

    public void moveBackward(double val) {
        if (isFlyMode) val = speedFly;
        if (isCrouching) val *= crouch_multiplier;

        double new_x = this.pos_x - Math.sin(context.getCamera().getRotateX() / 57.3) * val;
        double new_z = this.pos_z - Math.cos(context.getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);

    }

    public void moveLeft(double val) {
        if (isFlyMode) val = speedFly;

        if (isCrouching) val *= crouch_multiplier;

        double new_z = this.pos_z + Math.sin(context.getCamera().getRotateX() / 57.3) * val;
        double new_x = this.pos_x - Math.cos(context.getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);

    }

    public void moveRight(double val) {
        if (isFlyMode) val = speedFly;
        if (isCrouching) val *= crouch_multiplier;

        double new_x = this.pos_x + Math.cos(context.getCamera().getRotateX() / 57.3) * val;
        double new_z = this.pos_z - Math.sin(context.getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    public void handle_collision(double new_x, double new_z) {
        double ground_level_x = -context.getEnvironment().getTerrainYfromPlayerXZ(new_x, this.pos_z);
        double ground_level_z = -context.getEnvironment().getTerrainYfromPlayerXZ(this.pos_x, new_z);

        if ((ground_level_x - pos_y < autoJumpCutoffHeight) || isClipMode) {
            this.pos_x = new_x;
        }
        if ((ground_level_z - pos_y < autoJumpCutoffHeight) || isClipMode) {
            this.pos_z = new_z;
        }
    }

    public void moveUp(double val) {
        this.pos_y += val;
        onGround = false;
    }


    double fall_height = 0;

    public void moveDown(double val) {
        double ground_level = -context.getEnvironment().getTerrainYfromPlayerXZ(getPos_x(), getPos_z());

        // if the player is above ground level, let the player fall
        if (getPos_y() > ground_level || isClipMode) {

            pos_y -= val;
            fall_height += val;

            // if the player is more than a block above the ground , set onGround = false;
            if (pos_y - ground_level > context.getEnvironment().getBlockDim()) {
                onGround = false;
            }
            if (!isOnGround() && !isRunning && !isFlyMode) {
                context.getCamera().getCamera().setFieldOfView(context.getCamera().getFov_default() + val * 5 * (1 - Math.cos((context.getCamera().getRotateY()) * Math.PI / 180)));
            }
        } else {
            if (!onGround && !isFlyMode) {
                System.out.println("Player Hit Ground from height: " + fall_height);


                if (fall_height > jump_height_multiplier * player_height * 1.8)
                    takeDamage(fall_height / 8);

            }
            onGround = true;
            fall_height = 0;
            speed_fall_initial = 0;
            // once the player fell enough to hit ground, set onGround to true

            // reposition the player back to above ground

            // CURRENTLY UNUSED IMPLEMENTATION WHERE THE PLAYER JUMPS IF HE HAS TO CLIMP MORE THAN A SET HEIGHT
            if (ground_level - pos_y > context.getEnvironment().getBlockDim() * .75) {
                jump();
            } else {
                pos_y = ground_level;
            }
            // reset the "current" fall speed back to 0 since the player is now on ground.
            // Next time player is above ground the gravity will keep on getting added to the fall speed, simulating the effects of gravity

        }

        if (pos_y < -5000) {
            reset();
        }
    }


    public StatusBar getStaminaBar(){
        return (StatusBar) context.getHUD().getElement(HUDUtil.STAMINA);
    }
    public StatusBar getHealthBar(){
        return (StatusBar) context.getHUD().getElement(HUDUtil.HEALTH);
    }
    public double getHealth() {
        return getHealthBar().getCurrStatus();
    }

    public void takeDamage(double d) {
        getHealthBar().setCurrStatus(getHealthBar().getCurrStatus() - d);
        if (getHealthBar().getCurrStatus() == 0) {
            die();
        }
    }

    public double getPos_x() {
        return pos_x;
    }

    public double getPos_y() {
        return pos_y;
    }

    public double getPos_z() {
        return pos_z;
    }

    /**
     * Returns the 3D placement of the character in the world (X, Y, Z coords)
     *
     * @return
     */
    public Point3D getPoint3D() {
        return new Point3D(getPos_x(), -getPos_y(), getPos_z());
    }

    /**
     * Returns the 2D placement of the character in the world (X and Z coords)
     *
     * @return
     */
    public Point2D getPoint2D() {
        return new Point2D(getPos_x(), getPos_z());
    }

    public double getPlayerHeight() {
        return player_height;
    }

    public boolean isOnGround() {
        return onGround;
    }


    public void setPosition(double newx, double newy, double newz) {
        pos_x = newx;
        pos_y = newy;
        pos_z = newz;
    }

    void reset() {
        System.out.println("## You died!");
        setPosition(0, 0, 0);
        context.getCamera().reset();
        isClipMode = false;
        isRunning = false;
        isFlyMode = false;
        isJumping = false;
        getHealthBar().setCurrStatus(getHealthBar().getMaxStatus());
        getStaminaBar().setCurrStatus(getStaminaBar().getMaxStatus());

        context.getGameSceneControls().reset();
        context.getEnvironment().reset();
    }

    void die(){
        reset();
        context.showScene(context.getMenu().getScene());
    }


    boolean getResetWorldOnDeath(){
        return resetWorldOnDeath;
    }
    void setResetWorldOnDeath(boolean val){
        resetWorldOnDeath = val;
    }

    void setUV_light(boolean state) {
        uv_light.setLightOn(state);
        uv_light_state = state;
    }

    void toggleUVlight() {
        if (uv_light_state) {
            setUV_light(false);
        } else {
            setUV_light(true);
        }
    }

    void toggleCrouch() {
        if (!isFlyMode) {
            context.getPlayer().isCrouching = !context.getPlayer().isCrouching;
        }
    }


    boolean getIsClipMode() {
        return isClipMode;
    }

    void toggleIsClipMode() {
        context.getPlayer().isClipMode = !context.getPlayer().isClipMode;
    }

    boolean getIsFlyMode() {
        return isFlyMode;
    }

    void toggleIsFlyMode() {
        context.getPlayer().isFlyMode = !context.getPlayer().isFlyMode;
    }

    public double getAutoJumpCutoffHeight() {
        return autoJumpCutoffHeight / getPlayerHeight();
    }

    public void setAutoJumpCutoffHeight(double val) {
        try {
            if (val >= 0) {
                this.autoJumpCutoffHeight = player_height * val;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }


    public double getFlySpeed() {
        return speedFly;
    }

    public void setFlySpeed(double spd) {
        try {
            if (spd >= 0) {
                speedFly = spd;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public double getRunMultiplier() {
        return runMultiplier;
    }

    public void setRunMultiplier(double mult) {
        try {
            if (mult >= 0) {
                runMultiplier = mult;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }


    public double getJumpHeightMultiplier() {
        return jump_height_multiplier;
    }

    public void setJumpHeightMultiplier(double mult) {
        try {
            if (mult >= 0) {
                jump_height_multiplier = mult;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}

