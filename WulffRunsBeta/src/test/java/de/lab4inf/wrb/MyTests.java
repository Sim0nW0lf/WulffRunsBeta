package de.lab4inf.wrb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

import com.ibm.icu.text.DecimalFormat;

public class MyTests extends AbstractScriptTest {
	
	static public void matrixCompare(Double[][] matrixExpected, Double[][] matrixActual) {
        if (Arrays.deepEquals(matrixExpected, matrixActual) == true) {
            return;
        } else {
        	throw new IllegalArgumentException("\nExpected: " + MyMatrix.print(matrixExpected) + " but got: " + MyMatrix.print(matrixActual));
        }
    }
	
	public final Double myRnd(int range) {
		DecimalFormat numberFormat = new DecimalFormat("#.000");
		String s = numberFormat.format(range * (Math.random()+Math.random()-1)).replace(',', '.');
		return Double.parseDouble(s);
	}
	
	public String rndMatrixDefinition(Double[][] matrix, String name, int hight, int width) {
		return rndMatrixDefinition(matrix, name, hight, width, 10);
	}
	
	public String rndMatrixDefinition(Double[][] matrix, String name, int hight, int width, int range){
		String matrixDefinition = name + " = {";
		Double rnd = 0.0;
	    // write to matrix and create matrixDefinition String
	    for (int y = 0, x = 0; y < hight; y++) {
	    	rnd = myRnd(range);
	    	matrix[y][x] = rnd;
	    	matrixDefinition = matrixDefinition.concat(Double.toString(rnd));
	    	for (x = 1; x < width; x++) {
		    	rnd = myRnd(range);
		    	matrix[y][x] = rnd;
		    	matrixDefinition = matrixDefinition.concat(","+ rnd);
	    	}
	    	x = 0;
	    	matrixDefinition = matrixDefinition.concat("; ");
	    }
	    matrixDefinition = matrixDefinition.concat("};");
	    return matrixDefinition;
	}
	
	public Double[][] matrixMultiplication(Double[][] matrixA, Double[][] matrixB){
		int hightA = matrixA.length, widthB = matrixB[0].length, widthA_hightB = matrixB.length;
		Double[][] matrixSolution = new Double[hightA][widthB];
	    for (int i = 0; i < hightA; i++) {
			for (int j = 0; j < widthB; j++) {
				// initialize res
				matrixSolution[i][j] = 0.0;
				for (int k = 0; k < widthA_hightB; k++) {
					matrixSolution[i][j] += matrixA[i][k] * matrixB[k][j];
				}
			}
	    }
	    return matrixSolution;
	}

