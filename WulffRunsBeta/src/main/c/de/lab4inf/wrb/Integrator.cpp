#include <stdio.h>
#include <stdlib.h>
#include <cmath>
#include <math.h>

#include "JavaFunction.h"
#include "Integrator.h"

int int_calls;

/*
 * Check if the integral converges
 */
bool integral_converges(double an, double old_an, int i, int n, double eps) {
	if(i >= 2) {
		// Check absolute error
		if(fabs(an-old_an) < eps)
			return true;

		// Check relative error
		if(fabs(an - old_an) < eps  * fabs(an+old_an)/2)
			return true;
	}

	// if integration takes too long, it doesn't converge
	if(n>MAX_N)
		throw "no convergence";

	return false;
}

double integrate(Function &f, double a, double b, double eps) {
	double result, old_result, h, sum1, sum2, xj, xj1, fa, fb;
	int i=0;				// amount of loops
	int n=1;				// the bigger the n, the better the accuracy
	int_calls = 0;			// amount of function calls

	if(f(a) != f(a))
		throw "no convergence";

	// Save f(a) and f(b) so you don't have to do 2 more function calls in every loop
	fa = f(a);
	fb = f(b);
	int_calls += 2;

	if(b==a)
		return 0;

	do {
		old_result = result;
		sum1 = 0, sum2 = 0;
		h = (b-a)/n;

		// calculate left sum
		for(int j=1; j<n; j++) {
			xj = a+h*j;
			sum1 += f(xj);
			int_calls++;
		}

		// calculate right sum
		for(int j=0; j<n; j++) {
			xj = a+h*j;
			xj1 = a+h*(j+1);
			sum2 += f((xj+xj1)/2);
			int_calls++;
		}

		result = ((b-a)/(6*n))*(fa + fb + 2*sum1 + 4*sum2);
		n *= 2;				// raise n for more accuracy
		i++;
	}while(!integral_converges(result, old_result, i, int_calls, eps));

	return result;
}

JNIEXPORT jdouble JNICALL Java_de_lab4inf_wrb_Integrator_integrate
(JNIEnv *jvm, jobject jThis, jobject jFunction, jdouble a, jdouble b, jdouble eps) {
	JavaFunction f = JavaFunction(jvm, jFunction);
	try {
		return integrate(f, a, b, eps);
	} catch(const char *msg) {
		jclass exp = jvm->FindClass("java/lang/ArithmeticException");
		jvm->ThrowNew(exp, msg);
	}
	return -1;
}

