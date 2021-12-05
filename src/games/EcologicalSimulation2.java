package games;

import javafx.scene.input.MouseButton;
import javafx.util.Pair;
import model.Clickable2D;
import model.CustomColor;
import model.entities.balls.Ball;
import model.entities.balls.DynamicBall;
import model.entities.balls.Dynamical;
import model.entities.factories.BallFactory;
import model.entities.live.LivingBall;
import model.entities.live.LivingDynamicBall;
import model.physics.BallPhysics;
import model.population.Population;
import model.statistics.Graphics;
import model.statistics.tracker.VariablePairTracker;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;
import olcPGEApproach.Input;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

import java.util.*;

public class EcologicalSimulation2 implements AbstractGame {

    private int id = 0;

    private double avgVel = 0;

    private double time = 0;

    private ArrayList<Ball> balls;

    private Population<LivingDynamicBall> f;

    private Population<LivingDynamicBall> p;

    private Population<LivingDynamicBall> s;

    private ArrayList<VariablePairTracker<Double, Double>> trackers;

    private ArrayList<Pair<Integer, Integer>> colors;

    private int textPosY = 10;

    private Random rnd;

    private Ball selectedBall = null;

    private Vec2df velCache = null;

    @Override
    public void initialize(GameContainer gc) {
        rnd = new Random();
        balls = new ArrayList<>();

        trackers = new ArrayList<>();
        trackers.add(new VariablePairTracker<>("food", 5000));
        trackers.add(new VariablePairTracker<>("predators", 5000));
        trackers.add(new VariablePairTracker<>("avg vel", 5000));

        colors = new ArrayList<>();
        colors.add(new Pair<>(HexColors.RED, HexColors.GREEN));
        colors.add(new Pair<>(HexColors.BLUE, HexColors.ORANGE));
        colors.add(new Pair<>(HexColors.CYAN, HexColors.FANCY_RED));

        f = new Population<LivingDynamicBall>() {
            @Override
            public LivingDynamicBall makeBaby(LivingDynamicBall mom) {
                return (LivingDynamicBall) mom.makeBaby();
            }

            @Override
            public LivingDynamicBall death(LivingDynamicBall old) {
                return old;
            }
        };

        p = new Population<LivingDynamicBall>() {
            @Override
            public LivingDynamicBall makeBaby(LivingDynamicBall mom) {
                return (LivingDynamicBall) mom.makeBaby();
            }

            @Override
            public LivingDynamicBall death(LivingDynamicBall old) {
                return old;
            }
        };

        s = new Population<LivingDynamicBall>() {
            @Override
            public LivingDynamicBall makeBaby(LivingDynamicBall mom) {
                return (LivingDynamicBall) mom.makeBaby();
            }

            @Override
            public LivingDynamicBall death(LivingDynamicBall old) {
                return old;
            }
        };

        init(gc);
    }

    /**
     * f.getIndividuals().addAll(BallFactory.buildLivingBalls(
     *                 id,
     *                 100,
     *                 gc.getRenderer().getW(),
     *                 gc.getRenderer().getH(),
     *                 5,
     *                 2,
     *                 1000,
     *                 100
     *         ));
     */
    private void init(GameContainer gc) {
        balls.clear();
        f.getIndividuals().addAll(BallFactory.buildLivingDynamicBalls(
                id,
                200,
                gc.getRenderer().getW(),
                gc.getRenderer().getH(),
                7,
                3,
                200,
                5000,
                new CustomColor(HexColors.YELLOW)
        ));
        id = balls.size();
        p.getIndividuals().addAll(BallFactory.buildLivingDynamicBalls(
                id,
                5,
                gc.getRenderer().getW(),
                gc.getRenderer().getH(),
                15,
                10,
                150,
                10000,
                new CustomColor(HexColors.RED)
        ));
        id = balls.size();
        s.getIndividuals().addAll(BallFactory.buildLivingDynamicBalls(
                id,
                4,
                gc.getRenderer().getW(),
                gc.getRenderer().getH(),
                20,
                18,
                100,
                15000,
                new CustomColor(HexColors.BLUE)
        ));
        id = balls.size();
        balls.addAll(p.getIndividuals());
        balls.addAll(f.getIndividuals());
        balls.addAll(s.getIndividuals());
    }

    private void updateBalls(int w, int h, float dt) {
        f.update(dt);
        p.update(dt);
        s.update(dt);

        for (LivingDynamicBall i : f.getIndividuals()) {
            i.setEnergy(i.getEnergy() + 300.0f * dt);
        }

        for (Ball b : balls) {
            BallPhysics.ballVsEdgesHalf(w, h, b);

            if (b instanceof Dynamical) {
                avgVel += ((Dynamical)b).getVel().mag2();
            }

            if (Float.isNaN(b.getPos().getX()) || Float.isNaN(b.getPos().getY())) {
                System.out.println("¡Hay pelotas fuera!");
                int threshold = b.getR();
                b.getPos().setX(rnd.nextInt(w - threshold) + threshold);
                b.getPos().setY(rnd.nextInt(h - threshold) + threshold);
            }

        }
    }

