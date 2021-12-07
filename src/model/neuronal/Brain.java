package model.neuronal;

import model.Drawable;
import model.entities.Updatable;
import olcPGEApproach.gfx.Renderer;

import java.util.ArrayList;
import java.util.HashMap;

public class Brain implements Updatable, Drawable {

    private int numLayers = 2;

    private final HashMap<String, Neuron> inputs = new HashMap<>();

    private final HashMap<String, Neuron> outputs = new HashMap<>();

    private final ArrayList<Neuron> neurons = new ArrayList<>();

    public Brain() {

    }

    @Override
    public void update(float dt) {
        // clear neuron inputs
        for (Neuron n : neurons) {
            n.setInput(0);
        }
        // update all neurons
        for (Neuron n : neurons) {
            n.update(dt);
        }
    }

    @Override
    public void drawYourSelf(Renderer r) {
        for (Neuron n : neurons) {
            n.drawYourSelf(r);
        }
    }
}
