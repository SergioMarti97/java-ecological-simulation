package model.neuronal;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import model.Clickable2D;
import model.Drawable;
import model.entities.Updatable;
import model.physics.CollisionDetection;
import olcPGEApproach.Input;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2di;

import java.util.*;
import java.util.stream.Collectors;

public class Brain implements Updatable, Drawable, Clickable2D {

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

    private final ArrayList<ArrayList<Neuron>> layers = new ArrayList<>();

    // fields to draw the brain
    private final Vec2di size = new Vec2di(800, 600);

    private final Vec2di offset = new Vec2di();

    private final Vec2di paddingLT = new Vec2di(10, 10);

    private final Vec2di paddingRB = new Vec2di(10, 10);

    private final Vec2di realSize = new Vec2di();

    private final Vec2di sizeMiniRect = new Vec2di(15, 15);

    // fields to manage the user input
    private boolean isSelected = false;

    private boolean isMiniRectSelected = false;

    private Neuron selectedNeuron = null;

    private Connection selectedConnection = null;

    private Neuron selectedOriConnection = null;

    private Neuron selectedTarConnection = null;

    private final Vec2di mousePos = new Vec2di();

    private final Vec2di offsetBefore = new Vec2di();

    private final Vec2di sizeBefore = new Vec2di();

