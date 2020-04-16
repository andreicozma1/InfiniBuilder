package app.GUI.menu;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Interface Builder is a class used to easily create shapes in a group and hold the group
 */
public class InterfaceBuilder extends Parent {
    // global variables
    private Group group;

    // Constructor intializes the group
    public InterfaceBuilder() {
        group = new Group();
    }

    // getters and setters
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group Group) {
        this.group = group;
    }

    // this adds a custom node to the group
    public Node addNode(Node n) {
        group.getChildren().add(n);
        return n;
    }

    // this will draw a line for the user and add it to the group
    public Line drawLine(double x1, double y1, double x2, double y2, Paint paint) {
        Line line = new Line(x1, y1, x2, y2);
        line.setFill(paint);
        group.getChildren().add(line);
        return line;
    }

    // this will draw a rectangle for the user and add it to the group
    public Rectangle drawRectangle(double x, double y, double w, double h, double arcW, double arcH, Paint paint) {
        Rectangle rectangle = new Rectangle(w, h, paint);
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setArcWidth(arcW);
        rectangle.setArcHeight(arcH);
        group.getChildren().add(rectangle);
        return rectangle;
    }

    // this will draw a circle for the user and add it to the group
    public Circle drawCircle(double centerX, double centerY, double radius, Paint paint) {
        Circle circle = new Circle(centerX, centerY, radius, paint);
        group.getChildren().add(circle);
        return circle;
    }

    // this will draw an ellipse for the user and add it to the group
    public Ellipse drawEllipse(double centerX, double centerY, double radiusX, double radiusY, Paint paint) {
        Ellipse ellipse = new Ellipse(centerX, centerY, radiusX, radiusY);
        ellipse.setFill(paint);
        group.getChildren().add(ellipse);
        return ellipse;
    }

    // this will draw a polygon for the user and add it to the group
    public Polygon drawPolygon(double[] points, Paint paint) {
        Polygon polygon = new Polygon(points);
        polygon.setFill(paint);
        group.getChildren().add(polygon);
        return polygon;
    }

    // this will draw a cubic curve for the user and add it to the group
    public CubicCurve drawCubicCurve(double startX, double startY, double controlX1, double controlY1, double controlX2, double controlY2, double endX, double endY, Paint paint) {
        CubicCurve cubicCurve = new CubicCurve(startX, startY, controlX1, controlY1, controlX2, controlY2, endX, endY);
        cubicCurve.setFill(paint);
        group.getChildren().add(cubicCurve);
        return cubicCurve;
    }

    // this will draw quadratic curve for the user and add it to the group
    public QuadCurve drawQuadCurve(double startX, double startY, double controlX, double controlY, double endX, double endY, Paint paint) {
        QuadCurve quadCurve = new QuadCurve(startX, startY, controlX, controlY, endX, endY);
        quadCurve.setFill(paint);
        group.getChildren().add(quadCurve);
        return quadCurve;

    }

    // this will draw an arc for the user and add it to the group
    public Arc drawArc(double centerX, double centerY, double radiusX, double radiusY, double startAngle, double length, Paint paint) {
        Arc arc = new Arc(centerX, centerY, radiusX, radiusY, startAngle, length);
        arc.setFill(paint);
        group.getChildren().add(arc);
        return arc;
    }

    // this will draw a basic button for the user and add it to the group
    public Button drawButton(Button button, double x, double y) {
        button.setLayoutX(x);
        button.setLayoutY(y);
        group.getChildren().add(button);
        return button;
    }

    // this will add a box to the group for the user
    public Box drawBox(Box box, double x, double y) {
        box.setTranslateY(x);
        box.setTranslateX(y);
        group.getChildren().add(box);
        return box;
    }

    // this will draw a label for the user and add it to the group
    public Label drawLabel(String label, double x, double y, Color color) {
        Label message = new Label(label);
        message.setLayoutX(x);
        message.setLayoutY(y);
        message.setTextFill(color);
        group.getChildren().add(message);
        return message;
    }

    // this will draw text for the user and add it to the group
    public Text drawText(String text, double x, double y, Color color, Font font) {
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
