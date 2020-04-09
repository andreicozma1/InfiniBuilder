package app.player;

import app.GUI.HUD.HUDElements.*;
import app.GUI.HUD.HUDUtil;
import app.environment.EnvironmentUtil;
import app.structures.StructureBuilder;
import app.structures.objects.Base_Structure;
import app.utils.InventoryUtil;
import app.utils.Log;
import app.utils.ProjectileUtil;
import app.utils.ResourcesUtil;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import app.structures.objects.Base_Sphere;
import app.GameBuilder;

public class PlayerUtil {

    private static final String TAG = "PlayerUtil";

    public GameBuilder context;
    private final Group player_group;

    private final String player_name = "Steve";
    private final int player_width = 10;
    private final int player_height = 50;

    private double pos_x = 0;
    private double pos_y = 0;
    private double pos_z = 0;

    private final double staminaRegenSpeed = .05;
    private final double staminaDepletionSpeed = 0.2;
    private final double healthRegenSpeed = .008;

    private double runMultiplier;
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

    private final PointLight uv_light;
    private boolean uv_light_state = false;

    InventoryUtil inventoryUtil;

    public PlayerUtil(GameBuilder ctx) {
        Log.p(TAG,"CONSTRUCTOR");

        context = ctx;
        player_group = new Group();

        inventoryUtil = new InventoryUtil(this,10);

        uv_light = new PointLight();
        uv_light.setLightOn(false);
        uv_light.setColor(Color.DARKBLUE);
        player_group.getChildren().add(uv_light);

        setJumpHeightMultiplier(1);
        setAutoJumpCutoffHeight(.5);
        setRunMultiplier(1.10);
    }

    public void update_handler(double dt) {
        context.getComponents().getCamera().update_handler();

        context.getComponents().getEnvironment().generateChunks3D(getPos_x(), getPos_z());
//        context.getComponents().getEnvironment().generateChunks(getPos_x(), getPos_z());
        context.getComponents().getEnvironment().showChunksAroundPlayer(getPos_x(), getPos_z());

//        System.out.println("########## " + (int)Math.floor(-getPos_y()/context.getComponents().getEnvironment().getBlockDim()) + "   " + context.getComponents().getEnvironment().getWorldYFromPlayerPt2D(getPoint2D()));

        player_group.setTranslateX(getPos_x());
        player_group.setTranslateY(getPos_y() - player_height);
        player_group.setTranslateZ(getPos_z());


        // Jumping Mechanism. As long as player is not in fly mode, execute mechanism
        if (!isFlyMode) {
            // If the player initiated a jump and hasn't reached the top, move the player up

            if (isJumping && pos_y < jump_height_initial + player_height * jump_height_multiplier) {
                Log.p(TAG,"Jumping from " + jump_height_initial + " to " + (jump_height_initial + player_height * jump_height_multiplier));
                moveUp(speedFly * dt);
            } else {
                // if the player reached the top, set isJumping to false, and let the player fall.
                isJumping = false;
                moveDown(speed_fall_initial * dt);
                // gravity acceleration
            }
        }

        // Running mechanism. Changes camera FOV incrementally from 45 to 60 when running and from 60 to 45 when not running
        double curr_fov = context.getComponents().getCamera().getCamera().getFieldOfView();
        if (isRunning) {
            if (curr_fov < context.getComponents().getCamera().getFov_default() * context.getComponents().getCamera().getFov_running_multiplier()) {
                context.getComponents().getCamera().getCamera().setFieldOfView(curr_fov + 1);
                if (context.getEffects().getMotionBlurEnabled()) {
                    context.getEffects().EFFECT_MOTION_BLUR.setRadius(context.getEffects().EFFECT_MOTION_BLUR.getRadius() + .5);
                }
            }
        } else {
            if (getStaminaBar().getCurrStatus() < getStaminaBar().getMaxStatus()) {
                getStaminaBar().setCurrStatus(getStaminaBar().getCurrStatus() + staminaRegenSpeed * dt);
            }
            if (curr_fov > context.getComponents().getCamera().getFov_default()) {
                context.getComponents().getCamera().getCamera().setFieldOfView(curr_fov - 5);
            } else if (curr_fov < context.getComponents().getCamera().getFov_default() - 2) {
                context.getComponents().getCamera().getCamera().setFieldOfView(curr_fov + 2);
            }
            if (context.getEffects().getMotionBlurEnabled()) {
                if (context.getEffects().EFFECT_MOTION_BLUR.getRadius() > 0) {
                    double deBlurSpeed = 1;
                    if (isFlyMode) {
                        deBlurSpeed = 5;
                    }
                    context.getEffects().EFFECT_MOTION_BLUR.setRadius(context.getEffects().EFFECT_MOTION_BLUR.getRadius() - deBlurSpeed);
                } else {
                    context.getEffects().EFFECT_MOTION_BLUR.setRadius(0);
                }
            }
        }

        if (getStaminaBar().getCurrStatus() > getStaminaBar().getMaxStatus() / 2 && getHealthBar().getCurrStatus() != getHealthBar().getMaxStatus()) {
            // Regenerate health if stamina > half
            getHealthBar().setCurrStatus(getHealthBar().getCurrStatus() + healthRegenSpeed * dt);
        }



        /* TODO - unfinished implementation for resetting the player's status bars if walking over a crystal.
        if(context.getComponents().getEnvironment().terrain_map_block.containsKey(getPoint2D())){
            if(context.getComponents().getEnvironment().terrain_map_block.get(context.getComponents().getEnvironment().getWorldPoint2D(getPoint2D())).getProps().getPROPERTY_ITEM_TAG().toLowerCase().contains("crystal")){
                resetBars();
            }
        }
        */

    }

