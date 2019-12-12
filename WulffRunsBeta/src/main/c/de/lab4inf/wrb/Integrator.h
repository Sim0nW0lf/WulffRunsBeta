/*
 * Integrator.h
 *
 * @since:   17.11.2012
 * @author:  nwulff
 * @version: $Id: Integrator.h,v 1.1 2017/12/02 15:47:21 nwulff Exp $
 */

#ifndef INTEGRATOR_H_
#define INTEGRATOR_H_

#include "de_lab4inf_wrb_Integrator.h"
#include "Function.h"

#define MAX_N 1200

#ifdef __cplusplus
extern "C" {
#endif
extern int int_calls;

/**                                   /b
 * Numerical calculation of F[b,a] = / f(t) dt within integration borders a,b.
 *                                  /a
 * @param f function to integrate
 * @param a left integration border
 * @param x right integration border
 * @param eps precission
 * @return F(b)-F(a)
 */
double integrate(Function& f, double a, double b, double eps);
#ifdef __cplusplus
}
#endif

#endif /* INTEGRATOR_H_ */
