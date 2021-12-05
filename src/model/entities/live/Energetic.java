package model.entities.live;

import model.CustomColor;
import olcPGEApproach.gfx.HexColors;

/**
 * This interface is for everything
 * what has the living behaviur
 *
 * Must have
 */
public interface Energetic {

    CustomColor energyColor = new CustomColor(HexColors.YELLOW);

    float calEnergyDecrease(float dt);

    boolean hasToDie();

    float getEnergy();

    void setEnergy(float energy);

    float getMaxEnergy();

    void setMaxEnergy(float maxEnergy);

    float getTimeLiving();

    void setTimeLiving(float timeLiving);

}
