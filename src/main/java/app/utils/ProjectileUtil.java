package app.utils;

import app.environment.EnvironmentUtil;
import javafx.animation.AnimationTimer;
import app.structures.StructureBuilder;

public class ProjectileUtil extends StructureBuilder {
    private static final String TAG = "ProjectileUtil";

    private final EnvironmentUtil context;
    private final StructureBuilder str;
    private double speed;
    private final double DEFAULT_SPEED = 5;

    public ProjectileUtil(EnvironmentUtil u, StructureBuilder b) {
        Log.p(TAG,"CONSTRUCTOR");

        context = u;
        str = b;

        setSpeed(DEFAULT_SPEED);
        this.getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_WEAPON);
        // TODO - Change to stone
    }

    public void setSpeed(double s) {
        speed = s;
    }

    public void shoot() {
        double initialVel = speed;
        context.addToGroup(EnvironmentUtil.GROUP_STRUCTURES, str);

        double velY = initialVel * Math.cos(Math.toRadians(context.context.getComponents().getCamera().getRotateY()));

        double startrotX = Math.toRadians(context.context.getComponents().getCamera().getRotateX());
        double startrotY = Math.toRadians(context.context.getComponents().getCamera().getRotateY());

        final double[] posx = {context.context.getComponents().getPlayer().getPositionX()};
        final double[] posy = {-context.context.getComponents().getPlayer().getPositionYwithHeight() - context.context.getComponents().getPlayer().getPlayerHeight()};
        final double[] posz = {context.context.getComponents().getPlayer().getPositionZ()};


        System.out.println(posx[0] + "   " + posy[0] + "   " + posz[0]);

        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long l) {

                posx[0] += initialVel * Math.sin(startrotX) * Math.cos(startrotY);
                posy[0] -= initialVel * Math.sin(startrotY);
                posz[0] += initialVel * Math.cos(startrotX) * Math.cos(startrotY);
                str.setTranslateIndependent(posx[0], posy[0], posz[0]);
            }
        };
        at.start();
    }
}
