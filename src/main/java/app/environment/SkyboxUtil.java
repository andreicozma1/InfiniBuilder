package app.environment;

import app.utils.Log;
import app.utils.ResourcesUtil;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class SkyboxUtil {
    public static final int MODE_CYCLE = 0;
    public static final int MODE_DAY = 1;
    public static final int MODE_NIGHT = 2;
    private static final String TAG = "SkyboxUtil";
    private final EnvironmentUtil context;
    private final Group group_skybox;
    private final double sun_offset_ratio = 0; // value between -1 and 1 (shifts sin up)
    private final double big_star_rotate_speed = .005;
    //TODO? Put these in classes?
    private final Sphere sun;
    private final Rotate sun_rotate;
    private final PointLight sunlight;
    //TODO? Put these in classes?
    private final Sphere moon;
    private final Rotate moon_rotate;
    private final PointLight moonlight;
    private final Sphere big_star;
    private final Rotate big_star_rotate;
    private final Sphere clouds;
    private final double clouds_rotate_speed;
    private final Rotate clouds_rotate_x;
    private final Rotate clouds_rotate_y;
    private final Rotate clouds_rotate_z;
    private final double clouds_height;
    public double planet_diameter = 8000;
    public Color sky_color = null;
    Color suncolor;
    Color dayskycolor;
    Color sunset_color;
    Color mooncolor;
    Color nightskycolor;
    private AmbientLight ambient = null;
    private int sun_moon_period_multiplier = 128;
    private int big_planet_period_multiplier = 128;
    private double sun_rotation_speed = .05;
    private double moon_rotation_speed = .05;
    private int MODE_CURR;
    private double sun_distance;
    private double moon_distance;
    private double big_star_distance;
    /**
     *
     */

    private double fixed_time = -1; // set as default to -1 to track whether the user set it manually
    private boolean clouds_enabled;
    private boolean planets_enabled;

    Color defaultAmbientColor;

    /**
     * Constructor for SkyboxUtil. This initializes
     *
     * @param envir
     */
    public SkyboxUtil(EnvironmentUtil envir) {
        Log.p(TAG, "CONSTRUCTOR");

        context = envir;
        group_skybox = new Group();
        MODE_CURR = MODE_CYCLE;

        setDefaultAmbientColor(Color.rgb(90,90,90));
        AmbientLight amb = new AmbientLight();
        amb.setColor(defaultAmbientColor);
        setAmbientLight(amb);

        sunset_color = Color.rgb(253, 94, 83);

        clouds_height = 800;

        clouds = new Sphere();
        clouds.setCullFace(CullFace.NONE);
        clouds.setMaterial(ResourcesUtil.clouds);
        clouds_rotate_speed = 0.001;
        clouds.setTranslateY(planet_diameter - clouds_height);
        clouds.setScaleX(planet_diameter + clouds_height);
        clouds.setScaleY(planet_diameter + clouds_height);
        clouds.setScaleZ(planet_diameter + clouds_height);
        clouds.setEffect(new GaussianBlur(5));

        clouds_rotate_x = new Rotate(90, new Point3D(1, 0, 0));
        clouds_rotate_y = new Rotate(0, new Point3D(0, 1, 0));
        clouds_rotate_z = new Rotate(0, new Point3D(0, 0, 1));
        clouds.getTransforms().setAll(clouds_rotate_x, clouds_rotate_y, clouds_rotate_z);
        setShouldHaveClouds(true);

        big_star = new Sphere();
        big_star.setMaterial(ResourcesUtil.big_star);
        setBigStarScale(4000);
        setBigStarDistance(planet_diameter + 4000);
        big_star_rotate = new Rotate(0, new Point3D(0, 1, 0));
        big_star.getTransforms().setAll(big_star_rotate);

        moon = new Sphere();
        moonlight = new PointLight();
        moonlight.setDepthTest(DepthTest.ENABLE);
        setMoonScale(1000);
        setMoonDistance(planet_diameter + 3000);
        setMoonMaterial(ResourcesUtil.moon);
        setMoonlightColor(Color.rgb(20, 20, 60));
        setNightSkyColor(Color.rgb(8, 8, 30));
        moon_rotate = new Rotate(0, new Point3D(0, 1, 0));
        moon.getTransforms().setAll(moon_rotate);

        sun = new Sphere();
        sunlight = new PointLight();
        sunlight.setDepthTest(DepthTest.ENABLE);
        setSunScale(2000);
        setSunDistance(planet_diameter + 8000);
        setSunMaterial(ResourcesUtil.sun);
        setSunlightColor(Color.WHITE);
        setDaySkyColor(Color.rgb(135, 206, 235));
        sun_rotate = new Rotate(0, new Point3D(0, 1, 0));
        sun.getTransforms().setAll(sun_rotate);

        setShouldHavePlanets(true);

        group_skybox.getChildren().addAll(sun, sunlight, moon, moonlight, big_star, clouds);
    }
    // if this variable is not -1, use this value as the game_time value

    void update_handler() {
        double game_time;
        switch (MODE_CURR) {
            case MODE_DAY:
                game_time = -Math.PI / 2 / 6.5 * sun_moon_period_multiplier;
                break;
            case MODE_NIGHT:
                game_time = Math.PI / 2 / 6.5 * sun_moon_period_multiplier;
                break;
            default:
                if (fixed_time == -1) {
                    game_time = context.context.time_current / (1000.0);
                } else {
                    game_time = fixed_time;
                }
                break;
        }

        rotateSun(game_time / sun_moon_period_multiplier * 6.5, sun_distance);
        rotateMoon(game_time / sun_moon_period_multiplier * 6.5, moon_distance);
        rotateBigStar(game_time / big_planet_period_multiplier * 6.5, big_star_distance);
        rotateClouds();
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
            if (sky_color == null) {
                ambient.setColor(Color.rgb((int)(defaultAmbientColor.getRed() * 255),(int)(defaultAmbientColor.getGreen()*255),(int)(defaultAmbientColor.getBlue()*255)));
                sunlight.setColor(Color.rgb((int) (sin * ((sunset_color.getRed() * (1 - sin) * 255) + (suncolor.getRed() * sin * 255))), (int) (sin * ((sunset_color.getGreen() * (1 - sin) * 255) + (suncolor.getGreen() * sin * 255))), (int) (sin * ((sunset_color.getBlue() * (1 - sin) * 255) + (suncolor.getBlue() * sin * 255)))));
                context.context.getWindow().getGameSubscene().setFill(Color.rgb((int) ((sunset_color.getRed() * (1 - sin) * 255) + (dayskycolor.getRed() * sin * 255)), (int) ((sunset_color.getGreen() * (1 - sin) * 255) + (dayskycolor.getGreen() * sin * 255)), (int) ((sunset_color.getBlue() * (1 - sin) * 255) + (dayskycolor.getBlue() * sin * 255))));
            } else {
                sunlight.setColor(sky_color);
                context.context.getWindow().getGameSubscene().setFill(sky_color);
            }
        } else {
            sunlight.setColor(Color.rgb(0, 0, 0));
        }


        // position the sun relative to the player's position
        // the sun is a full 180 degrees (pi) away from the moon, so the sin and cos values are flipped on the sun compared to the moon
        sunlight.setTranslateX(context.context.getComponents().getPlayer().getPositionX());
        sunlight.setTranslateY(sindist);
        sunlight.setTranslateZ(cosdist + context.context.getComponents().getPlayer().getPositionZ());
        sun.setTranslateX(context.context.getComponents().getPlayer().getPositionX());
        sun.setTranslateY(sindist);
        sun.setTranslateZ(cosdist + context.context.getComponents().getPlayer().getPositionZ());


        sun_rotate.setAngle(sun_rotate.getAngle() + sun_rotation_speed);
    }

    private void rotateClouds() {
        clouds.setTranslateX(context.context.getComponents().getPlayer().getPositionX());
        clouds.setTranslateZ(context.context.getComponents().getPlayer().getPositionZ());

        clouds_rotate_z.setAngle(clouds_rotate_z.getAngle() + clouds_rotate_speed);
        clouds_rotate_y.setAngle(clouds_rotate_y.getAngle() + clouds_rotate_speed);
    }

    private void rotateBigStar(double time, double dist) {
        double sin = Math.sin(time);
        double sindist = sin * dist;
        double cos = Math.cos(time);
        double cosdist = cos * dist;
        big_star.setTranslateX(sindist + context.context.getComponents().getPlayer().getPositionX());
        big_star.setTranslateY(sindist);
        big_star.setTranslateZ(cosdist + context.context.getComponents().getPlayer().getPositionZ());
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
            if (sky_color == null) {
                ambient.setColor(Color.rgb((int)(defaultAmbientColor.getRed() * 255 - 80 * sin),(int)(defaultAmbientColor.getGreen()*255 - 80 * sin),(int)(defaultAmbientColor.getBlue()*255 - 80 * sin)));
                moonlight.setColor(Color.rgb((int) (sin * ((sunset_color.getRed() * (1 - sin) * 255) + (mooncolor.getRed() * sin * 255))), (int) (sin * ((sunset_color.getGreen() * (1 - sin) * 255) + (mooncolor.getGreen() * sin * 255))), (int) (sin * (sunset_color.getBlue() * (1 - sin) * 255) + (mooncolor.getBlue() * sin * 255))));
                context.context.getWindow().getGameSubscene().setFill(Color.rgb((int) ((sunset_color.getRed() * (1 - sin) * 255) + (nightskycolor.getRed() * sin * 255)), (int) ((sunset_color.getGreen() * (1 - sin) * 255) + (nightskycolor.getGreen() * sin * 255)), (int) ((sunset_color.getBlue() * (1 - sin) * 255) + (nightskycolor.getBlue() * sin * 255))));
            } else {
                sunlight.setColor(sky_color);
                context.context.getWindow().getGameSubscene().setFill(sky_color);
            }

        } else {
            moonlight.setColor(Color.rgb(0, 0, 0));
        }

        // position the moon relative to the player's position
        // the moon is a full 180 degrees (pi) away from the sun, so the sin and cos values are flipped on the moon compared to the sun
        moonlight.setTranslateX(context.context.getComponents().getPlayer().getPositionX());
        moonlight.setTranslateY(-sindist);
        moonlight.setTranslateZ(-cosdist + context.context.getComponents().getPlayer().getPositionZ());
        moon.setTranslateY(-sindist);
        moon.setTranslateX(context.context.getComponents().getPlayer().getPositionX());
        moon.setTranslateZ(-cosdist + context.context.getComponents().getPlayer().getPositionZ());

        moon_rotate.setAngle(sun_rotate.getAngle() + moon_rotation_speed);

    }

    public double getSunScale() {
        return sun.getScaleX();
    }

    public void setSunScale(double scale) {
        sun.setScaleX(scale);
        sun.setScaleY(scale);
        sun.setScaleZ(scale);
    }

    public double getMoonScale() {
        return moon.getScaleX();
    }

    public void setMoonScale(double scale) {
        moon.setScaleX(scale);
        moon.setScaleY(scale);
        moon.setScaleZ(scale);
    }

    public double getBigStarScale() {
        return big_star.getScaleX();
    }

    public void setBigStarScale(double scale) {
        big_star.setScaleX(scale);
        big_star.setScaleY(scale);
        big_star.setScaleZ(scale);
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

    public void setSunDistance(double dist) {
        sun_distance = clouds_height + dist;
    }

    public double getMoonDistance() {
        return moon_distance;
    }

    public void setMoonDistance(double dist) {
        moon_distance = clouds_height + dist;
    }

    public double getBigStarDistance() {
        return big_star_distance;
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
        try {
            if (num > 0) {
                sun_moon_period_multiplier = num;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public int getBig_planet_period_multiplier() {
        return big_planet_period_multiplier;
    }

    public void setBig_planet_period_multiplier(int num) {
        try {
            if (num > 0) {
                big_planet_period_multiplier = num;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
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
        try {
            if (t >= -1) {
                fixed_time = t;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    } // -1 uses System Time. Any other value locks it to a specific time

    public Group getGroup() {
        return group_skybox;
    }

    public int getMode() {
        return MODE_CURR;
    }

    public void setMode(int mode_new) {
        MODE_CURR = mode_new;
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

    // these functions are used to set the current status of the clouds
    public boolean getCloudsVisibile() {
        return clouds.getOpacity() == 1;
    }

    public void setCloudsVisible(boolean val) {
        if (val) {
            clouds.setOpacity(1);
        } else {
            clouds.setOpacity(0);
        }
    }


    // these functions are used to globally enable or disable clouds;
    public boolean getShouldHaveClouds() {
        return clouds_enabled;
    }

    public void setShouldHaveClouds(boolean val) {
        setCloudsVisible(val);
        clouds_enabled = val;
    }


    // these functions are used to set the current status of the planets
    public boolean getPlanetsVisible() {
        return sun.getOpacity() == 1;
    }

    public void setPlanetsVisible(boolean val) {
        if (val) {
            sun.setOpacity(1);
            moon.setOpacity(1);
            big_star.setOpacity(1);

        } else {
            sun.setOpacity(0);
            moon.setOpacity(0);
            big_star.setOpacity(0);

        }
    }

    // these functions are used to globally enable or disable planets;
    public boolean getShouldHavePlanets() {
        return planets_enabled;
    }

    public void setShouldHavePlanets(boolean val) {
        setPlanetsVisible(val);
        planets_enabled = val;
    }

    public Color getDefaultAmbientColor() {
        return defaultAmbientColor;
    }

    public void setDefaultAmbientColor(Color defaultAmbientColor) {
        this.defaultAmbientColor = defaultAmbientColor;
    }
}
