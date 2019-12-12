#include <stdio.h>
#include <stdlib.h>
#include <cmath>
#include <math.h>

#include "JavaFunction.h"
#include "Differentiator.h"

int diff_calls;

/*
 * Calculates f(x+h)-f(x-h)
 */
double delta(Function &f, double h, double x) {
	return f(x+h)-f(x-h);
}

/*
 * Checks if the result of the differentiation converges
 */
bool diff_converges(double an, double old_an, int i, int n, double eps) {
	if(i >= 2) {
		// Check absolute error
		if(fabs(an-old_an) < eps)
			return true;

		// Check relative error
		if(fabs(an - old_an) < eps  * fabs(an+old_an)/2)
			return true;
	}

	// if differentiaton takes too long, it doesn't converge
	if(n>MAX_CALLS)
		throw "no convergence";

	return false;
}


double differentiate(Function &f, double x, double eps) {
	int i = 0;					// amount of loops
	diff_calls = 0; 			// amount of function calls
	double dl, old_dl, fh, old_fh, result, old_result;
	double h = 1.E-3;
	double fx = f(x);

	if(fx != f(x))
		throw "no convergence";

	do {
		// calculate delta(x)
		old_dl = dl;
		dl = delta(f,h,x);
		diff_calls += 2;

		// calculate fh(x)
		old_fh = fh;
		fh = (8*dl-old_dl)/(12*h);

		// calculate the result of the differentiation
		old_result = result;
		result = (16*fh-old_fh)/15;

		h /= 2;
		i++;
	}while(!diff_converges(result, old_result, i, diff_calls, eps));

	return result;
}

JNIEXPORT jdouble JNICALL Java_de_lab4inf_wrb_Differentiator_differentiate
(JNIEnv *jvm, jobject jThis, jobject jFunction, jdouble x, jdouble eps) {
	JavaFunction f = JavaFunction(jvm, jFunction);
	try {
		return differentiate(f, x, eps);
	} catch(const char *msg) {
		jclass exp = jvm->FindClass("java/lang/ArithmeticException");
		jvm->ThrowNew(exp, msg);
	}
	return -1;
}

