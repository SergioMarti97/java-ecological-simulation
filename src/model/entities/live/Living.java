package model.entities.live;

import model.entities.Updatable;

/**
 * This interface is for all objects what are living beings
 * To be a living being:
 * - Must be update for each frame
 * - Must have energetic values
 * - Can reproduce
 */
public interface Living extends Updatable, Energetic, Reproducible {

}
