package model.neuronal;

import model.Clickable2D;
import model.DrawUtils;
import model.Drawable;
import model.physics.CollisionDetection;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;

/**
 * This class represent a connection between
 * two neurons
 */
public class Connection implements Clickable2D, Drawable {

    // Constants
    final int MAX_THICKNESS = 12;

    final int MIN_THICKNESS = 1;

    final int DIFF_THICKNESS = MAX_THICKNESS - MIN_THICKNESS;

    // Fields of the connection

    private Neuron ori;

    private Neuron tar;

    private double weight;

    // Fields two draw the object
    private int thickness;

    private int color;

    private final int[] xPoints;

    private final int[] yPoints;

    /**
     * Constructor
     * @param ori origin neuron
     * @param tar target neuron
     * @param weight weight of the connection
     */
    public Connection(Neuron ori, Neuron tar, double weight) {
        this.ori = ori;
        this.tar = tar;
        this.weight = weight;

        xPoints = new int[4];
        yPoints = new int[4];

        calThickness();
        calColor();
        calPointsPos();
    }

    private void calThickness(double weight) {
        if (weight < 0) {
            weight = -weight;
        }
        thickness = (int)(weight * DIFF_THICKNESS + MIN_THICKNESS);
    }

    private void calColor(double weight) {
        if (weight > 0) {
            color = DrawUtils.interpolateColor(HexColors.GREEN, HexColors.WHITE, weight);
        } else {
            color = DrawUtils.interpolateColor(HexColors.RED, HexColors.WHITE, -weight);
        }
    }

    private void calPointsPos(int x1, int y1, int x2, int y2, int thickness) {
        // The thick line is in fact a filled polygon
        int dX = x2 - x1;
        int dY = y2 - y1;
        // line length
        double lineLength = Math.sqrt(dX * dX + dY * dY);

        double scale = (double)(thickness) / (2 * lineLength);

        // The x,y increments from an endpoint needed to create a rectangle...
        double ddx = -scale * (double)dY;
        double ddy = scale * (double)dX;
        ddx += (ddx > 0) ? 0.5 : -0.5;
        ddy += (ddy > 0) ? 0.5 : -0.5;
        int dx = (int)ddx;
        int dy = (int)ddy;

        // Now we can compute the corner points...
        xPoints[0] = x1 + dx; yPoints[0] = y1 + dy;
        xPoints[1] = x1 - dx; yPoints[1] = y1 - dy;
        xPoints[2] = x2 - dx; yPoints[2] = y2 - dy;
        xPoints[3] = x2 + dx; yPoints[3] = y2 + dy;
    }

    public void calThickness() {
        calThickness(thickness);
    }

    public void calColor() {
        calColor(weight);
    }

    public void calPointsPos() {
        calPointsPos(
                (int)this.ori.getB().getPos().getX(),
                (int)this.ori.getB().getPos().getY(),
                (int)this.tar.getB().getPos().getX(),
                (int)this.tar.getB().getPos().getY(),
                thickness);
    }

    @Override
    public boolean isMouseInside(double mouseX, double mouseY) {
        return CollisionDetection.isPointInTriangle(mouseX, mouseY,
                xPoints[0], yPoints[0],
                xPoints[1], yPoints[1],
                xPoints[2], yPoints[2]) ||
                CollisionDetection.isPointInTriangle(mouseX, mouseY,
                        xPoints[0], yPoints[0],
                        xPoints[2], yPoints[2],
                        xPoints[3], yPoints[3]);
    }

    public void drawThickLine(Renderer r, int c) {
        r.drawFillTriangle(xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2], c);
        r.drawFillTriangle(xPoints[0], yPoints[0], xPoints[2], yPoints[2], xPoints[3], yPoints[3], c);
    }

    @Override
    public void drawYourSelf(Renderer r) {
        if (thickness == MIN_THICKNESS) {
            r.drawLine(
                    (int)ori.getB().getPos().getX(),
                    (int)ori.getB().getPos().getY(),
                    (int)tar.getB().getPos().getX(),
                    (int)tar.getB().getPos().getY(),
                    color);
        } else {
            drawThickLine(r, color);
        }

        String out = String.format("%.3f", weight);
        int offText = (out.length() / 2) * 10;
        r.drawText(out,
                (int)((tar.getB().getPos().getX() - ori.getB().getPos().getX()) / 2 + ori.getB().getPos().getX() - offText),
                (int)((tar.getB().getPos().getX() - ori.getB().getPos().getY()) / 2 + ori.getB().getPos().getY()),
                HexColors.BLUE);
    }

    // Getters and Setters

    public Neuron getOri() {
        return ori;
    }

    public void setOri(Neuron ori) {
        this.ori = ori;
        calPointsPos();
    }

    public Neuron getTar() {
        return tar;
    }

    public void setTar(Neuron tar) {
        this.tar = tar;
        calPointsPos();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        calThickness();
        calColor();
    }

}
