package app;

import app.GUI.HUD.HUDElements.LoadingScreen;
import app.GUI.HUD.HUDElements.PauseMenu;
import app.GUI.HUD.HUDElements.PlayerInfo;
import app.GUI.HUD.HUDUtil;
import app.GUI.menu.MenuUtil;
import app.environment.EnvironmentUtil;
import app.player.CameraUtil;
import app.player.ControlsUtil;
import app.player.PlayerUtil;
import app.utils.Log;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.SepiaTone;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;

public class GameBuilder {
    private static final String TAG = "GameBuilder";
    public static long time_current = System.currentTimeMillis();
    private final AnimationTimer GAME_ANIMATION_TIMER;
    private GameFX GAME_EFFECTS;
    private GameComponents GAME_COMPONENTS;
    private GameWindow GAME_WINDOW;

    private static long lastUpdate = 0;
    private static int index = 0;
    private static double[] frameRates = new double[100];

    /**
     * GameBuilder constructor takes in the primaryStage from MainExecution
     * as well as a width and a height corresponding to the size of the window to be drawn
     *
     * @param stg
     * @param w
     * @param h
     */
    public GameBuilder(Stage stg, int w, int h) {
        Log.d(TAG, "CONSTRUCTOR");
        Log.d(TAG, "Creating Game Window with dimensions: " + w + " x " + h);
        stg.setResizable(false);

        // Initialize the 3 Primary game components
        new GameWindow(stg, w, h);
        new GameFX(this);
        new GameComponents(this);

        // Primary Game-Loop, which runs at 60FPS and is meant to handle
        // all operations that need to be calculated every tick
        GAME_ANIMATION_TIMER = new AnimationTimer() {
            double deltaT = 0;

            @Override
            public void handle(long now) {
                time_current = System.currentTimeMillis();
                if (!((PauseMenu) getComponents().getHUD().getElement(HUDUtil.PAUSE)).isPaused()) {

                    if (lastUpdate > 0)
                    {
                        long nanosElapsed = now - lastUpdate;
                        double frameRate = 1000000000.0 / nanosElapsed;
                        index %= frameRates.length;
                        frameRates[index++] = frameRate;
                    }
                    lastUpdate = now;


                    if (getComponents().getEnvironment() != null) {
                        getComponents().getEnvironment().update_handler();
                    }

                    if(getInstantFPS() > 0){
                        if (((LoadingScreen)getComponents().getHUD().getElement(HUDUtil.LOADING_SCREEN)).isShown()){
                            ((LoadingScreen)getComponents().getHUD().getElement(HUDUtil.LOADING_SCREEN)).setShown(false);
                        }
                        ((PlayerInfo) getComponents().UTIL_HUD.getElement(HUDUtil.PLAYER_INFO)).setFps((int)getAverageFPS());
                        deltaT = 60 / getAverageFPS();

                        if (GAME_EFFECTS.PROPERTY_IS_TRIPPY_MODE) {
                            getEffects().EFFECT_COLOR_ADJUST.setHue(Math.sin(time_current / 1000.0));
                            getEffects().EFFECT_COLOR_ADJUST.setContrast((Math.sin(time_current / 15000.0)) / 5);
                            getEffects().EFFECT_BLOOM.setThreshold(Math.sin(time_current / 5000.0) / 2 + 1);
                        }

                        if (getComponents().getGameSceneControls() != null) {
                            getComponents().getGameSceneControls().update_handler(deltaT);
                        }
                        if (getComponents().getPlayer() != null) {
                            getComponents().getPlayer().update_handler(deltaT);
                        }
                    }else{
                        if (!((LoadingScreen)getComponents().getHUD().getElement(HUDUtil.LOADING_SCREEN)).isShown()){
                            ((LoadingScreen)getComponents().getHUD().getElement(HUDUtil.LOADING_SCREEN)).setShown(true);
                        }
                    }
                }
            }
        };
    }

    public static double getInstantFPS()
    {
        return frameRates[index % frameRates.length];
    }

    /**
     * Returns the average FPS for the last 100 frames rendered.
     * @return
     */
    public static double getAverageFPS()
    {
        double total = 0.0d;

        for (int i = 0; i < frameRates.length; i++)
        {
            total += frameRates[i];
        }

        return total / frameRates.length;
    }

    /**
     * Getter for GameComponents
     * Used to be able to reference all connected classes from one main Context (GameBuilder)
     *
     * @return
     */
    public GameComponents getComponents() {
        return GAME_COMPONENTS;
    }

