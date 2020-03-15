package sample;

import javafx.scene.Group;
import javafx.scene.Node;

public class StructureBuilder {
    private double x = 0;
    private double y = 0;
    private double z = 0;
    private double scaleX = 1;
    private double scaleY = 1;
    private double scaleZ = 1;
    private boolean solidState = false;
    private boolean physicsState = false;
    private PhysicsUtil physicsUtil;
    private Group group;

    public StructureBuilder(double x, double y, double z) {
        group = new Group();
        setX(x);
        setY(y);
        setZ(z);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setSolidState(boolean state) {
        solidState = state;
    }

    public boolean getSolidState() {
        return solidState;
    }

    public void setPhysicsState(boolean state) {
        physicsState = state;
    }

    public boolean getPhysicsState() {
        return physicsState;
    }

    public void setPhysicsUtil(PhysicsUtil physicsUtil) {
        this.physicsUtil = physicsUtil;
    }

    public PhysicsUtil getPhysicsUtil() {
        return physicsUtil;
    }

    public void setPos(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
    }

    public void setX(double x) {
        this.x = x;
        group.setTranslateX(x);
    }

    public void setY(double y) {
        this.y = y;
        group.setTranslateY(y);
    }

    public void setZ(double z) {
        this.z = z;
        group.setTranslateZ(z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setScaleX(double width) {
        this.scaleX = width;
        group.setScaleX(scaleX);
    }

    public void setScaleY(double height) {
        this.scaleY = height;
        group.setScaleY(scaleY);
    }

    public void setScaleZ(double depth) {
        this.scaleZ = depth;
        group.setScaleZ(scaleZ);
    }

    public double getWidth() {
        return getGroup().getBoundsInParent().getWidth();
    }

    public double getHeight() {
        return getGroup().getBoundsInParent().getHeight();
    }

    public double getDepth() {
        return getGroup().getBoundsInParent().getDepth();
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public double getScaleZ() {
        return scaleZ;
    }

    void addMember(Node o) {
        group.getChildren().add(o);
    }
}

