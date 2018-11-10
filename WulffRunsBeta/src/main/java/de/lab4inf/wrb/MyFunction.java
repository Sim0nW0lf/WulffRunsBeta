package de.lab4inf.wrb;

import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class MyFunction implements Function {

	protected String name;
	protected DemoParser.FunctionDefinitionContext root;
	protected int argc;
	protected ArrayList<String> argList = new ArrayList<String>();
	protected MyVisitor parent;
	protected HashMap<String, Variable> varListTemp = new HashMap<String, Variable>();

	/**
	 * @param root   ParseTree node of the function declaration.
	 * @param parent the MyVisitor that generated this Function. Used for copying
	 *               functions on calling eval.
	 */
	public MyFunction(DemoParser.FunctionDefinitionContext root, MyVisitor parent) {
		this.root = root;
		this.name = root.getChild(0).getText();
		for (String s : root.getChild(2).getText().split(",")) {
			this.argList.add(s);
		}
		this.argc = this.argList.size();
		this.parent = parent;
	}

	@Override
	public double eval(double... args) {
		// Check if this can even work at all
		if (args.length != this.argc) {
			return 0;
		}
		//System.out.println("Initiating Function: " + this.root.getChild(5).getText() + "\n");

		// add all the given values to current variable set after savepointing them
		int i = 0;
		for (double arg : args) {
			try {
				this.parent.getVariable(this.argList.get(i)).setSave();
			} catch (IllegalArgumentException e) {

			}
			this.parent.setVariable(this.argList.get(i), arg);
			// System.out.println("Adding Variable: " + this.argList.get(i) + "\n");
			i++;
		}

		// Math the shit out of it all
		double sol = this.parent.visitChildren(root.expression());
		// System.out.println("Got: " + sol + "\n");

		// Restore used Variables to previous value
		i = 0;
		for (double arg : args) {
			this.parent.getVariable(this.argList.get(i)).loadSave();
			i++;
		}
		return sol;
	}

	/**
	 * @return the Name of this Function
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the root Node for this Function
	 */
	public ParseTree getRoot() {
		return root;
	}

	/**
	 * @return number of Arguments for this Function
	 */
	public int getArgc() {
		return argc;
	}

	/**
	 * @return the Parent Visitor that created this Fuction
	 */
	public MyVisitor getParent() {
		return parent;
	}

	/**
	 * @param parent adopt this Function to a different Visitor.
	 */
	public void setParent(MyVisitor parent) {
		this.parent = parent;
	}
}
