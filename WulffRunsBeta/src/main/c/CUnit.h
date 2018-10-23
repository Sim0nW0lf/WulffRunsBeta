/*
 *  CUnit.h
 *
 * Some macros and declaration for a C unit test.
 * A typical (main) test function will coded like:
 *
 * =====================================================
 *
 * DECLARE_TEST(modul_a)
 * DECLARE_TEST(modul_b)
 * DECLARE_TEST(modul_c)
 *
 * BEG_SUITE(suite)
 *   ADD_TEST(modul_a),
 *   ADD_TEST(modul_b),
 *   ADD_TEST(mudul_c
 * END_SUITE(suite)
 *
 * RUN_SUITE(suite)
 *
 * =====================================================
 *
 * By naming convention the user has to code three
 * functions modul_aTest,modul_bTest and modul_cTest
 * enriched with some assertTrue, assertEquals and fail
 * expressions from the set of provided CUnit macros.
 *
 *
 *  @since:   23.11.2014
 *  @author:  nwulff
 *  @version: $Id: CUnit.h,v 1.2 2017/12/05 18:24:41 nwulff Exp $
 */

#ifndef _CUNIT_H_
#define _CUNIT_H_
#include <assert.h>
#include <string.h>
#include <stdio.h>
#ifdef __cplusplus
extern "C" {
#endif

/**
 * main method a like signature for a test method to execute.
 */
typedef int (*cunit_Test)(int, char**);
/**
 * Internal structure to for the named test cases to execute.
 */

typedef struct cunit_testSuite {
	char const *name;
	cunit_Test test;
} CUnitTestSuite;
/**
 * Our "main" method enhanced with test suite to execute.
 * @param argc argument counter
 * @param argv argument array
 * @param suite to execute
 * @return return code of execution
 */

extern int cunit_runAllTests(int argc, char** argv, const CUnitTestSuite *suite);
/**
 * Macro to wrap the main method, executing all tests.
 */
#define RUN_SUITE(Suite)                               \
int main(int argc, char** argv) {                      \
   assert(Suite != NULL);                              \
   return cunit_runAllTests(argc, argv, Suite);        \
}

/** by convention every type xxx has a xxxTest method   */
#define TEST_TYPE(T) (T##Test)
#define TEST_NAME(T) (#T "Test")

/** this xxxTest method has to be declarded as external */
#define DECLARE_TEST(T) extern int TEST_TYPE(T)(int,char**);
/** and must be added to the test suite                 */
#define ADD_TEST(T) {TEST_NAME(T), &TEST_TYPE(T) }
/**
 * Start of the test suite array.
 */
#define BEG_SUITE(S) static const struct cunit_testSuite (S)[] = {
/**
 * End of the test suite array.
 */
#define END_SUITE(S) ,{"eof suite",0} };

/**
 * Internal error counting function for assertXXX methods.
 * @param msg to report
 */
extern void cunit_report_error(const char* msg);
extern void cunit_report_failure(const char* msg);

#define CUNIT_ABS(x) ((x)<0 ? -(x):(x))
#define BUFSIZE 128

#define __assertEquals__(x,y,fmt) {                \
	if( (x)!=(y) ) {                               \
		char _test_buf[BUFSIZE];                   \
		sprintf(_test_buf,fmt,                     \
        __FILE__, __LINE__, (x), (y));             \
        cunit_report_error(_test_buf);             \
	}                                              \
}

/**
 * raise a failure situation with message.
 */
#define fail(msg)             {                    \
    char _test_buf[BUFSIZE];                       \
    sprintf(_test_buf,"FAIL %s:%d %s",             \
    __FILE__, __LINE__, msg);                      \
    cunit_report_failure(_test_buf);               \
}

/**
 * check that the given expression is true.
 */
#define assertTrue(expr)      {                    \
	if( !(expr) ) {                                \
		char _test_buf[BUFSIZE];                   \
		sprintf(_test_buf,"ERROR %s:%d %s",        \
        __FILE__, __LINE__, #expr);                \
        cunit_report_error(_test_buf);             \
	}                                              \
}
/**
 * check that the given expression is false.
 */
#define assertFalse(expr)      {                   \
	if(  (expr) ) {                                \
		char _test_buf[BUFSIZE];                   \
		sprintf(_test_buf,"ERROR %s:%d %s",        \
        __FILE__, __LINE__, #expr);                \
        cunit_report_error(_test_buf);             \
	}                                              \
}
/**
 * check that the given reference is not null.
 */
#define assertNotNull(ref)    {                    \
	assertFalse(ref==NULL);                        \
}
/**
 * check that the pointers x and y are equal.
 */
#define assertEqualsP(x,y) {                       \
	if( (x)!=(y) ) {                               \
		char _test_buf[BUFSIZE];                   \
		sprintf(_test_buf,"ERROR %s:%d %p != %p",  \
        __FILE__, __LINE__,(void*)(x),(void*)(y)); \
        cunit_report_error(_test_buf);             \
	}                                              \
}
/**
 * check that the floating point numbers x and y
 * are equal within the given precission delta.
 */
#define assertEqualsF(x,y,delta) {                 \
	if(CUNIT_ABS((x)-(y))>delta) {                 \
		char _test_buf[BUFSIZE];                   \
		sprintf(_test_buf,"ERROR %s:%d %g != %g",  \
        __FILE__, __LINE__, (x), (y));             \
        cunit_report_error(_test_buf);             \
	}                                              \
}
/**
 * check that the integer numbers x and y are equal.
 */
#define assertEqualsI(x,y) {                       \
	__assertEquals__(x,y,"ERROR %s:%d %d != %d");  \
}
/**
 * check that the chars x and y are equal.
 */
#define assertEqualsC(x,y) {                       \
	__assertEquals__(x,y,"ERROR %s:%d %c != %c");  \
}
/**
 * check that the strings x and y are equal.
 */
#define assertEqualsS(x,y) {                       \
	if(strcmp((x),(y))!=0) {                       \
		char _test_buf[BUFSIZE];                   \
		sprintf(_test_buf,"ERROR %s:%d %s != %s",  \
        __FILE__, __LINE__, (x), (y));             \
        cunit_report_error(_test_buf);             \
	}                                              \
}
#ifdef __cplusplus
}
#endif

#endif /* _CUNIT_H_ */
