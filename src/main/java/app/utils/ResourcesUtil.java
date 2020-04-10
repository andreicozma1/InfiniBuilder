package app.utils;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;

import java.util.*;

public class ResourcesUtil {
    private static final String TAG = "ResourcesUtil";

    public static List<String> world_types_sorted;
    public static PhongMaterial stone;
    public static PhongMaterial grass;
    public static PhongMaterial dirt;
    public static PhongMaterial moss;
    public static PhongMaterial sand;
    public static PhongMaterial blue;
    public static PhongMaterial green;
    public static PhongMaterial red;
    public static PhongMaterial purple;
    public static PhongMaterial sun;
    public static PhongMaterial moon;
    public static PhongMaterial big_star;
    public static PhongMaterial clouds;
    public static PhongMaterial water;
    public static PhongMaterial metal;
    public static Map<String, Material> world_types = new HashMap<String, Material>() {
        {
            put("Default", null);
            put("Grass", grass);
            put("Dirt", dirt);
            put("Moss", moss);
            put("Sand", sand);
            put("Metal", metal);
            put("Water", water);
            put("Lava", sun);
            put("Moon Rock", moon);
        }
    };
    Application context;

    public ResourcesUtil(Application app) {
        Log.p(TAG, "CONSTRUCTOR");

        context = app;
        setupMaterials();
        world_types_sorted = new ArrayList<>(world_types.keySet());
        Collections.sort(world_types_sorted);
    }

    public void setupMaterials() {
        stone = new PhongMaterial();
        stone.setDiffuseMap(getImage("/textures/stone.jpg"));

        grass = new PhongMaterial();
        grass.setDiffuseMap(getImage("/textures/grass/grass01.jpg"));
        grass.setSpecularMap(getImage("/textures/grass/grass01_s.jpg"));
        grass.setBumpMap(getImage("/textures/grass/grass01_n.jpg"));

        dirt = new PhongMaterial();
        dirt.setDiffuseMap(getImage("/textures/dirt.jpg"));

        moss = new PhongMaterial();
        moss.setDiffuseMap(getImage("/textures/moss.jpg"));

        sand = new PhongMaterial();
        sand.setDiffuseMap(getImage("/textures/sand.jpg"));

        metal = new PhongMaterial();
        metal.setDiffuseMap(getImage("/textures/metal.jpg"));
        metal.setSpecularMap(getImage("/textures/metal.jpg"));

        water = new PhongMaterial();
        water.setDiffuseMap(getImage("/textures/water.jpg"));

        // THESE STAY THE SAME

        clouds = new PhongMaterial();
        clouds.setDiffuseMap(getImage("/textures/clouds.png"));

        sun = new PhongMaterial();
        sun.setDiffuseMap(getImage("/textures/sun.jpg"));
        sun.setSelfIlluminationMap(getImage("/textures/sun.jpg"));
        sun.setSpecularMap(getImage("/textures/sun.jpg"));
        sun.setSpecularPower(1);

        moon = new PhongMaterial();
        moon.setDiffuseMap(getImage("/textures/moon.jpg"));
        moon.setSelfIlluminationMap(getImage("/textures/moon.jpg"));

        big_star = new PhongMaterial();
        big_star.setDiffuseMap(getImage("/textures/planet.png"));
        big_star.setSelfIlluminationMap(getImage("/textures/planet.png"));

        blue = new PhongMaterial();
        blue.setDiffuseColor(Color.BLUE);
        blue.setSpecularColor(Color.BLUE);


        green = new PhongMaterial();
        green.setDiffuseColor(Color.GREEN);
        green.setSpecularColor(Color.GREEN);

        red = new PhongMaterial();
        red.setDiffuseColor(Color.RED);
        red.setSpecularColor(Color.RED);

        purple = new PhongMaterial();
        purple.setDiffuseColor(Color.PURPLE);
        purple.setSpecularColor(Color.PURPLE);
    }


    public Image getImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }
}
