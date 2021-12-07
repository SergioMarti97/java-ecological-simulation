package model.neuronal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

public class BrainUtils {

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

                    if (info[0].matches("i*")) { // input neuron
                        b.getInputs().put(info[0], n.getId());
                    }
                    if (info[0].matches("o*")) { // output neuron
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

}
