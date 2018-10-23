/*
 * Differentiator.h
 *
 * @since:   17.11.2012
 * @author:  nwulff
 * @version: $Id: Differentiator.h,v 1.1 2017/12/02 15:47:22 nwulff Exp $
 */

#ifndef DIFFERENTIATOR_H_
#define DIFFERENTIATOR_H_
#include "Function.h"
#ifdef __cplusplus
extern "C" {
#endif
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
