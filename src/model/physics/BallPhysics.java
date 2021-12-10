package model.physics;

import javafx.util.Pair;
import model.entities.balls.Ball;
import model.entities.balls.DynamicBall;

import java.util.ArrayList;
import java.util.List;

public class BallPhysics {

    public static final ArrayList<Pair<Ball, Ball>> colliding = new ArrayList<>();
    
    public static void ballVsEdgesFull(int w, int h, Ball b) {
        if (b.getPos().getX() <= b.getR()) {
            b.getPos().setX(1 + b.getR());
            if (b instanceof DynamicBall) {
                DynamicBall db = (DynamicBall) b;
                db.getVel().setX(-db.getVel().getX());
            }
        }
        if (b.getPos().getX() >= w - b.getR()) {
            b.getPos().setX(w - b.getR() - 1);
            if (b instanceof DynamicBall) {
                DynamicBall db = (DynamicBall) b;
                db.getVel().setX(-db.getVel().getX());
            }
        }
        if (b.getPos().getY() <= b.getR()) {
            b.getPos().setY(1 + b.getR());
            if (b instanceof DynamicBall) {
                DynamicBall db = (DynamicBall) b;
                db.getVel().setY(-db.getVel().getY());
            }
        }
        if (b.getPos().getY() >= h - b.getR()) {
            b.getPos().setY(h - b.getR() - 1);
            if (b instanceof DynamicBall) {
                DynamicBall db = (DynamicBall) b;
                db.getVel().setY(-db.getVel().getY());
            }
        }
    }

    public static void ballVsEdgesHalf(int w, int h, Ball b) {
        if (b.getPos().getX() <= 0) {
            b.getPos().setX(0);
            if (b instanceof DynamicBall) {
                DynamicBall db = (DynamicBall) b;
                db.getVel().setX(-db.getVel().getX());
            }
        }
        if (b.getPos().getX() >= w) {
            b.getPos().setX(w);
            if (b instanceof DynamicBall) {
                DynamicBall db = (DynamicBall) b;
                db.getVel().setX(-db.getVel().getX());
            }
        }
        if (b.getPos().getY() <= 0) {
            b.getPos().setY(0);
            if (b instanceof DynamicBall) {
                DynamicBall db = (DynamicBall) b;
                db.getVel().setY(-db.getVel().getY());
            }
        }
        if (b.getPos().getY() >= h) {
            b.getPos().setY(h);
            if (b instanceof DynamicBall) {
                DynamicBall db = (DynamicBall) b;
                db.getVel().setY(-db.getVel().getY());
            }
        }
    }

    public static void ballVsEdgesAsteroids(int w, int h, Ball b) {
        if (b.getPos().getX() < b.getR()) {
            b.getPos().setX(w - 2 * b.getR());
        }
        if (b.getPos().getX() >= w - b.getR()) {
            b.getPos().setX(b.getR());
        }
        if (b.getPos().getY() < b.getR()) {
            b.getPos().setY(h - 2 * b.getR());
        }
        if (b.getPos().getY() >= h - b.getR()) {
            b.getPos().setY(b.getR());
        }
    }

    public static void detectCollisions(List<Ball> balls) {
        // Static collisions, i.e. overlap
        for (Ball b : balls) {
            for (Ball t : balls) {
                if (!b.equals(t)) { // b.getId() != t.getId()
                    if (CollisionDetection.doCirclesOverlap(b, t)) {
                        // Collision has occured
                        colliding.add(new Pair<>(b, t));
                        if (b instanceof Dynamical || t instanceof Dynamical) {
                            updateStaticCollision(b, t);
                        }
                    }
                }
            }
        }
    }

    public static void updateStaticCollision(Ball b, Ball t) {
        // Distance between ball centers
        float diffX = b.getPos().getX() - t.getPos().getX();
        float diffY = b.getPos().getY() - t.getPos().getY();
        float dist = (float)Math.sqrt(diffX * diffX + diffY * diffY);

        // Calculate displacement required
        float fOverlap = 0.5f * (dist - b.getR() - t.getR());

        if (dist == 0) {
            dist = 1;
        }

        // Displace Current Ball away from collision
        b.getPos().addToX(-(fOverlap * (b.getPos().getX() - t.getPos().getX()) / dist));
        b.getPos().addToY(-(fOverlap * (b.getPos().getY() - t.getPos().getY()) / dist));

        // Displace Target Ball away from collision
        t.getPos().addToX(fOverlap * (b.getPos().getX() - t.getPos().getX()) / dist);
        t.getPos().addToY(fOverlap * (b.getPos().getY() - t.getPos().getY()) / dist);
    }

    /**
     * Wikipedia Version - Maths is smarter but same
     * float kx = (b1.getVel().getX() - b2.getVel().getX());
     * float ky = (b1.getVel().getY() - b2.getVel().getY());
     * float p = 2.0 * (nx * kx + ny * ky) / (b1.getMass() + b2.getMass());
     * b1.getVel().setX(b1.getVel().getX() - p * b2.getMass() * nx);
     * b1.getVel().getY(b1.getVel().getY() - p * b2.getMass() * ny);
     * b2.getVel().getX(b2.getVel().getX() + p * b1.getMass() * nx);
     * b2.getVel().getY(b2.getVel().getY() + p * b1.getMass() * ny);
     */
    public static void updateDynamicCollision(DynamicBall b1, DynamicBall b2) {
        // Distance between balls
        float diffX = b1.getPos().getX() - b2.getPos().getX();
        float diffY = b1.getPos().getY() - b2.getPos().getY();
        float dist = (float)Math.sqrt(diffX * diffX + diffY * diffY);

        if (dist == 0) {
            dist = 1;
        }

        // Normal
        float nx = (b2.getPos().getX() - b1.getPos().getX()) / dist;
        float ny = (b2.getPos().getY() - b1.getPos().getY()) / dist;

        // Tangent
        float tx = -ny;
        float ty = nx;

        // Dot Product Tangent
        float dpTan1 = b1.getVel().getX() * tx + b1.getVel().getY() * ty;
        float dpTan2 = b2.getVel().getX() * tx + b2.getVel().getY() * ty;

        // Dot Product Normal
        float dpNorm1 = b1.getVel().getX() * nx + b1.getVel().getY() * ny;
        float dpNorm2 = b2.getVel().getX() * nx + b2.getVel().getY() * ny;

        // Conservation of momentum in 1D
        float m1 = (dpNorm1 * (b1.getMass() - b2.getMass()) + 2.0f * b2.getMass() * dpNorm2) / (b1.getMass() + b2.getMass());
        float m2 = (dpNorm2 * (b2.getMass() - b1.getMass()) + 2.0f * b1.getMass() * dpNorm1) / (b1.getMass() + b2.getMass());

        // Update ball b.getVelocities
        b1.getVel().setX(tx * dpTan1 + nx * m1);
        b1.getVel().setY(ty * dpTan1 + ny * m1);

        b2.getVel().setX(tx * dpTan2 + nx * m2);
        b2.getVel().setY(ty * dpTan2 + ny * m2);
    }

}
