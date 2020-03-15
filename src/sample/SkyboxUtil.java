package sample;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class SkyboxUtil {
    private EnvironmentUtil context;

    Group group_skybox;

    private AmbientLight ambient = null;
    public int day_length_multiplier = 5;

    private Sphere sun;
    private PointLight sunlight;
    private double sun_distance;
    Color suncolor;
    Color dayskycolor;
    private Sphere moon;
    private PointLight moonlight;
    private double moon_distance;
    Color mooncolor;
    Color nightskycolor;


    SkyboxUtil(EnvironmentUtil envir) {
        context = envir;
        group_skybox = new Group();

        sun = new Sphere();
        sunlight = new PointLight();
        sunlight.setDepthTest(DepthTest.ENABLE);
        setSunScale(500);
        setSunDistance(10000);
        setSunMaterial(MaterialsUtil.sun);
        setSunlightColor(Color.WHITE);
        setDaySkyColor(Color.rgb(135, 206, 235));

        moon = new Sphere();
        moonlight = new PointLight();
        moonlight.setDepthTest(DepthTest.ENABLE);
        setMoonScale(200);
        setMoonDistance(10000);
        setMoonMaterial(MaterialsUtil.moon);
        setMoonlightColor(Color.rgb(20, 20, 60));
        setNightSkyColor(Color.rgb(10, 10, 35));


        group_skybox.getChildren().addAll(sun, sunlight, moon, moonlight);

        context.getEnvironmentGroup().getChildren().add(group_skybox);
    }

    void handle() {
        rotateSun(sun_distance);
        rotateMoon(moon_distance);
    }

    private void rotateSun(double dist) {
        double sin = Math.sin(System.currentTimeMillis() / (1000.0 * day_length_multiplier));
        double sindist = sin * dist;
        double cos = Math.cos(System.currentTimeMillis() / (1000.0 * day_length_multiplier));
        double cosdist = cos * dist;

        System.out.println("SUN sin: " + sin + " cos: " + cos);

        if (sin <= .5) {
            sin = sin * -1;
            sin += .5;
            if (sin > 1) {
                sin = 1;
            }
//            System.out.println((suncolor.getRed() * 255 * sin) + "  " + (suncolor.getGreen() * 255 * sin) + "  " + (suncolor.getBlue() * 255 * sin));

            sunlight.setColor(Color.rgb((int) (suncolor.getRed() * sin * 255), (int) (suncolor.getGreen() * sin * 255), (int) (suncolor.getBlue() * sin * 255)));
            context.context.SCENE_GAME.setFill(Color.rgb((int) (dayskycolor.getRed() * sin * 255), (int) (dayskycolor.getGreen() * sin * 255), (int) (dayskycolor.getBlue() * sin * 255)));
        } else {
//            sunlight.setColor(Color.BLACK);
        }

        sunlight.setTranslateZ(cosdist);
        sunlight.setTranslateY(sindist);
        sun.setTranslateZ(cosdist);
        sun.setTranslateY(sindist);
    }

    private void rotateMoon(double dist) {
        double sin = Math.sin(System.currentTimeMillis() / (1000.0 * day_length_multiplier));
        double sindist = sin * dist;
        double cos = Math.cos(System.currentTimeMillis() / (1000.0 * day_length_multiplier));
        double cosdist = cos * dist;
         System.out.println("MOON sin: " + -sin + " cos: " + -cos);

        if (sin >= .5) {
            sin -=.5;
            sin *= 2;
            // DO NOT UNCOMMENT FOR NOW
            moonlight.setColor(Color.rgb((int) (mooncolor.getRed() * sin * 255), (int) (mooncolor.getGreen() * sin * 255), (int) (mooncolor.getBlue() * sin * 255)));
            context.context.SCENE_GAME.setFill(Color.rgb((int) (nightskycolor.getRed() * sin * 255), (int) (nightskycolor.getGreen() * sin * 255), (int) (nightskycolor.getBlue() * sin * 255)));
        } else {
            // DO NOT UNCOMMENT FOR NOW
            // moonlight.setColor(Color.BLACK);
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
    }

    public void setMoonlightColor(Color clr) {
        mooncolor = clr;
    }

    public void setDaySkyColor(Color cr) {
        dayskycolor = cr;
    }

    public void setNightSkyColor(Color cr) {
        nightskycolor = cr;
    }

    public void setSunDistance(double dist) {
        sun_distance = dist;
    }

    public void setMoonDistance(double dist) {
        moon_distance = dist;
    }


    public void setAmbientLight(AmbientLight node) {
        ambient = node;
        context.getEnvironmentGroup().getChildren().add(ambient);
    }

    public void resetLighting() {
        if (context.getEnvironmentGroup().getChildren().contains(ambient)) {
            context.getEnvironmentGroup().getChildren().remove(ambient);
            context.getEnvironmentGroup().getChildren().add(ambient);
        }
    }

}
