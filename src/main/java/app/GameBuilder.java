package app;

import app.GUI.HUD.HUDUtil;
import app.GUI.HUD.HUDElements.*;
import app.environment.EnvironmentUtil;
import app.GUI.menu.MenuUtil;
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
    private static int WINDOW_WIDTH;
    private static int WINDOW_HEIGHT;

    public Stage STAGE;

    private Scene SCENE_CURRENT; // hold the current scene displayed to the user

    // This scene holds other SubScenes, such as SCENE_GAME which is used to render the 3D world with a PerspectiveCamera, as well as the 2D HUD as an overlay
    // Controls are set on the root scene.
    private Scene ROOT_SCENE;
    private Group ROOT_GROUP;
    // This subscene is a child of SCENE_ROOT.
    private SubScene GAME_SCENE;
    private Group GAME_GROUP;
    // Each scene contains it's own Group, which is used to hold the scene's children in a container

    private HUDUtil hud_util; // The HUDUtil class contains the other SubScene which is placed in the SCENE_ROOT together with SCENE_GAME
    // Other scenes are defined within the MenuUtil class in order to draw the main menu
    private MenuUtil menu_util;

    // Other helper utils
    private PlayerUtil player_util = null;
    private CameraUtil cam_util = null;
    private EnvironmentUtil env_util = null;
    private ControlsUtil ctrls_util = null;

    // MAIN GAME LOOP
    private AnimationTimer timer;
    private long runtime = 0;

    public MotionBlur EFFECT_MOTION_BLUR;
    private boolean EFFECT_MOTION_BLUR_ENABLED;
    private Bloom EFFECT_BLOOM;
    private ColorAdjust EFFECT_COLOR_ADJUST;
    private SepiaTone EFFECT_SEPIA_TONE;

    boolean trippy;

    public GameBuilder(Stage stg, int w, int h) {
        System.out.println("Creating game window with dimensions: " + w + " x " + h);
        STAGE = stg;
        WINDOW_WIDTH = w;
        WINDOW_HEIGHT = h;

        resetEffects();

        GAME_GROUP = new Group();
        GAME_SCENE = new SubScene(GAME_GROUP, WINDOW_WIDTH, WINDOW_HEIGHT, true, SceneAntialiasing.BALANCED);
        GAME_SCENE.setEffect(EFFECT_SEPIA_TONE);

        ROOT_GROUP = new Group();
        ROOT_SCENE = new Scene(ROOT_GROUP, WINDOW_WIDTH, WINDOW_HEIGHT);
        ROOT_GROUP.getChildren().add(GAME_SCENE);

        setCamera(new CameraUtil(this));
        setGameSceneControls(new ControlsUtil(this));

        timer = new AnimationTimer() {
            long last = 0;
            int frames = 0;
            double dt = 0;
            @Override
            public void handle(long now) {
                if (!((PauseMenu) hud_util.getElement(HUDUtil.PAUSE)).isPaused()) {
                    if (ctrls_util != null) {
                        ctrls_util.update_handler(dt);
                    }
                    if (env_util != null) {
                        env_util.update_handler();
                    }
                    if (player_util != null) {
                        player_util.update_handler(dt);
                    }
                    frames++;
                    long curr = System.currentTimeMillis();

                    if(trippy){
                        EFFECT_COLOR_ADJUST.setHue(Math.sin(curr/1000.0));
                        EFFECT_COLOR_ADJUST.setContrast((Math.sin(curr/15000.0))/5);
                        EFFECT_BLOOM.setThreshold(Math.sin(curr/5000.0)/2+1);
                    }

                    if (curr - last > 1000.0) {
//                    System.out.println("HEARTBEAT -> " + runtime + "(" + curr + ") -> FPS: " + frames + " -> DeltaT: " + dt);
                        dt = 60.0 / frames;
                        if (dt > 5) {
                            dt = 1;
                        }
                        frames = 0;
                        runtime++;
                        last = curr;
                    }
                }
            }
        };
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
    }


    public void setCamera(CameraUtil cam) {
        cam_util = cam;
        GAME_SCENE.setCamera(cam.getCamera());
    }

    public CameraUtil getCamera() {
        return cam_util;
    }

    public void setGameSceneControls(ControlsUtil ctrls) {
        ctrls_util = ctrls;
        ctrls_util.apply(ROOT_SCENE);
    }

    public ControlsUtil getGameSceneControls() {
        return ctrls_util;
    }

    public void setPlayer(PlayerUtil player) {
        player_util = player;
        GAME_GROUP.getChildren().add(player_util.getGroup());
    }

    public PlayerUtil getPlayer() {
        return player_util;
    }

    public void setEnvironment(EnvironmentUtil env) {
        env_util = env;
        GAME_GROUP.getChildren().add(env_util.getWorldGroup());
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

    public Stage getSTAGE() {
        return STAGE;
    }

    public void setHUD(HUDUtil hud) {
        hud_util = hud;
        ROOT_GROUP.getChildren().add(hud.getSubScene());
    }

    public HUDUtil getHUD() {
        return hud_util;
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



    public void showScene(Scene NEXT_SCENE) {
        SCENE_CURRENT = NEXT_SCENE;
        timer.stop();

        if (SCENE_CURRENT == ROOT_SCENE) {
            System.out.println("Switched to Game Scene");
            getEnvironment().getSkybox().resetLighting();
            hideCursor();

            timer.start();
        }
        if (SCENE_CURRENT == menu_util.getScene()) {
            System.out.println("Switched to Menu Scene");
            showCursor(Cursor.DEFAULT);
        }

        STAGE.setScene(SCENE_CURRENT);
        STAGE.setTitle("307FinalProject");
        STAGE.show();
    }

    public Scene getGameRootScene() {
        return ROOT_SCENE;
    }

    public SubScene getGameSubscene() {
        return GAME_SCENE;
    }

    public static int getWindowWidth() {
        return WINDOW_WIDTH;
    }

    public static int getWindowHeight() {
        return WINDOW_HEIGHT;
    }

    public Scene getCurrentScene() {
        return SCENE_CURRENT;
    }

    public void closeWindow() {
        STAGE.close();
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
