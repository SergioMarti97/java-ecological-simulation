package model.neuronal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class BrainUtils {

    public static int calRndInt(Random rnd, int max, int min) {
        return rnd.nextInt(max - min) + min;
    }

    public static float calRndFloat(Random rnd, float max, float min) {
        return (max - min) * rnd.nextFloat() + min;
    }

    /**
     *         // Create neurons
     *         final ArrayList<Neuron> neurons = new ArrayList<>();
     *         for (int i = 0; i < 5; i++) {
     *             Neuron n = new Neuron(calRndInt(rnd, 3, 1), NeuronFunctions.obtainRandomNF(rnd).name());
     *             n.setId(neurons.size());
     *             neurons.add(n);
     *         }
     *
     *         // Make connexions between neurons
     *         neurons.get(0).setLayer(0);
     *         neurons.get(0).addConnexion(neurons.get(1), calRndFloat(rnd, 1, -1));
     *         neurons.get(0).addConnexion(neurons.get(2), calRndFloat(rnd, 1, -1));
     *         neurons.get(1).setLayer(1);
     *         neurons.get(2).setLayer(1);
     *
     *         neurons.get(1).addConnexion(neurons.get(3), calRndFloat(rnd, 1, -1));
     *         neurons.get(2).addConnexion(neurons.get(3), calRndFloat(rnd, 1, -1));
     *         neurons.get(3).setLayer(2);
     *
     *         neurons.get(3).addConnexion(neurons.get(4), calRndFloat(rnd, 1, -1));
     *         neurons.get(4).setLayer(3);
     *
     *         // Input and Output layer
     *         final HashMap<String, Neuron> inputs = new HashMap<>();
     *         inputs.put("i", neurons.get(0));
     *
     *         final HashMap<String, Neuron> output = new HashMap<>();
     *         output.put("o", neurons.get(4));
     *
     *         // Create the brain
     *         return new Brain(inputs, output, neurons);
     */
    public static Brain buildSimpleBrain(String[] in, String[] out, int maxBias, int minBias) {
        Random rnd = new Random();
        final ArrayList<Neuron> neurons = new ArrayList<>();
        final HashMap<String, Neuron> input = new HashMap<>();
        for (String i : in) {
            Neuron n = buildRndNeuron(rnd, maxBias, minBias);
            n.setId(neurons.size());
            n.setLayer(0);
            neurons.add(n);
            input.put(i, n);
        }
        final HashMap<String, Neuron> output = new HashMap<>();
        for (String o : out) {
            Neuron n = buildRndNeuron(rnd, maxBias, minBias);
            n.setId(neurons.size());
            n.setLayer(1);
            neurons.add(n);
            output.put(o, n);
        }
        return new Brain(input, output, neurons);
    }

    public static Neuron buildRndNeuron(Random rnd, int maxBias, int minBias) {
        return new Neuron(calRndInt(rnd, maxBias, minBias), NeuronFunctions.obtainRandomNF(rnd).name());
    }

    public static void addNeuron(Brain brain, int layer, Neuron neuron) {
        neuron.setId(brain.getNeurons().size());
        neuron.setLayer(layer);
        brain.getNeurons().put(neuron.getId(), neuron);
    }

    public static void addRndNeuron(Brain brain, Random rnd, int maxBias, int minBias, int layer) {
        addNeuron(brain, layer, buildRndNeuron(rnd, maxBias, minBias));
    }

    public static void makeRndConnection(Brain brain, Random rnd, float maxWeight, float minWeight) {
        List<Neuron> before = brain.getAllNeurons().stream()
                .filter(x -> !brain.getOutputs().containsValue(x.getId()))
                .collect(Collectors.toList());
        List<Neuron> after = brain.getAllNeurons().stream()
                .filter(x -> !brain.getInputs().containsValue(x.getId()))
                .collect(Collectors.toList());

        Neuron ori;
        Neuron tar;
        do {
            ori = before.get(rnd.nextInt(before.size()));
            tar = after.get(rnd.nextInt(after.size()));
        } while (ori.equals(tar) || ori.getLayer() >= tar.getLayer());

        brain.makeConnection(ori.getId(), tar.getId(), calRndFloat(rnd, maxWeight, minWeight));
    }

    // IO methods

    public static void readFromFile(Brain b, String file) {
        int numLayers = 2;
        try (FileReader fileReader = new FileReader(file)) {
            BufferedReader br = new BufferedReader(fileReader);

            br.readLine(); // the first line are the headers

            ArrayList<String> connections = new ArrayList<>();
            String line;
            do {
                line = br.readLine();
                if (line != null) {
                    String[] info = line.split(";");

                    Neuron n = new Neuron(Double.parseDouble(info[4]), info[3]);
                    n.setId(Integer.parseInt(info[1]));
                    int layer = Integer.parseInt(info[2]);
                    n.setLayer(layer);

                    if (numLayers < layer) {
                        numLayers = layer;
                    }

                    for (int i = 5; i < info.length; i++) {
                        connections.add(info[1] + ":" + info[i]);
                    }

                    b.getNeurons().put(n.getId(), n);

                    if (info[0].toCharArray()[0] == 'i') { // input neuron
                        b.getInputs().put(info[0], n.getId());
                    }
                    if (info[0].toCharArray()[0] == 'o') { // output neuron
                        b.getOutputs().put(info[0], n.getId());
                    }
                }
            } while (line != null);

            b.setNumLayers(numLayers);

            for (String con : connections) {
                String[] info = con.split(":");
                b.makeConnection(
                        Integer.parseInt(info[0]),
                        Integer.parseInt(info[1]),
                        Double.parseDouble(info[2]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToFile(Brain b, String file) {
        try (PrintWriter pw = new PrintWriter(file)) {
            // Headers
            pw.println("type;id;layer;function;bias;connections");

            // Save inputs
            for (Map.Entry<String, Integer> e : b.getInputs().entrySet()) {
                Neuron n = b.getNeurons().get(e.getValue());
                pw.println(e.getKey() + ";" + n.toString());
            }
            // Save neurons (inter neurons)
            for (Map.Entry<Integer, Neuron> e : b.getNeurons().entrySet()) {
                if (!b.getInputs().containsValue(e.getKey()) && !b.getOutputs().containsValue(e.getKey())) {
                    pw.println("e;" + e.getValue().toString());
                }
            }
            // Save outputs
            for (Map.Entry<String, Integer> e : b.getOutputs().entrySet()) {
                Neuron n = b.getNeurons().get(e.getValue());
                pw.println(e.getKey() + ";" + n.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveTest(Brain brain, String input, String output, double min, double max, double step, String file) {
        try (PrintWriter pw = new PrintWriter(file)) {
            for (double i = min; i < max; i += step) {
                brain.getIn(input).setInput(i);
                brain.update(0);
                double r = brain.getOut(output).result();
                pw.println("" + i + ';' + r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
