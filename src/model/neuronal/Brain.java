package model.neuronal;

import model.entities.Updatable;

import java.util.*;
import java.util.stream.Collectors;

public class Brain implements Updatable {

    /**
     * layer inputs: name and id
     */
    private final HashMap<String, Integer> inputs = new HashMap<>();

    /**
     * Layer outputs: name and id
     */
    private final HashMap<String, Integer> outputs = new HashMap<>();

    /**
     * All neurons: id and neuron
     */
    private final HashMap<Integer, Neuron> neurons = new HashMap<>();

    /**
     * The layers of the brain
     */
    private final ArrayList<ArrayList<Neuron>> layers = new ArrayList<>();

    // Constructors

    public Brain() {

    }

    public Brain(HashMap<String, Neuron> inputs, HashMap<String, Neuron> outputs, ArrayList<Neuron> neurons) {
        for (Map.Entry<String, Neuron> e : inputs.entrySet()) {
            this.inputs.put(e.getKey(), e.getValue().getId());
        }
        for (Map.Entry<String, Neuron> e : outputs.entrySet()) {
            this.outputs.put(e.getKey(), e.getValue().getId());
        }
        int numLayers = 2;
        for (Neuron n : neurons) {
            this.neurons.put(n.getId(), n);
            if (n.getLayer() > numLayers) {
                numLayers = n.getLayer();
            }
        }
        // Make the layers of the neurons
        setNumLayers(numLayers);
    }

    // methods

    public void addLayer() {
        layers.add(new ArrayList<>());
    }

    public void addLayer(int layerBefore) {
        layerBefore++;
        for (int i = layerBefore; i < layers.size(); i++) {
            for (Neuron n : layers.get(i)) {
                n.setLayer(n.getLayer() + 1);
            }
        }
        layers.add(layerBefore, new ArrayList<>());
    }

    public ArrayList<Neuron> removeLayer(int layer) {
        ArrayList<Neuron> r = layers.remove(layer);
        return r;
    }

    public void addNeuron(Brain brain, int layer, Neuron neuron) {
        neuron.setId(brain.getNeurons().size());
        neuron.setLayer(layer);
        brain.getNeurons().put(neuron.getId(), neuron);
    }

    public void makeConnection(int idOri, int idTar, double weight) {
        neurons.get(idOri).addConnexion(neurons.get(idTar), weight);
    }

    public Neuron getIn(String name) {
        return neurons.get(inputs.get(name));
    }

    public Neuron getOut(String name) {
        return neurons.get(outputs.get(name));
    }

    // update methods

    /**
     * This method clears the neuron inputs.
     * Input neurons not clear its input.
     */
    private void clearNeuronInputs() {
        for (Map.Entry<Integer, Neuron> e : neurons.entrySet()) {
            if (!inputs.containsValue(e.getKey())) {
                e.getValue().clearInput();
            }
        }
    }

    /**
     * This method updates all the neurons.
     * For update, it sorts the neurons by layer.
     * @param dt the time between two frames
     */
    private void updateNeurons(float dt) {
        for (Neuron n : neurons.values().stream()
                .sorted(Comparator.comparingInt(Neuron::getLayer))
                .collect(Collectors.toList())) {
            n.update(dt);
        }
    }

    @Override
    public void update(float dt) {
        clearNeuronInputs();
        updateNeurons(dt);
    }

    // Getters and Setters

    public HashMap<Integer, Neuron> getNeurons() {
        return neurons;
    }

    public Collection<Neuron> getAllNeurons() {
        return neurons.values();
    }

    public Neuron getNeuron(int id) {
        return neurons.get(id);
    }

    public HashMap<String, Integer> getInputs() {
        return inputs;
    }

    public HashMap<String, Integer> getOutputs() {
        return outputs;
    }

    public ArrayList<ArrayList<Neuron>> getLayers() {
        return layers;
    }

    public int getNumLayers() {
        return layers.size();
    }

    public void setNumLayers(int numLayers) {
        for (int i = 0; i < numLayers; i++) {
            layers.add(new ArrayList<>());
        }
        for (Neuron n : getAllNeurons()) {
            layers.get(n.getLayer()).add(n);
        }
    }

}
