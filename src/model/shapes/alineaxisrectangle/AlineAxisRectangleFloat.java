package model.shapes.alineaxisrectangle;

import olcPGEApproach.vectors.points2d.Vec2df;

public class AlineAxisRectangleFloat extends AlineAxisRectangle<Vec2df> {

    public AlineAxisRectangleFloat(Vec2df pos, Vec2df size, int color) {
        super(pos, size, color);
    }

    public float getPosX() {
        return getPos().getX();
    }

    public void setPosX(float x) {
        getPos().setX(x);
    }

    public float getPosY() {
        return getPos().getY();
    }

    public void setPosY(float y) {
        getPos().setY(y);
    }

    public float getWidth() {
        return getSize().getX();
    }

    public void setWidth(float width) {
        getSize().setX(width);
    }

    public float getHeight() {
        return getSize().getY();
    }

    public void setHeight(float height) {
        getSize().setY(height);
    }

}
