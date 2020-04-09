package app.GUI.menu;

import app.GUI.HUD.HUDUtil;
import app.GameBuilder;
import app.player.Inventory;
import app.utils.ResourcesUtil;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.*;


public class MenuUtil {

    // class variables
    Scene SCENE_MENU;
    GameBuilder context;
    private final String singleArrow = ">";
    private final String doubleArrow = ">>";
    private final Font title = Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,30);
    private final Font options = Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,25);
    private final Color GREEN = Color.valueOf("#20C20E");



    // main menu group
    InterfaceBuilder mainMenu;

    // settings groups
    InterfaceBuilder settingsMenu;
    InterfaceBuilder environmentMenu;
    InterfaceBuilder skyBoxMenu;
    InterfaceBuilder playerMenu;
    InterfaceBuilder cameraMenu;
    InterfaceBuilder graphicsMenu;

    // menu to explain the controls
    InterfaceBuilder controlsMenu;

    // menu to explain the project
    InterfaceBuilder aboutMenu;



    // variables for the settings menu return state
    private String settingsReturnState;
    public static final String PAUSE = "PAUSE";
    public static final String MAIN = "MAIN";

    // world generation settings
    private int curr_world_type = 0;
    private double curr_world_height_mult;
    private double curr_vegetation_mult;
    private double curr_render_distance;
    private boolean curr_terrain_has_water;

    // sky box settings
    private double curr_sun_scale;
    private double curr_moon_scale;
    private double curr_big_star_scale;
    private int curr_sun_moon_period;
    private int curr_big_star_period;

    // player settings
    private double curr_fly_speed;
    private double curr_jump_height;
    private double curr_run_speed;

    // camera settings
    private int curr_fov_default;
    private double curr_fov_running;
    private double curr_fov_tired;

    // graphics settings
    private double curr_sepia_tone;
    private double curr_bloom;
    private double curr_contrast;
    private double curr_saturation;
    private double curr_hue;
    private double curr_brightness;
    private boolean is_trip_mode;
    private boolean is_motion_blur;



    // static strings to access any menu from the menuGroupHashMap
    public static String GROUP_MAIN_MENU = "GROUP_MAIN_MENU";
    public static String GROUP_CONTROLS = "GROUP_CONTROLS";
    public static String GROUP_SETTINGS = "GROUP_SETTINGS";
    public static String GROUP_ENVIRONMENT = "GROUP_ENVIRONMENT";
    public static String GROUP_SKYBOX = "GROUP_SKYBOX";
    public static String GROUP_PLAYER = "GROUP_PLAYER";
    public static String GROUP_CAMERA = "GROUP_CAMERA";
    public static String GROUP_ABOUT = "GROUP_ABOUT";
    public static String GROUP_GRAPHICS = "GROUP_GRAPHICS";

    // variables to hold and keep track of the different menus
    String currentGroup;
    public HashMap<String, Group> menuGroupMap = new HashMap<>();

    public MenuUtil(GameBuilder ctx) {
        context = ctx;

        // set up interfaces
        mainMenu = new InterfaceBuilder();

        controlsMenu = new InterfaceBuilder();
        aboutMenu = new InterfaceBuilder();
        settingsMenu = new InterfaceBuilder();
        environmentMenu= new InterfaceBuilder();
        skyBoxMenu = new InterfaceBuilder();
        playerMenu = new InterfaceBuilder();
        cameraMenu = new InterfaceBuilder();
        graphicsMenu = new InterfaceBuilder();

        SCENE_MENU = new Scene(mainMenu.getGroup(), context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight());

        settingsReturnState = MAIN;

        curr_world_height_mult = context.getComponents().getEnvironment().getTerrainHeightMultiplier();
        curr_vegetation_mult = context.getComponents().getEnvironment().getVegetationDensityPercent();
        curr_render_distance = context.getComponents().getEnvironment().getTerrainGenerateDistance();
        curr_terrain_has_water = context.getComponents().getEnvironment().getTerrainShouldHaveWater();

        curr_sun_scale = context.getComponents().getEnvironment().getSkybox().getSunScale();
        curr_moon_scale = context.getComponents().getEnvironment().getSkybox().getMoonScale();
        curr_big_star_scale = context.getComponents().getEnvironment().getSkybox().getBigStarScale();
        curr_sun_moon_period = context.getComponents().getEnvironment().getSkybox().getSun_moon_period_multiplier();
        curr_big_star_period = context.getComponents().getEnvironment().getSkybox().getBig_planet_period_multiplier();

        curr_fly_speed = context.getComponents().getPlayer().getFlySpeed();
        curr_jump_height = context.getComponents().getPlayer().getJumpHeightMultiplier();
        curr_run_speed = context.getComponents().getPlayer().getRunMultiplier();

        curr_fov_default = context.getComponents().getCamera().getFov_default();
        curr_fov_running = context.getComponents().getCamera().getFov_running_multiplier();
        curr_fov_tired = context.getComponents().getCamera().getFov_tired_multiplier();

        curr_sepia_tone = context.getEffects().getSepiaTone();
        curr_bloom = context.getEffects().getBloom();
        is_trip_mode = context.getEffects().getTripMode();
        is_motion_blur = context.getEffects().getMotionBlurEnabled();
        curr_contrast = context.getEffects().getContrast();
        curr_saturation = context.getEffects().getSaturation();
        curr_hue = context.getEffects().getHue();
        curr_brightness = context.getEffects().getBrightness();

        buildMainMenu();
        buildControlsMenu();
        buildSettingsMenu();
        buildEnvironmentMenu();
        buildSkyBoxMenu();
        buildPlayerMenu();
        buildCameraMenu();
        buildAboutMenu();
        buildGraphicsMenu();

        addGroup(GROUP_MAIN_MENU, mainMenu.getGroup());
        addGroup(GROUP_SETTINGS, settingsMenu.getGroup());
        addGroup(GROUP_ENVIRONMENT, environmentMenu.getGroup());
        addGroup(GROUP_SKYBOX, skyBoxMenu.getGroup());
        addGroup(GROUP_PLAYER, playerMenu.getGroup());
        addGroup(GROUP_CAMERA, cameraMenu.getGroup());
        addGroup(GROUP_CONTROLS, controlsMenu.getGroup());
        addGroup(GROUP_ABOUT, aboutMenu.getGroup());
        addGroup(GROUP_CONTROLS,aboutMenu.getGroup());
        addGroup(GROUP_GRAPHICS,graphicsMenu.getGroup());

        setControlScheme();
    }

    public void setControlScheme(){
        SCENE_MENU.setOnKeyReleased(keyEvent -> {
            switch(keyEvent.getCode()){
                case ESCAPE:
                    context.getWindow().closeWindow();
                    break;
                case ENTER:
//                    TODO
                    break;
            }
        });
    }

    protected void addGroup(String name, Group group) {
        menuGroupMap.put(name, group);
    }

    protected void removeGroup(String name) {
        menuGroupMap.remove(name);
    }

    public void activateGroup(String name) {
        currentGroup = name;
        SCENE_MENU.setRoot(menuGroupMap.get(name));
    }

    public Scene getScene(){
        return SCENE_MENU;
    }

    public void setSettingsReturnState(String state){settingsReturnState = state;}

    public void buildMainMenu() {

        // draw black backdrop
        mainMenu.drawRectangle(0,0, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight(),0,0, Color.BLACK);

        //draw title
        mainMenu.drawText("ROOT@CS307:~$",
                50,
                50,
                GREEN,
                title);

        mainMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);

        // Start button handler
        Text startArrow = mainMenu.drawText(singleArrow, 50, 140, GREEN, options);
        Text startText = mainMenu.drawText("./Start_Game", 95, 140, Color.WHITE, options);
        Rectangle startHitBox = mainMenu.drawRectangle(50,120,600,30,0,0,Color.TRANSPARENT);
        startHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.getComponents().getEnvironment().reset();
                        context.getWindow().showScene(context.getWindow().getRootScene());
                        ((Inventory) context.getComponents().getHUD().getElement(HUDUtil.INVENTORY)).update();

                    }
                });
        startHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        startArrow.setText(doubleArrow);
                        startText.setFill(GREEN);
                    }
                });
        startHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        startArrow.setText(singleArrow);
                        startText.setFill(Color.WHITE);
                    }
                });

        //options handler
        Text settingsArrow = mainMenu.drawText(singleArrow, 50, 190, GREEN, options);
        Text settingsText = mainMenu.drawText("./Settings", 95, 190, Color.WHITE, options);
        Rectangle settingsHitBox = mainMenu.drawRectangle(50,170,600,30,0,0,Color.TRANSPARENT);
        settingsHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        setSettingsReturnState(MAIN);
                        activateGroup(GROUP_SETTINGS);
                    }
                });
        settingsHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        settingsArrow.setText(doubleArrow);
                        settingsText.setFill(GREEN);
                    }
                });
        settingsHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        settingsArrow.setText(singleArrow);
                        settingsText.setFill(Color.WHITE);
                    }
                });


        //controls handler
        Text controlsArrow = mainMenu.drawText(singleArrow, 50, 240, GREEN, options);
        Text controlsText = mainMenu.drawText("./Controls", 95, 240, Color.WHITE, options);
        Rectangle controlsHitBox = mainMenu.drawRectangle(50,220,600,30,0,0,Color.TRANSPARENT);
        controlsHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_CONTROLS); }
                });
        controlsHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        controlsArrow.setText(doubleArrow);
                        controlsText.setFill(GREEN);
                    }
                });
        controlsHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        controlsArrow.setText(singleArrow);
                        controlsText.setFill(Color.WHITE);
                    }
                });

        //about handler
        Text aboutArrow = mainMenu.drawText(singleArrow, 50, 290, GREEN, options);
        Text aboutText = mainMenu.drawText("./About", 95, 290, Color.WHITE, options);
        Rectangle aboutHitBox = mainMenu.drawRectangle(50,270,600,30,0,0,Color.TRANSPARENT);
        aboutHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_ABOUT); }
                });
        aboutHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        aboutArrow.setText(doubleArrow);
                        aboutText.setFill(GREEN);
                    }
                });
        aboutHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        aboutArrow.setText(singleArrow);
                        aboutText.setFill(Color.WHITE);
                    }
                });

        //quit handler
        Text quitArrow = mainMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text quitText = mainMenu.drawText("./Quit", 95, 340, Color.WHITE, options);
        Rectangle quitHitBox = mainMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
        quitHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { context.getWindow().getStage().close(); }
                });
        quitHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        quitArrow.setText(doubleArrow);
                        quitText.setFill(GREEN);
                    }
                });
        quitHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        quitArrow.setText(singleArrow);
                        quitText.setFill(Color.WHITE);
                    }
                });
    }

    public void buildControlsMenu() {
        // draw black backdrop
        controlsMenu.drawRectangle(0,0, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight(),0,0, Color.BLACK);

        //draw title
        controlsMenu.drawText("ROOT@CS307:~/Controls$",
                50,
                50,
                GREEN,
                title);

        controlsMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);

        //quit handler
        Text returnArrow = controlsMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text returnText = controlsMenu.drawText("./Quit", 95, 340, Color.WHITE, options);
        Rectangle returnHitBox = controlsMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
        returnHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_MAIN_MENU); }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(doubleArrow);
                        returnText.setFill(GREEN);
                    }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(singleArrow);
                        returnText.setFill(Color.WHITE);
                    }
                });
    }

    //*****************************************************************************************************


    public void buildSettingsMenu() {
        // draw black backdrop
        settingsMenu.drawRectangle(0,0, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight(),0,0, Color.BLACK);

        //draw title
        settingsMenu.drawText("ROOT@CS307:~/Settings$",
                50,
                50,
                GREEN,
                title);

        settingsMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);


        Text environmentArrow = settingsMenu.drawText(singleArrow, 50, 140, GREEN, options);
        Text environmentText = settingsMenu.drawText("cd Environment_Settings", 95, 140, Color.WHITE, options);
        Rectangle environmentHitBox = settingsMenu.drawRectangle(50,120,600,30,0,0,Color.TRANSPARENT);
        environmentHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_ENVIRONMENT); }
                });
        environmentHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        environmentArrow.setText(doubleArrow);
                        environmentText.setFill(GREEN);
                    }
                });
        environmentHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        environmentArrow.setText(singleArrow);
                        environmentText.setFill(Color.WHITE);
                    }
                });

        Text skyBoxArrow = settingsMenu.drawText(singleArrow, 50, 190, GREEN, options);
        Text skyBoxText = settingsMenu.drawText("cd SkyBox_Settings", 95, 190, Color.WHITE, options);
        Rectangle skyBoxHitBox = settingsMenu.drawRectangle(50,170,600,30,0,0,Color.TRANSPARENT);
        skyBoxHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_SKYBOX); }
                });
        skyBoxHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        skyBoxArrow.setText(doubleArrow);
                        skyBoxText.setFill(GREEN);
                    }
                });
        skyBoxHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        skyBoxArrow.setText(singleArrow);
                        skyBoxText.setFill(Color.WHITE);
                    }
                });

        Text playerArrow = settingsMenu.drawText(singleArrow, 50, 240, GREEN, options);
        Text playerText = settingsMenu.drawText("cd Player_Settings", 95, 240, Color.WHITE, options);
        Rectangle playerHitBox = settingsMenu.drawRectangle(50,220,600,30,0,0,Color.TRANSPARENT);
        playerHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_PLAYER); }
                });
        playerHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        playerArrow.setText(doubleArrow);
                        playerText.setFill(GREEN);
                    }
                });
        playerHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        playerArrow.setText(singleArrow);
                        playerText.setFill(Color.WHITE);
                    }
                });

        Text cameraArrow = settingsMenu.drawText(singleArrow, 50, 290, GREEN, options);
        Text cameraText = settingsMenu.drawText("cd Camera_Settings", 95, 290, Color.WHITE, options);
        Rectangle cameraHitBox = settingsMenu.drawRectangle(50,270,600,30,0,0,Color.TRANSPARENT);
        cameraHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_CAMERA); }
                });
        cameraHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        cameraArrow.setText(doubleArrow);
                        cameraText.setFill(GREEN);
                    }
                });
        cameraHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        cameraArrow.setText(singleArrow);
                        cameraText.setFill(Color.WHITE);
                    }
                });
        Text graphicsArrow = settingsMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text graphicsText = settingsMenu.drawText("cd Graphics_Settings", 95, 340, Color.WHITE, options);
        Rectangle graphicsHitBox = settingsMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
        graphicsHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_GRAPHICS); }
                });
        graphicsHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        graphicsArrow.setText(doubleArrow);
                        graphicsText.setFill(GREEN);
                    }
                });
        graphicsHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        graphicsArrow.setText(singleArrow);
                        graphicsText.setFill(Color.WHITE);
                    }
                });

        //quit handler
        Text returnArrow = settingsMenu.drawText(singleArrow, 50, 390, GREEN, options);
        Text returnText = settingsMenu.drawText("cd ..", 95, 390, Color.WHITE, options);
        Rectangle returnHitBox = settingsMenu.drawRectangle(50,370,600,30,0,0,Color.TRANSPARENT);
        returnHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        if(settingsReturnState==MAIN){
                            activateGroup(GROUP_MAIN_MENU);
                        }else {
                            context.getWindow().showScene(context.getWindow().getRootScene());
                        }
                    }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(doubleArrow);
                        returnText.setFill(GREEN);
                    }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(singleArrow);
                        returnText.setFill(Color.WHITE);
                    }
                });


    }


    public void buildEnvironmentMenu(){
        // draw black backdrop
        environmentMenu.drawRectangle(0,0, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight(),0,0, Color.BLACK);

        //draw title
        environmentMenu.drawText("ROOT@CS307:~/Settings/Environment$",
                50,
                50,
                GREEN,
                title);

        environmentMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);

        // World Height Multiplier

        Text worldHeightArrow = environmentMenu.drawText(singleArrow, 50, 140, GREEN, options);
        Text worldHeightText= environmentMenu.drawText("./World_Height_Multiplier", 95, 140, Color.WHITE, options);
        Text worldHeightMult = environmentMenu.drawText((int) curr_world_height_mult + " blocks", 550, 140, Color.WHITE, options);
        Rectangle worldHeightHitBox = environmentMenu.drawRectangle(50,120,600,30,0,0,Color.TRANSPARENT);
        worldHeightHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_world_height_mult +=5;
                        if(curr_world_height_mult >35) curr_world_height_mult = 0;
                        worldHeightMult.setText((int) curr_world_height_mult + " blocks");
                        context.getComponents().getEnvironment().setTerrainHeightMultiplier(curr_world_height_mult);
                    }
                });
        worldHeightHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        worldHeightArrow.setText(doubleArrow);
                        worldHeightText.setFill(GREEN);
                        worldHeightMult.setFill(GREEN);
                    }
                });
        worldHeightHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        worldHeightArrow.setText(singleArrow);
                        worldHeightText.setFill(Color.WHITE);
                        worldHeightMult.setFill(Color.WHITE);
                    }
                });

        // Vegetation Multiplier

        Text vegetationArrow = environmentMenu.drawText(singleArrow, 50, 190, GREEN, options);
        Text vegetationText= environmentMenu.drawText("./Vegetation_Percent", 95, 190, Color.WHITE, options);
        Text vegetationMult = environmentMenu.drawText((int) curr_vegetation_mult +"%", 550, 190, Color.WHITE, options);
        Rectangle vegetationHitBox = environmentMenu.drawRectangle(50,170,600,30,0,0,Color.TRANSPARENT);
        vegetationHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_vegetation_mult+=5;
                        if(curr_vegetation_mult>100)curr_vegetation_mult=0;
                        vegetationMult.setText((int) curr_vegetation_mult +"%");
                        context.getComponents().getEnvironment().setVegetationDensityPercent(curr_vegetation_mult);
                    }
                });
        vegetationHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        vegetationArrow.setText(doubleArrow);
                        vegetationText.setFill(GREEN);
                        vegetationMult.setFill(GREEN);
                    }
                });
        vegetationHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        vegetationArrow.setText(singleArrow);
                        vegetationText.setFill(Color.WHITE);
                        vegetationMult.setFill(Color.WHITE);
                    }
                });

        // World type

        Text worldTypeArrow = environmentMenu.drawText(singleArrow, 50, 240, GREEN, options);
        Text worldTypeText = environmentMenu.drawText("./World_Type", 95, 240, Color.WHITE, options);
        Text worldTypeChoice = environmentMenu.drawText(ResourcesUtil.world_types_sorted.get(curr_world_type), 550, 240, Color.WHITE, options);
        Rectangle worldTypeHitBox = environmentMenu.drawRectangle(50,220,600,30,0,0,Color.TRANSPARENT);
        worldTypeHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_world_type++;
                        if(curr_world_type == ResourcesUtil.world_types_sorted.size())curr_world_type = 0;
                        String world_type = ResourcesUtil.world_types_sorted.get(curr_world_type);
                        worldTypeChoice.setText(world_type);
                        System.out.println(world_type +" "+ResourcesUtil.world_types.get(world_type));
                        context.getComponents().getEnvironment().setTerrainBlockType(ResourcesUtil.world_types.get(world_type));
                    }
                });
        worldTypeHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        worldTypeArrow.setText(doubleArrow);
                        worldTypeText.setFill(GREEN);
                        worldTypeChoice.setFill(GREEN);
                    }
                });
        worldTypeHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        worldTypeArrow.setText(singleArrow);
                        worldTypeText.setFill(Color.WHITE);
                        worldTypeChoice.setFill(Color.WHITE);
                    }
                });

        // render distance
        Text renderDistanceArrow = environmentMenu.drawText(singleArrow, 50, 290, GREEN, options);
        Text renderDistanceText= environmentMenu.drawText("./Render_Distance", 95, 290, Color.WHITE, options);
        Text renderDistanceMult = environmentMenu.drawText((int) curr_render_distance + " blocks", 550, 290, Color.WHITE, options);
        Rectangle renderDistanceHitBox = environmentMenu.drawRectangle(50,270,600,30,0,0,Color.TRANSPARENT);
        renderDistanceHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_render_distance+=5;
                        if(curr_render_distance>75)curr_render_distance=0;
                        renderDistanceMult.setText((int) curr_render_distance + " blocks");
                        context.getComponents().getEnvironment().setTerrainGenerateDistance(curr_render_distance);
                    }
                });
        renderDistanceHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        renderDistanceArrow.setText(doubleArrow);
                        renderDistanceText.setFill(GREEN);
                        renderDistanceMult.setFill(GREEN);
                    }
                });
        renderDistanceHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        renderDistanceArrow.setText(singleArrow);
                        renderDistanceText.setFill(Color.WHITE);
                        renderDistanceMult.setFill(Color.WHITE);
                    }
                });


        // if the terrain has water
        Text hasWaterArrow = environmentMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text hasWaterText = environmentMenu.drawText("./Has_Water", 95, 340, Color.WHITE, options);
        Text hasWaterChoice = environmentMenu.drawText( String.valueOf(curr_terrain_has_water), 550, 340, Color.WHITE, options);
        Rectangle hasWaterHitBox = environmentMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
        hasWaterHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_terrain_has_water= !curr_terrain_has_water;

                        hasWaterChoice.setText(String.valueOf(curr_terrain_has_water));
                        context.getComponents().getEnvironment().setTerrainShouldHaveWater(curr_terrain_has_water);
                    }
                });
        hasWaterHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        hasWaterArrow.setText(doubleArrow);
                        hasWaterText.setFill(GREEN);
                        hasWaterChoice.setFill(GREEN);
                    }
                });
        hasWaterHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        hasWaterArrow.setText(singleArrow);
                        hasWaterText.setFill(Color.WHITE);
                        hasWaterChoice.setFill(Color.WHITE);
                    }
                });

        //quit handler
        Text returnArrow = environmentMenu.drawText(singleArrow, 50, 390, GREEN, options);
        Text returnText = environmentMenu.drawText("./Back", 95, 390, Color.WHITE, options);
        Rectangle returnHitBox = environmentMenu.drawRectangle(50,370,600,30,0,0,Color.TRANSPARENT);
        returnHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_SETTINGS); }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(doubleArrow);
                        returnText.setFill(GREEN);
                    }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(singleArrow);
                        returnText.setFill(Color.WHITE);
                    }
                });

    }


    public void buildSkyBoxMenu(){
        // draw black backdrop
        skyBoxMenu.drawRectangle(0,0, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight(),0,0, Color.BLACK);

        //draw title
        skyBoxMenu.drawText("ROOT@CS307:~/Settings/SkyBox$",
                50,
                50,
                GREEN,
                title);

        skyBoxMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);

        // sun size multiplier
        Text sunArrow = skyBoxMenu.drawText(singleArrow, 50, 140, GREEN, options);
        Text sunText= skyBoxMenu.drawText("./Sun_Scale", 95, 140, Color.WHITE, options);
        Text sunMult = skyBoxMenu.drawText(Integer.toString((int)curr_sun_scale), 550, 140, Color.WHITE, options);
        Rectangle sunHitBox = skyBoxMenu.drawRectangle(50,120,600,30,0,0,Color.TRANSPARENT);
        sunHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_sun_scale +=500;
                        if(curr_sun_scale == 10500) curr_sun_scale = 0;
                        sunMult.setText(Integer.toString((int)curr_sun_scale));
                        context.getComponents().getEnvironment().getSkybox().setSunScale(curr_sun_scale);
                    }
                });
        sunHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        sunArrow.setText(doubleArrow);
                        sunText.setFill(GREEN);
                        sunMult.setFill(GREEN);
                    }
                });
        sunHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        sunArrow.setText(singleArrow);
                        sunText.setFill(Color.WHITE);
                        sunMult.setFill(Color.WHITE);
                    }
                });

        // moon size multiplier
        Text moonArrow = skyBoxMenu.drawText(singleArrow, 50, 190, GREEN, options);
        Text moonText= skyBoxMenu.drawText("./Moon_Scale", 95, 190, Color.WHITE, options);
        Text moonMult = skyBoxMenu.drawText(Integer.toString((int)curr_moon_scale), 550, 190, Color.WHITE, options);
        Rectangle moonHitBox = skyBoxMenu.drawRectangle(50,170,600,30,0,0,Color.TRANSPARENT);
        moonHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_moon_scale +=500;
                        if(curr_moon_scale == 8000) curr_moon_scale = 0;
                        moonMult.setText(Integer.toString((int)curr_moon_scale));
                        context.getComponents().getEnvironment().getSkybox().setMoonScale(curr_moon_scale);
                    }
                });
        moonHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        moonArrow.setText(doubleArrow);
                        moonText.setFill(GREEN);
                        moonMult.setFill(GREEN);
                    }
                });
        moonHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        moonArrow.setText(singleArrow);
                        moonText.setFill(Color.WHITE);
                        moonMult.setFill(Color.WHITE);
                    }
                });

        // big star size multiplier
        Text bigStarArrow = skyBoxMenu.drawText(singleArrow, 50, 240, GREEN, options);
        Text bigStarText= skyBoxMenu.drawText("./Big_Star_Scale", 95, 240, Color.WHITE, options);
        Text bigStarMult = skyBoxMenu.drawText(Integer.toString((int)curr_big_star_scale), 550, 240, Color.WHITE, options);
        Rectangle bigStarHitBox = skyBoxMenu.drawRectangle(50,220,600,30,0,0,Color.TRANSPARENT);
        bigStarHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_big_star_scale +=500;
                        if(curr_big_star_scale == 10500) curr_big_star_scale = 0;
                        bigStarMult.setText(Integer.toString((int)curr_big_star_scale));
                        context.getComponents().getEnvironment().getSkybox().setBigStarScale(curr_big_star_scale);
                    }
                });
        bigStarHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        bigStarArrow.setText(doubleArrow);
                        bigStarText.setFill(GREEN);
                        bigStarMult.setFill(GREEN);
                    }
                });
        bigStarHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        bigStarArrow.setText(singleArrow);
                        bigStarText.setFill(Color.WHITE);
                        bigStarMult.setFill(Color.WHITE);
                    }
                });

        // period multiplier for sun / moon  . cannot be 0. exponential up to 256
        Text sunMoonPeriodArrow = skyBoxMenu.drawText(singleArrow, 50, 290, GREEN, options);
        Text sunMoonPeriodText= skyBoxMenu.drawText("./Sun_Moon_Period_Multiplier", 95, 290, Color.WHITE, options);
        Text sunMoonPeriodMult = skyBoxMenu.drawText(curr_sun_moon_period + " sec", 550, 290, Color.WHITE, options);
        Rectangle sunMoonPeriodHitBox = skyBoxMenu.drawRectangle(50,270,600,30,0,0,Color.TRANSPARENT);
        sunMoonPeriodHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_sun_moon_period *= 2;
                        if(curr_sun_moon_period > 256) curr_sun_moon_period = 2;
                        sunMoonPeriodMult.setText(curr_sun_moon_period + " sec");
                        context.getComponents().getEnvironment().getSkybox().setSun_moon_period_multiplier(curr_sun_moon_period);
                    }
                });
        sunMoonPeriodHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        sunMoonPeriodArrow.setText(doubleArrow);
                        sunMoonPeriodText.setFill(GREEN);
                        sunMoonPeriodMult.setFill(GREEN);
                    }
                });
        sunMoonPeriodHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        sunMoonPeriodArrow.setText(singleArrow);
                        sunMoonPeriodText.setFill(Color.WHITE);
                        sunMoonPeriodMult.setFill(Color.WHITE);
                    }
                });

        // period multiplier for big star
        Text bigStarPeriodArrow = skyBoxMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text bigStarPeriodText= skyBoxMenu.drawText("./Big_Star_Period_Multiplier", 95, 340, Color.WHITE, options);
        Text bigStarPeriodMult = skyBoxMenu.drawText(curr_big_star_period + " sec", 550, 340, Color.WHITE, options);
        Rectangle bigStarPeriodHitBox = skyBoxMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
        bigStarPeriodHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_big_star_period *= 2;
                        if(curr_big_star_period > 256) curr_big_star_period = 2;
                        bigStarPeriodMult.setText(curr_big_star_period + " sec");
                        context.getComponents().getEnvironment().getSkybox().setBig_planet_period_multiplier(curr_big_star_period);
                    }
                });
        bigStarPeriodHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        bigStarPeriodArrow.setText(doubleArrow);
                        bigStarPeriodText.setFill(GREEN);
                        bigStarPeriodMult.setFill(GREEN);
                    }
                });
        bigStarPeriodHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        bigStarPeriodArrow.setText(singleArrow);
                        bigStarPeriodText.setFill(Color.WHITE);
                        bigStarPeriodMult.setFill(Color.WHITE);
                    }
                });



        //quit handler
        Text returnArrow = skyBoxMenu.drawText(singleArrow, 50, 440, GREEN, options);
        Text returnText = skyBoxMenu.drawText("./Back", 95, 440, Color.WHITE, options);
        Rectangle returnHitBox = skyBoxMenu.drawRectangle(50,420,600,30,0,0,Color.TRANSPARENT);
        returnHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_SETTINGS); }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(doubleArrow);
                        returnText.setFill(GREEN);
                    }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(singleArrow);
                        returnText.setFill(Color.WHITE);
                    }
                });
    }


    public void buildPlayerMenu(){
        // draw black backdrop
        playerMenu.drawRectangle(0,0, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight(),0,0, Color.BLACK);

        //draw title
        playerMenu.drawText("ROOT@CS307:~/Settings/Player$",
                50,
                50,
                GREEN,
                title);

        playerMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);


        Text playerFlySpeedArrow = playerMenu.drawText(singleArrow, 50, 140, GREEN, options);
        Text playerFlySpeedText= playerMenu.drawText("./Fly_Speed", 95, 140, Color.WHITE, options);
        Text playerFlySpeedMult = playerMenu.drawText(Integer.toString((int)curr_fly_speed), 550, 140, Color.WHITE, options);
        Rectangle playerFlySpeedHitBox = playerMenu.drawRectangle(50,120,600,30,0,0,Color.TRANSPARENT);
        playerFlySpeedHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_fly_speed +=1;
                        if(curr_fly_speed>10) curr_fly_speed =1;
                        playerFlySpeedMult.setText(Integer.toString((int)curr_fly_speed));
                        context.getComponents().getPlayer().setFlySpeed(curr_fly_speed);
                    }
                });
        playerFlySpeedHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        playerFlySpeedArrow.setText(doubleArrow);
                        playerFlySpeedText.setFill(GREEN);
                        playerFlySpeedMult.setFill(GREEN);
                    }
                });
        playerFlySpeedHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        playerFlySpeedArrow.setText(singleArrow);
                        playerFlySpeedText.setFill(Color.WHITE);
                        playerFlySpeedMult.setFill(Color.WHITE);
                    }
                });


        Text playerJumpHeightArrow = playerMenu.drawText(singleArrow, 50, 190, GREEN, options);
        Text playerJumpHeightText= playerMenu.drawText("./Jump_Height_Multiplier", 95, 190, Color.WHITE, options);
        Text playerJumpHeightMult = playerMenu.drawText(Double.toString(curr_jump_height), 550, 190, Color.WHITE, options);
        Rectangle playerJumpHeightHitBox = playerMenu.drawRectangle(50,170,600,30,0,0,Color.TRANSPARENT);
        playerJumpHeightHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_jump_height +=.5;
                        if(curr_jump_height>10) curr_jump_height = 0;
                        playerJumpHeightMult.setText(Double.toString(curr_jump_height));
                        context.getComponents().getPlayer().setJumpHeightMultiplier(curr_jump_height);
                    }
                });
        playerJumpHeightHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        playerJumpHeightArrow.setText(doubleArrow);
                        playerJumpHeightText.setFill(GREEN);
                        playerJumpHeightMult.setFill(GREEN);
                    }
                });
        playerJumpHeightHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        playerJumpHeightArrow.setText(singleArrow);
                        playerJumpHeightText.setFill(Color.WHITE);
                        playerJumpHeightMult.setFill(Color.WHITE);
                    }
                });

        Text playerRunSpeedArrow = playerMenu.drawText(singleArrow, 50, 240, GREEN, options);
        Text playerRunSpeedText = playerMenu.drawText("./Run_Speed_Multiplier", 95, 240, Color.WHITE, options);
        Text playerRunSpeedMult = playerMenu.drawText(Double.toString(curr_run_speed), 550, 240, Color.WHITE, options);
        Rectangle playerRunSpeedHitBox = playerMenu.drawRectangle(50,220,600,30,0,0,Color.TRANSPARENT);
        playerRunSpeedHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_run_speed +=.25;
                        if(curr_run_speed>5) curr_run_speed = .5;
                        playerRunSpeedMult.setText(Double.toString(curr_run_speed));
                        context.getComponents().getPlayer().setRunMultiplier(curr_run_speed);
                    }
                });
        playerRunSpeedHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        playerRunSpeedArrow.setText(doubleArrow);
                        playerRunSpeedText.setFill(GREEN);
                        playerRunSpeedMult.setFill(GREEN);
                    }
                });
        playerRunSpeedHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        playerRunSpeedArrow.setText(singleArrow);
                        playerRunSpeedText.setFill(Color.WHITE);
                        playerRunSpeedMult.setFill(Color.WHITE);
                    }
                });

        // player run multiplier


        //quit handler
        Text returnArrow = playerMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text returnText = playerMenu.drawText("./Back", 95, 340, Color.WHITE, options);
        Rectangle returnHitBox = playerMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
        returnHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_SETTINGS); }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(doubleArrow);
                        returnText.setFill(GREEN);
                    }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(singleArrow);
                        returnText.setFill(Color.WHITE);
                    }
                });
    }


    public void buildCameraMenu(){
        // draw black backdrop
        cameraMenu.drawRectangle(0,0, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight(),0,0, Color.BLACK);

        //draw title
        cameraMenu.drawText("ROOT@CS307:~/Settings/Camera$",
                50,
                50,
                GREEN,
                title);

        cameraMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);

        // default fov
        Text defaultFovArrow = cameraMenu.drawText(singleArrow, 50, 140, GREEN, options);
        Text defaultFovText= cameraMenu.drawText("./Default_FOV", 95, 140, Color.WHITE, options);
        Text defaultFovMult = cameraMenu.drawText(curr_fov_default + " deg", 550, 140, Color.WHITE, options);
        Rectangle defaultFovHitBox = cameraMenu.drawRectangle(50,120,600,30,0,0,Color.TRANSPARENT);
        defaultFovHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_fov_default +=5;
                        if(curr_fov_default > 100) curr_fov_default = 20;
                        defaultFovMult.setText(curr_fov_default + " deg");
                        context.getComponents().getCamera().setFov_default(curr_fov_default);
                    }
                });
        defaultFovHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        defaultFovArrow.setText(doubleArrow);
                        defaultFovText.setFill(GREEN);
                        defaultFovMult.setFill(GREEN);
                    }
                });
        defaultFovHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        defaultFovArrow.setText(singleArrow);
                        defaultFovText.setFill(Color.WHITE);
                        defaultFovMult.setFill(Color.WHITE);
                    }
                });

        // running fov
        Text runningFovArrow = cameraMenu.drawText(singleArrow, 50, 190, GREEN, options);
        Text runningFovText= cameraMenu.drawText("./Running_FOV_Multiplier", 95, 190, Color.WHITE, options);
        Text runningFovMult = cameraMenu.drawText(Math.floor(curr_fov_running * 100) / 100 + " x Default", 550, 190, Color.WHITE, options);
        Rectangle runningFovHitBox = cameraMenu.drawRectangle(50,170,600,30,0,0,Color.TRANSPARENT);
        runningFovHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_fov_running += .25;
                        if(curr_fov_running > 3) curr_fov_running = 1;
                        runningFovMult.setText(Math.floor(curr_fov_running * 100) / 100 + " x Default");
                        context.getComponents().getCamera().setFov_running_multiplier(curr_fov_running);
                    }
                });
        runningFovHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        runningFovArrow.setText(doubleArrow);
                        runningFovText.setFill(GREEN);
                        runningFovMult.setFill(GREEN);
                    }
                });
        runningFovHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        runningFovArrow.setText(singleArrow);
                        runningFovText.setFill(Color.WHITE);
                        runningFovMult.setFill(Color.WHITE);
                    }
                });

        // tired fov
        Text tiredFovArrow = cameraMenu.drawText(singleArrow, 50, 240, GREEN, options);
        Text tiredFovText= cameraMenu.drawText("./Tired_FOV_Multiplier", 95, 240, Color.WHITE, options);
        Text tiredFovMult = cameraMenu.drawText(Math.floor(curr_fov_tired * 100) / 100 + " x Default", 550, 240, Color.WHITE, options);
        Rectangle tiredFovHitBox = cameraMenu.drawRectangle(50,220,600,30,0,0,Color.TRANSPARENT);
        tiredFovHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_fov_tired -= .1;
                        if(curr_fov_tired < .5) curr_fov_tired = 1;
                        tiredFovMult.setText(Math.floor(curr_fov_tired * 100) / 100 + " x Default");
                        context.getComponents().getCamera().setFov_tired_multiplier(curr_fov_tired);
                    }
                });
        tiredFovHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        tiredFovArrow.setText(doubleArrow);
                        tiredFovText.setFill(GREEN);
                        tiredFovMult.setFill(GREEN);
                    }
                });
        tiredFovHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        tiredFovArrow.setText(singleArrow);
                        tiredFovText.setFill(Color.WHITE);
                        tiredFovMult.setFill(Color.WHITE);
                    }
                });

        //quit handler
        Text returnArrow = cameraMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text returnText = cameraMenu.drawText("./Back", 95, 340, Color.WHITE, options);
        Rectangle returnHitBox = cameraMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
        returnHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_SETTINGS); }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(doubleArrow);
                        returnText.setFill(GREEN);
                    }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(singleArrow);
                        returnText.setFill(Color.WHITE);
                    }
                });
    }

    public void buildGraphicsMenu(){
        // draw black backdrop
        graphicsMenu.drawRectangle(0,0, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight(),0,0, Color.BLACK);

        //draw title
        graphicsMenu.drawText("ROOT@CS307:~$ ./About",
                50,
                50,
                GREEN,
                title);

        graphicsMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);

        Text sepiaToneArrow = graphicsMenu.drawText(singleArrow, 50, 140, GREEN, options);
        Text sepiaToneText= graphicsMenu.drawText("./Sepia_Tone", 95, 140, Color.WHITE, options);
        Text sepiaToneMult = graphicsMenu.drawText(Double.toString(curr_sepia_tone) , 550, 140, Color.WHITE, options);
        Rectangle sepiaToneHitBox = graphicsMenu.drawRectangle(50,120,600,30,0,0,Color.TRANSPARENT);
        sepiaToneHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_sepia_tone += .1;
                        if(curr_sepia_tone >1) curr_sepia_tone = 0.0;
                        sepiaToneMult.setText(Double.toString(curr_sepia_tone));
                        context.getEffects().setSepiaTone(curr_sepia_tone);
                    }
                });
        sepiaToneHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        sepiaToneArrow.setText(doubleArrow);
                        sepiaToneText.setFill(GREEN);
                        sepiaToneMult.setFill(GREEN);
                    }
                });
        sepiaToneHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        sepiaToneArrow.setText(singleArrow);
                        sepiaToneText.setFill(Color.WHITE);
                        sepiaToneMult.setFill(Color.WHITE);
                    }
                });

        Text bloomArrow = graphicsMenu.drawText(singleArrow, 50, 190, GREEN, options);
        Text bloomText= graphicsMenu.drawText("./Bloom", 95, 190, Color.WHITE, options);
        Text bloomMult = graphicsMenu.drawText(Double.toString(curr_bloom) , 550, 190, Color.WHITE, options);
        Rectangle bloomHitBox = graphicsMenu.drawRectangle(50,170,600,30,0,0,Color.TRANSPARENT);
        bloomHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_bloom += .1;
                        if(curr_bloom >1) curr_bloom = 0.0;
                        bloomMult.setText(Double.toString(curr_bloom));
                        context.getEffects().setBloom(curr_bloom);
                    }
                });
        bloomHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        bloomArrow.setText(doubleArrow);
                        bloomText.setFill(GREEN);
                        bloomMult.setFill(GREEN);
                    }
                });
        bloomHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        bloomArrow.setText(singleArrow);
                        bloomText.setFill(Color.WHITE);
                        bloomMult.setFill(Color.WHITE);
                    }
                });

        Text tripModeArrow = graphicsMenu.drawText(singleArrow, 50, 240, GREEN, options);
        Text tripModeText= graphicsMenu.drawText("./Trip_Mode", 95, 240, Color.WHITE, options);
        Text tripModeMult = graphicsMenu.drawText(String.valueOf(is_trip_mode) , 550, 240, Color.WHITE, options);
        Rectangle tripModeHitBox = graphicsMenu.drawRectangle(50,220,600,30,0,0,Color.TRANSPARENT);
        tripModeHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        is_trip_mode = !is_trip_mode;
                        tripModeMult.setText(String.valueOf(is_trip_mode));
                        context.getEffects().setTripMode(is_trip_mode);
                    }
                });
        tripModeHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        tripModeArrow.setText(doubleArrow);
                        tripModeText.setFill(GREEN);
                        tripModeMult.setFill(GREEN);
                    }
                });
        tripModeHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        tripModeArrow.setText(singleArrow);
                        tripModeText.setFill(Color.WHITE);
                        tripModeMult.setFill(Color.WHITE);
                    }
                });

        Text motionBlurArrow = graphicsMenu.drawText(singleArrow, 50, 290, GREEN, options);
        Text motionBlurText= graphicsMenu.drawText("./Motion_Blur", 95, 290, Color.WHITE, options);
        Text motionBlurMult = graphicsMenu.drawText(String.valueOf(is_motion_blur) , 550, 290, Color.WHITE, options);
        Rectangle motionBlurHitBox = graphicsMenu.drawRectangle(50,270,600,30,0,0,Color.TRANSPARENT);
        motionBlurHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        is_motion_blur = !is_motion_blur;
                        motionBlurMult.setText(String.valueOf(is_motion_blur));
                        context.getEffects().setMotionBlurEnabled(is_motion_blur);
                    }
                });
        motionBlurHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        motionBlurArrow.setText(doubleArrow);
                        motionBlurText.setFill(GREEN);
                        motionBlurMult.setFill(GREEN);
                    }
                });
        motionBlurHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        motionBlurArrow.setText(singleArrow);
                        motionBlurText.setFill(Color.WHITE);
                        motionBlurMult.setFill(Color.WHITE);
                    }
                });

        Text contrastArrow = graphicsMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text contrastText= graphicsMenu.drawText("./Contrast", 95, 340, Color.WHITE, options);
        Text contrastMult = graphicsMenu.drawText(Double.toString(curr_contrast) , 550, 340, Color.WHITE, options);
        Rectangle contrastHitBox = graphicsMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
        contrastHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_contrast += .1;
                        if(curr_contrast >1) curr_contrast = 0.0;
                        contrastMult.setText(Double.toString(curr_contrast));
                        context.getEffects().setContrast(curr_contrast);
                    }
                });
        contrastHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        contrastArrow.setText(doubleArrow);
                        contrastText.setFill(GREEN);
                        contrastMult.setFill(GREEN);
                    }
                });
        contrastHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        contrastArrow.setText(singleArrow);
                        contrastText.setFill(Color.WHITE);
                        contrastMult.setFill(Color.WHITE);
                    }
                });

        Text saturationArrow = graphicsMenu.drawText(singleArrow, 50, 390, GREEN, options);
        Text saturationText= graphicsMenu.drawText("./Saturation", 95, 390, Color.WHITE, options);
        Text saturationMult = graphicsMenu.drawText(Double.toString(curr_saturation) , 550, 390, Color.WHITE, options);
        Rectangle saturationHitBox = graphicsMenu.drawRectangle(50,370,600,30,0,0,Color.TRANSPARENT);
        saturationHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_saturation += .1;
                        if(curr_saturation >1) curr_saturation = 0.0;
                        saturationMult.setText(Double.toString(curr_saturation));
                        context.getEffects().setContrast(curr_saturation);
                    }
                });
        saturationHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        saturationArrow.setText(doubleArrow);
                        saturationText.setFill(GREEN);
                        saturationMult.setFill(GREEN);
                    }
                });
        saturationHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        saturationArrow.setText(singleArrow);
                        saturationText.setFill(Color.WHITE);
                        saturationMult.setFill(Color.WHITE);
                    }
                });

        Text hueArrow = graphicsMenu.drawText(singleArrow, 50, 440, GREEN, options);
        Text hueText= graphicsMenu.drawText("./Hue", 95, 440, Color.WHITE, options);
        Text hueMult = graphicsMenu.drawText(Double.toString(curr_hue) , 550, 440, Color.WHITE, options);
        Rectangle hueHitBox = graphicsMenu.drawRectangle(50,420,600,30,0,0,Color.TRANSPARENT);
        hueHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_hue += .1;
                        if(curr_hue >1) curr_hue = 0.0;
                        hueMult.setText(Double.toString(curr_hue));
                        context.getEffects().setContrast(curr_hue);
                    }
                });
        hueHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        hueArrow.setText(doubleArrow);
                        hueText.setFill(GREEN);
                        hueMult.setFill(GREEN);
                    }
                });
        hueHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        hueArrow.setText(singleArrow);
                        hueText.setFill(Color.WHITE);
                        hueMult.setFill(Color.WHITE);
                    }
                });

        Text brightnessArrow = graphicsMenu.drawText(singleArrow, 50, 490, GREEN, options);
        Text brightnessText= graphicsMenu.drawText("./Brightness", 95, 490, Color.WHITE, options);
        Text brightnessMult = graphicsMenu.drawText(Double.toString(curr_brightness) , 550, 490, Color.WHITE, options);
        Rectangle brightnessHitBox = graphicsMenu.drawRectangle(50,470,600,30,0,0,Color.TRANSPARENT);
        brightnessHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_brightness += .1;
                        if(curr_brightness >1) curr_brightness = 0.0;
                        brightnessMult.setText(Double.toString(curr_brightness));
                        context.getEffects().setContrast(curr_brightness);
                    }
                });
        brightnessHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        brightnessArrow.setText(doubleArrow);
                        brightnessText.setFill(GREEN);
                        brightnessMult.setFill(GREEN);
                    }
                });
        brightnessHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        brightnessArrow.setText(singleArrow);
                        brightnessText.setFill(Color.WHITE);
                        brightnessMult.setFill(Color.WHITE);
                    }
                });

        //quit handler
        Text returnArrow = graphicsMenu.drawText(singleArrow, 50, 540, GREEN, options);
        Text returnText = graphicsMenu.drawText("./Back", 95, 540, Color.WHITE, options);
        Rectangle returnHitBox = graphicsMenu.drawRectangle(50,520,600,30,0,0,Color.TRANSPARENT);
        returnHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_SETTINGS); }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(doubleArrow);
                        returnText.setFill(GREEN);
                    }
                });
        returnHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        returnArrow.setText(singleArrow);
                        returnText.setFill(Color.WHITE);
                    }
                });
    }


    //*****************************************************************************************************
    // ABOUT MENU
    public void buildAboutMenu() {
        // draw black backdrop
        aboutMenu.drawRectangle(0,0, context.getWindow().getWindowWidth(), context.getWindow().getWindowHeight(),0,0, Color.BLACK);

        //draw title
        aboutMenu.drawText("ROOT@CS307:~$ ./About",
                50,
                50,
                GREEN,
                title);

        aboutMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);

        //quit handler
        Text quitArrow = aboutMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text quitText = aboutMenu.drawText("./Main_Menu", 95, 340, Color.WHITE, options);
        Rectangle quitHitBox = aboutMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
        quitHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_MAIN_MENU); }
                });
        quitHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        quitArrow.setText(doubleArrow);
                        quitText.setFill(GREEN);
                    }
                });
        quitHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        quitArrow.setText(singleArrow);
                        quitText.setFill(Color.WHITE);
                    }
                });
    }


}