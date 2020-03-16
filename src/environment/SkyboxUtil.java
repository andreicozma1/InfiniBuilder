package environment;

import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class SkyboxUtil {
    private EnvironmentUtil context;
    private Group group_skybox;

    private AmbientLight ambient = null;
    public int day_length_multiplier = 30;

    public static final int MODE_CYCLE = 0;
    public static final int MODE_DAY = 1;
    public static final int MODE_NIGHT = 2;
    private int MODE_CURR;

    //TODO? Put these in classes?
    private Sphere sun;
    private PointLight sunlight;
    private double sun_distance;
    Color suncolor;
    Color dayskycolor;

    //TODO? Put these in classes?
    private Sphere moon;
    private PointLight moonlight;
    private double moon_distance;
    Color mooncolor;
    Color nightskycolor;

    /**
     * Constructor for SkyboxUtil. This initializes
     * @param envir
     */
    public SkyboxUtil(EnvironmentUtil envir) {
        context = envir;
        group_skybox = new Group();
        MODE_CURR = MODE_CYCLE;

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


    }

    /**
     *
     */
    void update_handler() {

        double time;
        switch(MODE_CURR){
            case MODE_DAY:
                time = -day_length_multiplier * 1000;
                break;
            case MODE_NIGHT:
                time = day_length_multiplier * 1000;
                break;
            default:
                time = System.currentTimeMillis();
                break;
        }
        rotateSun(time,sun_distance);
        rotateMoon(time,moon_distance);
    }

    private void rotateSun(double time, double dist) {
        double sin = Math.sin(time / (1000.0 * day_length_multiplier));
        double sindist = sin * dist;
        double cos = Math.cos(time / (1000.0 * day_length_multiplier));
        double cosdist = cos * dist;

        // offset for dimming the sky and light to black
        // since the sun will be at horizon when sin/cos is 0, we want an offset so that the light isn't completely out when the sun is setting
        // we want the world to turn compeltely dark AFTER the sun is set with a small delay, till the moon comes out and shines the world
        if (sin <= .5) {
            sin = sin * -1;
            sin += .5;
            // flip the values of the sin and add .5 to it for the offset, now the values will be from 0 to 1.5.
            // Since we use the sin to calculate the intensity of the colors, we want the multiplier to be capped at 1
            // so if the sin > 1, set it back to 1.
            // this way, during mid day, the intensity of the sun remains constant for longer, before slowly fading to black.
            if (sin > 1) {
                sin = 1;
            }
            sunlight.setColor(Color.rgb((int) (suncolor.getRed() * sin * 255), (int) (suncolor.getGreen() * sin * 255), (int) (suncolor.getBlue() * sin * 255)));
            context.context.SCENE_GAME.setFill(Color.rgb((int) (dayskycolor.getRed() * sin * 255), (int) (dayskycolor.getGreen() * sin * 255), (int) (dayskycolor.getBlue() * sin * 255)));
        } else {
            sunlight.setColor(Color.rgb(0, 0, 0));

//            sunlight.setColor(Color.BLACK);
        }

        // position the sun relative to the player's position
        // the sun is a full 180 degrees (pi) away from the moon, so the sin and cos values are flipped on the sun compared to the moon
        sunlight.setTranslateX(context.context.getPlayer().getX());
        sunlight.setTranslateY(sindist);
        sunlight.setTranslateZ(cosdist+ context.context.getPlayer().getZ());
        sun.setTranslateX(context.context.getPlayer().getX());
        sun.setTranslateY(sindist);
        sun.setTranslateZ(cosdist+ context.context.getPlayer().getZ());

    }

    private void rotateMoon(double time, double dist) {
        double sin = Math.sin(time / (1000.0 * day_length_multiplier));
        double sindist = sin * dist;
        double cos = Math.cos(time / (1000.0 * day_length_multiplier));
        double cosdist = cos * dist;

        // complement to the offset set on the sun.
        // this way, we check if the sun has already set, if so, we subtract the multiplier from the sun and multiply by 2
        // to achieve a value from 0 to 1 which we will use for calculating the intensity of the moon light
        // TODO? use the .5 as a variable multiplier?
        if (sin >= .5) {
            sin -=.5;
            sin *= 2;
            moonlight.setColor(Color.rgb((int) (mooncolor.getRed() * sin * 255), (int) (mooncolor.getGreen() * sin * 255), (int) (mooncolor.getBlue() * sin * 255)));
            context.context.SCENE_GAME.setFill(Color.rgb((int) (nightskycolor.getRed() * sin * 255), (int) (nightskycolor.getGreen() * sin * 255), (int) (nightskycolor.getBlue() * sin * 255)));
        } else{
            moonlight.setColor(Color.rgb(0, 0, 0));
        }

        // position the moon relative to the player's position
        // the moon is a full 180 degrees (pi) away from the sun, so the sin and cos values are flipped on the moon compared to the sun
        moonlight.setTranslateX(context.context.getPlayer().getX());
        moonlight.setTranslateY(-sindist);
        moonlight.setTranslateZ(-cosdist + context.context.getPlayer().getZ());
        moon.setTranslateY(-sindist);
        moon.setTranslateX(context.context.getPlayer().getX());
        moon.setTranslateZ(-cosdist+ context.context.getPlayer().getZ());
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
        context.getWorldGroup().getChildren().add(ambient);
    }

    public void resetLighting() {
        if (context.getWorldGroup().getChildren().contains(ambient)) {
            context.getWorldGroup().getChildren().remove(ambient);
            context.getWorldGroup().getChildren().add(ambient);
        }
    }

    public Group getGroup(){
        return group_skybox;
    }

  public void setMode(int mode_new){
        MODE_CURR = mode_new;
  }
  public int getMode(){
        return MODE_CURR;
  }

}
