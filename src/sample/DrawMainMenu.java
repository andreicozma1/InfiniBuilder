package sample;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.shape.*;




public class DrawMainMenu {
    public static MenuUtil menu;
    public void drawMainMenu(Stage primaryStage, Scene game,boolean state){

        if(state) {
            // to program a menu first declare a new menu variable and assign it to a new MenuUtil and pass it
            // the primary stage
            menu = new MenuUtil(primaryStage);
            // set up menu parameters
            // setMenu takes x, y, width, height, and state
            menu.setMenu(primaryStage,0,0,500,600, true);

            // to draw a square pass in the x1, y1, x2, y2
            menu.drawLine(150,150, 200 ,200, Color.BLACK);

            // to draw a rectangle pass in the x, y, width, height, arc width, arc height, and color
            menu.drawRectangle(50,50,100,100,0,0, Color.BLUE);

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
            Polygon p1 = menu.drawPolygon(points4, Color.BLACK);
            Polygon p2 = menu.drawPolygon(points3, Color.WHITE);
            Polygon p3 = menu.drawPolygon(points2, Color.DARKGREEN);
            Polygon p4 = menu.drawPolygon(points, Color.GREEN);

            // example of using a shape as a button
            p4.addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent me) {

                            System.out.println("start");
                        }
                    });



            // to add a button create a Button variable and set up the button
            Button playButton = new Button("play game");
            playButton.setOnAction(e -> {
                menu.setState(false);
                primaryStage.setScene(game);
                MainExecution.reset();
            });

            // now just call draw button passing in the button and the x and y coordinates of the button
            menu.drawButton(playButton,250,250);

            //to draw
            menu.drawText("Welcome To Our Game!",250,200);




            primaryStage.setScene(menu.scene);
        }
    }
}
