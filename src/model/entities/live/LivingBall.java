package model.entities.live;

import model.CustomColor;
import model.DrawUtils;
import model.Clickable2D;
import model.entities.balls.Ball;
import model.physics.CollisionDetection;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

import java.util.Random;

public class LivingBall extends Ball implements Living, Clickable2D {

    private float maxEnergy;

    private float energy;

    private float timeLiving = 0;

    public LivingBall(Vec2df pos, int r, float maxEnergy) {
        super(pos, r);
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }

    public LivingBall(Vec2df pos, int r, int col, float maxEnergy) {
        super(pos, r, col);
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }

    public LivingBall(Vec2df pos, int r, CustomColor col, float maxEnergy) {
        super(pos, r, col);
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
    }

    public LivingBall(LivingBall lb) {
        super(lb.getPos(), lb.getRadius(), lb.getCol());
        this.maxEnergy = lb.getMaxEnergy();
        this.energy = lb.getEnergy();
    }

    @Override
    public float calEnergyDecrease(float dt) {
        return 100.0f * dt;
    }

    @Override
    public boolean hasToDie() {
        return timeLiving >= 10;
    }

    @Override
    public void update(float dt) {
        timeLiving += dt;
        energy += calEnergyDecrease(dt);

        // Custom for the food
        int col = (int)((energy / maxEnergy) * 255);
        this.col.setR(col);
        this.col.setG(col);
    }

    @Override
    public boolean canMakeBaby() {
        return energy >= maxEnergy;
    }

    @Override
    public Reproducible makeBaby() {
        energy /= 2;
        LivingBall lb = new LivingBall(this);
        Random rnd = new Random();
        int offSetX = rnd.nextInt(20) - 10;
        int offSetY = rnd.nextInt(20) - 10;
        lb.getPos().addToX(getR() + offSetX);
        lb.getPos().addToY(getR() + offSetY);
        return lb;
    }

    public void drawYourSelf(Renderer r, boolean drawEnergyBar) {
        super.drawYourSelf(r);
        if (drawEnergyBar) {
            DrawUtils.drawEnergyBar(
                    r,
                    pos.getX(),
                    pos.getY(),
                    15,
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

    @Override
    public boolean isMouseInside(double mouseX, double mouseY) {
        return CollisionDetection.isPointInCircle(mouseX, mouseY, this);
    }
}
