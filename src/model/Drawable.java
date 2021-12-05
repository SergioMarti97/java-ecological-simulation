package model;

import olcPGEApproach.gfx.Renderer;

/**
 * Interface for all objects what
 * are going to be drawn on screen
 */
public interface Drawable {

    /**
     * The method to draw on screen the object
     * @param r the renderer
     */
    void drawYourSelf(Renderer r);

}