    public InventoryUtil getInventory(){
        return inventoryUtil;
    }


    public void shoot() {
        Log.p(TAG,"shoot()");
        Base_Sphere sp = new Base_Sphere("Projectile", 5);
        sp.getShape().setMaterial(ResourcesUtil.metal);

        ProjectileUtil proj = new ProjectileUtil(context.getComponents().getEnvironment(), sp);
        proj.setSpeed(10);
        proj.shoot();
    }

    public void placeObject() {
        Base_Structure inventory_item = ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().popCurrentItem();
        context.getComponents().getHUD().getElement(HUDUtil.INVENTORY).update();

        Log.p(TAG,"placeObject() " + inventory_item.getProps().getPROPERTY_ITEM_TAG() + " " + inventory_item.getScaleX() + " " + inventory_item.getScaleY() + " " + inventory_item.getScaleZ());

        if (inventory_item.getProps().getPROPERTY_ITEM_TAG() != StructureBuilder.UNDEFINED_TAG) {
            switch (inventory_item.getProps().getPROPERTY_ITEM_TYPE()) {
                case StructureBuilder.TYPE_OBJECT:
                    Base_Structure cb = StructureBuilder.resolve(inventory_item);
                    cb.place(context.getComponents().getEnvironment(), getPoint2D());
                    break;
                default:
                    inventory_item.place(context.getComponents().getEnvironment(), getPoint2D());
                    break;
            }
        }
    }

    public void jump() {
        Log.p(TAG,"jump()");
        isJumping = true;
        canJump = false;
        jump_height_initial = getPos_y();
        isCrouching = false;
    }


    public Group getGroup() {
        return player_group;
    }

    public void setIsRunning(boolean run) {
        StatusBar bar = (StatusBar) context.getComponents().getHUD().getElement(HUDUtil.STAMINA);

        if (!isRunning && run && bar.getCurrStatus() > bar.getMaxStatus() / 3) {
            isRunning = true;
        } else if (isRunning && run && bar.getCurrStatus() > 0) {
            isRunning = true;
        } else {
            if (isRunning) {
                takeDamage(1);
            }
            isRunning = false;
        }
    }

