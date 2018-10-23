#ifndef _Included_de_lab4inf_wrb_Integrator
#define _Included_de_lab4inf_wrb_Integrator
#include <jni.h>
#include "Function.h"
#include "JavaFunction.h"
#include "Integrator.h"

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     de_lab4inf_wrb_Integrator
 * Method:    integrate
 * Signature: (Lde/lab4inf/wrb/Function;DDD)D
 */
JNIEXPORT jdouble JNICALL Java_de_lab4inf_wrb_Integrator_integrate
(JNIEnv * env, jobject obj, jobject fct, jdouble a, jdouble b, jdouble eps) {
    double dF =0;
    try {
        JavaFunction f = JavaFunction(env,fct);
        dF = integrate(f,a,b,eps);
    } catch(const char* error) {
        jclass jExcepClazz = env->FindClass("java/lang/ArithmeticException");
        env->ThrowNew(jExcepClazz, error);
    }
    return (dF);
}

#ifdef __cplusplus
}
#endif
#endif
