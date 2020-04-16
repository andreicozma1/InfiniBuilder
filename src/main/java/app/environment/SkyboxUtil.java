package app.environment;

import app.GameBuilder;
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
    private final Group GROUP_SKYBOX;
    private final double SUN_OFFSET_RATIO = 0; // value between -1 and 1 (shifts sin up)
    private final double BIG_STAR_ROTATE_SPEED = .005;
    //TODO? Put these in classes?
    private final Sphere SUN_OBJECT;
    private final Rotate SUN_ROTATE;
    private final PointLight SUN_LIGHT;
    //TODO? Put these in classes?
    private final Sphere MOON_OBJECT;
    private final Rotate MOON_ROTATE;
    private final PointLight MOON_LIGHT;
    private final Sphere BIG_STAR_OBJECT;
    private final Rotate BIG_STAR_ROTATE;
    private final Sphere CLOUDS_OBJECT;
    private final double CLOUDS_ROTATE_SPEED;
    private final Rotate CLOUDS_ROTATE_X;
    private final Rotate CLOUDS_ROTATE_Y;
    private final Rotate CLOUDS_ROTATE_Z;
    private final double CLOUDS_HEIGHT;
    public static double PROPERTY_PLANET_DIAMETER = 8000;
    public Color SKY_COLOR = null;
    Color SUN_COLOR;
    Color SKY_COLOR_DAY;
    Color SKY_COLOR_SUNSET;
    Color MOON_COLOR;
    Color SKY_COLOR_NIGHT;
    private AmbientLight AMBIENT_LIGHT = null;
    private int SUN_MOON_PERIOD_MULTIPLIER = 128;
    private int BIG_PLANET_PERIOD_MULTIPLIER = 128;
    private double PROPERTY_SUN_ROTATION_SPD = .05;
    private double PROPERTY_MOON_ROTATION_SPD = .05;
    private int MODE_CURR;
    private double SUN_DISTANCE;
    private double MOON_DISTANCE;
    private double BIG_STAR_DISTANCE;


    private double PROPERTY_FIXED_TIME = -1; // set as default to -1 to track whether the user set it manually
    private boolean PROPERTY_CLOUDS_ENABLED;
    private boolean PROPERTY_PLANETS_ENABLED;

    Color AMBIENT_LIGHT_COLOR_DEFAULT;

    /**
     * Constructor for SkyboxUtil. This initializes all of our sky objects,
     * such as the sun, moon, big star, and clouds, as well as set their default values.
     *
     * @param envir
     */
    public SkyboxUtil(EnvironmentUtil envir) {
        Log.d(TAG, "CONSTRUCTOR");

        context = envir;
        GROUP_SKYBOX = new Group();
        // Sets up the default mode to cycle, which is the normal behavior
        MODE_CURR = MODE_CYCLE;

        // Set up the ambient light, which is the illumination to the world at all times
        setDefaultAmbientColor(Color.rgb(100,100,100));
        AmbientLight amb = new AmbientLight();
        amb.setColor(AMBIENT_LIGHT_COLOR_DEFAULT);
        setAmbientLight(amb);

        SKY_COLOR_SUNSET = Color.rgb(253, 94, 83);

        // Set up everything related to the clouds
        CLOUDS_HEIGHT = 800;
        CLOUDS_OBJECT = new Sphere();
        CLOUDS_OBJECT.setCullFace(CullFace.NONE);
        CLOUDS_OBJECT.setMaterial(ResourcesUtil.clouds);
        CLOUDS_ROTATE_SPEED = 0.001;
        CLOUDS_OBJECT.setTranslateY(PROPERTY_PLANET_DIAMETER - CLOUDS_HEIGHT);
        CLOUDS_OBJECT.setScaleX(PROPERTY_PLANET_DIAMETER + CLOUDS_HEIGHT);
        CLOUDS_OBJECT.setScaleY(PROPERTY_PLANET_DIAMETER + CLOUDS_HEIGHT);
        CLOUDS_OBJECT.setScaleZ(PROPERTY_PLANET_DIAMETER + CLOUDS_HEIGHT);
        CLOUDS_OBJECT.setEffect(new GaussianBlur(5));

        CLOUDS_ROTATE_X = new Rotate(90, new Point3D(1, 0, 0));
        CLOUDS_ROTATE_Y = new Rotate(0, new Point3D(0, 1, 0));
        CLOUDS_ROTATE_Z = new Rotate(0, new Point3D(0, 0, 1));
        CLOUDS_OBJECT.getTransforms().setAll(CLOUDS_ROTATE_X, CLOUDS_ROTATE_Y, CLOUDS_ROTATE_Z);
        setShouldHaveClouds(true);

        // set up everything related to the big star
        BIG_STAR_OBJECT = new Sphere();
        BIG_STAR_OBJECT.setMaterial(ResourcesUtil.big_star);
        setBigStarScale(4000);
        setBigStarDistance(PROPERTY_PLANET_DIAMETER + 4000);
        BIG_STAR_ROTATE = new Rotate(0, new Point3D(0, 1, 0));
        BIG_STAR_OBJECT.getTransforms().setAll(BIG_STAR_ROTATE);

        // set up everything related to the moon
        MOON_OBJECT = new Sphere();
        MOON_LIGHT = new PointLight();
        MOON_LIGHT.setDepthTest(DepthTest.ENABLE);
        setMoonScale(1000);
        setMoonDistance(PROPERTY_PLANET_DIAMETER + 3000);
        setMoonMaterial(ResourcesUtil.moon);
        setMoonlightColor(Color.rgb(20, 20, 60));
        setNightSkyColor(Color.rgb(8, 8, 30));
        MOON_ROTATE = new Rotate(0, new Point3D(0, 1, 0));
        MOON_OBJECT.getTransforms().setAll(MOON_ROTATE);

        // set up everything related to the sun
        SUN_OBJECT = new Sphere();
        SUN_LIGHT = new PointLight();
        SUN_LIGHT.setDepthTest(DepthTest.ENABLE);
        setSunScale(2000);
        setSunDistance(PROPERTY_PLANET_DIAMETER + 8000);
        setSunMaterial(ResourcesUtil.sun);
        setSunlightColor(Color.WHITE);
        setDaySkyColor(Color.rgb(135, 206, 235));
        SUN_ROTATE = new Rotate(0, new Point3D(0, 1, 0));
        SUN_OBJECT.getTransforms().setAll(SUN_ROTATE);

        // set up default setting - show planets
        setShouldHavePlanets(true);

        // add all the sky structure
        GROUP_SKYBOX.getChildren().addAll(SUN_OBJECT, SUN_LIGHT, MOON_OBJECT, MOON_LIGHT, BIG_STAR_OBJECT, CLOUDS_OBJECT);
    }

    void update_handler() {
        double game_time;
        switch (MODE_CURR) {
            case MODE_DAY:
                game_time = -Math.PI / 2 / 6.5 * SUN_MOON_PERIOD_MULTIPLIER;
                break;
            case MODE_NIGHT:
                game_time = Math.PI / 2 / 6.5 * SUN_MOON_PERIOD_MULTIPLIER;
                break;
            default:
                if (PROPERTY_FIXED_TIME == -1) {
                    game_time = GameBuilder.time_current / (1000.0);
                } else {
                    game_time = PROPERTY_FIXED_TIME;
                }
                break;
        }

        rotateSun(game_time / SUN_MOON_PERIOD_MULTIPLIER * 6.5, SUN_DISTANCE);
        rotateMoon(game_time / SUN_MOON_PERIOD_MULTIPLIER * 6.5, MOON_DISTANCE);
        rotateBigStar(game_time / BIG_PLANET_PERIOD_MULTIPLIER * 6.5, BIG_STAR_DISTANCE);
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
        if (sin <= SUN_OFFSET_RATIO) {
            sin = sin * -1;
            sin += SUN_OFFSET_RATIO;
            // flip the values of the sin and add .5 to it for the offset, now the values will be from 0 to 1.5.
            // Since we use the sin to calculate the intensity of the colors, we want the multiplier to be capped at 1
            // so if the sin > 1, set it back to 1.
            // this way, during mid day, the intensity of the sun remains constant for longer, before slowly fading to black.
            if (sin > 1) {
                sin = 1;
            }
            if (SKY_COLOR == null) {
                AMBIENT_LIGHT.setColor(Color.rgb((int)(AMBIENT_LIGHT_COLOR_DEFAULT.getRed() * 255),(int)(AMBIENT_LIGHT_COLOR_DEFAULT.getGreen()*255),(int)(AMBIENT_LIGHT_COLOR_DEFAULT.getBlue()*255)));
                SUN_LIGHT.setColor(Color.rgb((int) (sin * ((SKY_COLOR_SUNSET.getRed() * (1 - sin) * 255) + (SUN_COLOR.getRed() * sin * 255))), (int) (sin * ((SKY_COLOR_SUNSET.getGreen() * (1 - sin) * 255) + (SUN_COLOR.getGreen() * sin * 255))), (int) (sin * ((SKY_COLOR_SUNSET.getBlue() * (1 - sin) * 255) + (SUN_COLOR.getBlue() * sin * 255)))));
                context.context.getWindow().getGameSubscene().setFill(Color.rgb((int) ((SKY_COLOR_SUNSET.getRed() * (1 - sin) * 255) + (SKY_COLOR_DAY.getRed() * sin * 255)), (int) ((SKY_COLOR_SUNSET.getGreen() * (1 - sin) * 255) + (SKY_COLOR_DAY.getGreen() * sin * 255)), (int) ((SKY_COLOR_SUNSET.getBlue() * (1 - sin) * 255) + (SKY_COLOR_DAY.getBlue() * sin * 255))));
            } else {
                SUN_LIGHT.setColor(SKY_COLOR);
                context.context.getWindow().getGameSubscene().setFill(SKY_COLOR);
            }
        } else {
            SUN_LIGHT.setColor(Color.rgb(0, 0, 0));
        }


        // position the sun relative to the player's position
        // the sun is a full 180 degrees (pi) away from the moon, so the sin and cos values are flipped on the sun compared to the moon
        SUN_LIGHT.setTranslateX(context.context.getComponents().getPlayer().getPositionX());
        SUN_LIGHT.setTranslateY(sindist);
        SUN_LIGHT.setTranslateZ(cosdist + context.context.getComponents().getPlayer().getPositionZ());
        SUN_OBJECT.setTranslateX(context.context.getComponents().getPlayer().getPositionX());
        SUN_OBJECT.setTranslateY(sindist);
        SUN_OBJECT.setTranslateZ(cosdist + context.context.getComponents().getPlayer().getPositionZ());

        SUN_ROTATE.setAngle(SUN_ROTATE.getAngle() + PROPERTY_SUN_ROTATION_SPD);
    }

    private void rotateClouds() {
        CLOUDS_OBJECT.setTranslateX(context.context.getComponents().getPlayer().getPositionX());
        CLOUDS_OBJECT.setTranslateZ(context.context.getComponents().getPlayer().getPositionZ());

        CLOUDS_ROTATE_Z.setAngle(CLOUDS_ROTATE_Z.getAngle() + CLOUDS_ROTATE_SPEED);
        CLOUDS_ROTATE_Y.setAngle(CLOUDS_ROTATE_Y.getAngle() + CLOUDS_ROTATE_SPEED);
    }

    private void rotateBigStar(double time, double dist) {
        double sin = Math.sin(time);
        double sindist = sin * dist;
        double cos = Math.cos(time);
        double cosdist = cos * dist;
        BIG_STAR_OBJECT.setTranslateX(sindist + context.context.getComponents().getPlayer().getPositionX());
        BIG_STAR_OBJECT.setTranslateY(sindist);
        BIG_STAR_OBJECT.setTranslateZ(cosdist + context.context.getComponents().getPlayer().getPositionZ());
        BIG_STAR_ROTATE.setAngle(BIG_STAR_ROTATE.getAngle() + BIG_STAR_ROTATE_SPEED);
    }


    private void rotateMoon(double time, double dist) {
        double sin = Math.sin(time);
        double sindist = sin * dist;
        double cos = Math.cos(time);
        double cosdist = cos * dist;

        // complement to the offset set on the sun.
        // this way, we check if the sun has already set, if so, we subtract the multiplier from the sun and multiply by 2
        // to achieve a value from 0 to 1 which we will use for calculating the intensity of the moon light
        if (sin >= SUN_OFFSET_RATIO) {
            sin -= SUN_OFFSET_RATIO;
            if (SKY_COLOR == null) {
                AMBIENT_LIGHT.setColor(Color.rgb((int)(AMBIENT_LIGHT_COLOR_DEFAULT.getRed() * 255 - 60 * sin),(int)(AMBIENT_LIGHT_COLOR_DEFAULT.getGreen()*255 - 60 * sin),(int)(AMBIENT_LIGHT_COLOR_DEFAULT.getBlue()*255 - 60 * sin)));
                MOON_LIGHT.setColor(Color.rgb((int) (sin * ((SKY_COLOR_SUNSET.getRed() * (1 - sin) * 255) + (MOON_COLOR.getRed() * sin * 255))), (int) (sin * ((SKY_COLOR_SUNSET.getGreen() * (1 - sin) * 255) + (MOON_COLOR.getGreen() * sin * 255))), (int) (sin * (SKY_COLOR_SUNSET.getBlue() * (1 - sin) * 255) + (MOON_COLOR.getBlue() * sin * 255))));
                context.context.getWindow().getGameSubscene().setFill(Color.rgb((int) ((SKY_COLOR_SUNSET.getRed() * (1 - sin) * 255) + (SKY_COLOR_NIGHT.getRed() * sin * 255)), (int) ((SKY_COLOR_SUNSET.getGreen() * (1 - sin) * 255) + (SKY_COLOR_NIGHT.getGreen() * sin * 255)), (int) ((SKY_COLOR_SUNSET.getBlue() * (1 - sin) * 255) + (SKY_COLOR_NIGHT.getBlue() * sin * 255))));
            } else {
                SUN_LIGHT.setColor(SKY_COLOR);
                context.context.getWindow().getGameSubscene().setFill(SKY_COLOR);
            }

        } else {
            MOON_LIGHT.setColor(Color.rgb(0, 0, 0));
        }

        // position the moon relative to the player's position
        // the moon is a full 180 degrees (pi) away from the sun, so the sin and cos values are flipped on the moon compared to the sun
        MOON_LIGHT.setTranslateX(context.context.getComponents().getPlayer().getPositionX());
        MOON_LIGHT.setTranslateY(-sindist);
        MOON_LIGHT.setTranslateZ(-cosdist + context.context.getComponents().getPlayer().getPositionZ());
        MOON_OBJECT.setTranslateY(-sindist);
        MOON_OBJECT.setTranslateX(context.context.getComponents().getPlayer().getPositionX());
        MOON_OBJECT.setTranslateZ(-cosdist + context.context.getComponents().getPlayer().getPositionZ());

        MOON_ROTATE.setAngle(SUN_ROTATE.getAngle() + PROPERTY_MOON_ROTATION_SPD);

    }

    public double getSunScale() {
        return SUN_OBJECT.getScaleX();
    }

    public void setSunScale(double scale) {
        SUN_OBJECT.setScaleX(scale);
        SUN_OBJECT.setScaleY(scale);
        SUN_OBJECT.setScaleZ(scale);
    }

    public double getMoonScale() {
        return MOON_OBJECT.getScaleX();
    }

    public void setMoonScale(double scale) {
        MOON_OBJECT.setScaleX(scale);
        MOON_OBJECT.setScaleY(scale);
        MOON_OBJECT.setScaleZ(scale);
    }

    public double getBigStarScale() {
        return BIG_STAR_OBJECT.getScaleX();
    }

    public void setBigStarScale(double scale) {
        BIG_STAR_OBJECT.setScaleX(scale);
        BIG_STAR_OBJECT.setScaleY(scale);
        BIG_STAR_OBJECT.setScaleZ(scale);
    }

    public void setSunMaterial(PhongMaterial mat) {
        SUN_OBJECT.setMaterial(mat);
    }

    public void setMoonMaterial(PhongMaterial mat) {
        MOON_OBJECT.setMaterial(mat);
    }

    public void setSunlightColor(Color clr) {
        SUN_COLOR = clr;
    }

    public void setMoonlightColor(Color clr) {
        MOON_COLOR = clr;
    }

    public void setDaySkyColor(Color cr) {
        SKY_COLOR_DAY = cr;
    }

    public void setNightSkyColor(Color cr) {
        SKY_COLOR_NIGHT = cr;
    }

    public double getSunDistance() {
        return SUN_DISTANCE;
    }

    public void setSunDistance(double dist) {
        SUN_DISTANCE = CLOUDS_HEIGHT + dist;
    }

    public double getMoonDistance() {
        return MOON_DISTANCE;
    }

    public void setMoonDistance(double dist) {
        MOON_DISTANCE = CLOUDS_HEIGHT + dist;
    }

    public double getBigStarDistance() {
        return BIG_STAR_DISTANCE;
    }

    public void setBigStarDistance(double dist) {
        BIG_STAR_DISTANCE = CLOUDS_HEIGHT + dist;
    }


    public void setAmbientLight(AmbientLight node) {
        AMBIENT_LIGHT = node;
        context.getWorldGroup().getChildren().add(AMBIENT_LIGHT);
    }

    public void resetLighting() {
        if (context.getWorldGroup().getChildren().contains(AMBIENT_LIGHT)) {
            context.getWorldGroup().getChildren().remove(AMBIENT_LIGHT);
            context.getWorldGroup().getChildren().add(AMBIENT_LIGHT);
        }
    }

    public int getSun_moon_period_multiplier() {
        return SUN_MOON_PERIOD_MULTIPLIER;
    }

    public void setSun_moon_period_multiplier(int num) {
        try {
            if (num > 0) {
                SUN_MOON_PERIOD_MULTIPLIER = num;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public int getBig_planet_period_multiplier() {
        return BIG_PLANET_PERIOD_MULTIPLIER;
    }

    public void setBig_planet_period_multiplier(int num) {
        try {
            if (num > 0) {
                BIG_PLANET_PERIOD_MULTIPLIER = num;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void setSun_rotation_speed(double s) {
        PROPERTY_SUN_ROTATION_SPD = s;
    }

    public void setMoon_rotation_speed(double s) {
        PROPERTY_MOON_ROTATION_SPD = s;
    }

    public double getFixedTime() {
        return PROPERTY_FIXED_TIME;
    }

    public void setFixedTime(double t) {
        try {
            if (t >= -1) {
                PROPERTY_FIXED_TIME = t;
            } else {
                throw new IndexOutOfBoundsException();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    } // -1 uses System Time. Any other value locks it to a specific time

    public Group getGroup() {
        return GROUP_SKYBOX;
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
        return CLOUDS_OBJECT.getOpacity() == 1;
    }

    public void setCloudsVisible(boolean val) {
        if (val) {
            CLOUDS_OBJECT.setOpacity(1);
        } else {
            CLOUDS_OBJECT.setOpacity(0);
        }
    }


    // these functions are used to globally enable or disable clouds;
    public boolean getShouldHaveClouds() {
        return PROPERTY_CLOUDS_ENABLED;
    }

    public void setShouldHaveClouds(boolean val) {
        setCloudsVisible(val);
        PROPERTY_CLOUDS_ENABLED = val;
    }


    // these functions are used to set the current status of the planets
    public boolean getPlanetsVisible() {
        return SUN_OBJECT.getOpacity() == 1;
    }

    public void setPlanetsVisible(boolean val) {
        if (val) {
            SUN_OBJECT.setOpacity(1);
            MOON_OBJECT.setOpacity(1);
            BIG_STAR_OBJECT.setOpacity(1);

        } else {
            SUN_OBJECT.setOpacity(0);
            MOON_OBJECT.setOpacity(0);
            BIG_STAR_OBJECT.setOpacity(0);

        }
    }

    // these functions are used to globally enable or disable planets;
    public boolean getShouldHavePlanets() {
        return PROPERTY_PLANETS_ENABLED;
    }

    public void setShouldHavePlanets(boolean val) {
        setPlanetsVisible(val);
        PROPERTY_PLANETS_ENABLED = val;
    }

    public Color getDefaultAmbientColor() {
        return AMBIENT_LIGHT_COLOR_DEFAULT;
    }

    public void setDefaultAmbientColor(Color defaultAmbientColor) {
        this.AMBIENT_LIGHT_COLOR_DEFAULT = defaultAmbientColor;
    }
}
