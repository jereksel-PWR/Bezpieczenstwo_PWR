#include <libcrypt.hpp>
#include <base64.hpp>
#include <fstream>
#include <sys/termios.h>
#include <sys/unistd.h>
#include <iostream>
#include <malloc.h>
#include <utils.hpp>

int main(int argc, char **argv) {

    if (argc < 2) {
        return 0;
    }

    if (std::string(argv[1]) == "encrypt") {

        /* A 128 bit IV */
        auto *iv = generate_iv(128);

        printf("Proszę podać hasło\n");

        termios oldt;
        tcgetattr(STDIN_FILENO, &oldt);
        termios newt = oldt;
        newt.c_lflag &= ~ECHO;
        tcsetattr(STDIN_FILENO, TCSANOW, &newt);

        std::string key;
        getline(std::cin, key);

        tcsetattr(STDIN_FILENO, TCSANOW, &oldt);

        std::string *contents = fileToString(argv[2]);

        std::string *encrypted = encrypt(contents, &key, iv);

        std::ofstream myfile;
        myfile.open(argv[3], std::ios::trunc | std::ios::binary);
        myfile << base64_encode(std::string((char *) iv));
        myfile << "-";
        myfile << base64_encode(*encrypted);
        myfile.close();


    } else if (std::string(argv[1]) == "decrypt") {

        printf("Proszę podać hasło\n");

        termios oldt;
        tcgetattr(STDIN_FILENO, &oldt);
        termios newt = oldt;
        newt.c_lflag &= ~ECHO;
        tcsetattr(STDIN_FILENO, TCSANOW, &newt);

        std::string key;
        getline(std::cin, key);

        tcsetattr(STDIN_FILENO, TCSANOW, &oldt);

        std::string *contents = fileToString(argv[2]);

        std::string iv_encode = contents->substr(0, contents->find("-"));
        std::string encrypted_encode = contents->erase(0, iv_encode.size() + 1);

        std::string iv_decode = base64_decode(iv_encode);
        std::string encrypted_decode = base64_decode(encrypted_encode);

        std::string *decrypted = decrypt(&encrypted_decode, &key, &iv_decode);

        //std::cout << *decrypted << std::endl;

        std::ofstream myfile;
        myfile.open(argv[3], std::ios::trunc | std::ios::binary);
        myfile << *decrypted;
        myfile.close();

    }

    return 0;
}
