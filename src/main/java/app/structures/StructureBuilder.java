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



    public StructureBuilder() {
        p = new properties();
        p.PROPERTY_ITEM_TYPE = TYPE_OBJECT;
        p.PROPERTY_ITEM_TAG = UNDEFINED_TAG;
    }

    public static StructureBuilder resolve(StructureBuilder orig){
        StructureBuilder result = null;
        switch(orig.getProps().getTYPE_CURRENT()){
            case TYPE_CUBE:
                result = new Base_Cube(orig.getProps().getItemTag(),((Base_Cube)orig).getMaterial(), orig.getScaleX(), orig.getScaleY(), orig.getScaleZ());
                break;
            case TYPE_SPHERE:
                result = new Base_Sphere(orig.getProps().getItemTag(),((Base_Sphere)orig).getMaterial(), ((Base_Sphere)orig).getSphere().getRadius(), orig.getScaleX(), orig.getScaleY(), orig.getScaleZ());
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

    public class properties {
        public int PROPERTY_ITEM_TYPE;
        public String PROPERTY_ITEM_TAG;
        public boolean PROPERTY_DESTRUCTIBLE = false;

        public void setTYPE_CURRENT(int t){
            PROPERTY_ITEM_TYPE = t;
        }
        public int getTYPE_CURRENT(){
            return PROPERTY_ITEM_TYPE;
        }

        public String getItemTag(){
            return PROPERTY_ITEM_TAG;
        }
        public void setItemTag(String itm) {
            PROPERTY_ITEM_TAG = itm;
        }

        public boolean getIsDestructible(){
            return PROPERTY_DESTRUCTIBLE;
        }
        public void setIsDestructible(boolean val){
            PROPERTY_DESTRUCTIBLE = val;
        }
    }

}

