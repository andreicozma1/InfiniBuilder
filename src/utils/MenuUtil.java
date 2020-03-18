package utils;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class MenuUtil {
    Scene menuScene;
    WindowUtil context;
    GroupBuilder mainMenu;
    GroupBuilder settingsMenu;
    GroupBuilder controlsMenu;
    GroupBuilder aboutMenu;
    GroupBuilder exitButton;

    private Color GREEN = Color.valueOf("#20C20E");

    //    harvard colors
    private Color BACKDROP = Color.valueOf("#1a1831");
    //    private Color BACKDROP = Color.DARKGRAY;
    private Color BORDER_COLOR = Color.valueOf("#a21232");
    private Color MAIN_COLOR = Color.valueOf("#1a1831");
    private Color TEXT_BOX = Color.valueOf("#f6faf7");

    //blues w red border
//    private Color BACKDROP = Color.valueOf("#1a1831");
//    private Color BORDER_COLOR = Color.valueOf("#a21232");
//    private Color MAIN_COLOR = Color.valueOf("#1a1831");
//    private Color TEXT_BOX = Color.valueOf("#f6faf7");


    //greens
//    private Color BACKDROP = Color.valueOf("#b4bb72");
//    private Color BORDER_COLOR = Color.valueOf("#303e27");
//    private Color MAIN_COLOR = Color.valueOf("#303e27");
//    private Color TEXT_BOX = Color.valueOf("#f6faf7");

    //blues w light blue border
//    private Color BACKDROP = Color.valueOf("#222831");
//    private Color BORDER_COLOR = Color.valueOf("#00adb5");
//    private Color MAIN_COLOR = Color.valueOf("#393e46");
//    private Color TEXT_BOX = Color.valueOf("#eeeeee");

    public static String GROUP_MAIN_MENU = "GROUP_MAIN_MENU";
    public static String GROUP_CONTROLS = "GROUP_CONTROLS";
    public static String GROUP_SETTINGS = "GROUP_SETTINGS";
    public static String GROUP_ABOUT = "GROUP_ABOUT";
    public static String GROUP_EXIT = "GROUP_EXIT";



    MenuUtil(WindowUtil ctx) {
        context = ctx;
        mainMenu = new GroupBuilder();
        settingsMenu = new GroupBuilder();
        controlsMenu = new GroupBuilder();
        aboutMenu = new GroupBuilder();
        exitButton = new GroupBuilder();

        menuScene = new Scene(mainMenu.getGroup(),context.WIDTH,context.HEIGHT);
        buildMainMenu();
        buildControlsMenu();
        buildSettingsMenu();
        buildAboutMenu();


        context.addGroup(GROUP_MAIN_MENU, mainMenu.getGroup());
        context.addGroup(GROUP_SETTINGS, settingsMenu.getGroup());
        context.addGroup(GROUP_CONTROLS, controlsMenu.getGroup());
        context.addGroup(GROUP_ABOUT, aboutMenu.getGroup());
        context.addGroup(GROUP_EXIT, exitButton.getGroup());

        context.SCENE_MENU = menuScene;
    }



    public void buildMainMenu() {
        // set fonts
        int fontNum = 2;

        String singleArrow = ">";
        String doubleArrow = ">>";

        Font title = Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,30);
        Font options = Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,25);
        Font boldedOptions = Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,25);

        // draw black backdrop
        mainMenu.drawRectangle(0,0,context.WIDTH,context.HEIGHT,0,0, Color.BLACK);

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
                    public void handle(MouseEvent me) { context.showScene(context.SCENE_GAME); }
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
                    public void handle(MouseEvent me) { context.activateGroup(GROUP_SETTINGS); }
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
                    public void handle(MouseEvent me) { context.activateGroup(GROUP_CONTROLS); }
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
                    public void handle(MouseEvent me) { context.activateGroup(GROUP_ABOUT); }
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
                    public void handle(MouseEvent me) { context.getStage().close(); }
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
        // draw basic menu
        drawBasicMenuLayout(controlsMenu);

        // draw title
        controlsMenu.drawText("Controls", 32, 65, BORDER_COLOR, Font.font("vedana", FontWeight.EXTRA_BOLD , FontPosture.REGULAR, 55));

        // draw information
        controlsMenu.drawCircle(85,150,5,MAIN_COLOR);
        controlsMenu.drawText("WASD to move",100,155,MAIN_COLOR, Font.font("vedana", FontWeight.NORMAL, FontPosture.REGULAR, 15));


    }

    public void buildSettingsMenu() {
        drawBasicMenuLayout(settingsMenu);
        settingsMenu.drawText("Settings", 330, 60, BACKDROP, Font.font("vedana", FontWeight.BOLD , FontPosture.REGULAR, 30));
    }

    public void buildAboutMenu() {
        drawBasicMenuLayout(aboutMenu);
        aboutMenu.drawText("About", 345, 60, BACKDROP, Font.font("vedana", FontWeight.BOLD , FontPosture.REGULAR, 30));
    }

    private void drawBasicMenuLayout(GroupBuilder group){
        // draw menu back drop
        group.drawRectangle(0,0,context.WIDTH,context.HEIGHT,0,0, BACKDROP);
//        group.drawRectangle(148,18,context.WIDTH-296,64,0,0,BORDER_COLOR);
//        group.drawRectangle(152,22,context.WIDTH-304,56,0,20,TEXT_BOX);
        group.drawRectangle(28,108,context.WIDTH-56,context.HEIGHT-136,0,0,BORDER_COLOR);
        group.drawRectangle(32,112,context.WIDTH-64,context.HEIGHT-144,0,0,TEXT_BOX);

        // draw exit to Main Menu button
        group.drawRectangle(298, 513, context.WIDTH-596, 39, 0, 0,BORDER_COLOR);
        Rectangle gotoExitButton = group.drawRectangle(300, 515, context.WIDTH-600, 35, 0, 0, Color.LIGHTGRAY);
        gotoExitButton.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.activateGroup(GROUP_MAIN_MENU);
                    }
                });
        Text exitText = group.drawText("> MAIN MENU", 318, 538, MAIN_COLOR, Font.font("vedana", FontWeight.NORMAL , FontPosture.REGULAR, 18));
        exitText.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.activateGroup(GROUP_MAIN_MENU);
                    }
                });
        gotoExitButton.setCursor(Cursor.HAND);
        exitText.setCursor(Cursor.HAND);
    }
}