package app.structures.spawnables.grapher;

/**
 * Variable contains one portion of a polynomial equation
 */
public class Variable {
    // global variables
    private boolean isAddition;
    private double constant;
    private boolean hasX;
    private double exponent;

    /**
     * Constructor initializes the class variables
     *
     * @param isAddition
     * @param constant
     * @param hasX
     * @param exponent
     */
    public Variable(boolean isAddition, double constant, boolean hasX, double exponent) {
        this.isAddition = isAddition;
        this.constant = constant;
        this.hasX = hasX;
        this.exponent = exponent;
    }

    // getters
    public boolean isAddition() {
        return isAddition;
    }

    // setters
    public void setAddition(boolean addition) {
        isAddition = addition;
    }

    public boolean isHasX() {
        return hasX;
    }

    public void setHasX(boolean hasX) {
        this.hasX = hasX;
    }

    public double getConstant() {
        return constant;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

    public double getExponent() {
        return exponent;
    }

    public void setExponent(double exponent) {
        this.exponent = exponent;
    }

    // computes the value of the variable and adds it to the current total and returns the final value
    public double compute(double currentTotal, double x) {
        double value;
        if (hasX) {
            value = x;
            value = Math.pow(value, exponent);
            value *= constant;
        } else {
            value = constant;
        }
        if (!isAddition) value = value * -1;
        return value + currentTotal;
    }
}
