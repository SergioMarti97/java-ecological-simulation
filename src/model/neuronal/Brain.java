package model.neuronal;

import model.Drawable;
import model.entities.Updatable;
import olcPGEApproach.gfx.Renderer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Brain implements Updatable, Drawable {

    private int numLayers = 2;

    /**
     * layer inputs
     * name and id
     */
    private final HashMap<String, Integer> inputs = new HashMap<>();

    /**
     * Layer outputs
     * name and id
     */
    private final HashMap<String, Integer> outputs = new HashMap<>();

    /**
     * All neurons
     * id and neuron
     */
    private final HashMap<Integer, Neuron> neurons = new HashMap<>();

    public Brain() {

    }

    public void readFromFile(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            do {
                line = br.readLine();

            } while (line != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveToFile(String file) {

    }

    public Neuron getIn(String name) {
        return neurons.get(inputs.get(name));
    }

    public Neuron getOut(String name) {
        return neurons.get(outputs.get(name));
    }

    @Override
    public void update(float dt) {
        // clear neuron inputs
        for (Map.Entry<Integer, Neuron> e : neurons.entrySet()) {
            if (!inputs.containsValue(e.getKey())) {
                e.getValue().clearInput();
            }
        }
        // update all neurons
        for (Map.Entry<Integer, Neuron> e : neurons.entrySet()) {
            e.getValue().update(dt);
        }
    }

    @Override
    public void drawYourSelf(Renderer r) {
        for (Map.Entry<Integer, Neuron> e : neurons.entrySet()) {
            e.getValue().drawYourSelf(r);
        }
    }
}
