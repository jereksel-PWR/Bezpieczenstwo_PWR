#include <gtest/gtest.h>
#include <openssl/rand.h>
#include "libcrypt.hpp"

int sizeOfArray(unsigned char *array);

TEST(LibCrypt, BasicEncDec) {
    // ASSERT_EQ(6, 6);

    /* Message to be encrypted */
    std::string plaintext = "The quick brown fox jumps over the lazy dog";

    unsigned char *key = new unsigned char[256 / 8];

    RAND_bytes(key, 256 / 8);

    unsigned char *iv = generate_iv();

    std::string* encrypted = encrypt(&plaintext, key, iv);

    // ASSERT_NE(plaintext, encrypted);

    EXPECT_NE(0, encrypted->compare(plaintext));

    std::string* decrypted = decrypt(encrypted, key, iv);

    EXPECT_EQ(0, plaintext.compare(*decrypted));

}

int sizeOfArray(unsigned char *array) {

    int i = 0;

    while (array[i] != '\0') {
        i++;
    }

    return i;

}

int main(int argc, char **argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}