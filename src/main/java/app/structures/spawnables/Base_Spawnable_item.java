package app.structures.spawnables;

import app.structures.SpawnableStructure;
import app.structures.StructureProperties;
import app.structures.objects.BaseStructure;

public abstract class Base_Spawnable_item extends BaseStructure {
    public SpawnableStructure spawnable;

    Base_Spawnable_item(SpawnableStructure str, BaseStructure m){
        spawnable = str;

        this.setShape(m.getShape());
        this.getShape().setMaterial(m.getShape().getMaterial());
        this.setScaleIndependent(m.getScaleX(),m.getScaleY(),m.getScaleZ());
        this.setProps(m.getProps());
        getProps().setPROPERTY_ITEM_TYPE(StructureProperties.TYPE_SPAWNABLE);
        super.getChildren().add(this.getShape());
    }

    public SpawnableStructure getSpawnableStructure(){
        return spawnable;
    }

}