	@Override
	protected Script getScript() {
		return new WRBScript();
	}
	
//	@Test
//	public void testOwnTimingCachedFunctions() throws Exception {
//		final String fmt = "\n\n" + "average Parser Timing Test    \n" + "=====================    \n"
//				+ "average cached :%6d \u03BCs/call  \n" + "average parsed :%6d \u03BCs/call  \n" + "average speedup: %.2f => %s  \n\n";
//		final int MAX_PARSE_TIME = 1000;
//		final int MAX_LOOPS = 5000, AVERAGE_LOOP = 20;
//		final double SCALED = -1000 * MAX_LOOPS;
//		double averageSpeedup = 0, x = 0.1;
//		long timeParsed, timeCached, averageTimeCached = 0, averageTimeParsed = 0;
//		String task;
//		// sine function Taylor expansion will produce some performance burden.
//		task = "a1=1;a3=-1/6;a5=1/120; a7=-1/5040; a0=0;a2=0;a4=0;a6=0;a8=0;"
//				+ "s(x)=a0+a1*x+a2*x*x+a3*x^3+a4*x^4+a5*x^5+a6*x^6+a7*x^7+a8*x^8; err(x) = s(x) - sin(x);";
//		script.parse(task);
//		Function fct = script.getFunction("err");
//		// first check that the parsing is correct
//		for (x = -0.8; x <= 0.8; x += 0.001) {
//			script.setVariable("x", x);
//			assertEquals(format("error(%.2f) to large: %.3g", x, fct.eval(x)), 0.0, fct.eval(x), 100 * EPS);
//		}
//		// second check that the parsing is correct
//		fct = script.getFunction("s");
//		for (x = -0.8; x <= 0.8; x += 0.001) {
//			script.setVariable("x", x);
//			assertEquals(format("error(%.2f) to large: %.3g", x, fct.eval(x)), Math.sin(x), fct.eval(x), 100 * EPS);
//		}
//		for (int i = 0; i < AVERAGE_LOOP; i++) {
//			// now calculate the timing; knowing the calculations are correct
//			timeCached = System.nanoTime();
//			x = rnd() / 2;
//			for (int k = 0; k < MAX_LOOPS; k++) {
//				fct.eval(x);
//			}
//			timeCached -= System.nanoTime();
//
//			timeParsed = System.nanoTime();
//			for (int k = 0; k < MAX_LOOPS; k++) {
//				script.parse(task);
//			}
//			timeParsed -= System.nanoTime();
//
//			timeParsed /= SCALED;
//			timeCached /= SCALED;
//
//			assertTrue(format("parsing too slow %d \u03BCs/call", timeParsed), timeParsed < MAX_PARSE_TIME);
//
//			averageTimeCached += timeCached;
//			averageTimeParsed += timeParsed;
//		}
//		averageTimeCached = averageTimeCached/AVERAGE_LOOP;
//		averageTimeParsed = averageTimeParsed/AVERAGE_LOOP;
//		averageSpeedup = (averageTimeParsed) / averageTimeCached;
//		
//		String rating = "failed ";
//		if (averageSpeedup > 9) {
//			rating = "passed excellent";
//		} else if (averageSpeedup > 6) {
//			rating = "passed ok";
//		} else if (averageSpeedup > 3) {
//			rating = "passed but poor";
//		}
//		System.err.printf(format(fmt, averageTimeCached, averageTimeParsed, averageSpeedup, rating));
//
//		assertTrue("function syntax tree not cached", averageSpeedup > 3);
//	}

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
	public final void testMatrixMultiplikation() throws Exception {
		String task = "A = {1,2;3,4;}; B = {4,3;2,1;}; m:A*m:B;";
		script.parse(task);
		Double[][] matrixExpected = {{8.0, 5.0}, {20.0, 13.0}};
		matrixCompare(matrixExpected, script.getMatrixSolution("m:A*m:B"));
	}
	
	@Test
	public final void testMatrixMultiRandom() throws Exception {
		int hightA = 10, widthA_hightB = 4, widthB = 4, range = 10; // range means myRnd(range) can be -range up to range.
	    
		Double[][] matrixA = new Double[hightA][widthA_hightB];
	    Double[][] matrixB = new Double[widthA_hightB][widthB];
		String task = rndMatrixDefinition(matrixA, "A", hightA, widthA_hightB, range) +
					rndMatrixDefinition(matrixB, "B", widthA_hightB, widthB, range) +
					"m:A*m:B";
		
	    Double[][] matrixExpected = matrixMultiplication(matrixA, matrixB);
		script.parse(task);
		matrixCompare(matrixExpected, script.getMatrixSolution("m:A*m:B"));
	}
	
	private long matrixParallel(int n) {
		int heightA = n-1, widthA_heightB = n, widthB =n+1, range = 10; // range means myRnd(range) can be -range up to range.
	    
		Double[][] matrixA = new Double[heightA][widthA_heightB];
	    Double[][] matrixB = new Double[widthA_heightB][widthB];
	    
	    //Init with random sh**
		for(int x = 0; x < heightA;x++) {
			for(int y = 0; y < widthA_heightB;y++) {
				matrixA[x][y] = range * (Math.random()+Math.random()-1);
			}
		}
		for(int x = 0; x < widthA_heightB;x++) {
			for(int y = 0; y < widthB;y++) {
				matrixB[x][y] = range * (Math.random()+Math.random()-1);
			}
		}
		
	    Double[][] matrixExpected = matrixMultiplication(matrixA, matrixB);
	    MyMatrix m1 = new MyMatrix(matrixA);
	    MyMatrix m2 = new MyMatrix(matrixB);
	    long l = System.nanoTime();
		matrixCompare(matrixExpected, m1.multiplyParrallel(m2).getDmatrix());
		l -= System.nanoTime();
		
		return l * -1;
	}
	
