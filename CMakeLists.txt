cmake_minimum_required(VERSION 2.8.7)
project(Bezpieczestwo)

enable_testing()
find_package(GTest REQUIRED)
find_package(OpenSSL REQUIRED)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++0x")

set(SOURCE_FILES main.cpp)
add_executable(Bezpieczestwo ${SOURCE_FILES})
target_link_libraries(Bezpieczestwo ${OPENSSL_LIBRARIES})

add_executable(tests tests.cpp)
target_link_libraries(tests ${GTEST_BOTH_LIBRARIES})
add_test(AllTestsInFoo tests)