package model.statistics.regression;

public class RegressionObj {

    private double slope;

    private double origin;

    private double r2;

    public RegressionObj(double slope, double origin, double r2) {
        this.slope = slope;
        this.origin = origin;
        this.r2 = r2;
    }

    // Getters and Setters

    public double getSlope() {
        return slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public double getOrigin() {
        return origin;
    }

    public void setOrigin(double origin) {
        this.origin = origin;
    }

    public double getR2() {
        return r2;
    }

    public void setR2(double r2) {
        this.r2 = r2;
    }
}