	private MyMatrix matrixGen(int n, int m) {
		int range = 10; // range means myRnd(range) can be -range up to range.
		Double[][] matrix = new Double[n][m];
	    
	    //Init with random sh**
		for(int x = 0; x < n;x++) {
			for(int y = 0; y < m;y++) {
				matrix[x][y] = range * (Math.random()+Math.random()-1);
			}
		}
		
		return new MyMatrix(matrix);
	}
	
//	@Test
	public final void testMatrixMultiTiming() throws Exception {
		int sets[][] = {{1, 64}, {10, 64}, {10, 128}, {5, 256}, {5, 512}, {2, 768}}; //, {2, 1024}, {1, 1536}, {1, 2048}
		
		//do sh*
		System.out.printf("\n repetitions \t | dimension \t | serial \t | parallel \t | speedup \n");
		long[][] times = new long[sets.length][2];
		for(int j = 0; j < sets.length; j++) {
			MyMatrix matrixA = matrixGen(sets[j][1]-1, sets[j][1]);
			MyMatrix matrixB = matrixGen(sets[j][1], sets[j][1]+1);
			long tmp;
			//Serial
			for(int i = 0; i < sets[j][0]; i++) {
				tmp = System.nanoTime();
				matrixA.multiplication(matrixB);
				times[j][0] += System.nanoTime() - tmp;
			}
			times[j][0] /= sets[j][0];
			
			//Parallel
			for(int i = 0; i < sets[j][0]; i++) {
				tmp = System.nanoTime();
				matrixA.multiplyParrallel(matrixB);
				times[j][1] += System.nanoTime() - tmp;
			}
			times[j][1] /= sets[j][0];
			double speedUp = times[j][0] / times[j][1];
			System.out.printf("\t %d \t | \t %d \t | %d \t | %d \t | %.2f \n", sets[j][0], sets[j][1], times[j][0]/100, times[j][1]/100, speedUp);
		}
		
	}
	
	@Test
	public final void testMatrixMultiTimingAlternative() throws Exception {
		int sets[][] = {{1, 64}, {10, 64}, {10, 128}, {5, 256}, {5, 512}, {2, 768}}; //, {2, 1024}, {1, 1536}, {1, 2048}
		
		//do sh*
		System.out.printf("\n repetitions \t | dimension \t | serial \t | parallel \t | speedup \n");
		long[][] times = new long[sets.length][2];
		for(int j = 0; j < sets.length; j++) {
			MyMatrix matrixA = matrixGen(sets[j][1]-1, sets[j][1]);
			MyMatrix matrixB = matrixGen(sets[j][1], sets[j][1]+1);
			Double[][] solutionMatrix = new Double[sets[j][1]-1][sets[j][1]+1];

			long tmp;
			//Serial
			for(int i = 0; i < sets[j][0]; i++) {
				tmp = System.nanoTime();
				matrixA.multiplication(matrixB);
				times[j][0] += System.nanoTime() - tmp;
			}
			times[j][0] /= sets[j][0];
			
			//Parallel
			for(int i = 0; i < sets[j][0]; i++) {
				tmp = System.nanoTime();
				matrixA.matParallelSimon(matrixB, solutionMatrix);
				times[j][1] += System.nanoTime() - tmp;
			}
			times[j][1] /= sets[j][0];
			double speedUp = times[j][0] / times[j][1];
			System.out.printf("\t %d \t | \t %d \t | %d \t | %d \t | %.2f \n", sets[j][0], sets[j][1], times[j][0]/100000, times[j][1]/100000, speedUp);
		}
		
	}
		
	@Test
	public final void testMatrixMultiWithFunction() throws Exception {
		String task = "a = 2; f(x) = 2*x; A = {1,2;3,f(a);}; B = {4,3;2,1;}; m:A*m:B;";
		script.parse(task);
		Double[][] matrixExpected = {{8.0, 5.0}, {20.0, 13.0}};
		matrixCompare(matrixExpected, script.getMatrixSolution("m:A*m:B"));
	}
	@Test
	public final void testMatrixMultiBothContainFunction() throws Exception {
		String task = "a = 2; f(x) = 2*x; A = {1,2;3,f(a);}; B = {f(a),3;2,1;};  m:A*m:B;";
		script.parse(task);
		Double[][] matrixExpected = {{8.0, 5.0}, {20.0, 13.0}};
		matrixCompare(matrixExpected, script.getMatrixSolution("m:A*m:B"));
	}
	@Test
	public final void testMatrixMultiManyFunctions() throws Exception {
		String task = "A = {1,2;3,f(a);}; B = {f(a),a(f);f(z(a)),1;};"
					+ "a = 2; f = 3; f(x) = 2*x; a(x) = (2*x)/2; z(x) = x/b; b = 2;"
					+ "m:A*m:B;";
		script.parse(task);
		Double[][] matrixExpected = {{8.0, 5.0}, {20.0, 13.0}};
		matrixCompare(matrixExpected, script.getMatrixSolution("m:A*m:B"));
	}
	
	
}
