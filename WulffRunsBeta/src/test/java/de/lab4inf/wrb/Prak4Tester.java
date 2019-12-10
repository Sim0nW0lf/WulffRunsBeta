/*
 * Project: WRB
 *
 * Copyright (c) 2008-2013,  Prof. Dr. Nikolaus Wulff
 * University of Applied Sciences, Muenster, Germany
 * Lab for Computer Sciences (Lab4Inf).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package de.lab4inf.wrb;

import static java.lang.String.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Abstract base test of the Differentiator and the Integrator implementations.
 * To execute extend this class and implement the getScript method as in Lab 2.
 * 
 * @author nwulff
 * @since 28.11.2013
 * @version $Id: Prak4Tester.java,v 1.17 2018/12/04 17:28:20 nwulff Exp $
 */
public abstract class Prak4Tester {
    /** the attribute TOO_MANY_FUNCTION_CALLS. */
    private static final String INTEGRATOR_TOO_MANY_FUNCTION_CALLS = "looks like Kepler Integration! too many function calls %d ";
    private static final String DIFFERENTIATOR_TOO_MANY_FUNCTION_CALLS = "looks like 2-point Diffenrentiator! too many function calls %d ";
    static final String DIFFERENTIATOR = "de.lab4inf.wrb.Differentiator";
    static final String INTEGRATOR = "de.lab4inf.wrb.Integrator";
    static final double EPS = 1.E-8; // relaxed tolerance...
    static final int CALLS_DIFFERENTIATOR = 12; // maximal calls for differentiation
    static final int CALLS_INTEGRATOR = 512; // maximal calls for integration
    protected double eps = EPS;
    private Script script;
    protected int calls;
    private static Differentiator diff;
    private static Integrator intg;

    /**
     * Construct the Differentiator and Integrator via Reflection.
     * This checks:</br>
     * a) the package and class names are as wanted.
     * b) the native C library could be instantiated.
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static final void checkNatives() throws Exception {
        Function fct = new DummyFunction();
        Class<?> clazz = null;
        String className = null;
        // construct the Differentiator class and instance
        try {
            className = DIFFERENTIATOR;
            clazz = Class.forName(className);
            diff = (Differentiator) clazz.newInstance();
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }
        assertNotNull("no Differentiator "+className, diff);

        // construct the Integrator class and instance
        try {
            clazz = null;
            className = INTEGRATOR;
            clazz = Class.forName(className);
            intg = (Integrator) clazz.newInstance();
        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }
        ;
        assertNotNull("no Integrator "+className, intg);

        // dummy calls to see if native functions are found
        diff.differentiate(fct, 0);
        intg.integrate(fct, 0, 1);
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public final void setUp() throws Exception {
        calls = 0;
        script = getScript();
        diff.setError(eps);
        intg.setEps(eps);
        assertNotNull("no script implementation", script);
    }

    /**
     * Get the actual implementation for the script test.
     * 
     * @return script implementation
     */
    protected abstract Script getScript();

    /**
     * Calculate the relative or absolute tolerated error.
     * 
     * @param f the true function value to check for
     * @return the maximal tolerance
     */
    protected final double rEps(final double f) {
        double a = Math.abs(f), rEps = eps;
        if ((0<a&&a<0.1)||a>1)
            rEps *= a;
        return rEps;
    }

