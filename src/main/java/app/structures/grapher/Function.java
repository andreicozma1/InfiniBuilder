package app.structures.grapher;

import java.util.ArrayList;
import java.util.List;

public class Function {
    private List<Variable> variables;
    public Function() {
        variables = new ArrayList<>();
    }
    public Function(List<Variable> variables){
        this.variables = variables;
    }
    public void addVariable(Variable var) { variables.add(var); }
    public List<Variable> getEquation(){ return  variables; }

    public double compute(double x){
        double value = 0;
        for(Variable v : variables){
            value = v.compute(value,x);
        }
        return value;
    }
}