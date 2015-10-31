#ifndef BEZPIECZESTWO_LIBCRYPT_H
#define BEZPIECZESTWO_LIBCRYPT_H

#include <string>

unsigned char *generate_iv(unsigned int bits);

std::string *encrypt(std::string* plaintext_string, unsigned char *key,
                     unsigned char *iv);

std::string *decrypt(std::string* ciphertext_string, unsigned char *key,
                     unsigned char *iv);


#endif