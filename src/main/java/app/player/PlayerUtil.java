package app.player;

import app.GUI.HUD.HUDElements.Crosshair;
import app.GUI.HUD.HUDElements.DeathMenu;
import app.GUI.HUD.HUDElements.StatusBar;
import app.GUI.HUD.HUDUtil;
import app.GameBuilder;
import app.environment.EnvironmentUtil;
import app.structures.StructureBuilder;
import app.structures.objects.Base_Projectile;
import app.structures.objects.Base_Sphere;
import app.structures.objects.Base_Structure;
import app.utils.InventoryUtil;
import app.utils.Log;
import app.utils.ResourcesUtil;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;

public class PlayerUtil {

    private static final String TAG = "PlayerUtil";
    final double PROPERTY_MULTIPLIER_CROUCH_HEIGHT = .4;
    final double PROPERTY_SPEED_JUMP = 5;
    final double PROPERTY_SPEED_FORWARD = 3;
    final double PROPERTY_SPEED_BACKWARD = 2;
    final double PROPERTY_SPEED_SIDE = 2;
    private final Group GROUP;
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
    private boolean isCrouchToggle = false;
    boolean isOnGround = true;
    boolean isFlyMode = false;
    private double POSITION_X = 0;
    private double POSITION_Y = -200;
    private double POSITION_Z = 0;
    private double PROPERTY_MULTIPLIER_RUN;
    private double PROPERTY_MULTIPLIER_JUMP;
    private double PROPERTY_MULTIPLIER_MAX_BLOCKS_AUTOJUMP;
    private boolean isJumping = false;
    private boolean isClipMode = false;
    private boolean uv_light_state = false;
    private double speed_fall_initial = 0; // Original speed before gravity is applied; Only used in update_handler to call moveDown
    private double jump_height_initial;

    public PlayerUtil(GameBuilder ctx) {
        Log.p(TAG, "CONSTRUCTOR");

        context = ctx;
        GROUP = new Group();

        UTIL_INVENTORY = new InventoryUtil(this, 50);

        uv_light = new PointLight();
        uv_light.setLightOn(false);
        uv_light.setColor(Color.DARKBLUE);
        GROUP.getChildren().add(uv_light);

        setJumpHeightMultiplier(1);
        setMaxAutoJumpHeightMultiplier(.8);
        setRunMultiplier(1.10);
        setFlySpeed(10);
    }

