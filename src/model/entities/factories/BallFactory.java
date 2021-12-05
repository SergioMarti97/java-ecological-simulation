package model.entities.factories;

import model.CustomColor;
import model.entities.live.LivingBall;
import model.entities.live.LivingDynamicBall;
import olcPGEApproach.vectors.points2d.Vec2df;

import java.util.ArrayList;
import java.util.Random;

public class BallFactory {

    private static final Random rnd = new Random();

    public static LivingDynamicBall buildLivingDynamicBall(
            int w,
            int h,
            int maxR,
            int minR,
            int maxVel,
            float maxEnergy) {
        int r = rnd.nextInt(maxR - minR) + minR;
        Vec2df pos = new Vec2df(
                (float)rnd.nextInt(w - 2 * r) + r,
                (float)rnd.nextInt(h - 2 * r) + r
        );
        Vec2df vel = new Vec2df(
                (float)rnd.nextInt(maxVel) - (float)maxVel / 2.0F,
                (float)rnd.nextInt(maxVel) - (float)maxVel / 2.0F
        );
        int col = CustomColor.makeRandom();
        return new LivingDynamicBall(pos, vel, r, maxEnergy, col);
    }

    public static LivingBall buildLivingBall(
            int w,
            int h,
            int maxR,
            int minR,
            float maxEnergy,
            float minEnergy) {
        Vec2df pos = new Vec2df(
                (float)rnd.nextInt(w - 10),
                (float)rnd.nextInt(h - 10)
        );
        LivingBall lb = new LivingBall(pos, 0, maxEnergy);
        float energy = rnd.nextInt((int)maxEnergy) + (int)minEnergy - 100;
        float percentage = (energy / maxEnergy);
        int r = (int)(percentage * (maxR - minR) + minR);
        int col = (int)(percentage * 255);
        lb.setRadius(r);
        lb.setEnergy(energy);
        lb.setCol(new CustomColor(col, col, 0));
        return lb;
    }

    public static ArrayList<LivingDynamicBall> buildLivingDynamicBalls(
            int id,
            int numBalls,
            int w,
            int h,
            int maxR,
            int minR,
            int maxVel,
            int maxEnergy) {
        ArrayList<LivingDynamicBall> balls = new ArrayList<>();
        for (int i = 0; i < numBalls; i++) {
            LivingDynamicBall b = BallFactory.buildLivingDynamicBall(
                    w,
                    h,
                    maxR,
                    minR,
                    maxVel,
                    maxEnergy);
            b.setId(id);
            id++;
            balls.add(b);
        }
        return balls;
    }

    public static ArrayList<LivingDynamicBall> buildLivingDynamicBalls(
            int id,
            int numBalls,
            int w,
            int h,
            int maxR,
            int minR,
            int maxVel,
            int maxEnergy,
            CustomColor c) {
        ArrayList<LivingDynamicBall> balls = new ArrayList<>();
        for (int i = 0; i < numBalls; i++) {
            LivingDynamicBall b = BallFactory.buildLivingDynamicBall(
                    w,
                    h,
                    maxR,
                    minR,
                    maxVel,
                    maxEnergy);
            b.setCol(c);
            b.setId(id);
            id++;
            balls.add(b);
        }
        return balls;
    }

    public static ArrayList<LivingBall> buildLivingBalls(
            int id,
            int numBalls,
            int w,
            int h,
            int maxR,
            int minR,
            int maxEnergy,
            int minEnergy) {
        ArrayList<LivingBall> balls = new ArrayList<>();
        for (int i = 0; i < numBalls; i++) {
            LivingBall b = BallFactory.buildLivingBall(
                    w,
                    h,
                    maxR,
                    minR,
                    maxEnergy,
                    minEnergy);
            b.setId(id);
            id++;
            balls.add(b);
        }
        return balls;
    }

}
