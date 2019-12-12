cmake_minimum_required(VERSION 3.5)
project(WulffRunsBeta)
set(CMAKE_CXX_STANDARD 14)

find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR}/../c-lib)

set(SOURCE_FILES_LIB JavaFunction.h Function.h 
Differentiator.cpp Differentiator.h de_lab4inf_wrb_Differentiator.h 
Integrator.cpp Integrator.h de_lab4inf_wrb_Integrator.h)

set(SOURCE_FILES_EXE Tests.cpp Tests.h CUnit.c CUnit.h Function.h 
Differentiator.cpp Differentiator.h Integrator.cpp Integrator.h)

add_library(WRB4 SHARED ${SOURCE_FILES_LIB})
add_executable(WRB4-Test ${SOURCE_FILES_EXE})

add_custom_command(TARGET WRB4 POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy $<TARGET_FILE:WRB4> ${PROJECT_BINARY_DIR}/../../lib/$<TARGET_FILE_NAME:WRB4>
)
add_custom_command(TARGET WRB4-Test POST_BUILD
    COMMAND ${CMAKE_COMMAND} -E copy $<TARGET_FILE:WRB4-Test> ${PROJECT_BINARY_DIR}/../../exe/$<TARGET_FILE_NAME:WRB4-Test>
)