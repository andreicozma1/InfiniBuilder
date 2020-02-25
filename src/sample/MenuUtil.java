package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class MenuUtil {
    float menu_w = 800;
    float menu_h = 600;
    float menu_x = 0;
    float menu_y = 0;
    public static Scene scene;
    public static boolean isOnMenu;
    public static Stage primaryStage;
    public static Pane menuLayout;

    MenuUtil(Stage primaryStage) {

        this.primaryStage = primaryStage;
        isOnMenu = false;
        menuLayout = new Pane();

        scene =  new Scene(menuLayout,menu_w,menu_h);

    }
    public void setMenu(Stage primaryStage, float x, float y, float w, float h , boolean state){
        this.primaryStage = primaryStage;
        menu_x = x;
        menu_x = y;
        menu_w = w;
        menu_h = h;
        isOnMenu = state;
    }
    public void setMenuX(float x){ menu_x = x; }
    public float getMenuX(){ return menu_x; }
    public void setMenuY(float y){ menu_x = y; }
    public float getMenuY(){ return menu_y; }
    public void setMenuWidth(int width){menu_w = width;}
    public float getMenuWidth(){ return menu_w; }
    public void setMenuHeight(int menuHeight){menu_h = menuHeight;}
    public float getMenuHeight(){ return menu_h; }
    public void setPrimaryStage(Stage primaryStage){ this.primaryStage = primaryStage; }
    public Stage getPrimaryStage(){ return primaryStage; }
    public void setState(boolean state){
        isOnMenu = state;
        if(state = true) primaryStage.setScene(scene);
    }
    public boolean getState(){ return isOnMenu; }
    public Pane getPane(){ return menuLayout; }
    public void getPane(Pane pane){ menuLayout = pane; }

    public Line drawLine(float x1, float y1, float x2, float y2,Paint paint){
        Line line = new Line(x1,y1,x2,y2);
        line.setFill(paint);
        menuLayout.getChildren().add(line);
        return line;
    }

    public Rectangle drawRectangle(float x, float y, float w, float h,float arcW,float arcH, Paint paint){
        Rectangle rectangle = new Rectangle(w, h, paint);
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setArcWidth(arcW);
        rectangle.setArcHeight(arcH);
        menuLayout.getChildren().add(rectangle);
        return rectangle;
    }

    public Circle drawCircle(float centerX, float centerY, float radius, Paint paint){
        Circle circle = new Circle(centerX, centerY, radius, paint);
        menuLayout.getChildren().add(circle);
        return circle;
    }

    public Ellipse drawEllipse(float centerX, float centerY, float radiusX, float radiusY, Paint paint){
        Ellipse ellipse = new Ellipse(centerX, centerY, radiusX, radiusY);
        ellipse.setFill(paint);
        menuLayout.getChildren().add(ellipse);
        return ellipse;
    }

    public Polygon drawPolygon(double[] points, Paint paint){
        Polygon polygon = new Polygon(points);
        polygon.setFill(paint);
        menuLayout.getChildren().add(polygon);
        return polygon;
    }

    public CubicCurve drawCubicCurve (float startX, float startY, float controlX1,  float controlY1, float controlX2, float controlY2, float endX, float endY, Paint paint){
        CubicCurve cubicCurve = new CubicCurve(startX, startY, controlX1, controlY1, controlX2, controlY2, endX, endY);
        cubicCurve.setFill(paint);
        menuLayout.getChildren().add(cubicCurve);
        return cubicCurve;
    }

    public QuadCurve drawQuadCurve (float startX, float startY, float controlX,  float controlY, float endX, float endY, Paint paint){
        QuadCurve quadCurve = new QuadCurve(startX, startY, controlX, controlY, endX, endY);
        quadCurve.setFill(paint);
        menuLayout.getChildren().add(quadCurve);
        return quadCurve;

    }

    public Arc drawArc(float centerX, float centerY,float radiusX, float radiusY, float startAngle, float length, Paint paint){
        Arc arc = new Arc(centerX, centerY,radiusX,radiusY,startAngle,length);
        arc.setFill(paint);
        menuLayout.getChildren().add(arc);
        return arc;
    }

    public Button drawButton(Button button, float x, float y){
        button.setLayoutX(x);
        button.setLayoutY(y);
        menuLayout.getChildren().add(button);
        return button;
    }

    public Label drawText(String label, float x, float y){
        Label message = new Label(label);
        message.setLayoutX(x);
        message.setLayoutY(y);
        menuLayout.getChildren().add(message);
        return message;
    }

    public Shape createShape(Shape shapes[]){
        if( shapes.length < 2) return null;

        Shape combinedShape = Shape.union(shapes[0],shapes[1]);
        for( int i = 2 ; i < shapes.length ; i++ ){
            combinedShape = Shape.union(shapes[i], combinedShape);
        }
        return combinedShape;
    }


}
