package games;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import model.neuronal.Brain;
import model.neuronal.BrainUtils;
import model.neuronal.Neuron;
import model.neuronal.NeuronFunctions;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;
import olcPGEApproach.Input;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.vectors.points2d.Vec2di;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class NeuronGame implements AbstractGame {

    private Brain brain;

    private int calRndInt(Random rnd, int max, int min) {
        return rnd.nextInt(max - min) + min;
    }

    private float calRndFloat(Random rnd, float max, float min) {
        return (max - min) * rnd.nextFloat() + min;
    }

    @Override
    public void initialize(GameContainer gc) {
        /*Random rnd = new Random();
        // Create neurons
        int x = 100;
        final ArrayList<Neuron> neurons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Neuron n = new Neuron(calRndInt(rnd, 3, 1), NeuronFunctions.obtainRandomNF(rnd).name());
            n.getB().getPos().setX(x);
            n.getB().getPos().setY(100);
            n.setId(neurons.size());
            neurons.add(n);
            x += 130;
        }

        // Make connexions between neurons
        neurons.get(0).setLayer(0);
        neurons.get(0).addConnexion(neurons.get(1), calRndFloat(rnd, 1, -1));
        neurons.get(0).addConnexion(neurons.get(2), calRndFloat(rnd, 1, -1));
        neurons.get(1).setLayer(1);
        neurons.get(2).setLayer(1);

        neurons.get(1).addConnexion(neurons.get(3), calRndFloat(rnd, 1, -1));
        neurons.get(2).addConnexion(neurons.get(3), calRndFloat(rnd, 1, -1));
        neurons.get(3).setLayer(2);

        neurons.get(3).addConnexion(neurons.get(4), calRndFloat(rnd, 1, -1));
        neurons.get(4).setLayer(3);

        // Input and Output layer
        final HashMap<String, Neuron> inputs = new HashMap<>();
        inputs.put("i", neurons.get(0));

        final HashMap<String, Neuron> output = new HashMap<>();
        output.put("o", neurons.get(4));

        // Create the brain
        brain = new Brain(inputs, output, neurons);*/
        brain = new Brain();
        BrainUtils.readFromFile(brain, "C:\\Users\\Sergio\\IdeaProjects\\JAVAFX\\javafx-pelotas\\resources\\brains\\brain.txt");
        brain.calNeuronsPositions();
    }

    private void updateUserInput(Input input) {
        brain.updateUserInput(input);

        if (input.isKeyDown(KeyCode.SPACE)) {
            BrainUtils.saveToFile(brain, "C:\\Users\\Sergio\\IdeaProjects\\JAVAFX\\javafx-pelotas\\resources\\brains\\brain.txt");
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
