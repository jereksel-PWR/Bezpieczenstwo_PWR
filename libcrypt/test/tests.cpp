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

    auto *key = new std::string("Key");

    auto *iv = generate_iv(128);

    auto encrypted = encrypt(&plaintext, key, iv);

    EXPECT_FALSE(plaintext == *encrypted);

    auto decrypted = decrypt(encrypted, key, iv);

    EXPECT_TRUE(plaintext == *decrypted);

    EXPECT_TRUE(*key == std::string("Key"));

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

TEST(Libcrypt, DuzyPlik) {

    std::string plaintext  = "test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test ";

    auto *key = generate_iv(256);

    auto *iv = generate_iv(128);

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

TEST(Libcrypt, WithBase64) {

    std::string plaintext  = "a test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test test ";

    auto *key = generate_iv(256);

    auto *iv = generate_iv(128);

    auto encrypted = encrypt(&plaintext, key, iv);

    std::string encoded = base64_encode(*encrypted);
    std::string decoded = base64_decode(encoded);

    auto decrypted = decrypt(&decoded, key, iv);

    EXPECT_TRUE(plaintext == *decrypted);

}

TEST(Base64, TestFromWebsite) {
    const std::string s = "ADP GmbH\nAnalyse Design & Programmierung\nGesellschaft mit beschr�nkter Haftung";

    std::string encoded = base64_encode(s);
    std::string decoded = base64_decode(encoded);

    EXPECT_FALSE(s == encoded);
    EXPECT_TRUE(decoded == s);
}

TEST(Base64, SpecialCharacters) {
    const std::string s = "\1\2\3\4\0\5\6\7";

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
/*
TEST(KeyStore, FromString) {
    std::string* string = new std::string("key_1-IV_1-EncryptedKey1-key_2-IV_2-EncryptedKey2");

    keystore *myKeystore = keystore::loadFromString(string);

    EXPECT_TRUE("IV_1" == *myKeystore->directGetPair("key_1")->second);
    EXPECT_TRUE("EncryptedKey1" == *myKeystore->directGetPair("key_1")->first);

    EXPECT_TRUE("IV_2" == *myKeystore->directGetPair("key_2")->second);
    EXPECT_TRUE("EncryptedKey2" == *myKeystore->directGetPair("key_2")->first);
}
*/
TEST(KeyStore, EmptyString) {
    std::string* string = new std::string("");

    EXPECT_NO_FATAL_FAILURE(keystore::loadFromString(string));
}

TEST(KeyStore, NamesTest) {
    keystore *myKeystore = new keystore();

    myKeystore->addGeneratedKey("1", "1");
    myKeystore->addGeneratedKey("2", "2");

    auto namesVector = (myKeystore->getNames());

    ASSERT_EQ(2, namesVector->size());

    ASSERT_TRUE(*namesVector->data()[0] == *new std::string("1") || *namesVector->data()[0] == *new std::string("2"));
    ASSERT_TRUE(*namesVector->data()[1] == *new std::string("1") || *namesVector->data()[1] == *new std::string("2"));
    ASSERT_TRUE(*namesVector->data()[0] != *namesVector->data()[1]);

}

int main(int argc, char **argv) {
    testing::InitGoogleTest(&argc, argv);
    return RUN_ALL_TESTS();
}