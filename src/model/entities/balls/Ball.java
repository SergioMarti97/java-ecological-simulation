package model.entities.balls;

import model.CustomColor;
import model.DrawUtils;
import model.Drawable;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

/**
 * This class represents a static ball
 */
public class Ball implements Drawable {

    protected int id = 0;

    protected int r;

    private int border = 1;

    protected Vec2df pos;

    protected CustomColor col = new CustomColor(0);

    public Ball(Vec2df pos, int r) {
        this.pos = new Vec2df(pos);
        this.r = r;
    }

    public Ball(Vec2df pos, int r, int col) {
        this.pos = new Vec2df(pos);
        this.r = r;
        this.col = new CustomColor(col);
    }

    public Ball(Vec2df pos, int r, CustomColor col) {
        this.pos = new Vec2df(pos);
        this.r = r;
        this.col = new CustomColor(col.getHex());
    }

    @Override
    public void drawYourSelf(Renderer r) {
        DrawUtils.drawBallWithBorder(r, this);
    }

    public float getMass() {
        return 1.0f * getR();
    }

    public Vec2df getPos() {
        return pos;
    }

    public void setPos(Vec2df pos) {
        this.pos = pos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRadius() {
        return r;
    }

    public void setRadius(int r) {
        this.r = r;
    }

    public int getR() {
        return this.r + border;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public CustomColor getCol() {
        return col;
    }

    public void setCol(CustomColor col) {
        this.col = col;
    }

    @Override
    public String toString() {
        return "Ball{" + id + '}';
    }
}
