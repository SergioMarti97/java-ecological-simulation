package model.neuronal;

import model.Clickable2D;
import model.DrawUtils;
import model.Drawable;
import model.physics.CollisionDetection;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

/**
 * This class represent a connection between
 * two neurons
 */
public class Connection implements Clickable2D, Drawable {

    // Constants
    final int MAX_THICKNESS = 12;

    final int MIN_THICKNESS = 3;

    final int DIFF_THICKNESS = MAX_THICKNESS - MIN_THICKNESS;

    // Fields of the connection

    private int id = 0;

    private Vec2df ori;

    private Vec2df tar;

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
    public Connection(Vec2df ori, Vec2df tar, double weight) {
        this.ori = ori;
        this.tar = tar;
        this.weight = weight;

        xPoints = new int[4];
        yPoints = new int[4];

        cal();
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
        calThickness(weight);
    }

    public void calColor() {
        calColor(weight);
    }

    public void calPointsPos() {
        calPointsPos(
                (int)this.ori.getX(),
                (int)this.ori.getY(),
                (int)this.tar.getX(),
                (int)this.tar.getY(),
                thickness);
    }

    public void cal() {
        calThickness();
        calPointsPos();
        calColor();
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

    @Override
    public void drawYourSelf(Renderer r) {
        DrawUtils.drawFillSquare(r, xPoints, yPoints, color);

        String out = String.format("%.3f", weight);
        int offText = (out.length() / 2) * 10;
        int textX = (int)((tar.getX() - ori.getX()) / 2 + ori.getX());
        int textY = (int)((tar.getY() - ori.getY()) / 2 + ori.getY());
        r.drawText(out, textX  - offText, textY, HexColors.BLUE);
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vec2df getOri() {
        return ori;
    }

    public void setOri(Vec2df ori) {
        this.ori = ori;
        calPointsPos();
    }

    public Vec2df getTar() {
        return tar;
    }

    public void setTar(Vec2df tar) {
        this.tar = tar;
        calPointsPos();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
        cal();
    }

    public void addToWeight(double quantity) {
        this.weight += quantity;
        cal();
    }

    public int getColor() {
        return color;
    }

    public int getThickness() {
        return thickness;
    }

    public int[] getXp() {
        return xPoints;
    }

    public int[] getYp() {
        return yPoints;
    }

}
