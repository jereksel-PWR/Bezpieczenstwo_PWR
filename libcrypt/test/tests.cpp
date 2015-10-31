#include <gtest/gtest.h>
#include <openssl/rand.h>
#include <base64.hpp>
#include "libcrypt.hpp"

TEST(Libcrypt, BasicEncDec) {
    /* Message to be encrypted */
    std::string plaintext = "The quick brown fox jumps over the lazy dog";

    //256 bit key
    auto *key = new unsigned char[256 / 8];

    RAND_bytes(key, 256 / 8);

    //128 bit iv
    auto *iv = generate_iv(128);

    auto encrypted = encrypt(&plaintext, key, iv);

    EXPECT_FALSE(plaintext == *encrypted);

    auto decrypted = decrypt(encrypted, key, iv);

    EXPECT_TRUE(plaintext == *decrypted);

    delete encrypted;
    delete decrypted;

}

TEST(Base64, TestFromWebsite) {

    const std::string s = "ADP GmbH\nAnalyse Design & Programmierung\nGesellschaft mit beschränkter Haftung" ;

    std::string encoded = base64_encode(s);
    std::string decoded = base64_decode(encoded);

    EXPECT_FALSE(s == encoded);
    EXPECT_TRUE(decoded == s);

}


int main(int argc, char **argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}