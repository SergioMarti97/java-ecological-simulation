package games;

import model.neuronal.Neuron;
import olcPGEApproach.AbstractGame;
import olcPGEApproach.GameContainer;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.vectors.points2d.Vec2df;

import java.util.ArrayList;
import java.util.Random;

public class NeuronGame implements AbstractGame {

    private ArrayList<Neuron> neurons = new ArrayList<>();

    @Override
    public void initialize(GameContainer gc) {
        Random rnd = new Random();
        int x = 100;
        for (int i = 0; i < 5; i++) {
            Neuron n = new Neuron(new Vec2df(x, 100), 30, HexColors.YELLOW);
            n.setId(neurons.size());
            n.setModule(rnd.nextInt(20) - 10);
            neurons.add(n);
            x += 130;
        }
        neurons.get(0).getChildren().add(neurons.get(1));
        neurons.get(1).getChildren().add(neurons.get(2));
        neurons.get(2).getChildren().add(neurons.get(3));
        neurons.get(3).getChildren().add(neurons.get(4));
    }

    @Override
    public void update(GameContainer gc, float v) {
        for (Neuron n : neurons) {
            n.setValue(0);
        }
        neurons.get(0).setValue((float) gc.getInput().getMouseX());
        for (Neuron n : neurons) {
            n.giveValueToChildren();
        }
    }

    @Override
    public void render(GameContainer gc) {
        gc.getRenderer().clear(HexColors.WHITE);
        for (Neuron n : neurons) {
            n.drawYourSelf(gc.getRenderer());
        }
    }

}
