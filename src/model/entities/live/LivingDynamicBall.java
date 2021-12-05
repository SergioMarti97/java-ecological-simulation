package model.entities.live;

import model.CustomColor;
import model.DrawUtils;
import model.Clickable2D;
import model.entities.balls.DynamicBall;
import model.physics.CollisionDetection;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

/**
 * A living being has to store two things:
 * the energy what it has
 * the time what it exits
 */
public class LivingDynamicBall extends DynamicBall implements Living, Clickable2D {

    protected float maxEnergy;

    protected float energy;

    protected float timeLiving = 0.0f;

    public LivingDynamicBall(Vec2df pos, Vec2df vel, int r, float maxEnergy, int col) {
        super(pos, vel, r, col);
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }

    public LivingDynamicBall(Vec2df pos, Vec2df vel, int r, float maxEnergy, CustomColor col) {
        super(pos, vel, r, col);
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }

    public LivingDynamicBall(Vec2df pos, Vec2df vel, int r, float energy, float maxEnergy, int col) {
        super(pos, vel, r, col);
        this.maxEnergy = maxEnergy;
        this.energy = energy;
    }

    public LivingDynamicBall(Vec2df pos, Vec2df vel, int r, float energy, float maxEnergy, CustomColor col) {
        super(pos, vel, r, col);
        this.maxEnergy = maxEnergy;
        this.energy = energy;
    }

    public LivingDynamicBall(LivingDynamicBall ldb) {
        super(ldb.getPos(), ldb.getVel(), ldb.getRadius(), ldb.getCol());
        this.maxEnergy = ldb.getMaxEnergy();
        this.energy = ldb.getEnergy();
    }

    @Override
    public boolean isMouseInside(double mouseX, double mouseY) {
        return CollisionDetection.isPointInCircle(mouseX, mouseY, this);
    }

    @Override
    public float calEnergyDecrease(float dt) {
        return 200.0f * dt;
    }

    @Override
    public boolean hasToDie() {
        return energy <= 0;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        timeLiving += dt;
        energy -= calEnergyDecrease(dt);
    }

    @Override
    public boolean canMakeBaby() {
        return energy >= maxEnergy;
    }

    @Override
    public Reproducible makeBaby() {
        energy /= 2.0f;
        LivingDynamicBall ldb = new LivingDynamicBall(this);
        ldb.setCol(ldb.getCol().modify(20));
        ldb.getPos().addToX(getR() * 2);
        return ldb;
    }

    public void drawYourSelf(Renderer r, boolean drawArrows, boolean drawEnergyBar) {
        super.drawYourSelf(r, drawArrows);
        if (drawEnergyBar) {
            DrawUtils.drawEnergyBar(
                    r,
                    pos.getX(),
                    pos.getY(),
                    20,
                    5,
                    energy,
                    maxEnergy,
                    energyColor.getHex(),
                    HexColors.WHITE,
                    0,
                    this.r + 5,
                    true
                    );
        }
    }

    @Override
    public float getEnergy() {
        return energy;
    }

    @Override
    public void setEnergy(float energy) {
        this.energy = energy;
    }

    @Override
    public float getMaxEnergy() {
        return maxEnergy;
    }

    @Override
    public void setMaxEnergy(float maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    @Override
    public float getTimeLiving() {
        return timeLiving;
    }

    @Override
    public void setTimeLiving(float timeLiving) {
        this.timeLiving = timeLiving;
    }

}
