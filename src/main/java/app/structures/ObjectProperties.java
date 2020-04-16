package app.structures;

public class ObjectProperties {
    public static final String UNDEFINED_TAG = "Undefined";
    public static final String UNDEFINED_DESCRIPTION = "No description available";

    public static final int TYPE_OBJECT = 0;
    public static final int TYPE_SPAWNABLE_STRUCTURE = 1;
    public static final int TYPE_WEAPON = 2;

    public static final int OBJECT_TYPE_CUBE = 0;
    public static final int OBJECT_TYPE_SPHERE = 1;
    public static final int OBJECT_TYPE_CYLINDER = 2;
    public static final int OBJECT_TYPE_MODEL = 3;

    private int PROPERTY_ITEM_TYPE;
    private int PROPERTY_OBJECT_TYPE;
    private String PROPERTY_ITEM_TAG;
    private String PROPERTY_ITEM_DESCRIPTION;
    private boolean PROPERTY_DESTRUCTIBLE = false;

    /**
     * StructureProperties class can store different properties for our StructureBuilder class.
     * These properties are used to determine different actions to perform based on the structure's property
     */
    public ObjectProperties() {
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
