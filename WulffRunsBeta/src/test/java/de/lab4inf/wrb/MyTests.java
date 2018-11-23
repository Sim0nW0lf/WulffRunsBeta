package de.lab4inf.wrb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

public class MyTests extends AbstractScriptTest {
	
	static public void matrixCompare(Double[][] matrixExpected, Double[][] MatrixActual) {
        if (Arrays.deepEquals(matrixExpected, MatrixActual) == true) {
            return;
        }else {
        	throw new IllegalArgumentException();
        }
    }

	@Override
	protected Script getScript() {
		return new WRBScript();
	}

	@Test
	public final void testTanh() throws Exception {
		double x = AbstractScriptTest.rnd();
		script.setVariable("x", x);
		String task = "tanh(x)";
		assertEquals(Math.tanh(x), script.parse(task), EPS);
	}

	@Test
	public final void testMod() throws Exception {
		String task = "12%5";
		assertEquals(2.0, script.parse(task), EPS);
	}

	@Test(expected = IllegalArgumentException.class)
	public final void testVar() throws Exception {
		String task = "a + 2";
		assertEquals(2.0, script.parse(task), EPS);
	}

	public double factorial(double x) {
		if (x == 1)
			return 1;
		return x * factorial(x - 1);
	}

	@Test
	public void testOwnTimingCachedFunctions() throws Exception {
		final String fmt = "\n\n" + "average Parser Timing Test    \n" + "=====================    \n"
				+ "average cached :%6d \u03BCs/call  \n" + "average parsed :%6d \u03BCs/call  \n" + "average speedup: %.2f => %s  \n\n";
		final int MAX_PARSE_TIME = 1000;
		final int MAX_LOOPS = 5000, AVERAGE_LOOP = 20;
		final double SCALED = -1000 * MAX_LOOPS;
		double averageSpeedup = 0, x = 0.1;
		long timeParsed, timeCached, averageTimeCached = 0, averageTimeParsed = 0;
		String task;
		// sine function Taylor expansion will produce some performance burden.
		task = "a1=1;a3=-1/6;a5=1/120; a7=-1/5040; a0=0;a2=0;a4=0;a6=0;a8=0;"
				+ "s(x)=a0+a1*x+a2*x*x+a3*x^3+a4*x^4+a5*x^5+a6*x^6+a7*x^7+a8*x^8; err(x) = s(x) - sin(x);";
		script.parse(task);
		Function fct = script.getFunction("err");
		// first check that the parsing is correct
		for (x = -0.8; x <= 0.8; x += 0.001) {
			script.setVariable("x", x);
			assertEquals(format("error(%.2f) to large: %.3g", x, fct.eval(x)), 0.0, fct.eval(x), 100 * EPS);
		}
		// second check that the parsing is correct
		fct = script.getFunction("s");
		for (x = -0.8; x <= 0.8; x += 0.001) {
			script.setVariable("x", x);
			assertEquals(format("error(%.2f) to large: %.3g", x, fct.eval(x)), Math.sin(x), fct.eval(x), 100 * EPS);
		}
		for (int i = 0; i < AVERAGE_LOOP; i++) {
			// now calculate the timing; knowing the calculations are correct
			timeCached = System.nanoTime();
			x = rnd() / 2;
			for (int k = 0; k < MAX_LOOPS; k++) {
				fct.eval(x);
			}
			timeCached -= System.nanoTime();

			timeParsed = System.nanoTime();
			for (int k = 0; k < MAX_LOOPS; k++) {
				script.parse(task);
			}
			timeParsed -= System.nanoTime();

			timeParsed /= SCALED;
			timeCached /= SCALED;

			assertTrue(format("parsing too slow %d \u03BCs/call", timeParsed), timeParsed < MAX_PARSE_TIME);

			averageTimeCached += timeCached;
			averageTimeParsed += timeParsed;
		}
		averageTimeCached = averageTimeCached/AVERAGE_LOOP;
		averageTimeParsed = averageTimeParsed/AVERAGE_LOOP;
		averageSpeedup = (averageTimeParsed) / averageTimeCached;
		
		String rating = "failed ";
		if (averageSpeedup > 9) {
			rating = "passed excellent";
		} else if (averageSpeedup > 6) {
			rating = "passed ok";
		} else if (averageSpeedup > 3) {
			rating = "passed but poor";
		}
		System.err.printf(format(fmt, averageTimeCached, averageTimeParsed, averageSpeedup, rating));

		assertTrue("function syntax tree not cached", averageSpeedup > 3);
	}
	
	@Test
    public final void testSetGetMatrix() throws Exception {
        double y, x = rnd();
        String key = "XYZ";
        script.setVariable(key, x);
        y = script.getVariable(key);
        assertEquals(x, y, EPS);
    }
	
	@Test
	public final void testParseMatrix() throws Exception {
		String task = "matrixA = {2,3;4,5;};";
		assertEquals(2.0, script.parse(task), EPS);
	}
	
	@Test
	public final void testMatrixMultiplikation() throws Exception {
		String task = "matrixA = {1,2;3,4;}; matrixB = {4,3;2,1;}; matrixA*matrixB;";
		Double[][] matrixExpected;
		matrixExpected[0][0] = 8.0;
		matrixExpected[0][1] = 5.0;
		matrixExpected[1][0] = 20.0;
		matrixExpected[1][1] = 13.0;
		matrixCompare(matrixExpected, script.parse(task));
	}
	
}
