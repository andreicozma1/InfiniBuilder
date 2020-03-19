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

    private String singleArrow = ">";
    private String doubleArrow = ">>";

    private Font title = Font.font("Monospaced",FontWeight.BOLD,FontPosture.REGULAR,30);
    private Font options = Font.font("Monospaced",FontWeight.NORMAL,FontPosture.REGULAR,25);

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
        // draw black backdrop
        controlsMenu.drawRectangle(0,0,context.WIDTH,context.HEIGHT,0,0, Color.BLACK);

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
                    public void handle(MouseEvent me) { context.activateGroup(GROUP_MAIN_MENU); }
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
        settingsMenu.drawRectangle(0,0,context.WIDTH,context.HEIGHT,0,0, Color.BLACK);

        //draw title
        settingsMenu.drawText("ROOT@CS307:~$ ./Settings",
                50,
                50,
                GREEN,
                title);

        settingsMenu.drawText("-------------",
                50,
                85,
                Color.WHITE,
                title);

        //quit handler
        Text returnArrow = settingsMenu.drawText(singleArrow, 50, 340, GREEN, options);
        Text returnText = settingsMenu.drawText("./Main_Menu", 95, 340, Color.WHITE, options);
        Rectangle returnHitBox = settingsMenu.drawRectangle(50,320,225,30,0,0,Color.TRANSPARENT);
        returnHitBox.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { context.activateGroup(GROUP_MAIN_MENU); }
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
        aboutMenu.drawRectangle(0,0,context.WIDTH,context.HEIGHT,0,0, Color.BLACK);

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
                    public void handle(MouseEvent me) { context.activateGroup(GROUP_MAIN_MENU); }
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

    private void drawBasicMenuLayout(GroupBuilder group){
    }
}