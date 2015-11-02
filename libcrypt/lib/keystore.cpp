#include "keystore.hpp"
#include "libcrypt.hpp"

void keystore::addKey(std::string name, std::string key, std::string password) {

    std::string *random_iv = new std::string("01234567890123456");

    std::string *zaszyfrowany_klucz = encrypt(&key, &password, random_iv);

    keysMap[name] = new std::pair<std::string *, std::string *>(zaszyfrowany_klucz, random_iv);

}

std::string *keystore::getKey(std::string name, std::string password) {

    std::pair<std::string *, std::string*> *myPair = keysMap[name];

    if (myPair == nullptr) {
        return nullptr;
    }

    std::string *random_iv = new std::string("01234567890123456");

    return decrypt(myPair->first, &password, random_iv);
}
