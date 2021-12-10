package model.neuronal;

import model.Clickable2D;
import model.DrawUtils;
import model.Drawable;
import model.entities.Updatable;
import model.entities.balls.Ball;
import model.physics.CollisionDetection;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

import java.util.HashMap;
import java.util.Map;

/**
 * Now, the neuron is a composition of
 * classes and not inherits from the ball
 * class
 */
public class Neuron implements Updatable, Drawable, Clickable2D {

    private int id;

    private int numLayer = 0;

    private double input = 0;

    private double output = 0;

    private double bias;

    private final String functionName;

    private final NeuronFunction function;

    /**
     * Neuron and the weight of the connexion
     */
    private HashMap<Neuron, Connection> children = new HashMap<>();

    private Ball b;

    // Constructor
    public Neuron(double bias, String function) {
        b = new Ball(new Vec2df(), 20, 0xFFE5E8E8);
        this.bias = bias;
        functionName = function;
        this.function = NeuronFunctions.obtainFunction(function);
    }

    // Methods

    public void calOutput() {
        output = function.cal(input) + bias;
    }

    public void addToInput(double quantity) {
        input += quantity;
    }

    public void addConnexion(Neuron n, double weight) {
        children.put(n, new Connection(this.getB().getPos(), n.getB().getPos(), weight));
    }

    public void giveValueToChildren() {
        for (Map.Entry<Neuron, Connection> e : children.entrySet()) {
            e.getKey().addToInput(this.getOutput() * e.getValue().getWeight());
        }
    }

    public void calConnectionPos() {
        for (Map.Entry<Neuron, Connection> e : children.entrySet()) {
            e.getValue().calPointsPos();
        }
    }

    @Override
    public void update(float dt) {
        calOutput();
        giveValueToChildren();
    }

    @Override
    public void drawYourSelf(Renderer r) {
        for (Map.Entry<Neuron, Connection> e : children.entrySet()) {
            e.getValue().drawYourSelf(r);
        }
        DrawUtils.drawBall(r, b);
        r.drawText("{" + b.getId() + "}", (int)b.getPos().getX(), (int)b.getPos().getY(), HexColors.BLACK);
        r.drawText(String.format("layer: %d", numLayer), (int)b.getPos().getX(), (int)b.getPos().getY() + 25, HexColors.BLACK);
        r.drawText(functionName, (int)b.getPos().getX(), (int)b.getPos().getY() + 50, HexColors.BLACK);
        r.drawText(String.format("in: %.3f", input), (int)b.getPos().getX(), (int)b.getPos().getY() + 75, HexColors.BLACK);
        r.drawText(String.format("out: %.3f", output), (int)b.getPos().getX(), (int)b.getPos().getY() + 100, HexColors.GREEN);
        r.drawText(String.format("bias: %.3f", bias), (int)b.getPos().getX(), (int)b.getPos().getY() + 125, HexColors.RED);
    }

    @Override
    public boolean isMouseInside(double mouseX, double mouseY) {
        return CollisionDetection.isPointInCircle(mouseX, mouseY, b);
    }

    public Map.Entry<Neuron, Connection> isMouseInsideConnection(double mouseX, double mouseY) {
        for (Map.Entry<Neuron, Connection> e : children.entrySet()) {
            if (e.getValue().isMouseInside(mouseX, mouseY)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder con = new StringBuilder();
        for (Map.Entry<Neuron, Connection> e : children.entrySet()) {
            con.append(e.getKey().getId()).append(':').append(e.getValue().getWeight()).append(';');
        }
        return "" + id + ';' + numLayer + ';' + functionName + ';' + bias + ';' + con;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.b.setId(id);
    }

    public double getOutput() {
        return output;
    }

    public double getInput() {
        return input;
    }

    public void setInput(double input) {
        this.input = input;
    }

    public void clearInput() {
        input = 0;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public HashMap<Neuron, Connection> getChildren() {
        return children;
    }

    public void setChildren(HashMap<Neuron, Connection> children) {
        this.children = children;
    }

    public int getLayer() {
        return numLayer;
    }

    public void setLayer(int numLayer) {
        this.numLayer = numLayer;
    }

    public Ball getB() {
        return b;
    }

    public void setB(Ball b) {
        this.b = b;
    }

    public String getFunctionName() {
        return functionName;
    }

}
