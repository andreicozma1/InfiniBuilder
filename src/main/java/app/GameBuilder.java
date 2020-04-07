package app;

import app.GUI.HUD.HUDUtil;
import app.GUI.HUD.HUDElements.*;
import app.environment.EnvironmentUtil;
import app.GUI.menu.MenuUtil;
import app.utils.Log;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.effect.*;
import javafx.scene.robot.Robot;
import javafx.stage.Stage;
import app.player.CameraUtil;
import app.player.ControlsUtil;
import app.player.PlayerUtil;

public class GameBuilder {
    private static final String TAG = "CameraUtil";


    // MAIN GAME LOOP
    private final AnimationTimer GAME_ANIMATION_TIMER;
    private long TOTAL_RUNTIME = 0;

    private GameFX GAME_EFFECTS;
    private GameComponents GAME_COMPONENTS;
    private GameWindow GAME_WINDOW;

    public GameBuilder(Stage stg, int w, int h) {
        Log.p(TAG,"CONSTRUCTOR");
        Log.p(TAG,"Creating Game Window with dimensions: " + w + " x " + h);


        new GameWindow(stg,w,h);
        new GameFX(this);
        new GameComponents(this);

        GAME_ANIMATION_TIMER = new AnimationTimer() {
            long last = 0;
            int frames = 0;
            double dt = 0;
            @Override
            public void handle(long now) {
                if (!((PauseMenu) getComponents().getHUD().getElement(HUDUtil.PAUSE)).isPaused()) {
                    if (getComponents().getGameSceneControls() != null) {
                        getComponents().getGameSceneControls().update_handler(dt);
                    }
                    if (getComponents().getEnvironment() != null) {
                        getComponents().getEnvironment().update_handler();
                    }
                    if (getComponents().getPlayer() != null) {
                        getComponents().getPlayer().update_handler(dt);
                    }
                    frames++;
                    long curr = System.currentTimeMillis();

                    if(GAME_EFFECTS.trippy){
                        getEffects().EFFECT_COLOR_ADJUST.setHue(Math.sin(curr/1000.0));
                        getEffects().EFFECT_COLOR_ADJUST.setContrast((Math.sin(curr/15000.0))/5);
                        getEffects().EFFECT_BLOOM.setThreshold(Math.sin(curr/5000.0)/2+1);
                    }

                    if (curr - last > 1000.0) {
                    Log.p(TAG,"HEARTBEAT -> " + TOTAL_RUNTIME + "(" + curr + ") -> FPS: " + frames + " -> DeltaT: " + dt);
                        dt = 60.0 / frames;
                        if (dt > 5) {
                            dt = 1;
                        }
                        frames = 0;
                        TOTAL_RUNTIME++;
                        last = curr;
                    }
                }
            }
        };
    }


    public GameComponents getComponents(){
        return GAME_COMPONENTS;
    }

    public GameFX getEffects(){
        return GAME_EFFECTS;
    }

    public GameWindow getWindow() {return GAME_WINDOW;}

    public class GameWindow{
        public Stage STAGE;
        private final int WINDOW_WIDTH;
        private final int WINDOW_HEIGHT;

        private Scene SCENE_CURRENT; // hold the current scene displayed to the user

        // This scene holds other SubScenes, such as SCENE_GAME which is used to render the 3D world with a PerspectiveCamera, as well as the 2D HUD as an overlay
        // Controls are set on the root scene.
        private final Scene ROOT_SCENE;
        private final Group ROOT_GROUP;
        // This subscene is a child of SCENE_ROOT.
        private final SubScene GAME_SCENE;
        private final Group GAME_GROUP;
        // Each scene contains it's own Group, which is used to hold the scene's children in a container

