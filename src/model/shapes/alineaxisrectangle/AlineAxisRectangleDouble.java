package model.shapes.alineaxisrectangle;

import olcPGEApproach.vectors.points2d.Vec2dd;

public class AlineAxisRectangleDouble extends AlineAxisRectangle<Vec2dd> {

    public AlineAxisRectangleDouble(Vec2dd pos, Vec2dd size, int color) {
        super(pos, size, color);
    }

    public double getPosX() {
        return getPos().getX();
    }

    public void setPosX(double x) {
        getPos().setX(x);
    }

    public double getPosY() {
        return getPos().getY();
    }

    public void setPosY(double y) {
        getPos().setY(y);
    }

    public double getWidth() {
        return getSize().getX();
    }

    public void setWidth(double width) {
        getSize().setX(width);
    }

    public double getHeight() {
        return getSize().getY();
    }

    public void setHeight(double height) {
        getSize().setY(height);
    }

}