    private void updateBallCollisions() {
        BallPhysics.colliding.clear();
        BallPhysics.detectCollisions(balls);
        for (Pair<Ball, Ball> pair : BallPhysics.colliding) {
            LivingDynamicBall b1 = (LivingDynamicBall) pair.getKey();
            LivingDynamicBall b2 = (LivingDynamicBall) pair.getValue();

            if (f.getIndividuals().contains(b1) && f.getIndividuals().contains(b2)) {
                BallPhysics.updateStaticCollision(b1, b2);
            }
            if (p.getIndividuals().contains(b1) && p.getIndividuals().contains(b2)) {
                BallPhysics.updateDynamicCollision(b1, b2);
            }
            if (s.getIndividuals().contains(b1) && s.getIndividuals().contains(b2)) {
                BallPhysics.updateDynamicCollision(b1, b2);
            }

            if (f.getIndividuals().contains(b1) && p.getIndividuals().contains(b2)) {
                if (rnd.nextBoolean()) {
                    b2.setEnergy(b2.getEnergy() + b1.getEnergy());
                    f.getDeaths().add(b1);
                }
            }
            if (p.getIndividuals().contains(b1) && f.getIndividuals().contains(b2)) {
                if (rnd.nextBoolean()) {
                    b1.setEnergy(b1.getEnergy() + b2.getEnergy());
                    f.getDeaths().add(b2);
                }
            }

            if (p.getIndividuals().contains(b1) && s.getIndividuals().contains(b2)) {
                if (rnd.nextBoolean()) {
                    b2.setEnergy(b2.getEnergy() + b1.getEnergy());
                    p.getDeaths().add(b1);
                }
            }
            if (s.getIndividuals().contains( b1) && p.getIndividuals().contains( b2)) {
                if (rnd.nextBoolean()) {
                    b1.setEnergy(b1.getEnergy() + b2.getEnergy());
                    p.getDeaths().add(b2);
                }
            }

            if (f.getIndividuals().contains(b1) && s.getIndividuals().contains(b2)) {
                BallPhysics.updateDynamicCollision(b1, b2);
                if (b2.getEnergy() <= b2.getMaxEnergy() / 5) {
                    b2.setEnergy(b2.getMaxEnergy() + 100);
                    f.getDeaths().add(b1);
                }
            }
            if (s.getIndividuals().contains( b1) && f.getIndividuals().contains( b2)) {
                BallPhysics.updateDynamicCollision(b1, b2);
                if (b1.getEnergy() <= b1.getMaxEnergy() / 5) {
                    b1.setEnergy(b1.getMaxEnergy() + 100);
                    f.getDeaths().add(b2);
                }
            }

        }
    }

    private void updateDeathsAndBabies() {
        p.updateDeathsAndBabies();
        f.updateDeathsAndBabies();
        s.updateDeathsAndBabies();
    }

    private void updateUserInput(Input input) {
        if (input.isButtonDown(MouseButton.PRIMARY) || input.isButtonDown(MouseButton.SECONDARY)) {
            for (Ball b : balls) {
                if (b instanceof Clickable2D) {
                    if (((Clickable2D) b).isMouseInside(input.getMouseX(), input.getMouseY())) {
                        selectedBall = b;
                        if (b instanceof Dynamical) {
                            Dynamical d = (Dynamical) b;
                            velCache = new Vec2df(d.getVel());
                            ((Dynamical) selectedBall).getVel().setX(0);
                            ((Dynamical) selectedBall).getVel().setY(0);
                        }
                    }
                }
            }
        }
        if (input.isButtonHeld(MouseButton.PRIMARY)) {
            if (selectedBall != null) {
                selectedBall.getPos().setX((float) input.getMouseX());
                selectedBall.getPos().setY((float) input.getMouseY());
            }
        }
        if (input.isButtonUp(MouseButton.PRIMARY)) {
            if (selectedBall instanceof Dynamical && velCache != null) {
                ((Dynamical) selectedBall).setVel(velCache);
            }
            selectedBall = null;
            velCache = null;
        }
        if (input.isButtonUp(MouseButton.SECONDARY)) {
            if (selectedBall instanceof Dynamical) {
                Dynamical d = (Dynamical) selectedBall;
                d.getVel().setX(5.0f * (selectedBall.getPos().getX() - (float)input.getMouseX()));
                d.getVel().setY(5.0f * (selectedBall.getPos().getY() - (float)input.getMouseY()));
            }
            selectedBall = null;
            velCache = null;
        }
    }

