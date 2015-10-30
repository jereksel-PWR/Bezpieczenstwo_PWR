#include <gtest/gtest.h>
#include <openssl/rand.h>
#include "libcrypt.h"

TEST(LibCrypt, BasicEncDec) {
    // ASSERT_EQ(6, 6);

    /* Message to be encrypted */
    unsigned char *plaintext =
            (unsigned char *) "The quick brown fox jumps over the lazy dog";

    unsigned char *key = new unsigned char[256 / 8];

    RAND_bytes(key, 256 / 8);

    unsigned char *iv = generate_iv();

    unsigned char *encrypted = new unsigned char[strlen((const char *) plaintext) * 2];

    encrypt(plaintext, (int) strlen((const char *) plaintext), key, iv, encrypted);

    ASSERT_NE(plaintext, encrypted);

}

int main(int argc, char **argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}