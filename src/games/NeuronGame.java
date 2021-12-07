package games;

import javafx.scene.input.MouseButton;
import model.neuronal.Neuron;
import model.neuronal.NeuronFunctions;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;
import olcPGEApproach.Input;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.vectors.points2d.Vec2df;

import java.util.ArrayList;
import java.util.Random;

public class NeuronGame implements AbstractGame {

    private final ArrayList<Neuron> neurons = new ArrayList<>();

    private Neuron selectedNeuron = null;

    private int calRndInt(Random rnd, int max, int min) {
        return rnd.nextInt(max - min) + min;
    }

    private float calRndFloat(Random rnd, float max, float min) {
        return (max - min) * rnd.nextFloat() + min;
    }

    @Override
    public void initialize(GameContainer gc) {
        Random rnd = new Random();
        int x = 100;
        for (int i = 0; i < 5; i++) {
            Neuron n = new Neuron(calRndInt(rnd, 3, 1), NeuronFunctions.obtainRandomNF(rnd).name());
            n.getB().getPos().setX(x);
            n.getB().getPos().setY(100);
            n.setId(neurons.size());
            neurons.add(n);
            x += 130;
        }
        neurons.get(0).addConnexion(neurons.get(1), calRndFloat(rnd, 1, -1));
        neurons.get(1).addConnexion(neurons.get(2), calRndFloat(rnd, 1, -1));
        neurons.get(2).addConnexion(neurons.get(3), calRndFloat(rnd, 1, -1));
        neurons.get(3).addConnexion(neurons.get(4), calRndFloat(rnd, 1, -1));
    }

    private void updateUserInput(Input input) {
        if (input.isButtonDown(MouseButton.PRIMARY)) {
            for (Neuron n : neurons) {
                if (n.isMouseInside(input.getMouseX(), input.getMouseY())) {
                    selectedNeuron = n;
                }
            }
        }
        if (input.isButtonHeld(MouseButton.PRIMARY)) {
            if (selectedNeuron != null) {
                selectedNeuron.getB().getPos().setX((float) input.getMouseX());
                selectedNeuron.getB().getPos().setY((float) input.getMouseY());
            }
        }
        if (input.isButtonUp(MouseButton.PRIMARY)) {
            selectedNeuron = null;
        }
    }

    @Override
    public void update(GameContainer gc, float dt) {
        for (Neuron n : neurons) {
            n.setInput(0);
        }
        neurons.get(0).setInput((float) gc.getInput().getMouseX());
        for (Neuron n : neurons) {
            n.update(dt);
        }
        updateUserInput(gc.getInput());
    }

    @Override
    public void render(GameContainer gc) {
        gc.getRenderer().clear(HexColors.WHITE);
        for (Neuron n : neurons) {
            n.drawYourSelf(gc.getRenderer());
        }
    }

}
