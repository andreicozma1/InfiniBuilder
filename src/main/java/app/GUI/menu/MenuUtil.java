package app.GUI.menu;

import app.GameBuilder;
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
    private String singleArrow = ">";
    private String doubleArrow = ">>";
    private Font title = Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,30);
    private Font options = Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,25);
    private Color GREEN = Color.valueOf("#20C20E");



    // main menu group
    InterfaceBuilder mainMenu;

    // settings groups
    InterfaceBuilder settingsMenu;
    InterfaceBuilder environmentMenu;
    InterfaceBuilder skyBoxMenu;
    InterfaceBuilder playerMenu;
    InterfaceBuilder cameraMenu;


    // menu to explain the controls
    InterfaceBuilder controlsMenu;

    // menu to explain the project
    InterfaceBuilder aboutMenu;

    // menu that will display when the player dies
    InterfaceBuilder deathMenu;

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

    // static strings to access any menu from the menuGroupHashMap
    public static String GROUP_MAIN_MENU = "GROUP_MAIN_MENU";
    public static String GROUP_CONTROLS = "GROUP_CONTROLS";
    public static String GROUP_SETTINGS = "GROUP_SETTINGS";
    public static String GROUP_ENVIRONMENT = "GROUP_ENVIRONMENT";
    public static String GROUP_SKYBOX = "GROUP_SKYBOX";
    public static String GROUP_PLAYER = "GROUP_PLAYER";
    public static String GROUP_CAMERA = "GROUP_CAMERA";
    public static String GROUP_ABOUT = "GROUP_ABOUT";
    public static String GROUP_DEATH = "GROUP_DEATH";

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
        deathMenu = new InterfaceBuilder();

        SCENE_MENU = new Scene(mainMenu.getGroup(),context.getWindowWidth(),context.getWindowHeight());

        settingsReturnState = MAIN;

        curr_world_height_mult = context.getEnvironment().getTerrainHeightMultiplier();
        curr_vegetation_mult = context.getEnvironment().getVegetationDensityPercent();
        curr_render_distance = context.getEnvironment().getTerrainRenderDistance();
        curr_terrain_has_water = context.getEnvironment().isTerrain_should_have_water();

        curr_sun_scale = context.getEnvironment().getSkybox().getSunScale();
        curr_moon_scale = context.getEnvironment().getSkybox().getMoonScale();
        curr_big_star_scale = context.getEnvironment().getSkybox().getBigStarScale();
        curr_sun_moon_period = context.getEnvironment().getSkybox().getSun_moon_period_multiplier();
        curr_big_star_period = context.getEnvironment().getSkybox().getBig_planet_period_multiplier();

        curr_fly_speed = context.getPlayer().getFlySpeed();
        curr_jump_height = context.getPlayer().getJumpHeightMultiplier();
        curr_run_speed = context.getPlayer().getRunMultiplier();

        curr_fov_default = context.getCamera().getFov_default();
        curr_fov_running = context.getCamera().getFov_running_multiplier();
        curr_fov_tired = context.getCamera().getFov_tired_multiplier();


        buildMainMenu();
        buildControlsMenu();
        buildSettingsMenu();
        buildEnvironmentMenu();
        buildSkyBoxMenu();
        buildPlayerMenu();
        buildCameraMenu();
        buildAboutMenu();
        buildDeathMenu();

        addGroup(GROUP_MAIN_MENU, mainMenu.getGroup());
        addGroup(GROUP_SETTINGS, settingsMenu.getGroup());
        addGroup(GROUP_ENVIRONMENT, environmentMenu.getGroup());
        addGroup(GROUP_SKYBOX, skyBoxMenu.getGroup());
        addGroup(GROUP_PLAYER, playerMenu.getGroup());
        addGroup(GROUP_CAMERA, cameraMenu.getGroup());
        addGroup(GROUP_CONTROLS, controlsMenu.getGroup());
        addGroup(GROUP_ABOUT, aboutMenu.getGroup());
        addGroup(GROUP_DEATH,deathMenu.getGroup());

        System.out.println("curr_sun_scale = "+curr_sun_scale);
        setControlScheme();
    }

    public void setControlScheme(){
        SCENE_MENU.setOnKeyReleased(keyEvent -> {
            switch(keyEvent.getCode()){
                case ESCAPE:
                    context.closeWindow();
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
        mainMenu.drawRectangle(0,0,context.getWindowWidth(),context.getWindowHeight(),0,0, Color.BLACK);

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
                        context.getEnvironment().reset();
                        context.showScene(context.getGameRootScene());
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
                    public void handle(MouseEvent me) { context.getSTAGE().close(); }
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
        controlsMenu.drawRectangle(0,0,context.getWindowWidth(),context.getWindowHeight(),0,0, Color.BLACK);

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
        settingsMenu.drawRectangle(0,0,context.getWindowWidth(),context.getWindowHeight(),0,0, Color.BLACK);

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


        //quit handler
        Text returnArrow = settingsMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text returnText = settingsMenu.drawText("cd ..", 95, 340, Color.WHITE, options);
        Rectangle returnHitBox = settingsMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
        returnHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        if(settingsReturnState==MAIN){
                            activateGroup(GROUP_MAIN_MENU);
                        }else {
                            context.showScene(context.getGameRootScene());
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
        environmentMenu.drawRectangle(0,0,context.getWindowWidth(),context.getWindowHeight(),0,0, Color.BLACK);

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
        Text worldHeightMult = environmentMenu.drawText(Integer.toString((int)curr_world_height_mult) + " blocks", 550, 140, Color.WHITE, options);
        Rectangle worldHeightHitBox = environmentMenu.drawRectangle(50,120,600,30,0,0,Color.TRANSPARENT);
        worldHeightHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_world_height_mult +=5;
                        if(curr_world_height_mult >35) curr_world_height_mult = 0;
                        worldHeightMult.setText(Integer.toString((int)curr_world_height_mult) + " blocks");
                        context.getEnvironment().setTerrainHeightMultiplier(curr_world_height_mult);
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
        Text vegetationMult = environmentMenu.drawText(Integer.toString((int) curr_vegetation_mult)+"%", 550, 190, Color.WHITE, options);
        Rectangle vegetationHitBox = environmentMenu.drawRectangle(50,170,600,30,0,0,Color.TRANSPARENT);
        vegetationHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_vegetation_mult+=5;
                        if(curr_vegetation_mult>100)curr_vegetation_mult=0;
                        vegetationMult.setText(Integer.toString((int) curr_vegetation_mult)+"%");
                        context.getEnvironment().setVegetationDensityPercent(curr_vegetation_mult);
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
                        context.getEnvironment().setTerrainBlockType(ResourcesUtil.world_types.get(world_type));
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
        Text renderDistanceMult = environmentMenu.drawText(Integer.toString((int)curr_render_distance) + " blocks", 550, 290, Color.WHITE, options);
        Rectangle renderDistanceHitBox = environmentMenu.drawRectangle(50,270,600,30,0,0,Color.TRANSPARENT);
        renderDistanceHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_render_distance+=5;
                        if(curr_render_distance>50)curr_render_distance=0;
                        renderDistanceMult.setText(Integer.toString((int) curr_render_distance) + " blocks");
                        context.getEnvironment().setTerrainRenderDistance(curr_render_distance);
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
                        if(curr_terrain_has_water)curr_terrain_has_water=false;
                        else curr_terrain_has_water = true;

                        hasWaterChoice.setText(String.valueOf(curr_terrain_has_water));
                        context.getEnvironment().setTerrain_should_have_water(curr_terrain_has_water);
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
        skyBoxMenu.drawRectangle(0,0,context.getWindowWidth(),context.getWindowHeight(),0,0, Color.BLACK);

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
                        context.getEnvironment().getSkybox().setSunScale(curr_sun_scale);
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
                        context.getEnvironment().getSkybox().setMoonScale(curr_moon_scale);
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
                        context.getEnvironment().getSkybox().setBigStarScale(curr_big_star_scale);
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
        Text sunMoonPeriodMult = skyBoxMenu.drawText(Integer.toString((int)curr_sun_moon_period) + " sec", 550, 290, Color.WHITE, options);
        Rectangle sunMoonPeriodHitBox = skyBoxMenu.drawRectangle(50,270,600,30,0,0,Color.TRANSPARENT);
        sunMoonPeriodHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_sun_moon_period *= 2;
                        if(curr_sun_moon_period > 256) curr_sun_moon_period = 2;
                        sunMoonPeriodMult.setText(Integer.toString((int)curr_sun_moon_period) + " sec");
                        context.getEnvironment().getSkybox().setSun_moon_period_multiplier(curr_sun_moon_period);
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
        Text bigStarPeriodMult = skyBoxMenu.drawText(Integer.toString((int)curr_big_star_period) + " sec", 550, 340, Color.WHITE, options);
        Rectangle bigStarPeriodHitBox = skyBoxMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
        bigStarPeriodHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_big_star_period *= 2;
                        if(curr_big_star_period > 256) curr_big_star_period = 2;
                        bigStarPeriodMult.setText(Integer.toString((int)curr_big_star_period) + " sec");
                        context.getEnvironment().getSkybox().setBig_planet_period_multiplier(curr_big_star_period);
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
        playerMenu.drawRectangle(0,0,context.getWindowWidth(),context.getWindowHeight(),0,0, Color.BLACK);

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
                        context.getPlayer().setFlySpeed(curr_fly_speed);
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
                        context.getPlayer().setJumpHeightMultiplier(curr_jump_height);
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
                        context.getPlayer().setRunMultiplier(curr_run_speed);
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
        cameraMenu.drawRectangle(0,0,context.getWindowWidth(),context.getWindowHeight(),0,0, Color.BLACK);

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
        Text defaultFovMult = cameraMenu.drawText(Integer.toString(curr_fov_default) + " deg", 550, 140, Color.WHITE, options);
        Rectangle defaultFovHitBox = cameraMenu.drawRectangle(50,120,600,30,0,0,Color.TRANSPARENT);
        defaultFovHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_fov_default +=5;
                        if(curr_fov_default > 100) curr_fov_default = 20;
                        defaultFovMult.setText(Integer.toString(curr_fov_default) + " deg");
                        context.getCamera().setFov_default(curr_fov_default);
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
                        context.getCamera().setFov_running_multiplier(curr_fov_running);
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
                        context.getCamera().setFov_tired_multiplier(curr_fov_tired);
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

    //*****************************************************************************************************
    // ABOUT MENU
    public void buildAboutMenu() {
        // draw black backdrop
        aboutMenu.drawRectangle(0,0,context.getWindowWidth(),context.getWindowHeight(),0,0, Color.BLACK);

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

    public void buildDeathMenu(){
        // draw black backdrop
        deathMenu.drawRectangle(0,0,context.getWindowWidth(),context.getWindowHeight(),0,0, Color.BLACK);

        //draw title
        deathMenu.drawText("ROOT@CS307:~/PLAYER_HAS_DIED$",
                50,
                50,
                GREEN,
                title);

        deathMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);

        // Start A New Game
        Text respawnArrow = deathMenu.drawText(singleArrow, 50, 140, GREEN, options);
        Text respawnGameText = deathMenu.drawText("./Respawn", 95, 140, Color.WHITE, options);
        Rectangle respawnGameHitBox = deathMenu.drawRectangle(50,120,600,30,0,0,Color.TRANSPARENT);
        respawnGameHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.showScene(context.getGameRootScene());
                    }
                });
        respawnGameHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        respawnArrow.setText(doubleArrow);
                        respawnGameText.setFill(GREEN);
                    }
                });
        respawnGameHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        respawnArrow.setText(singleArrow);
                        respawnGameText.setFill(Color.WHITE);
                    }
                });

        // Start A New Game
        Text startNewGameArrow = deathMenu.drawText(singleArrow, 50, 190, GREEN, options);
        Text startNewGameText = deathMenu.drawText("./Start_New_Game", 95, 190, Color.WHITE, options);
        Rectangle startNewGameHitBox = deathMenu.drawRectangle(50,170,600,30,0,0,Color.TRANSPARENT);
        startNewGameHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.getEnvironment().reset();
                        context.showScene(context.getGameRootScene());
                    }
                });
        startNewGameHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        startNewGameArrow.setText(doubleArrow);
                        startNewGameText.setFill(GREEN);
                    }
                });
        startNewGameHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        startNewGameArrow.setText(singleArrow);
                        startNewGameText.setFill(Color.WHITE);
                    }
                });

        // Start A New Game
        Text mainMenuArrow = deathMenu.drawText(singleArrow, 50, 240, GREEN, options);
        Text mainMenuGameText = deathMenu.drawText("./Exit_To_Main_Menu", 95, 240, Color.WHITE, options);
        Rectangle mainMenuGameHitBox = deathMenu.drawRectangle(50,220,600,30,0,0,Color.TRANSPARENT);
        mainMenuGameHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {activateGroup(GROUP_MAIN_MENU); }
                });
        mainMenuGameHitBox.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        mainMenuArrow.setText(doubleArrow);
                        mainMenuGameText.setFill(GREEN);
                    }
                });
        mainMenuGameHitBox.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        mainMenuArrow.setText(singleArrow);
                        mainMenuGameText.setFill(Color.WHITE);
                    }
                });
    }

}