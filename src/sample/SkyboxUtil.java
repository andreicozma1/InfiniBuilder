package sample;

import javafx.scene.AmbientLight;
import javafx.scene.DepthTest;
import javafx.scene.LightBase;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.sql.Time;

public class SkyboxUtil {
    private EnvironmentUtil context;
    private AmbientLight ambient = null;

    private Sphere sun;
    private PointLight sunlight;
    private double sun_distance;
    Color suncolor;
    private Sphere moon;
    private PointLight moonlight;
    private double moon_distance;
    Color mooncolor;


    SkyboxUtil(EnvironmentUtil envir) {
        context = envir;

        sun = new Sphere();
        sunlight = new PointLight();
        sunlight.setDepthTest(DepthTest.ENABLE);
        setSunScale(500);
        setSunDistance(10000);
        setSunMaterial(MaterialsUtil.red);
        setSunlightColor(Color.RED);

        moon = new Sphere();
        moonlight = new PointLight();
        moonlight.setDepthTest(DepthTest.ENABLE);
        setMoonScale(500);
        setMoonDistance(10000);
        setMoonMaterial(MaterialsUtil.blue);
        setMoonlightColor(Color.DARKBLUE);

        context.getGroup().getChildren().addAll(sun, sunlight, moon, moonlight);
    }

    void handle() {
        rotateSun(sun_distance);
        rotateMoon(moon_distance);
    }

    private void rotateSun(double dist) {
        double sin = Math.sin(System.currentTimeMillis() / 10000.0);
        double sindist = sin * dist;
        double cos = Math.cos(System.currentTimeMillis() / 10000.0);
        double cosdist = cos * dist;

//        System.out.println("SUN sin: " + sin + " cos: " + cos);

        Color newcol = sunlight.getColor();
        if (sin <= 0) {
            sin = sin * -1;
            sunlight.setColor(Color.rgb((int) (suncolor.getRed() * sin * 255), (int) (suncolor.getGreen() * sin * 255), (int) (suncolor.getBlue() * sin * 255)));
        }

        sunlight.setTranslateZ(cosdist);
        sunlight.setTranslateY(sindist);
        sun.setTranslateZ(cosdist);
        sun.setTranslateY(sindist);
    }

    private void rotateMoon(double dist) {
        double sin = Math.sin(System.currentTimeMillis() / 10000.0);
        double sindist = sin * dist;
        double cos = Math.cos(System.currentTimeMillis() / 10000.0);
        double cosdist = cos * dist;
        System.out.println("MOON sin: " + -sin + " cos: " + -cos);

        Color newcol = moonlight.getColor();
        if (-sin <= 0) {
            System.out.println((mooncolor.getRed() * 255 * sin) + "  " + (mooncolor.getGreen() * 255 * sin) + "  " + (mooncolor.getBlue() * 255 * sin));

            moonlight.setColor(Color.rgb((int) (mooncolor.getRed() * sin * 255), (int) (mooncolor.getGreen() * sin * 255), (int) (mooncolor.getBlue() * sin * 255)));
        }

        moonlight.setTranslateZ(-cosdist);
        moonlight.setTranslateY(-sindist);
        moon.setTranslateZ(-cosdist);
        moon.setTranslateY(-sindist);
    }


    public void setSunScale(double scale) {
        sun.setScaleX(scale);
        sun.setScaleY(scale);
        sun.setScaleZ(scale);
    }

    public void setMoonScale(double scale) {
        moon.setScaleX(scale);
        moon.setScaleY(scale);
        moon.setScaleZ(scale);
    }

    public void setSunMaterial(PhongMaterial mat) {
        sun.setMaterial(mat);
    }

    public void setMoonMaterial(PhongMaterial mat) {
        moon.setMaterial(mat);
    }

    public void setSunlightColor(Color clr) {
        suncolor = clr;
        sunlight.setColor(suncolor);
    }

    public void setMoonlightColor(Color clr) {
        mooncolor = clr;
        moonlight.setColor(mooncolor);
    }

    public void setSunDistance(double dist) {
        sun_distance = dist;
    }

    public void setMoonDistance(double dist) {
        moon_distance = dist;
    }


    public void setAmbientLight(AmbientLight node) {
        ambient = node;
        context.getGroup().getChildren().add(ambient);
    }

    public void resetLighting() {
        if (context.getGroup().getChildren().contains(ambient)) {
            context.getGroup().getChildren().remove(ambient);
            context.getGroup().getChildren().add(ambient);
        }
    }

}
