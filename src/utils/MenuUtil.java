package utils;

import environment.MaterialsUtil;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

import java.util.Collection;


public class MenuUtil {
    Scene menuScene;
    WindowUtil context;
    GroupBuilder mainMenu;
    GroupBuilder controlsMenu;
    GroupBuilder optionsMenu;
    GroupBuilder aboutMenu;
    GroupBuilder exitButton;

    private Color BACKDROP = Color.BLACK;
    private Color UT_ORANGE = Color.valueOf("#f77f00");
    private Color TEXT_BOX = Color.valueOf("#7c7c7c");

    public static String GROUP_MAIN_MENU = "GROUP_MAIN_MENU";
    public static String GROUP_CONTROLS = "GROUP_HIGH_SCORES";
    public static String GROUP_OPTIONS = "GROUP_OPTIONS";
    public static String GROUP_ABOUT = "GROUP_ABOUT";
    public static String GROUP_EXIT = "GROUP_EXIT";



    MenuUtil(WindowUtil ctx) {
        context = ctx;
        mainMenu = new GroupBuilder();
        controlsMenu = new GroupBuilder();
        optionsMenu = new GroupBuilder();
        aboutMenu = new GroupBuilder();
        exitButton = new GroupBuilder();

        menuScene = new Scene(mainMenu.getGroup(),context.WIDTH,context.HEIGHT);
        buildMainMenu();
        buildControlsMenu();
        buildOptionsMenu();
        buildAboutMenu();


        context.addGroup(GROUP_MAIN_MENU, mainMenu.getGroup());
        context.addGroup(GROUP_CONTROLS, controlsMenu.getGroup());
        context.addGroup(GROUP_OPTIONS, optionsMenu.getGroup());
        context.addGroup(GROUP_ABOUT, aboutMenu.getGroup());
        context.addGroup(GROUP_EXIT, exitButton.getGroup());

        context.SCENE_MENU = menuScene;
    }



    public void buildMainMenu() {

        // create a new box
        Box b = new Box();
        Rotate rxBox = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate ryBox = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        Rotate rzBox = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
        rxBox.setAngle(30);
        ryBox.setAngle(50);
        rzBox.setAngle(30);
        b.getTransforms().addAll(rxBox, ryBox, rzBox);
        b.setMaterial(MaterialsUtil.grass);
        b.setDepth(50);
        b.setHeight(50);
        b.setWidth(50);
        mainMenu.drawBox(b, 350, 250);

        mainMenu.drawRectangle(-20,180,250,330,20,20,Color.DARKBLUE);

        // go to game button
        Rectangle gotoGame = mainMenu.drawRectangle(-20, 200, 200, 50, 20, 20, Color.LIGHTBLUE);
        gotoGame.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { context.showScene(context.SCENE_GAME); }
                });
        Text gameText = mainMenu.drawText("Start Game", 20, 230, Color.DARKBLUE, Font.font("vedana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        gameText.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { context.showScene(context.SCENE_GAME); }
                });
        gotoGame.setCursor(Cursor.HAND);
        gameText.setCursor(Cursor.HAND);

        // go to high score button
        Rectangle gotoHighScoreMenu = mainMenu.drawRectangle(-20, 260, 200, 50, 20, 20, Color.LIGHTBLUE);
        gotoHighScoreMenu.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.activateGroup(GROUP_CONTROLS);
                    }
                });
        Text highScoreText = mainMenu.drawText("Controls", 20, 290, Color.DARKBLUE, Font.font("vedana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        highScoreText.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.activateGroup(GROUP_CONTROLS);
                    }
                });
        gotoHighScoreMenu.setCursor(Cursor.HAND);
        highScoreText.setCursor(Cursor.HAND);

        // go to settings button
        Rectangle gotoOptionsMenu = mainMenu.drawRectangle(-20, 320, 200, 50, 20, 20, Color.LIGHTBLUE);
        gotoOptionsMenu.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.activateGroup(GROUP_OPTIONS);
                    }
                });
        Text optionsText = mainMenu.drawText("Options", 20, 350, Color.DARKBLUE, Font.font("vedana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        optionsText.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.activateGroup(GROUP_OPTIONS);
                    }
                });
        gotoOptionsMenu.setCursor(Cursor.HAND);
        optionsText.setCursor(Cursor.HAND);

        // go to settings button
        Rectangle gotoAboutMenu = mainMenu.drawRectangle(-20, 380, 200, 50, 20, 20, Color.LIGHTBLUE);
        gotoAboutMenu.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.activateGroup(GROUP_ABOUT);
                    }
                });
        Text aboutText = mainMenu.drawText("About", 20, 410, Color.DARKBLUE, Font.font("vedana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        aboutText.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.activateGroup(GROUP_ABOUT);
                    }
                });
        gotoAboutMenu.setCursor(Cursor.HAND);
        aboutText.setCursor(Cursor.HAND);

        Rectangle gotoExitButton = mainMenu.drawRectangle(-20, 440, 200, 50, 20, 20, Color.LIGHTBLUE);
        gotoExitButton.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.getStage().close();
                    }
                });
        Text exitText = mainMenu.drawText("Exit", 20, 470, Color.DARKBLUE, Font.font("vedana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        exitText.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.getStage().close();
                    }
                });
        gotoExitButton.setCursor(Cursor.HAND);
        exitText.setCursor(Cursor.HAND);

    }

    public void buildControlsMenu() {
        // draw basic menu
        drawBasicMenuLayout(controlsMenu);

        // draw title
        controlsMenu.drawText("Controls", 325, 60, BACKDROP, Font.font("vedana", FontWeight.BOLD , FontPosture.REGULAR, 30));

        // draw information
        controlsMenu.drawText("WASD to move",100,150,BACKDROP, Font.font("vedana", FontWeight.NORMAL, FontPosture.REGULAR, 15));


    }

    public void buildOptionsMenu() {
        drawBasicMenuLayout(optionsMenu);
        optionsMenu.drawText("Options", 330, 60, BACKDROP, Font.font("vedana", FontWeight.BOLD , FontPosture.REGULAR, 30));
    }

    public void buildAboutMenu() {
        drawBasicMenuLayout(aboutMenu);
        aboutMenu.drawText("About", 345, 60, BACKDROP, Font.font("vedana", FontWeight.BOLD , FontPosture.REGULAR, 30));
    }

    private void drawBasicMenuLayout(GroupBuilder group){
        // draw menu back drop
        group.drawRectangle(0,0,context.WIDTH,context.HEIGHT,0,0,BACKDROP);
        group.drawRectangle(0,0,context.WIDTH,context.HEIGHT,180,180, UT_ORANGE);
        group.drawRectangle(148,18,context.WIDTH-296,64,20,20,BACKDROP);
        group.drawRectangle(150,20,context.WIDTH-300,60,20,20,TEXT_BOX);
        group.drawRectangle(28,108,context.WIDTH-56,context.HEIGHT-136,180,180,BACKDROP);
        group.drawRectangle(30,110,context.WIDTH-60,context.HEIGHT-140,180,180,TEXT_BOX);

        // draw exit to Main Menu button
        group.drawRectangle(298, 513, context.WIDTH-596, 39, 20, 20,BACKDROP);
        Rectangle gotoExitButton = group.drawRectangle(300, 515, context.WIDTH-600, 35, 20, 20, Color.LIGHTGRAY);
        gotoExitButton.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.activateGroup(GROUP_MAIN_MENU);
                    }
                });
        Text exitText = group.drawText("Exit To Main Menu", 318, 538, BACKDROP, Font.font("vedana", FontWeight.NORMAL , FontPosture.REGULAR, 18));
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