    public void update_handler(double dt) {
        context.getComponents().getCamera().update_handler();

        context.getComponents().getEnvironment().generateMap(getPositionX(), getPositionZ());
        context.getComponents().getEnvironment().renderMap(getPositionX(), getPositionZ());

        GROUP.setTranslateX(getPositionX());
        GROUP.setTranslateY(-getPositionYwithHeight() - PROPERTY_HEIGHT);
        GROUP.setTranslateZ(getPositionZ());

        // Jumping Mechanism. As long as player is not in fly mode, execute mechanism
        if (!isFlyMode) {
            // If the player initiated a jump and the current position of the player is grater than the calculated height to jump to,
            // that means that the player has not gotten to that point in jumping yet so, move the player up
            double jump_height_final = jump_height_initial - PROPERTY_HEIGHT * PROPERTY_MULTIPLIER_JUMP;
            if (isJumping && getPositionYnoHeight() > jump_height_final) {
                Log.p(TAG, "Jumping from " + jump_height_initial + " to " + jump_height_final);
                moveUp(PROPERTY_SPEED_JUMP * dt);
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
            if (curr_fov < context.getComponents().getCamera().getFOVdefault() * context.getComponents().getCamera().getFOVrunningMultiplier()) {
                context.getComponents().getCamera().getCamera().setFieldOfView(curr_fov + 1);
                if (context.getEffects().getMotionBlurEnabled()) {
                    context.getEffects().EFFECT_MOTION_BLUR.setRadius(context.getEffects().EFFECT_MOTION_BLUR.getRadius() + .5);
                }
            }
        } else {
            if (getStaminaBar().getCurrStatus() < getStaminaBar().getMaxStatus()) {
                getStaminaBar().setCurrStatus(getStaminaBar().getCurrStatus() + PROPERTY_STATUS_STAMINA_REGEN_SPD * dt);
            }
            if (curr_fov > context.getComponents().getCamera().getFOVdefault()) {
                context.getComponents().getCamera().getCamera().setFieldOfView(curr_fov - 5);
            } else if (curr_fov < context.getComponents().getCamera().getFOVdefault() - 2) {
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
        Log.p(TAG, "shoot()");
        Base_Sphere sp = new Base_Sphere("Projectile", 3);
        sp.getShape().setMaterial(ResourcesUtil.metal);

        Base_Projectile proj = new Base_Projectile(context.getComponents().getEnvironment(), sp);
        proj.setSpeed(10);
        proj.shoot();
    }

    public void placeObject() {
        Base_Structure inventory_item = ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).getInventoryUtil().popCurrentItem();
        context.getComponents().getHUD().getElement(HUDUtil.INVENTORY).update();

        Log.p(TAG, "placeObject() " + inventory_item.getProps().getPROPERTY_ITEM_TAG() + " " + inventory_item.getScaleX() + " " + inventory_item.getScaleY() + " " + inventory_item.getScaleZ());

        if (inventory_item.getProps().getPROPERTY_ITEM_TAG() != StructureBuilder.UNDEFINED_TAG) {
            double posx = getPositionX();
            double posy = getPositionYwithHeight();
            double posz = getPositionZ();
            double startrotX = Math.toRadians(context.getComponents().getCamera().getRotateX());
            double startrotY = Math.toRadians(context.getComponents().getCamera().getRotateY());

            double dist_traveled = 0;
            while (dist_traveled < 200) {
                double ray_speed = 2;
                double posx_next = posx + ray_speed * Math.sin(startrotX) * Math.cos(startrotY);
                double posy_next = posy - ray_speed * Math.sin(startrotY);
                double posz_next = posz + ray_speed * Math.cos(startrotX) * Math.cos(startrotY);
                dist_traveled += ray_speed;
                System.out.println("Dist traveled: " + dist_traveled + " posx: " + posx + " posy: " + posy + " posz: " + posz);

                Point3D loc_next = new Point3D(context.getComponents().getEnvironment().convertAbsoluteToTerrainPos(posx_next), Math.floor(posy_next / context.getComponents().getEnvironment().getBlockDim()), context.getComponents().getEnvironment().convertAbsoluteToTerrainPos(posz_next));

                if (context.getComponents().getEnvironment().MAP_RENDERING.containsKey(loc_next)) {
                    AbsolutePoint3D loc = new AbsolutePoint3D(posx, posy, posz);

                    switch (inventory_item.getProps().getPROPERTY_ITEM_TYPE()) {
                        case StructureBuilder.TYPE_OBJECT:
                            Base_Structure cb = StructureBuilder.resolve(inventory_item);
                            Log.p(TAG, "placeObject() -> Copy created. Scale: X: " + cb.getScaleX() + " Y: " + cb.getScaleY() + " Z: " + cb.getScaleZ() + "; Width: " + cb.getWidth() + " Height: " + cb.getHeight() + " Depth: " + cb.getDepth() + "; Props: " + cb.getProps().toString());
                            cb.placeObject(context.getComponents().getEnvironment(), loc, true);
                            break;
                        default:
                            inventory_item.placeObject(context.getComponents().getEnvironment(), loc, true);
                            break;
                    }
                    break;
                } else {
                    posx = posx_next;
                    posy = posy_next;
                    posz = posz_next;
                }
            }
        }
    }

    public void moveDown(double val) {
        double ground_level = context.getComponents().getEnvironment().getClosestGroundLevel(getPlayerPoint3D(), true);

        // Player Y being smaller than ground level means the player is above ground. Up is -Y axis
        if (getPositionYnoHeight() < ground_level || isClipMode) {

            POSITION_Y += val;
            speed_fall_initial += EnvironmentUtil.GRAVITY;

            // if the player is more than a block above the ground , set onGround = false;
            if ((POSITION_Y - ground_level) > context.getComponents().getEnvironment().getBlockDim() * 1.5) {
                isOnGround = false;
            }
            if (!isOnGround && !isRunning && !isFlyMode) {
//                Log.p(TAG, "moveDown() -> Falling at speed " + val);
                if (context.getComponents().getCamera().getCamera().getFieldOfView() < 120) {
                    context.getComponents().getCamera().getCamera().setFieldOfView(context.getComponents().getCamera().getFOVdefault() + val * 5);
                    if (context.getEffects().getMotionBlurEnabled()) {
                        context.getEffects().EFFECT_MOTION_BLUR.setRadius(context.getEffects().EFFECT_MOTION_BLUR.getRadius() + val / 2);
                    }
                }
            }

        } else if (getPositionYnoHeight() != ground_level) {
            if (!isOnGround && !isFlyMode) {
                if (val > 7) {
                    takeDamage(val * 5);
                }
            }

            if ((POSITION_Y - ground_level) > context.getComponents().getEnvironment().getBlockDim() * getMaxAutoJumpHeightMultiplier()) {
                jump();
            } else {
                warpToGround();
            }
        }
    }

    public void moveUp(double val) {
        this.POSITION_Y -= val;
        isOnGround = false;
    }

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
        if (isFlyMode) val = PROPERTY_SPEED_FLY;
        if (isCrouching) val *= PROPERTY_MULTIPLIER_CROUCH_HEIGHT;

        double new_x = this.POSITION_X - Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_z = this.POSITION_Z - Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    public void moveLeft(double val) {
        if (isFlyMode) val = PROPERTY_SPEED_FLY;
        if (isCrouching) val *= PROPERTY_MULTIPLIER_CROUCH_HEIGHT;

        double new_z = this.POSITION_Z + Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_x = this.POSITION_X - Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    public void moveRight(double val) {
        if (isFlyMode) val = PROPERTY_SPEED_FLY;
        if (isCrouching) val *= PROPERTY_MULTIPLIER_CROUCH_HEIGHT;

        double new_x = this.POSITION_X + Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_z = this.POSITION_Z - Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }


    public void jump() {
        Log.p(TAG, "jump()");
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

    public boolean isCrouchToggle() { return isCrouchToggle; }

    public void setCrouchToggle(boolean crouchToggle) { isCrouchToggle = crouchToggle; }

    /**
     * Returns the 2D placement of the character in the world (X and Z coords)
     *
     * @return
     */
    public AbsolutePoint2D getPoint2D() {
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
        uv_light.setLightOn(state);
        uv_light_state = state;
    }

    public void toggleUVlight() {
        Log.p(TAG, "toggleUVlight()");
        setUV_light(!uv_light_state);
    }

    public InventoryUtil getInventory() {
        return UTIL_INVENTORY;
    }

    public Group getGroup() {
        return GROUP;
    }

    public double getPlayerHeight() {
        return PROPERTY_HEIGHT;
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

    public double getMaxAutoJumpHeightMultiplier() {
        return PROPERTY_MULTIPLIER_MAX_BLOCKS_AUTOJUMP;
    }

    public void setMaxAutoJumpHeightMultiplier(double val) {
        try {
            if (val >= 0) {
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


    public void resetBars() {
        getHealthBar().setCurrStatus(getHealthBar().getMaxStatus());
        getStaminaBar().setCurrStatus(getStaminaBar().getMaxStatus());
        getHungerBar().setCurrStatus(getHungerBar().getMaxStatus());
    }

    public void takeDamage(double d) {
        Log.p(TAG, "takeDamage() -> Took " + d + " damage");
        getHealthBar().setCurrStatus(getHealthBar().getCurrStatus() - d);

        if (getHealthBar().getCurrStatus() == 0) {
            die();
        }
    }

    public void die() {
        Log.p(TAG, "die()");
        ((DeathMenu) context.getComponents().getHUD().getElement(HUDUtil.DEATH)).setDead(true);
        ((Crosshair) context.getComponents().getHUD().getElement(HUDUtil.CROSSHAIR)).toggleCrosshair();
        context.getComponents().getHUD().getElement(HUDUtil.DEATH).update();
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
}

