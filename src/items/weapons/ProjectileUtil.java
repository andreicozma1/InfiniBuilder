package items.weapons;

import environment.EnvironmentUtil;
import javafx.animation.AnimationTimer;
import structures.DrawSphere;
import resources.ResourcesUtil;
import structures.StructureBuilder;
import utils.WindowUtil;

public class ProjectileUtil extends StructureBuilder{

    EnvironmentUtil context;
    StructureBuilder str;
    public ProjectileUtil(EnvironmentUtil u, StructureBuilder b){
        System.out.println("Added projectile");
        context = u;
        str = b;
        this.setType(StructureBuilder.TYPE_WEAPON);
        // TODO - Change to stone

        this.getChildren().addAll(str);
        context.addFromGroup(EnvironmentUtil.GROUP_WORLD, this);
    }

    public void shoot(double initialVel){

System.out.println("Shooting");
        double velY = initialVel * Math.cos(Math.toRadians(context.context.getCamera().getRotateY()));

        double startrotX = Math.toRadians(context.context.getCamera().getRotateX());
        double startrotY =Math.toRadians(context.context.getCamera().getRotateY());

        double startx = context.context.getPlayer().getX();
        double starty = -context.context.getPlayer().getY() - context.context.getPlayer().player_height;
        double startz = context.context.getPlayer().getZ();
        final double[] posx = {startx};
        final double[] posy = {starty};
        final double[] posz = {startz};

        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long l) {
                System.out.println("Traveling");

                posx[0] += initialVel* Math.sin(startrotX) * Math.cos(startrotY);
                posy[0] -= initialVel * Math.sin(startrotY);
                posz[0] += initialVel * Math.cos(startrotX)* Math.cos(startrotY);
                str.setTranslateXYZ(posx[0], posy[0], posz[0]);


            }
        };
        at.start();
    }
}
