package app.structures.spawnables.grapher;

import java.util.ArrayList;
import java.util.List;

/**
 * Function holds a collection of variables and can compute the value of the function given an x value.
 */
public class Function {
    // global variables
    private final List<Variable> variables;

    //Constructor initializes the variables list
    public Function() {
        variables = new ArrayList<>();
    }

    // constructor overload. takes a list of varaiables
    public Function(List<Variable> variables) {
        this.variables = variables;
    }

    // adds a variable to the function
    public void addVariable(Variable var) {
        variables.add(var);
    }

    // returns the list of variables
    public List<Variable> getEquation() {
        return variables;
    }

    // runs through the list of variables and computes the functions value from the given x
    public double compute(double x) {
        double value = 0;
        for (Variable v : variables) {
            value = v.compute(value, x);
        }
        return value;
    }
}