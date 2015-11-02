#ifndef PROJECT_KEYSTORE_H
#define PROJECT_KEYSTORE_H

#include <string>
#include <map>

class keystore {

private:
    //         nazwa         zaszyfrowany klucz  iv
    std::map<std::string, std::pair<std::string*, std::string*>*> keysMap;

public:
    //Klucz w postaci jawnej + hasło jakich chcemy go zaszyfrować
    void addKey(std::string name, std::string key, std::string password);
    std::string* getKey(std::string name, std::string password);
    void saveToFile(char* file);
};


#endif
