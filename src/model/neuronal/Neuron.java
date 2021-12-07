package model.neuronal;

import model.Clickable2D;
import model.CustomColor;
import model.Drawable;
import model.entities.Updatable;
import model.entities.balls.Ball;
import model.physics.CollisionDetection;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

import java.util.HashMap;
import java.util.Map;

public class Neuron implements Updatable, Drawable, Clickable2D {

    private int id;

    private int numLayer = 0;

    private double input = 0;

    private double output = 0;

    private double bias;

    private final String functionName;

    private final NeuronFunction function;

    private HashMap<Neuron, Float> children = new HashMap<>();

    private Ball b;

    // Constructor
    public Neuron(double bias, String function) {
        b = new Ball(new Vec2df(), 20, HexColors.YELLOW);
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

    public void addConnexion(Neuron n, float weight) {
        children.put(n, weight);
    }

    public void giveValueToChildren() {
        for (Map.Entry<Neuron, Float> e : children.entrySet()) {
            e.getKey().addToInput(this.result() * e.getValue());
        }
    }

    @Override
    public void update(float dt) {
        calOutput();
        giveValueToChildren();
    }

    @Override
    public void drawYourSelf(Renderer r) {
        for (Map.Entry<Neuron, Float> e : children.entrySet()) {
            int sx = (int)b.getPos().getX();
            int sy = (int)b.getPos().getY();
            int ex = (int)e.getKey().getB().getPos().getX();
            int ey = (int)e.getKey().getB().getPos().getY();
            r.drawLine(sx, sy, ex, ey, HexColors.BLACK);
            String out = String.format("%.3f", e.getValue());
            int offText = (out.length() / 2) * 10;
            r.drawText(out,
                    (int)((ex - sx) / 2 + b.getPos().getX() - offText),
                    (int)((ey - sy) / 2 + b.getPos().getY()),
                    HexColors.BLUE);
        }
        b.drawYourSelf(r);
        r.drawText(functionName,(int)b.getPos().getX(), (int)b.getPos().getY(), HexColors.BLACK);
        r.drawText(String.format("in: %.3f", input), (int)b.getPos().getX(), (int)b.getPos().getY() + 25, HexColors.BLACK);
        r.drawText(String.format("out: %.3f", output), (int)b.getPos().getX(), (int)b.getPos().getY() + 50, HexColors.GREEN);
        r.drawText(String.format("bias: %.3f", bias), (int)b.getPos().getX(), (int)b.getPos().getY() + 75, HexColors.RED);
    }

    @Override
    public boolean isMouseInside(double mouseX, double mouseY) {
        return CollisionDetection.isPointInCircle(mouseX, mouseY, b);
    }

    @Override
    public String toString() {
        return "Neuron{" + id + "} bias: " + bias + "";
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private double result() {
        return output;
    }

    public double getInput() {
        return input;
    }

    public void setInput(double input) {
        this.input = input;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    public HashMap<Neuron, Float> getChildren() {
        return children;
    }

    public void setChildren(HashMap<Neuron, Float> children) {
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
