package app.structures;

import app.environment.EnvironmentUtil;
import app.player.AbsolutePoint3D;
import app.structures.objects.Base_Cube;
import app.structures.objects.Base_Cylinder;
import app.structures.objects.Base_Sphere;
import app.structures.objects.Base_Structure;
import javafx.scene.Group;
import javafx.scene.paint.Material;

public abstract class StructureBuilder extends Group implements Interactable {
    public static final String UNDEFINED_TAG = "Undefined";
    public static final String UNDEFINED_DESCRIPTION = "No description available";


    public static final int TYPE_OBJECT = 0;
    public static final int TYPE_SPAWNABLE = 1;
    public static final int TYPE_WEAPON = 2;

    public static final int OBJECT_TYPE_CUBE = 0;
    public static final int OBJECT_TYPE_SPHERE = 1;
    public static final int OBJECT_TYPE_CYLINDER = 2;
    public static final int OBJECT_TYPE_MODEL = 3;

    private StructureProperties p;

    public StructureBuilder() {
        p = new StructureProperties();
    }

    public static Base_Structure resolve(Base_Structure orig) {
        Base_Structure result = new Base_Structure();

        switch (orig.getProps().getPROPERTY_OBJECT_TYPE()) {
            case OBJECT_TYPE_CUBE:
                result = new Base_Cube(orig);
                break;
            case OBJECT_TYPE_SPHERE:
                result = new Base_Sphere(orig);
                break;
            case OBJECT_TYPE_CYLINDER:
                result = new Base_Cylinder(orig);
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
        return this.getBoundsInParent().getWidth();
    }

    public double getHeight() {
        return this.getBoundsInParent().getHeight();
    }

    public double getDepth() {
        return this.getBoundsInParent().getDepth();
    }

    public StructureProperties getProps() {
        return p;
    }

    public void setProps(StructureProperties pr) {
        p = pr;
    }


    @Override
    public void placeObject(EnvironmentUtil e, AbsolutePoint3D pos, boolean shouldStack) {
        // right click action usually
        e.placeObject(pos, this, shouldStack);
    }

    @Override
    public void use() {
        // left click action usually

    }


    public class StructureProperties {
        private int PROPERTY_ITEM_TYPE;
        private int PROPERTY_OBJECT_TYPE;
        private String PROPERTY_ITEM_TAG;
        private String PROPERTY_ITEM_DESCRIPTION;
        private boolean PROPERTY_DESTRUCTIBLE = false;

        StructureProperties(){
            PROPERTY_ITEM_TYPE = TYPE_OBJECT;
            PROPERTY_ITEM_TAG = UNDEFINED_TAG;
            PROPERTY_ITEM_DESCRIPTION = UNDEFINED_DESCRIPTION;
        }

        public int getPROPERTY_ITEM_TYPE() {
            return PROPERTY_ITEM_TYPE;
        }

        public void setPROPERTY_ITEM_TYPE(int t) {
            PROPERTY_ITEM_TYPE = t;
        }

        public int getPROPERTY_OBJECT_TYPE() {
            return PROPERTY_OBJECT_TYPE;
        }

        public void setPROPERTY_OBJECT_TYPE(int t) {
            PROPERTY_OBJECT_TYPE = t;
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

        public String getPROPERTY_ITEM_DESCRIPTION() {
            return PROPERTY_ITEM_DESCRIPTION;
        }

        public void setPROPERTY_ITEM_DESCRIPTION(String PROPERTY_ITEM_DESCRIPTION) {
            this.PROPERTY_ITEM_DESCRIPTION = PROPERTY_ITEM_DESCRIPTION;
        }
    }

}

