package de.lab4inf.wrb;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class MyTests extends AbstractScriptTest {

	@Override
	protected Script getScript() {
		return new WRBScript();
	}


//	@Test
//    public final void testnewPlus() throws Exception {
//        String task = "2+3";
//        assertEquals(5.0, script.parse(task), eps);
//    }

}
