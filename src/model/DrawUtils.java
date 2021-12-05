package model;

import olcPGEApproach.gfx.Renderer;
import olcPGEApproach.vectors.points2d.Vec2df;

/**
 * This class contains the code to draw some interface objects
 * (arrows, bars...)
 */
public class DrawUtils {

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
