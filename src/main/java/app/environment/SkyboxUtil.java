package app.environment;

import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import app.utils.ResourcesUtil;

public class SkyboxUtil {
    private EnvironmentUtil context;
    private Group group_skybox;

    private AmbientLight ambient = null;
    private int sun_moon_period_multiplier = 128;
    private int big_planet_period_multiplier = 128;
    private double sun_offset_ratio = 0; // value between -1 and 1 (shifts sin up)
    private double sun_rotation_speed = .05;
    private double moon_rotation_speed = .05;
    private double big_star_rotate_speed = .005;

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

    private Sphere big_star;
    private Rotate big_star_rotate;
    private double big_star_distance;

    private Sphere clouds;
    private double clouds_rotate_speed;
    private Rotate clouds_rotate_x;
    private Rotate clouds_rotate_y;
    private Rotate clouds_rotate_z;
    private double clouds_height;

    /**
     * Constructor for SkyboxUtil. This initializes
     *
     * @param envir
     */
    public SkyboxUtil(EnvironmentUtil envir) {
        context = envir;
        group_skybox = new Group();
        MODE_CURR = MODE_CYCLE;

        sunset_color = Color.rgb(253, 94, 83);

        clouds_height = 800;

        clouds = new Sphere();
        clouds.setCullFace(CullFace.NONE);
        clouds.setMaterial(ResourcesUtil.clouds);
        clouds_rotate_speed = 0.001;
        clouds.setTranslateY(context.planet_diameter - clouds_height);
        clouds.setScaleX(context.planet_diameter + clouds_height);
        clouds.setScaleY(context.planet_diameter + clouds_height);
        clouds.setScaleZ(context.planet_diameter + clouds_height);
        clouds.setEffect(new GaussianBlur(5));

        clouds_rotate_x = new Rotate(90, new Point3D(1, 0, 0));
        clouds_rotate_y = new Rotate(0, new Point3D(0, 1, 0));
        clouds_rotate_z = new Rotate(0, new Point3D(0, 0, 1));
        clouds.getTransforms().setAll(clouds_rotate_x, clouds_rotate_y, clouds_rotate_z);


        big_star = new Sphere();
        big_star.setMaterial(ResourcesUtil.big_star);
        setBigStarScale(4000);
        setBigStarDistance(context.planet_diameter + 1000);
        big_star_rotate = new Rotate(0, new Point3D(0, 1, 0));
        big_star.getTransforms().setAll(big_star_rotate);


        moon = new Sphere();
        moonlight = new PointLight();
        moonlight.setDepthTest(DepthTest.ENABLE);
        setMoonScale(1000);
        setMoonDistance(context.planet_diameter + 3000);
        setMoonMaterial(ResourcesUtil.moon);
        setMoonlightColor(Color.rgb(20, 20, 60));
        setNightSkyColor(Color.rgb(10, 10, 35));
        moon_rotate = new Rotate(0, new Point3D(0, 1, 0));
        moon.getTransforms().setAll(moon_rotate);


        sun = new Sphere();
        sunlight = new PointLight();
        sunlight.setDepthTest(DepthTest.ENABLE);
        setSunScale(2000);
        setSunDistance(context.planet_diameter + 8000);
        setSunMaterial(ResourcesUtil.sun);
        setSunlightColor(Color.WHITE);
        setDaySkyColor(Color.rgb(135, 206, 235));
        sun_rotate = new Rotate(0, new Point3D(0, 1, 0));
        sun.getTransforms().setAll(sun_rotate);


        group_skybox.getChildren().addAll(sun, sunlight, moon, moonlight, big_star, clouds);
    }

    /**
     *
     */

    private double fixed_time = -1; // set as default to -1 to track whether the user set it manually
    // if this variable is not -1, use this value as the game_time value

