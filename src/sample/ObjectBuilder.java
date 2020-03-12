package sample;

import javafx.scene.Group;

public class ObjectBuilder {
    private double x = 0;
    private double y = 0;
    private double z = 0;
    private double width = 1;
    private double height = 1;
    private double depth = 1;
    private boolean solidState = false;
    private boolean physicsState = false;
    private Group group;
    private EnvironmentUtil environmentUtil;

    public ObjectBuilder(float x,float y,float z){
        group = new Group();
    }



    public Group getGroup(){
        return group;
    }
    public void setGroup(Group group){ this.group = group; }

    public void setSolidState(boolean state) { solidState = state; }
    public boolean getSolidState() { return solidState; }

    public void setPhysicsState(boolean state) { physicsState = state; }
    public boolean getPhysicsState(){ return physicsState; }

    public void setPos(double x, double y, double z){
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

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }

    public void setWidth(double width) {
        this.width = width;
        group.setScaleX(width);
    }
    public void setHeight(double height) {
        this.height = height;
        group.setScaleY(width);
    }
    public void setDepth(double depth) {
        this.depth = depth;
        group.setScaleZ(depth);
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getDepth() { return depth; }


}

