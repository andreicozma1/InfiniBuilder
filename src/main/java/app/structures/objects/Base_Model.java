package app.structures.objects;

import app.structures.StructureBuilder;

public class Base_Model extends StructureBuilder {

    public static String BASE_MODEL_TAG = "Base Model";
    public Base_Model(String ITEM_TAG){
        getProps().setItemTag(ITEM_TAG);
        getProps().setTYPE_CURRENT(StructureBuilder.TYPE_MODEL);
    }
    public Base_Model(){
        getProps().setItemTag(BASE_MODEL_TAG);
        getProps().setTYPE_CURRENT(StructureBuilder.TYPE_MODEL);
    }
}
