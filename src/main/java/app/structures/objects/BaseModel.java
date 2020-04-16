package app.structures.objects;

import app.structures.ObjectProperties;

public class BaseModel extends BaseObject {

    public static String BASE_MODEL_TAG = "Base Model";

    public BaseModel(String ITEM_TAG) {
        getProps().setPROPERTY_ITEM_TAG(ITEM_TAG);
        getProps().setPROPERTY_ITEM_TYPE(ObjectProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(ObjectProperties.OBJECT_TYPE_MODEL);
    }

    public BaseModel() {
        getProps().setPROPERTY_ITEM_TAG(BASE_MODEL_TAG);
        getProps().setPROPERTY_ITEM_TYPE(ObjectProperties.TYPE_OBJECT);
        getProps().setPROPERTY_OBJECT_TYPE(ObjectProperties.OBJECT_TYPE_MODEL);
    }
}
