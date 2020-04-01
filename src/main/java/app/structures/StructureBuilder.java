package app.structures;

import app.structures.objects.Base_Cube;
import app.structures.objects.Base_Sphere;
import javafx.scene.Group;

public class StructureBuilder extends Group{
    public static String UNDEFINED_TAG = "Undefined";

    public static final int TYPE_OBJECT = 0;
    public static final int TYPE_CUBE = 1;
    public static final int TYPE_MODEL = 2;
    public static final int TYPE_SPHERE = 3;
    public static final int TYPE_WEAPON = 4;

    StructureBuilder.properties p;

    public class properties {
        public int TYPE_CURRENT;
        public String itemTag;
        public boolean isSolid = false;
    }

    public StructureBuilder() {
        p = new properties();
        p.TYPE_CURRENT = TYPE_OBJECT;
        p.itemTag = UNDEFINED_TAG;
    }

    public static StructureBuilder resolve(StructureBuilder orig){
        StructureBuilder result = null;
        switch(orig.getTYPE_CURRENT()){
            case TYPE_CUBE:
                result = new Base_Cube(orig.getItemTag(),((Base_Cube)orig).getMaterial(), orig.getScaleX(), orig.getScaleY(), orig.getScaleZ());
                break;
            case TYPE_SPHERE:
                result = new Base_Sphere(orig.getItemTag(),((Base_Sphere)orig).getMaterial(), ((Base_Sphere)orig).getSphere().getRadius(), orig.getScaleX(), orig.getScaleY(), orig.getScaleZ());
                break;
            case TYPE_MODEL:
                // TODO
                break;
            default:
                result = new Base_Cube(UNDEFINED_TAG,orig.getScaleX(),orig.getScaleY(),orig.getScaleZ());
        }
        result.setProps(orig.getProps());
        return result;
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

    public properties getProps(){
        return p;
    }
    public void setProps(properties pr){
        p = pr;
    }

    public void setTYPE_CURRENT(int t){
        p.TYPE_CURRENT = t;
    }
    public int getTYPE_CURRENT(){
        return p.TYPE_CURRENT;
    }

    public String getItemTag(){
        return p.itemTag;
    }
    public void setItemTag(String itm) {
        p.itemTag = itm;
    }

    public boolean getIsSolid(){
        return p.isSolid;
    }
    public void setIsSolid(boolean val){
        p.isSolid = val;
    }
}

