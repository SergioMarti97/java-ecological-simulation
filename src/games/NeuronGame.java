package games;

import javafx.scene.input.KeyCode;
import model.neuronal.Brain;
import model.neuronal.BrainUtils;
import model.neuronal.Neuron;
import model.neuronal.NeuronFunctions;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;
import olcPGEApproach.Input;
import olcPGEApproach.gfx.HexColors;

import java.util.Random;

public class NeuronGame implements AbstractGame {

    private final String path = "C:\\Users\\Sergio\\IdeaProjects\\JAVAFX\\javafx-pelotas\\resources\\brains\\brain.txt";

    private Brain brain;

    @Override
    public void initialize(GameContainer gc) {
        brain = new Brain();
        BrainUtils.readFromFile(brain, path);

        /*for (int i = 0; i < 2; i++) {
            Neuron n = BrainUtils.buildRndNeuron(new Random(), 12, 5);
            BrainUtils.addNeuron(brain, 2, n);
        }*/

        brain.calNeuronsPositions();
    }

    private void updateUserInput(Input input) {
        brain.updateUserInput(input);

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
    }

    @Override
    public void update(GameContainer gc, float dt) {
        brain.getIn("i").setInput(gc.getInput().getMouseX());
        brain.update(dt);
        updateUserInput(gc.getInput());
    }

    @Override
    public void render(GameContainer gc) {
        gc.getRenderer().clear(HexColors.GREY);
        brain.drawYourSelf(gc.getRenderer());
    }

}