    /**
     * Getter for GameFX
     * Used to be able to reference all effects applied to the game scene from the main context
     *
     * @return
     */
    public GameFX getEffects() {
        return GAME_EFFECTS;
    }

    /**
     * Getter for GameWindow
     * Used to be able to reference all window-related operations from the main context
     */
    public GameWindow getWindow() {
        return GAME_WINDOW;
    }

    /**
     * One of the 3 primary subclasses of GameBuilder
     * GameWindow handles everything related to the window.
     */
    public class GameWindow {
        private final int WINDOW_WIDTH;
        private final int WINDOW_HEIGHT;

        // The root scene holds other SubScenes, such as GAME_SCENE which is used to
        // render the 3D world with a PerspectiveCamera, as well as the 2D HUD as an overlay
        // Game Controls are set on the root scene since we want to use them all throughout.
        private final Scene ROOT_SCENE;
        private final Group ROOT_GROUP;

        // This subscene is a child of SCENE_ROOT, and is responsible for holding all
        // in-game elements, such as the environment, skybox, etc.
        private final SubScene GAME_SCENE;
        private final Group GAME_GROUP;
        // Each scene contains it's own Group, which is used to hold the scene's children in a container

        // The stage is responsible for rendering our scenes to the window
        public Stage STAGE;
        private Scene SCENE_CURRENT; // Keep track of which scene is currently displayed to the user

        /**
         * GameWindow constructor saves an instance of itself to GameBuilder
         * in order to be able to reference these subclasses from anywhere within the game.
         * It also initializes our Root Scene and Game Scene
         * (as well as the element groups partaining to each of them)
         *
         * @param stg
         * @param w
         * @param h
         */
        GameWindow(Stage stg, int w, int h) {
            GAME_WINDOW = this;

            STAGE = stg;

            WINDOW_WIDTH = w;
            WINDOW_HEIGHT = h;

            // First, set up the game group and subscene based on our given width and height
            // This game group hold everything pertaining to the actual in-game
            GAME_GROUP = new Group();
            GAME_SCENE = new SubScene(GAME_GROUP, WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);

            // Second, set up the root group and scene based on our given width and height
            // This root group will hold the game scene itself.
            ROOT_GROUP = new Group();
            ROOT_SCENE = new Scene(ROOT_GROUP, WINDOW_WIDTH, WINDOW_HEIGHT);
            ROOT_GROUP.getChildren().add(GAME_SCENE);
        }


        /**
         * Function used to be able to switch between scenes,
         * such as going from the Main Menu to the game, and vice-versa
         *
         * @param NEXT_SCENE
         */
        public void showScene(Scene NEXT_SCENE) {
            SCENE_CURRENT = NEXT_SCENE;
            GAME_ANIMATION_TIMER.stop();

            if (SCENE_CURRENT == ROOT_SCENE) {
                Log.d(TAG, "Switched to Game Scene");
                getComponents().getEnvironment().getSkybox().resetLighting();
                GAME_ANIMATION_TIMER.start();
            }
            if (SCENE_CURRENT == getComponents().getMenu().getScene()) {
                Log.d(TAG, "Switched to Menu Scene");
                showCursor(Cursor.DEFAULT);
            }

            // Finally, set the scene to the stage and show it
            STAGE.setScene(SCENE_CURRENT);
            STAGE.setTitle("InfiniBuilder");
            STAGE.show();
        }

        public Stage getStage() {
            return STAGE;
        }

        public Scene getCurrentScene() {
            return SCENE_CURRENT;
        }

        public Scene getRootScene() {
            return ROOT_SCENE;
        }

        public Group getRootSceneGroup() {
            return ROOT_GROUP;
        }

        public SubScene getGameSubscene() {
            return GAME_SCENE;
        }

        public Group getGameSubsceneGroup() {
            return GAME_GROUP;
        }

        public int getWindowWidth() {
            return WINDOW_WIDTH;
        }

        public int getWindowHeight() {
            return WINDOW_HEIGHT;
        }

        public void closeWindow() {
            STAGE.close();
        }

        public void lockCursor(boolean state) {
            if (state) {
                centerCursor();
                hideCursor();
            }
        }