    void update_handler() {

        double game_time;
        switch (MODE_CURR) {
            case MODE_DAY:
                game_time = -Math.PI / 2 * sun_moon_period_multiplier;
                break;
            case MODE_NIGHT:
                game_time = Math.PI / 2 * sun_moon_period_multiplier;
                break;
            default:
                if (fixed_time == -1) {
                    game_time = System.currentTimeMillis() / (1000.0);
                } else {
                    game_time = fixed_time;
                }
                break;
        }

        rotateSun(game_time / sun_moon_period_multiplier, sun_distance);
        rotateMoon(game_time / sun_moon_period_multiplier, moon_distance);
        rotateBigStar(game_time / big_planet_period_multiplier, big_star_distance);
        rotateClouds(game_time);
    }

    private void rotateClouds(double time) {
        clouds.setTranslateX(context.context.getPlayer().getPos_x());
        clouds.setTranslateZ(context.context.getPlayer().getPos_z());

        clouds_rotate_z.setAngle(clouds_rotate_z.getAngle() + clouds_rotate_speed);
        clouds_rotate_y.setAngle(clouds_rotate_y.getAngle() + clouds_rotate_speed);
//System.out.println((Math.sin(time) + 1)/2);
//        clouds.opacityProperty().setValue((Math.sin(time) + 1)/2);
    }

    private void rotateSun(double time, double dist) {
        double sin = Math.sin(time);
        double sindist = sin * dist;
        double cos = Math.cos(time);
        double cosdist = cos * dist;

        // offset for dimming the sky and light to black
        // since the sun will be at horizon when sin/cos is 0, we want an offset so that the light isn't completely out when the sun is setting
        // we want the world to turn compeltely dark AFTER the sun is set with a small delay, till the moon comes out and shines the world
        if (sin <= sun_offset_ratio) {
            sin = sin * -1;
            sin += sun_offset_ratio;
            // flip the values of the sin and add .5 to it for the offset, now the values will be from 0 to 1.5.
            // Since we use the sin to calculate the intensity of the colors, we want the multiplier to be capped at 1
            // so if the sin > 1, set it back to 1.
            // this way, during mid day, the intensity of the sun remains constant for longer, before slowly fading to black.
            if (sin > 1) {
                sin = 1;
            }
//            System.out.println(sin);
            sunlight.setColor(Color.rgb((int) (sin * ((sunset_color.getRed() * (1 - sin) * 255) + (suncolor.getRed() * sin * 255))), (int) (sin * ((sunset_color.getGreen() * (1 - sin) * 255) + (suncolor.getGreen() * sin * 255))), (int) (sin * ((sunset_color.getBlue() * (1 - sin) * 255) + (suncolor.getBlue() * sin * 255)))));
            context.context.getGameSubscene().setFill(Color.rgb((int) ((sunset_color.getRed() * (1 - sin) * 255) + (dayskycolor.getRed() * sin * 255)), (int) ((sunset_color.getGreen() * (1 - sin) * 255) + (dayskycolor.getGreen() * sin * 255)), (int) ((sunset_color.getBlue() * (1 - sin) * 255) + (dayskycolor.getBlue() * sin * 255))));
        } else {
            sunlight.setColor(Color.rgb(0, 0, 0));
        }


        // position the sun relative to the player's position
        // the sun is a full 180 degrees (pi) away from the moon, so the sin and cos values are flipped on the sun compared to the moon
        sunlight.setTranslateX(context.context.getPlayer().getPos_x());
        sunlight.setTranslateY(sindist);
        sunlight.setTranslateZ(cosdist + context.context.getPlayer().getPos_z());
        sun.setTranslateX(context.context.getPlayer().getPos_x());
        sun.setTranslateY(sindist);
        sun.setTranslateZ(cosdist + context.context.getPlayer().getPos_z());


        sun_rotate.setAngle(sun_rotate.getAngle() + sun_rotation_speed);
    }


