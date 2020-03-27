package app.structures.objects;

import app.structures.StructureBuilder;

public class Base_Model extends StructureBuilder {

    public Base_Model(String ITEM_TAG){
        setItemTag(ITEM_TAG);
    }
    public Base_Model(){
        setItemTag("Base Model");
    }
}
