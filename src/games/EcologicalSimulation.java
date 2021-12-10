package games;

import javafx.scene.input.MouseButton;
import javafx.util.Pair;
import model.Clickable2D;
import model.entities.balls.Ball;
import model.entities.Updatable;
import model.entities.balls.DynamicBall;
import model.physics.Dynamical;
import model.entities.factories.BallFactory;
import model.entities.live.Energetic;
import model.entities.live.LivingBall;
import model.entities.live.LivingDynamicBall;
import model.entities.live.Reproducible;
import model.physics.BallPhysics;
import model.statistics.Graphics;
import model.statistics.regression.Regression;
import model.statistics.regression.RegressionObj;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;
import olcPGEApproach.Input;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

import java.util.*;
import java.util.stream.Collectors;

public class EcologicalSimulation implements AbstractGame {

    private int id = 0;

    private ArrayList<Ball> balls;

    private ArrayList<Integer> deaths;

    private ArrayList<Ball> babies;

    private double avgVel = 0;

    private double time = 0;

    private Deque<Pair<Double, Double>> values1;

    private Deque<Pair<Double, Double>> values2;

    private int numDynamicBalls = 0;

    private int numStaticBalls = 0;

    private RegressionObj reg1;

    private RegressionObj reg2;

    private int textPosY = 10;

    private Random rnd;

    private Ball selectedBall = null;

    private Vec2df velCache = null;

    @Override
    public void initialize(GameContainer gc) {
        rnd = new Random();
        values1 = new LinkedList<>();
        values2 = new LinkedList<>();
        balls = new ArrayList<>();
        deaths = new ArrayList<>();
        babies = new ArrayList<>();
        reg1 = new RegressionObj(0, 0, 0);
        reg2 = new RegressionObj(0, 0, 0);
        init(gc);
    }

    private void init(GameContainer gc) {
        values1.clear();
        values2.clear();
        balls.clear();
        balls.addAll(BallFactory.buildLivingDynamicBalls(
                id,
                100,
                gc.getRenderer().getW(),
                gc.getRenderer().getH(),
                30,
                8,
                10,
                100000
        ));
        id = balls.size();
        balls.addAll(BallFactory.buildLivingBalls(
                id,
                0,
                gc.getRenderer().getW(),
                gc.getRenderer().getH(),
                5,
                2,
                1000,
                50
        ));
        id = balls.size();
    }

    private void updateBalls(int w, int h, float dt) {
        for (Ball b : balls) {
            BallPhysics.ballVsEdgesFull(w, h, b);
            if (b instanceof Updatable) {
                ((Updatable) b).update(dt);
            }
            if (b instanceof Dynamical) {
                avgVel += ((Dynamical) b).getVel().mag2();
            }
            if (b instanceof Energetic) {
                if (((Energetic) b).hasToDie()) {
                    deaths.add(b.getId());
                }
            }
            if (b instanceof Reproducible) {
                if (((Reproducible) b).canMakeBaby()) {
                    if (b instanceof LivingBall) {
                        babies.add((LivingBall) ((Reproducible) b).makeBaby());
                    }
                    if (b instanceof LivingDynamicBall) {
                        babies.add((LivingDynamicBall) ((Reproducible) b).makeBaby());
                    }
                }
            }
            if (b instanceof LivingBall) {
                final int threshold = b.getR();
                if (b.getPos().getX() <= threshold) {
                    b.getPos().setX(rnd.nextInt(w - threshold) + threshold);
                }
                if (b.getPos().getY() <= threshold) {
                    b.getPos().setY(rnd.nextInt(h - threshold) + threshold);
                }
                if (b.getPos().getX() >= w - threshold) {
                    b.getPos().setX(rnd.nextInt(w - threshold) + threshold);
                }
                if (b.getPos().getY() >= h - threshold) {
                    b.getPos().setY(rnd.nextInt(h - threshold) + threshold);
                }
            }
        }
    }

    private void updateBallCollisions() {
        BallPhysics.colliding.clear();
        BallPhysics.detectCollisions(balls);
        for (Pair<Ball, Ball> pair : BallPhysics.colliding) {
            Ball b1 = pair.getKey();
            Ball b2 = pair.getValue();
            if (b1 instanceof LivingBall && b2 instanceof LivingBall) {
                BallPhysics.updateStaticCollision(b1, b2);
            }
            if (b1 instanceof LivingDynamicBall && b2 instanceof LivingDynamicBall) {
                BallPhysics.updateDynamicCollision((LivingDynamicBall)b1, (LivingDynamicBall)b2);
            }
            if (b1 instanceof LivingBall && b2 instanceof LivingDynamicBall) {
                LivingBall lb = (LivingBall) b1;
                LivingDynamicBall ldb = (LivingDynamicBall) b2;
                ldb.setEnergy(ldb.getEnergy() + lb.getEnergy());
                deaths.add(b1.getId());
            }
            if (b1 instanceof LivingDynamicBall && b2 instanceof LivingBall) {
                LivingBall lb = (LivingBall) b2;
                LivingDynamicBall ldb = (LivingDynamicBall) b1;
                ldb.setEnergy(ldb.getEnergy() + lb.getEnergy());
                deaths.add(b2.getId());
            }
        }
    }

