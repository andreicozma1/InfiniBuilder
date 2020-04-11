package app;

import app.GUI.HUD.HUDElements.PauseMenu;
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

    // MAIN GAME LOOP
    private final AnimationTimer GAME_ANIMATION_TIMER;
    public long time_current;
    private long TOTAL_RUNTIME = 0;
    private GameFX GAME_EFFECTS;
    private GameComponents GAME_COMPONENTS;
    private GameWindow GAME_WINDOW;

    public GameBuilder(Stage stg, int w, int h) {
        Log.p(TAG, "CONSTRUCTOR");
        Log.p(TAG, "Creating Game Window with dimensions: " + w + " x " + h);

        new GameWindow(stg, w, h);
        new GameFX(this);
        new GameComponents(this);

        GAME_ANIMATION_TIMER = new AnimationTimer() {
            long reading_last = 0;
            int reading_frames = 0;
            double deltaT = 0;

            @Override
            public void handle(long now) {
                if (!((PauseMenu) getComponents().getHUD().getElement(HUDUtil.PAUSE)).isPaused()) {

                    reading_frames++;
                    time_current = System.currentTimeMillis();

                    if (time_current - reading_last > 1000.0) {
                        Log.p(TAG, "HEARTBEAT -> " + TOTAL_RUNTIME + "(" + time_current + ") -> FPS: " + reading_frames + " -> DeltaT: " + deltaT);
                        deltaT = 60.0 / reading_frames;
                        if (deltaT > 5) {
                            deltaT = 1;
                        }
                        reading_last = time_current;
                        reading_frames = 0;
                        TOTAL_RUNTIME++;
                    }

                    if (GAME_EFFECTS.PROPERTY_IS_TRIPPY_MODE) {
                        getEffects().EFFECT_COLOR_ADJUST.setHue(Math.sin(time_current / 1000.0));
                        getEffects().EFFECT_COLOR_ADJUST.setContrast((Math.sin(time_current / 15000.0)) / 5);
                        getEffects().EFFECT_BLOOM.setThreshold(Math.sin(time_current / 5000.0) / 2 + 1);
                    }

                    if (getComponents().getGameSceneControls() != null) {
                        getComponents().getGameSceneControls().update_handler(deltaT);
                    }
                    if (getComponents().getEnvironment() != null) {
                        getComponents().getEnvironment().update_handler();
                    }
                    if (getComponents().getPlayer() != null) {
                        getComponents().getPlayer().update_handler(deltaT);
                    }
                }
            }
        };
    }


    public GameComponents getComponents() {
        return GAME_COMPONENTS;
    }

    public GameFX getEffects() {
        return GAME_EFFECTS;
    }

    public GameWindow getWindow() {
        return GAME_WINDOW;
    }

    public class GameWindow {
        private final int WINDOW_WIDTH;
        private final int WINDOW_HEIGHT;
        // This scene holds other SubScenes, such as SCENE_GAME which is used to render the 3D world with a PerspectiveCamera, as well as the 2D HUD as an overlay
        // Controls are set on the root scene.
        private final Scene ROOT_SCENE;
        private final Group ROOT_GROUP;
        // This subscene is a child of SCENE_ROOT.
        private final SubScene GAME_SCENE;
        private final Group GAME_GROUP;
        public Stage STAGE;
        private Scene SCENE_CURRENT; // hold the current scene displayed to the user
        // Each scene contains it's own Group, which is used to hold the scene's children in a container

        GameWindow(Stage stg, int w, int h) {
            GAME_WINDOW = this;

            STAGE = stg;

            WINDOW_WIDTH = w;
            WINDOW_HEIGHT = h;

            GAME_GROUP = new Group();
            GAME_SCENE = new SubScene(GAME_GROUP, WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);

            ROOT_GROUP = new Group();
            ROOT_SCENE = new Scene(ROOT_GROUP, WINDOW_WIDTH, WINDOW_HEIGHT);
            ROOT_GROUP.getChildren().add(GAME_SCENE);
        }


        public void showScene(Scene NEXT_SCENE) {
            SCENE_CURRENT = NEXT_SCENE;
            GAME_ANIMATION_TIMER.stop();

            if (SCENE_CURRENT == ROOT_SCENE) {
                System.out.println("Switched to Game Scene");
                getComponents().getEnvironment().getSkybox().resetLighting();

                GAME_ANIMATION_TIMER.start();
            }
            if (SCENE_CURRENT == getComponents().getMenu().getScene()) {
                System.out.println("Switched to Menu Scene");
                showCursor(Cursor.DEFAULT);
            }

            STAGE.setScene(SCENE_CURRENT);
            STAGE.setTitle("307FinalProject");
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

        public void centerCursor() {
            moveCursor((int) STAGE.getX() + WINDOW_WIDTH / 2, (int) STAGE.getY() + WINDOW_HEIGHT / 2);
        }

        public void showCursor(Cursor c) {
            getCurrentScene().setCursor(c);
        }

        public void hideCursor() {
            getCurrentScene().setCursor(Cursor.NONE);
        }

        public void moveCursor(double screenX, double screenY) {
            Platform.runLater(() -> {
                Robot robot = new Robot();
                robot.mouseMove(screenX, screenY);
            });
        }
    }

    public class GameComponents {

        private HUDUtil UTIL_HUD; // The HUDUtil class contains the other SubScene which is placed in the SCENE_ROOT together with SCENE_GAME
        // Other scenes are defined within the MenuUtil class in order to draw the main menu
        private MenuUtil UTIL_MENU;

        // Other helper utils
        private PlayerUtil UTIL_PLAYER;
        private CameraUtil UTIL_CAMERA;
        private EnvironmentUtil UTIL_ENVIRONMENT;
        private ControlsUtil UTIL_CONTROLS;

        GameComponents(GameBuilder ctx) {
            GAME_COMPONENTS = this;
            setCamera(new CameraUtil(ctx));
            setGameSceneControls(new ControlsUtil(ctx));
            setPlayer(new PlayerUtil(ctx));
            setEnvironment(new EnvironmentUtil(ctx));
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

    public class GameFX {
        public MotionBlur EFFECT_MOTION_BLUR;
        GameBuilder context;
        boolean PROPERTY_IS_TRIPPY_MODE;
        private boolean EFFECT_MOTION_BLUR_ENABLED;
        private Bloom EFFECT_BLOOM;
        private ColorAdjust EFFECT_COLOR_ADJUST;
        private SepiaTone EFFECT_SEPIA_TONE;

        GameFX(GameBuilder ctx) {
            context = ctx;
            GAME_EFFECTS = this;
            resetEffects();
        }

        public void resetEffects() {
            EFFECT_MOTION_BLUR = new MotionBlur();
            EFFECT_BLOOM = new Bloom();
            EFFECT_BLOOM.setInput(EFFECT_MOTION_BLUR);
            EFFECT_COLOR_ADJUST = new ColorAdjust();
            EFFECT_COLOR_ADJUST.setInput(EFFECT_BLOOM);
            EFFECT_SEPIA_TONE = new SepiaTone();
            EFFECT_SEPIA_TONE.setInput(EFFECT_COLOR_ADJUST);

            setSepiaTone(0);
            setBloom(.88);
            setMotionBlur(0);
            setMotionBlurEnabled(true);

            getWindow().getGameSubscene().setEffect(EFFECT_SEPIA_TONE);
        }

        public boolean getTripMode() {
            return PROPERTY_IS_TRIPPY_MODE;
        }

        public void setTripMode(boolean val) {
            PROPERTY_IS_TRIPPY_MODE = val;
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

        public void setMotionBlurEnabled(boolean val) {
            EFFECT_MOTION_BLUR_ENABLED = val;
        }

        public double getMotionBlur() {
            return EFFECT_SEPIA_TONE.getLevel();
        }

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
