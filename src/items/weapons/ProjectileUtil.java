package items.weapons;

import environment.EnvironmentUtil;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import objects.DrawSphere;
import resources.ResourcesUtil;

public class ProjectileUtil {

    EnvironmentUtil context;
    DrawSphere proj;

    public ProjectileUtil(EnvironmentUtil u){
        System.out.println("Added projectile");
        proj = new DrawSphere(1);
        context = u;
        // TODO - Change to stone
        proj.setMaterial(ResourcesUtil.stone);

    }

    public void setRadius(double r){
        proj.setScale(r);
    }

    public DrawSphere getProjectile(){
        return proj;
    }

    public void shoot(double initialVel){

        context.addToWorldGroup(proj);

        double velY = initialVel * Math.cos(Math.toRadians(context.context.getCamera().getRotateY()));

        double startrotX = Math.toRadians(context.context.getCamera().getRotateX());
        double startrotY =Math.toRadians(context.context.getCamera().getRotateY());

        final double[] posx = {context.context.getPlayer().getX()};
        final double[] posy = {-context.context.getPlayer().getY() - context.context.getPlayer().player_height};
        final double[] posz = {context.context.getPlayer().getZ()};

        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long l) {
                posx[0] += initialVel * Math.sin(startrotX) * Math.cos(startrotY);
                posy[0] -= initialVel * Math.sin(startrotY);
                posz[0] += initialVel * Math.cos(startrotX)* Math.cos(startrotY);
                proj.setTranslateXYZ(posx[0], posy[0], posz[0]);
            }
        };
        at.start();



    }

}
