project(libcrypt_lib)

find_package(OpenSSL REQUIRED)

add_library(LibCrypt libcrypt.cpp ${OPENSSL_LIBRARIES})
target_link_libraries(LibCrypt ${OPENSSL_LIBRARIES})

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99 -O2")

target_include_directories(LibCrypt PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})
