package model.shapes.alineaxisrectangle;

import olcPGEApproach.vectors.points2d.Vec2di;

public class AlineAxisRectangleInteger extends AlineAxisRectangle<Vec2di> {

    public AlineAxisRectangleInteger(Vec2di pos, Vec2di size, int color) {
        super(pos, size, color);
    }

    public int getPosX() {
        return getPos().getX();
    }

    public void setPosX(int x) {
        getPos().setX(x);
    }

    public int getPosY() {
        return getPos().getY();
    }

    public void setPosY(int y) {
        getPos().setY(y);
    }

    public int getWidth() {
        return getSize().getX();
    }

    public void setWidth(int width) {
        getSize().setX(width);
    }

    public int getHeight() {
        return getSize().getY();
    }

    public void setHeight(int height) {
        getSize().setY(height);
    }

}
