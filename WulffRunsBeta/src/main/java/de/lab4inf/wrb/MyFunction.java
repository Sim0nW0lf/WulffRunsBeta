package de.lab4inf.wrb;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;

public class MyFunction implements Function {
	
	protected String name;
	protected ParseTree root;
	protected int argc;
	protected ArrayList<String> argList = new ArrayList<String>();
	protected MyVisitor myVisitor;
	protected MyVisitor parent;

	/**
	 * @param root ParseTree node of the function declaration. 
	 * @param parent the MyVisitor that generated this Function. Used for copying functions on calling eval.
	 */
	public MyFunction(ParseTree root, MyVisitor parent) {
		this.root = root;
		this.name = root.getChild(0).getText();
		for(String s : root.getChild(2).getText().split(",")) {
			this.argList.add(s);
		}
		this.argc = this.argList.size();
		this.myVisitor = new MyVisitor();
		this.parent = parent;
	}
	
	@Override
	public double eval(double... args) {
		//Check if this can even work at all
		if(args.length != this.argc) {
			return 0;
		}
//		System.out.println("Initiating Function: " + this.root.getChild(5).getText() + "\n");
		//Get current Variables thanks to the power of Pointers
		this.myVisitor.setVarMap(this.parent.getVarMap());
		this.myVisitor.setFuncMap(this.parent.getFuncMap());
		//add all the given values to current variable set
		int i = 0;
		for(double arg : args) {
			this.myVisitor.setVariable(this.argList.get(i), arg);
//			System.out.println("Adding Variable: " + this.argList.get(i) + "\n");
			i++;
		}
		//Math the shit out of it all
		double sol = myVisitor.rechnen(this.root.getChild(5));
//		System.out.println("Got: " + sol + "\n");
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
