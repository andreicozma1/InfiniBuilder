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

public class InterfaceBuilder extends Parent {
    private Group group;

    public InterfaceBuilder() {
        group = new Group();
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group Group) {
        this.group = group;
    }

    public Node addNode(Node n){
        group.getChildren().add(n);
        return n;
    }

    public Line drawLine(double x1, double y1, double x2, double y2, Paint paint) {
        Line line = new Line(x1, y1, x2, y2);
        line.setFill(paint);
        group.getChildren().add(line);
        return line;
    }

    public Rectangle drawRectangle(double x, double y, double w, double h, double arcW, double arcH, Paint paint) {
        Rectangle rectangle = new Rectangle(w, h, paint);
        rectangle.setX(x);
        rectangle.setY(y);
        rectangle.setArcWidth(arcW);
        rectangle.setArcHeight(arcH);
        group.getChildren().add(rectangle);
        return rectangle;
    }

    public Circle drawCircle(double centerX, double centerY, double radius, Paint paint) {
        Circle circle = new Circle(centerX, centerY, radius, paint);
        group.getChildren().add(circle);
        return circle;
    }

    public Ellipse drawEllipse(double centerX, double centerY, double radiusX, double radiusY, Paint paint) {
        Ellipse ellipse = new Ellipse(centerX, centerY, radiusX, radiusY);
        ellipse.setFill(paint);
        group.getChildren().add(ellipse);
        return ellipse;
    }

    public Polygon drawPolygon(double[] points, Paint paint) {
        Polygon polygon = new Polygon(points);
        polygon.setFill(paint);
        group.getChildren().add(polygon);
        return polygon;
    }

    public CubicCurve drawCubicCurve(double startX, double startY, double controlX1, double controlY1, double controlX2, double controlY2, double endX, double endY, Paint paint) {
        CubicCurve cubicCurve = new CubicCurve(startX, startY, controlX1, controlY1, controlX2, controlY2, endX, endY);
        cubicCurve.setFill(paint);
        group.getChildren().add(cubicCurve);
        return cubicCurve;
    }

    public QuadCurve drawQuadCurve(double startX, double startY, double controlX, double controlY, double endX, double endY, Paint paint) {
        QuadCurve quadCurve = new QuadCurve(startX, startY, controlX, controlY, endX, endY);
        quadCurve.setFill(paint);
        group.getChildren().add(quadCurve);
        return quadCurve;

    }

    public Arc drawArc(double centerX, double centerY, double radiusX, double radiusY, double startAngle, double length, Paint paint) {
        Arc arc = new Arc(centerX, centerY, radiusX, radiusY, startAngle, length);
        arc.setFill(paint);
        group.getChildren().add(arc);
        return arc;
    }

    public Button drawButton(Button button, double x, double y) {
        button.setLayoutX(x);
        button.setLayoutY(y);
        group.getChildren().add(button);
        return button;
    }

    public Box drawBox(Box box, double x, double y) {
        box.setTranslateY(x);
        box.setTranslateX(y);
        group.getChildren().add(box);
        return box;
    }

    public Label drawLabel(String label, double x, double y, Color color) {
        Label message = new Label(label);
        message.setLayoutX(x);
        message.setLayoutY(y);
        message.setTextFill(color);
        group.getChildren().add(message);
        return message;
    }

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
