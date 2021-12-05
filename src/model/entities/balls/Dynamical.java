package model.entities.balls;

import olcPGEApproach.vectors.points2d.Vec2df;

/**
 * This interface is for all objects what are
 * dynamical. It means, have to behave following
 * the rules of dynamics physics
 */
public interface Dynamical {

    Vec2df getVel();

    void setVel(Vec2df vel);

}
