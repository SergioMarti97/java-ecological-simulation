package model;

import model.entities.balls.Ball;
import olcPGEApproach.gfx.HexColors;
import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

/**
 * This class contains the code to draw some interface objects
 * (arrows, bars...)
 */
public class DrawUtils {

    /**
     * @see "https://stackoverflow.com/questions/17544157/generate-n-colors-between-two-colors"
     */
    public static int interpolateColor(int x, int y, double blending) {
        double inverseBlending = 1 - blending;

        int xR = x >> 16 & 255;
        int xG = x >> 8 & 255;
        int xB = x & 255;

        int yR = y >> 16 & 255;
        int yG = y >> 8 & 255;
        int yB = y & 255;

        double r = xR * blending + yR * inverseBlending;
        double g = xG * blending + yG * inverseBlending;
        double b = xB * blending + yB * inverseBlending;

        return 255 << 24 | (int)r << 16 | (int)g << 8 | (int)b;
    }

    public static void drawSquare(Renderer r, int[] xPoints, int[] yPoints, int c) {
        r.drawLine(xPoints[0], yPoints[0], xPoints[1], yPoints[1], c);
        r.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2], c);
        r.drawLine(xPoints[2], yPoints[2], xPoints[3], yPoints[3], c);
        r.drawLine(xPoints[3], yPoints[3], xPoints[0], yPoints[0], c);
    }

    public static void drawFillSquare(Renderer r, int[] xPoints, int[] yPoints, int c) {
        r.drawFillTriangle(xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2], c);
        r.drawFillTriangle(xPoints[0], yPoints[0], xPoints[2], yPoints[2], xPoints[3], yPoints[3], c);
    }

    /**
     * @see "https://www.rgagnon.com/javadetails/java-0260.html"
     */
    public static void drawFillThickLine(
            Renderer r, int x1, int y1, int x2, int y2, int thickness, int c) {
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
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        xPoints[0] = x1 + dx; yPoints[0] = y1 + dy;
        xPoints[1] = x1 - dx; yPoints[1] = y1 - dy;
        xPoints[2] = x2 - dx; yPoints[2] = y2 - dy;
        xPoints[3] = x2 + dx; yPoints[3] = y2 + dy;

        drawFillSquare(r, xPoints, yPoints, c);
    }

    public static void drawThickLine(
            Renderer r, int x1, int y1, int x2, int y2, int thickness, int c) {
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
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        xPoints[0] = x1 + dx; yPoints[0] = y1 + dy;
        xPoints[1] = x1 - dx; yPoints[1] = y1 - dy;
        xPoints[2] = x2 - dx; yPoints[2] = y2 - dy;
        xPoints[3] = x2 + dx; yPoints[3] = y2 + dy;

        drawSquare(r, xPoints, yPoints, c);
    }

    public static void drawBall(Renderer r, Ball b) {
        r.drawFillCircle((int)b.getPos().getX(), (int)b.getPos().getY(), b.getRadius(), b.getCol().getHex());
    }

    public static void drawBallWithBorder(Renderer r, Ball b, int borderColor) {
        r.drawFillCircle((int)b.getPos().getX(), (int)b.getPos().getY(), b.getR(), borderColor);
        r.drawFillCircle((int)b.getPos().getX(), (int)b.getPos().getY(), b.getRadius(), b.getCol().getHex());
    }

    public static void drawBallWithBorder(Renderer r, Ball b) {
        drawBallWithBorder(r, b, HexColors.BLACK);
    }

    public static void drawArrow(Renderer r, Vec2df ori, Vec2df vec, int color) {
        r.drawFillCircle(
                (int)(ori.getX() + vec.getX()),
                (int)(ori.getY() + vec.getY()),
                5,
                color
        );
        r.drawLine(
                (int)ori.getX(),
                (int)ori.getY(),
                (int)(ori.getX() + vec.getX()),
                (int)(ori.getY() + vec.getY()),
                color
        );
    }

    public static void drawEnergyBar(
            Renderer r,
            float posX,
            float posY,
            float width,
            float height,
            float energy,
            float maxEnergy,
            int barColor,
            int borderColor,
            int offX,
            int offY,
            boolean drawAlineX
    ) {
        if (!drawAlineX) {
            r.drawFillRectangle(
                    (int)(posX) + offX,
                    (int)(posY) + offY,
                    (int) (energy / maxEnergy * width),
                    (int) height,
                    barColor
            );
            r.drawRectangle(
                    (int)(posX) + offX,
                    (int)(posY) + offY,
                    (int) width,
                    (int) height,
                    borderColor
            );
        } else {
            float halfWidth = width / 2.0f;
            r.drawFillRectangle(
                    (int)(posX - halfWidth) + offX,
                    (int)(posY) + offY,
                    (int) (energy / maxEnergy * width),
                    (int) height,
                    barColor
            );
            r.drawRectangle(
                    (int)(posX - halfWidth) + offX,
                    (int)(posY) + offY,
                    (int) width,
                    (int) height,
                    borderColor
            );
        }
    }
    
}