    @Override
    public void update(GameContainer gc, float dt) {
        time += dt;

        balls.clear();
        balls.addAll(p.getIndividuals());
        balls.addAll(f.getIndividuals());
        balls.addAll(s.getIndividuals());

        updateBalls(gc.getRenderer().getW(), gc.getRenderer().getH(), dt);

        updateBallCollisions();

        updateDeathsAndBabies();

        updateUserInput(gc.getInput());

        if (p.getIndividuals().size() >= 1) {
            avgVel = Math.sqrt(avgVel) / p.getIndividuals().size();
        } else {
            avgVel = Math.sqrt(avgVel);
        }

        trackers.get(0).pushPair(time, (double) f.getIndividuals().size());
        trackers.get(1).pushPair(time, (double) p.getIndividuals().size());
        trackers.get(2).pushPair(time, (double) s.getIndividuals().size());
        for (VariablePairTracker<Double, Double> t : trackers) {
            t.update();
        }

        if (balls.size() == 0 || f.getIndividuals().size() >= 3000) {
            init(gc);
        }
    }

    private void drawBalls(GameContainer gc) {
        for (Ball b : balls) {
            if (b instanceof DynamicBall) {
                DynamicBall db = (DynamicBall)b;
                if (db instanceof LivingDynamicBall) {
                    LivingDynamicBall ldb = (LivingDynamicBall) db;
                    ldb.drawYourSelf(gc.getRenderer(), false, true);
                } else {
                    db.drawYourSelf(gc.getRenderer(), false);
                }
            } else if (b instanceof LivingBall) {
                LivingBall lf = (LivingBall)b;
                lf.drawYourSelf(gc.getRenderer(), false);
            } else {
                b.drawYourSelf(gc.getRenderer());
            }
        }
    }

    private void drawCollisions(GameContainer gc) {
        for (Pair<Ball, Ball> pair : BallPhysics.colliding) {
            gc.getRenderer().drawLine(
                    (int)pair.getKey().getPos().getX(),
                    (int)pair.getKey().getPos().getY(),
                    (int)pair.getValue().getPos().getX(),
                    (int)pair.getValue().getPos().getY(),
                    HexColors.RED
            );
        }
    }

    private void drawGraphs(GameContainer gc) {
        int w = 1170;
        int h = 250;
        int marginBottom = 5;
        for (int i = 0; i < trackers.size(); i++) {
            Graphics.drawGraph(
                    gc.getRenderer(),
                    trackers.get(i).getValues(),
                    trackers.get(i).getRegression().getSlope(),
                    trackers.get(i).getRegression().getOrigin(),
                    (gc.getRenderer().getW() - w) / 2,
                    (gc.getRenderer().getH() - (h + marginBottom)),
                    w,
                    h,
                    i == 0,
                    (i == 0) ? 0x77FFFFFF : 0x00000000,
                    colors.get(i).getKey(),
                    colors.get(i).getValue());
        }
    }

    private void drawText(Renderer r, String text, int posX, int col) {
        r.drawText(text, posX, textPosY, col);
        textPosY += 30;
    }

    @Override
    public void render(GameContainer gc) {
        gc.getRenderer().clear(HexColors.GREY);

        drawBalls(gc);

        drawCollisions(gc);

        gc.getRenderer().drawCircle((int)gc.getInput().getMouseX(), (int)gc.getInput().getMouseY(), 3, HexColors.RED);

        if (selectedBall != null) {
            gc.getRenderer().drawLine(
                    (int)gc.getInput().getMouseX(),
                    (int)gc.getInput().getMouseY(),
                    (int)selectedBall.getPos().getX(),
                    (int)selectedBall.getPos().getY(),
                    HexColors.BLUE
            );
        }

        drawGraphs(gc);

        textPosY = 10;
        gc.getRenderer().drawFillRectangle(5, 5, 50 * 12, 125, 0x77FFFFFF);
        //drawText(gc.getRenderer(), String.format("Media de la magnitud de la velocidad: %.3f", avgVel), 10, HexColors.BLACK);
        drawText(gc.getRenderer(), String.format("Individuos especie 1: %d", f.getIndividuals().size()), 10, HexColors.BLACK);
        drawText(gc.getRenderer(), String.format("Individuos especie 2: %d", p.getIndividuals().size()), 10, HexColors.BLACK);
        drawText(gc.getRenderer(), String.format("Individuos especie 3: %d", s.getIndividuals().size()), 10, HexColors.BLACK);
        for (int i = 0; i < trackers.size(); i++) {
            drawText(gc.getRenderer(),
                    String.format("regresion " + i + ": %.3fX + %.3f  r^2: %.3f",
                            trackers.get(i).getRegression().getSlope(),
                            trackers.get(i).getRegression().getOrigin(),
                            trackers.get(i).getRegression().getR2()),
                    10, HexColors.BLACK);
        }
        if (velCache != null) {
            drawText(gc.getRenderer(), velCache.toString(), 10, HexColors.RED);
        }
    }

}
