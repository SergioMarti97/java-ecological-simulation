package model.entities.balls;

import model.CustomColor;
import model.DrawUtils;
import model.entities.Updatable;
import model.physics.Dynamical;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

/**
 * This class represents a dynamic ball
 * A dynamic ball stores some physical variables
 * as the velocity, the acceleration, the force
 * of that object
 */
public class DynamicBall extends Ball implements Updatable, Dynamical {

    private Vec2df vel;

    private Vec2df acc;

    private Vec2df force;

    public DynamicBall(Vec2df pos, Vec2df vel, int r, int col) {
        super(pos, r, col);
        this.vel = new Vec2df(vel);
    }

    public DynamicBall(Vec2df pos, Vec2df vel, int r, CustomColor col) {
        super(pos, r, col);
        this.vel = new Vec2df(vel);
    }

    @Override
    public void update(float dt) {
        //acc.setX(force.getX() / getMass());
        //acc.setY(force.getY() / getMass());

        //vel.addToX(acc.getX() * dt);
        //vel.addToY(acc.getY() * dt);

        pos.addToX(vel.getX() * dt);
        pos.addToY(vel.getY() * dt);
    }

    public void drawYourSelf(Renderer r, boolean drawArrows) {
        drawYourSelf(r);
        if (drawArrows) {
            //drawArrow(r, force, HexColors.ORANGE);
            //drawArrow(r, acc, HexColors.RED);
            DrawUtils.drawArrow(r, pos, vel, HexColors.BLUE);
        }
    }

    @Override
    public Vec2df getVel() {
        return vel;
    }

    @Override
    public void setVel(Vec2df vel) {
        this.vel = vel;
    }

}
