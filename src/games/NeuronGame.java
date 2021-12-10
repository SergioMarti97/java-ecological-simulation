package games;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import model.entities.balls.Ball;
import model.neuronal.Brain;
import model.neuronal.BrainUtils;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;
import olcPGEApproach.Input;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.vectors.points2d.Vec2df;

import java.util.Random;

public class NeuronGame implements AbstractGame {

    private final String path = "C:\\Users\\Sergio\\IdeaProjects\\JAVAFX\\javafx-pelotas\\resources\\brains\\brain.txt";

    private Brain brain;

    private Ball inputBall;

    private Ball outputBall;

    @Override
    public void initialize(GameContainer gc) {
        brain = new Brain();
        //BrainUtils.readFromFile(brain, path);

        String[] in = {"i1", "i2"};
        String[] out = {"o1", "o2"};

        brain = BrainUtils.buildSimpleBrain(in, out, 4, 1);

        brain.calRealSize();

        outputBall = new Ball(new Vec2df(), 12, HexColors.YELLOW);
        inputBall = new Ball(new Vec2df(), 12, HexColors.ORANGE);
    }

    private void updateUserInput(Input input, float dt) {
        brain.updateUserInput(input, dt);

        if (input.isKeyDown(KeyCode.R)) {
            BrainUtils.makeRndConnection(brain, new Random(), 1, -1);
        }

        if (input.isKeyDown(KeyCode.T)) {
            String file = "C:\\Users\\Sergio\\IdeaProjects\\JAVAFX\\javafx-pelotas\\resources\\brains\\result_test.txt";
            BrainUtils.saveTest(brain, "i", "o", -100, 100, 1, file);
            System.out.println("saved test");
        }

        if (input.isKeyDown(KeyCode.SPACE)) {
            BrainUtils.saveToFile(brain, path);
            System.out.println("saved brain");
        }

        if (input.isKeyDown(KeyCode.A)) {
            brain.addLayer(0);
        }
    }

    @Override
    public void update(GameContainer gc, float dt) {
        inputBall.getPos().setX((float)gc.getInput().getMouseX());
        inputBall.getPos().setY((float)gc.getInput().getMouseY());

        brain.getIn("i1").setInput(inputBall.getPos().getX());
        brain.getIn("i2").setInput(inputBall.getPos().getY());
        brain.update(dt);

        updateUserInput(gc.getInput(), dt);

        outputBall.getPos().setX((float)brain.getOut("o1").getOutput());
        outputBall.getPos().setY((float)brain.getOut("o2").getOutput());
    }

    @Override
    public void render(GameContainer gc) {
        gc.getRenderer().clear(0xFFB2D8D8);
        brain.drawYourSelf(gc.getRenderer());
        inputBall.drawYourSelf(gc.getRenderer());
        outputBall.drawYourSelf(gc.getRenderer());
    }

}
