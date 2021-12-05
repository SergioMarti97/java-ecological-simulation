package model.statistics.tracker;

import java.util.Deque;
import java.util.LinkedList;

/**
 * This class is an object what follows a determinate
 * variable of a system
 * @param <T> the type of the variable to track
 */
public class VariableTracker<T> {

    /**
     * The name of the variable tracker
     */
    protected String name = "";

    /**
     * The maximum number of values to store
     */
    protected int maxNumValues = 0;

    /**
     * Values
     */
    protected final Deque<T> values;

    /**
     * Constructor
     * @param maxNumValues the max number of variables to follow
     */
    public VariableTracker(int maxNumValues) {
        this.maxNumValues = maxNumValues;
        values = new LinkedList<>();
    }

    /**
     * Constructor
     * @param name the name of the variable tracker
     * @param maxNumValues the max number of variables to follow
     */
    public VariableTracker(String name, int maxNumValues) {
        this.name = name;
        this.maxNumValues = maxNumValues;
        values = new LinkedList<>();
    }

    public void update() {
        if (values.size() > maxNumValues) {
            values.removeLast();
        }
    }

    // Getters & Setters
    public Deque<T> getValues() {
        return values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxNumValues() {
        return maxNumValues;
    }

    public void setMaxNumValues(int maxNumValues) {
        this.maxNumValues = maxNumValues;
    }

}
