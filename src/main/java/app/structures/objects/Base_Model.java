package app.structures.objects;

import app.structures.StructureBuilder;

public class Base_Model extends Base_Structure {

    public static String BASE_MODEL_TAG = "Base Model";
    public Base_Model(String ITEM_TAG){
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_MODEL);
    }
    public Base_Model(){
        getProps().setPROPERTY_ITEM_TAG(BASE_MODEL_TAG);
        getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureBuilder.OBJECT_TYPE_MODEL);
    }
}
