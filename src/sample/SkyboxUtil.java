package sample;

import javafx.scene.AmbientLight;
import javafx.scene.LightBase;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.sql.Time;

public class SkyboxUtil {
    private EnvironmentUtil context;
    AmbientLight ambient = null;
    PointLight point;
    Sphere sun = new Sphere();

    SkyboxUtil(EnvironmentUtil envir){
        context = envir;


        PointLight pt = new PointLight();
        pt.setColor(Color.RED);

        setPointLight(pt);
    }

    void handle(){


        double sin = Math.sin(System.currentTimeMillis()/10000.0) * 10000;
        double cos = Math.cos(System.currentTimeMillis()/10000.0) * 10000;

        System.out.println("sin: " + sin + " cos: " + cos);


point.setTranslateZ(sin);
point.setTranslateY(cos);
sun.setTranslateZ(sin);
sun.setTranslateY(cos);
    }


    public void setPointLight(PointLight pt){
        point = pt;

        sun.setMaterial(MaterialsUtil.red);
        sun.setScaleX(500);
        sun.setScaleY(500);
        sun.setScaleZ(500);

        context.getGroup().getChildren().addAll(point,sun);
    }

    public void setAmbientLight(AmbientLight node) {
        ambient = node;

        context.getGroup().getChildren().add(ambient);
    }

    public void resetLighting(){
        if(context.getGroup().getChildren().contains(ambient)){
            context.getGroup().getChildren().remove(ambient);
            context.getGroup().getChildren().add(ambient);
        }
    }


}
