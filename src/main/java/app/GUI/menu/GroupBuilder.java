package app.GUI.menu;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GroupBuilder extends Parent {
    public Group group;

    public GroupBuilder() {
        group = new Group();
    }

    public Group getGroup(){ return group; }
    public void setGroup(Group Group){ this.group = group; }

    public Line drawLine(float x1, float y1, float x2, float y2,Paint paint){
        Line line = new Line(x1,y1,x2,y2);
        line.setFill(paint);
        group.getChildren().add(line);
        return line;
    }

    public Rectangle drawRectangle(float x, float y, double w, double h, float arcW, float arcH, Paint paint){
        Rectangle rectangle = new Rectangle(w, h, paint);
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setArcWidth(arcW);
        rectangle.setArcHeight(arcH);
        group.getChildren().add(rectangle);
        return rectangle;
    }

    public Circle drawCircle(float centerX, float centerY, float radius, Paint paint){
        Circle circle = new Circle(centerX, centerY, radius, paint);
        group.getChildren().add(circle);
        return circle;
    }

    public Ellipse drawEllipse(float centerX, float centerY, float radiusX, float radiusY, Paint paint){
        Ellipse ellipse = new Ellipse(centerX, centerY, radiusX, radiusY);
        ellipse.setFill(paint);
        group.getChildren().add(ellipse);
        return ellipse;
    }

    public Polygon drawPolygon(double[] points, Paint paint){
        Polygon polygon = new Polygon(points);
        polygon.setFill(paint);
        group.getChildren().add(polygon);
        return polygon;
    }

    public CubicCurve drawCubicCurve (float startX, float startY, float controlX1,  float controlY1, float controlX2, float controlY2, float endX, float endY, Paint paint){
        CubicCurve cubicCurve = new CubicCurve(startX, startY, controlX1, controlY1, controlX2, controlY2, endX, endY);
        cubicCurve.setFill(paint);
        group.getChildren().add(cubicCurve);
        return cubicCurve;
    }

    public QuadCurve drawQuadCurve (float startX, float startY, float controlX,  float controlY, float endX, float endY, Paint paint){
        QuadCurve quadCurve = new QuadCurve(startX, startY, controlX, controlY, endX, endY);
        quadCurve.setFill(paint);
        group.getChildren().add(quadCurve);
        return quadCurve;

    }

    public Arc drawArc(float centerX, float centerY,float radiusX, float radiusY, float startAngle, float length, Paint paint){
        Arc arc = new Arc(centerX, centerY,radiusX,radiusY,startAngle,length);
        arc.setFill(paint);
        group.getChildren().add(arc);
        return arc;
    }

    public Button drawButton(Button button, float x, float y){
        button.setLayoutX(x);
        button.setLayoutY(y);
        group.getChildren().add(button);
        return button;
    }

    public Box drawBox(Box box, float x, float y){
        box.setTranslateY(50);
        box.setTranslateX(50);
        group.getChildren().add(box);
        return box;
    }

    public Label drawLabel(String label, float x, float y, Color color){
        Label message = new Label(label);
        message.setLayoutX(x);
        message.setLayoutY(y);
        message.setTextFill(color);
        group.getChildren().add(message);
        return message;
    }

    public Text drawText(String text, float x, float y, Color color, Font font){
        Text message = new Text();

        message.setText(text);
        message.setX(x);
        message.setY(y);
        message.setFill(color);
        message.setFont(font);
        group.getChildren().add(message);
        return message;
    }


}
