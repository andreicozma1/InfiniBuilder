package app.structures;

import app.items.Item;
import javafx.scene.Group;
import app.utils.PhysicsUtil;

public abstract class StructureBuilder extends Group implements Item {
    private String itemTag;
    private double x = 0;
    private double y = 0;
    private double z = 0;
    private double scaleX = 1;
    private double scaleY = 1;
    private double scaleZ = 1;
    private boolean solidState = false;
    private boolean physicsState = false;
    private PhysicsUtil physicsUtil;
    private int type;
    public static int TYPE_OBJECT = 0;
    public static int TYPE_WEAPON = 1;

    public StructureBuilder() {
        type = TYPE_OBJECT;
    }

    @Override
    public String getItemTag() {
        return itemTag;
    }

    @Override
    public void setItemTag(String tag) {
        itemTag = tag;
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

    public void setScale(double scale){
        setScaleX(scale);
        setScaleY(scale);
        setScaleZ(scale);
    }
    public void setScaleXYZ(double x,double y,double z){
        setScaleX(x);
        setScaleY(y);
        setScaleZ(z);
    }
    public void setTranslateXYZ(double x,double y,double z){
        setTranslateX(x);
        setTranslateY(y);
        setTranslateZ(z);
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

    public void setType(int t){
        type = t;
    }
    public int getType(){
        return type;
    }

}

