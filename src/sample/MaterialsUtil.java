package sample;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class MaterialsUtil {

    public static PhongMaterial stone;
    public static PhongMaterial grass;
    public static PhongMaterial blue;
    public static PhongMaterial green;
    public static PhongMaterial red;
    public static PhongMaterial purple;
    public static PhongMaterial sun;
    public static PhongMaterial moon;

   MaterialsUtil(){
       setupMaterials();
   }

    public static void setupMaterials(){
        stone = new PhongMaterial();
        stone.setDiffuseMap(new Image("https://images.unsplash.com/photo-1522409640003-31c21bd5e696?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2550&q=80"));

        grass = new PhongMaterial();
        grass.setDiffuseMap(new Image("https://texturehaven.com/files/textures/jpg/1k/grass_path_2/grass_path_2_diff_1k.jpg"));
//        grass.setBumpMap(new Image("https://texturehaven.com/files/textures/jpg/1k/grass_path_2/grass_path_2_bump_1k.jpg"));
//        grass.setSpecularMap(new Image("https://texturehaven.com/files/textures/jpg/1k/grass_path_2/grass_path_2_spec_1k.jpg"));

        sun = new PhongMaterial();
        sun.setDiffuseMap(new Image("https://lh3.googleusercontent.com/proxy/PD1ZGN2tLtQAg8rpkaL4QLk3vLEEe6TRB2UNQYThB8i7Q0r03dCTUg0ExKZP9eveMdTyk7sTT3tsSNJuvB6AXS0m1EC0zWH7MAGcvbK3eWim4dDq5FM5JiSle4UPdHiAwX-GQkA"));
        sun.setSelfIlluminationMap(new Image("https://lh3.googleusercontent.com/proxy/PD1ZGN2tLtQAg8rpkaL4QLk3vLEEe6TRB2UNQYThB8i7Q0r03dCTUg0ExKZP9eveMdTyk7sTT3tsSNJuvB6AXS0m1EC0zWH7MAGcvbK3eWim4dDq5FM5JiSle4UPdHiAwX-GQkA"));

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
