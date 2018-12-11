#include "Differentiator.h"
#include "Function.h"
#include <iostream>
#include <stdlib.h>
#include <cmath>

int MAXSTEPS = 16;

double differentiate(Function& f, double x, double err) {
	int i = 10;
	double d1 = 0, d2 = 0;
	double ld1, ld2;
	double h = 1.E-6;
	double fx = f(x);

	for (i = MAXSTEPS; i > 0 && h > err; i--) {
		ld1 = d1;
		ld2 = d2;

		d1 = (f(x + h) - fx) / h;
		d2 = (fx - f(x - h)) / h;

		if (std::abs(d1) < err && std::abs(d2) < err) {
			//	std::cout << "early return at " << i << std::endl;
			return (d1 + d2) / 2;
		}

		h /= 8;
	}
	return (d1 + d2) / 2;
}
