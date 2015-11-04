#include <gtest/gtest.h>
#include <base64.hpp>
#include <libcrypt.hpp>
#include <keystore.hpp>
#include <OpensslException.h>

TEST(Libcrypt, BasicEncDec) {
    /* Message to be encrypted */
    std::string plaintext = "The quick brown fox jumps over the lazy dog";

    //256 bit key
    auto *key = generate_iv(256);

    //128 bit iv
    auto *iv = generate_iv(128);

    auto encrypted = encrypt(&plaintext, key, iv);

    EXPECT_FALSE(plaintext == *encrypted);

    auto decrypted = decrypt(encrypted, key, iv);

    EXPECT_TRUE(plaintext == *decrypted);

    delete encrypted;
    delete decrypted;

}

TEST(Libcrypt, WrongData) {
    EXPECT_THROW(decrypt(new std::string("AAA"), new std::string("A"), new std::string("A")), OpensslException);
}


TEST(Libcrypt, SmallKey) {
    /* Message to be encrypted */
    std::string plaintext = "The quick brown fox jumps over the lazy dog";

    //256 bit key
    auto *key = new std::string("Key");

    //128 bit iv
    auto *iv = generate_iv(128);

    auto encrypted = encrypt(&plaintext, key, iv);

    EXPECT_FALSE(plaintext == *encrypted);

    auto decrypted = decrypt(encrypted, key, iv);

    EXPECT_TRUE(plaintext == *decrypted);

}

TEST(Libcrypt, SmallIV) {
    /* Message to be encrypted */
    std::string plaintext = "The quick brown fox jumps over the lazy dog";

    //256 bit key
    auto *key = new std::string("Key");

    //128 bit iv
    auto *iv = new std::string("IV");

    auto encrypted = encrypt(&plaintext, key, iv);

    EXPECT_FALSE(plaintext == *encrypted);

    auto decrypted = decrypt(encrypted, key, iv);

    EXPECT_TRUE(plaintext == *decrypted);
}

TEST(Libcrypt, IVWithZero) {
    /* Message to be encrypted */
    std::string plaintext = "The quick brown fox jumps over the lazy dog";

    //256 bit key
    auto *key = new std::string("Key");

    //128 bit iv
    auto *iv = new std::string("01234567890\00023456");

    auto encrypted = encrypt(&plaintext, key, iv);

    EXPECT_FALSE(plaintext == *encrypted);

    auto decrypted = decrypt(encrypted, key, iv);

    EXPECT_TRUE(plaintext == *decrypted);
}


TEST(Base64, TestFromWebsite) {
    const std::string s = "ADP GmbH\nAnalyse Design & Programmierung\nGesellschaft mit beschränkter Haftung";

    std::string encoded = base64_encode(s);
    std::string decoded = base64_decode(encoded);

    EXPECT_FALSE(s == encoded);
    EXPECT_TRUE(decoded == s);
}

TEST(KeyStore, BasicTest) {
    keystore *myKeystore = new keystore();
    myKeystore->addKey("Key1", "MyVeryImportantKey", "SecretPassword");

    EXPECT_EQ(0, myKeystore->getKey("Key1", "SecretPassword")->compare(std::string("MyVeryImportantKey")));
}

TEST(KeyStore, FromString) {
    std::string* string = new std::string("key_1-IV_1-EncryptedKey1-key_2-IV_2-EncryptedKey2");

    keystore *myKeystore = keystore::loadFromString(string);

    EXPECT_TRUE("IV_1" == *myKeystore->getPair("key_1")->second);
    EXPECT_TRUE("EncryptedKey1" == *myKeystore->getPair("key_1")->first);

    EXPECT_TRUE("IV_2" == *myKeystore->getPair("key_2")->second);
    EXPECT_TRUE("EncryptedKey2" == *myKeystore->getPair("key_2")->first);
}

int main(int argc, char **argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}