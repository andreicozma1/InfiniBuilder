package app.utils;

import app.environment.EnvironmentUtil;
import app.player.AbsolutePoint3D;
import app.structures.StructureBuilder;
import javafx.animation.AnimationTimer;

public class ProjectileUtil extends StructureBuilder {
    private static final String TAG = "ProjectileUtil";

    private final EnvironmentUtil context;
    private final StructureBuilder object;
    private double speed;

    public ProjectileUtil(EnvironmentUtil ctx, StructureBuilder obj) {
        Log.p(TAG, "CONSTRUCTOR");

        context = ctx;
        object = obj;

        // Set default speed if not set by user
        setSpeed(5);
        this.getProps().setPROPERTY_ITEM_TYPE(StructureBuilder.TYPE_WEAPON);
    }

    public void setSpeed(double spd) {
        speed = spd;
    }

    public void shoot() {
        context.addToGroup(EnvironmentUtil.GROUP_OTHER, object);

        double startrotX = Math.toRadians(context.context.getComponents().getCamera().getRotateX());
        double startrotY = Math.toRadians(context.context.getComponents().getCamera().getRotateY());


        AnimationTimer at = new AnimationTimer() {
            final double initialVel = speed;
            double posx = context.context.getComponents().getPlayer().getPositionX();
            double posy = context.context.getComponents().getPlayer().getPositionYwithHeight();
            double posz = context.context.getComponents().getPlayer().getPositionZ();
            double initialVelY = 0;

            boolean isOnGround = false;
            long onGroundTimestamp;

            @Override
            public void handle(long l) {
                if (!isOnGround) {
//                    System.out.println(posy +  "   " + context.getClosestGroundLevel(new PlayerPoint3D(posx,posy,posz)));
                    posx += initialVel * Math.sin(startrotX) * Math.cos(startrotY);
                    posy -= initialVel * Math.sin(startrotY) + initialVelY;
                    initialVelY -= EnvironmentUtil.GRAVITY;
                    posz += initialVel * Math.cos(startrotX) * Math.cos(startrotY);
                    object.setTranslateIndependent(posx, posy, posz);

                    if (posy > context.getClosestGroundLevel(new AbsolutePoint3D(posx, posy, posz), true) - context.getBlockDim() / 2.0 - object.getHeight() / 2) {
                        isOnGround = true;
                        onGroundTimestamp = context.context.time_current;
                    }
                } else {
                    if ((context.context.time_current - onGroundTimestamp) / 1000 > 10) {
                        context.removeFromGroup(EnvironmentUtil.GROUP_OTHER, object);
                        this.stop();
                    }
                }

                if (posy > EnvironmentUtil.LIMIT_MIN) {
                    context.removeFromGroup(EnvironmentUtil.GROUP_OTHER, object);
                    this.stop();
                }

            }
        };
        at.start();
    }
}
