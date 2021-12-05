package model;

/**
 * This interface is for the objects what
 * behave with mouse clicks
 */
public interface Clickable2D {

    /**
     * If the mouse is inside the object
     * @param mouseX mouse x position
     * @param mouseY mouse y position
     * @return true if the mouse is inside false if not
     */
    boolean isMouseInside(double mouseX, double mouseY);

}