    private void rotateBigStar(double time, double dist) {
        double sin = Math.sin(time);
        double sindist = sin * dist;
        double cos = Math.cos(time);
        double cosdist = cos * dist;
        big_star.setTranslateX(sindist + context.context.getPlayer().getPos_x());
        big_star.setTranslateY(sindist);
        big_star.setTranslateZ(cosdist + context.context.getPlayer().getPos_z());
        big_star_rotate.setAngle(big_star_rotate.getAngle() + big_star_rotate_speed);
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
        if (sin >= sun_offset_ratio) {
            sin -= sun_offset_ratio;
//            sin *= (1/sun_offset);
            moonlight.setColor(Color.rgb((int) (sin * ((sunset_color.getRed() * (1 - sin) * 255) + (mooncolor.getRed() * sin * 255))), (int) (sin * ((sunset_color.getGreen() * (1 - sin) * 255) + (mooncolor.getGreen() * sin * 255))), (int) (sin * (sunset_color.getBlue() * (1 - sin) * 255) + (mooncolor.getBlue() * sin * 255))));
            context.context.getGameSubscene().setFill(Color.rgb((int) ((sunset_color.getRed() * (1 - sin) * 255) + (nightskycolor.getRed() * sin * 255)), (int) ((sunset_color.getGreen() * (1 - sin) * 255) + (nightskycolor.getGreen() * sin * 255)), (int) ((sunset_color.getBlue() * (1 - sin) * 255) + (nightskycolor.getBlue() * sin * 255))));
        } else {
            moonlight.setColor(Color.rgb(0, 0, 0));
        }

        // position the moon relative to the player's position
        // the moon is a full 180 degrees (pi) away from the sun, so the sin and cos values are flipped on the moon compared to the sun
        moonlight.setTranslateX(context.context.getPlayer().getPos_x());
        moonlight.setTranslateY(-sindist);
        moonlight.setTranslateZ(-cosdist + context.context.getPlayer().getPos_z());
        moon.setTranslateY(-sindist);
        moon.setTranslateX(context.context.getPlayer().getPos_x());
        moon.setTranslateZ(-cosdist + context.context.getPlayer().getPos_z());

        moon_rotate.setAngle(sun_rotate.getAngle() + moon_rotation_speed);

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


    public void setBigStarScale(double scale) {
        big_star.setScaleX(scale);
        big_star.setScaleY(scale);
        big_star.setScaleZ(scale);
    }

    public double getSunScale() {
        return sun.getScaleX();
    }

    public double getMoonScale() {
        return moon.getScaleX();
    }

    public double getBigStarScale() {
        return big_star.getScaleX();
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

    public double getSunDistance() {
        return sun_distance;
    }

    public double getMoonDistance() {
        return moon_distance;
    }

    public double getBigStarDistance() {
        return big_star_distance;
    }

    public void setSunDistance(double dist) {
        sun_distance = clouds_height + dist;
    }

    public void setMoonDistance(double dist) {
        moon_distance = clouds_height + dist;
    }

    public void setBigStarDistance(double dist) {
        big_star_distance = clouds_height + dist;
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

    public int getSun_moon_period_multiplier() {
        return sun_moon_period_multiplier;
    }

    public void setSun_moon_period_multiplier(int num) {
        try{
            if(num > 0){
                sun_moon_period_multiplier = num;
            } else{
                throw new IndexOutOfBoundsException();
            }
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public int getBig_planet_period_multiplier() {
        return big_planet_period_multiplier;
    }

    public void setBig_planet_period_multiplier(int num) {
        try{
            if(num > 0){
                big_planet_period_multiplier = num;
            } else{
                throw new IndexOutOfBoundsException();
            }
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public void setSun_rotation_speed(double s) {
        sun_rotation_speed = s;
    }

    public void setMoon_rotation_speed(double s) {
        moon_rotation_speed = s;
    }

    public double getFixedTime() {
        return fixed_time;
    }

    public void setFixedTime(double t) {
        try{
            if(t >= -1){
                fixed_time = t;
            } else{
                throw new IndexOutOfBoundsException();
            }
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    } // -1 uses System Time. Any other value locks it to a specific time

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
