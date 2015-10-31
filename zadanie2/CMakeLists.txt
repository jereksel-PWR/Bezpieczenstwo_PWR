cmake_minimum_required(VERSION 2.8.12)
project(zadanie2)

find_package(LibAO REQUIRED)
find_package(MPG123 REQUIRED)
find_package(Threads REQUIRED)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++0x")

add_executable(zadanie2 main.cpp)
target_link_libraries(zadanie2 LINK_PUBLIC LibCrypt ${LIBAO_LIBRARIES} ${MPG123_LIBRARIES} ${CMAKE_THREAD_LIBS_INIT})
set_target_properties(zadanie2 PROPERTIES RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR})
