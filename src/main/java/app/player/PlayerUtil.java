package app.player;

import app.GUI.HUD.HUDElements.DeathMenu;
import app.GUI.HUD.HUDElements.Inventory;
import app.GUI.HUD.HUDElements.StatusBar;
import app.GUI.HUD.HUDUtil;
import app.GameBuilder;
import app.environment.EnvironmentUtil;
import app.structures.StructureBuilder;
import app.structures.objects.Base_Projectile;
import app.structures.objects.Base_Structure;
import app.GUI.HUD.InventoryUtil;
import app.utils.Log;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class PlayerUtil {

    private static final String TAG = "PlayerUtil";
    final double PROPERTY_MULTIPLIER_CROUCH_HEIGHT = .4;
    final double PROPERTY_SPEED_UP = 5;
    final double PROPERTY_SPEED_FORWARD = 3;
    final double PROPERTY_SPEED_BACKWARD = 2;
    final double PROPERTY_SPEED_SIDE = 2;
    private final Group PLAYER_GROUP;
    private final InventoryUtil UTIL_INVENTORY;
    private final String PROPERTY_NAME = "Steve";
    private final int PROPERTY_WIDTH = 10;
    private final int PROPERTY_HEIGHT = 50;
    private final double PROPERTY_STATUS_STAMINA_REGEN_SPD = .05;
    private final double PROPERTY_STATUS_STAMINA_DEPLETE_SPD = 0.2;
    private final double PROPERTY_STATUS_HEALTH_REGEN_SPD = .008;
    private final double PROPERTY_STATUS_HEALTH_DEPLETE_SPD = .008;
    private final double PROPERTY_STATUS_HUNGER_DEPLETE_SPD = 0.007;
    private final PointLight uv_light;
    public GameBuilder context;
    double PROPERTY_SPEED_FLY = 5;
    boolean canJump = true;
    boolean isRunning = false;
    boolean isCrouching = false;
    boolean isOnGround = true;
    boolean isFlyMode = false;
    boolean isUnderWater = false;
    boolean isSwitchingItem = false;
    private boolean isCrouchToggle = false;
    AnimationTimer switchAnimation;
    AnimationTimer placeAnimation;
    private double POSITION_X = 0;
    private double POSITION_Y = 0;
    private double POSITION_Z = 0;
    private double PROPERTY_MULTIPLIER_RUN;
    private double PROPERTY_MULTIPLIER_JUMP;
    private double PROPERTY_MULTIPLIER_MAX_BLOCKS_AUTOJUMP;
    private boolean isJumping = false;
    private boolean isClipMode = false;
    private boolean uv_light_state = false;
    private double speed_fall_initial = 0; // Original speed before gravity is applied; Only used in update_handler to call moveDown
    private double jump_height_initial;
    private boolean didTeleport;
    private final Group HOLDING_GROUP;
    private final Rotate GROUP_ROTATE_LEFT_RIGHT;
    private final Rotate GROUP_ROTATE_UP_DOWN;

    public PlayerUtil(GameBuilder ctx) {
        Log.d(TAG, "CONSTRUCTOR");

        context = ctx;
        UTIL_INVENTORY = new InventoryUtil(this, 60);

        PLAYER_GROUP = new Group();
        HOLDING_GROUP = new Group();

        uv_light = new PointLight();
        uv_light.setLightOn(false);
        uv_light.setColor(Color.DARKBLUE);
        uv_light.setTranslateX(0);
        uv_light.setTranslateY(-getPlayerHeight());
        uv_light.setTranslateZ(0);
        PLAYER_GROUP.getChildren().add(uv_light);

        PLAYER_GROUP.getChildren().add(HOLDING_GROUP);

        GROUP_ROTATE_LEFT_RIGHT = new Rotate(0, Rotate.Y_AXIS);
        GROUP_ROTATE_UP_DOWN = new Rotate(0, Rotate.X_AXIS);
        PLAYER_GROUP.getTransforms().setAll(GROUP_ROTATE_LEFT_RIGHT, GROUP_ROTATE_UP_DOWN);

        setJumpHeightMultiplier(1);
        setMaxAutoJumpHeightMultiplier(.8);
        setRunMultiplier(1.10);
        setFlySpeed(PROPERTY_SPEED_FLY);
    }

    public void update_handler(double dt) {
        context.getComponents().getCamera().update_handler();

        context.getComponents().getEnvironment().generateMap(getPositionX(), getPositionZ());
        context.getComponents().getEnvironment().renderMap(getPositionX(), getPositionZ());

        if (didTeleport && POSITION_Y == EnvironmentUtil.LIMIT_MAX) {
            POSITION_Y = context.getComponents().getEnvironment().getClosestGroundLevel(getPlayerPoint3D(), true) - getPlayerHeight();
            didTeleport = false;
        }

        PLAYER_GROUP.setTranslateX(getPositionX());
        PLAYER_GROUP.setTranslateY(getPositionYwithHeight());
        PLAYER_GROUP.setTranslateZ(getPositionZ());
        if (!isSwitchingItem) {
            GROUP_ROTATE_LEFT_RIGHT.setAngle(context.getComponents().getCamera().getRotateX());
            GROUP_ROTATE_UP_DOWN.setAngle(context.getComponents().getCamera().getRotateY());
        }

        // Jumping Mechanism. As long as player is not in fly mode, execute mechanism
        if (!isFlyMode) {
            // If the player initiated a jump and the current position of the player is grater than the calculated height to jump to,
            // that means that the player has not gotten to that point in jumping yet so, move the player up
            double jump_height_final = jump_height_initial - PROPERTY_HEIGHT * PROPERTY_MULTIPLIER_JUMP;
            if ((isJumping && getPositionYnoHeight() > jump_height_final)) {
                Log.d(TAG,"update_handler() -> Jumping from " + getPositionYnoHeight() + " to " + jump_height_final);
                moveUp(PROPERTY_SPEED_UP * dt);
            } else {
                // if the player reached the top, set isJumping to false, and let the player fall.
                isJumping = false;
                if (!(isInWater() && ControlsUtil.pressed.contains(KeyCode.SPACE))) {
                    moveDown(speed_fall_initial * dt);
                }
            }
        }

        // Running mechanism. Changes camera FOV incrementally from 45 to 60 when running and from 60 to 45 when not running
        double curr_fov = context.getComponents().getCamera().getCamera().getFieldOfView();
        if (isRunning) {
            if (!isUnderWater) {
                if (curr_fov < context.getComponents().getCamera().getFOVdefault() * context.getComponents().getCamera().getFOVrunningMultiplier()) {
                    context.getComponents().getCamera().getCamera().setFieldOfView(curr_fov + 1);
                    if (context.getEffects().getMotionBlurEnabled()) {
                        context.getEffects().EFFECT_MOTION_BLUR.setRadius(context.getEffects().EFFECT_MOTION_BLUR.getRadius() + .5);
                    }
                }
            }
        } else {
            if (getStaminaBar().getCurrStatus() < getStaminaBar().getMaxStatus()) {
                getStaminaBar().setCurrStatus(getStaminaBar().getCurrStatus() + PROPERTY_STATUS_STAMINA_REGEN_SPD * dt);
            }
            if (isOnGround || isFlyMode || isUnderWater) {
                if (curr_fov > context.getComponents().getCamera().getFOVdefault()) {
                    context.getComponents().getCamera().getCamera().setFieldOfView(curr_fov - 5);
                } else if (curr_fov < context.getComponents().getCamera().getFOVdefault() - 2) {
                    context.getComponents().getCamera().getCamera().setFieldOfView(curr_fov + 2);
                }
            }
            if (context.getEffects().getMotionBlurEnabled() && !isUnderWater) {
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

        if(context.getComponents().getEnvironment().getTerrainShouldHaveWater()){
            double closest_ground = context.getComponents().getEnvironment().getClosestGroundLevel(getPlayerPoint3D(), true);
            if (getPositionYwithHeight() + 10 > context.getComponents().getEnvironment().PROPERTY_WATER_LEVEL && getPositionYwithHeight() < closest_ground) {
                if (!isUnderWater) {
                    context.getEffects().setMotionBlur(30);
                    context.getComponents().getEnvironment().getSkybox().sky_color = Color.DARKBLUE;
                    context.getComponents().getEnvironment().getSkybox().setCloudsVisible(false);
                    context.getComponents().getEnvironment().getSkybox().setPlanetsVisible(false);
                    updateHoldingGroup(true);
                    context.getEffects().setBrightness(-0.6);
                    isUnderWater = true;
                }
            } else {
                if (isUnderWater) {
                    if (context.getEffects().getBrightness() != 0) {
                        context.getEffects().setBrightness(0);
                    }
                    if (context.getComponents().getEnvironment().getSkybox().getShouldHaveClouds() && !context.getComponents().getEnvironment().getSkybox().getCloudsVisibile()) {
                        context.getComponents().getEnvironment().getSkybox().setCloudsVisible(true);
                    }
                    if (context.getComponents().getEnvironment().getSkybox().getShouldHavePlanets() && !context.getComponents().getEnvironment().getSkybox().getPlanetsVisible()) {
                        context.getComponents().getEnvironment().getSkybox().setPlanetsVisible(true);
                    }
                    if (context.getComponents().getEnvironment().getSkybox().sky_color != null) {
                        context.getComponents().getEnvironment().getSkybox().sky_color = null;
                    }
                    isUnderWater = false;
                }
            }
        }


        if (getStaminaBar().getCurrStatus() > getStaminaBar().getMaxStatus() / 2 && getHungerBar().getCurrStatus() > getHungerBar().getMaxStatus() / 2 && getHealthBar().getCurrStatus() != getHealthBar().getMaxStatus()) {
            // Regenerate health if stamina > half
            getHealthBar().setCurrStatus(getHealthBar().getCurrStatus() + PROPERTY_STATUS_HEALTH_REGEN_SPD * dt);
        }
        if (getHealthBar().getCurrStatus() < getHealthBar().getMaxStatus() / 3) {
            if (context.getEffects().getSaturation() > -1) {
                context.getEffects().setSaturation(1 / (getHealthBar().getMaxStatus() / 3) * getHealthBar().getCurrStatus() - 1);
            }
        }
        if (getHungerBar().getCurrStatus() > 0) {
            getHungerBar().setCurrStatus(getHungerBar().getCurrStatus() - ((getStaminaBar().getMaxStatus() - getStaminaBar().getCurrStatus() * .5) / getStaminaBar().getMaxStatus()) * PROPERTY_STATUS_HUNGER_DEPLETE_SPD * dt);
        } else {
            takeDamage(PROPERTY_STATUS_HEALTH_DEPLETE_SPD * dt);
        }
    }

    public void shoot() {
        Log.d(TAG, "shoot()");

        Base_Structure inventory_item = UTIL_INVENTORY.getCurrentItem();

        if (inventory_item.getProps().getPROPERTY_ITEM_TAG() != StructureBuilder.UNDEFINED_TAG) {
            Base_Structure duplicate = StructureBuilder.resolve(inventory_item);

            UTIL_INVENTORY.popCurrentItem();
            performPlaceAnimation();

            Base_Projectile proj = new Base_Projectile(context.getComponents().getEnvironment(), duplicate);
            proj.setSpeed(10);
            proj.shoot();
        } else {
            updateHoldingGroup(false);
        }
    }

    public void placeObject() {
        Base_Structure inventory_item = UTIL_INVENTORY.getCurrentItem();


        if (inventory_item.getProps().getPROPERTY_ITEM_TAG() != StructureBuilder.UNDEFINED_TAG) {
            Log.d(TAG, "placeObject() -> Attempting to place " + inventory_item.getProps().getPROPERTY_ITEM_TAG() + " " + inventory_item.getScaleX() + " " + inventory_item.getScaleY() + " " + inventory_item.getScaleZ());

            double posx = getPositionX();
            double posy = getPositionYwithHeight();
            double posz = getPositionZ();
            double startrotX = Math.toRadians(context.getComponents().getCamera().getRotateX());
            double startrotY = Math.toRadians(context.getComponents().getCamera().getRotateY());

            double dist_traveled = 0;
            int ray_distance_limit = 200;
            while (dist_traveled < ray_distance_limit) {
                double ray_speed = 2;
                double posx_next = posx + ray_speed * Math.sin(startrotX) * Math.cos(startrotY);
                double posy_next = posy - ray_speed * Math.sin(startrotY);
                double posz_next = posz + ray_speed * Math.cos(startrotX) * Math.cos(startrotY);
                dist_traveled += ray_speed;

                Point3D loc_next = new Point3D(context.getComponents().getEnvironment().convertAbsoluteToTerrainPos(posx_next), Math.floor(posy_next / context.getComponents().getEnvironment().getBlockDim()), context.getComponents().getEnvironment().convertAbsoluteToTerrainPos(posz_next));

                if (context.getComponents().getEnvironment().MAP_RENDERING.containsKey(loc_next)) {
                    AbsolutePoint3D loc = new AbsolutePoint3D(posx, posy, posz);

                    performPlaceAnimation();
                    Log.d(TAG, "placeObject() -> Action called for type: " + inventory_item.getProps().getPROPERTY_ITEM_TYPE());
                    switch (inventory_item.getProps().getPROPERTY_ITEM_TYPE()) {
                        case StructureBuilder.TYPE_OBJECT:
                            Base_Structure cb = StructureBuilder.resolve(inventory_item);
                            Log.d(TAG, "placeObject() -> Copy created. Scale: X: " + cb.getScaleX() + " Y: " + cb.getScaleY() + " Z: " + cb.getScaleZ() + "; Width: " + cb.getWidth() + " Height: " + cb.getHeight() + " Depth: " + cb.getDepth() + "; Props: " + cb.getProps().toString());
                            cb.placeObject(context.getComponents().getEnvironment(), loc, true);
                            break;
                        default:
                            inventory_item.placeObject(context.getComponents().getEnvironment(), loc, true);
                            break;
                    }
                    UTIL_INVENTORY.popCurrentItem();
                    context.getComponents().getHUD().getElement(HUDUtil.INVENTORY).update();

                    break;
                } else {
                    posx = posx_next;
                    posy = posy_next;
                    posz = posz_next;
                }
            }
            if(dist_traveled > ray_distance_limit){
                Log.d(TAG,"placeObject() -> Cannot place that far!");
            }
        } else{
            Log.d(TAG, "placeObject() -> Cannot place " + inventory_item.getProps().getPROPERTY_ITEM_TAG());
        }
    }

    public void moveDown(double val) {
        if (isInWater() && !isFlyMode) {
            val = PROPERTY_SPEED_UP;
            val *= (EnvironmentUtil.WATER_DRAG_COEFFICIENT);
        }


        double ground_level = context.getComponents().getEnvironment().getClosestGroundLevel(getPlayerPoint3D(), true);

        // Player Y being smaller than ground level means the player is above ground. Up is -Y axis
        if (getPositionYnoHeight() < ground_level || isClipMode) {

            POSITION_Y += val;
            if (speed_fall_initial < EnvironmentUtil.VELOCITY_TERMINAL) {
                speed_fall_initial += EnvironmentUtil.GRAVITY;
            }

            // if the player is more than a block above the ground , set onGround = false;
            if ((POSITION_Y - ground_level) > context.getComponents().getEnvironment().getBlockDim() * 1.5) {
                isOnGround = false;
            }
            if (!isOnGround && !isRunning && !isFlyMode && !isUnderWater) {
                if (context.getComponents().getCamera().getCamera().getFieldOfView() < 100) {
                    context.getComponents().getCamera().getCamera().setFieldOfView(context.getComponents().getCamera().getFOVdefault() + val * 5);
                    if (context.getEffects().getMotionBlurEnabled()) {
                        context.getEffects().EFFECT_MOTION_BLUR.setRadius(context.getEffects().EFFECT_MOTION_BLUR.getRadius() + val / 2);
                    }
                }
            }

        } else if (getPositionYnoHeight() != ground_level) {
            if (!isOnGround && !isFlyMode) {
                if (val > 8) {
                    takeDamage((val-8) * 8);
                }
            }

            if ((POSITION_Y - ground_level) > context.getComponents().getEnvironment().getBlockDim() * getMaxAutoJumpHeightMultiplier()) {
                jump();
            } else {
                warpToGround();
            }
        }

        if (POSITION_Y > EnvironmentUtil.LIMIT_MIN) {
            takeDamage(1);
        }
    }

    public void moveUp(double val) {
        if (isInWater() && !isFlyMode) val *= EnvironmentUtil.WATER_DRAG_COEFFICIENT;

        this.POSITION_Y -= val;
        isOnGround = false;
    }

    public boolean isCrouchToggle() { return isCrouchToggle; }

    public void setCrouchToggle(boolean crouchToggle) { isCrouchToggle = crouchToggle; }

    /**
     * Function used by moveForward, moveBackward, moveLeft, moveRight
     *
     * @param new_x
     * @param new_z
     */
    public void handle_collision(double new_x, double new_z) {
        double ground_level_x = context.getComponents().getEnvironment().getClosestGroundLevel(new AbsolutePoint3D(new_x, getPositionYwithHeight(), this.POSITION_Z), true);
        double ground_level_z = context.getComponents().getEnvironment().getClosestGroundLevel(new AbsolutePoint3D(this.POSITION_X, getPositionYwithHeight(), new_z), true);

        if ((POSITION_Y - ground_level_x < context.getComponents().getEnvironment().getBlockDim() * PROPERTY_MULTIPLIER_MAX_BLOCKS_AUTOJUMP * 1.5) || isClipMode) {
            this.POSITION_X = new_x;
        }
        if ((POSITION_Y - ground_level_z < context.getComponents().getEnvironment().getBlockDim() * PROPERTY_MULTIPLIER_MAX_BLOCKS_AUTOJUMP * 1.5) || isClipMode) {
            this.POSITION_Z = new_z;
        }
    }

    public void moveForward(double val) {
        if (isInWater() && !isFlyMode) val *= EnvironmentUtil.WATER_DRAG_COEFFICIENT;
        if (isFlyMode) val = PROPERTY_SPEED_FLY;
        if (isCrouching) val *= PROPERTY_MULTIPLIER_CROUCH_HEIGHT;

        if (isRunning) {
            val *= PROPERTY_MULTIPLIER_RUN;
            getStaminaBar().setCurrStatus(getStaminaBar().getCurrStatus() - PROPERTY_STATUS_STAMINA_DEPLETE_SPD);
        }

        double new_x = this.POSITION_X + Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_z = this.POSITION_Z + Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    public void moveBackward(double val) {
        if (isInWater() && !isFlyMode) val *= EnvironmentUtil.WATER_DRAG_COEFFICIENT;
        if (isFlyMode) val = PROPERTY_SPEED_FLY;
        if (isCrouching) val *= PROPERTY_MULTIPLIER_CROUCH_HEIGHT;

        double new_x = this.POSITION_X - Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_z = this.POSITION_Z - Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    public void moveLeft(double val) {
        if (isInWater() && !isFlyMode) val *= EnvironmentUtil.WATER_DRAG_COEFFICIENT;
        if (isFlyMode) val = PROPERTY_SPEED_FLY;
        if (isCrouching) val *= PROPERTY_MULTIPLIER_CROUCH_HEIGHT;

        double new_z = this.POSITION_Z + Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_x = this.POSITION_X - Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    public void moveRight(double val) {
        if (isInWater() && !isFlyMode) val *= EnvironmentUtil.WATER_DRAG_COEFFICIENT;
        if (isFlyMode) val = PROPERTY_SPEED_FLY;
        if (isCrouching) val *= PROPERTY_MULTIPLIER_CROUCH_HEIGHT;

        double new_x = this.POSITION_X + Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_z = this.POSITION_Z - Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    public void jump() {
        Log.d(TAG, "jump()");
        isJumping = true;
        canJump = false;
        jump_height_initial = getPositionYnoHeight();
        isCrouching = false;
    }

    public void setIsRunning(boolean run) {
        StatusBar bar = (StatusBar) context.getComponents().getHUD().getElement(HUDUtil.STAMINA);

        if (!isRunning && run && bar.getCurrStatus() > bar.getMaxStatus() / 3) {
            isRunning = true;
        } else if (isRunning && run && bar.getCurrStatus() > 0) {
            isRunning = true;
        } else {
            if (isRunning) {
                takeDamage(getHealthBar().getMaxStatus() / 25);
            }
            isRunning = false;
        }
    }

    private void warpToGround() {
        double ground_level = context.getComponents().getEnvironment().getClosestGroundLevel(getPlayerPoint3D(), true);
        POSITION_Y = ground_level;
        isOnGround = true;
        speed_fall_initial = 0;
    }

    public double getPositionX() {
        return POSITION_X;
    }

    public double getPositionYnoHeight() {
        return POSITION_Y;
    }

    public double getPositionYwithHeight() {
        return POSITION_Y - PROPERTY_HEIGHT;
    }

    public double getPositionZ() {
        return POSITION_Z;
    }

    /**
     * Returns the 2D placement of the character in the world (X and Z coords)
     *
     * @return
     */
    public AbsolutePoint2D getPlayerPoint2D() {
        return new AbsolutePoint2D(getPositionX(), getPositionZ());
    }

    /**
     * Returns the 3D placement of the character in the world (X, Y, Z coords)
     *
     * @return
     */
    public AbsolutePoint3D getPlayerPoint3D() {
        return new AbsolutePoint3D(getPositionX(), getPositionYwithHeight(), getPositionZ());
    }

    public void setPosition(double newx, double newy, double newz) {
        POSITION_X = newx;
        POSITION_Y = newy;
        POSITION_Z = newz;
    }

    public void setUV_light(boolean state) {
        Log.d(TAG, "setUV_light() -> " + state);

        uv_light.setLightOn(state);
        uv_light_state = state;
    }

    public void toggleUVlight() {
        setUV_light(!uv_light_state);
    }

    public InventoryUtil getInventory() {
        return UTIL_INVENTORY;
    }

    public Group getGroup() {
        return PLAYER_GROUP;
    }

    public double getPlayerHeight() {
        return PROPERTY_HEIGHT;
    }


    public void toggleCrouch() {
        if (!isFlyMode) {
            Log.d(TAG, "toggleCrouch()");
            context.getComponents().getPlayer().isCrouching = !context.getComponents().getPlayer().isCrouching;
        }
    }

    public boolean getIsClipMode() {
        return isClipMode;
    }

    public void setIsClipMode(boolean val) {
        Log.d(TAG, "setIsClipMode() -> " + val);

        isClipMode = val;
    }

    public void toggleIsClipMode() {
        setIsClipMode(!getIsClipMode());
    }

    public boolean getIsFlyMode() {
        return isFlyMode;
    }

    public void setIsFlyMode(boolean val) {
        Log.d(TAG, "setIsFlyMode() -> " + val);

        isFlyMode = val;
        speed_fall_initial = 0;
    }

    public void toggleIsFlyMode() {
        setIsFlyMode(!getIsFlyMode());
    }

    public double getMaxAutoJumpHeightMultiplier() {
        return PROPERTY_MULTIPLIER_MAX_BLOCKS_AUTOJUMP;
    }

    public void setMaxAutoJumpHeightMultiplier(double val) {
        try {
            if (val >= 0) {
                Log.d(TAG,"setMaxAutoJumpHeightMultiplier() -> " + val);

                this.PROPERTY_MULTIPLIER_MAX_BLOCKS_AUTOJUMP = val;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public double getFlySpeed() {
        return PROPERTY_SPEED_FLY;
    }

    public void setFlySpeed(double spd) {
        try {
            if (spd >= 0) {
                Log.d(TAG,"setFlySpeed() -> " + spd);

                PROPERTY_SPEED_FLY = spd;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public double getRunMultiplier() {
        return PROPERTY_MULTIPLIER_RUN;
    }

    public void setRunMultiplier(double mult) {
        try {
            if (mult >= 0) {
                Log.d(TAG,"setRunMultiplier() -> " + mult);

                PROPERTY_MULTIPLIER_RUN = mult;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public double getJumpHeightMultiplier() {
        return PROPERTY_MULTIPLIER_JUMP;
    }

    public void setJumpHeightMultiplier(double mult) {
        try {
            if (mult >= 0) {
                Log.d(TAG,"setJumpHeightMultiplier() -> " + mult);
                PROPERTY_MULTIPLIER_JUMP = mult;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public StatusBar getStaminaBar() {
        return (StatusBar) context.getComponents().getHUD().getElement(HUDUtil.STAMINA);
    }

    public StatusBar getHealthBar() {
        return (StatusBar) context.getComponents().getHUD().getElement(HUDUtil.HEALTH);
    }

    public StatusBar getTempBar() {
        return (StatusBar) context.getComponents().getHUD().getElement(HUDUtil.TEMPERATURE);
    }

    public StatusBar getHungerBar() {
        return (StatusBar) context.getComponents().getHUD().getElement(HUDUtil.HUNGER);
    }

    public void resetStatusBars() {
        Log.d(TAG,"resetStatusBars()");
        getHealthBar().setCurrStatus(getHealthBar().getMaxStatus());
        getStaminaBar().setCurrStatus(getStaminaBar().getMaxStatus());
        getHungerBar().setCurrStatus(getHungerBar().getMaxStatus());
    }

    public void takeDamage(double d) {
        Log.d(TAG, "takeDamage() -> Took " + d + " damage");
        getHealthBar().setCurrStatus(getHealthBar().getCurrStatus() - d);

        if (getHealthBar().getCurrStatus() == 0) {
            die();
        }
    }

    public void die() {
        Log.d(TAG, "die()");
        ((DeathMenu) context.getComponents().getHUD().getElement(HUDUtil.DEATH)).setDead(true);
        context.getComponents().getHUD().getElement(HUDUtil.DEATH).update();
    }

    public void reset() {
        Log.d(TAG, "reset()");
        setPosition(0, 0, 0);

        isClipMode = false;
        isRunning = false;
        isFlyMode = false;
        isJumping = false;

        resetStatusBars();
        context.getComponents().getCamera().reset();
        context.getEffects().resetEffects();
        context.getComponents().getGameSceneControls().reset();
        updateHoldingGroup(false);
    }

    public void teleportRandom() {
        double randomX = Math.random() * 100000;
        double randomZ = Math.random() * 100000;
        setPosition(randomX, EnvironmentUtil.LIMIT_MAX, randomZ);
        didTeleport = true;

        Log.d(TAG,"teleportRandom() -> X: " + randomX + "  Z: " + randomZ);
    }

    public void setInventoryIndexOffset(int i) {
        if (i < 0) {
            UTIL_INVENTORY.moveCurrIndex(i);
        }
        if (i > 0) {
            if (((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).isExtendedInventoryDisplayed()) {
                UTIL_INVENTORY.moveCurrIndex(i);
            } else {
                if (UTIL_INVENTORY.getCurrentIndex() != ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getSlotsDisplayed() - 1) {
                    UTIL_INVENTORY.moveCurrIndex(i);
                }
            }
        }

        Base_Structure currItem = UTIL_INVENTORY.getCurrentItem();
        Log.d(TAG,"setInventoryIndexOffset() -> " + i + " -> Tag: " + currItem.getProps().getPROPERTY_ITEM_TAG() + "; ItmType: " + currItem.getProps().getPROPERTY_ITEM_TYPE()+ "; ObjType: " + currItem.getProps().getPROPERTY_OBJECT_TYPE());

        updateHoldingGroup(false);
    }

    public void setInventoryIndex(int i) {
        ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().setCurrentIndex(i);

        updateHoldingGroup(false);
    }

    public void updateHoldingGroup(boolean shouldHide) {
        context.getComponents().getHUD().getElement(HUDUtil.INVENTORY).update();
        Base_Structure inventoryItem = UTIL_INVENTORY.getCurrentItem();

        if (!inventoryItem.getProps().getPROPERTY_ITEM_TAG().equals(StructureBuilder.UNDEFINED_TAG)) {


            if (switchAnimation != null) {
                switchAnimation.stop();
            }
            switchAnimation = new AnimationTimer() {
                long animation_start = -1;
                boolean finished = false;
                @Override
                public void handle(long l) {
                    if (animation_start == -1) {
                        animation_start = l;
                    }
                    double angle = (Math.sin((l - animation_start) / 170000000.0)) * 30;
                    GROUP_ROTATE_LEFT_RIGHT.setAngle(GROUP_ROTATE_LEFT_RIGHT.getAngle() + angle);
                    GROUP_ROTATE_UP_DOWN.setAngle(GROUP_ROTATE_UP_DOWN.getAngle() + angle);

                    Base_Structure new_item = StructureBuilder.resolve(inventoryItem);
                    double scale = new_item.getBoundsInParent().getWidth() / 2;
                    new_item.setScaleAll(scale);
                    new_item.setTranslateZ(20);
                    new_item.setTranslateY(8);
                    new_item.setTranslateX(10);
                    if (angle > 29 && !HOLDING_GROUP.getChildren().contains(new_item)) {
                        if(shouldHide){
                            HOLDING_GROUP.getChildren().clear();
                        } else{
                            HOLDING_GROUP.getChildren().setAll(new_item);
                        }
                        finished = true;
                    }
                    if (angle <= -.01 && finished) {
                        this.stop();
                        animation_start = -1;
                    }
                }
            };

            switchAnimation.start();
        } else {
            HOLDING_GROUP.getChildren().clear();
        }

    }

    public void performPlaceAnimation() {
        if (placeAnimation != null) {
            placeAnimation.stop();
        }
        placeAnimation = new AnimationTimer() {
            long animation_start = -1;

            @Override
            public void handle(long l) {
                if (animation_start == -1) {
                    animation_start = l;
                }
                double angle = (Math.cos((l - animation_start) / 150000000.0)) * 25;
                GROUP_ROTATE_LEFT_RIGHT.setAngle(GROUP_ROTATE_LEFT_RIGHT.getAngle() + angle);
                GROUP_ROTATE_UP_DOWN.setAngle(GROUP_ROTATE_UP_DOWN.getAngle() + angle);
                if (angle <= -.01) {
                    this.stop();
                    animation_start = -1;
                }
            }
        };
        placeAnimation.start();
    }

    public boolean isInWater() {
        if(!context.getComponents().getEnvironment().getTerrainShouldHaveWater()){
            return false;
        }
        return (getPositionYwithHeight() + getPlayerHeight() > context.getComponents().getEnvironment().PROPERTY_WATER_LEVEL);
    }

}

