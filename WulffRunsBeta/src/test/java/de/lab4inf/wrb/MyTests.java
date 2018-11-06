package de.lab4inf.wrb;

public class MyTests extends AbstractScriptTest {

	@Override
	protected Script getScript() {
		return new WRBScript();
	}

}
