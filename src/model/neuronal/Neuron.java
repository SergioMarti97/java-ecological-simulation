package model.neuronal;

import model.CustomColor;
import model.entities.balls.Ball;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

import java.util.ArrayList;

public class Neuron extends Ball {

    private float value = 0;

    private float module = 1;

    private ArrayList<Neuron> children = new ArrayList<>();

    public Neuron(Vec2df pos, int r) {
        super(pos, r);
    }

    public Neuron(Vec2df pos, int r, int col) {
        super(pos, r, col);
    }

    public Neuron(Vec2df pos, int r, CustomColor col) {
        super(pos, r, col);
    }

    private float result() {
        return value * module;
    }

    public void addToValue(float quantity) {
        value += quantity;
    }

    public void giveValueToChildren() {
        for (Neuron n : children) {
            n.addToValue(this.result());
        }
    }

    @Override
    public void drawYourSelf(Renderer r) {
        for (Neuron n : children) {
            r.drawLine(
                    (int)this.pos.getX(),
                    (int)this.pos.getY(),
                    (int)n.getPos().getX(),
                    (int)n.getPos().getY(),
                    HexColors.BLACK
            );
        }
        super.drawYourSelf(r);
        r.drawText("" + value, (int)this.pos.getX(), (int)this.pos.getY(), HexColors.BLACK);
        r.drawText("" + module, (int)this.pos.getX(), (int)this.pos.getY() + 25, HexColors.RED);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getModule() {
        return module;
    }

    public void setModule(float module) {
        this.module = module;
    }

    public ArrayList<Neuron> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Neuron> children) {
        this.children = children;
    }
}