        GameWindow(Stage stg, int w, int h){
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
                hideCursor();

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

        public Stage getSTAGE() {
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

        public void moveCursor(double screenX, double screenY) {
            Platform.runLater(() -> {
                Robot robot = new Robot();
                robot.mouseMove(screenX, screenY);
            });
        }

        public void centerCursor() {
            moveCursor((int) STAGE.getX() + WINDOW_WIDTH / 2, (int) STAGE.getY() + WINDOW_HEIGHT / 2);
        }

        public void hideCursor() {
//        getCurrentScene().setCursor(Cursor.NONE);
        }


        public void showCursor(Cursor c) {
            getCurrentScene().setCursor(c);
        }

        public void lockCursor(boolean state) {
            if (state) {
                centerCursor();
                hideCursor();
            }
        }
    }

    public class GameComponents{

        private HUDUtil hud_util = null; // The HUDUtil class contains the other SubScene which is placed in the SCENE_ROOT together with SCENE_GAME
        // Other scenes are defined within the MenuUtil class in order to draw the main menu
        private MenuUtil menu_util = null;

        // Other helper utils
        private PlayerUtil player_util = null;
        private CameraUtil cam_util = null;
        private EnvironmentUtil env_util = null;
        private ControlsUtil ctrls_util = null;

        GameComponents(GameBuilder ctx){
            GAME_COMPONENTS = this;
            setCamera(new CameraUtil(ctx));
            setGameSceneControls(new ControlsUtil(ctx));
            setPlayer(new PlayerUtil(ctx));
            setEnvironment(new EnvironmentUtil(ctx));
            setMenu(new MenuUtil(ctx));
            setHUD(new HUDUtil(ctx));
        }

        public void setCamera(CameraUtil cam) {
            cam_util = cam;
            getWindow().getGameSubscene().setCamera(cam.getCamera());
        }

        public CameraUtil getCamera() {
            return cam_util;
        }

        public void setGameSceneControls(ControlsUtil ctrls) {
            ctrls_util = ctrls;
            ctrls_util.apply(getWindow().getRootScene());
        }

        public ControlsUtil getGameSceneControls() {
            return ctrls_util;
        }

        public void setPlayer(PlayerUtil player) {
            player_util = player;
            getWindow().getGameSubsceneGroup().getChildren().add(player_util.getGroup());
        }

        public PlayerUtil getPlayer() {
            return player_util;
        }

        public void setEnvironment(EnvironmentUtil env) {
            env_util = env;
            getWindow().getGameSubsceneGroup().getChildren().add(env_util.getWorldGroup());
        }

        public EnvironmentUtil getEnvironment() {
            return env_util;
        }

        public void setMenu(MenuUtil ut) {
            menu_util = ut;
        }

        public MenuUtil getMenu() {
            return menu_util;
        }

        public void setHUD(HUDUtil hud) {
            hud_util = hud;
            getWindow().getRootSceneGroup().getChildren().add(hud.getSubScene());
        }

        public HUDUtil getHUD() {
            return hud_util;
        }
    }

    public class GameFX {
        GameBuilder context;

        public MotionBlur EFFECT_MOTION_BLUR;
        private boolean EFFECT_MOTION_BLUR_ENABLED;
        private Bloom EFFECT_BLOOM;
        private ColorAdjust EFFECT_COLOR_ADJUST;
        private SepiaTone EFFECT_SEPIA_TONE;
        boolean trippy;

        GameFX(GameBuilder ctx){
            context = ctx;
            GAME_EFFECTS = this;
            resetEffects();
        }

        public void resetEffects(){
            EFFECT_MOTION_BLUR = new MotionBlur();
            EFFECT_BLOOM = new Bloom();
            EFFECT_BLOOM.setInput(EFFECT_MOTION_BLUR);
            EFFECT_COLOR_ADJUST = new ColorAdjust();
            EFFECT_COLOR_ADJUST.setInput(EFFECT_BLOOM);
            EFFECT_SEPIA_TONE = new SepiaTone();
            EFFECT_SEPIA_TONE.setInput(EFFECT_COLOR_ADJUST);

            setSepiaTone(0);
            setBloom(.85);
            setMotionBlur(0);
            setMotionBlurEnabled(true);

            getWindow().getGameSubscene().setEffect(EFFECT_SEPIA_TONE);
        }

        public void setTripMode(boolean val){
            trippy = val;
        }
        public boolean getTripMode(){
            return trippy;
        }

        public double getContrast(){
            return EFFECT_COLOR_ADJUST.getContrast();
        }
        public void setContrast(double val){
            try{
                if(val >= -1 && val <= 1){
                    EFFECT_COLOR_ADJUST.setContrast(val);
                } else{
                    throw new IndexOutOfBoundsException();
                }
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }

        public double getBrightness(){
            return EFFECT_COLOR_ADJUST.getBrightness();
        }

        public void setBrightness(double val){
            try{
                if(val >= -1 && val <= 1){
                    EFFECT_COLOR_ADJUST.setBrightness(val);
                } else{
                    throw new IndexOutOfBoundsException();
                }
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }

        public double getSaturation(){
            return EFFECT_COLOR_ADJUST.getSaturation();
        }

        public void setSaturation(double val){
            try{
                if(val >= -1 && val <= 1){
                    EFFECT_COLOR_ADJUST.setSaturation(val);
                } else{
                    throw new IndexOutOfBoundsException();
                }
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }

        public double getHue(){
            return EFFECT_COLOR_ADJUST.getHue();
        }

        public void setHue(double val){
            try{
                if(val >= -1 && val <= 1){
                    EFFECT_COLOR_ADJUST.setHue(val);
                } else{
                    throw new IndexOutOfBoundsException();
                }
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }

        public double getBloom(){
            return EFFECT_BLOOM.getThreshold();
        }

        public void setBloom(double val){
            try{
                if(val >= 0 && val <= 1){
                    EFFECT_BLOOM.setThreshold(val);
                } else{
                    throw new IndexOutOfBoundsException();
                }
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }

        public double getSepiaTone(){
            return EFFECT_SEPIA_TONE.getLevel();
        }
        public void setSepiaTone(double val){
            try{
                if(val >= 0 && val <= 1){
                    EFFECT_SEPIA_TONE.setLevel(val);
                } else{
                    throw new IndexOutOfBoundsException();
                }
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }

        public boolean getMotionBlurEnabled(){
            return EFFECT_MOTION_BLUR_ENABLED;
        }

        public void setMotionBlurEnabled(boolean val){
            EFFECT_MOTION_BLUR_ENABLED = val;
        }

        public double getMotionBlur(){
            return EFFECT_SEPIA_TONE.getLevel();
        }
        public void setMotionBlur(double val){
            try{
                if(val >= 0){
                    EFFECT_MOTION_BLUR.setRadius(val);
                } else{
                    throw new IndexOutOfBoundsException();
                }
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    }

}
