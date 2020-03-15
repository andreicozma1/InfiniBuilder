package environment;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class MaterialsUtil {

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

    public MaterialsUtil() {
        setupMaterials();
    }

    public static void setupMaterials() {
        stone = new PhongMaterial();
        stone.setDiffuseMap(new Image("https://images.unsplash.com/photo-1522409640003-31c21bd5e696?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2550&q=80"));
//stone.setSpecularMap(new Image("https://images.unsplash.com/photo-1522409640003-31c21bd5e696?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2550&q=80"));

        grass = new PhongMaterial();
        grass.setDiffuseMap(new Image("https://cmkt-image-prd.freetls.fastly.net/0.1.0/ps/5283862/910/610/m2/fpnw/wm1/s0lxcdijdpssi6barbnwapmletoaep7ror6xli8uiriu7ewukmhydxkejhtzloor-.jpg?1540855452&s=0a7e8bea6fc75c7ae98954c8f78039da"));
//        grass.setBumpMap(new Image("https://texturehaven.com/files/textures/jpg/1k/grass_path_2/grass_path_2_bump_1k.jpg"));
//        grass.setSpecularMap(new Image("https://texturehaven.com/files/textures/jpg/1k/grass_path_2/grass_path_2_spec_1k.jpg"));


        dirt = new PhongMaterial();
        dirt.setDiffuseMap(new Image("https://4.bp.blogspot.com/-Mz94fzjf9DM/UmpLfICutiI/AAAAAAAAEk8/8Uid3yVbuzc/s1600/Dirt+00+seamless.jpg"));


        moss = new PhongMaterial();
        moss.setDiffuseMap(new Image("https://images.freecreatives.com/wp-content/uploads/2016/01/Seamless-Tileable-Grass-Texture.jpeg"));


        sand = new PhongMaterial();
        sand.setDiffuseMap(new Image("https://images.freecreatives.com/wp-content/uploads/2016/02/Seamless-Beach-Sand-Texture.jpg"));


        sun = new PhongMaterial();
        sun.setDiffuseMap(new Image("https://www.filterforge.com/filters/10089.jpg"));
        sun.setSelfIlluminationMap(new Image("https://www.filterforge.com/filters/10089.jpg"));

        moon = new PhongMaterial();
        moon.setDiffuseMap(new Image("https://i.pinimg.com/originals/3e/dc/e0/3edce08472326f054d5b3f004ebf08f6.jpg"));
        moon.setSelfIlluminationMap(new Image("https://i.pinimg.com/originals/3e/dc/e0/3edce08472326f054d5b3f004ebf08f6.jpg"));


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

}
