/*
 * CUnit.c
 *
 *  @since:   23.11.2014
 *  @author:  nwulff
 *  @version: $Id: CUnit.c,v 1.7 2017/12/05 21:40:22 nwulff Exp $
 */

#ifdef __cplusplus
extern "C" {
#endif

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <setjmp.h>
#include <signal.h>
#include <string.h>
#include <stdarg.h>  // varargs
#include "CUnit.h"


#define NAME_OF_TEST(T) (T).name
#define TEST_METHOD(T)  (T).test

static int signalRegistered = 0;
static jmp_buf env;

typedef struct test_run {
	int runs;
	int errors;
	int fatal;
	int failure;
} TestRun;

static TestRun testRun;

static void output(const char *fmt, ...)  {
    #define MAXBUF 512
	static char buffer[MAXBUF];
    va_list vargs;
    va_start(vargs, fmt);
    vsprintf(buffer, fmt, vargs);
    va_end(vargs);
    fprintf(stdout,"%s\n",buffer);
    fflush(stdout);
}

void cunit_report_error(const char* msg) {
    output("%s", msg);
    testRun.errors++;
    longjmp(env, 1);
}
void cunit_report_failure(const char* msg) {
    output("%s", msg);
    testRun.failure++;
    longjmp(env, 1);
}
void cunit_report_fatal(const char* msg) {
    output("%s", msg);
    testRun.fatal++;
    longjmp(env, 1);
}

static void clearResults() {
	testRun.runs = 0;
	testRun.errors = 0;
	testRun.fatal = 0;
	testRun.failure = 0;
}
static void cunit_catch_signal_error(int signo) {
    static char msg[256];
    sprintf(msg, "FATAL sig error: %d => %s", signo,  strsignal(signo));
    cunit_report_fatal(msg);
}
static int setupRunner() {
    if (!signalRegistered) {
        int signals[] = { SIGILL, SIGFPE, SIGSEGV };
        int signo, k, numSignals = sizeof(signals) / sizeof(int);
        for (k = 0; k < numSignals ; k++) {
            signo = signals[k];
            if (signal(signo, cunit_catch_signal_error) == SIG_ERR) {
                output("couldn't register signal handler %d %s",signo,strsignal(signo));
                return EXIT_FAILURE;
            }
        }
        signalRegistered = 1;
    }
    return EXIT_SUCCESS;
}

static int cunit_consoleRunner(int argc, char** argv, const CUnitTestSuite *suite) {
    CUnitTestSuite test;
    setupRunner();
    clearResults();
    output("\nSTARTING  ALL TESTS \n");
    test = suite[testRun.runs];
    while (TEST_METHOD(test)) {
        output("BEG Test >>%s<<", NAME_OF_TEST(test));
        if (!setjmp(env)) {
            TEST_METHOD(test)(argc, argv);
        }
        output("END Test >>%s<<", NAME_OF_TEST(test));
        testRun.runs++;
        test = suite[testRun.runs];
    }
    output("\nTEST RESULTS");
    output("runs: %d errors: %d failures: %d fatal: %d \n", testRun.runs, testRun.errors, testRun.failure, testRun.fatal);
    return EXIT_SUCCESS;
}

int cunit_runAllTests(int argc, char** argv, const CUnitTestSuite *suite) {
    return cunit_consoleRunner(argc,argv,suite);
}

#ifdef __cplusplus
}
#endif

