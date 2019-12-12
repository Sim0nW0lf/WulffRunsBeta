/*
 * Function.h
 *
 * @since:   17.11.2012
 * @author:  nwulff
 * @version: $Id: Function.h,v 1.2 2017/12/03 17:42:09 nwulff Exp $
 */

#ifndef FUNCTION_H_
#define FUNCTION_H_
#include <assert.h>
/**
 * Declaration for a real valued C function-pointer, mapping
 * 1-dimensional double to double
 */
typedef double (*FctPointer)(double x);
/**
 * A C++ Function class wrapping standard C function-pointers.
 */
class Function {
protected:
	FctPointer fp;           // C function-pointer to use
	const char* fctName;     // optional function name
	// constructor for derived classes like JavaFunction
	Function() : fp(0), fctName("Java-Fct") {};
public:
	// constructor to wrap a C math function-pointer
	Function(const FctPointer p, const char* n="C-Fct")
			   : fp(p), fctName(n){
	};
	// virtual destructor for derived classes
	virtual ~Function(){};
    // access to the function name
	const char* name() const {assert(fctName!=0); return (fctName);};
    // operator to execute  double y = f(x) with 1-dim array
    virtual double operator()(double x) const {assert(fp!=0); return (fp(x));};
};
#endif /* FUNCTION_H_ */
