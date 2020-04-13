package app.structures.objects;

import app.structures.StructureBuilder;
import app.structures.StructureProperties;

public class BaseModel extends BaseStructure {

    public static String BASE_MODEL_TAG = "Base Model";

    public BaseModel(String ITEM_TAG) {
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_MODEL);
    }

    public BaseModel() {
        getProps().setPROPERTY_ITEM_TAG(BASE_MODEL_TAG);
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(StructureProperties.OBJECT_TYPE_MODEL);
    }
}
