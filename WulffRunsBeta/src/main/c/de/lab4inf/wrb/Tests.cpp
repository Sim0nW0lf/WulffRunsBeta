#include <stdio.h>
#include <stdlib.h>
#include <cmath>
#include <math.h>
#include <time.h>

#include "Differentiator.h"
#include "Integrator.h"
#include "Function.h"
#include "Tests.h"
#include "CUnit.h"


#define EPS 1.E-8

double rEps(double f) {
	double a = fabs(f), rEps = EPS;

	if ((0 < a && a < 0.1) || a > 1)
		rEps *= a;
	if (rEps < EPS)
		return EPS;
	return rEps;
}

void test_function(Function &f, Function &dx, Function &F, double start, double stop, double step){
	printf("\nTesting function '%s'\n"
			"---------+----------------+----------------+----------------+-----+------\n"
			"    x    |   f(x)=%3s(x)  |      f'(x)     |       F(x)     | #f' | #int  \n"
			"---------+----------------+----------------+----------------+-----+------\n", f.name(), f.name());
	double x;
	double fx;
	double dxx;
	double Fx;
	for(x=start; x<=stop; x+=step){
		fx = f(x);
		try {
			dxx = differentiate(f, x, EPS);
			assertEqualsF(dxx, dx(x), rEps(dxx));
		} catch(const char* error) {
			dxx = NAN;
		}
		try {
			Fx = integrate(f, 0, x, EPS);
			assertEqualsF(Fx, F(x)-F(0), rEps(Fx));
		} catch(const char* error) {
			Fx = NAN;
		}

		printf(" %7.4f | %14.8f | %14.8f | %14.8f | %3d | %4d\n", x, fx,
				dxx, Fx, diff_calls, int_calls);
		assertEqualsF(dxx, dx(x), rEps(dxx));
		assertEqualsF(Fx, F(x)-F(0), rEps(Fx));
	}
	printf("\n");

}

int x_quadratTest(int i, char** args){
	Function f = Function(x_quadrat, "x^2");
	Function dx = Function(x_quadrat_diff, "2x");
	Function F = Function(x_quadrat_int, "(x^3)/3");
	test_function(f, dx, F, 0, 1, 0.25);
	return 0;
}

int expTest(int i, char** args){
	Function f = Function(my_exp, "Exp");
	Function dx = Function(my_exp, "Exp");
	Function F = Function(my_exp, "Exp");
	test_function(f, dx, F, 0, 10, 1);
	return 0;
}

int sinTest(int i, char** args){
	Function f = Function(my_sin, "Sin");
	Function dx = Function(my_cos, "Cos");
	Function F = Function(my_sin_int, "-Cos");
	test_function(f, dx, F, 0, M_PI, M_PI/8);
	return 0;
}

int cosTest(int i, char** args){
	Function f = Function(my_cos, "Cos");
	Function dx = Function(my_cos_diff, "-Sin");
	Function F = Function(my_sin, "Sin");
	test_function(f, dx, F, 0, M_PI, M_PI/8);
	return 0;
}

int tanTest(int i, char** args){
	Function f = Function(my_tan, "Tan");
	Function dx = Function(my_tan_diff, "1/cos^2(x)");
	Function F = Function(my_tan_int, "-log(cos(x))");
	test_function(f, dx, F, 0, M_PI/4, M_PI/16);
	return 0;
}

int EinsDurchxTest(int i, char** args){
	Function f = Function(my_1_x, "1/x");
	Function dx = Function(my_1_x_diff, "-1/x^2");
	Function F = Function(my_1_x_int, "log(x)");
	test_function(f, dx, F, 1, 5, 1);
	return 0;
}

DECLARE_TEST(x_quadrat);
DECLARE_TEST(exp);
DECLARE_TEST(sin);
DECLARE_TEST(cos);
DECLARE_TEST(tan);
DECLARE_TEST(EinsDurchx);

BEG_SUITE(suite)
ADD_TEST(x_quadrat),
ADD_TEST(exp),
ADD_TEST(sin),
ADD_TEST(cos),
ADD_TEST(tan),
ADD_TEST(EinsDurchx)

END_SUITE(suite)

RUN_SUITE(suite)
