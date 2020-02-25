package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.shape.*;



public class DrawMenu {


    public static MenuUtil mainMenu;
    public static MenuUtil highScoreMenu;

    DrawMenu(Stage primaryStage){
        highScoreMenu = new MenuUtil(primaryStage);
        mainMenu = new MenuUtil(primaryStage);
    }

    public void drawHighScoreMenu(Stage primaryStage, Scene game, boolean state) {
        if(highScoreMenu.isOnMenu){

            highScoreMenu.setMenu(primaryStage,0,0,500,600, true);
            primaryStage.setScene(highScoreMenu.scene);

            Button gotoMainMenu = new Button("High Scores");

            gotoMainMenu.setOnAction(e -> {
                mainMenu.setState(true);
                highScoreMenu.setState(false);
                primaryStage.setScene(mainMenu.scene);
                System.out.println("main menu");
                System.out.println("main menu state "+mainMenu.getState());
                System.out.println("high school menu state "+highScoreMenu.getState());
//                this.drawMainMenu(primaryStage,game,true);
                MainExecution.reset();
            });
            highScoreMenu.drawButton(gotoMainMenu,250,300);
        }
    }

    public void drawMainMenu(Stage primaryStage, Scene game, boolean state){
        if(mainMenu.isOnMenu) {
            // to program a menu first declare a new menu variable and assign it to a new MenuUtil and pass it
            // the primary stage
            // set up menu parameters
            // setMenu takes x, y, width, height, and state
            mainMenu.setMenu(primaryStage,0,0,500,600, true);
            primaryStage.setScene(mainMenu.scene);


            Button hs = new Button("High Scores");
            hs.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    mainMenu.setState(false);
                    highScoreMenu.setState(true);
                    primaryStage.setScene(highScoreMenu.scene);
                    System.out.println("High scores");
                    System.out.println("main menu state "+mainMenu.getState());
                    System.out.println("high school menu state "+highScoreMenu.getState());
//                    this.drawHighScoreMenu(primaryStage,game,true);
                    MainExecution.reset();
                }
            });
            mainMenu.drawButton(hs,250,300);

//            hs.setOnAction(new EventHandler<ActionEvent>() {
//                @Override public void handle(ActionEvent e) {
//                    label
//                }
//            });

            BackgroundFill[] fills = new BackgroundFill[]{
                    new BackgroundFill(Color.GREY, new CornerRadii(10), new Insets(0,0,200,200)),
            };
            Image image = new Image("https://images-na.ssl-images-amazon.com/images/I/81kTc0r6StL._SX425_.jpg",10,10,false,false);
            BackgroundImage[] images = new BackgroundImage[]{
//                    new BackgroundImage(
//                            image,
//                            BackgroundRepeat.NO_REPEAT,
//                            BackgroundRepeat.NO_REPEAT,
//                            BackgroundPosition.CENTER,
//                            new BackgroundSize(10,10, true,true,true,true))
            };
            mainMenu.drawBackground(fills,images);


            // to draw a square pass in the x1, y1, x2, y2
            mainMenu.drawLine(150,150, 200 ,200, Color.BLACK);

            // to draw a rectangle pass in the x, y, width, height, arc width, arc height, and color
            mainMenu.drawRectangle(50, 50, 100, 100, 0, 0, Color.BLUE);

            // to draw a polygon pass a double array
            double points[] = new double[]{
                    200.0, 50.0,
                    400.0, 50.0,
                    450.0, 150.0,
                    400.0, 250.0,
                    200.0, 250.0,
                    250.0, 150.0,
            };
            double points2[] = new double[]{
                    190.0, 50.0,
                    390.0, 50.0,
                    440.0, 150.0,
                    390.0, 250.0,
                    190.0, 250.0,
                    240.0, 150.0,
            };
            double points3[] = new double[]{
                    180.0, 45.0,
                    405.0, 45.0,
                    455.0, 150.0,
                    405.0, 255.0,
                    180.0, 255.0,
                    235.0, 150.0,
            };double points4[] = new double[]{
                    170.0, 40.0,
                    410.0, 40.0,
                    465.0, 150.0,
                    410.0, 260.0,
                    170.0, 260.0,
                    225.0, 150.0,
            };
            Polygon p1 = mainMenu.drawPolygon(points4, Color.BLACK);
            Polygon p2 = mainMenu.drawPolygon(points3, Color.WHITE);
            Polygon p3 = mainMenu.drawPolygon(points2, Color.DARKGREEN);
            Polygon p4 = mainMenu.drawPolygon(points, Color.GREEN);

            // example of using a shape as a button
            p4.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {
                            mainMenu.setState(false);
                            primaryStage.setScene(game);
                            MainExecution.reset();
                            System.out.println("start");
                        }
                    });



            // to add a button create a Button variable and set up the button
            Button playButton = new Button("play game");
            playButton.setOnAction(e -> {
                mainMenu.setState(false);
                primaryStage.setScene(game);
                MainExecution.reset();
            });

            // now just call draw button passing in the button and the x and y coordinates of the button
            mainMenu.drawButton(playButton,250,250);

            //to draw
            mainMenu.drawText("Welcome To Our Game!",250,200);




        }
    }
}
