#ifndef BEZPIECZESTWO_LIBCRYPT_H
#define BEZPIECZESTWO_LIBCRYPT_H

#include <string>

std::string *generate_iv(unsigned int bits);

std::string *encrypt(std::string* plaintext_string, std::string *key,
                     std::string *iv);

std::string *decrypt(std::string* ciphertext_string, std::string *key,
                     std::string *iv);


#endif
