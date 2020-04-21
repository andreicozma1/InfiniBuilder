package app.player;

import app.GUI.HUD.HUDElements.DeathMenu;
import app.GUI.HUD.HUDElements.Inventory;
import app.GUI.HUD.HUDElements.ItemInfo;
import app.GUI.HUD.HUDElements.StatusBar;
import app.GUI.HUD.HUDUtil;
import app.GUI.HUD.InventoryUtil;
import app.GameBuilder;
import app.environment.EnvironmentUtil;
import app.structures.ObjectBuilder;
import app.structures.ObjectProperties;
import app.structures.objects.BaseObject;
import app.structures.weapons.Base_Projectile;
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

    public GameBuilder context;

    private final String PROPERTY_NAME = "Steve";
    private final int PROPERTY_WIDTH = 10;
    private final int PROPERTY_HEIGHT = 50;

    // hold the player's current X Y Z coordinates
    private double POSITION_X = 0;
    private double POSITION_Y = 0;
    private double POSITION_Z = 0;

    final double PROPERTY_SPEED_UP = 5;
    final double PROPERTY_SPEED_FORWARD = 3;
    final double PROPERTY_SPEED_BACKWARD = 2;
    final double PROPERTY_SPEED_SIDE = 2;
    double PROPERTY_SPEED_FLY = 5;

    // these hold the values of multipliers set in settings for player attributes
    private double PROPERTY_MULTIPLIER_RUN;
    private double PROPERTY_MULTIPLIER_JUMP;
    private double PROPERTY_MULTIPLIER_MAX_BLOCKS_AUTOJUMP;
    public double PROPERTY_MULTIPLIER_CROUCH_HEIGHT = .4;

    private final InventoryUtil UTIL_INVENTORY;
    private final Group PLAYER_GROUP; // this is the root group which is the actual player
    private final Group HOLDING_GROUP; // this group is what the player is holding in the right hand
    // Rotation transformations for the player group
    private final Rotate GROUP_ROTATE_LEFT_RIGHT;
    private final Rotate GROUP_ROTATE_UP_DOWN;

    private final PointLight ITEM_UV_LIGHT;
    private boolean ITEM_UV_LIGHT_STATE = false;

    // these animation timers handle switching and placing animations
    AnimationTimer ANIMATION_SWITCH;
    AnimationTimer ANIMATION_PLACE;

    // these properties define the speeds at which the status bars regenerate or deplete
    private final double PROPERTY_STATUS_STAMINA_REGEN_SPD = .07;
    private final double PROPERTY_STATUS_STAMINA_DEPLETE_SPD = 0.2;
    private final double PROPERTY_STATUS_HEALTH_REGEN_SPD = .008;
    private final double PROPERTY_STATUS_HEALTH_DEPLETE_SPD = .008;
    private final double PROPERTY_STATUS_HUNGER_DEPLETE_SPD = 0.007;

    // these variables are used to determine current actions the player is taking
    boolean canJump = true;
    private boolean isJumping = false;
    boolean isRunning = false;
    boolean isCrouching = false;
    boolean isOnGround = true;
    boolean isFlyMode = false;
    private boolean isClipMode = false;
    boolean isUnderWater = false;
    private boolean isSwitchingItem = false;
    private double jump_height_initial;

    private boolean isCrouchToggle = false;
    private boolean isBoostFlySpeed = false;

    private double speed_fall_initial = 0; // Original speed before gravity is applied; Only used in update_handler to call moveDown
    private boolean didTeleport;

    public PlayerUtil(GameBuilder ctx) {
        Log.d(TAG, "CONSTRUCTOR");

        context = ctx;
        UTIL_INVENTORY = new InventoryUtil(this, 60);

        PLAYER_GROUP = new Group();
        HOLDING_GROUP = new Group();

        ITEM_UV_LIGHT = new PointLight();
        ITEM_UV_LIGHT.setLightOn(false);
        ITEM_UV_LIGHT.setColor(Color.DARKBLUE);
        ITEM_UV_LIGHT.setTranslateX(0);
        ITEM_UV_LIGHT.setTranslateY(-getPlayerHeight());
        ITEM_UV_LIGHT.setTranslateZ(0);
        PLAYER_GROUP.getChildren().add(ITEM_UV_LIGHT);

        PLAYER_GROUP.getChildren().add(HOLDING_GROUP);

        GROUP_ROTATE_LEFT_RIGHT = new Rotate(0, Rotate.Y_AXIS);
        GROUP_ROTATE_UP_DOWN = new Rotate(0, Rotate.X_AXIS);
        PLAYER_GROUP.getTransforms().setAll(GROUP_ROTATE_LEFT_RIGHT, GROUP_ROTATE_UP_DOWN);

        setJumpHeightMultiplier(1);
        setMaxAutoJumpHeightMultiplier(.8);
        setRunMultiplier(1.10);
        setFlySpeed(PROPERTY_SPEED_FLY);
    }

    /**
     * Main update function which gets called every tick
     * This handles everything about the player, including physics for running, jumping, swimming
     * As well as status bars, etc.
     * @param dt
     */
    public void update_handler(double dt) {
        context.getComponents().getCamera().update_handler();

        context.getComponents().getEnvironment().generateMap(getPositionX(), getPositionZ());
        context.getComponents().getEnvironment().renderMap(getPositionX(), getPositionZ());

        // if player recently teleported randomly from ControlsUtil action, update the player's Y position to be above ground
        if (didTeleport && POSITION_Y == EnvironmentUtil.LIMIT_MAX) {
            POSITION_Y = context.getComponents().getEnvironment().getClosestGroundLevel(getPlayerPoint3D(), true) - getPlayerHeight();
            didTeleport = false;
        }

        // determine the player's current height, based off whether the player is crouching or not.
        // this is used to update the height of the PLAYER_GROUP (including holding group)
        // such that the items lower too when the player is crouching
        double height = context.getComponents().getPlayer().getPlayerHeight();
        if (context.getComponents().getPlayer().isCrouching) {
            height /= 2;
            height += height * context.getComponents().getPlayer().PROPERTY_MULTIPLIER_CROUCH_HEIGHT;
        }

        // translate the player group based off the player's current coordinates and the newly calculated height
        PLAYER_GROUP.setTranslateX(getPositionX());
        PLAYER_GROUP.setTranslateY(getPositionYnoHeight() - height);
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
                Log.d(TAG, "update_handler() -> Jumping from " + getPositionYnoHeight() + " to " + jump_height_final);
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
            // We don't want to be able to run underwater
            if (!isUnderWater) {
                if (curr_fov < context.getComponents().getCamera().getFOVdefault() * context.getComponents().getCamera().getFOVrunningMultiplier()) {
                    context.getComponents().getCamera().getCamera().setFieldOfView(curr_fov + 1);
                    // with the camera FOV change, we also update the motion blur from running (if enabled)
                    if (context.getEffects().getMotionBlurEnabled()) {
                        context.getEffects().EFFECT_MOTION_BLUR.setRadius(context.getEffects().EFFECT_MOTION_BLUR.getRadius() + .5);
                    }
                }
            }
        } else {
            // If the player is not running, we want the stamina to regenerate as long as it hasn't reached it's max
            if (getStaminaBar().getCurrStatus() < getStaminaBar().getMaxStatus()) {
                getStaminaBar().setCurrStatus(getStaminaBar().getCurrStatus() + PROPERTY_STATUS_STAMINA_REGEN_SPD * dt);
            }
            // this mechanism handles gradually resetting the FOV back to it's default when the player stopped running
            if (isOnGround || isFlyMode || isUnderWater) {
                if (curr_fov > context.getComponents().getCamera().getFOVdefault()) {
                    context.getComponents().getCamera().getCamera().setFieldOfView(curr_fov - 5);
                } else if (curr_fov < context.getComponents().getCamera().getFOVdefault() - 2) {
                    context.getComponents().getCamera().getCamera().setFieldOfView(curr_fov + 2);
                }
            }
            // this mechanism handles gradually decreasing the motion blur (as long as the player is not underwater)
            // we want the motion blur to stay if underwater (as an underwater effect)
            if (context.getEffects().getMotionBlurEnabled() && !isUnderWater) {
                if (context.getEffects().EFFECT_MOTION_BLUR.getRadius() > 0) {
                    double deBlurSpeed = 1;
                    // if flying, we want the motion blur to decrease much faster
                    if (isFlyMode) {
                        deBlurSpeed = 5;
                    }
                    context.getEffects().EFFECT_MOTION_BLUR.setRadius(context.getEffects().EFFECT_MOTION_BLUR.getRadius() - deBlurSpeed);
                } else {
                    // if somehow the radius of the motion blur got below 0 (because of subtracting) reset it back to 0
                    context.getEffects().EFFECT_MOTION_BLUR.setRadius(0);
                }
            }
        }

        // Handles the underwater effects (only do this if water is even enabled)
        if (context.getComponents().getEnvironment().getTerrainShouldHaveWater()) {
            double closest_ground = context.getComponents().getEnvironment().getClosestGroundLevel(getPlayerPoint3D(), true);
            // determine whether the player is underwater)
            if (getPositionYwithHeight() + 10 > context.getComponents().getEnvironment().PROPERTY_WATER_LEVEL && getPositionYwithHeight() < closest_ground) {
                if (!isUnderWater) {
                    // if player wasn't underwater but just got underwater, set all the effects
                    context.getEffects().setMotionBlur(30);
                    context.getComponents().getEnvironment().getSkybox().SKY_COLOR = Color.DARKBLUE;
                    context.getComponents().getEnvironment().getSkybox().setCloudsVisible(false);
                    context.getComponents().getEnvironment().getSkybox().setPlanetsVisible(false);
                    updateHoldingGroup(true);
                    context.getEffects().setBrightness(-0.6);
                    // also set that we are now underwater
                    isUnderWater = true;
                }
            } else {
                // if player got out of water (meaning isUnderWater should be true now, we want to take away the effects)
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
                    if (context.getComponents().getEnvironment().getSkybox().SKY_COLOR != null) {
                        context.getComponents().getEnvironment().getSkybox().SKY_COLOR = null;
                    }
                    // finally, acknowledge that the player got out of water
                    isUnderWater = false;
                }
            }
        }

        // STATUS BAR HANDLING
        if (getStaminaBar().getCurrStatus() > getStaminaBar().getMaxStatus() / 2 && getHungerBar().getCurrStatus() > getHungerBar().getMaxStatus() / 2 && getHealthBar().getCurrStatus() != getHealthBar().getMaxStatus()) {
            // Regenerate health if stamina > half
            getHealthBar().setCurrStatus(getHealthBar().getCurrStatus() + PROPERTY_STATUS_HEALTH_REGEN_SPD * dt);
        }
        if (getHealthBar().getCurrStatus() < getHealthBar().getMaxStatus() / 3) {
            // HealthBar handling - decrease screen saturation of health is low (death effect grayscale)
            if (context.getEffects().getSaturation() > -1) {
                context.getEffects().setSaturation(1 / (getHealthBar().getMaxStatus() / 3) * getHealthBar().getCurrStatus() - 1);
            }
        }
        // Hunger Bar Handling. If hungerbar is > 0, then decrease hunger
        if (getHungerBar().getCurrStatus() > 0) {
            getHungerBar().setCurrStatus(getHungerBar().getCurrStatus() - ((getStaminaBar().getMaxStatus() - getStaminaBar().getCurrStatus() * .5) / getStaminaBar().getMaxStatus()) * PROPERTY_STATUS_HUNGER_DEPLETE_SPD * dt);
            // If the temperature is low, hunger bar depletes faster!
            if (getTempBar().getCurrStatus() < getTempBar().getMaxStatus() / 2) {
                getHungerBar().setCurrStatus(getHungerBar().getCurrStatus() - ((getTempBar().getMaxStatus() - getTempBar().getCurrStatus()) / getTempBar().getMaxStatus()) / 40 * dt);
            }
        } else {
            // When the hunger bar reached 0, start decreasing health
            takeDamage(PROPERTY_STATUS_HEALTH_DEPLETE_SPD * dt);
        }

        // TemperatureBar Handling. Determine the environment temperature based off player's height.
        double val = (getTempBar().getMaxStatus() / (EnvironmentUtil.PROPERTY_DESERT_LEVEL_1 - EnvironmentUtil.PROPERTY_ICE_LEVEL)) * (getPositionYwithHeight() - EnvironmentUtil.PROPERTY_ICE_LEVEL);
        if (val > 0 && val < getTempBar().getMaxStatus()) {
            getTempBar().setCurrStatus(val);
        }

        // StaminaBar handling - slowly regenerate stamina at all times
        if (getStaminaBar().getCurrStatus() > 0 && getTempBar().getCurrStatus() > getTempBar().getMaxStatus() * .7) {
            // if the temperature is Hot, we want the stamina to regenerate much slower.
            getStaminaBar().setCurrStatus(getStaminaBar().getCurrStatus() - (getTempBar().getCurrStatus() / getTempBar().getMaxStatus()) / 12 * dt);
        }
        // END STATUS BAR HANDLING
    }

    /**
     * This function handles player tossing/shooting the current object in the inventory
     */
    public void shootAction() {
        Log.d(TAG, "shoot()");
        // Only toss item if the ItemInfo menu is not displayed, so we don't accidentally shoot something
        if (!((ItemInfo) context.getComponents().getHUD().getElement(HUDUtil.ITEM_INFO)).isDisplayed()) {
            // get the current item into a base structure
            BaseObject inventory_item = UTIL_INVENTORY.getCurrentItem();

            // check if the item in the current slot, then attempt to create a projectile
            if (!inventory_item.getProps().getPROPERTY_ITEM_TAG().equals(ObjectProperties.UNDEFINED_TAG)) {
                BaseObject duplicate = ObjectBuilder.clone(inventory_item);

                // remove the item from the inventory
                UTIL_INVENTORY.popCurrentItem();
                // perform an animation (this also updates the holding group)
                performPlaceAnimation();

                // create projectile object based off the duplicate we created
                Base_Projectile proj = new Base_Projectile(context.getComponents().getEnvironment(), duplicate);

                // TODO - make this shoot speed variable through a setter
                proj.setSpeed(10);
                proj.shoot();
            } else {
                // if attempting to shoot nothing, it hides the holding group
                updateHoldingGroup(false);
            }
        }

    }

    /**
     * Implements 360 degree rotational item placement, traces an invisible ray in order to find the item's final position
     * TODO - tweak for more accuracy
     */
    public void placeAction() {
        BaseObject inventory_item = UTIL_INVENTORY.getCurrentItem();

        if (!inventory_item.getProps().getPROPERTY_ITEM_TAG().equals(ObjectProperties.UNDEFINED_TAG)) {
            Log.d(TAG, "placeObject() -> Attempting to place " + inventory_item.getProps().getPROPERTY_ITEM_TAG() + " " + inventory_item.getScaleX() + " " + inventory_item.getScaleY() + " " + inventory_item.getScaleZ());

            // get the player's current position from which to start drawing the ray
            double posx = getPositionX();
            double posy = getPositionYwithHeight();
            double posz = getPositionZ();
            // get the initial rotation of the gamera to be able to draw a ray in a direction orthogonal to the player's position
            double startrotX = Math.toRadians(context.getComponents().getCamera().getRotateX());
            double startrotY = Math.toRadians(context.getComponents().getCamera().getRotateY());

            double dist_traveled = 0;
            // This defines the distance limit at which objects should be able to be placed
            // TODO - make this customizable with a setter
            int ray_distance_limit = 200;

            double ray_speed = 2;
            // run this loop as long as we haven't exceeded the ray distance limit
            while (dist_traveled < ray_distance_limit) {
                // calculate the next position of the ray as it travels, using trigonometry
                double posx_next = posx + ray_speed * Math.sin(startrotX) * Math.cos(startrotY);
                double posy_next = posy - ray_speed * Math.sin(startrotY);
                double posz_next = posz + ray_speed * Math.cos(startrotX) * Math.cos(startrotY);
                dist_traveled += ray_speed;

                // convert the ray's current absolute coordinates to Terrain coordinates so that we can check if our ray crossed terrain
                Point3D loc_next = new Point3D(context.getComponents().getEnvironment().convertAbsoluteToTerrainPos(posx_next), Math.floor(posy_next / context.getComponents().getEnvironment().getBlockDim()), context.getComponents().getEnvironment().convertAbsoluteToTerrainPos(posz_next));

                // this determines whether we hit terrain, if we do, we perform this
                if (context.getComponents().getEnvironment().MAP_RENDERING.containsKey(loc_next)) {
                    // make an absolutePoint3D to be able to place the object at that position
                    AbsolutePoint3D loc = new AbsolutePoint3D(posx, posy, posz);

                    // remove the item from the inventory
                    UTIL_INVENTORY.popCurrentItem();
                    // do this
                    performPlaceAnimation();
                    Log.d(TAG, "placeObject() -> Action called for type: " + inventory_item.getProps().getPROPERTY_ITEM_TYPE());

                    // perform different actions based on what object we are holding
                    switch (inventory_item.getProps().getPROPERTY_ITEM_TYPE()) {
                        case ObjectProperties.TYPE_OBJECT:
                            // if we are holding an object, simply create a copy of it and place it on the ground
                            BaseObject cb = ObjectBuilder.clone(inventory_item);
                            Log.d(TAG, "placeObject() -> Copy created. Scale: X: " + cb.getScaleX() + " Y: " + cb.getScaleY() + " Z: " + cb.getScaleZ() + "; Width: " + cb.getWidth() + " Height: " + cb.getHeight() + " Depth: " + cb.getDepth() + "; Props: " + cb.getProps().toString());
                            cb.placeObject(context.getComponents().getEnvironment(), loc, true);
                            break;
                        default:
                            // if we are holding anything else (like a structure), we perform it's corresponding action
                            inventory_item.placeObject(context.getComponents().getEnvironment(), loc, true);
                            break;
                    }
                    break;
                } else {
                    // otherwise, update the current position coordinates and continue the loop
                    posx = posx_next;
                    posy = posy_next;
                    posz = posz_next;
                }
            }
            if (dist_traveled > ray_distance_limit) {
                Log.d(TAG, "placeObject() -> Cannot place that far!");
            }
        } else {
            Log.d(TAG, "placeObject() -> Cannot place " + inventory_item.getProps().getPROPERTY_ITEM_TAG());
        }
    }

    /**
     * Move-down mechanics. Takes in the amount the player should move down
     * @param val
     */
    public void moveDown(double val) {
        if (isInWater() && !isFlyMode) {
            // if underwater, we want to be affected by the water drag coefficient (undes in Fly mode)
            val = PROPERTY_SPEED_UP;
            val *= (EnvironmentUtil.WATER_DRAG_COEFFICIENT);
        }

        double ground_level = context.getComponents().getEnvironment().getClosestGroundLevel(getPlayerPoint3D(), true);

        // Player Y being smaller than ground level means the player is above ground. Up is -Y axis
        if (getPositionYnoHeight() < ground_level || isClipMode) {

            // decrease the player's position (Positive is DOWN)
            POSITION_Y += val;
            // increase the current velocity due to gravity
            if (speed_fall_initial < EnvironmentUtil.VELOCITY_TERMINAL) {
                speed_fall_initial += EnvironmentUtil.GRAVITY;
            }

            // if the player is more than a block above the ground , set onGround = false;
            if ((POSITION_Y - ground_level) > context.getComponents().getEnvironment().getBlockDim() * 1.5) {
                isOnGround = false;
            }
            // handle falling FOV increase
            if (!isOnGround && !isRunning && !isFlyMode && !isUnderWater) {
                if (context.getComponents().getCamera().getCamera().getFieldOfView() < 100) {
                    context.getComponents().getCamera().getCamera().setFieldOfView(context.getComponents().getCamera().getFOVdefault() + val * 5);
                    if (context.getEffects().getMotionBlurEnabled()) {
                        context.getEffects().EFFECT_MOTION_BLUR.setRadius(context.getEffects().EFFECT_MOTION_BLUR.getRadius() + val / 2);
                    }
                }
            }
        } else if (getPositionYnoHeight() != ground_level) {
            // If we've hit ground (or below), handle this by taking appropriate damage
            if (!isOnGround && !isFlyMode) {
                if (val > 8) {
                    takeDamage((val - 8) * 10);
                }
            }

            // if we are trying to move into a block that relatively small offset from our current position, simply warp the player to that Y position
            // if the offset between the player's current Y and the ground's Y is larger than a block*(the multiplier), then perform a jump
            if ((POSITION_Y - ground_level) > context.getComponents().getEnvironment().getBlockDim() * getMaxAutoJumpHeightMultiplier()) {
                jump();
            } else {
                warpToGround();
            }
        }

        // If we fall more than the lower limit, we start taking damage till the player dies
        if (POSITION_Y > EnvironmentUtil.LIMIT_MIN) {
            takeDamage(1);
        }
    }

    public void moveUp(double val) {
        // handle the drag coefficient
        if (isInWater() && !isFlyMode) val *= EnvironmentUtil.WATER_DRAG_COEFFICIENT;

        this.POSITION_Y -= val;
        // if we're moving up it means we're not on ground anymore
        isOnGround = false;
    }

    public boolean isCrouchToggle() {
        return isCrouchToggle;
    }

    public void setCrouchToggle(boolean crouchToggle) {
        isCrouchToggle = crouchToggle;
    }

    /**
     * Function used by moveForward, moveBackward, moveLeft, moveRight
     *
     * @param new_x
     * @param new_z
     */
    public void handle_collision(double new_x, double new_z) {
        // determine the ground level at each of the X and Z coordinates that we are moving into
        // the given new_x and new_z are NOT yet updated with the player's coordinates. We want to determine whether we can move
        // in that direction before we actually change those coordinates
        double ground_level_x = context.getComponents().getEnvironment().getClosestGroundLevel(new AbsolutePoint3D(new_x, getPositionYwithHeight(), this.POSITION_Z), true);
        double ground_level_z = context.getComponents().getEnvironment().getClosestGroundLevel(new AbsolutePoint3D(this.POSITION_X, getPositionYwithHeight(), new_z), true);

        // if we can move into that direction, do so.
        // these two are separate such that we don't get stuck into a wall when one of the axes fails the check
        if ((POSITION_Y - ground_level_x < context.getComponents().getEnvironment().getBlockDim() * PROPERTY_MULTIPLIER_MAX_BLOCKS_AUTOJUMP * 1.5) || isClipMode) {
            this.POSITION_X = new_x;
        }
        if ((POSITION_Y - ground_level_z < context.getComponents().getEnvironment().getBlockDim() * PROPERTY_MULTIPLIER_MAX_BLOCKS_AUTOJUMP * 1.5) || isClipMode) {
            this.POSITION_Z = new_z;
        }
    }

    /**
     * Handles player moving forward function
     * @param val
     */
    public void moveForward(double val) {
        // handle drag coefficient
        if (isInWater() && !isFlyMode) val *= EnvironmentUtil.WATER_DRAG_COEFFICIENT;
        // if in fly mode, change forward speed to speed_fly
        if (isFlyMode) val = PROPERTY_SPEED_FLY;
        // if crouching, use the crouch height multiplier to decrease the forward speed. The lower the crouch the lower the speed
        if (isCrouching) val *= PROPERTY_MULTIPLIER_CROUCH_HEIGHT;
        // if running, decrease stamina and also move faster by the run multiplier
        if (isRunning) {
            val *= PROPERTY_MULTIPLIER_RUN;
            getStaminaBar().setCurrStatus(getStaminaBar().getCurrStatus() - PROPERTY_STATUS_STAMINA_DEPLETE_SPD);
        }

        double new_x = this.POSITION_X + Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_z = this.POSITION_Z + Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    /**
     * Similar to moveForward function
     * @param val
     */
    public void moveBackward(double val) {
        if (isInWater() && !isFlyMode) val *= EnvironmentUtil.WATER_DRAG_COEFFICIENT;
        if (isFlyMode) val = PROPERTY_SPEED_FLY;
        if (isCrouching) val *= PROPERTY_MULTIPLIER_CROUCH_HEIGHT;

        double new_x = this.POSITION_X - Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_z = this.POSITION_Z - Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    /**
     * Similar to the other move functions
     * @param val
     */
    public void moveLeft(double val) {
        if (isInWater() && !isFlyMode) val *= EnvironmentUtil.WATER_DRAG_COEFFICIENT;
        if (isFlyMode) val = PROPERTY_SPEED_FLY;
        if (isCrouching) val *= PROPERTY_MULTIPLIER_CROUCH_HEIGHT;

        double new_z = this.POSITION_Z + Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_x = this.POSITION_X - Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    /**
     * Similar to the other move functions
     * @param val
     */
    public void moveRight(double val) {
        if (isInWater() && !isFlyMode) val *= EnvironmentUtil.WATER_DRAG_COEFFICIENT;
        if (isFlyMode) val = PROPERTY_SPEED_FLY;
        if (isCrouching) val *= PROPERTY_MULTIPLIER_CROUCH_HEIGHT;

        double new_x = this.POSITION_X + Math.cos(context.getComponents().getCamera().getRotateX() / 57.3) * val;
        double new_z = this.POSITION_Z - Math.sin(context.getComponents().getCamera().getRotateX() / 57.3) * val;

        handle_collision(new_x, new_z);
    }

    /**
     * Jump actions. This triggers a jump in update_handler)
     */
    public void jump() {
        Log.d(TAG, "jump()");
        isJumping = true;
        // set canJump to false because we won't be able to jump again until previous jumping event finished
        canJump = false;
        // when jump is triggered, we want to keep track of what height we started from.
        jump_height_initial = getPositionYnoHeight();
        // also if triggered jump, we don't want to crouch anymore
        isCrouching = false;
    }

    /**
     * Handles running action triggered by ControlsUtil
     * @param shouldRun
     */
    public void setIsRunning(boolean shouldRun) {
        if (!isRunning && shouldRun && getStaminaBar().getCurrStatus() > getStaminaBar().getMaxStatus() / 3) {
            isRunning = true;
        } else if (isRunning && shouldRun && getStaminaBar().getCurrStatus() > 0) {
            isRunning = true;
        } else {
            if (isRunning) {
                takeDamage(getHealthBar().getMaxStatus() / 25);
            }
            isRunning = false;
        }
    }

    /**
     * This function warps the player to ground level
     */
    private void warpToGround() {
        POSITION_Y = context.getComponents().getEnvironment().getClosestGroundLevel(getPlayerPoint3D(), true);
        // therefore set that we are now on ground
        isOnGround = true;
        // reset our fall velocity
        speed_fall_initial = 0;
    }

    public double getPositionX() {
        return POSITION_X;
    }

    public void setPositionX(double pos) {
        POSITION_X = pos;
    }

    public double getPositionYnoHeight() {
        return POSITION_Y;
    }

    public double getPositionYwithHeight() {
        return POSITION_Y - PROPERTY_HEIGHT;
    }

    public void setPositionY(double pos) {
        POSITION_Y = pos;
    }

    public double getPositionZ() {
        return POSITION_Z;
    }

    public void setPositionZ(double pos) {
        POSITION_Z = pos;
    }

    public void setPositionIndependent(double newx, double newy, double newz) {
        POSITION_X = newx;
        POSITION_Y = newy;
        POSITION_Z = newz;
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

    public void setUV_light(boolean state) {
        Log.d(TAG, "setUV_light() -> " + state);

        ITEM_UV_LIGHT.setLightOn(state);
        ITEM_UV_LIGHT_STATE = state;
    }

    public void toggleUVlight() {
        setUV_light(!ITEM_UV_LIGHT_STATE);
    }

    public InventoryUtil getInventoryUtil() {
        return UTIL_INVENTORY;
    }

    public Inventory getInventory() {
        return (Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY);
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
                Log.d(TAG, "setMaxAutoJumpHeightMultiplier() -> " + val);

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
                Log.d(TAG, "setFlySpeed() -> " + spd);

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
                Log.d(TAG, "setRunMultiplier() -> " + mult);

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
                Log.d(TAG, "setJumpHeightMultiplier() -> " + mult);
                PROPERTY_MULTIPLIER_JUMP = mult;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public DeathMenu getDeathMenu() {
        return (DeathMenu) context.getComponents().getHUD().getElement(HUDUtil.DEATH);
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

    /**
     * Clears out the status bars and sets them back to full status
     */
    public void resetStatusBars() {
        Log.d(TAG, "resetStatusBars()");
        getHealthBar().setCurrStatus(getHealthBar().getMaxStatus());
        getStaminaBar().setCurrStatus(getStaminaBar().getMaxStatus());
        getHungerBar().setCurrStatus(getHungerBar().getMaxStatus());
    }

    /**
     * Function used when the player should take damage. Also handles death when the current status reaches 0
     * @param d
     */
    public void takeDamage(double d) {
        Log.d(TAG, "takeDamage() -> Took " + d + " damage");
        getHealthBar().setCurrStatus(getHealthBar().getCurrStatus() - d);

        if (getHealthBar().getCurrStatus() == 0) {
            die();
        }
    }

    /**
     * Handles player death function. Spawns the death menu
     */
    public void die() {
        Log.d(TAG, "die()");
        getDeathMenu().setDead(true);
        context.getComponents().getHUD().getElement(HUDUtil.DEATH).update();
    }

    /**
     * Handles a full player reset to default values.
     */
    public void reset() {
        Log.d(TAG, "reset()");
        setPositionIndependent(0, 0, 0);

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

    /**
     * Uses a random number generator to take the player to a random X Z coordinate
     * Then, the update_handler will update the Y coordinate such that the player is warped to ground
     */
    public void teleportRandom() {
        double randomX = Math.random() * 100000;
        double randomZ = Math.random() * 100000;
        // temporarily place the player at LIMIT_MAX, because we will warp the player to ground on the next update_handler tick
        setPositionIndependent(randomX, EnvironmentUtil.LIMIT_MAX, randomZ);
        didTeleport = true;

        Log.d(TAG, "teleportRandom() -> X: " + randomX + "  Z: " + randomZ);
    }

    /**
     * Function used to set an inventory offset internal to the player group.
     * It also handles updating the holdingGroup such that the item in the player's hand is updated
     * @param i
     */
    public void setInventoryIndexOffset(int i) {
        if (i < 0) {
            getInventoryUtil().moveCurrIndex(i);
        }
        if (i > 0) {
            if (getInventory().isExtendedInventoryDisplayed()) {
                getInventoryUtil().moveCurrIndex(i);
            } else {
                if (getInventoryUtil().getCurrentIndex() != getInventory().getSlotsDisplayed() - 1) {
                    getInventoryUtil().moveCurrIndex(i);
                }
            }
        }

        BaseObject currItem = getInventoryUtil().getCurrentItem();
        Log.d(TAG, "setInventoryIndexOffset() -> " + i + " -> Tag: " + currItem.getProps().getPROPERTY_ITEM_TAG() + "; ItmType: " + currItem.getProps().getPROPERTY_ITEM_TYPE() + "; ObjType: " + currItem.getProps().getPROPERTY_OBJECT_TYPE());

        updateHoldingGroup(false);
    }

    public void setInventoryIndex(int i) {
        getInventoryUtil().setCurrentIndex(i);

        updateHoldingGroup(false);
    }

    public void updateHoldingGroup(boolean shouldHide) {
        context.getComponents().getHUD().getElement(HUDUtil.INVENTORY).update();
        BaseObject inventoryItem = UTIL_INVENTORY.getCurrentItem();

        if (!inventoryItem.getProps().getPROPERTY_ITEM_TAG().equals(ObjectProperties.UNDEFINED_TAG)) {


            if (ANIMATION_SWITCH != null) {
                ANIMATION_SWITCH.stop();
            }
            ANIMATION_SWITCH = new AnimationTimer() {
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

                    BaseObject new_item = ObjectBuilder.clone(inventoryItem);

                    double scalex = new_item.getBoundsInParent().getWidth() / 2;
                    double scaley = new_item.getBoundsInParent().getHeight() / 2;
                    double scalez = new_item.getBoundsInParent().getDepth() / 2;

                    new_item.setScaleIndependent(scalex, scaley, scalez);
                    new_item.setTranslateIndependent(10, 8, 20);

                    if (angle > 29 && !HOLDING_GROUP.getChildren().contains(new_item)) {
                        if (shouldHide) {
                            HOLDING_GROUP.getChildren().clear();
                        } else {
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

            ANIMATION_SWITCH.start();
        } else {
            HOLDING_GROUP.getChildren().clear();
        }
    }

    public void performPlaceAnimation() {

        if (ANIMATION_PLACE != null) {
            ANIMATION_PLACE.stop();
        }
        ANIMATION_PLACE = new AnimationTimer() {
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
        ANIMATION_PLACE.start();

        updateHoldingGroup(false);
    }

    /**
     * Returns a boolean to determine whether the player is in water or not.
     * If the terrain is set to not even have water, implicitly return false
     * @return
     */
    public boolean isInWater() {
        if (!context.getComponents().getEnvironment().getTerrainShouldHaveWater()) {
            return false;
        }
        return (getPositionYwithHeight() + getPlayerHeight() > context.getComponents().getEnvironment().PROPERTY_WATER_LEVEL);
    }

    public void toggleBoostFlySpeed() {
        if (isBoostFlySpeed) {
            setFlySpeed(getFlySpeed() / 2);
            isBoostFlySpeed = false;
        } else {
            setFlySpeed(getFlySpeed() * 2);
            isBoostFlySpeed = true;
        }
    }
}

