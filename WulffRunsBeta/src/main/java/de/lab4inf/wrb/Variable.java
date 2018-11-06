package de.lab4inf.wrb;

public class Variable {
	private String varName;
	private double varValue;

	/**
	 * @param varName Name of the Variable
	 * @param varValue Value of the Variable
	 */
	public Variable(String varName, double varValue) {
		this.varName = varName;
		this.varValue = varValue;
	}
	
	/**
	 * @return Name of the Variable
	 */
	public String getName() {
		return this.varName;
	}
	
	/**
	 * @return Value of the Variable
	 */
	public double getValue() {
		return this.varValue;
	}
	
	/**
	 * @param varValue new Value of the Variable
	 */
	public void setValue(double varValue) {
		this.varValue = varValue;
	}
}
