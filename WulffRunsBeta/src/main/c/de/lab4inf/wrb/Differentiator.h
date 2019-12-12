/*
 * Differentiator.h
 *
 * @since:   17.11.2012
 * @author:  nwulff
 * @version: $Id: Differentiator.h,v 1.1 2017/12/02 15:47:22 nwulff Exp $
 */

#ifndef DIFFERENTIATOR_H_
#define DIFFERENTIATOR_H_

#include "de_lab4inf_wrb_Differentiator.h"
#include "Function.h"

#define MAX_CALLS 12

#ifdef __cplusplus
extern "C" {
#endif
extern int diff_calls;

/**
 * Numerical differentiate the given function at point x.
 * @param f function to differentiate
 * @param x argument
 * @return value f'(x)
 */
double differentiate(Function& f, double x, double err);
#ifdef __cplusplus
}
#endif

#endif /* DIFFERENTIATOR_H_ */
