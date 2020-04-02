package app.structures;

import app.environment.EnvironmentUtil;
import app.structures.objects.Base_Cube;
import app.structures.objects.Base_Sphere;
import javafx.geometry.Point2D;
import javafx.scene.Group;

public class StructureBuilder extends Group implements Interactable {
    public static String UNDEFINED_TAG = "Undefined";

    public static final int TYPE_OBJECT = 0;
    public static final int TYPE_STRUCTURE_2D = 1;
    public static final int TYPE_STRUCTURE_3D = 2;
    public static final int TYPE_WEAPON = 3;

    public static final int OBJECT_TYPE_CUBE = 0;
    public static final int OBJECT_TYPE_SPHERE = 1;
    public static final int OBJECT_TYPE_MODEL = 2;

    StructureBuilder.properties p;


    public StructureBuilder() {
        p = new properties();
        p.PROPERTY_ITEM_TYPE = TYPE_OBJECT;
        p.PROPERTY_ITEM_TAG = UNDEFINED_TAG;
    }

    public static StructureBuilder resolve(StructureBuilder orig) {
        StructureBuilder result = null;
        switch (orig.getProps().getPROPERTY_OBJECT_TYPE()) {
            case OBJECT_TYPE_CUBE:
                System.out.println("HERE 1");
                Base_Cube cube = new Base_Cube(orig.getProps().getPROPERTY_ITEM_TAG(), ((Base_Cube) orig).getMaterial(), orig.getScaleX(), orig.getScaleY(), orig.getScaleZ());
                result = cube;
                break;
            case OBJECT_TYPE_SPHERE:
                Base_Sphere sphere = new Base_Sphere(orig.getProps().getPROPERTY_ITEM_TAG(), ((Base_Sphere) orig).getMaterial(), ((Base_Sphere) orig).getSphere().getRadius(), orig.getScaleX(), orig.getScaleY(), orig.getScaleZ());
                result = sphere;
                break;
            case OBJECT_TYPE_MODEL:
                // TODO
                break;
            default:
                Base_Cube def = new Base_Cube(UNDEFINED_TAG, orig.getScaleX(), orig.getScaleY(), orig.getScaleZ());
                result = def;
                break;
        }
        result.setProps(orig.getProps());
        return result;
    }


    public void setScaleIndependent(double x, double y, double z) {
        this.setScaleX(x);
        this.setScaleY(y);
        this.setScaleZ(z);
    }

    public void setScaleAll(double scale) {
        this.setScaleX(scale);
        this.setScaleY(scale);
        this.setScaleZ(scale);
    }

    public void setTranslateIndependent(double x, double y, double z) {
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

    public properties getProps() {
        return p;
    }

    public void setProps(properties pr) {
        p = pr;
    }

    @Override
    public void place(EnvironmentUtil e, Point2D pos) {
        // right click action usually
        e.placeObject(pos, this, false);
    }

    @Override
    public void use() {
        // left click action usually

    }

    public class properties {
        private int PROPERTY_ITEM_TYPE;
        private int PROPERTY_OBJECT_TYPE;
        private String PROPERTY_ITEM_TAG;
        private boolean PROPERTY_DESTRUCTIBLE = false;

        public void setPROPERTY_ITEM_TYPE(int t) {
            PROPERTY_ITEM_TYPE = t;
        }

        public int getPROPERTY_ITEM_TYPE() {
            return PROPERTY_ITEM_TYPE;
        }

        public void setPROPERTY_OBJECT_TYPE(int t) {
            PROPERTY_OBJECT_TYPE = t;
        }

        public int getPROPERTY_OBJECT_TYPE() {
            return PROPERTY_OBJECT_TYPE;
        }


        public String getPROPERTY_ITEM_TAG() {
            return PROPERTY_ITEM_TAG;
        }

        public void setPROPERTY_ITEM_TAG(String itm) {
            PROPERTY_ITEM_TAG = itm;
        }

        public boolean getPROPERTY_DESTRUCTIBLE() {
            return PROPERTY_DESTRUCTIBLE;
        }

        public void setPROPERTY_DESTRUCTIBLE(boolean val) {
            PROPERTY_DESTRUCTIBLE = val;
        }
    }

}

