package app.structures;

import javafx.scene.Group;

public class StructureBuilder extends Group{
    private String itemTag;

    private int TYPE_CURRENT;
    public static int TYPE_OBJECT = 0;
    public static int TYPE_WEAPON = 1;

    public StructureBuilder() {
        TYPE_CURRENT = TYPE_OBJECT;
        itemTag = "Undefined";
    }

    public String getItemTag(){
        return itemTag;
    }
    public void setItemTag(String itm) {
        itemTag = itm;
    }

    public void setScaleIndependent(double x, double y, double z){
        this.setScaleX(x);
        this.setScaleY(y);
        this.setScaleZ(z);
    }
    public void setScaleAll(double scale){
        this.setScaleX(scale);
        this.setScaleY(scale);
        this.setScaleZ(scale);
    }
    public void setTranslateIndependent(double x, double y, double z){
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setTranslateZ(z);
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

    public void setTYPE_CURRENT(int t){
        TYPE_CURRENT = t;
    }
    public int getTYPE_CURRENT(){
        return TYPE_CURRENT;
    }
}

