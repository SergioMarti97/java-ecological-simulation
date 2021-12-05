package model.entities;

/**
 * Interface for every object what has to
 * update for each frame
 */
public interface Updatable {

    /**
     * Method update
     * @param dt the elapsed time between frames
     */
    void update(float dt);

}
