package model.statistics.tracker;

import javafx.util.Pair;
import model.statistics.regression.Regression;
import model.statistics.regression.RegressionObj;

import java.util.ArrayList;
import java.util.Deque;

/**
 * A extended version of the variable tracker, to
 * pairs of two types
 * @param <K> the key type
 * @param <V> the value type
 */
public class VariablePairTracker<K, V> extends VariableTracker<Pair<K, V>> {

    /**
     * Regression object
     */
    protected RegressionObj regression;

    /**
     * Constructor
     * @param maxNumValues the maximum number of variables to track
     */
    public VariablePairTracker(int maxNumValues) {
        super(maxNumValues);
        regression = new RegressionObj(0, 0, 0);
    }

    /**
     * Constructor
     * @param name name of the regression
     * @param maxNumValues the maximum number of values
     */
    public VariablePairTracker(String name, int maxNumValues) {
        super(name, maxNumValues);
        regression = new RegressionObj(0, 0, 0);
    }

    private void calRegression(Deque<Pair<K, V>> values) {
        ArrayList<Double> xValues = new ArrayList<>();
        ArrayList<Double> yValues = new ArrayList<>();
        for (Pair<K, V> p : values) {
            if (p.getKey() instanceof Double && p.getValue() instanceof Double) {
                xValues.add((Double) p.getKey());
                yValues.add((Double) p.getValue());
            }
        }
        regression.setSlope(Regression.slope(xValues, yValues));
        regression.setOrigin(Regression.origin(xValues, yValues));
        double r = Regression.calR(xValues, yValues);
        regression.setR2(r * r);
    }

    @Override
    public void update() {
        super.update();
        if (values.size() >= 2) {
            calRegression(values);
        }
    }

    // Getters & Setters

    public void pushPair(K key, V value) {
        values.push(new Pair<>(key, value));
    }

    public RegressionObj getRegression() {
        return regression;
    }

}
