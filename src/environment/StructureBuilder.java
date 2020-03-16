package environment;

import javafx.scene.Group;
import javafx.scene.Node;
import utils.PhysicsUtil;

public class StructureBuilder extends Group {
    private double x = 0;
    private double y = 0;
    private double z = 0;
    private double scaleX = 1;
    private double scaleY = 1;
    private double scaleZ = 1;
    private boolean solidState = false;
    private boolean physicsState = false;
    private PhysicsUtil physicsUtil;

    public StructureBuilder() {
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



    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }


    public double getWidth() {
        return this.getBoundsInLocal().getWidth();
    }

    public double getHeight() {
        return this.getBoundsInParent().getHeight();
    }

    public double getDepth() {
        return this.getBoundsInParent().getDepth();
    }

}