    private void updateDeathsAndBabies() {
        balls.removeIf((dynamicBall) -> deaths.contains(dynamicBall.getId()));
        babies.iterator().forEachRemaining(b -> {
            b.setId(id);
            id++;
            balls.add(b);
        });
        deaths.clear();
        babies.clear();
    }

    private void calRegression(Collection<Pair<Double, Double>> values, RegressionObj reg) {
        double[] xValues = new double[values.size()];
        double[] yValues = new double[values.size()];
        int index = 0;
        for (Pair<Double, Double> p : values) {
            xValues[index] = p.getKey();
            yValues[index] = p.getValue();
            index++;
        }
        reg.setSlope(Regression.slope(xValues, yValues, values.size()));
        reg.setOrigin(Regression.origin(xValues, yValues, values.size()));
        double r = Regression.calR(xValues, yValues, values.size());
        reg.setR2(r * r);
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

        updateBalls(gc.getRenderer().getW(), gc.getRenderer().getH(), dt);

        updateBallCollisions();

        updateDeathsAndBabies();

        updateUserInput(gc.getInput());

        ArrayList<Ball> dynamicBalls =
                (ArrayList<Ball>) balls.stream().filter(b -> b instanceof DynamicBall).collect(Collectors.toList());
        numDynamicBalls = dynamicBalls.size();

        ArrayList<Ball> staticBalls =
                (ArrayList<Ball>) balls.stream().filter(b -> b instanceof LivingBall).collect(Collectors.toList());
        numStaticBalls = staticBalls.size();

        // do the cals for some study variables
        avgVel = Math.sqrt(avgVel);
        avgVel /= numDynamicBalls;
        values1.push(new Pair<>(time, avgVel));
        values2.push(new Pair<>(time, (double)numDynamicBalls));
        final int numValues = 5000;
        if (values1.size() > numValues) {
            values1.removeLast();
        }
        if (values2.size() > numValues) {
            values2.removeLast();
        }

        // if there is more than two values
        // then the regression is cal
        if (values1.size() >= 2) {
            calRegression(values1, reg1);
        }
        if (values2.size() >= 2) {
            calRegression(values2, reg2);
        }

        // if all the balls die or the static balls grow out proportions
        // then the simulation is restart
        if (balls.size() == 0 || numStaticBalls >= 3000) {
            init(gc);
        }
    }

    private void drawBalls(GameContainer gc) {
        boolean drawEnergyBar = false;
        boolean drawArrows = false;
        for (Ball b : balls) {
            if (b instanceof DynamicBall) {
                DynamicBall db = (DynamicBall)b;
                if (db instanceof LivingDynamicBall) {
                    LivingDynamicBall ldb = (LivingDynamicBall) db;
                    ldb.drawYourSelf(gc.getRenderer(), drawArrows, false);
                } else {
                    db.drawYourSelf(gc.getRenderer(), drawArrows);
                }
            } else if (b instanceof LivingBall) {
                LivingBall lf = (LivingBall)b;
                lf.drawYourSelf(gc.getRenderer(), drawEnergyBar);
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
        Graphics.drawGraph(
                gc.getRenderer(),
                values1,
                reg1.getSlope(),
                reg1.getOrigin(),
                (gc.getRenderer().getW() - w) / 2,
                (gc.getRenderer().getH() - (h + marginBottom)),
                w,
                h,
                true,
                0x77FFFFFF,
                HexColors.RED,
                HexColors.GREEN);
        Graphics.drawGraph(
                gc.getRenderer(),
                values2,
                reg2.getSlope(),
                reg2.getOrigin(),
                (gc.getRenderer().getW() - w) / 2,
                (gc.getRenderer().getH() - (h + marginBottom)),
                w,
                h,
                false,
                0x00000000,
                HexColors.BLUE,
                HexColors.ORANGE);
    }

    private void drawText(Renderer r, String text, int posX, int col) {
        r.drawText(text, posX, textPosY, col);
        textPosY += 30;
    }

    @Override
    public void render(GameContainer gc) {
        gc.getRenderer().clear(HexColors.GREY);

        drawBalls(gc);

        //drawCollisions(gc);

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
        drawText(gc.getRenderer(), String.format("Individuos especie 1: %d", numDynamicBalls), 10, HexColors.BLACK);
        drawText(gc.getRenderer(), String.format("Individuos especie 2: %d", numStaticBalls), 10, HexColors.BLACK);
        drawText(gc.getRenderer(),
                String.format("regresion 1: %.3fX + %.3f  r^2: %.3f", reg1.getSlope(), reg1.getOrigin(), reg1.getR2()),
                10, HexColors.BLACK);
        drawText(gc.getRenderer(),
                String.format("regresion 2: %.3fX + %.3f  r^2: %.3f", reg2.getSlope(), reg2.getOrigin(), reg2.getR2()),
                10, HexColors.BLACK);
        if (velCache != null) {
            drawText(gc.getRenderer(), velCache.toString(), 10, HexColors.RED);
        }
    }

}