        /**
         * centerCursor function moves the user's mouse pointer to the center of the screen based
         * on the window's width and height
         */
        public void centerCursor() {
            moveCursor((int) STAGE.getX() + WINDOW_WIDTH / 2.0, (int) STAGE.getY() + WINDOW_HEIGHT / 2.0);
        }

        public void showCursor(Cursor c) {
            getCurrentScene().setCursor(c);
        }

        public void hideCursor() {
            getCurrentScene().setCursor(Cursor.NONE);
        }

        /**
         * This function handles moving the host's mouse pointer to any arbitrary X and Y coordinate
         *
         * @param screenX
         * @param screenY
         */
        public void moveCursor(double screenX, double screenY) {
            Platform.runLater(() -> {
                Robot robot = new Robot();
                robot.mouseMove(screenX, screenY);
            });
        }
    }

    /**
     * One of the 3 primary subclasses of GameBuilder
     * Handles operations and access of any came component from within the GameBuilder context.
     */
    public class GameComponents {

        // The HUDUtil class contains the other SubScene which is placed in the SCENE_ROOT together with SCENE_GAME
        private HUDUtil UTIL_HUD;
        // Other scenes are defined within the MenuUtil class in order to draw the main menu
        private MenuUtil UTIL_MENU;

        // Other helper utils
        private PlayerUtil UTIL_PLAYER;
        private CameraUtil UTIL_CAMERA;
        private EnvironmentUtil UTIL_ENVIRONMENT;
        private ControlsUtil UTIL_CONTROLS;

        /**
         * GameComponents constructor saves it's instance to GameBuilder,
         * such that it's components can be referenced anywhere based on the saved GameBuilder context
         *
         * @param ctx
         */
        GameComponents(GameBuilder ctx) {
            GAME_COMPONENTS = this;
            // The constructor creates instances of each component and then saves them
            // to their corresponding variables
            setCamera(new CameraUtil(ctx));
            setGameSceneControls(new ControlsUtil(ctx));
            setEnvironment(new EnvironmentUtil(ctx));
            setPlayer(new PlayerUtil(ctx));
            setHUD(new HUDUtil(ctx));
            setMenu(new MenuUtil(ctx));
            UTIL_HUD.drawHUD();
        }

        public CameraUtil getCamera() {
            return UTIL_CAMERA;
        }

        public void setCamera(CameraUtil cam) {
            UTIL_CAMERA = cam;
            getWindow().getGameSubscene().setCamera(cam.getCamera());
        }

        public ControlsUtil getGameSceneControls() {
            return UTIL_CONTROLS;
        }

        public void setGameSceneControls(ControlsUtil ctrls) {
            UTIL_CONTROLS = ctrls;
            UTIL_CONTROLS.apply(getWindow().getRootScene());
        }

        public PlayerUtil getPlayer() {
            return UTIL_PLAYER;
        }

        public void setPlayer(PlayerUtil player) {
            UTIL_PLAYER = player;
            getWindow().getGameSubsceneGroup().getChildren().add(UTIL_PLAYER.getGroup());
        }

        public EnvironmentUtil getEnvironment() {
            return UTIL_ENVIRONMENT;
        }

        public void setEnvironment(EnvironmentUtil env) {
            UTIL_ENVIRONMENT = env;
            getWindow().getGameSubsceneGroup().getChildren().add(UTIL_ENVIRONMENT.getWorldGroup());
        }

        public MenuUtil getMenu() {
            return UTIL_MENU;
        }

        public void setMenu(MenuUtil ut) {
            UTIL_MENU = ut;
        }

        public HUDUtil getHUD() {
            return UTIL_HUD;
        }

        public void setHUD(HUDUtil hud) {
            UTIL_HUD = hud;
            getWindow().getRootSceneGroup().getChildren().add(hud.getSubScene());
            UTIL_HUD.drawHUD();
        }
    }

    /**
     * One of the 3 primary subclasses of GameBuilder
     * GameFX handles all game effects, such as motion blur, bloom, color adjust, sepia tone, etc.
     */
    public class GameFX {
        public MotionBlur EFFECT_MOTION_BLUR;
        GameBuilder context;
        boolean PROPERTY_IS_TRIPPY_MODE;
        private boolean EFFECT_MOTION_BLUR_ENABLED;
        private Bloom EFFECT_BLOOM;
        private ColorAdjust EFFECT_COLOR_ADJUST;
        private SepiaTone EFFECT_SEPIA_TONE;

