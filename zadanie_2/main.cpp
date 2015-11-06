#include <ao/ao.h>
#include <mpg123.h>
#include <iostream>
#include <utils.hpp>
#include <fstream>
#include <base64.hpp>
#include <libcrypt.hpp>
#include <vector>
#include <boost/algorithm/string.hpp>
#include <keystore.hpp>
#include <OpensslException.h>

#define BITS 8

//Travis hax
#if MPG123_API_VERSION < 40
int mpg123_encsize(int encoding) {
    return 0;
}
#endif

//Uber tajny klucz
std::string key = std::string("supertajnykluczdoodtwarzaczamp3_");
std::string plik = std::string(".mp3config");

void usage() {
    std::cout << "zadanie2 init <keystore> <id klucza>" << std::endl;
}

void usageWithConfig() {
    std::cout << "zadanie2 <zaszyfrowany plik>" << std::endl;
}

//Kanged from https://hzqtc.github.io/2012/05/play-mp3-with-libmpg123-and-libao.html
int main(int argc, char *argv[]) {



    if (argc > 1 && std::string(argv[1]) == "init") {

        auto sciezka = std::string(argv[2]);
        auto id_klucza = std::string(argv[3]);

        //Sprawdzamy, czy keystore, klucz i hasło się zgadzają

        FILE *file = fopen(sciezka.data(), "rb");

        if (file == nullptr) {
            std::cout << "Ścieżka do keystora jest niepoprawna" << std::endl;
            return -1;
        }

        keystore* keystore1 = keystore::loadFromFile((char *) sciezka.data());

        if (!keystore1->checkKeyExistence(id_klucza)) {
            std::cout << "Nazwa klucza jest niepoprawna" << std::endl;
            return -1;
        }


        std::cout << "Podaj hasło do klucza" << std::endl;
        auto haslo = getPasswordSecurely();


        try {
            keystore1->getKey(id_klucza, *haslo);
        } catch (OpensslException &e) {
            std::cout << "Błędne hasło" << std::endl;
            return -1;
        }


        std::cout << "Podaj PIN" << std::endl;
        auto pin = getPasswordSecurely();

        auto toWrite = std::string();



        toWrite.append(base64_encode(sciezka));
        toWrite.append("-");
        toWrite.append(base64_encode(id_klucza));
        toWrite.append("-");
        toWrite.append(base64_encode(*haslo));
        toWrite.append("-");
        toWrite.append(base64_encode(*pin));


        std::ofstream fileStream;
        fileStream.open(plik, std::ios::trunc | std::ios::binary);
        fileStream << *encrypt(&toWrite, &key, &key);
        fileStream.close();


        return 0;
    }


    FILE *file = fopen(plik.data(), "rb");

    if (file == nullptr) {
        usage();
        return -1;
    }

    fclose(file);

    if (argc == 1) {
        usageWithConfig();
        return -1;
    }

    auto configFile = fileToString((char *) plik.data());

    if (configFile->empty()) {
        std::cout << "No config file" << std::endl;
        return -1;
    }

    auto configFileDecrypted = decrypt(configFile, &key, &key);

    std::vector<std::string> splitVec;
    boost::split(splitVec, *configFileDecrypted, boost::is_any_of("-"),
                 boost::token_compress_on);

    auto sciezka = base64_decode(splitVec.data()[0]);
    auto id_klucza = base64_decode(splitVec.data()[1]);
    auto haslo = base64_decode(splitVec.data()[2]);
    std::string pin = base64_decode(splitVec.data()[3]);

    std::cout << "Podaj PIN" << std::endl;

    std::string* pinGet = getPasswordSecurely();

    if (pin != *pinGet) {
        std::cout << "Nieprawidłowy PIN" << std::endl;
        return -1;
    }


    auto keystore = keystore::loadFromFile((char *) sciezka.data());

    auto klucz = keystore->getKey(id_klucza, haslo);

    std::string *encryptedMusic = fileToString(argv[1]);

    std::string iv_encode = encryptedMusic->substr(0, encryptedMusic->find("-"));
    std::string encrypted_encode = encryptedMusic->erase(0, iv_encode.size() + 1);

    std::string iv_decode = base64_decode(iv_encode);
    std::string encrypted_decode = base64_decode(encrypted_encode);

    std::string *decryptedMusic = decrypt(&encrypted_decode, klucz, &iv_decode);

    mpg123_handle *mh;
    unsigned char *buffer;
    size_t buffer_size;
    size_t done;
    int err;

    int driver;
    ao_device *dev;

    ao_sample_format format;
    int channels, encoding;
    long rate;

    /* initializations */
    ao_initialize();
    driver = ao_default_driver_id();
    mpg123_init();
    mh = mpg123_new(NULL, &err);
    buffer_size = mpg123_outblock(mh);
    buffer = (unsigned char *) malloc(buffer_size * sizeof(unsigned char));

    mpg123_open_feed(mh);


    mpg123_feed(mh, (const unsigned char *) decryptedMusic->data(), decryptedMusic->size());

    mpg123_getformat(mh, &rate, &channels, &encoding);

    /* set the output format and open the output device */
    format.bits = mpg123_encsize(encoding) * BITS;
    format.rate = rate;
    format.channels = channels;
    format.byte_format = AO_FMT_NATIVE;
    format.matrix = 0;
    dev = ao_open_live(driver, &format, NULL);

    /* decode and play */
    while (mpg123_read(mh, buffer, buffer_size, &done) == MPG123_OK)
        ao_play(dev, (char *) buffer, (uint_32) done);

    /* clean up */
    free(buffer);
    ao_close(dev);
    mpg123_close(mh);
    mpg123_delete(mh);
    mpg123_exit();
    ao_shutdown();

    return 0;
}
