package model.physics;

import model.entities.balls.Ball;
import olcPGEApproach.vectors.points2d.Vec2d;
import olcPGEApproach.vectors.points2d.Vec2dd;
import olcPGEApproach.vectors.points2d.Vec2df;
import olcPGEApproach.vectors.points2d.Vec2di;

public class CollisionDetection {

    public static boolean doCirclesOverlap(Ball b1, Ball b2) {
        return (b1.getPos().getX() - b2.getPos().getX()) * (b1.getPos().getX() - b2.getPos().getX()) +
                (b1.getPos().getY() - b2.getPos().getY()) * (b1.getPos().getY() - b2.getPos().getY()) <=
                (b1.getR() + b2.getR()) * (b1.getR() + b2.getR());
    }

    // Is point in circle methods

    public static boolean isPointInCircle(int x, int y, Ball b) {
        return (x - b.getPos().getX()) * (x - b.getPos().getX()) +
                (y - b.getPos().getY()) * (y - b.getPos().getY()) <=
                (b.getR() * b.getR());
    }

    public static boolean isPointInCircle(float x, float y, Ball b) {
        return (x - b.getPos().getX()) * (x - b.getPos().getX()) +
                (y - b.getPos().getY()) * (y - b.getPos().getY()) <=
                (b.getR() * b.getR());
    }

    public static boolean isPointInCircle(double x, double y, Ball b) {
        return (x - b.getPos().getX()) * (x - b.getPos().getX()) +
                (y - b.getPos().getY()) * (y - b.getPos().getY()) <=
                (b.getR() * b.getR());
    }

    public static boolean isPointInCircle(Vec2di p, Ball b) {
        return isPointInCircle(p.getX(), p.getY(), b);
    }

    public static boolean isPointInCircle(Vec2df p, Ball b) {
        return isPointInCircle(p.getX(), p.getY(), b);
    }

    public static boolean isPointInCircle(Vec2dd p, Ball b) {
        return isPointInCircle(p.getX(), p.getY(), b);
    }

    public static boolean isPointInCircle(Vec2d p, Ball b) {
        if (p instanceof Vec2di) {
            return isPointInCircle((Vec2di) p, b);
        } else if (p instanceof Vec2df) {
            return isPointInCircle((Vec2df) p, b);
        } else if (p instanceof Vec2dd) {
            return isPointInCircle((Vec2dd) p, b);
        } else {
            return false;
        }
    }

}