    private final double INC_WEIGHT_CONNECTION = 0.1;

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
        calNeuronsPositions();
    }

    public void addLayer(int layerBefore) {
        layerBefore++;
        for (int i = layerBefore; i < layers.size(); i++) {
            for (Neuron n : layers.get(i)) {
                n.setLayer(n.getLayer() + 1);
            }
        }
        layers.add(layerBefore, new ArrayList<>());
        calNeuronsPositions();
    }

    public ArrayList<Neuron> removeLayer(int layer) {
        ArrayList<Neuron> r = layers.remove(layer);
        calNeuronsPositions();
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

    @Override
    public boolean isMouseInside(double mouseX, double mouseY) {
        return CollisionDetection.isPointInRect(mouseX, mouseY, offset.getX(), offset.getY(), size.getX(), size.getY());
    }

    public Neuron isMouseInsideNeuron(double mouseX, double mouseY) {
        for (Neuron n : neurons.values()) {
            if (n.isMouseInside(mouseX, mouseY)) {
                return n;
            }
        }
        return null;
    }

    public boolean isMouseInsideMiniRect(double mouseX, double mouseY) {
        return CollisionDetection.isPointInRect(mouseX, mouseY,
                offset.getX() + (size.getX() - sizeMiniRect.getX()),
                offset.getY() + (size.getY() - sizeMiniRect.getY()),
                sizeMiniRect.getX(),
                sizeMiniRect.getY());
    }

    public void updateUserInput(Input input, float dt) {
        if (input.isButtonDown(MouseButton.PRIMARY)) {
            selectedNeuron = isMouseInsideNeuron(input.getMouseX(), input.getMouseY());

            if (selectedNeuron == null) {
                Map.Entry<Neuron, Connection> con;
                for (Neuron n : getAllNeurons()) {
                    con = n.isMouseInsideConnection(input.getMouseX(), input.getMouseY());
                    if (con != null) {
                        selectedConnection = con.getValue();
                        selectedOriConnection = n;
                        selectedTarConnection = con.getKey();
                        break;
                    }
                }
            }

            if (isMouseInsideMiniRect(input.getMouseX(), input.getMouseY())) {
                isMiniRectSelected = true;
                mousePos.setX((int)input.getMouseX());
                mousePos.setY((int)input.getMouseY());
                sizeBefore.setX(size.getX());
                sizeBefore.setY(size.getY());
            }

            if (isMouseInside(input.getMouseX(), input.getMouseY()) &&
                    selectedNeuron == null &&
                    !isMiniRectSelected &&
                    selectedConnection == null) {
                isSelected = true;
                mousePos.setX((int)input.getMouseX());
                mousePos.setY((int)input.getMouseY());
                offsetBefore.setX(offset.getX());
                offsetBefore.setY(offset.getY());
            }
        }
        if (input.isButtonHeld(MouseButton.PRIMARY)) {
            if (selectedNeuron != null) {
                selectedNeuron.getB().getPos().setX((float) input.getMouseX());
                selectedNeuron.getB().getPos().setY((float) input.getMouseY());
                calConnectionsPositions();
            }
            if (isSelected) {
                offset.setX(offsetBefore.getX() + ((int) input.getMouseX() - mousePos.getX()));
                offset.setY(offsetBefore.getY() + ((int) input.getMouseY() - mousePos.getY()));
                calNeuronsPositions();
            }
            if (isMiniRectSelected) {
                size.setX(sizeBefore.getX() + ((int)input.getMouseX() - sizeBefore.getX()));
                size.setY(sizeBefore.getY() + ((int)input.getMouseY() - sizeBefore.getY()));
                calNeuronsPositions();
            }
        }

        if (selectedConnection != null) {
            selectedConnection.addToWeight((input.getScroll() / 100.0) * dt);

            if (input.isKeyHeld(KeyCode.PLUS)) {
                selectedConnection.addToWeight(INC_WEIGHT_CONNECTION * dt);
            }
            if (input.isKeyHeld(KeyCode.MINUS)) {
                selectedConnection.addToWeight(-INC_WEIGHT_CONNECTION * dt);
            }
            if (input.isKeyHeld(KeyCode.X)) {
                selectedOriConnection.getChildren().remove(selectedTarConnection, selectedConnection);
            }

            if (selectedConnection.getWeight() > 1) {
                selectedConnection.setWeight(1);
            }
            if (selectedConnection.getWeight() < -1) {
                selectedConnection.setWeight(-1);
            }
        }

        if (input.isButtonUp(MouseButton.PRIMARY)) {
            selectedNeuron = null;
            selectedConnection = null;
            selectedOriConnection = null;
            selectedTarConnection = null;

            isSelected = false;
            isMiniRectSelected = false;

            mousePos.setX(0);
            mousePos.setY(0);

            offsetBefore.setX(0);
            offsetBefore.setY(0);
            sizeBefore.setX(0);
            sizeBefore.setY(0);
        }
    }

    @Override
    public void update(float dt) {
        // clear neuron inputs
        for (Map.Entry<Integer, Neuron> e : neurons.entrySet()) {
            // if the neuron is an input, not clear its input
            if (!inputs.containsValue(e.getKey())) {
                e.getValue().clearInput();
            }
        }
        // update all neurons
        for (Neuron n : neurons.values().stream().sorted(Comparator.comparingInt(Neuron::getLayer)).collect(Collectors.toList())) {
            n.update(dt);
        }
    }

    // drawing methods

    public void setDrawingParameters(Vec2di size, Vec2di offset, Vec2di paddingLT, Vec2di paddingRB) {
        // Copy the values
        this.size.setX(size.getX());
        this.size.setY(size.getY());

        this.offset.setX(offset.getX());
        this.offset.setY(offset.getY());

        this.paddingLT.setX(paddingLT.getX());
        this.paddingLT.setY(paddingLT.getY());

        this.paddingRB.setX(paddingRB.getX());
        this.paddingRB.setY(paddingRB.getY());

        calRealSize();
    }

    /**
     * Cal the real size of the square to paint the brain
     */
    public void calRealSize() {
        realSize.setX(size.getX() - paddingLT.getX() - paddingRB.getX());
        realSize.setY(size.getY() - paddingLT.getY() - paddingRB.getY());
    }

    /**
     * For each neuron, update the position of the connection
     */
    public void calConnectionsPositions() {
        for (Neuron n : getAllNeurons()) {
            n.calConnectionPos();
        }
    }

    public void calNeuronsPositions() {
        calRealSize();

        // todo hay problemas con el calculo del ancho de la seccion por ser una division entera, hay que acarrear el error
        int x = offset.getX() + paddingLT.getX();

        int sectionW = realSize.getX();
        if (getNumLayers() >= 1) {
            sectionW /= getNumLayers();
        }

        x += sectionW / 2;
        for (ArrayList<Neuron> layer : layers) {
            int y = offset.getY() + paddingLT.getY();

            int sectionH = realSize.getY();
            if (layer.size() >= 1) {
                sectionH /= layer.size();
            }

            y += sectionH / 2;
            for (Neuron n : layer) {
                n.getB().getPos().setX(x);
                n.getB().getPos().setY(y);
                y += sectionH;
            }
            x += sectionW;
        }

        calConnectionsPositions();
    }

    private void drawSections(Renderer r) {
        int sectionW = realSize.getX() / getNumLayers();
        int x = offset.getX() + paddingLT.getX();
        int y = offset.getY() + paddingLT.getY();
        for (int i = 0; i < getNumLayers(); i++) {
            r.drawRect(x, y, sectionW, realSize.getY(), HexColors.BLACK);
            String layerName = "Layer " + i;
            if (i == 0) {
                layerName = "Input layer";
            }
            if (i == getNumLayers() - 1) {
                layerName = "Output layer";
            }
            r.drawText(layerName, x + 5, y + 5, HexColors.BLACK);
            x += sectionW;
        }
    }

    @Override
    public void drawYourSelf(Renderer r) {
        r.drawFillRect(
                offset.getX(),
                offset.getY(),
                size.getX(),
                size.getY(),
                0xFF8EACAC
        );
        drawSections(r);
        for (Neuron n : neurons.values().stream().sorted(Comparator.comparingInt(Neuron::getLayer)).collect(Collectors.toList())) {
            n.drawYourSelf(r);
        }
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
        calNeuronsPositions();
    }

    public Vec2di getSize() {
        return size;
    }

    public Vec2di getOffset() {
        return offset;
    }

    public Vec2di getPaddingLT() {
        return paddingLT;
    }

    public Vec2di getPaddingRB() {
        return paddingRB;
    }

}
