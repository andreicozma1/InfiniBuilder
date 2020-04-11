package app.structures.grapher;

public class Variable {
    private boolean isAddition;
    private double constant;
    private boolean hasX;
    private double exponent;

    public Variable(boolean isAddition, double constant, boolean hasX, double exponent){
        this.isAddition = isAddition;
        this.constant = constant;
        this.hasX = hasX;
        this.exponent = exponent;
    }

    public boolean isAddition() { return isAddition; }
    public double getConstant() { return constant; }
    public boolean isHasX() { return hasX; }
    public double getExponent() { return exponent;}

    public void setAddition(boolean addition) { isAddition = addition; }
    public void setConstant(double constant) { this.constant = constant; }
    public void setHasX(boolean hasX) { this.hasX = hasX; }
    public void setExponent(double exponent) { this.exponent = exponent; }

    public double compute(double currentTotal, double x){
        double value;
        if(hasX) {
            value = x;
            value = Math.pow(value, exponent);
            value *= constant;
        }else{
            value = constant;
        }
        if(!isAddition) value=value*-1;
        return value + currentTotal;

    }
}
