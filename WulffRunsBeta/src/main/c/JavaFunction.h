/*
 * JavaFunction.h
 *
 * @since:   17.11.2012
 * @author:  nwulff
 * @version: $Id: JavaFunction.h,v 1.2 2017/12/03 17:42:09 nwulff Exp $
 */
#ifndef JAVAFUNCTION_H_
#define JAVAFUNCTION_H_
#include <jni.h>
#include "Function.h"
/**
 * A C++ Function suitable for JNI calls.
 */
class JavaFunction: public Function {
private:
	JNIEnv   *env;       // JVM environment
	jobject   instance;  // the Java function instance
	jmethodID fct;       // the Java method
	jstring   jname;     // the Java function name
	jdoubleArray array;  // the Java array as argument
public:
	/**
	 * Constructor to wrap a Java Function implementation.
	 */
	JavaFunction(JNIEnv *env,jobject instance);
	virtual ~JavaFunction();
	// overloaded operator to execute  double y = f(x)
	// for java functions implementations.
	virtual double operator()(double x) const {
		// our C++ functions are one dimensional the java function not...
        env->SetDoubleArrayRegion(array,0,1,&x);
        return (env->CallDoubleMethod(instance,fct,array));
	}
};
#endif /* JAVAFUNCTION_H_ */
