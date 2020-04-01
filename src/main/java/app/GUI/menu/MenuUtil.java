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
    Scene SCENE_MENU;
    GameBuilder context;
    InterfaceBuilder mainMenu;
    InterfaceBuilder settingsMenu;
    InterfaceBuilder controlsMenu;
    InterfaceBuilder aboutMenu;
    InterfaceBuilder exitButton;

    private String singleArrow = ">";
    private String doubleArrow = ">>";

    private Font title = Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,30);
    private Font options = Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,25);

    private Color GREEN = Color.valueOf("#20C20E");


    // sky box settings
    private double curr_sun_scale ;
    private double curr_moon_scale ;
    private double curr_big_star_scale;

    // world generation settings
    private int curr_world_type = 0;
    private double curr_world_height_mult;
    private double curr_vegetation_mult;

    // player settings
    private double curr_fly_speed;
    private double curr_jump_height;

    public static String GROUP_MAIN_MENU = "GROUP_MAIN_MENU";
    public static String GROUP_CONTROLS = "GROUP_CONTROLS";
    public static String GROUP_SETTINGS = "GROUP_SETTINGS";
    public static String GROUP_ABOUT = "GROUP_ABOUT";
    public static String GROUP_EXIT = "GROUP_EXIT";

    String currentGroup;
    public HashMap<String, Group> menuGroupMap = new HashMap<>();

    public MenuUtil(GameBuilder ctx) {
        context = ctx;
        mainMenu = new InterfaceBuilder();
        settingsMenu = new InterfaceBuilder();
        controlsMenu = new InterfaceBuilder();
        aboutMenu = new InterfaceBuilder();
        exitButton = new InterfaceBuilder();

        SCENE_MENU = new Scene(mainMenu.getGroup(),context.getWindowWidth(),context.getWindowHeight());


        curr_world_height_mult = context.getEnvironment().getTerrainHeightMultiplier();
        curr_vegetation_mult = context.getEnvironment().getVegetationDensity();

        curr_sun_scale = context.getEnvironment().getSkybox().getSunScale();
        curr_moon_scale = context.getEnvironment().getSkybox().getMoonScale();
        curr_big_star_scale = context.getEnvironment().getSkybox().getBigStarScale();

        curr_fly_speed = context.getPlayer().getFlySpeed();
        curr_jump_height = context.getPlayer().getJumpHeight();

        buildMainMenu();
        buildControlsMenu();
        buildSettingsMenu();
        buildAboutMenu();

        addGroup(GROUP_MAIN_MENU, mainMenu.getGroup());
        addGroup(GROUP_SETTINGS, settingsMenu.getGroup());
        addGroup(GROUP_CONTROLS, controlsMenu.getGroup());
        addGroup(GROUP_ABOUT, aboutMenu.getGroup());
        addGroup(GROUP_EXIT, exitButton.getGroup());




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
        Rectangle startHitBox = mainMenu.drawRectangle(50,120,225,30,0,0,Color.TRANSPARENT);
        startHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { context.showScene(context.getGameRootScene()); }
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
        Rectangle settingsHitBox = mainMenu.drawRectangle(50,170,225,30,0,0,Color.TRANSPARENT);
        settingsHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { activateGroup(GROUP_SETTINGS); }
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
        Rectangle controlsHitBox = mainMenu.drawRectangle(50,220,225,30,0,0,Color.TRANSPARENT);
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
        Rectangle aboutHitBox = mainMenu.drawRectangle(50,270,225,30,0,0,Color.TRANSPARENT);
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
        Rectangle quitHitBox = mainMenu.drawRectangle(50,320,225,30,0,0,Color.TRANSPARENT);
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
        controlsMenu.drawText("ROOT@CS307:~$ ./Controls",
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
        Rectangle returnHitBox = controlsMenu.drawRectangle(50,320,225,30,0,0,Color.TRANSPARENT);
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

    public void buildSettingsMenu() {
        // draw black backdrop
        settingsMenu.drawRectangle(0,0,context.getWindowWidth(),context.getWindowHeight(),0,0, Color.BLACK);

        //draw title
        settingsMenu.drawText("Settings@CS307:~$",
                50,
                50,
                GREEN,
                title);

        settingsMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);

        // World Height Multiplier

        Text worldHeightArrow = settingsMenu.drawText(singleArrow, 50, 140, GREEN, options);
        Text worldHeightText= settingsMenu.drawText("./World_Height_Multiplier", 95, 140, Color.WHITE, options);
        Text worldHeightMult = settingsMenu.drawText(Integer.toString((int)curr_world_height_mult), 550, 140, Color.WHITE, options);
        Rectangle worldHeightHitBox = settingsMenu.drawRectangle(50,120,600,30,0,0,Color.TRANSPARENT);
        worldHeightHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_world_height_mult +=10;
                        if(curr_world_height_mult >100) curr_world_height_mult =0;
                        worldHeightMult.setText(Integer.toString((int)curr_world_height_mult));
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

        Text vegetationArrow = settingsMenu.drawText(singleArrow, 50, 190, GREEN, options);
        Text vegetationText= settingsMenu.drawText("./Vegetation_Multiplier", 95, 190, Color.WHITE, options);
        Text vegetationMult = settingsMenu.drawText(Integer.toString((int) curr_vegetation_mult), 550, 190, Color.WHITE, options);
        Rectangle vegetationHitBox = settingsMenu.drawRectangle(50,170,600,30,0,0,Color.TRANSPARENT);
        vegetationHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        curr_vegetation_mult+=10;
                        if(curr_vegetation_mult>100)curr_vegetation_mult=0;
                        vegetationMult.setText(Integer.toString((int) curr_vegetation_mult));
                        context.getEnvironment().setVegetationDensity(curr_vegetation_mult);
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

        Text worldTypeArrow = settingsMenu.drawText(singleArrow, 50, 240, GREEN, options);
        Text worldTypeText = settingsMenu.drawText("./World_Type", 95, 240, Color.WHITE, options);
        Text worldTypeChoice = settingsMenu.drawText(ResourcesUtil.world_types_sorted.get(curr_world_type), 550, 240, Color.WHITE, options);
        Rectangle worldTypeHitBox = settingsMenu.drawRectangle(50,220,600,30,0,0,Color.TRANSPARENT);
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


        Text sunArrow = settingsMenu.drawText(singleArrow, 50, 290, GREEN, options);
        Text sunText= settingsMenu.drawText("./Sun_Scale", 95, 290, Color.WHITE, options);
        Text sunMult = settingsMenu.drawText(Integer.toString((int)curr_sun_scale), 550, 290, Color.WHITE, options);
        Rectangle sunHitBox = settingsMenu.drawRectangle(50,270,600,30,0,0,Color.TRANSPARENT);
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

        Text moonArrow = settingsMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text moonText= settingsMenu.drawText("./Moon_Scale", 95, 340, Color.WHITE, options);
        Text moonMult = settingsMenu.drawText(Integer.toString((int)curr_moon_scale), 550, 340, Color.WHITE, options);
        Rectangle moonHitBox = settingsMenu.drawRectangle(50,320,600,30,0,0,Color.TRANSPARENT);
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

        Text bigStarArrow = settingsMenu.drawText(singleArrow, 50, 390, GREEN, options);
        Text bigStarText= settingsMenu.drawText("./Big_Star_Scale", 95, 390, Color.WHITE, options);
        Text bigStarMult = settingsMenu.drawText(Integer.toString((int)curr_big_star_scale), 550, 390, Color.WHITE, options);
        Rectangle bigStarHitBox = settingsMenu.drawRectangle(50,370,600,30,0,0,Color.TRANSPARENT);
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





        //quit handler
        Text returnArrow = settingsMenu.drawText(singleArrow, 50, 550, GREEN, options);
        Text returnText = settingsMenu.drawText("./Main_Menu", 95, 550, Color.WHITE, options);
        Rectangle returnHitBox = settingsMenu.drawRectangle(50,530,600,30,0,0,Color.TRANSPARENT);
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
        Rectangle quitHitBox = aboutMenu.drawRectangle(50,320,225,30,0,0,Color.TRANSPARENT);
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

    private void drawBasicMenuLayout(InterfaceBuilder group){
    }
}