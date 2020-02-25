package sample;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class PaneBuilder {
    public Pane pane;

    PaneBuilder() {
        pane = new Pane();
    }

    public Pane getPane(){ return pane; }
    public void setPane(Pane pane){ this.pane = pane; }

    public Line drawLine(float x1, float y1, float x2, float y2,Paint paint){
        Line line = new Line(x1,y1,x2,y2);
        line.setFill(paint);
        pane.getChildren().add(line);
        return line;
    }

    public Rectangle drawRectangle(float x, float y, float w, float h,float arcW,float arcH, Paint paint){
        Rectangle rectangle = new Rectangle(w, h, paint);
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setArcWidth(arcW);
        rectangle.setArcHeight(arcH);
        pane.getChildren().add(rectangle);
        return rectangle;
    }

    public Circle drawCircle(float centerX, float centerY, float radius, Paint paint){
        Circle circle = new Circle(centerX, centerY, radius, paint);
        pane.getChildren().add(circle);
        return circle;
    }

    public Ellipse drawEllipse(float centerX, float centerY, float radiusX, float radiusY, Paint paint){
        Ellipse ellipse = new Ellipse(centerX, centerY, radiusX, radiusY);
        ellipse.setFill(paint);
        pane.getChildren().add(ellipse);
        return ellipse;
    }

    public Polygon drawPolygon(double[] points, Paint paint){
        Polygon polygon = new Polygon(points);
        polygon.setFill(paint);
        pane.getChildren().add(polygon);
        return polygon;
    }

    public CubicCurve drawCubicCurve (float startX, float startY, float controlX1,  float controlY1, float controlX2, float controlY2, float endX, float endY, Paint paint){
        CubicCurve cubicCurve = new CubicCurve(startX, startY, controlX1, controlY1, controlX2, controlY2, endX, endY);
        cubicCurve.setFill(paint);
        pane.getChildren().add(cubicCurve);
        return cubicCurve;
    }

    public QuadCurve drawQuadCurve (float startX, float startY, float controlX,  float controlY, float endX, float endY, Paint paint){
        QuadCurve quadCurve = new QuadCurve(startX, startY, controlX, controlY, endX, endY);
        quadCurve.setFill(paint);
        pane.getChildren().add(quadCurve);
        return quadCurve;

    }

    public Arc drawArc(float centerX, float centerY,float radiusX, float radiusY, float startAngle, float length, Paint paint){
        Arc arc = new Arc(centerX, centerY,radiusX,radiusY,startAngle,length);
        arc.setFill(paint);
        pane.getChildren().add(arc);
        return arc;
    }

    public Button drawButton(Button button, float x, float y){
        button.setLayoutX(x);
        button.setLayoutY(y);
        pane.getChildren().add(button);
        return button;
    }

    public Box drawBox(Box box, float x, float y){
        box.setTranslateY(50);
        box.setTranslateX(50);
        pane.getChildren().add(box);
        return box;
    }

    public Label drawText(String label, float x, float y){
        Label message = new Label(label);
        message.setLayoutX(x);
        message.setLayoutY(y);
        pane.getChildren().add(message);
        return message;
    }

    public Background drawBackground(BackgroundFill[] fills, BackgroundImage[] images){
        Background background = new Background(fills, images);
        pane.setBackground(background);
        return background;
    }


}
