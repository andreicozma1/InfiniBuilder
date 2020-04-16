package app.structures.spawnables.utils;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.*;

public class ResourcesUtil {
    private static final String TAG = "ResourcesUtil";

    public static PhongMaterial asphalt_01;
    public static PhongMaterial brick_01;
    public static PhongMaterial dirt_01;
    public static PhongMaterial grass_01;
    public static PhongMaterial grass_02;
    public static PhongMaterial grass_03;
    public static PhongMaterial grass_04;
    public static PhongMaterial ice_01;
    public static PhongMaterial ice_02;
    public static PhongMaterial lava_01;
    public static PhongMaterial metal_01;
    public static PhongMaterial metal_02;
    public static PhongMaterial mud_01;
    public static PhongMaterial pebbles_01;
    public static PhongMaterial rock_01;
    public static PhongMaterial rock_02;
    public static PhongMaterial sand_01;
    public static PhongMaterial sand_02;
    public static PhongMaterial sand_03;
    public static PhongMaterial snow_01;
    public static PhongMaterial stone_path_01;
    public static PhongMaterial stone_path_02;
    public static PhongMaterial stone_wall_01;
    public static PhongMaterial stone_wall_02;
    public static PhongMaterial water_01;
    public static PhongMaterial wood_bark_01;
    public static PhongMaterial wood_bark_02;
    public static PhongMaterial wood_bark_03;
    public static PhongMaterial wood_planks_01;
    public static PhongMaterial wood_planks_02;
    public static PhongMaterial wood_planks_03;
    public static PhongMaterial wood_planks_04;

    public static PhongMaterial stone;
    public static PhongMaterial moss;
    public static PhongMaterial sand;
    public static PhongMaterial dirt;
    public static PhongMaterial blue;
    public static PhongMaterial green;
    public static PhongMaterial red;
    public static PhongMaterial black;
    public static PhongMaterial purple;
    public static PhongMaterial sun;
    public static PhongMaterial moon;
    public static PhongMaterial big_star;
    public static PhongMaterial clouds;
    public static PhongMaterial water;
    public static PhongMaterial metal;
    public static PhongMaterial grass;
    public static Map<String, PhongMaterial> MAP_ALL_MATERIALS;
    public static List<String> MAP_ALL_MATERIALS_SORTED;
    Application context;

    // TODO - Rewrite this class so that all textures are automatically read in to a hash-map upon load of the game

    /**
     * This class is responsible for reading in all of our textures and saving them into a map structure
     * in order to use them as materials
     * @param app
     */
    public ResourcesUtil(Application app) {
        Log.d(TAG, "CONSTRUCTOR");

        context = app;
        setupMaterials();
        // after reading in all the materials, add them to a map
        MAP_ALL_MATERIALS = new HashMap<>() {
            {
                put("Default", null);

                put("asphalt_01", asphalt_01);
                put("brick_01", brick_01);

                put("mud_01", mud_01);
                put("dirt_01", dirt_01);
                put("moss", moss);
                put("grass", grass);
                put("grass_01", grass_01);
                put("grass_02", grass_02);
                put("grass_03", grass_03);
                put("grass_04", grass_04);

                put("snow_01", snow_01);
                put("ice_01", ice_01);
                put("ice_02", ice_02);
                put("lava_01", lava_01);

                put("metal", metal);
                put("metal_01", metal_01);
                put("metal_02", metal_02);

                put("pebbles_01", pebbles_01);
                put("rock_01", rock_01);
                put("rock_02", rock_02);

                put("sand", sand);
                put("sand_01", sand_01);
                put("sand_02", sand_02);
                put("sand_03", sand_03);

                put("stone", stone);
                put("stone_path_01", stone_path_01);
                put("stone_path_02", stone_path_02);
                put("stone_wall_01", stone_wall_01);
                put("stone_wall_02", stone_wall_02);

                put("water", water);
                put("water_01", water_01);

                put("wood_bark_01", wood_bark_01);
                put("wood_bark_02", wood_bark_02);
                put("wood_bark_03", wood_bark_03);

                put("wood_planks_01", wood_planks_01);
                put("wood_planks_02", wood_planks_02);
                put("wood_planks_03", wood_planks_03);
                put("wood_planks_04", wood_planks_04);

                put("dirt", dirt);
                put("blue", blue);
                put("green", green);
                put("red", red);
                put("black", black);
                put("purple", purple);

                put("sun", sun);
                put("moon", moon);
                put("big_star", big_star);

                put("clouds", clouds);

            }
        };
        Log.d(TAG, "Created MAP_ALL_MATERIALS");
        MAP_ALL_MATERIALS_SORTED = new ArrayList<>(MAP_ALL_MATERIALS.keySet());
        // then also sort them alphabetically
        Collections.sort(MAP_ALL_MATERIALS_SORTED);
        Log.d(TAG, "Sorted MAP_ALL_MATERIALS_SORTED");

    }

