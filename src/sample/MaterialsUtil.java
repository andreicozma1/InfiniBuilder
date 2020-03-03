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

   MaterialsUtil(){
       setupMaterials();
   }

    public static void setupMaterials(){
        stone = new PhongMaterial();
        stone.setDiffuseMap(new Image("https://images.unsplash.com/photo-1522409640003-31c21bd5e696?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2550&q=80"));

        grass = new PhongMaterial();
        grass.setDiffuseMap(new Image("https://3dwarehouse.sketchup.com/warehouse/v1.0/publiccontent/5577056a-3ec8-4856-b2c1-4604107a6166"));

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
