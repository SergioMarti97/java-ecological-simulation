package model.shapes;

import model.Clickable2D;
import model.Drawable;
import model.physics.CollisionDetection;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

/**
 * This class represent a rectangle
 */
public class AlineAxisRectangle implements Drawable, Clickable2D {

    private Vec2df pos;

    private Vec2df size;

    private int color;

    private boolean isFill = false;

    public AlineAxisRectangle(Vec2df pos, Vec2df size, int color) {
        this.pos = new Vec2df(pos);
        this.size = new Vec2df(size);
        this.color = color;
    }

    @Override
    public boolean isMouseInside(double mouseX, double mouseY) {
        return CollisionDetection.isPointInRect(mouseX, mouseY, pos.getX(), pos.getY(), getWidth(), getHeight());
    }

    @Override
    public void drawYourSelf(Renderer r) {
        r.drawFillRectangle((int)pos.getX(), (int)pos.getY(), (int)size.getX(), (int)size.getY(), color);
    }

    // Getter and Setter

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isFill() {
        return isFill;
    }

    public void setFill(boolean fill) {
        isFill = fill;
    }

    public Vec2df getPos() {
        return pos;
    }

    public float getPosX() {
        return pos.getX();
    }

    public float getPosY() {
        return pos.getY();
    }

    public void setPos(Vec2df pos) {
        this.pos = pos;
    }

    public void setX(float x) {
        getPos().setX(x);
    }

    public void setY(float y) {
        getPos().setY(y);
    }

    public Vec2df getSize() {
        return size;
    }

    public float getWidth() {
        return size.getX();
    }

    public float getHeight() {
        return size.getY();
    }

    public void setSize(Vec2df size) {
        this.size = size;
    }

    public void setWidth(float width) {
        getSize().setX(width);
    }

    public void setHeight(float height) {
        getSize().setY(height);
    }

}
