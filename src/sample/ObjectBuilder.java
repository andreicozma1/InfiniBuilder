package sample;

import javafx.scene.Group;

public class ObjectBuilder {
    public double x = 0;
    public double y = 0;
    public double z = 0;
    public Group group;
    public EnvironmentUtil environmentUtil;

    public ObjectBuilder( EnvironmentUtil environmentUtil ){
        this.environmentUtil = environmentUtil;
        group = new Group();
    }

    public void show(boolean state){
        if(state){
            draw();
        } else {
            environmentUtil.environment_group.getChildren().remove(group);
        }
    }

    public void draw(){
        environmentUtil.environment_group.getChildren().add(group);
    }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setZ(double z) { this.z = z; }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
}

