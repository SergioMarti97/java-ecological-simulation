package model.population;

import model.entities.live.*;

import java.util.ArrayList;

public abstract class Population<S extends Living>{

    protected ArrayList<S> individuals;

    protected ArrayList<S> deaths;

    protected ArrayList<S> babies;

    public Population() {
        individuals = new ArrayList<>();
        deaths = new ArrayList<>();
        babies = new ArrayList<>();
    }

    public abstract S makeBaby(S mom);

    public abstract S death(S old);

    public void whatToDoWithBabies() {
        individuals.addAll(babies);
    }

    public void whatToDoWithDeaths() {
        individuals.removeAll(deaths);
    }

    public void update(float dt) {
        for (S i : individuals) {
            i.update(dt);
            if (i.hasToDie()) {
                deaths.add(death(i));
            }
            if (i.canMakeBaby()) {
                babies.add(makeBaby(i));
            }
        }
    }

    public void updateDeathsAndBabies() {
        whatToDoWithBabies();
        whatToDoWithDeaths();
        deaths.clear();
        babies.clear();
    }

    public ArrayList<S> getIndividuals() {
        return individuals;
    }

    public ArrayList<S> getBabies() {
        return babies;
    }

    public ArrayList<S> getDeaths() {
        return deaths;
    }

}
