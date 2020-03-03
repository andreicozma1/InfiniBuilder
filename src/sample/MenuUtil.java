package sample;

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
import javafx.stage.Window;


public class MenuUtil {
    Scene menuScene;
    WindowUtil context;
    GroupBuilder mainMenu;
    GroupBuilder highScoreMenu;
    GroupBuilder optionsMenu;
    GroupBuilder aboutMenu;
    GroupBuilder exitButton;


    public static String GROUP_MAIN_MENU = "GROUP_MAIN_MENU";
    public static String GROUP_HIGH_SCORES = "GROUP_HIGH_SCORES";
    public static String GROUP_OPTIONS = "GROUP_OPTIONS";
    public static String GROUP_ABOUT = "GROUP_ABOUT";
    public static String GROUP_EXIT = "GROUP_EXIT";



    MenuUtil(WindowUtil ctx) {
        context = ctx;
        mainMenu = new GroupBuilder();
        highScoreMenu = new GroupBuilder();
        optionsMenu = new GroupBuilder();
        aboutMenu = new GroupBuilder();
        exitButton = new GroupBuilder();

        menuScene = new Scene(mainMenu.getGroup(),context.WIDTH,context.HEIGHT);
        buildMainMenu();
        buildHighScoreMenu();
        buildOptionsMenu();
        buildAboutMenu();


        context.addGroup(GROUP_MAIN_MENU, mainMenu.getGroup());
        context.addGroup(GROUP_HIGH_SCORES, highScoreMenu.getGroup());
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
                    public void handle(MouseEvent me) { context.show(context.SCENE_GAME); }
                });
        Text gameText = mainMenu.drawText("Start Game", 20, 230, Color.DARKBLUE, Font.font("vedana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        gameText.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) { context.show(context.SCENE_GAME); }
                });
        gotoGame.setCursor(Cursor.HAND);
        gameText.setCursor(Cursor.HAND);

        // go to high score button
        Rectangle gotoHighScoreMenu = mainMenu.drawRectangle(-20, 260, 200, 50, 20, 20, Color.LIGHTBLUE);
        gotoHighScoreMenu.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.activateGroup(GROUP_HIGH_SCORES);
                    }
                });
        Text highScoreText = mainMenu.drawText("High Score", 20, 290, Color.DARKBLUE, Font.font("vedana", FontWeight.BOLD, FontPosture.REGULAR, 20));
        highScoreText.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent me) {
                        context.activateGroup(GROUP_HIGH_SCORES);
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


        // add it to the main menu pain

        // create button to add to GROUP

        // add it to the main menu pain

    }

    public void buildHighScoreMenu() {
        // create label
//        highScoreMenu.drawText("High Scores",400,250,Color.DARKBLUE);

        // create button to add to GROUP
        Button gotoMainMenu = new Button("Exit to Main Menu");
        gotoMainMenu.setOnAction(e -> {
            context.activateGroup(GROUP_MAIN_MENU);
        });
        // add it to the high score menu
        highScoreMenu.drawButton(gotoMainMenu, 400, 300);
    }

    public void buildOptionsMenu() {
        // create label
//        highScoreMenu.drawText("High Scores",400,250,Color.DARKBLUE);

        // create button to add to GROUP
        Button gotoMainMenu = new Button("Exit to Main Menu");
        gotoMainMenu.setOnAction(e -> {
            context.activateGroup(GROUP_MAIN_MENU);
        });
        // add it to the high score menu
        optionsMenu.drawButton(gotoMainMenu, 100, 100);
    }

    public void buildAboutMenu() {
        Button gotoMainMenu = new Button("Exit to Main Menu");
        gotoMainMenu.setOnAction(e -> {
            context.activateGroup(GROUP_MAIN_MENU);
        });
        // add it to the high score menu
        aboutMenu.drawButton(gotoMainMenu, 100, 100);
    }
}