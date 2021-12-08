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
    private HashMap<Neuron, Double> children = new HashMap<>();

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
        children.put(n, weight);
    }

    public void giveValueToChildren() {
        for (Map.Entry<Neuron, Double> e : children.entrySet()) {
            e.getKey().addToInput(this.result() * e.getValue());
        }
    }

    @Override
    public void update(float dt) {
        calOutput();
        giveValueToChildren();
    }

    public void drawConnection(Renderer r, int x1, int y1, int x2, int y2, double weight) {
        final int maxThickness = 12;
        final int minThickness = 1;
        int color;
        int thickness;
        if (weight > 0) {
            color = DrawUtils.interpolateColor(HexColors.GREEN, HexColors.WHITE, weight);
            thickness = (int)(weight * (maxThickness - minThickness) + minThickness);
        } else {
            color = DrawUtils.interpolateColor(HexColors.RED, HexColors.WHITE, -weight);
            thickness = (int)(-weight * (maxThickness - minThickness) + minThickness);
        }

        if (thickness == minThickness) {
            r.drawLine(x1, y1, x2, y2, color);
        } else {
            DrawUtils.drawThickLine(r, x1, y1, x2, y2, thickness, color);
        }

        String out = String.format("%.3f", weight);
        int offText = (out.length() / 2) * 10;
        r.drawText(out,
                (int)((x2 - x1) / 2 + b.getPos().getX() - offText),
                (int)((y2 - y1) / 2 + b.getPos().getY()),
                HexColors.BLUE);
    }

    @Override
    public void drawYourSelf(Renderer r) {
        for (Map.Entry<Neuron, Double> e : children.entrySet()) {
            int sx = (int)b.getPos().getX();
            int sy = (int)b.getPos().getY();
            int ex = (int)e.getKey().getB().getPos().getX();
            int ey = (int)e.getKey().getB().getPos().getY();
            drawConnection(r, sx, sy, ex, ey, e.getValue());
        }
        b.drawYourSelf(r);
        r.drawText(b.toString(), (int)b.getPos().getX(), (int)b.getPos().getY(), HexColors.BLACK);
        r.drawText(functionName, (int)b.getPos().getX(), (int)b.getPos().getY() + 25, HexColors.BLACK);
        r.drawText(String.format("in: %.3f", input), (int)b.getPos().getX(), (int)b.getPos().getY() + 50, HexColors.BLACK);
        r.drawText(String.format("out: %.3f", output), (int)b.getPos().getX(), (int)b.getPos().getY() + 75, HexColors.GREEN);
        r.drawText(String.format("bias: %.3f", bias), (int)b.getPos().getX(), (int)b.getPos().getY() + 100, HexColors.RED);
    }

    @Override
    public boolean isMouseInside(double mouseX, double mouseY) {
        return CollisionDetection.isPointInCircle(mouseX, mouseY, b);
    }

    @Override
    public String toString() {
        StringBuilder con = new StringBuilder();
        for (Map.Entry<Neuron, Double> e : children.entrySet()) {
            con.append(e.getKey().getId()).append(':').append(e.getValue()).append(';');
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

    public double result() {
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

    public HashMap<Neuron, Double> getChildren() {
        return children;
    }

    public void setChildren(HashMap<Neuron, Double> children) {
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
