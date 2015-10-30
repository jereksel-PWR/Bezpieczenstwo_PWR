#ifndef BEZPIECZESTWO_LIBCRYPT_H
#define BEZPIECZESTWO_LIBCRYPT_H

unsigned char *generate_iv();

//Ciphertext size | generated iv
int encrypt(unsigned char *plaintext, int plaintext_len, unsigned char *key,
            unsigned char *iv, unsigned char *ciphertext);

int decrypt(unsigned char *ciphertext, int ciphertext_len, unsigned char *key,
            unsigned char *iv, unsigned char *plaintext);


#endif