        /**
         * GameFX Constructor saves an instance of itself to GameBuilder such that
         * it can be referenced from anywhere within the game.
         *
         * @param ctx
         */
        GameFX(GameBuilder ctx) {
            context = ctx;
            GAME_EFFECTS = this;
            // first-time set up of the effects
            resetEffects();
            // set up default setting - motion blur enabled.
            setMotionBlurEnabled(true);
        }

        /**
         * resetEffects function sets up the motion blur, bloom, color adjust, and sepia tone effects
         * Each one of these must be an input of the other in order for the effects to be blended.
         */
        public void resetEffects() {
            Log.d(TAG, "resetEffects()");

            EFFECT_MOTION_BLUR = new MotionBlur();
            EFFECT_BLOOM = new Bloom();
            EFFECT_BLOOM.setInput(EFFECT_MOTION_BLUR);
            EFFECT_COLOR_ADJUST = new ColorAdjust();
            EFFECT_COLOR_ADJUST.setInput(EFFECT_BLOOM);
            EFFECT_SEPIA_TONE = new SepiaTone();
            EFFECT_SEPIA_TONE.setInput(EFFECT_COLOR_ADJUST);

            // set up default setting - sepia tone to 0
            setSepiaTone(0);
            setBloom(1);
            setMotionBlur(0);

            // finally, we only want these effects to be shown in-game
            // so set them on the Game Subscene
            getWindow().getGameSubscene().setEffect(EFFECT_SEPIA_TONE);
        }

        public boolean getTripMode() {
            return PROPERTY_IS_TRIPPY_MODE;
        }

        public void setTripMode(boolean val) {
            PROPERTY_IS_TRIPPY_MODE = val;
            resetEffects();
        }

        public double getContrast() {
            return EFFECT_COLOR_ADJUST.getContrast();
        }

        public void setContrast(double val) {
            try {
                if (val >= -1 && val <= 1) {
                    EFFECT_COLOR_ADJUST.setContrast(val);
                } else {
                    throw new IndexOutOfBoundsException();
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        public double getBrightness() {
            return EFFECT_COLOR_ADJUST.getBrightness();
        }

        public void setBrightness(double val) {
            try {
                if (val >= -1 && val <= 1) {
                    EFFECT_COLOR_ADJUST.setBrightness(val);
                } else {
                    throw new IndexOutOfBoundsException();
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        public double getSaturation() {
            return EFFECT_COLOR_ADJUST.getSaturation();
        }

        public void setSaturation(double val) {
            try {
                if (val >= -1 && val <= 1) {
                    EFFECT_COLOR_ADJUST.setSaturation(val);
                } else {
                    throw new IndexOutOfBoundsException();
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        public double getHue() {
            return EFFECT_COLOR_ADJUST.getHue();
        }

        public void setHue(double val) {
            try {
                if (val >= -1 && val <= 1) {
                    EFFECT_COLOR_ADJUST.setHue(val);
                } else {
                    throw new IndexOutOfBoundsException();
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        public double getBloom() {
            return EFFECT_BLOOM.getThreshold();
        }

        public void setBloom(double val) {
            try {
                if (val >= 0 && val <= 1) {
                    EFFECT_BLOOM.setThreshold(val);
                } else {
                    throw new IndexOutOfBoundsException();
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        public double getSepiaTone() {
            return EFFECT_SEPIA_TONE.getLevel();
        }

        public void setSepiaTone(double val) {
            try {
                if (val >= 0 && val <= 1) {
                    EFFECT_SEPIA_TONE.setLevel(val);
                } else {
                    throw new IndexOutOfBoundsException();
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }


        public boolean getMotionBlurEnabled() {
            return EFFECT_MOTION_BLUR_ENABLED;
        }

        /**
         * MotionBlurEnabled determines whether motion blur should appear at all within the game
         * This is a global boolean that disables or enables the motion blur completely
         *
         * @return
         */
        public void setMotionBlurEnabled(boolean val) {
            EFFECT_MOTION_BLUR_ENABLED = val;
        }

        public double getMotionBlur() {
            return EFFECT_SEPIA_TONE.getLevel();
        }

        /**
         * This setting determines the level intensity of the motion blur applied.
         * If motion blur is globally disabled, it does nothing.
         *
         * @param val
         */
        public void setMotionBlur(double val) {
            try {
                if (val >= 0) {
                    EFFECT_MOTION_BLUR.setRadius(val);
                } else {
                    throw new IndexOutOfBoundsException();
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }
}
