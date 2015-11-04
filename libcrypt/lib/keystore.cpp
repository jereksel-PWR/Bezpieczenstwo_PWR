#include <keystore.hpp>
#include <libcrypt.hpp>
#include <base64.hpp>
#include <fstream>
#include <malloc.h>
#include <boost/algorithm/string.hpp>

//Works nice with \0 in files
std::string *fileToString(char *file_location);

void keystore::addKey(std::string name, std::string key, std::string password) {

    std::string *random_iv = new std::string("01234567890123456");

    std::string *zaszyfrowany_klucz = encrypt(&key, &password, random_iv);

    keysMap[name] = new std::pair<std::string *, std::string *>(zaszyfrowany_klucz, random_iv);

}

std::string *keystore::getKey(std::string name, std::string password) {

    std::pair<std::string *, std::string *> *myPair = keysMap[name];

    if (myPair == nullptr) {
        return nullptr;
    }

    std::string *random_iv = new std::string("01234567890123456");

    return decrypt(myPair->first, &password, random_iv);
}


//<NAZWA>$<IV>$<KLUCZ>
void keystore::saveToFile(char *file) {

    std::string *data = new std::string;

    typedef std::map<std::string, std::pair<std::string *, std::string *> *>::iterator it_type;
    for (it_type iterator = keysMap.begin(); iterator != keysMap.end(); iterator++) {

        data->append(base64_encode(iterator->first));
        data->append("-");
        data->append(base64_encode(*iterator->second->second));
        data->append("-");
        data->append(base64_encode(*iterator->second->first));
        data->append("-");

    }

    data->erase(data->size()-1, 1);;

    std::ofstream fileStream;
    fileStream.open(file, std::ios::trunc | std::ios::binary);
    fileStream << *data;
    fileStream.close();

}

keystore *keystore::loadFromFile(char *file) {
    return keystore::loadFromString(fileToString(file));
}

keystore *keystore::loadFromString(std::string *string) {

    std::vector<std::string> splitVec; // #2: Search for tokens
    boost::split(splitVec, *string, boost::is_any_of("-"),
                 boost::token_compress_on); // splitVec == { "hello abc","ABC","aBc goodbye" }


    int i = 0;

    keyStoreMap map;

    while (i < splitVec.size()) {
        map[splitVec[i]] = new std::pair<std::string *, std::string *>(new std::string(splitVec[i + 2]),
                                                                       new std::string(splitVec[i + 1]));
        i = i + 3;
    }

    return new keystore(map);
}

//Works nice with \0 in files
std::string *fileToString(char *file_location) {

    char *buffer = NULL;
    long length;

    FILE *file = fopen(file_location, "rb");

    fseek(file, 0, SEEK_END);
    length = ftell(file);
    fseek(file, 0, SEEK_SET);
    buffer = (char *) malloc(length * sizeof(char));
    if (buffer)
        fread(buffer, 1, length, file);
    fclose(file);

    return new std::string(buffer, length);

}

std::pair<std::string *, std::string *> *keystore::getPair(std::string name) {
    return keysMap[name];
}
