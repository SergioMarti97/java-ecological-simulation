package model.entities.live;

/**
 * Interface for the objects what are going
 * to have children
 */
public interface Reproducible {

    /**
     * Flag to make a baby or not
     * @return true if can make a baby false if not
     */
    boolean canMakeBaby();

    /**
     * This method must make a baby from the parent
     * @return a new individual
     */
    Reproducible makeBaby();

}