    public void moveForward(double val) {
        // If the player is running, move forward by the specified runMultiplier amount
        if (isFlyMode) val = speedFly;
        StatusBar bar = (StatusBar) context.getComponents().getHUD().getElement(HUDUtil.STAMINA);
        if (isRunning) {
            val *= runMultiplier;
            bar.setCurrStatus(bar.getCurrStatus() - staminaDepletionSpeed);
        }

        if (isCrouching) val *= crouch_multiplier;

        double new_x = this.pos_x + Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_z = this.pos_z + Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);

    }

    public void moveBackward(double val) {
        if (isFlyMode) val = speedFly;
        if (isCrouching) val *= crouch_multiplier;

        double new_x = this.pos_x - Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_z = this.pos_z - Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);

    }

    public void moveLeft(double val) {
        if (isFlyMode) val = speedFly;

        if (isCrouching) val *= crouch_multiplier;

        double new_z = this.pos_z + Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_x = this.pos_x - Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);

    }

    public void moveRight(double val) {
        if (isFlyMode) val = speedFly;
        if (isCrouching) val *= crouch_multiplier;

        double new_x = this.pos_x + Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_z = this.pos_z - Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    public void handle_collision(double new_x, double new_z) {
        double ground_level_x = context.getComponents().getEnvironment().getTerrainYfromPlayerXYZ(new_x, getPos_y(),this.pos_z);
        double ground_level_z = context.getComponents().getEnvironment().getTerrainYfromPlayerXYZ(this.pos_x,getPos_y() ,new_z);

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

    public void moveDown(double val) {
        double ground_level = context.getComponents().getEnvironment().getTerrainYfromPlayerXYZ(getPos_x(),getPos_y() - player_height, getPos_z());
        System.out.println("######## HERE0");

        // if the player is above ground level, let the player fall
        if (getPos_y() > ground_level || isClipMode) {
            System.out.println("######## HERE1");
            pos_y -= val;
            speed_fall_initial += EnvironmentUtil.GRAVITY;
            // if the player is more than a block above the ground , set onGround = false;
            if (pos_y - ground_level > context.getComponents().getEnvironment().getBlockDim()) {
                onGround = false;
            }
            if (!isOnGround() && !isRunning && !isFlyMode) {
                Log.p(TAG, "moveDown() -> Falling at speed " + val);
                if (context.getComponents().getCamera().getCamera().getFieldOfView() < 120) {
                    context.getComponents().getCamera().getCamera().setFieldOfView(context.getComponents().getCamera().getFov_default() + val * 5);
                    if (context.getEffects().getMotionBlurEnabled()) {
                        context.getEffects().EFFECT_MOTION_BLUR.setRadius(context.getEffects().EFFECT_MOTION_BLUR.getRadius() + val / 2);
                    }
                }
            }
        } else if (pos_y != ground_level) {
            System.out.println("######## HERE2");

            if (!onGround && !isFlyMode) {
                Log.p(TAG, "moveDown() -> Hit ground from height " + val);
                if (val > 7) {
                    takeDamage(val*3);
                }
            }

            if (pos_y - ground_level > context.getComponents().getEnvironment().getBlockDim() * .75) {
                System.out.println("######## HERE32");

                jump();
            } else {
                System.out.println("######## HERE42 " + ground_level);

                warpToGround();
            }
        }

//        if (pos_y < -5000) {
//            reset();
//        }
    }

    private void warpToGround() {
        double ground_level = context.getComponents().getEnvironment().getTerrainYfromPlayerXYZ(getPos_x(), getPos_y() - player_height, getPos_z());
        pos_y = ground_level;
        onGround = true;
        speed_fall_initial = 0;
    }

    public StatusBar getStaminaBar() {
        return (StatusBar) context.getComponents().getHUD().getElement(HUDUtil.STAMINA);
    }

    public StatusBar getHealthBar() {
        return (StatusBar) context.getComponents().getHUD().getElement(HUDUtil.HEALTH);
    }

    public double getHealth() {
        return getHealthBar().getCurrStatus();
    }

    public void takeDamage(double d) {
        Log.p(TAG, "takeDamage() -> Took " + d + " damage");
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

    public void reset() {
        Log.p(TAG, "reset()");
        setPosition(0, 0, 0);

        isClipMode = false;
        isRunning = false;
        isFlyMode = false;
        isJumping = false;

        resetBars();
        context.getComponents().getCamera().reset();
        context.getEffects().resetEffects();
        context.getComponents().getGameSceneControls().reset();
    }

    public void resetBars(){
        getHealthBar().setCurrStatus(getHealthBar().getMaxStatus());
        getStaminaBar().setCurrStatus(getStaminaBar().getMaxStatus());
    }

    public void die() {
        Log.p(TAG, "die()");
        ((DeathMenu) context.getComponents().getHUD().getElement(HUDUtil.DEATH)).setDead(true);
        context.getComponents().getHUD().getElement(HUDUtil.DEATH).update();
    }


    public void setUV_light(boolean state) {
        uv_light.setLightOn(state);
        uv_light_state = state;
    }

    public void toggleUVlight() {
        Log.p(TAG, "toggleUVlight()");
        setUV_light(!uv_light_state);
    }

    public void toggleCrouch() {
        if (!isFlyMode) {
            Log.p(TAG, "toggleCrouch()");
            context.getComponents().getPlayer().isCrouching = !context.getComponents().getPlayer().isCrouching;
        }
    }


    public boolean getIsClipMode() {
        return isClipMode;
    }

    public void setIsClipMode(boolean val) {
        isClipMode = val;
    }

    public void toggleIsClipMode() {
        Log.p(TAG, "toggleIsClipMode()");
        setIsClipMode(!getIsClipMode());
    }

    public boolean getIsFlyMode() {
        return isFlyMode;
    }

    public void setIsFlyMode(boolean val) {
        isFlyMode = val;
        speed_fall_initial = 0;
    }

    public void toggleIsFlyMode() {
        Log.p(TAG, "toggleIsFlyMode()");
        setIsFlyMode(!getIsFlyMode());
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

