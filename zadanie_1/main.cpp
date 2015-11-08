#include <libcrypt.hpp>
#include <base64.hpp>
#include <fstream>
#include <sys/termios.h>
#include <sys/unistd.h>
#include <iostream>
#include <utils.hpp>
#include <keystore.hpp>

void printHelp() {
    std::cout
    << "Usage:" << std::endl
    << "zadanie1 <encrypt/decrypt> <keystore> <id klucza> <plik> <plik wynikowy>" << std::endl;
}

int main(int argc, char **argv) {

    if (argc != 6) {
        printHelp();
        return -1;
    }

    if (std::string(argv[1]) == "encrypt") {

        /* A 128 bit IV */
        auto *iv = generate_iv(128);

        printf("Proszę podać hasło\n");

        auto *password = getPasswordSecurely();

        std::string *contents = fileToString(argv[4]);

        auto keystore = keystore::loadFromFile(argv[2]);

        auto temp = new std::string(argv[3]);

        std::string *key = keystore->getKey(*temp, *password);

        std::string *encrypted = encrypt(contents, key, iv);

        std::ofstream myfile(argv[5], std::ios::trunc | std::ios::binary);
        myfile << base64_encode(*iv);
        myfile << "-";
        myfile << base64_encode(*encrypted);
        myfile.close();


    } else if (std::string(argv[1]) == "decrypt") {

        printf("Proszę podać hasło\n");

        std::string *password = getPasswordSecurely();

        std::string *contents = fileToString(argv[4]);

        std::string iv_encode = contents->substr(0, contents->find("-"));
        std::string encrypted_encode = contents->substr(iv_encode.size() + 1, contents->size());

        std::string iv_decode = base64_decode(iv_encode);
        std::string encrypted_decode = base64_decode(encrypted_encode);

        auto keystore = keystore::loadFromFile(argv[2]);

        std::string *key = keystore->getKey(std::string(argv[3]), *password);

        std::string *decrypted = decrypt(&encrypted_decode, key, &iv_decode);

        std::ofstream myfile(argv[5], std::ios::trunc | std::ios::binary);
        myfile << *decrypted;
        myfile.close();

    }

    return 0;
}
