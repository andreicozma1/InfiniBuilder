package items.weapons;

import environment.EnvironmentUtil;
import javafx.animation.AnimationTimer;
import structures.DrawSphere;
import resources.ResourcesUtil;
import structures.StructureBuilder;

public class ProjectileUtil extends StructureBuilder{

    EnvironmentUtil context;
    StructureBuilder str;
    public ProjectileUtil(EnvironmentUtil u, StructureBuilder b){
        System.out.println("Added projectile");
        context = u;
        str = b;
        // TODO - Change to stone

        this.getChildren().addAll(str);
        context.addToWorldGroup(this);
    }

    public void shoot(double initialVel){


        double velY = initialVel * Math.cos(Math.toRadians(context.context.getCamera().getRotateY()));

        double startrotX = Math.toRadians(context.context.getCamera().getRotateX());
        double startrotY =Math.toRadians(context.context.getCamera().getRotateY());

        final double[] posx = {context.context.getPlayer().getX()};
        final double[] posy = {-context.context.getPlayer().getY() - context.context.getPlayer().player_height};
        final double[] posz = {context.context.getPlayer().getZ()};

        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long l) {
                posx[0] += initialVel* Math.sin(startrotX) * Math.cos(startrotY);
                posy[0] -= initialVel * Math.sin(startrotY);
                posz[0] += initialVel * Math.cos(startrotX)* Math.cos(startrotY);
                str.setTranslateXYZ(posx[0], posy[0], posz[0]);
            }
        };
        at.start();
    }
}
