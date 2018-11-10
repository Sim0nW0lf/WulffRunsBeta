package de.lab4inf.wrb;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class MyTests extends AbstractScriptTest {

	@Override
	protected Script getScript() {
		return new WRBScript();
	}


	@Test
    public final void testMod() throws Exception {
        String task = "12%5";
        assertEquals(2.0, script.parse(task), EPS);
    }
	
	@Test
    public final void testTanh() throws Exception {
        String task = "tanh(3)";
        assertEquals(Math.tanh(3), script.parse(task), EPS);
    }
	
	@Test(expected = IllegalArgumentException.class)
    public final void testVar() throws Exception {
        String task = "a + 2";
        assertEquals(2.0, script.parse(task), EPS);
    }

}