    @Test(timeout = 1000)
    public void testDifferentiatePoly2() {
        Poly p = new Poly(1, 2, 3);
        Poly dP = new Poly(2, 6);
        double x, y, z;

        for (x = 0; x<4; x += 0.25) {
            calls = 0;
            y = diff.differentiate(p, x);
            z = dP.eval(x);
            assertEquals(z, y, rEps(z));
            assertTrue(format(DIFFERENTIATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_DIFFERENTIATOR);
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiatePoly3() {
        Poly p = new Poly(1, 2, 3, 4);
        Poly dP = new Poly(2, 6, 12);
        double x, y, z;

        for (x = 0; x<4; x += 0.25) {
            z = dP.eval(x);
            calls = 0;
            y = diff.differentiate(p, x);
            // y=2+3*2*x+4*3*x*x;
            assertEquals(z, y, rEps(z));
            assertTrue(format(DIFFERENTIATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_DIFFERENTIATOR);
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiatePoly4() {
        Poly p = new Poly(1, 2, 3, 4, 5);
        Poly dP = new Poly(2, 6, 12, 20);
        double x, y, z;

        for (x = 0; x<4; x += 0.25) {
            z = dP.eval(x);
            calls = 0;
            y = diff.differentiate(p, x);
            assertEquals(z, y, rEps(z));
            assertTrue(format(DIFFERENTIATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_DIFFERENTIATOR);
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiatePoly5() {
        Poly p = new Poly(1, 2, 3, 4, 5, 6);
        Poly dP = new Poly(2, 6, 12, 20, 30);
        double x, y, z;

        for (x = 0; x<4; x += 0.25) {
            z = dP.eval(x);
            calls = 0;
            y = diff.differentiate(p, x);
            assertEquals(z, y, rEps(z));
            assertTrue(format(DIFFERENTIATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_DIFFERENTIATOR);
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiatePoly6() {
        Poly p = new Poly(1, 2, 3, 4, 5, 6, 7);
        Poly dP = new Poly(2, 6, 12, 20, 30, 42);
        double x, y, z;

        for (x = 0; x<4; x += 0.25) {
            z = dP.eval(x);
            calls = 0;
            y = diff.differentiate(p, x);
            assertEquals(z, y, rEps(z));
            assertTrue(format(DIFFERENTIATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_DIFFERENTIATOR);
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiatePoly7() {
        Poly p = new Poly(1, 2, 3, 4, 5, 6, 7, 8);
        Poly dP = new Poly(2, 6, 12, 20, 30, 42, 56);
        double x, y, z;

        for (x = 0; x<4; x += 0.25) {
            z = dP.eval(x);
            calls = 0;
            y = diff.differentiate(p, x);
            assertEquals(z, y, rEps(z));
            assertTrue(format(DIFFERENTIATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_DIFFERENTIATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly1() {
        Poly p = new Poly(1, 2);
        Poly iP = new Poly(0, 1, 1);
        double a = 1, b = 5, x, y, z;

        for (x = a; x<b; x += 0.25) {
            z = iP.eval(x)-iP.eval(a);
            calls = 0;
            y = intg.integrate(p, a, x);
            assertEquals(z, y, rEps(z));
            assertTrue(format(INTEGRATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly2() {
        Poly p = new Poly(1, 2, 3);
        Poly iP = new Poly(0, 1, 1, 1);
        double a = 1, b = 5, x, y, z;

        for (x = a; x<b; x += 0.25) {
            z = iP.eval(x)-iP.eval(a);
            calls = 0;
            y = intg.integrate(p, a, x);
            assertEquals(z, y, rEps(z));
            assertTrue(format(INTEGRATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly3() {
        Poly p = new Poly(1, 1, 1, 1);
        Poly iP = new Poly(4711, 1, 0.5, 1./3, 0.25);
        double a = 1, b = 5, x, y, z;

        for (x = a; x<b; x += 0.25) {
            z = iP.eval(x)-iP.eval(a);
            calls = 0;
            y = intg.integrate(p, a, x);
            assertEquals(z, y, rEps(z));
            assertTrue(format(INTEGRATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly4() {
        Poly p = new Poly(1, 2, 3, 4, 5);
        Poly iP = new Poly(0, 1, 1, 1, 1, 1);
        double a = 0, b = 3, x, y, z;

        for (x = a; x<b; x += 0.25) {
            z = iP.eval(x)-iP.eval(a);
            calls = 0;
            y = intg.integrate(p, a, x, eps);
            System.out.printf("calls %d \n", calls);
            assertEquals(z, y, rEps(z));
            assertTrue(format(INTEGRATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly5() {
        Poly p = new Poly(1, 2, 3, 4, 5, 6);
        Poly iP = new Poly(0, 1, 1, 1, 1, 1, 1);
        double a = 0, b = 3, x, y, z;

        for (x = a; x<b; x += 0.25) {
            z = iP.eval(x)-iP.eval(a);
            calls = 0;
            y = intg.integrate(p, a, x, eps);
            assertEquals(z, y, rEps(z));
            assertTrue(format(INTEGRATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly6() {
        Poly p = new Poly(1, 2, 3, 4, 5, 6, 7);
        Poly iP = new Poly(0, 1, 1, 1, 1, 1, 1, 1);
        double a = 0, b = 2, x, y, z;

        for (x = a; x<b; x += 0.25) {
            z = iP.eval(x)-iP.eval(a);
            calls = 0;
            y = intg.integrate(p, a, x, eps);
            assertEquals(z, y, rEps(z));
            assertTrue(format(INTEGRATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly7() {
        Poly p = new Poly(1, 2, 3, 4, 5, 6, 7, 8);
        Poly iP = new Poly(0, 1, 1, 1, 1, 1, 1, 1, 1);
        double a = 0, b = 1, x, y, z;

        for (x = a; x<b; x += 0.25) {
            z = iP.eval(x)-iP.eval(a);
            calls = 0;
            y = intg.integrate(p, a, x, eps);
            assertEquals(z, y, rEps(z));
            assertTrue(format(INTEGRATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratePoly4BordersWrong() {
        Poly p = new Poly(1, 2, 3, 4, 5);
        Poly iP = new Poly(0, 1, 1, 1, 1, 1);
        double a = 0, b = 3, x, y, z;

        for (x = a; x<b; x += 0.25) {
            z = iP.eval(a)-iP.eval(x);
            calls = 0;
            y = intg.integrate(p, x, a, eps);
            assertEquals(z, y, rEps(z));
            assertTrue(format(INTEGRATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateThrowsException() {
        double y, x = 1;
        try {
            Function f = new NoneContinuous();
            y = diff.differentiate(f, x);
            Assert.fail("no exception thrown "+y);
        } catch (ArithmeticException error) {
            String msg = error.getMessage();
            assertTrue(msg, msg.contains("no convergence"));
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateLPInt() {
        double y, x = 2;
        try {
            Function f = new LinearPeriodic();
            y = diff.differentiate(f, x);
            Assert.fail("no exception thrown "+y);
        } catch (ArithmeticException error) {
            String msg = error.getMessage();
            assertTrue(msg, msg.contains("no convergence"));
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateLP() {
        double y, x = 2;
        Function f = new LinearPeriodic();
        for (x = 0.1; x<=0.9; x += 0.1) {
            y = diff.differentiate(f, x);
            assertEquals(1.0, y, eps);
        }
    }

    @Test(timeout = 1000)
    public void testIntegrateLPInt() {
        double y, a = -0.5, b = 0.5;
        try {
            Function f = new LinearPeriodic();
            y = intg.integrate(f, a, b);
            Assert.fail("no exception thrown "+y);
        } catch (ArithmeticException error) {
            String msg = error.getMessage();
            assertTrue(msg, msg.contains("no convergence"));
        }
    }

    @Test(timeout = 1000)
    public void testIntegrateLP() {
        double y, x, a = 0.1, b = 0.9, dx = (b-a)/10;
        Function f = new LinearPeriodic();
        for (x = a+dx; x<=b; x += dx) {
            y = intg.integrate(f, a, x);
            assertEquals((x*x-a*a)/2-(x-a), y, eps);
        }
    }

    @Test(timeout = 1000)
    public void testIntegratorThrowsException() {
        double y, a = 0, b = 1;
        try {
            Function f = new NoneContinuous();
            y = intg.integrate(f, a, b);
            Assert.fail("no exception thrown "+y);
        } catch (ArithmeticException error) {
            String msg = error.getMessage();
            // error.printStackTrace();
            assertTrue(msg, msg.contains("no convergence"));
        }
    }

    @Test(timeout = 1000)
    public void testIntegratorThrowsExceptionScript() {
        String def = "a=0.0000000001;y(z)=1/(z+a)";
        script.parse(def);
        Function f = script.getFunction("y");
        double y, a = -0.5, b = 0.5;
        try {
            y = intg.integrate(f, a, b);
            Assert.fail("no exception thrown "+y);
        } catch (ArithmeticException error) {
            String msg = error.getMessage();
            // error.printStackTrace();
            assertTrue(msg, msg.contains("no convergence"));
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateHyperbola() {
        double a = 0.1, b = 3, x, y, z;
        Function hyp = new Hyperbola();
        for (x = a; x<b; x += 0.25) {
            z = -1/(x*x);
            calls = 0;
            y = diff.differentiate(hyp, x);
            assertEquals(z, y, rEps(z));
            assertTrue(format(INTEGRATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateSine() {
        double a = 0.1, b = 3, x, y, z;
        Function sin = new Sine();
        for (x = a; x<b; x += 0.25) {
            z = Math.cos(x);
            calls = 0;
            y = diff.differentiate(sin, x);
            assertEquals(z, y, rEps(z));
            assertTrue(format(DIFFERENTIATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_DIFFERENTIATOR);
        }
    }

    @Test(timeout = 1000)

    public void testDifferentiateSineScript() throws Exception {
        double a = 4, b = 2, x, y, z;
        String def = String.format("b=%.0f; f(u)=sin(b*u);", b);
        script.parse(def);
        Function f = script.getFunction("f");
        for (x = b+0.2; x<a; x += 0.25) {
            z = b*Math.cos(b*x);
            y = diff.differentiate(f, x);
            assertEquals(z, y, rEps(z));
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateSineExpansion() throws Exception {
        double x, result, expected, delta = 1.E-4;
        // sine function Taylor expansion
        String task = "a1=1;a3=-1/6;a5=1/120; a7=-1/5040; a0=0;a2=0;a4=0;a6=0;a8=0;"
                +"s(x)=a0+a1*x+a2*x*x+a3*x^3+a4*x^4+a5*x^5+a6*x^6+a7*x^7+a8*x^8; err(x) = s(x) - sin(x);";
        script.parse(task);
        Function f = script.getFunction("s");
        Function e = script.getFunction("err");
        for (x = -0.5; x<1.5; x += 0.1) {
            // first check that sin is within tolerance
            expected = Math.sin(x);
            result = f.eval(x);
            assertEquals(expected, result, delta);
            result = e.eval(x);
            assertEquals(0, result, delta);
            // now differentiate the script approximation
            expected = Math.cos(x);
            result = diff.differentiate(f, x);
            assertEquals(expected, result, delta*10);
        }
    }

    @Test(timeout = 1000)
    public void testIntegrateSineExpansion() throws Exception {
        double x, result, expected, delta = 5.E-6;
        // one more term in sine function Taylor expansion.
        String task = "a1=1;a3=-1/6;a5=1/120; a7=-1/5040; a0=0;a2=0;a4=0;a6=0;a8=0;a9=1/362880;"
                +"s(x)=a0+a1*x+a2*x*x+a3*x^3+a4*x^4+a5*x^5+a6*x^6+a7*x^7+a8*x^8+a9*x^9;";
        script.parse(task);
        Function f = script.getFunction("s");
        for (x = 0.1; x<1.5; x += 0.1) {
            // first check that sin is within tolerance
            expected = Math.sin(x);
            result = f.eval(x);
            assertEquals(expected, result, delta);
            // now integrate the script approximation
            expected = 1-Math.cos(x);
            result = intg.integrate(f, 0, x);
            assertEquals(expected, result, delta);
        }
    }

    @Test(timeout = 1000)
    public void testIntegrateHyperbola() {
        double a = 0.5, b = 1, x, y, z;
        Function hyp = new Hyperbola();
        for (x = a; x<b; x += 0.25) {
            z = Math.log(x/a);
            calls = 0;
            y = intg.integrate(hyp, a, x);
            assertEquals(z, y, rEps(z));
            assertTrue(format(INTEGRATOR_TOO_MANY_FUNCTION_CALLS, calls), calls<=CALLS_INTEGRATOR);
        }
    }

    @Test(timeout = 1000)
    public void testIntegrateHyperbolaScript() throws Exception {
        double a = 4, b = 2, x, y, z;
        String def = String.format("b=%.0f; g(u)=1/(u-b);", b);
        script.parse(def);
        Function hyp = script.getFunction("g");
        for (x = b+0.2; x<a; x += 0.25) {
            z = Math.log((x-b)/(a-b));
            y = intg.integrate(hyp, a, x);
            assertEquals(z, y, rEps(z));
        }
    }

    @Test(timeout = 1000)
    public void testIntegrateSineScript() throws Exception {
        double a = 4, b = 2, cosab, x, y, z;
        String def = String.format("b=%.0f; f(u)=sin(b*u);", b);
        script.parse(def);
        Function f = script.getFunction("f");
        cosab = Math.cos(a*b);
        for (x = b+0.2; x<a; x += 0.25) {
            z = (cosab-Math.cos(b*x))/b;
            y = intg.integrate(f, a, x);
            assertEquals(z, y, rEps(z));
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateScriptFct() throws Exception {
        double a = 0.1, b = 3, x, y, z;
        String def = "a=-1;y(z)=z**4 +a*z**2 + 1/z; g(v)=y(v)";
        script.parse(def);
        Function fct = script.getFunction("g");
        for (x = a; x<b; x += 0.25) {
            z = (4*x*x-2)*x-1/(x*x);
            y = diff.differentiate(fct, x);
            assertEquals(z, y, rEps(z));
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateScriptModSine() throws Exception {
        double a = 0.1, b = 2, x, y, z;
        String def = "f(x)=x*sin(1/x); df(z)=(f(z)-cos(1/z))/z;";
        script.parse(def);
        Function fct = script.getFunction("f");
        Function df = script.getFunction("df");
        for (x = a; x<b; x += 0.1) {
            z = df.eval(x);
            y = diff.differentiate(fct, x);
            assertEquals(z, y, rEps(z));
        }
    }

    @Test(timeout = 1000)
    public void testDifferentiateScriptFailing() throws Exception {
        double a = -0.05, b = -a, x, y, z;
        String def = "f(x)=x*sin(1/x); df(z)=(f(z)-cos(1/z))/z;";
        script.parse(def);
        Function fct = script.getFunction("f");
        Function df = script.getFunction("df");
        try {
            for (x = a; x<b; x += 0.01) {
                z = df.eval(x);
                y = diff.differentiate(fct, x);
                assertEquals(z, y, rEps(z));
            }
            Assert.fail("no convergence not detected");
        } catch (ArithmeticException error) {
            String msg = error.getMessage();
            assertTrue(msg, msg.contains("no convergence"));
        }
    }

    @Test
    public void testIntegrateScriptFct() throws Exception {
        double a = 0.1, b = 3, za, x, y, z;
        String def = "c=1; foo(u)=u**4 - u**2 + c/u; y(x)=x*x";
        script.parse(def);
        Function hyp = script.getFunction("foo");
        za = Math.pow(a, 5)/5-Math.pow(a, 3)/3+Math.log(a);
        for (x = a; x<b; x += 0.25) {
            z = (x*x*x/5-x/3)*x*x+Math.log(x)-za;
            y = intg.integrate(hyp, a, x);
            assertEquals(z, y, rEps(z));
        }
    }

    /**
     * Base class for test functions, incrementing the internal calls counter.
     */
    abstract class JUnitTestFunction implements Function {

        /*
         * (non-Javadoc)
         * 
         * @see de.lab4inf.wrb.Function#eval(double[])
         */
        @Override
        public double eval(double... args) {
            calls++ ;
            return value(args[0]);
        }

        protected abstract double value(final double x);
    }

    /**
     * Hyperbola function y=1/x.
     */
    class Hyperbola extends JUnitTestFunction {
        // @Override
        @Override
        public double value(double x) {
            return 1/x;
        }

    }

    /**
     * Sine function y=sin(x).
     */
    class Sine extends JUnitTestFunction {
        // @Override
        @Override
        public double value(double x) {
            return Math.sin(x);
        }

    }

    /**
     * Simple polynomial function.
     */
    class Poly extends JUnitTestFunction {
        private final double[] a;
        private final int n;

        public Poly(final double... coeff) {
            a = coeff;
            n = a.length;
        }

        // @Override
        @Override
        public double value(double x) {
            double y = a[n-1];
            for (int k = n-2; k>=0; k-- ) {
                y = y*x+a[k];
            }
            return y;
        }
    }

    /**
     * Dummy function just to see if Differentiator and Integrator are loaded.
     */
    static class DummyFunction implements Function {
        /*
         * (non-Javadoc)
         * 
         * @see de.lab4inf.wrb.Function#eval(double[])
         */
        @Override
        public double eval(double... args) {
            return 0;
        }
    }

    /**
     * None continuous random function to see if Differentiator and Integrator throw
     * exceptions.
     */
    class NoneContinuous extends JUnitTestFunction {
        /*
         * (non-Javadoc)
         * 
         * @see de.lab4inf.wrb.Prak4Tester.JUnitTestFunction#value(double)
         */
        @Override
        protected double value(double x) {
            return Math.random();
        }
    }

    /**
     * Oscillating function not differentiable at the origin to see if Differentiator and Integrator throw
     * exceptions.
     */
    class Oszillating extends JUnitTestFunction {
        /*
         * (non-Javadoc)
         * 
         * @see de.lab4inf.wrb.Prak4Tester.JUnitTestFunction#value(double)
         */
        @Override
        protected double value(double x) {
            if (Math.abs(x)<=Double.MIN_VALUE) {
                return 0;
            }
            return x*Math.sin(1/x);
        }
    }

    /**
     * Periodic piecewise linear function with none differential borders.
     */
    class LinearPeriodic extends JUnitTestFunction {
        /*
         * (non-Javadoc)
         * 
         * @see de.lab4inf.wrb.Prak4Tester.JUnitTestFunction#value(double)
         */
        @Override
        protected double value(double x) {
            double y = x-Math.ceil(x);
            return y;
        }
    }

}
