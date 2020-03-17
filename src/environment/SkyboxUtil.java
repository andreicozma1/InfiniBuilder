package environment;

import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class SkyboxUtil {
    private EnvironmentUtil context;
    private Group group_skybox;

    private AmbientLight ambient = null;
    public int day_length_multiplier = 5;

    public static final int MODE_CYCLE = 0;
    public static final int MODE_DAY = 1;
    public static final int MODE_NIGHT = 2;
    private int MODE_CURR;

    //TODO? Put these in classes?
    private Sphere sun;
    private Rotate sun_rotate;
    private PointLight sunlight;
    private double sun_distance;
    Color suncolor;
    Color dayskycolor;
    Color sunset_color;

    //TODO? Put these in classes?
    private Sphere moon;
    private Rotate moon_rotate;
    private PointLight moonlight;
    private double moon_distance;
    Color mooncolor;
    Color nightskycolor;

    /**
     * Constructor for SkyboxUtil. This initializes
     *
     * @param envir
     */
    public SkyboxUtil(EnvironmentUtil envir) {
        context = envir;
        group_skybox = new Group();
        MODE_CURR = MODE_CYCLE;

        sunset_color = Color.rgb(253,94,83);

        sun = new Sphere();
        sunlight = new PointLight();
        sunlight.setDepthTest(DepthTest.ENABLE);
        setSunScale(500);
        setSunDistance(3000);
        setSunMaterial(MaterialsUtil.sun);
        setSunlightColor(Color.WHITE);
        setDaySkyColor(Color.rgb(135, 206, 235));
        sun_rotate = new Rotate(0,new Point3D(0,1,0));
        sun.getTransforms().setAll(sun_rotate);

        moon = new Sphere();
        moonlight = new PointLight();
        moonlight.setDepthTest(DepthTest.ENABLE);
        setMoonScale(200);
        setMoonDistance(3000);
        setMoonMaterial(MaterialsUtil.moon);
        setMoonlightColor(Color.rgb(20, 20, 60));
        setNightSkyColor(Color.rgb(10, 10, 35));
        moon_rotate = new Rotate(0,new Point3D(0,1,0));
        moon.getTransforms().setAll(moon_rotate);

        group_skybox.getChildren().addAll(sun, sunlight, moon, moonlight);
    }

    /**
     *
     */
    void update_handler() {

        double time;
        switch (MODE_CURR) {
            case MODE_DAY:
                time = Math.PI/2;
                break;
            case MODE_NIGHT:
                time = -Math.PI/2;
                break;
            default:
                time = System.currentTimeMillis()/ (1000.0 * day_length_multiplier);
                break;
        }
        rotateSun(time, sun_distance);
        rotateMoon(time, moon_distance);
    }


    double sun_offset = 0;

    private void rotateSun(double time, double dist) {
        double sin = Math.sin(time );
        double sindist = sin * dist;
        double cos = Math.cos(time);
        double cosdist = cos * dist;

        // offset for dimming the sky and light to black
        // since the sun will be at horizon when sin/cos is 0, we want an offset so that the light isn't completely out when the sun is setting
        // we want the world to turn compeltely dark AFTER the sun is set with a small delay, till the moon comes out and shines the world
        if (sin <= sun_offset) {
            sin = sin * -1;
            sin += sun_offset;
            // flip the values of the sin and add .5 to it for the offset, now the values will be from 0 to 1.5.
            // Since we use the sin to calculate the intensity of the colors, we want the multiplier to be capped at 1
            // so if the sin > 1, set it back to 1.
            // this way, during mid day, the intensity of the sun remains constant for longer, before slowly fading to black.
            if (sin > 1) {
                sin = 1;
            }
            System.out.println(sin);
            sunlight.setColor(Color.rgb((int) (sin*((sunset_color.getRed() * (1-sin)* 255) + (suncolor.getRed() * sin * 255))), (int)(sin*((sunset_color.getGreen() * (1-sin)* 255) + (suncolor.getGreen() * sin * 255))), (int) (sin*((sunset_color.getBlue() * (1-sin)* 255) + (suncolor.getBlue() * sin * 255)))));
            context.context.SCENE_GAME.setFill(Color.rgb((int) ((sunset_color.getRed() * (1-sin)* 255) + (dayskycolor.getRed() * sin * 255)), (int) ((sunset_color.getGreen() * (1-sin)* 255) + (dayskycolor.getGreen() * sin * 255)), (int) ((sunset_color.getBlue() * (1-sin)* 255) + (dayskycolor.getBlue() * sin * 255))));
        } else {
            sunlight.setColor(Color.rgb(0, 0, 0));
        }



        // position the sun relative to the player's position
        // the sun is a full 180 degrees (pi) away from the moon, so the sin and cos values are flipped on the sun compared to the moon
        sunlight.setTranslateX(context.context.getPlayer().getX());
        sunlight.setTranslateY(sindist);
        sunlight.setTranslateZ(cosdist + context.context.getPlayer().getZ());
        sun.setTranslateX(context.context.getPlayer().getX());
        sun.setTranslateY(sindist);
        sun.setTranslateZ(cosdist + context.context.getPlayer().getZ());


        sun_rotate.setAngle(sun_rotate.getAngle() +.1);
    }

    private void rotateMoon(double time, double dist) {
        double sin = Math.sin(time);
        double sindist = sin * dist;
        double cos = Math.cos(time);
        double cosdist = cos * dist;

        // complement to the offset set on the sun.
        // this way, we check if the sun has already set, if so, we subtract the multiplier from the sun and multiply by 2
        // to achieve a value from 0 to 1 which we will use for calculating the intensity of the moon light
        // TODO? use the .5 as a variable multiplier?
        if (sin >= sun_offset) {
            sin -= sun_offset;
//            sin *= (1/sun_offset);
            moonlight.setColor(Color.rgb((int) (sin*((sunset_color.getRed() * (1-sin)* 255) + (mooncolor.getRed() * sin * 255))), (int) (sin*((sunset_color.getGreen() * (1-sin)* 255) + (mooncolor.getGreen() * sin * 255))), (int) (sin*(sunset_color.getBlue() * (1-sin)* 255) + (mooncolor.getBlue() * sin * 255))));
            context.context.SCENE_GAME.setFill(Color.rgb((int)((sunset_color.getRed() * (1-sin)* 255) +  (nightskycolor.getRed() * sin * 255)), (int) ((sunset_color.getGreen() * (1-sin)* 255) + (nightskycolor.getGreen() * sin * 255)), (int) ((sunset_color.getBlue() * (1-sin)* 255) + (nightskycolor.getBlue() * sin * 255))));
        } else {
            moonlight.setColor(Color.rgb(0, 0, 0));
        }

        // position the moon relative to the player's position
        // the moon is a full 180 degrees (pi) away from the sun, so the sin and cos values are flipped on the moon compared to the sun
        moonlight.setTranslateX(context.context.getPlayer().getX());
        moonlight.setTranslateY(-sindist);
        moonlight.setTranslateZ(-cosdist + context.context.getPlayer().getZ());
        moon.setTranslateY(-sindist);
        moon.setTranslateX(context.context.getPlayer().getX());
        moon.setTranslateZ(-cosdist + context.context.getPlayer().getZ());

        moon_rotate.setAngle(sun_rotate.getAngle() +.1);

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

    public Group getGroup() {
        return group_skybox;
    }

    public void setMode(int mode_new) {
        MODE_CURR = mode_new;
    }

    public int getMode() {
        return MODE_CURR;
    }

    public void cycleModes() {
        switch (getMode()) {
            case SkyboxUtil.MODE_CYCLE:
                setMode(SkyboxUtil.MODE_DAY);
                break;
            case SkyboxUtil.MODE_DAY:
                setMode(SkyboxUtil.MODE_NIGHT);
                break;
            case SkyboxUtil.MODE_NIGHT:
                setMode(SkyboxUtil.MODE_CYCLE);
                break;
        }
    }

}
