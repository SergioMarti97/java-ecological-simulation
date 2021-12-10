package model.shapes.alineaxisrectangle;

import model.Clickable2D;
import model.Drawable;
import model.physics.CollisionDetection;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2d;
import olcPGEApproach.vectors.points2d.Vec2dd;
import olcPGEApproach.vectors.points2d.Vec2df;
import olcPGEApproach.vectors.points2d.Vec2di;

/**
 * This class represent a rectangle
 */
public class AlineAxisRectangle<T extends Vec2d> implements Drawable, Clickable2D {

    protected T pos;

    protected T size;

    protected int color;

    protected boolean isFill = false;

    public AlineAxisRectangle(T pos, T size, int color) {
        this.pos = pos;
        this.size = size;
        this.color = color;
    }

    @Override
    public boolean isMouseInside(double mouseX, double mouseY) {
        if (pos instanceof Vec2di && size instanceof Vec2di) {
            return CollisionDetection.isPointInRect(mouseX, mouseY,
                    ((Vec2di)pos).getX(), ((Vec2di)pos).getY(),
                    ((Vec2di)size).getX(), ((Vec2di)size).getY());
        } else if (pos instanceof Vec2df && size instanceof Vec2df) {
            return CollisionDetection.isPointInRect(mouseX, mouseY,
                    ((Vec2df)pos).getX(), ((Vec2df)pos).getY(),
                    ((Vec2df)size).getX(), ((Vec2df)size).getY());
        } else if (pos instanceof Vec2dd && size instanceof Vec2dd) {
            return CollisionDetection.isPointInRect(mouseX, mouseY,
                    ((Vec2dd)pos).getX(), ((Vec2dd)pos).getY(),
                    ((Vec2dd)size).getX(), ((Vec2dd)size).getY());
        } else {
            return false;
        }
    }

    @Override
    public void drawYourSelf(Renderer r) {
        if (pos instanceof Vec2di && size instanceof Vec2di) {
            r.drawFillRectangle(
                    ((Vec2di)pos).getX(), ((Vec2di)pos).getY(),
                    ((Vec2di)size).getX(), ((Vec2di)size).getY(),
                    color);
        } else if (pos instanceof Vec2df && size instanceof Vec2df) {
            r.drawFillRectangle(
                    (int)((Vec2df)pos).getX(), (int)((Vec2df)pos).getY(),
                    (int)((Vec2df)size).getX(), (int)((Vec2df)size).getY(),
                    color);
        } else if (pos instanceof Vec2dd && size instanceof Vec2dd) {
            r.drawFillRectangle(
                    (int)((Vec2dd)pos).getX(), (int)((Vec2dd)pos).getY(),
                    (int)((Vec2dd)size).getX(), (int)((Vec2dd)size).getY(),
                    color);
        }
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

    public T getPos() {
        return pos;
    }

    public void setPos(T pos) {
        this.pos = pos;
    }

    public T getSize() {
        return size;
    }

    public void setSize(T size) {
        this.size = size;
    }

}
