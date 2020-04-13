package app.structures;

import app.environment.EnvironmentUtil;
import app.player.AbsolutePoint3D;
import app.structures.objects.BaseCube;
import app.structures.objects.BaseCylinder;
import app.structures.objects.BaseSphere;
import app.structures.objects.BaseStructure;
import javafx.scene.Group;

public abstract class StructureBuilder extends Group implements Interactable {

    private StructureProperties p;

    public StructureBuilder() {
        p = new StructureProperties();
    }

    public static BaseStructure resolve(BaseStructure orig) {
        BaseStructure result = new BaseStructure();

        switch (orig.getProps().getPROPERTY_OBJECT_TYPE()) {
            case StructureProperties.OBJECT_TYPE_CUBE:
                result = new BaseCube(orig);
                break;
            case StructureProperties.OBJECT_TYPE_SPHERE:
                result = new BaseSphere(orig);
                break;
            case StructureProperties.OBJECT_TYPE_CYLINDER:
                result = new BaseCylinder(orig);
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
}