    public void setupMaterials() {
        Log.d(TAG, "setupMaterials()");

        asphalt_01 = new PhongMaterial();
        asphalt_01.setDiffuseMap(getImage("/textures/asphalt_01/asphalt01.jpg"));
//        asphalt_01.setBumpMap(getImage("/textures/asphalt_01/asphalt01_n.jpg"));

        brick_01 = new PhongMaterial();
        brick_01.setDiffuseMap(getImage("/textures/brick_01/brick01.jpg"));
//        brick_01.setBumpMap(getImage("/textures/brick_01/brick01_n.jpg"));

        dirt_01 = new PhongMaterial();
        dirt_01.setDiffuseMap(getImage("/textures/dirt_01/dirt01.jpg"));
//        dirt_01.setBumpMap(getImage("/textures/dirt_01/dirt01_n.jpg"));

        grass_01 = new PhongMaterial();
        grass_01.setDiffuseMap(getImage("/textures/grass_01/grass01.jpg"));
//        grass_01.setSpecularMap(getImage("/textures/grass_01/grass01_s.jpg"));
//        grass_01.setBumpMap(getImage("/textures/grass_01/grass01_n.jpg"));

        grass_02 = new PhongMaterial();
        grass_02.setDiffuseMap(getImage("/textures/grass_02/grass02.jpg"));
//        grass_02.setSpecularMap(getImage("/textures/grass_02/grass02_s.jpg"));
//        grass_02.setBumpMap(getImage("/textures/grass_02/grass02_n.jpg"));

        grass_03 = new PhongMaterial();
        grass_03.setDiffuseMap(getImage("/textures/grass_03/grass03.jpg"));
//        grass_03.setSpecularMap(getImage("/textures/grass_03/grass03_s.jpg"));
//        grass_03.setBumpMap(getImage("/textures/grass_03/grass03_n.jpg"));

        grass_04 = new PhongMaterial();
        grass_04.setDiffuseMap(getImage("/textures/grass_04/grass04.jpg"));
//        grass_04.setBumpMap(getImage("/textures/grass_04/grass04_n.jpg"));

        ice_01 = new PhongMaterial();
        ice_01.setDiffuseMap(getImage("/textures/ice_01/ice01.jpg"));
//        ice_01.setSpecularMap(getImage("/textures/ice_01/ice01_s.jpg"));
//        ice_01.setBumpMap(getImage("/textures/ice_01/ice01_n.jpg"));

        ice_02 = new PhongMaterial();
        ice_02.setDiffuseMap(getImage("/textures/ice_02/ice02.jpg"));
//        ice_02.setBumpMap(getImage("/textures/ice_02/ice02_n.jpg"));

        lava_01 = new PhongMaterial();
        lava_01.setDiffuseMap(getImage("/textures/lava_01/lava01.jpg"));
        lava_01.setSelfIlluminationMap(getImage("/textures/lava_01/lava01.jpg"));

//        lava_01.setBumpMap(getImage("/textures/lava_01/lava01_n.jpg"));

        metal_01 = new PhongMaterial();
        metal_01.setDiffuseMap(getImage("/textures/metal_01/metal01.png"));
//        metal_01.setBumpMap(getImage("/textures/metal_01/metal01_n.png"));

        metal_02 = new PhongMaterial();
        metal_02.setDiffuseMap(getImage("/textures/metal_02/metal02.png"));
//        metal_02.setBumpMap(getImage("/textures/metal_02/metal02_n.jpg"));

        mud_01 = new PhongMaterial();
        mud_01.setDiffuseMap(getImage("/textures/mud_01/mud01.jpg"));
//        mud_01.setBumpMap(getImage("/textures/mud_01/mud01_n.jpg"));

        pebbles_01 = new PhongMaterial();
        pebbles_01.setDiffuseMap(getImage("/textures/pebbles_01/pebbles01.jpg"));
//        pebbles_01.setBumpMap(getImage("/textures/pebbles_01/pebbles01_n.jpg"));

        rock_01 = new PhongMaterial();
        rock_01.setDiffuseMap(getImage("/textures/rock_01/rock01.jpg"));
//        rock_01.setBumpMap(getImage("/textures/rock_01/rock01_n.jpg"));

        rock_02 = new PhongMaterial();
        rock_02.setDiffuseMap(getImage("/textures/rock_02/rock02.jpg"));
//        rock_02.setBumpMap(getImage("/textures/rock_02/rock02_n.jpg"));

        sand_01 = new PhongMaterial();
        sand_01.setDiffuseMap(getImage("/textures/sand_01/sand01.png"));
//        sand_01.setSpecularMap(getImage("/textures/sand_01/sand01_s.png"));
//        sand_01.setBumpMap(getImage("/textures/sand_01/sand01_n.png"));

        sand_02 = new PhongMaterial();
        sand_02.setDiffuseMap(getImage("/textures/sand_02/sand02.png"));
//        sand_02.setBumpMap(getImage("/textures/sand_02/sand02_n.png"));

        sand_03 = new PhongMaterial();
        sand_03.setDiffuseMap(getImage("/textures/sand_03/sand03.jpg"));
//        sand_03.setSpecularMap(getImage("/textures/sand_03/sand03_s.jpg"));
//        sand_03.setBumpMap(getImage("/textures/sand_03/sand03_n.jpg"));

        snow_01 = new PhongMaterial();
        snow_01.setDiffuseMap(getImage("/textures/snow_01/snow01.jpg"));
//        snow_01.setBumpMap(getImage("/textures/snow_01/snow01_n.jpg"));

        stone_path_01 = new PhongMaterial();
        stone_path_01.setDiffuseMap(getImage("/textures/stone_path_01/stone_path01.jpg"));
//        stone_path_01.setBumpMap(getImage("/textures/stone_path_01/stone_path01_n.jpg"));

        stone_path_02 = new PhongMaterial();
        stone_path_02.setDiffuseMap(getImage("/textures/stone_path_02/stone_path02.jpg"));
//        stone_path_02.setBumpMap(getImage("/textures/stone_path_02/stone_path02_n.jpg"));

        stone_wall_01 = new PhongMaterial();
        stone_wall_01.setDiffuseMap(getImage("/textures/stone_wall_01/stone_wall01.jpg"));
//        stone_wall_01.setSpecularMap(getImage("/textures/stone_wall_01/stone_wall01_s.jpg"));
//        stone_wall_01.setBumpMap(getImage("/textures/stone_wall_01/stone_wall01_n.jpg"));

        stone_wall_02 = new PhongMaterial();
        stone_wall_02.setDiffuseMap(getImage("/textures/stone_wall_02/stone_wall02.jpg"));
//        stone_wall_02.setSpecularMap(getImage("/textures/stone_wall_02/stone_wall02_s.jpg"));
//        stone_wall_02.setBumpMap(getImage("/textures/stone_wall_02/stone_wall02_n.jpg"));

        water_01 = new PhongMaterial();
        water_01.setDiffuseMap(getImage("/textures/water_01/water01.jpg"));
//        water_01.setSpecularMap(getImage("/textures/water_01/water01_s.jpg"));
//        water_01.setBumpMap(getImage("/textures/water_01/water01_n.jpg"));

        wood_bark_01 = new PhongMaterial();
        wood_bark_01.setDiffuseMap(getImage("/textures/wood_bark_01/wood_bark01.jpg"));
//        wood_bark_01.setBumpMap(getImage("/textures/wood_bark_01/wood_bark01_n.jpg"));

        wood_bark_02 = new PhongMaterial();
        wood_bark_02.setDiffuseMap(getImage("/textures/wood_bark_02/wood_bark02.jpg"));
//        wood_bark_02.setBumpMap(getImage("/textures/wood_bark_02/wood_bark02_n.jpg"));

        wood_bark_03 = new PhongMaterial();
        wood_bark_03.setDiffuseMap(getImage("/textures/wood_bark_03/wood_bark03.jpg"));
//        wood_bark_03.setBumpMap(getImage("/textures/wood_bark_03/wood_bark03_n.jpg"));


        wood_planks_01 = new PhongMaterial();
        wood_planks_01.setDiffuseMap(getImage("/textures/wood_planks_01/wood_planks01.jpg"));
//        wood_planks_01.setSpecularMap(getImage("/textures/wood_planks_01/wood_planks01_s.jpg"));
//        wood_planks_01.setBumpMap(getImage("/textures/wood_planks_01/wood_planks01_n.jpg"));

        wood_planks_02 = new PhongMaterial();
        wood_planks_02.setDiffuseMap(getImage("/textures/wood_planks_02/wood_planks02.jpg"));
//        wood_planks_02.setSpecularMap(getImage("/textures/wood_planks_02/wood_planks02_s.jpg"));
//        wood_planks_02.setBumpMap(getImage("/textures/wood_planks_02/wood_planks02_n.jpg"));

        wood_planks_03 = new PhongMaterial();
        wood_planks_03.setDiffuseMap(getImage("/textures/wood_planks_03/wood_planks03.jpg"));
//        wood_planks_03.setBumpMap(getImage("/textures/wood_planks_03/wood_planks03_n.jpg"));

        wood_planks_04 = new PhongMaterial();
        wood_planks_04.setDiffuseMap(getImage("/textures/wood_planks_04/wood_planks04.jpg"));
//        wood_planks_04.setBumpMap(getImage("/textures/wood_planks_04/wood_planks04_n.jpg"));


        grass = new PhongMaterial();
        grass.setDiffuseMap(getImage("/textures/grass.jpg"));
        stone = new PhongMaterial();
        stone.setDiffuseMap(getImage("/textures/stone.jpg"));
        dirt = new PhongMaterial();
        dirt.setDiffuseMap(getImage("/textures/dirt.jpg"));

        moss = new PhongMaterial();
        moss.setDiffuseMap(getImage("/textures/moss.jpg"));

        sand = new PhongMaterial();
        sand.setDiffuseMap(getImage("/textures/sand.jpg"));

        metal = new PhongMaterial();
        metal.setDiffuseMap(getImage("/textures/metal.jpg"));
//        metal.setSpecularMap(getImage("/textures/metal.jpg"));

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

        black = new PhongMaterial();
        black.setDiffuseColor(Color.BLACK);
        black.setSpecularColor(Color.BLACK);
    }


    public Image getImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }
}
